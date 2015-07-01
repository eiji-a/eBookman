name := "eBookman"
jarName in assembly := "eBookman.jar"

lazy val commonSettings = Seq(
  version := "0.1",
  organization := "com.example",
  scalaVersion := "2.10.3"
)

lazy val app = (project in file("app")).
  settings(commonSettings: _*)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.1",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.1",
  "javax.transaction" % "jta" % "1.1"
)

testOptions in ThisBuild <+= (target in Test) map {
  t => Tests.Argument("-o", "-u", t + "/test-reports")
}

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"



