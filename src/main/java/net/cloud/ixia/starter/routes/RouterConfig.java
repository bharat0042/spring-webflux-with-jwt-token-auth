package net.cloud.ixia.starter.routes;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;

import net.cloud.ixia.starter.hello.HelloWorldHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {
  private final String API_V1 = "/api/v1";

  @Bean
  RouterFunction<ServerResponse> basicRoutes(HelloWorldHandler helloWorldHandler) {
    return RouterFunctions.nest(
        path(API_V1), RouterFunctions.route(GET("/hello"), helloWorldHandler::helloWorld));
  }
}
