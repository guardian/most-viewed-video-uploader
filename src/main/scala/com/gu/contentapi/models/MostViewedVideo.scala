package com.gu.contentapi.models

case class MostViewedVideo(id: String, count: Int)
case class MostViewedVideoContainer(id: String, videos: List[MostViewedVideo])