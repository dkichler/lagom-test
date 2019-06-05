organization in ThisBuild := "org.dkichler"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `lagom-test` = (project in file("."))
  .aggregate(`lagom-test-api`, `lagom-test-impl`, `lagom-test-stream-api`, `lagom-test-stream-impl`)

lazy val `lagom-test-api` = (project in file("lagom-test-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-test-impl` = (project in file("lagom-test-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`lagom-test-api`)

lazy val `lagom-test-stream-api` = (project in file("lagom-test-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-test-stream-impl` = (project in file("lagom-test-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagom-test-stream-api`, `lagom-test-api`)
