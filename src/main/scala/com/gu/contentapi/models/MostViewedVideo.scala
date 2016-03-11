package com.gu.contentapi.models


case class MostViewedVideo(id: String, count: Int, paths: List[String])
case class MostViewedVideoContainer(id: String, content: List[MostViewedVideo])