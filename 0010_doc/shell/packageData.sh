cd ~/hekele
git pull

cd 8040_data/data-api      && mvn clean install && cd ../..
cd 8040_data/data-service  && mvn clean install && cd ../..

mv -f 8040_data/data-service/target/data-service.jar ~/app/

cd ~/app

