
import com.typesafe.sbt.pgp.PgpKeys.publishSigned

import ReleaseTransformations._

// default to unused
publishArtifact in ThisBuild := false
skip in publish := true
publishTo in ThisBuild := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

homepage in ThisBuild := Some(url(Common.Publish.homepage))

scmInfo in ThisBuild := Some(ScmInfo(url(Common.Publish.homepage), Common.Publish.repo))

developers in ThisBuild := Common.Publish.devs.map({ case (h,n,e,u) => Developer(h, n, e, url(u)) }).toList

licenses in ThisBuild += (Common.License.name, url(Common.License.url))

pomIncludeRepository in ThisBuild := { _ => false }

publishMavenStyle in ThisBuild := true

releaseCrossBuild in ThisBuild := true

releasePublishArtifactsAction in ThisBuild := PgpKeys.publishSigned.value

releaseProcess in ThisBuild := Seq[ReleaseStep](
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
	releaseStepCommand(s"sonatypeReleaseAll ${Common.organization}"),
	pushChanges
)