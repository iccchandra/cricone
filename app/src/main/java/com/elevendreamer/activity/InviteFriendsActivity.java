package com.elevendreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
import com.elevendreamer.R;
import com.elevendreamer.utils.SessionManager;
import com.elevendreamer.databinding.ActivityInviteFriendsBinding;

import org.json.JSONObject;

import static com.elevendreamer.APICallingPackage.Config.APKURL;

public class InviteFriendsActivity extends AppCompatActivity implements ResponseManager {
    InviteFriendsActivity  activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;
    String ReferralCode,UserName;
    ActivityInviteFriendsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this, R.layout.activity_invite_friends);

        context = activity = this;
        initViews();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        sessionManager = new SessionManager();
        ReferralCode = sessionManager.getUser(context).getReferral_code();
        UserName = sessionManager.getUser(context).getName();

        binding.tvUniqueCode.setText(ReferralCode);

        binding.tvInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Your friend " + UserName +" invited you to the" +
                        " 11Dreamer Fantasy App. " +
                        "Where you earn real money." +
                        "Download the app using this " +
                        "link:- "+APKURL+" and Enter this unique Code:- " +
                        ReferralCode+" And create your account.");

                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share"));
            }
        });

        binding.tvMyFriendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, InvitedFriendListActivity.class);
                startActivity(i);
            }
        });

    }

    public void initViews(){
        binding.head.tvHeaderName.setText(getResources().getString(R.string.invite_frnds));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

    }

    @Override
    public void onError(Context mContext, String type, String message) {

    }
}
