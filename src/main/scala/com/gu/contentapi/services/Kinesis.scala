package com.gu.contentapi.services

import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.kinesis.{ AmazonKinesisAsync, AmazonKinesisAsyncClientBuilder }
import com.amazonaws.services.kinesis.model.{ PutRecordsRequest, PutRecordsRequestEntry, PutRecordsResult }
import com.gu.contentapi.Config
import com.gu.contentapi.mostviewedvideo.model.v1._
import com.gu.contentapi.mostviewedvideo.CustomError
import com.gu.thrift.serializer._

trait Kinesis {

  private lazy val kinesisClient: AmazonKinesisAsync = {
    AmazonKinesisAsyncClientBuilder.standard()
      .withRegion(Region.getRegion(Regions.EU_WEST_1).getName)
      .build()
  }

  def publish(data: MostViewedVideoContainer, config: Config): Either[CustomError, String] = {

    val record = new PutRecordsRequestEntry()
      .withPartitionKey(data.id)
      .withData(ThriftSerializer.serializeToBytes(data, Some(GzipType), None))

    val request = new PutRecordsRequest()
      .withStreamName(config.kinesisName)
      .withRecords(record)

    val putRecordsResult: PutRecordsResult = kinesisClient.putRecords(request)
    if (putRecordsResult.getFailedRecordCount > 0)
      Left(CustomError(s"Failed to publish most-viewed videos for: ${data.id}."))
    else
      Right(data.id)
  }

}
