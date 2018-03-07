import sbt._

object Dependencies {

  // Versions
  val kafkaScalaVersion = "2.11.11"
  val kafkaVersion = "1.0.0"
  val kafkaSerializationVersion = "0.3.6"
  val akkaVersion = "2.4.17"
  val log4jVersion = "2.8.2"
  val circeVersion = "0.9.0"

  // Deps
  val typesafe = "com.typesafe" % "config" % "1.3.1"

  val reactiveKafka = "com.typesafe.akka" %% "akka-stream-kafka" % "0.19"
  val standardKafka = "org.apache.kafka" %% "kafka" % kafkaVersion % Test exclude("org.slf4j", "slf4j-log4j12")
  val cakeSolutionsKafka = "net.cakesolutions" %% "scala-kafka-client" % kafkaVersion
  val kafkaSerializationDeps = Seq(
    "com.ovoenergy" %% "kafka-serialization-core",
    "com.ovoenergy" %% "kafka-serialization-circe"
  ).map(_ % kafkaSerializationVersion)

  val actor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val logging = "com.typesafe.akka" %% "akka-slf4j" % akkaVersion

  val enumero =  "io.buildo" %% "enumero" % "1.2.1"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % "test"
  val kafkaTest = "net.manub" %% "scalatest-embedded-kafka" % "1.0.0" % "test"

  val circeDependencies = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

  val enumeroCirce = "io.buildo" %% "enumero-circe-support" % "1.2.1"
  val circeJava8 = "io.circe" %% "circe-java8" % circeVersion

  val log4jDependencies = Seq(
    "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
    "org.apache.logging.log4j" % "log4j-core" % log4jVersion % Runtime,
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.9"
  )

  val kafkaJava = Seq(
    typesafe,
    enumero,
    enumeroCirce,
    circeJava8,
    standardKafka,
    scalaTest,
    kafkaTest
  )

  val kafkaReactive = Seq(
    enumero,
    enumeroCirce,
    circeJava8,
    reactiveKafka,
    scalaTest,
    kafkaTest
  )

  val cakeKafka = Seq(
    typesafe,
    enumero,
    enumeroCirce,
    circeJava8,
    cakeSolutionsKafka,
    scalaTest,
    kafkaTest
  )
}
