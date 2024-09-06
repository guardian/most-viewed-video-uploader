import sbt._
import Keys._

val awsSdkVersion = "1.12.771"
val okHttpVersion = "4.12.0"
val capiClientVersion = "31.0.2"
val circeVersion = "0.14.5"
val apacheThriftVersion = "0.20.0"
val scroogeVersion = "22.12.0"
val thriftSerializerVersion = "5.0.7"

val root = Project("most-viewed-video-uploader", file("."))
  .settings(
    scalacOptions += "-deprecation",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.2.3",
      "com.amazonaws" % "aws-java-sdk-s3" % awsSdkVersion,
      "com.amazonaws" % "aws-java-sdk-kinesis" % awsSdkVersion,
      "com.squareup.okhttp3" % "okhttp" % okHttpVersion,
      "com.gu" %% "content-api-client-default" % capiClientVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.apache.thrift" % "libthrift" % apacheThriftVersion,
      "com.twitter" %% "scrooge-core" % scroogeVersion,
      "com.gu" %% "thrift-serializer" % thriftSerializerVersion,
      "org.scalatest" %% "scalatest" % "3.2.15" % "test"
    ),
    assemblyJarName := "most-viewed-video-uploader.jar"
  )
  .settings(basicSettings)
  .settings(
    assembly / assemblyMergeStrategy   := {
      case PathList("com", "gu", "storypackage", _*) => MergeStrategy.first
      case "shared.thrift"                           => MergeStrategy.first
      case "module-info.class" => MergeStrategy.first
      case PathList("META-INF", "versions", "9", "module-info.class") => MergeStrategy.first
      case PathList("mozilla", "public-suffix-list.txt") => MergeStrategy.filterDistinctLines
      case PathList("META-INF", "okio.kotlin_module") => MergeStrategy.preferProject
      case "BUILD" => MergeStrategy.first
      case x => 
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    }
  )

Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-u", sys.env.getOrElse("SBT_JUNIT_OUTPUT", "junit"), "-o")

lazy val basicSettings = Seq(
  organization  := "com.gu",
  description   := "AWS Lambda for uploading most viewed video data to CAPI.",
  scalaVersion  := "2.13.14",
  scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
)
