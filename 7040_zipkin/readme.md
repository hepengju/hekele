## 说明
最新版本的SpringCloud移除了zipkin server的支持, 建议直接使用

## 参考
- [Spring的官方说明](https://cloud.spring.io/spring-cloud-sleuth/reference/html/#zipkin-stream-span-consumer)
> We recommend using Zipkin’s native support for message-based span sending.
> Starting from the Edgware release, the Zipkin Stream server is deprecated. 
> In the Finchley release, it got removed.

- [openZipkin](https://zipkin.io)
```
docker run -d --name zipkin -p 7040:9411 openzipkin/zipkin
```