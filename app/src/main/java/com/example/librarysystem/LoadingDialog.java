package com.example.librarysystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

class LoadingDialog {
    Activity activity;
    AlertDialog alertDialog;
    boolean cancelable;
    String message;
    String title;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LoadingDialog(Activity activity) {
        this.activity = activity;
        message = "";
        title = "";
        cancelable = false;
    }
    public void setCancelable(){
        cancelable = true;
    }

    public void startloadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_loading,null));
        builder.setCancelable(cancelable);
        alertDialog = builder.create();
        alertDialog.setMessage(message);
        alertDialog.setTitle(title);
        alertDialog.show();

    }

    public void dialogDismiss(){
        alertDialog.dismiss();
    }
}
