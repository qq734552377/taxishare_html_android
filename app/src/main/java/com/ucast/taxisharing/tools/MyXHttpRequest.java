package com.ucast.taxisharing.tools;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.ucast.taxisharing.activity.LoginActivity;
import com.ucast.taxisharing.entity.BaseHttpResponseMsg;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import static com.ucast.taxisharing.R.string.login;


/**
 * Created by pj on 2017/7/12.
 */

public class MyXHttpRequest {
    /*
      * 请求返回的状态码
     */
    public static final String SUCCESS = "Success";
    public static final String TIMEOUT = "Timeout";
    public static final String ERROR = "Error";


    public static final String TOKEN = "token";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String NICKNAME = "nickname";
    public static final String PHONE = "phone";
    public static final String PASSWROD = "passwrod";
    public static final String ROLEID = "roleid";
    public static final String NRIC = "nric";//身份证号码
    public static final String GENDER = "gender";
    public static final String NRICFRONT = "nricfront";//身份证前照
    public static final String NRICBACK = "nricback";//身份证前照
    public static final String AUDITNUMBER = "auditnumber";//审核唯一标号
    public static final String USERSTATUS = "userstatus";//用户状态
    public static final String REGISTERTIME = "registertime";//注册时间


    public static void postParamsRequestNoToken(final Context context, String url, Map<String, String> params,
                                                final MyHttpSucessCallback callback) {

        RequestParams requestParams = new RequestParams(url);

        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestParams.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseHttpResponseMsg baseHttpResponseMsg = JSON.parseObject(result, BaseHttpResponseMsg.class);
                if (baseHttpResponseMsg==null){
                    return;
                }
                if (baseHttpResponseMsg.getMsgType().equals(SUCCESS)) {
                    //请求成功
                    if (callback != null)
                        callback.sucess(baseHttpResponseMsg.getData());
                } else if (baseHttpResponseMsg.getMsgType().equals(TIMEOUT)) {


                } else if (baseHttpResponseMsg.getMsgType().equals(ERROR)) {
                    MyDialog.showDialog(context, baseHttpResponseMsg.getInfo());
                }else {
                    MyDialog.showDialog(context, result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyDialog.showDialog(context, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }

    public static void postParamsRequestWithToken(final Context context, String url, Map<String, String> params,
                                                  final MyHttpSucessCallback callback) {

        RequestParams requestParams = new RequestParams(url);
        requestParams.addHeader("Authorization", "Basic " + SavePasswd.getInstace().get(TOKEN));
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestParams.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseHttpResponseMsg baseHttpResponseMsg = JSON.parseObject(result, BaseHttpResponseMsg.class);
                if (baseHttpResponseMsg==null){
                    return;
                }
                if (baseHttpResponseMsg.getMsgType().equals(SUCCESS)) {
                    //请求成功
                    if (callback != null)
                        callback.sucess(baseHttpResponseMsg.getData());
                } else if (baseHttpResponseMsg.getMsgType().equals(TIMEOUT)) {


                } else if (baseHttpResponseMsg.getMsgType().equals(ERROR)) {
                    MyDialog.showDialog(context, baseHttpResponseMsg.getInfo());
                }else {
                    MyDialog.showDialog(context, result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyDialog.showDialog(context, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });


    }


}
