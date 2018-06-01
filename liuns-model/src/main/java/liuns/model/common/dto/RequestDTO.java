package liuns.model.common.dto;

import java.io.Serializable;

/**
 * SOA接口请求公用类
 *
 * @param <T>
 */
public class RequestDTO<T> implements Serializable {

    private static final long serialVersionUID = -2287982778167180600L;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 初始化
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RequestDTO<T> getInstance(T data) {
        RequestDTO<T> request = new RequestDTO<T>();
        request.setData(data);
        return request;
    }
}
