
organization := "com.timperrett"

name := "mesos-operationalised-framework"

libraryDependencies ++= Seq(
  "org.apache.mesos"            % "mesos"           % "0.20.1",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.1.0",
  "ch.qos.logback"              % "logback-classic" % "1.1.2"
)

scalaVersion := "2.11.4"

scalacOptions += "-deprecation"

mainClass in assembly := Some("example.Framework")

assemblyJarName in assembly := "example-framework.jar"
