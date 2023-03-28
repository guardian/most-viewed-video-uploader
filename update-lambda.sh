#!/usr/bin/env bash

# update-lambda.sh STAGE

set -e

test -z $1 && echo 'Stage missing' && exit 1

STAGE=$1

my_dir=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)

sbt assembly

jar_file=$(echo $my_dir/target/scala-2.13/most-viewed-video-uploader*.jar)

aws lambda update-function-code \
  --function-name most-viewed-video-uploader-$STAGE \
  --region eu-west-1 \
  --profile capi \
  --zip-file fileb://$jar_file