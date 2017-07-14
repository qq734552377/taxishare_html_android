package com.ucast.taxisharing.entity;

/**
 * Created by pj on 2017/7/12.
 */

public class BaseHttpResponseMsg {
    private String MsgType;
    private String Info ;
    private String Data  ;
    private String Total  ;

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
}
