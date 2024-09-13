import org.scalajs.linker.interface.ModuleSplitStyle

import java.nio.file.{Files, Path, StandardCopyOption}
import scala.sys.process._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.5.0"

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "somment",
  )

scalaJSLinkerConfig ~= { _
  .withModuleKind(ModuleKind.ESModule)
  .withModuleSplitStyle(ModuleSplitStyle.FewestModules)
}

scalacOptions ++= Seq("-Xmax-inlines", "100")

lazy val copyToDemo = taskKey[Unit]("Build source and run demo")
copyToDemo := {
  Files.copy(
    Path.of("./target/scala-3.5.0/somment-fastopt/somment.js"),
    Path.of("./demo/somment.js"),
    StandardCopyOption.REPLACE_EXISTING,
  )

  Files.copy(
    Path.of("./target/scala-3.5.0/somment-fastopt/somment.js.map"),
    Path.of("./demo/somment.js.map"),
    StandardCopyOption.REPLACE_EXISTING,
  )
}

lazy val npmRelease = taskKey[Unit]("Build and release to npm registry")
npmRelease := {
  (Compile / fullOptJS).value

  Files.copy(
    Path.of("./target/scala-3.5.0/somment-opt/somment.js"),
    Path.of("./demo/somment.js"),
    StandardCopyOption.REPLACE_EXISTING,
  )

  Files.copy(
    Path.of("./target/scala-3.5.0/somment-opt/somment.js.map"),
    Path.of("./demo/somment.js.map"),
    StandardCopyOption.REPLACE_EXISTING,
  )

  val workDir = new File("./demo")
  Process("npm" :: "publish" :: Nil, workDir) !
}

val sttpVersion = "4.0.0-M17"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "2.8.0",
  "com.lihaoyi" %%% "scalatags" % "0.13.1",

  "org.typelevel" %%% "cats-effect" % "3.5.4",

  "com.lihaoyi" %%% "upickle" % "4.0.0",
  "com.softwaremill.sttp.client4" %%% "upickle" % sttpVersion,
  "com.softwaremill.sttp.client4" %%% "cats" % sttpVersion,

)

dependencyOverrides ++= Seq(
  "com.lihaoyi" %%% "upickle" % "4.0.0",
)


