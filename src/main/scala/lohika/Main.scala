package lohika

import java.io.File

object Main extends App {
    val areArgsValid = args.length == 2 && args(0) == "-d"

    if (!areArgsValid) {
        println("Invalid arguments")
    } else {
        val listOfCsvFiles = getListOfCsvFiles(args(1))
        if (listOfCsvFiles.isEmpty) {
            println("=== No .csv files found ===")
        } else {
            println("=== Found .csv files ===")
            listOfCsvFiles.foreach{(f: File) => println(f.getName)}
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
}
