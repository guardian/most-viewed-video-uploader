package com.gu.contentapi

import java.util.{ Map => JMap }
import cats.data.Xor
import cats.data.Xor._
import com.amazonaws.services.lambda.runtime.{ RequestHandler, Context }
import com.gu.contentapi.mostviewedvideo.model.v1._
import com.gu.contentapi.mostviewedvideo.{ CustomError, OphanStore }
import com.gu.contentapi.services.{ Kinesis, ContentApi }
import scala.concurrent.ExecutionContext.Implicits.global

class Lambda
    extends RequestHandler[JMap[String, Object], Unit]
    with Kinesis
    with OphanStore {

  override def handleRequest(event: JMap[String, Object], context: Context): Unit = {
    val config = new Config(context)

    val capi = new ContentApi(config.capiUrl, config.capiKey)

    def onOphanResponse(mostViewedContainers: Xor[CustomError, List[MostViewedVideoContainer]]): Unit = {
      mostViewedContainers match {
        case Left(error) => println(error.toString)
        case Right(containers) => containers.foreach(publish(_, config) map onKinesisResponse)
      }
    }
    def onKinesisResponse(kinesisResponse: Xor[CustomError, String]): Unit = {
      println(kinesisResponse.fold(
        { error => error.toString },
        { id => s"Successfully sent most-viewed videos for $id" }
      ))
    }

    val futureEditionIds = capi.getResponse(capi.editions).map(_.results.map(_.id))
    for {
      editionIds <- futureEditionIds
    } yield {

      editionIds map { editionId =>

        getMostViewedVideoBySection(Some(editionId), config) map onOphanResponse

        getMostViewedVideoOverall(Some(editionId), config) map onOphanResponse
      }
    }

    getMostViewedVideoBySection(None, config) map onOphanResponse
  }
}