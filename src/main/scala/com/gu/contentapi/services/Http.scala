package com.gu.contentapi.services

import okhttp3.OkHttpClient

trait Http {
  lazy val httpClient: OkHttpClient = new OkHttpClient()
}