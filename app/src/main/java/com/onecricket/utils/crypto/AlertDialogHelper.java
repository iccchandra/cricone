package com.onecricket.utils.crypto;

import android.content.Context;
import android.graphics.Color;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.onecricket.R;

public class AlertDialogHelper {
    private static final AlertDialogHelper dialogHelper = new AlertDialogHelper();

    public static AlertDialogHelper getInstance() {
        return dialogHelper;
    }

    private AlertDialogHelper() {
    }

    private FlatDialog flatDialog;

    public void showAlertDialog(Context context, String title, String message) {
        flatDialog = new FlatDialog(context);
        flatDialog.setIcon(R.drawable.crying)
                .setTitle(title)
                .setTitleColor(Color.parseColor("#000000"))
                .setSubtitle(message)
                .setSubtitleColor(Color.parseColor("#000000"))
                .setBackgroundColor(Color.parseColor("#a26ea1"))
                .setFirstButtonColor(Color.parseColor("#f18a9b"))
                .setFirstButtonTextColor(Color.parseColor("#000000"))
                .setFirstButtonText("OK")
                .withFirstButtonListner(view -> flatDialog.dismiss())
                .show();
    }

    public void dismissDialog() {
        if (flatDialog != null && flatDialog.isShowing()) {
            flatDialog.dismiss();
        }
    }

    public boolean isShowing() {
        return flatDialog != null && flatDialog.isShowing();
    }
}
