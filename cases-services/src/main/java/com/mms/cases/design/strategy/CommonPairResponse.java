package com.mms.cases.design.strategy;

public class CommonPairResponse<T, R> {


    /**
     * 提示信息.
     */
    private T msg;

    /**
     * 具体的内容.
     */
    private R data;

    public CommonPairResponse(T msg, R data) {
        this.msg = msg;
        this.data = data;
    }

    public CommonPairResponse(T msg) {
        this.msg = msg;
    }

    public static CommonPairResponse success(String s, Object data){
        return new CommonPairResponse(s,data);
    }

    public static CommonPairResponse failure(String s){
        return new CommonPairResponse(s);
    }
}
