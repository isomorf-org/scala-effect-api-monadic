
commands += Command.command("makeDocs") {
  "makeSite" :: "ghdvCopyReadme" :: "ghdvCopyScaladocs" ::  _
}

autoAPIMappings in ThisBuild := true

apiURL in ThisBuild := Some(url(Common.Docs.rootDir + version.value))

scalacOptions in (Compile, doc) := Seq("-groups", "-implicits")

enablePlugins(SiteScaladocPlugin)

siteSubdirName in SiteScaladoc := Common.Docs.relativeDir + version.value

enablePlugins(PreprocessPlugin)

enablePlugins(SbtGhDocVerPlugin)

preprocessVars in Preprocess := Map(
	"VERSION" -> version.value,
	"ORGANIZATION" -> Common.organization,
	"PROJECT" -> Common.GitHub.project,
	"PROJECT_ORG" -> Common.GitHub.organization,
	"ARTIFACT" -> Common.name
)