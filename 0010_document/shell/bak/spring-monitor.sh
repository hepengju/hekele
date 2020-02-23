appName=spring-monitor
jarName=$appName.jar
logFile=$HOME/app/log/$appName/$appName.log

ps -ef | grep $jarName | awk '{print $2}' | while read pid
do
  kill $pid
  sleep 2
done

nohup java  -jar -Xms256M -Xmx512M $jarName --eureka.instance.ip-address=101.132.97.183 >> /dev/null 2>&1 &

if [ ! -f logFile ]; then
  sleep 2
fi
tail -f ~/app/log/$appName/$appName.log

