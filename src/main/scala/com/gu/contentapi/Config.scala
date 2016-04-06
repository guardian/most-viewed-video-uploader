package com.gu.contentapi

import java.util.Properties

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.s3.AmazonS3Client

import scala.util.Try

class Config(val context: Context) {
  protected val s3Client: AmazonS3Client = new AmazonS3Client()

  val isProd = Try(context.getFunctionName.toLowerCase.contains("-prod")).getOrElse(false)
  private val stage = if (isProd) "PROD" else "CODE"
  private val config = loadConfig()

  val capiUrl = getConfigItem("capi.url")
  val capiKey = getConfigItem("capi.key")

  val ophanHost = getConfigItem("ophan.host")
  val ophanKey = getConfigItem("ophan.key")
  val ophanMinutes = getConfigItem("ophan.minutes")

  val kinesisName = getConfigItem("kinesis.name")

  private def loadConfig() = {
    val bucketName = "content-api-config"
    val configFileKey = s"most-viewed-video-uploader/$stage/config.properties"
    val configInputStream = s3Client.getObject(bucketName, configFileKey).getObjectContent
    val configFile: Properties = new Properties()
    Try(configFile.load(configInputStream)) orElse sys.error("Could not load config file from s3. This lambda will not run.")
    configFile
  }

  private def getConfigItem(itemName: String): String = {
    Option(config.getProperty(itemName)) getOrElse sys.error(s"'$itemName' property is missing.")
  }

}