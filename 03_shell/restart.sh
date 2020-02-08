ps -ef|grep hekele.jar | awk '{print $2}' | while read pid
do
  kill -9 $pid
done

nohup java  -jar /root/app/hekele.jar >> /root/app/hekele.log 2>&1 &

