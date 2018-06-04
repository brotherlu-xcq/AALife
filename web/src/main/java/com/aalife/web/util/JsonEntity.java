package com.aalife.web.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 *
 * @auther brother lu
 * @date 2018-06-04
 */
@ApiModel
public class JsonEntity<T> implements Serializable {
    @ApiModelProperty
    T data;
    @ApiModelProperty
    private int status = 200;
    @ApiModelProperty
    private String message;

    public JsonEntity(){}
    public JsonEntity(T data){
        this.data = data;
    }
    public JsonEntity(int status, String message){
        this.status = status;
        this.message = message;
    }
    public JsonEntity(T data, int status, String message){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
