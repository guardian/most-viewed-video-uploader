import { App } from "aws-cdk-lib";
import { Template } from "aws-cdk-lib/assertions";
import { MostViewedVideoUploader } from "./most-viewed-video-uploader";

describe("The MostViewedVideoUploader stack", () => {
  it("matches the snapshot", () => {
    const app = new App();
    const stack = new MostViewedVideoUploader(app, "MostViewedVideoUploader", { stack: "content-api", stage: "TEST" });
    const template = Template.fromStack(stack);
    expect(template.toJSON()).toMatchSnapshot();
  });
});
