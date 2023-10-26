#!/usr/bin/env bash

# upload Cloudwatch configuration, that was uploaded to S3
echo "Uploading Cloudwatch configuration..."
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/tmp/amazon-cloudwatch-agent.json
# run process in the background, redirect stdin, stdout, stderr to nowhere (/dev/null). nohup redirects input and output to specific file nohup.out
echo "Trying to run application..."

if [ "$DEPLOYMENT_GROUP_NAME" == "billing" ]
then
    nohup java -jar -Dspring.profiles.active=dev /tmp/billing-service.jar > /dev/null 2> /dev/null < /dev/null &
else
    nohup java -jar -Dspring.profiles.active=dev /tmp/order-service.jar > /dev/null 2> /dev/null < /dev/null &
fi
#else
#  echo "No running applications was found for the deployment group name!"
#fi

exit 0
