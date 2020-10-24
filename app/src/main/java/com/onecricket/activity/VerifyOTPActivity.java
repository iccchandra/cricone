package com.onecricket.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.Bean.UserDetails;
import com.onecricket.R;
import com.onecricket.utils.SessionManager;
import com.onecricket.databinding.ActivityVerifyOtpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static com.onecricket.APICallingPackage.Class.Validations.ShowToast;
import static com.onecricket.APICallingPackage.Config.LOGIN;
import static com.onecricket.APICallingPackage.Config.RESENDOTP;
import static com.onecricket.APICallingPackage.Config.VERIFYOTP;
import static com.onecricket.APICallingPackage.Constants.LOGINTYPE;
import static com.onecricket.APICallingPackage.Constants.RESENDOTPTYPE;
import static com.onecricket.APICallingPackage.Constants.VERIFYOTPTYPE;

public class VerifyOTPActivity extends AppCompatActivity implements ResponseManager {
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    Context context;
    VerifyOTPActivity activity;
    String OTP;
    String IntentNumber,IntentUserId,IntentPassword,IntentActivity;
    private static CountDownTimer countDownTimer;
    //Auto Login
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    SessionManager sessionManager;
    ActivityVerifyOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = activity = this;
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        initView();
        sessionManager = new SessionManager();

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        Intent o = getIntent();
        IntentNumber = o.getStringExtra("Number");
        IntentUserId = o.getStringExtra("UserId");
        IntentPassword = o.getStringExtra("Password");
        IntentActivity= o.getStringExtra("Activity");





        binding.tvOtpSendTo.setText("OTP sent to "+IntentNumber);


        countDownTimer =  new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                //  tvOtpTimer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                binding.tvOtpTimer.setText("Didn't receive the OTP? Request for a new one in "+ String.format("%d:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
            public void onFinish() {
                binding.tvOtpResend.setVisibility(View.VISIBLE);
                binding.tvOtpTimer.setVisibility(View.GONE);
            }

        }.start();

        binding.etOtp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.etOtp1.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.etOtp2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        binding.etOtp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.etOtp2.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.etOtp3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        binding.etOtp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.etOtp3.getText().toString().length() == 1)     //size as per your requirement
                {
                    binding.etOtp4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        binding.etOtp4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.etOtp4.getText().toString().length() == 1)     //size as per your requirement
                {
                    OTP = GetOTP();
                    callVerifyOTPApi(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });


        binding.tvVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OTP  = GetOTP();

                callVerifyOTPApi(true);
                countDownTimer.cancel();

            }
        });

        binding.tvOtpResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callResendOTPApi(true);

                binding.tvOtpTimer.setVisibility(View.VISIBLE);
                binding.tvOtpResend.setVisibility(View.GONE);
                countDownTimer =  new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        //tv_Timer.setText("Resend OTP in: " + millisUntilFinished / 1000);
                        binding.tvOtpTimer.setText("Didn't receive the OTP? Request for a new one in "+ String.format("%d:%d sec",
                                TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }
                    public void onFinish() {
                        binding.tvOtpResend.setVisibility(View.VISIBLE);
                        binding.tvOtpTimer.setVisibility(View.GONE);
                    }

                }.start();

            }
        });

    }

    public void initView(){


        binding.head.imBack.setOnClickListener(view -> {
            try {
                countDownTimer.cancel();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finish();
        });
        binding.head.tvHeaderName.setText("VERIFY OTP");


    }

    public String GetOTP(){
        String GETOTP = "";
        String Otp1 = binding.etOtp1.getText().toString();
        String Otp2 = binding.etOtp2.getText().toString();
        String Otp3 = binding.etOtp3.getText().toString();
        String Otp4 = binding.etOtp4.getText().toString();

        if (Otp1.equals("")){
            ShowToast(context,"Enter OTP");
        }
        else if (Otp2.equals("")){
            ShowToast(context,"Enter OTP");
        }else if (Otp3.equals("")){
            ShowToast(context,"Enter OTP");
        }else if (Otp4.equals("")){
            ShowToast(context,"Enter OTP");
        }
        else {
            GETOTP = Otp1+Otp2+Otp3+Otp4;
        }

        return GETOTP;
    }


    private void callVerifyOTPApi(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(VERIFYOTP,
                    createRequestJson(), context, activity, LOGINTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", IntentNumber);
            jsonObject.put("otp", OTP);
            jsonObject.put("user_id", IntentUserId);
            jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void callResendOTPApi(boolean isShowLoader) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id",IntentUserId);
            apiRequestManager.callAPI(RESENDOTP,
                    jsonObject, context, activity, RESENDOTPTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callLoginApi(boolean isShowLoader) {
        try {

            apiRequestManager.callAPI(LOGIN,
                    createRequestJsonLogin(), context, activity, LOGINTYPE,
                    isShowLoader,responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJsonLogin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", IntentNumber);
            jsonObject.put("password", IntentPassword);
            jsonObject.put("type", "Normal");
            jsonObject.put("token", FirebaseInstanceId.getInstance().getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        ShowToast(context,message);
        // ShowToast(context,"type:"+type);

        if (type.equals(VERIFYOTPTYPE)) {
            callLoginApi(true);
        }
        else if (type.equals(LOGINTYPE)){

            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.commit();
            try {
                UserDetails userDetails = new UserDetails();
                userDetails.setUser_id(result.getString("user_id"));
                userDetails.setName(result.getString("name"));
                userDetails.setMobile(result.getString("mobile"));
                userDetails.setEmail(result.getString("email"));
                userDetails.setType(result.getString("type"));
                userDetails.setVerify(result.getString("verify"));
                userDetails.setReferral_code(result.getString("referral_code"));
                userDetails.setImage(result.getString("image"));
                if (result.has("token")) {
                    String token = result.getString("token");
                    System.out.println(token);
                    userDetails.setToken(token);
                }

                sessionManager.setUser(context,userDetails);
            }
            catch (Exception e){
                e.printStackTrace();
            }


            Intent i = new Intent(activity, HomeActivity.class);
            startActivity(i);
            finish();
        }
        else if (type.equals(RESENDOTPTYPE)){
        }
    }



    @Override
    public void onError(Context mContext, String type, String message) {
        if (type.equals(VERIFYOTP)){
            ShowToast(context,"Invalid OTP");
        }
        else if (type.equals(LOGINTYPE)) {
            ShowToast(context,"Number Verified Successfully. Please Login to Continue");
            Intent i = new Intent(activity, LoginActivity.class);
            startActivity(i);
        }
        else if (type.equals(RESENDOTPTYPE)){
            ShowToast(context,message);
        }

    }
}
