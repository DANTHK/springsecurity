package top.shang.springsecurity.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends Exception {

    private int code;


    public CommonException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public static CommonException of(int code, String msg) {
        return new CommonException(code, msg);
    }

    public static CommonException of(String msg) {
        return new CommonException(500, msg);
    }

}
