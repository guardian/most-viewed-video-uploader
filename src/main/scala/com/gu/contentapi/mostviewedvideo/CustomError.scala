package com.gu.contentapi.mostviewedvideo

case class CustomError(message: String) {
  override def toString = message
}
