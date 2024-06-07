package top.shang.springsecurity.handler;

import jakarta.annotation.Nonnull;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import top.shang.springsecurity.exceptions.CommonException;
import top.shang.springsecurity.utils.JsonUtil;

import java.net.URI;

@Component
@Order(-1)
public class FilterExceptionHandler implements ErrorWebExceptionHandler {
    @Override
    public @Nonnull Mono<Void> handle(@Nonnull ServerWebExchange exchange, @Nonnull Throwable ex) {
        HttpStatus httpStatus;
        if (ex instanceof CommonException e) {
            httpStatus = HttpStatus.valueOf(e.getCode());
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        String instance = exchange.getRequest().getPath().value();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());
        problemDetail.setInstance(URI.create(instance));
        DataBuffer buffer = response.bufferFactory()
                .allocateBuffer(1024)
                .write(JsonUtil.toJsonStr(problemDetail).getBytes());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeAndFlushWith(Mono.just(ByteBufMono.just(buffer)));
    }
}
