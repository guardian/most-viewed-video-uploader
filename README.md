## Deploying

This project isn't deployable by any of our usual routes at this time, so when your changes are merged into the main 
branch, check that out locally.

You'll need some credentials loaded into your console under the capi profile. Get those from Janus.

You'll find an `update-lambda.sh` script in the project's root directory. Running this will run the build in sbt and 
upload the resulting .jar file to AWS.

For example, run

`$ ./update-lambda.sh PROD`

which, if successful, and after a brief delay after packaging, will output something resembling

```
[info] Packaging /path/to/your/local/most-viewed-video-uploader/target/scala-2.12/most-viewed-video-uploader-assembly-0.1-SNAPSHOT.jar ...
[info] Done packaging.
[success] Total time: 12 s, completed 10-Oct-2022 16:32:41
{
    "FunctionName": "most-viewed-video-uploader-PROD",
    .
    .
    .
}
```
## Temporarily

After some major dependency bumps, the .jar file is too big for the `update-lambda` script to upload and therefore it needs to be manually uploaded to S3 and then picked up by lambda. 


At this point just confirm that all is well by checking the last updated details of the function in the AWS console
and keep an eye on logs for signs of problems. You can also query the ophan logs for evidence of the requests this
lambda makes e.g. search for ophan logs with "/api/video/mostviewed" in the message, and for this application's 
ophan API key.
