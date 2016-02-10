name := """HEP"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "com.google.jimfs" % "jimfs" % "1.0" % "test",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.1" % "test",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc4"
)

libraryDependencies += specs2 % Test

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

fork in run := true