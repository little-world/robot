ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "robot2"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.8.5",
  "com.typesafe.akka" %% "akka-testkit" % "2.8.5",
  "org.scala-lang" % "scala-swing" % "2.10.3",
  "org.scalatest" % "scalatest_2.11" % "3.2.17" % "test",
  "org.mockito" % "mockito-all" % "2.0+"
)