package com.gu.contentapi.services

import java.nio.ByteBuffer

import cats.data.Xor
import cats.data.Xor._
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.handlers.AsyncHandler
import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.kinesis.AmazonKinesisAsyncClient
import com.amazonaws.services.kinesis.model.{ PutRecordsResult, PutRecordsRequestEntry, PutRecordsRequest }
import com.gu.contentapi.Config
import com.gu.contentapi.mostviewedvideo.model.v1._
import com.gu.contentapi.mostviewedvideo.CustomError
import com.gu.thrift.serializer.ThriftSerializer

import scala.concurrent.{ Future, Promise }

trait Kinesis extends ThriftSerializer {

  private lazy val kinesisClient: AmazonKinesisAsyncClient = {
    val kinesisClient = new AmazonKinesisAsyncClient(new ProfileCredentialsProvider("capi"))
    kinesisClient.setRegion(Region.getRegion(Regions.EU_WEST_1))
    kinesisClient

  }

  def publish(data: MostViewedVideoContainer, config: Config): Future[Xor[CustomError, String]] = {

    val record = new PutRecordsRequestEntry()
      .withPartitionKey(data.id)
      .withData(ByteBuffer.wrap(serializeToBytes(data)))

    val request = new PutRecordsRequest()
      .withStreamName(config.kinesisName)
      .withRecords(record)

    val promise = Promise[Xor[CustomError, String]]()

    kinesisClient.putRecordsAsync(request, new AsyncHandler[PutRecordsRequest, PutRecordsResult] {
      override def onError(exception: Exception): Unit =
        promise.success(left(CustomError(s"Failed to publish most-viewed videos for: ${data.id}. Failed with error: ${exception.getStackTrace}")))

      override def onSuccess(request: PutRecordsRequest, result: PutRecordsResult): Unit =
        promise.success(right(data.id))
    })

    promise.future
  }

}
