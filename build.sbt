
// shadow sbt-scalajs' crossProject and CrossType from Scala.js 0.6.x
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

enablePlugins(ScalaJSPlugin)

import com.typesafe.sbt.pgp.PgpKeys.publishSigned

import ReleaseTransformations._

lazy val root = crossProject(JSPlatform, JVMPlatform)
    .withoutSuffixFor(JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("."))
    .settings(commonSettings: _*)
    .settings(publishingSettings: _*)
    .jvmSettings(eclipseSettings: _*)
    .enablePlugins(SbtOsgi)
    .jvmSettings(osgiSettings)
    .jvmSettings(
    		(unmanagedResourceDirectories in Compile) += baseDirectory.value / "../src/main/resources",
		OsgiKeys.exportPackage  := Seq("org.isomorf.runtime.effect.api.monadic"),
		OsgiKeys.importPackage  := Seq(
			"org.isomorf.foundation.runtime", 
			"org.isomorf.foundation.runtime.effects",
			"org.isomorf.foundation.runtime.effects.plugins", 
			"scala"
		),
		OsgiKeys.additionalHeaders  := Map("Service-Component" -> "OSGI-INF/component.xml", "Bundle-ActivationPolicy" -> "lazy")
    )
    .jsSettings(
      EclipseKeys.skipProject := true
    )
    
libraryDependencies in ThisBuild += "org.isomorf" %%% "foundation-runtime-effects" % "0.1.0"

lazy val rootJVM = root.jvm

lazy val rootJS = root.js

val organizationGlobal = "org.isomorf"

val scalaVersionGlobal = "2.12.3"

val crossScalaVersionsGlobal = Seq("2.11.11", scalaVersionGlobal)

crossScalaVersions := crossScalaVersionsGlobal

val artifactGlobal = "effect-api-monadic"

val projectGlobal = "scala-" + artifactGlobal

val projectOrgGlobal = organizationGlobal.split("\\.").reverse.mkString("-")



val commonSettings = Seq(
  organization := organizationGlobal,
  name         := artifactGlobal,
  scalaVersion := scalaVersionGlobal,
  scalacOptions := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint:_", "-Ywarn-unused-import"),
  crossScalaVersions := crossScalaVersionsGlobal
)

val publishingSettings = Seq(
//useGpg := true,
  homepage   := Some(url("https://github.com/" + projectOrgGlobal + "/" + projectGlobal)),
  scmInfo    := Some(ScmInfo(url("https://github.com/" + projectOrgGlobal + "/" + projectGlobal),
                              "git@github.com:isomorf-org/" + projectGlobal + ".git")),
  developers := List(Developer("bdkent", "Brian Kent", "brian.kent@isomorf.io", url("https://github.com/bdkent"))),

  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),

  pomIncludeRepository := { _ => false },

  publishMavenStyle := true,

  // Add sonatype repository settings
  publishTo := Some(
    if (isSnapshot.value) {
      Opts.resolver.sonatypeSnapshots
    }
    else {
      Opts.resolver.sonatypeStaging
    }
  ),


  releaseCrossBuild := true,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    //publishArtifacts,
    releaseStepCommandAndRemaining("+publishArtifacts"),
    releaseStepCommand("makeDocs"),
    setNextVersion,
    commitNextVersion,
    releaseStepCommand(s"sonatypeReleaseAll ${organizationGlobal}"),
    pushChanges
  )
)

val eclipseSettings = Seq(
  EclipseKeys.withSource := true
)

publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

commands += Command.command("releaser") {
  "release cross" ::
  //s"sonatypeReleaseAll ${organizationGlobal}" ::
   _
}

commands += Command.command("makeDocs") {
  "makeSite" :: "ghdvCopyReadme" :: "ghdvCopyScaladocs" ::  _
}

autoAPIMappings := true

enablePlugins(SiteScaladocPlugin)

siteSubdirName in SiteScaladoc := "scaladocs/api/" + version.value

enablePlugins(PreprocessPlugin)

enablePlugins(SbtGhDocVerPlugin)

preprocessVars in Preprocess := Map(
	"VERSION" -> version.value,
	"ORGANIZATION" -> organizationGlobal,
	"PROJECT" -> projectGlobal,
	"PROJECT_ORG" -> projectOrgGlobal,
	"ARTIFACT" -> artifactGlobal
)
