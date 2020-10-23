name := "shopping-cart"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
  compilerPlugin("org.augustjune" %% "context-applied" % "0.1.2"),

  // Cats
  "org.typelevel" %% "cats-core" % "2.1.0",
  "org.typelevel" %% "cats-effect" % "2.1.0",
  "dev.profunktor" %% "console4cats" % "0.8.1",
  "org.manatki" %% "derevo-cats" % "0.10.5",
  "org.manatki" %% "derevo-cats-tagless" % "0.10.5",
  "com.github.cb372" %% "cats-retry" % "2.0.0",

  // Newtype
  "io.estatico" %% "newtype" % "0.4.3",

  // Refineminet types
  "eu.timepit" %% "refined" % "0.9.12",

  // squants
  "org.typelevel" %% "squants" % "1.6.0",

  // Logging
  "io.chrisdavenport" %% "log4cats-core" % "1.1.1"
)

scalacOptions += "-Ymacro-annotations"