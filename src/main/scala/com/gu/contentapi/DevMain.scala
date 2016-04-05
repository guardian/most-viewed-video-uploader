package com.gu.contentapi

import java.util
import scala.concurrent.duration.Duration

/**
 * Harness for running the Lambda on a dev machine.
 */
object DevMain extends App {

  val fakeScheduledEvent = new util.HashMap[String, Object]()

  new Lambda().handleRequest(fakeScheduledEvent, null)

}
