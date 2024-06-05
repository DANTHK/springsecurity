package top.shang.springsecurity.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import top.shang.springsecurity.exceptions.CommonException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(CommonException.class)
    protected ProblemDetail handleNotFound(CommonException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(ex.getCode()), ex.getMessage());
    }

}
