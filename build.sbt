
organization := "com.timperrett"

name := "mesos-operationalised-framework"

libraryDependencies ++= Seq(
  "org.apache.mesos"            % "mesos"         % "0.20.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
)

scalaVersion := "2.11.4"
