ThisBuild / scalaVersion := "2.12.7"
ThisBuild / organization := "com.lohika"

lazy val testTask = (project in file("."))
  .settings(
    name := "Test task",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  )