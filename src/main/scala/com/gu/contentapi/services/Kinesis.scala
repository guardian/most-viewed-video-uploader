package com.gu.contentapi.services

import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient

trait Kinesis {

  lazy val kinesisClient: AmazonKinesisAsyncClient = {
    val kinesisClient = new AmazonKinesisAsyncClient()
    kinesisClient.setRegion(Region.getRegion(Regions.EU_WEST_1))
    kinesisClient

  }
}
