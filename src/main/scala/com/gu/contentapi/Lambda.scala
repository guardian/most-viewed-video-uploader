package com.gu.contentapi

import java.util.{ Map => JMap }
import com.amazonaws.services.lambda.runtime.{ RequestHandler, Context }
import com.gu.contentapi.mostviewedvideo.model.v1._
import com.gu.contentapi.mostviewedvideo.{ CustomError, OphanStore }
import com.gu.contentapi.services.{ Kinesis, ContentApi }
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class Lambda
    extends RequestHandler[JMap[String, Object], Unit]
    with Kinesis
    with OphanStore {

  override def handleRequest(event: JMap[String, Object], context: Context): Unit = {
    val config = new Config(context)

    val capi = new ContentApi(config.capiUrl, config.capiKey)

    def upload(mostViewedVideoContainers: Either[CustomError, List[MostViewedVideoContainer]]): Unit = {
      val result = mostViewedVideoContainers map { containers =>
        val pubResults = containers map (publish(_, config))
        pubResults.flatMap(_.swap.toOption)
      }

      println(result.fold(
        { ophanError => ophanError.toString },
        { kinesisErrors => kinesisErrors.mkString("\n") }))
    }

    val editionIds = Await.result(capi.getResponse(capi.editions).map(_.results.map(_.id)), 5.seconds)

    editionIds foreach { editionId =>

      upload(getMostViewedVideoBySection(Some(editionId), config))

      upload(getMostViewedVideoOverall(Some(editionId), config))
    }

    upload(getMostViewedVideoBySection(None, config))

    upload(getMostViewedVideoOverall(None, config))
  }
}