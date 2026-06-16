package com.jsa.common;

/**
 * 业务状态码枚举（见 docs/04 接口文档 1.3 错误码约定）。
 * code 为业务码，httpStatus 为建议的 HTTP 状态码。
 */
public enum ResultCode {

    SUCCESS(200, 200, "success"),
    BAD_REQUEST(400, 400, "参数或业务错误"),
    UNAUTHORIZED(401, 401, "未登录或登录失败"),
    FORBIDDEN(403, 403, "权限不足"),
    NOT_FOUND(404, 404, "资源不存在"),

    LOGIN_FAILED(1001, 401, "用户名或密码错误"),
    RECORD_NOT_FOUND(1002, 404, "打卡记录不存在"),
    RECORD_NOT_PENDING(1003, 400, "该记录已审核，不可重复操作"),
    USERNAME_EXISTS(1004, 409, "用户名已存在"),

    INTERNAL_ERROR(500, 500, "服务器内部错误");

    private final int code;
    private final int httpStatus;
    private final String message;

    ResultCode(int code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
