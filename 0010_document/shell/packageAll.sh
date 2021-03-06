cd ~/hekele
git pull

cd 1000_parent/        && mvn clean install && cd ..
cd 1010_base/base-nodb && mvn clean install && cd ../..
cd 1010_base/base-all  && mvn clean install && cd ../..

cd 7010_eureka/            && mvn clean install && cd ..
cd 7020_config/            && mvn clean install && cd ..
cd 7030_monitor/           && mvn clean install && cd ..
cd 7040_hystrix_dashboard/ && mvn clean install && cd ..
cd 7050_turbine/           && mvn clean install && cd ..
cd 7100_zuul/              && mvn clean install && cd ..

cd 9010_data/data-api      && mvn clean install && cd ../..
cd 9010_data/data-service  && mvn clean install && cd ../..
cd 9020_weixin/      && mvn clean install && cd ..
cd 9090_demo/demo-api      && mvn clean install && cd ../..
cd 9090_demo/demo-service  && mvn clean install && cd ../..



mv -f 7010_eureka/target/spring-eureka.jar                       ~/app/
mv -f 7020_config/target/spring-config.jar                       ~/app/
mv -f 7030_monitor/target/spring-monitor.jar                     ~/app/
mv -f 7040_hystrix_dashboard/target/spring-hystrix-dashboard.jar ~/app/
mv -f 7050_turbine/target/spring-turbine.jar                     ~/app/
mv -f 7100_zuul/target/spring-zuul.jar                           ~/app/

mv -f 9010_data/data-service/target/data-service.jar ~/app/
mv -f 9020_weixin/target/weixin-service.jar          ~/app/
mv -f 9090_demo/demo-service/target/demo-service.jar ~/app/



cd ~/app

