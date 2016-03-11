package com.gu.contentapi.services

import com.squareup.okhttp.OkHttpClient

object Http {
  lazy val httpClient: OkHttpClient = new OkHttpClient()
}
