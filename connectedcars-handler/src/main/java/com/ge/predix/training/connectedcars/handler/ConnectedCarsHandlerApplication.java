package com.ge.predix.training.connectedcars.handler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource(
{
    "classpath*:META-INF/spring/ext-util-scan-context.xml",
    "classpath*:META-INF/spring/predix-rest-client-scan-context.xml",
    "classpath*:META-INF/spring/predix-websocket-client-scan-context.xml",
    "classpath*:META-INF/spring/timeseries-bootstrap-scan-context.xml"
       
})
public class ConnectedCarsHandlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectedCarsHandlerApplication.class, args);
	}
}