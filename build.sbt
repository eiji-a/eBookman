name := "eBookman"

version := "0.1"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.7.2" % "test",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.1",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.1"
)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"


