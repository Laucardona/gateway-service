package com.carvajal.gateway.config;

import com.carvajal.gateway.filter.JwtAuthFilter;
import com.carvajal.gateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class RouteConfig {

    @Value("${microservices.favorites-url}")
    private String favoritesUrl;

    @Value("${microservices.productos-url}")
    private String productosUrl;

    @Value("${microservices.historico-url}")
    private String historicoUrl;

    @Value("${microservices.notifications-url}")
    private String notificationsUrl;

    private final JwtAuthFilter jwtAuthFilter;

    public RouteConfig(JwtUtil jwtUtil) {
        this.jwtAuthFilter = new JwtAuthFilter(jwtUtil);
    }

    @Bean
    public RouterFunction<ServerResponse> favoritesRoute() {
        return route("favorites-service")
                .route(path("/api/v1/favorites/**"), http())
                .before(uri(favoritesUrl))
                .filter(jwtAuthFilter.filter())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productosRoute() {
        return route("productos-service")
                .route(path("/api/products/**"), http())
                .before(uri(productosUrl))
                .filter(jwtAuthFilter.filter())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> historicoRoute() {
        return route("historico-service")
                .route(path("/api/historico/**"), http())
                .before(uri(historicoUrl))
                .filter(jwtAuthFilter.filter())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationsRoute() {
        return route("notifications-service")
                .route(path("/api/notifications/**"), http())
                .before(uri(notificationsUrl))
                .filter(jwtAuthFilter.filter())
                .build();
    }
}