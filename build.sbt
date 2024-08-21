import org.scalajs.linker.interface.ModuleSplitStyle

import java.nio.file.{Files, Path, StandardCopyOption}

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

lazy val copyToDemo = taskKey[Unit]("Build source and run demo")

copyToDemo := {

  new File("./demo/build").mkdirs()

  Files.copy(
    Path.of("./target/scala-3.5.0/somment-fastopt/somment.js"),
    Path.of("./demo/build/somment.js"),
    StandardCopyOption.REPLACE_EXISTING,
  )

  Files.copy(
    Path.of("./target/scala-3.5.0/somment-fastopt/somment.js.map"),
    Path.of("./demo/build/somment.js.map"),
    StandardCopyOption.REPLACE_EXISTING,
  )
}

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "2.8.0",
  "com.lihaoyi" %%% "scalatags" % "0.13.1",
)
