package top.shang.springsecurity.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.shang.springsecurity.exceptions.CommonException;
import top.shang.springsecurity.jwt.JwtAuthenticationManager;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private final JwtAuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getAttribute("token");
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, token))
                .doOnError(
                        t -> AccessDeniedException.class.isAssignableFrom(t.getClass()),
                        t -> log.error("Error {} {}, tried to access {}", t.getMessage(), token, exchange.getRequest().getURI()))
                .onErrorMap(
                        CommonException.class,
                        t -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,  "Unauthorized"))
                .map(SecurityContextImpl::new);
    }
}
