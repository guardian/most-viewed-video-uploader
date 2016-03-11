package com.gu.contentapi

import java.util.{ Map => JMap }
import com.amazonaws.services.lambda.runtime.{ RequestHandler, Context }
import com.gu.contentapi.services.{Http, ContentApi}
import scala.concurrent.Future

class Lambda
    extends RequestHandler[JMap[String, Object], Unit] {

  override def handleRequest(event: JMap[String, Object], context: Context): Unit = {

    implicit val config = new Config(context)

    println("The uploading of most viewed video has started")


    println("The uploading of most viewed video has started.")
  }

}