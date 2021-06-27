package ru.eremin.ad.board.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.eremin.ad.board.route.handler.AdHandler;
import ru.eremin.ad.board.route.handler.CategoryHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfiguration {

    @Bean
    RouterFunction<ServerResponse> categoryRoute(final CategoryHandler handler) {
        return route()
            .path("/api/v1/category", builder ->
                builder
                    .GET("/all", accept(MediaType.APPLICATION_JSON), handler::findAll)
                    .POST("/create", accept(MediaType.APPLICATION_JSON), handler::create))
            .build();
    }

    @Bean
    RouterFunction<ServerResponse> adRoute(final AdHandler handler) {
        return route()
            .path("/api/v1/ad", builder ->
                builder
                    .GET("/find-by-category", accept(MediaType.APPLICATION_JSON), handler::findByCategory)
                    .POST("/create", accept(MediaType.APPLICATION_JSON), handler::create)
                    .POST("/update", accept(MediaType.APPLICATION_JSON), handler::update)
                    .POST("/upgrade", accept(MediaType.APPLICATION_JSON), handler::upgrade))
            .build();
    }
}
