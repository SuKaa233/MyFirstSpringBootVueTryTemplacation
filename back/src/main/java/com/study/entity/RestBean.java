package com.study.entity;

import lombok.Data;

@Data
public class RestBean<T> {
    private T message;
    private boolean success;
    private int status;

    public RestBean(T message, boolean success, int status) {
        this.message = message;
        this.success = success;
        this.status = status;
    }

    public static <T> RestBean<T> success(){
        return new RestBean<>(null,true,200);
    }
    public static <T> RestBean<T> success(T data){
        return new RestBean<>(data ,true,200);
    }
    public static <T> RestBean<T> failure(int status){
        return new RestBean<>(null, false, status);
    }
    public static <T> RestBean<T> failure(int status,T data){
        return new RestBean<>(data, false, status);
    }
}
