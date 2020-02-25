#!/bin/bash

############################################################
# 日期：2020-02-10
# 作者：何鹏举
# 说明：根据传入参数的jar包名称，重启SpringBoot应用并查看日志
############################################################
if [[ -z $1 ]]; then
  echo "Usage: ./restartApp.sh jarName [jenkins]"
  echo "    第二个参数用于jenkins自动打包时, 不自动查看日志"
  exit 1
fi

# 变量名字
scriptName=$0
jarName=$1
appName=${jarName%.*}
logFile=$HOME/app/log/$appName/$appName.log

echo "查看日志命令: tail -f "$logFile

# 找到进程号并关闭应用, 注意需要去掉grep命令和脚本本身命令的进程
pid=`ps -ef | grep $jarName | grep -v grep | grep -v $scriptName | awk '{print $2}'`
if [[ -n $pid ]]; then
  echo "找到应用"$appName"的进程号："$pid"，尝试正常停止应用"
  kill $pid
  sleep 5
fi

pid=`ps -ef | grep $jarName | grep -v grep | grep -v $scriptName | awk '{print $2}'`
if [[ -n $pid ]]; then
  echo "5秒内没有正常停止应用："$appName", 进程号:"$pid"，下面进行强制停止"
  kill -9 $pid
fi

# 启动应用
nohup java -jar -Xms256M -Xmx512M $jarName --eureka.instance.ip-address=101.132.97.183 >> /dev/null 2>&1 &
echo "正在启动应用..."

# jenkins启动的时候不查看日志(简单判断, 第二个参数不为空则是jenkins启动的)
if [[ -n $2 ]]; then
  echo "jenkins启动, 不自动查看日志"
  exit 0
fi

# 查看日志(日志文件不存在（首次启动），则休息下再tail)
if [[ ! -f $logFile ]]; then
  sleep 2
fi
tail -f $logFile
