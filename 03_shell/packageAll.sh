cd ~/hekele
git pull

cd 10_parent/    && mvn clean install && cd ..
cd 11_base/      && mvn clean install && cd ..
cd 7010_eureka/  && mvn clean install && cd ..
cd 7020_config/  && mvn clean install && cd ..
cd 7030_monitor/ && mvn clean install && cd ..
cd 8040_data/    && mvn clean install && cd ..

mv -f 7010_eureka/target/spring-eureka.jar ~/app/
mv -f 7020_config/target/spring-config.jar ~/app/
mv -f 7030_monitor/target/spring-monitor.jar ~/app/
mv -f 8040_data/target/data-exec.jar ~/app/

cd ~/app

