import sbt._
import Keys._
import com.typesafe.sbt.SbtScalariform.scalariformSettings

object MostViewedVideoUploaderBuild extends Build {

  val basicSettings = Seq(
    organization  := "com.gu",
    description   := "AWS Lambda for uploading most viewed video data to CAPI.",
    scalaVersion  := "2.11.7",
    scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
  )

  val root = Project("capi-most-viewed-video-uploader", file("."))
    .settings(

      libraryDependencies ++= Seq(
        "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
        "com.amazonaws" % "aws-java-sdk-s3" % "1.10.39",
        "com.amazonaws" % "aws-java-sdk-kinesis" % "1.10.39",
        "com.squareup.okhttp" % "okhttp" % "2.5.0",
        "com.gu" %% "content-api-client" % "7.29",
        "org.scalatest" %% "scalatest" % "2.2.5" % "test"
      )
    )
    .settings(basicSettings)
    .settings(scalariformSettings)

}