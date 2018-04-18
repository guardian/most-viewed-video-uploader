package com.gu.contentapi.mostviewedvideo

import com.gu.contentapi.Config
import com.gu.contentapi.services.Http
import com.gu.contentapi.mostviewedvideo.model.v1.{ MostViewedVideo => MostViewedVideoThrift, MostViewedVideoContainer => MostViewedVideoContainerThrift }
import com.gu.contentapi.models.{ MostViewedVideo => MostViewedVideoModel, MostViewedVideoContainer => MostViewedVideoContainerModel }
import com.squareup.okhttp.Request
import io.circe.parser._

trait OphanStore extends Http {
  import OphanStore._

  protected def getMostViewedVideoOverall(edition: Option[String], config: Config): Either[CustomError, List[MostViewedVideoContainerThrift]] = {
    val url = buildUrl("/api/video/mostviewed", edition, config)
    requestMostViewed(url, edition, config) flatMap (extractMostViewedVideoOverall(_, edition))
  }

  protected def getMostViewedVideoBySection(edition: Option[String], config: Config): Either[CustomError, List[MostViewedVideoContainerThrift]] = {
    val url = buildUrl("/api/video/mostviewed/sections", edition, config)
    requestMostViewed(url, edition, config) flatMap (extractMostViewedVideoBySection(_, edition))
  }

  private[this] def requestMostViewed(url: String, edition: Option[String], config: Config): Either[CustomError, String] = {
    val request = new Request.Builder().url(url).build

    val response = httpClient.newCall(request).execute
    if (response.isSuccessful())
      Right(response.body.string)
    else
      Left(CustomError(s"Failed to send request to Ophan for $url, response was ${response.code}"))
  }

  private[this] def buildUrl(path: String, edition: Option[String], config: Config) =
    s"${config.ophanHost}$path?${buildCountryQuery(edition)}mins=${config.ophanMinutes}&apiKey=${config.ophanKey}"

  private[this] def buildCountryQuery(edition: Option[String]) = {
    edition match {
      case Some(ed) => s"country=${capiEditionToOphanCountry(ed)}&"
      case None => ""
    }
  }
  private[this] def capiEditionToOphanCountry(capiEdition: String) = {
    capiEdition match {
      case "uk" => "gb"
      case other => other
    }
  }
}

object OphanStore {

  def extractMostViewedVideoOverall(json: String, edition: Option[String]): Either[CustomError, List[MostViewedVideoContainerThrift]] =
    decodeOverallJson(json, edition) flatMap (containers => Right(containers map mostViewedVideoContainerToThrift))

  def extractMostViewedVideoBySection(json: String, edition: Option[String]): Either[CustomError, List[MostViewedVideoContainerThrift]] =
    decodeSectionsJson(json, edition) flatMap (containers => Right(containers map mostViewedVideoContainerToThrift))

  /**
   * Unfortunately, circe cannot easily decode a json string to a thrift object (https://github.com/travisbrown/circe/issues/208).
   * The simplest solution is to decode to intermediary case classes, then construct the thrift objects from these.
   */
  private[this] def decodeOverallJson(json: String, edition: Option[String]): Either[CustomError, List[MostViewedVideoContainerModel]] = {
    import io.circe.generic.auto._
    decode[List[MostViewedVideoModel]](json).fold(
      { error => Left(CustomError(error.getMessage)) },
      { overallVideos =>
        val section = edition match {
          case None => Some("overall")
          case Some(_) => None
        }
        Right(List(MostViewedVideoContainerModel(buildId(edition, section), overallVideos)))
      })
  }
  private[this] def decodeSectionsJson(json: String, edition: Option[String]): Either[CustomError, List[MostViewedVideoContainerModel]] = {
    import io.circe.generic.auto._
    decode[Map[String, List[MostViewedVideoModel]]](json).fold(
      { error => Left(CustomError(error.getMessage)) },
      { sections =>
        {
          val containers = sections map { sectionData =>
            val sectionId = sectionData._1
            val videos = sectionData._2
            MostViewedVideoContainerModel(buildId(edition, Some(sectionId)), videos)
          }
          Right(containers.toList)
        }
      })
  }
  private[this] def mostViewedVideoToThrift(mostViewedVideo: MostViewedVideoModel): MostViewedVideoThrift =
    MostViewedVideoThrift(id = mostViewedVideo.id, count = mostViewedVideo.count)

  private[this] def mostViewedVideoContainerToThrift(mostViewedVideoContainer: MostViewedVideoContainerModel): MostViewedVideoContainerThrift = {
    MostViewedVideoContainerThrift(
      id = mostViewedVideoContainer.id,
      videos = mostViewedVideoContainer.videos.map(mostViewedVideoToThrift))
  }

  private[this] def buildId(edition: Option[String], section: Option[String]): String = {
    Seq(edition, section).flatten.mkString("/")
  }

}