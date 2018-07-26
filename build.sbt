// shadow sbt-scalajs' crossProject and CrossType from Scala.js 0.6.x
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

enablePlugins(ScalaJSPlugin)

lazy val root = crossProject(JSPlatform, JVMPlatform)
    .withoutSuffixFor(JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("."))
    .settings(
      name := Common.name, // must be here...seems to be artifact of crossProject?
//      skip in publish := false,
//      publishArtifact in (Test, packageBin) := false,
//      publishArtifact in (Test, packageDoc) := false,
      publishArtifact := true,
      publishTo := Some(if (isSnapshot.value) { Opts.resolver.sonatypeSnapshots } else { Opts.resolver.sonatypeStaging })
    )
    .jvmSettings(
      EclipseKeys.withSource := true
    )
    .enablePlugins(SbtOsgi)
    .jvmSettings(osgiSettings)
    .jvmSettings(
    		(unmanagedResourceDirectories in Compile) += baseDirectory.value / "../src/main/resources",
		OsgiKeys.exportPackage := Common.OSGi.exportPackage,
		OsgiKeys.importPackage := Common.OSGi.importPackage,
		OsgiKeys.additionalHeaders := Common.OSGi.additionalHeaders
    )
    .jsSettings(
      EclipseKeys.skipProject := true
    )
    
lazy val rootJVM = root.jvm

lazy val rootJS = root.js
