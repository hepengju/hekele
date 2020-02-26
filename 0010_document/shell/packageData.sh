cd ~/hekele
git pull

cd 9010_data/data-api      && mvn clean install && cd ../..
cd 9010_data/data-service  && mvn clean install && cd ../..

mv -f 9010_data/data-service/target/data-service.jar ~/app/

cd ~/app

