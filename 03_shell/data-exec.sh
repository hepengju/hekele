appName=data-exec.jar

ps -ef | grep $appName | awk '{print $2}' | while read pid
do
  kill $pid
  sleep 2
done

nohup java  -jar -Xms256M -Xmx512M --eureka.instance.ip-address=101.132.97.183 $appName >> /dev/null 2>&1 &

tail -f ~/app/log/data/data.log
