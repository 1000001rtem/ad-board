package ru.eremin.ad.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.eremin.ad.board.config.filter.UserInfoFilter;

@Configuration
@Profile("!test")
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeExchange()
            .pathMatchers("/api/v1/ad/find**", "/api/v1/category/all")
            .permitAll()
            .anyExchange()
            .authenticated()
            .and()
            .addFilterAfter(new UserInfoFilter(), SecurityWebFiltersOrder.LAST)
            .csrf()
            .disable()
            .oauth2ResourceServer()
            .jwt();
        return httpSecurity.build();
    }

}
