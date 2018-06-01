package liuns.model.common.dto;

import java.io.Serializable;

/**
 * SOA接口响应公用类
 *
 * @param <T>
 */
public class ResponseDTO<T> implements Serializable {

    private static final long serialVersionUID = 8127071370063482835L;

    private int code;

    private String msg;

    private T data;

    /**
     * 默认请求成功响应代码
     */
    private static final int DEFAULT_SUCCESS_CODE = 200;

    /**
     * 默认请求失败响应代码
     */
    private static final int DEFAULT_FAIL_CODE = 500;

    public static <T> ResponseDTO<T> success(T data) {
        ResponseDTO<T> response = new ResponseDTO<T>();
        response.setCode(DEFAULT_SUCCESS_CODE);
        response.setData(data);
        return response;
    }

    public static <T> ResponseDTO<T> fail(String msg) {
        ResponseDTO<T> response = new ResponseDTO<T>();
        response.setCode(DEFAULT_FAIL_CODE);
        response.setMsg(msg);
        return response;
    }

    public static <T> ResponseDTO<T> fail(int code, String msg) {
        ResponseDTO<T> response = new ResponseDTO<T>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
