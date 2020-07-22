package cn.com.diancan.encapsulation;


import java.io.Serializable;

/**
 * 通用 ResultUtil 返回值封装类
 *
 * @Author created by weilkss on 2019/5/9 0009 9:37
 */
public class Result<T> implements Serializable {
    private Integer status;
    private String message;
    private T data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    /**
     * 返回成功 success
     */
    public static Result success(Object object) {
        Result result = new Result();
        result.setStatus(200);
        result.setMessage("成功");
        result.setData(object);
        return result;
    }

    public static Result success() {
        return Result.success(null);
    }

    /**
     * 返回失败 error
     */
    public static Result error(Integer code, String data, String message) {
        Result result = new Result();
        result.setStatus(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result error(Integer code, String message) {
        return Result.error(code, null, message);
    }

    public static Result error() {
        return Result.error(500, null, "失败");
    }
}