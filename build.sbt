
organization := "com.timperrett"

name := "mesos-operationalised-framework"

libraryDependencies ++= Seq(
  "org.apache.mesos"            % "mesos"             % "0.20.1",
  "com.typesafe.scala-logging" %% "scala-logging"     % "3.1.0",
  "ch.qos.logback"              % "logback-classic"   % "1.1.2",
  "org.scalaz"                 %% "scalaz-core"       % "7.1.1",
  "org.apache.curator"          % "curator-framework" % "2.7.1",
  "org.apache.curator"          % "curator-recipes"   % "2.7.1"
)

scalaVersion := "2.11.4"

scalacOptions += "-deprecation"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

mainClass in assembly := Some("example.Framework")

assemblyJarName in assembly := "example-framework.jar"
