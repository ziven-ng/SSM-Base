package me.ziven.ssm.common.bean;

import com.alibaba.fastjson.JSON;

/**
 * Created by ziven on 2017/6/28.
 */
public class Result<T> {
    private boolean ok = true;
    private int code = 0;
    private T data;


    private Result(boolean ok, int code, T data) {
        this.ok = ok;
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", ok=" + ok +
                ", data=" + data +
                '}';
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true,0, data);
    }

    public static Result<String> success(String msg) {
        return new Result<>(true,0, msg);
    }

    public static Result error(String msg) {
        return new Result<>(false, -1, msg);
    }

    public static Result error(int code, String msg) {
        return new Result<>(false, code, msg);
    }
}
