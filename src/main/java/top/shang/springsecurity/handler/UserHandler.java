package top.shang.springsecurity.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.shang.springsecurity.service.UserService;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserHandler {

    private final UserService userService;


    public Mono<ServerResponse> users(ServerRequest request) {
        return userService.users()
                .collectList()
                .flatMap(users -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(users))
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> user(ServerRequest request) {
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return userService.user(id)
                .flatMap(userResp -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userResp))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
