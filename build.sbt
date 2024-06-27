import sbt._
import Keys._

val root = Project("most-viewed-video-uploader", file("."))
  .settings(
    scalacOptions += "-deprecation",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.2.2",
      "com.amazonaws" % "aws-java-sdk-s3" % "1.12.641",
      "com.amazonaws" % "aws-java-sdk-kinesis" % "1.12.641",
      "com.squareup.okhttp3" % "okhttp" % "4.10.0",
      "com.gu" %% "content-api-client-default" % "31.0.2",
      "io.circe" %% "circe-generic" % "0.14.5",
      "io.circe" %% "circe-parser" % "0.14.5",
      "org.apache.thrift" % "libthrift" % "0.18.1",
      "com.twitter" %% "scrooge-core" % "22.12.0",
      "com.gu" %% "thrift-serializer" % "5.0.7",
      "org.scalatest" %% "scalatest" % "3.2.15" % "test"
    ),
    assemblyJarName := "most-viewed-video-uploader.jar"
  )
  .settings(basicSettings)
  .settings(
    scalariformAutoformat := true,
    assembly / assemblyMergeStrategy   := {
      case PathList("com", "gu", "storypackage", _*) => MergeStrategy.first
      case "shared.thrift"                           => MergeStrategy.first
      case "module-info.class" => MergeStrategy.first
      case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.first
      case "BUILD" => MergeStrategy.first
      case x => 
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }
  )

dependencyOverrides ++=  Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.2"
)
Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-u", sys.env.getOrElse("SBT_JUNIT_OUTPUT", "junit"))

lazy val basicSettings = Seq(
  organization  := "com.gu",
  description   := "AWS Lambda for uploading most viewed video data to CAPI.",
  scalaVersion  := "2.13.0",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
)