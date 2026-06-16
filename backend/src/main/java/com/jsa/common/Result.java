package com.jsa.common;

/**
 * 统一响应体（见 docs/02 第4节、docs/04 1.1）。
 * 结构：{ code, message, data }
 */
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /** 成功，带数据 */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /** 成功，带数据与自定义提示 */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /** 成功，无数据 */
    public static Result<Void> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /** 失败，按业务码 */
    public static <T> Result<T> error(ResultCode rc) {
        return new Result<>(rc.getCode(), rc.getMessage(), null);
    }

    /** 失败，按业务码 + 自定义提示 */
    public static <T> Result<T> error(ResultCode rc, String message) {
        return new Result<>(rc.getCode(), message, null);
    }

    /** 失败，自定义码与提示 */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
