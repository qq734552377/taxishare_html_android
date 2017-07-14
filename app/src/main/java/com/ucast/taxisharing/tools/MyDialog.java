package com.ucast.taxisharing.tools;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.ucast.taxisharing.R;

/**
 * Created by pj on 2017/7/10.
 */

public class MyDialog {

    public static void showDialog(Context context,String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).setPositiveButton(context.getResources().getString(R
                .string.ok), null).create();
        alertDialog.setTitle(context.getResources().getString(R.string.tishi));
        alertDialog.setMessage(s);
        alertDialog.show();
    }
}
