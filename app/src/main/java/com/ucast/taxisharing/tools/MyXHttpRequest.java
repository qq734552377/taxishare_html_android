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

    public static final String SUCCESS = "Success";
    public static final String TIMEOUT = "Timeout";
    public static final String ERROR = "Error";

    public static void postParamsRequest(final Context context, String url, Map<String, String> params,
                                         final MyHttpSucessCallback callback) {

        RequestParams requestParams = new RequestParams(url);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestParams.addBodyParameter(entry.getKey(), entry.getValue());
        }
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseHttpResponseMsg baseHttpResponseMsg = JSON.parseObject(result, BaseHttpResponseMsg.class);
                if (baseHttpResponseMsg.getMsgType().equals(SUCCESS)) {
                    //请求成功
                    if (callback != null)
                        callback.sucess(baseHttpResponseMsg.getData());
                } else if (baseHttpResponseMsg.getMsgType().equals(TIMEOUT)) {



                } else if (baseHttpResponseMsg.getMsgType().equals(ERROR)){
                    MyDialog.showDialog(context, baseHttpResponseMsg.getInfo());
                }

                MyDialog.showDialog(context, result);
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
