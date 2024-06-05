package top.shang.springsecurity.router;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import top.shang.springsecurity.handler.AuthHandler;
import top.shang.springsecurity.handler.UserHandler;

@Configuration
@Slf4j
public class RouterAuth {

    private static final String PATH = "auth/";

    @Bean
    RouterFunction<ServerResponse> authRouter(AuthHandler handler) {
        return RouterFunctions.route()
                .POST(PATH + "login", handler::login)
                .POST(PATH + "create", handler::create)
                .POST(PATH + "signIn", handler::signIn)
                .POST( "token/refresh", handler::refresh)
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> userRouter(UserHandler handler) {
        return RouterFunctions.route()
                .GET("user/users", handler::users)
                .GET("user/{id}", handler::user)
                .build();
    }
}
