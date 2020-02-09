appName=demo-service
jarName=$appName.jar

ps -ef | grep $jarName | awk '{print $2}' | while read pid
do
  kill $pid
  sleep 2
done

nohup java  -jar -Xms256M -Xmx512M $jarName --eureka.instance.ip-address=101.132.97.183 >> /dev/null 2>&1 &

tail -f ~/app/log/$appName/$appName.log
