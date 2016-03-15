namespace scala com.gu.contentapi.mostviewedvideo.model.v1

struct MostViewedVideo {

    1: required string id;

    2: required i32 count;
}

struct MostViewedVideoContainer {

    1: required string id;

    2: required list<MostViewedVideo> videos;

}