import java.nio.file.{ Files, Paths }

import com.gu.contentapi.mostviewedvideo.model.v1._
import com.gu.contentapi.mostviewedvideo.OphanStore
import org.scalatest.{ Matchers, FlatSpec }

class ExtractMostViewedVideoTest extends FlatSpec with Matchers {

  behavior of "most-viewed video extraction"

  it should "extract overall most-viewed videos" in {
    val json = loadFileFromClasspath("most-viewed-video-overall.json")
    val expected = List(MostViewedVideoContainer(
      id = "us/overall",
      videos = List(
        MostViewedVideo(id = "gu-video-56ed4a37e4b0304172354aab", count = 126),
        MostViewedVideo(id = "gu-video-56ed24c4e4b0d5bc16af57dd", count = 61)
      )
    ))
    val overall = OphanStore.extractMostViewedVideoOverall(json, Some("us")).toOption
    overall should be(Some(expected))
  }

  it should "extract most-viewed videos by section" in {
    val json = loadFileFromClasspath("most-viewed-video-sections.json")
    val expected = List(
      MostViewedVideoContainer(id = "world", videos = List(
        MostViewedVideo("gu-video-56e99589e4b06837afd3e2d7", 686),
        MostViewedVideo("gu-video-56eb7663e4b0a5327f1d183e", 308))
      ),
      MostViewedVideoContainer(id = "us-news", videos = List(
        MostViewedVideo("gu-video-56ebbfcde4b0ba41c3b4a85e", 257),
        MostViewedVideo("gu-video-56e976d1e4b06837afd3e260", 223)
      ))
    )
    val sections = OphanStore.extractMostViewedVideoBySection(json, None).toOption
    sections should be(Some(expected))
  }

  def loadFileFromClasspath(path: String): String = {
    val uri = Paths.get(getClass.getClassLoader.getResource(path).toURI)
    val bytes = Files.readAllBytes(uri)
    new String(bytes, "UTF-8")
  }
}