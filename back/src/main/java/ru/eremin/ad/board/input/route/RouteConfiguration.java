package ru.eremin.ad.board.input.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.eremin.ad.board.input.route.handler.AdHandler;
import ru.eremin.ad.board.input.route.handler.CategoryHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouteConfiguration {

    public static final String EMAIL_HEADER = "email";
    public static final String FIRST_NAME_HEADER = "firstName";
    public static final String LAST_NAME_HEADER = "lastName";

    @Bean
    RouterFunction<ServerResponse> categoryRoute(final CategoryHandler handler) {
        return route()
            .path("/api/v1/category", builder ->
                    builder
                        .GET("/all", accept(MediaType.APPLICATION_JSON), handler::findAll)
//                      .POST("/create", accept(MediaType.APPLICATION_JSON), handler::create)
            )
            .build();
    }

    @Bean
    RouterFunction<ServerResponse> adRoute(final AdHandler handler) {
        return route()
            .path("/api/v1/ad", builder ->
                builder
                    .GET("/find-all-active", accept(MediaType.APPLICATION_JSON), handler::findAllActive)
                    .GET("/find-by-category", accept(MediaType.APPLICATION_JSON), handler::findByCategory)
                    .GET("/find-by-id", accept(MediaType.APPLICATION_JSON), handler::findById)
                    .POST("/create", accept(MediaType.APPLICATION_JSON), handler::create)
                    .PUT("/update", accept(MediaType.APPLICATION_JSON), handler::update)
                    .PUT("/upgrade", accept(MediaType.APPLICATION_JSON), handler::upgrade))
            .build();
    }
}
