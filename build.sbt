import Dependencies._

testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-minSuccessfulTests", "1000", "-workers", "1", "-verbosity", "1")


lazy val commonResolvers = Seq(
  "Sonatype Nexus Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  Resolver.bintrayRepo("buildo", "maven"),
  "buildo mvn" at "https://raw.github.com/buildo/mvn/master/releases",
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
  Resolver.bintrayRepo("kailuowang", "maven"),
  Resolver.bintrayRepo("ovotech", "maven"),
  Resolver.bintrayRepo("cakesolutions", "maven")
)

lazy val commonSettings = Seq(
  scalaVersion  := kafkaScalaVersion,
  resolvers ++= commonResolvers,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
)

lazy val classicJavaKafka = project
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= kafkaJava ++ circeDependencies ++ kafkaSerializationDeps ++ log4jDependencies,
  )

lazy val reactiveKafka = project
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= kafkaReactive ++ circeDependencies ++ kafkaSerializationDeps ++ log4jDependencies,
  )

lazy val cakeSolutionsKafka = project
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= cakeKafka ++ circeDependencies ++ kafkaSerializationDeps ++ log4jDependencies,
  )
