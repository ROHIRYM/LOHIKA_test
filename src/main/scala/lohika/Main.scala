package lohika

import java.io.File
import com.github.tototoshi.csv._
import scala.collection.immutable.ListMap

object Main extends App {
    val areArgsValid = args.length == 2 && args(0) == "-d"

    if (!areArgsValid) {
        println("Invalid arguments")
    } else {
        val listOfCsvFiles = getListOfCsvFiles(args(1))
        if (listOfCsvFiles.isEmpty) {
            println("No .csv files found")
        } else {
            processAllCsvFiles(listOfCsvFiles)
        }
    }

    def getListOfCsvFiles(dir: String): List[File] = {
        val d = new File(dir)
        if (d.exists && d.isDirectory) {
            d.listFiles.filter(_.isFile).toList.filter(_.getName.endsWith(".csv"))
        } else {
            List[File]()
        }
    }

    def processAllCsvFiles(files: List[File]) {
        val filesCrimes = files.map { f => 
            println("Processing " + f.getName + "...")

            val reader = CSVReader.open(f)
            val currentFileCrimes = reader.allWithHeaders()
            reader.close()

            currentFileCrimes.filter(oneRow => oneRow.get("Crime ID") != null && !oneRow.get("Crime ID").get.isEmpty)
        }

        val crimesGroupedByCoordinates = filesCrimes.flatten
            .groupBy(el => "(" + el.get("Longitude").get + "," + el.get("Latitude").get + ")")

        val numbersOfCrimesByCoordinates = crimesGroupedByCoordinates.map{ case (k,v) => (k,v.length) }

        val maxNumbers = find5MaxInts(numbersOfCrimesByCoordinates.values.toList)
        val maxNumbersKeys = get5KeysForValues(numbersOfCrimesByCoordinates, maxNumbers)

        maxNumbersKeys.foreach{ k =>
            println("------------------------------------------------")
            println(k + ": " + numbersOfCrimesByCoordinates.get(k).get)
            crimesGroupedByCoordinates.get(k).get.foreach { m =>
                println(m.get("Crime ID").get)
            }
        }
        println("------------------------------------------------")
    }

    def find5MaxInts(ints: List[Int]): List[Int] = {
        def iterate(list: List[Int], n: Int): List[Int] = {
            if (list.isEmpty) {
                List()
            } else {
                val max = list.max
                if (n == 0) {
                    List(max)
                } else {
                    val maxIndex = list.indexOf(max)
                    val splitedListByMaxIndex = list.splitAt(maxIndex)
                    val dropByMaxIndex = if (splitedListByMaxIndex._2.isEmpty) List() else splitedListByMaxIndex._2.tail
                    max :: iterate(splitedListByMaxIndex._1, n - 1) ::: iterate(dropByMaxIndex, n - 1)
                }
            }
        }

        iterate(ints, 5).sortWith((x, y) => x > y).take(5)
    }

    def get5KeysForValues(numbersOfCrimesByCoordinates: Map[String, Int], nums: List[Int]): List[String] = {
        val n = 5
        def iterate(nums: List[Int], accum: List[String]): List[String] = {
            if (nums.isEmpty || accum.length == n) {
                accum
            } else {
                iterate(nums.tail, accum ::: numbersOfCrimesByCoordinates.filter(el => el._2 == nums.head).keySet.toList)
            }
        }
        
        iterate(nums, List()).take(n)
    }
}
