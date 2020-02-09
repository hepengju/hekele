appName=spring-config.jar

ps -ef | grep $appName | awk '{print $2}' | while read pid
do
  kill $pid
  sleep 2
done

nohup java  -jar -Xms256M -Xmx512M $appName --eureka.instance.ip-address=101.132.97.183 >> /dev/null 2>&1 &

tail -f ~/app/log/spring-config/spring-config.log

