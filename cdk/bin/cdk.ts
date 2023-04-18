import "source-map-support/register";
import { App } from "aws-cdk-lib";
import { MostViewedVideoUploader } from "../lib/most-viewed-video-uploader";

const app = new App();
new MostViewedVideoUploader(app, "MostViewedVideoUploader-CODE", { stack: "content-api", stage: "CODE" });
new MostViewedVideoUploader(app, "MostViewedVideoUploader-PROD", { stack: "content-api", stage: "PROD" });
