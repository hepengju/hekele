cd ~/hekele
git pull

cd 9020_weixin/      && mvn clean install && cd ..

mv -f 9020_weixin/target/weixin-service.jar          ~/app/

cd ~/app

