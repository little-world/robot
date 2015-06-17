name := "robot2"

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

scalacOptions ++= Seq("-feature")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9", 
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9", 
  "org.scala-lang" % "scala-swing" % "2.11+",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.mockito" % "mockito-all" % "2.0+"
)

