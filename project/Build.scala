import com.twitter.scrooge.ScroogeSBT
import sbt._
import Keys._
import sbtassembly.Plugin._
import sbtassembly.Plugin.AssemblyKeys._

import com.typesafe.sbt.SbtScalariform.scalariformSettings
import sbtassembly.Plugin.{PathList, MergeStrategy}

object MostViewedVideoUploaderBuild extends Build {

  val basicSettings = Seq(
    organization  := "com.gu",
    description   := "AWS Lambda for uploading most viewed video data to CAPI.",
    scalaVersion  := "2.11.7",
    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
  )

  val root = Project("most-viewed-video-uploader", file("."))
    .settings(ScroogeSBT.newSettings: _*)
    .settings(

      libraryDependencies ++= Seq(
        "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
        "com.amazonaws" % "aws-java-sdk-s3" % "1.10.39",
        "com.amazonaws" % "aws-java-sdk-kinesis" % "1.10.39",
        "com.squareup.okhttp" % "okhttp" % "2.5.0",
        "com.gu" %% "content-api-client" % "7.29",
        "io.circe" %% "circe-generic" % "0.3.0",
        "io.circe" %% "circe-parser" % "0.3.0",
        "org.apache.thrift" % "libthrift" % "0.9.2",
        "com.twitter" %% "scrooge-core" % "3.20.0",
        "com.gu" %% "thrift-serializer" % "0.0.1",
        "org.scalatest" %% "scalatest" % "2.2.5" % "test"
      )
    )
    .settings(basicSettings)
    .settings(scalariformSettings)
    .settings(assemblySettings: _*)
    .settings(
      mergeStrategy in assembly <<= (mergeStrategy in assembly) {
        (old) => {
          case PathList("com", "gu", "storypackage", _*) => MergeStrategy.first
          case x => old(x)
        }
      }
    )

}