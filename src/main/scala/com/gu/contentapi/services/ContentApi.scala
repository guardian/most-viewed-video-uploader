package com.gu.contentapi.services

import com.gu.contentapi.client.GuardianContentClient

class ContentApi(override val targetUrl: String, override val apiKey: String) extends GuardianContentClient(apiKey)

