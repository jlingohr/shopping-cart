name := "shopping-cart"

version := "0.1"

scalaVersion := "2.13.3"

val http4sVersion = "0.21.8"
val circeVersion = "0.13.0"
val catsVersion = "2.2.0"
val catsEffectVersion = "2.2.0"
val refinedVersion = "0.9.17"
val fs2Version = "2.4.4"
val http4sJwtAuthVersion = "0.0.5"
val catsMeowMtlVersion = "0.4.1"

libraryDependencies ++= Seq(
  compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
  compilerPlugin("org.augustjune" %% "context-applied" % "0.1.2"),

  // Cats
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "dev.profunktor" %% "console4cats" % "0.8.1",
  "org.manatki" %% "derevo-cats" % "0.10.5",
  "org.manatki" %% "derevo-cats-tagless" % "0.10.5",
  "com.github.cb372" %% "cats-retry" % "2.0.0",
  "com.olegpy" %% "meow-mtl-core" % catsMeowMtlVersion,

  // Newtype
  "io.estatico" %% "newtype" % "0.4.3",

  // Refineminet types
  "eu.timepit" %% "refined" % refinedVersion,
  "eu.timepit" %% "refined-cats" % refinedVersion,

  // squants
  "org.typelevel" %% "squants" % "1.7.0",

  // Logging
  "io.chrisdavenport" %% "log4cats-slf4j" % "1.1.1",

  // web framework
  "co.fs2" %% "fs2-core" % fs2Version,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-server" % http4sVersion,
  "org.http4s" %% "http4s-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "dev.profunktor" %% "http4s-jwt-auth" % http4sJwtAuthVersion,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-refined" % circeVersion,
)

scalacOptions += "-Ymacro-annotations"