package ru.eremin.ad.board.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@TestConfiguration
@Order(1)
public class TestSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeExchange()
            .anyExchange()
            .permitAll()
            .and()
            .csrf()
            .disable();
        return httpSecurity.build();
    }


}