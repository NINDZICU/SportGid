package com.kpfu.khlopunov.sportgid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hlopu on 17.12.2017.
 */

public class ApiResult<TYPE> implements Serializable {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("body")
    @Expose
    private TYPE body;

    public ApiResult(int code, TYPE body) {
        this.code = code;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public TYPE getBody() {
        return body;
    }

    public void setBody(TYPE body) {
        this.body = body;
    }
}
