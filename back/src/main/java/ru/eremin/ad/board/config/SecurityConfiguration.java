package ru.eremin.ad.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeExchange()
            .pathMatchers("/api/v1/ad/find**", "/api/v1/category/all")
            .permitAll()
            .anyExchange()
            .authenticated()
            .and()
            .csrf()
            .disable()
            .oauth2ResourceServer()
            .jwt();
        return httpSecurity.build();
    }
}
