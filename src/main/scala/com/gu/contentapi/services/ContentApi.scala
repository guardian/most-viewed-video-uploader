package com.gu.contentapi.services

import com.gu.contentapi.client.GuardianContentClient
import com.gu.contentapi.client.ContentApiQueries

class ContentApi(override val targetUrl: String, override val apiKey: String) extends GuardianContentClient(apiKey) with ContentApiQueries

