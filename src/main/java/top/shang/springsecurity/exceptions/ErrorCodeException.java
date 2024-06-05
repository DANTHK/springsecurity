package top.shang.springsecurity.exceptions;

public class ErrorCodeException {

    public static final CommonException BAD_REQUEST = CommonException.of(400, "Bad Request");
    public static final CommonException UNAUTHORIZED = CommonException.of(401, "Unauthorized");
    public static final CommonException FORBIDDEN = CommonException.of(403, "Forbidden");
    public static final CommonException NOT_FOUND = CommonException.of(404, "Not found");
    public static final CommonException USER_NOT_FOUND = CommonException.of(404, "User not found");
    public static final CommonException METHOD_NOT_ALLOWED = CommonException.of(405, "Method Not Allowed");
    public static final CommonException NOT_ACCEPTABLE = CommonException.of(406, "Not Acceptable");
    public static final CommonException INTERNAL_SERVER_ERROR = CommonException.of(500, "Internal Server Error");
}
