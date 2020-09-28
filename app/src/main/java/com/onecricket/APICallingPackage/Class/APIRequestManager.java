package com.onecricket.APICallingPackage.Class;

import android.app.Activity;
import android.content.Context;

import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.APICallingPackage.Interface.ServerResponseListner;
import com.onecricket.APICallingPackage.Interface.VolleyRestClient;
import com.onecricket.R;
import com.onecricket.utils.NetworkState;
import com.onecricket.utils.crypto.AlertDialogHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class APIRequestManager implements ServerResponseListner {



    Context mContext;
    ResponseManager responseManager;
    private VolleyRestClient volleyRestClient;
    private AlertDialogHelper alertDialogHelper;
    public APIRequestManager(Context mContext) {
        this.mContext = mContext;
        alertDialogHelper = AlertDialogHelper.getInstance();
    }


    public void callAPI(String url, JSONObject jsonObject, Context mContext, Activity activity,
                        String type, boolean isShowProgress,
                        ResponseManager responseManager) throws JSONException {
        if (NetworkState.isNetworkAvailable(mContext)) {
            this.responseManager = responseManager;
            volleyRestClient = new VolleyApiCalling();
            volleyRestClient.callRestApi(url, jsonObject, mContext, activity, type,
                    this, isShowProgress, "");
        }
        else {
            if (!alertDialogHelper.isShowing()) {
                alertDialogHelper.showAlertDialog(mContext,
                        mContext.getString(R.string.internet_error_title),
                        mContext.getString(R.string.no_internet_message));
            }
        }

    }

    public void callAPIWithAuthorization(String url,
                                         JSONObject jsonObject,
                                         Context mContext,
                                         Activity activity,
                                         String type,
                                         boolean isShowProgress,
                                         ResponseManager responseManager,
                                         String authorization) throws JSONException {

        if (NetworkState.isNetworkAvailable(mContext)) {
            this.responseManager = responseManager;
            volleyRestClient = new VolleyApiCalling();
            volleyRestClient.callRestApi(url, jsonObject, mContext, activity, type, this, isShowProgress, authorization);
        }
        else {
            if (!alertDialogHelper.isShowing()) {
                alertDialogHelper.showAlertDialog(mContext,
                        mContext.getString(R.string.internet_error_title),
                        mContext.getString(R.string.no_internet_message));
            }
        }
    }


    @Override
    public void onSucess(JSONObject response, String type, String message) {


        //Response only consist data object/array/string

        if (response != null && !response.equals("")) {
            try {

                responseManager.getResult(mContext,type,message,response);

            }
            catch (Exception e){
                e.printStackTrace();
            }
            }



    }

    @Override
    public void onError(String error, String type) {
            responseManager.onError(mContext,type,error);

    }
}
