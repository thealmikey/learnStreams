name := "learnStreams"

version := "0.1"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( 
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.4" % Test
)

