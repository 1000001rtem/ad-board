package ru.eremin.ad.board.config.filter;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static ru.eremin.ad.board.input.route.RouteConfiguration.EMAIL_HEADER;
import static ru.eremin.ad.board.input.route.RouteConfiguration.FIRST_NAME_HEADER;
import static ru.eremin.ad.board.input.route.RouteConfiguration.LAST_NAME_HEADER;

@Slf4j
public class UserInfoFilter implements WebFilter {

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        var request = Optional.ofNullable(exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization")
            )
            .map(it -> it.substring(7))
            .map(this::parseToken)
            .map(it -> this.mutateRequest(exchange.getRequest(), extractClaims(it)))
            .orElse(exchange.getRequest());

        return chain.filter(exchange.mutate().request(request).build());
    }

    private ServerHttpRequest mutateRequest(final ServerHttpRequest request, final Map<String, Object> claims) {
        return request.mutate().headers(headers -> {
            headers.add(EMAIL_HEADER, claims.get("email").toString());
            headers.add(FIRST_NAME_HEADER, claims.get("given_name").toString());
            headers.add(LAST_NAME_HEADER, claims.get("family_name").toString());
        }).build();
    }

    private Map<String, Object> extractClaims(JWT token) {
        try {
            return token.getJWTClaimsSet()
                .getClaims();
        } catch (ParseException e) {
            throw new RuntimeException("Something with token");
        }
    }

    private JWT parseToken(String token) {
        try {
            return JWTParser.parse(token);
        } catch (ParseException e) {
            log.debug("Empty token {}", token);
        }
        return null;
    }
}
