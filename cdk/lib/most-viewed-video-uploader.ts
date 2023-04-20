import type {GuStackProps} from "@guardian/cdk/lib/constructs/core";
import {GuStack} from "@guardian/cdk/lib/constructs/core";
import type {App} from "aws-cdk-lib";
import {aws_ssm, Duration} from "aws-cdk-lib";
import {GuScheduledLambda} from "@guardian/cdk";
import {Architecture, Runtime} from "aws-cdk-lib/aws-lambda";
import {Schedule} from "aws-cdk-lib/aws-events";
import {Effect, PolicyStatement} from "aws-cdk-lib/aws-iam";

export class MostViewedVideoUploader extends GuStack {
  constructor(scope: App, id: string, props: GuStackProps) {
    super(scope, id, props);

    const urgentAlarmTopicArn = aws_ssm.StringParameter.fromStringParameterName(this, "urgent-alarm-arn", "/account/content-api-common/alarms/urgent-alarm-topic");
    const nonUrgentAlarmTopicArn = aws_ssm.StringParameter.fromStringParameterName(this, "non-urgent-alarm-arn", "/account/content-api-common/alarms/non-urgent-alarm-topic");

    new GuScheduledLambda(this, "MVV", {
      app: "most-viewed-video-uploader",
      architecture: Architecture.ARM_64,
      description: "Most-viewed video uploader",
      fileName: "most-viewed-video-uploader.jar",
      functionName: `most-viewed-video-uploader-v2-${this.stage}`,
      handler: "com.gu.contentapi.Lambda::handleRequest",
      memorySize: 512,
      monitoringConfiguration: {
        snsTopicName: urgentAlarmTopicArn.stringValue,
        toleratedErrorPercentage: 5,
        numberOfMinutesAboveThresholdBeforeAlarm: 1,
      },
      initialPolicy: [
         new PolicyStatement({
           effect: Effect.ALLOW,
           actions: ["s3:GetObject"],
           resources: [`arn:aws:s3:::content-api-config/most-viewed-video-uploader/${this.stage}/*`],
         }),
         new PolicyStatement({
           effect: Effect.ALLOW,
           actions: ["kinesis:PutRecords"],
           resources: [`arn:aws:kinesis:${this.region}:${this.account}:stream/content-api-most-viewed-video-${this.stage}`]
         }),
      ],
      rules: [
        {
          schedule: Schedule.rate(Duration.hours(1)),
        }
      ],
      runtime: Runtime.JAVA_11,
      timeout: Duration.minutes(1)
    })
  }
}
