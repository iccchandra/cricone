package com.onecricket.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.onecricket.R;


public class CommonProgressDialog {

    public static AlertDialog getProgressDialog(Context context) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view     = inflater.inflate(R.layout.default_progress_dialog, null);
        builder.setCancelable(false);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        return dialog;
    }
}
