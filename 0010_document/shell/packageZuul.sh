cd ~/hekele
git pull

cd 7100_zuul/              && mvn clean install && cd ..
mv -f 7100_zuul/target/spring-zuul.jar                           ~/app/

cd ~/app

