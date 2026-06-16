package com.jsa.common;

/**
 * 自定义业务异常（见 docs/02 第5节）。
 * Service 层通过 throw new BusinessException(ResultCode.XXX) 抛出，
 * 由 GlobalExceptionHandler 统一转换为 Result，Controller 不写 try-catch。
 */
public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
