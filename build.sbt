name := "rxcode"

version := "1.0.0"

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  jdbc,
  ws,
  "com.typesafe.play" %% "play-json" % "2.5.4",
  "com.typesafe.play" %% "play-ws" % "2.5.4",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.postgresql" % "postgresql" % "9.4.1209.jre7",
  "joda-time" % "joda-time" % "2.9.4",
  "com.h2database" % "h2" % "1.4.192"
)

routesGenerator := InjectedRoutesGenerator

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint"
)

scalacOptions in (Compile, doc) += "-no-link-warnings"


fork in run := true


