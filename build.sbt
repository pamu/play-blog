name := "blog"

version := "1.0.0"

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.5.0",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "com.zaxxer" % "HikariCP" % "2.4.5",
  "postgresql" % "postgresql" % "9.1-901.jdbc4"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

fork in run := true