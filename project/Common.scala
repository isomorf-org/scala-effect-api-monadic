import sbt._
import Keys._

object Common {
  
  val organization = "org.isomorf" 
	val name = "effect-api-monadic"
	
	object GitHub {
     val project = s"scala-${Common.name}"
     val organization = Common.organization.split("\\.").reverse.mkString("-")
  }
  
  object Docs {
     val relativeDir = "scaladocs/api/"
     val rootDir = "https://" + Common.GitHub.organization + ".github.io/" + Common.GitHub.project + "/" + Common.Docs.relativeDir
  }
  
  object Scala {
    val series = "2.12"
    val version = series + ".3"
    val crossVersions = Seq("2.11.11", version)
    val compilerOptions = Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint:_", "-Ywarn-unused-import")
  }
  
  object Publish {
    val homepage = "https://github.com/" + Common.GitHub.organization + "/" + Common.GitHub.project
    val repo = "git@github.com:" + Common.GitHub.organization + "/" + Common.GitHub.project + ".git"
    val devs = Seq(
      ("bdkent", "Brian Kent", "brian.kent@isomorf.io", "https://github.com/bdkent")
    )
  }
  
  object License {
    val name = "Apache-2.0"
    val url = "http://www.apache.org/licenses/LICENSE-2.0"
  }
  
  object OSGi {
    val exportPackage = Seq("org.isomorf.runtime.effect.api.monadic")
    val importPackage = Seq(
      "org.isomorf.foundation.runtime", 
      "org.isomorf.foundation.runtime.effects",
      "org.isomorf.foundation.runtime.effects.plugins", 
      "scala"
    )
    val additionalHeaders = Map(
      "Service-Component" -> "OSGI-INF/component.xml",
      "Bundle-ActivationPolicy" -> "lazy"
    )
  }
}