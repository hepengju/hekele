cd ~/hekele
git pull

cd 8060_weixin/      && mvn clean install && cd ..

mv -f 8060_weixin/target/weixin-service.jar          ~/app/

cd ~/app

