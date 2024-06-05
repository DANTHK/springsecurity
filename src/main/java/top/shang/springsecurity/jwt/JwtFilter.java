package top.shang.springsecurity.jwt;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtFilter implements WebFilter {

    @Override
    public @Nonnull Mono<Void> filter(@Nonnull ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        if (path.contains("auth"))
            return chain.filter(exchange);
        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        }
        String prefix = "Bearer ";
        if (!StringUtils.startsWithIgnoreCase(auth, prefix)) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials"));
        }
        String token = auth.replace(prefix, "");
        exchange.getAttributes().put("token", token);
        return chain.filter(exchange);
    }
}
