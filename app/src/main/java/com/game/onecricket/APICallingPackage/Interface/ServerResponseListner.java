package com.game.onecricket.APICallingPackage.Interface;

import org.json.JSONObject;


public interface ServerResponseListner
{
    public void onSucess(JSONObject response, String type, String message);
    public void onError(String error, String type);
}
