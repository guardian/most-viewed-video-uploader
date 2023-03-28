import sbt._
import Keys._

import com.typesafe.sbt.SbtScalariform.scalariformSettings

val root = Project("most-viewed-video-uploader", file("."))
  .settings(
    scalacOptions += "-deprecation",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
      "com.amazonaws" % "aws-java-sdk-s3" % "1.11.313",
      "com.amazonaws" % "aws-java-sdk-kinesis" % "1.11.313",
      "com.squareup.okhttp3" % "okhttp" % "3.10.0",
      "com.gu" %% "content-api-client-default" % "12.0",
      "io.circe" %% "circe-generic" % "0.9.3",
      "io.circe" %% "circe-parser" % "0.9.3",
      "org.apache.thrift" % "libthrift" % "0.9.2",
      "com.twitter" %% "scrooge-core" % "3.20.0",
      "com.gu" %% "thrift-serializer" % "2.1.0",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    )
  )
  .settings(basicSettings)
  .settings(
    scalariformAutoformat := true,
    assemblyMergeStrategy in assembly := {
      case PathList("com", "gu", "storypackage", _*) => MergeStrategy.first
      case "shared.thrift"                           => MergeStrategy.first
      case x => 
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )

lazy val basicSettings = Seq(
  organization  := "com.gu",
  description   := "AWS Lambda for uploading most viewed video data to CAPI.",
  scalaVersion  := "2.12.5",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
)