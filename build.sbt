import _root_.sbt.Keys._

lazy val root = (project in file(".")).enablePlugins(PlayScala)
name := "akka cluster api test"
scalaVersion := "2.11.8"

val akkaVersion = "2.4.4"

lazy val thirdPartyDependencies: Seq[ModuleID] = Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  play.sbt.PlayImport.ws withSources())

libraryDependencies ++= thirdPartyDependencies