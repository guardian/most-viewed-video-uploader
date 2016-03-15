package com.gu.contentapi.services

import com.squareup.okhttp.OkHttpClient

trait Http {
  lazy val httpClient: OkHttpClient = new OkHttpClient()
}