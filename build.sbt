name := "mapacho"

organization := "ru.evseev"

version := "1.1"

scalaVersion := "2.11.2"

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
    "org.scalatest"   %% "scalatest"    % "2.2.1",
    "org.scalacheck"  %% "scalacheck"   % "1.11.5"
)

scalacOptions ++= List("-feature","-deprecation", "-unchecked", "-Xlint")

scalacOptions ++= Seq("-encoding", "UTF-8")

javacOptions ++= Seq("-encoding", "UTF-8")

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l", "org.scalatest.tags.Slow", "-u","target/junit-xml-reports", "-oD", "-eS")

org.scalastyle.sbt.ScalastylePlugin.Settings
 
org.scalastyle.sbt.PluginKeys.config <<= baseDirectory { _ / "src/main/config" / "scalastyle-config.xml" }

EclipseKeys.withSource := true

instrumentSettings
