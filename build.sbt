import _root_.sbt.Keys._

lazy val root = (project in file(".")).enablePlugins(PlayScala)
name := "akka cluster api test"
scalaVersion := "2.11.8"

val akkaVersion = "2.4.4"

lazy val thirdPartyDependencies: Seq[ModuleID] = Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "org.scaldi" %% "scaldi-akka" % "0.5.8",
  "org.scaldi" %% "scaldi-play" % "0.5.15",
  "org.sangria-graphql" %% "sangria" % "1.0.0-RC2",
  "org.sangria-graphql" %% "sangria-play-json" % "0.3.3",
  play.sbt.PlayImport.ws withSources())

libraryDependencies ++= thirdPartyDependencies