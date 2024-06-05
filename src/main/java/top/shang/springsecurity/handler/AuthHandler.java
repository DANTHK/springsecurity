package top.shang.springsecurity.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import top.shang.springsecurity.dto.CreateUserDTO;
import top.shang.springsecurity.dto.LoginDTO;
import top.shang.springsecurity.dto.RefreshTokenDTO;
import top.shang.springsecurity.dto.TokenDTO;
import top.shang.springsecurity.entity.User;
import top.shang.springsecurity.service.UserService;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandler {

    private final UserService userService;

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginDTO> dtoMono = request.bodyToMono(LoginDTO.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.login(dto), TokenDTO.class));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<CreateUserDTO> dtoMono = request.bodyToMono(CreateUserDTO.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.create(dto), User.class));
    }

    public Mono<ServerResponse> signIn(ServerRequest request) {
        Mono<LoginDTO> dtoMono = request.bodyToMono(LoginDTO.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userService.signIn(dto), RefreshTokenDTO.RefreshTokenResp.class));
    }

    public Mono<ServerResponse> refresh(ServerRequest request) {
        Mono<RefreshTokenDTO.RefreshTokenReq> dtoMono = request.bodyToMono(RefreshTokenDTO.RefreshTokenReq.class);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userService.refresh(dto), RefreshTokenDTO.RefreshTokenResp.class));
    }
}
