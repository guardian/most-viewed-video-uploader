## Deploying

This project is built and uploaded via Github Actions and deployed using RiffRaff's continuous deployment configuration.

If you need to manually run or re-run a build you can run the `Build and upload` action. 
If this is done on the `main` branch, RiffRaff should pick it up and deploy it.

After merging main and the deploy going out, just confirm that all is well by checking the last updated details 
of the function in the AWS console and keep an eye on logs for signs of problems. 
You can also query the ophan logs for evidence of the requests this lambda makes e.g. search for ophan 
logs with "/api/video/mostviewed" in the message, and for this application's ophan API key.

Note that the run frequency is once per hour, so you may need to trigger a test run of the lambda after a deployment.

There is no CODE environment here. 

