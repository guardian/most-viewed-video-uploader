package com.gu.contentapi.services

import java.nio.ByteBuffer

import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient
import com.amazonaws.services.kinesis.model.{ PutRecordsResult, PutRecordsRequestEntry, PutRecordsRequest }
import com.gu.contentapi.Config
import com.gu.contentapi.mostviewedvideo.model.v1._
import com.gu.contentapi.mostviewedvideo.CustomError
import com.gu.thrift.serializer.ThriftSerializer

trait Kinesis extends ThriftSerializer {

  private lazy val kinesisClient: AmazonKinesisAsyncClient = {
    val kinesisClient = new AmazonKinesisAsyncClient()
    kinesisClient.setRegion(Region.getRegion(Regions.EU_WEST_1))
    kinesisClient

  }

  def publish(data: MostViewedVideoContainer, config: Config): Either[CustomError, String] = {

    val record = new PutRecordsRequestEntry()
      .withPartitionKey(data.id)
      .withData(ByteBuffer.wrap(serializeToBytes(data)))

    val request = new PutRecordsRequest()
      .withStreamName(config.kinesisName)
      .withRecords(record)

    val putRecordsResult: PutRecordsResult = kinesisClient.putRecords(request)
    if (putRecordsResult.getFailedRecordCount > 0)
      left(CustomError(s"Failed to publish most-viewed videos for: ${data.id}."))
    else
      right(data.id)
  }

}
