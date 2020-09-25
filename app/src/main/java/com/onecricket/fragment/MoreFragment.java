package com.onecricket.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.onecricket.APICallingPackage.Config;
import com.onecricket.activity.HomeActivity;
import com.onecricket.activity.InviteFriendsActivity;
import com.onecricket.activity.LoginActivity;
import com.onecricket.activity.MainActivity;
import com.onecricket.activity.WebviewAcitivity;
import com.onecricket.databinding.FragmentMoreBinding;

import static android.content.Context.MODE_PRIVATE;


public class MoreFragment extends Fragment {


    HomeActivity activity;
    Context context;

    FragmentMoreBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(inflater, container, false);

        context = activity = (HomeActivity)getActivity();


        binding.RLMoreInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, InviteFriendsActivity.class);
                startActivity(i);
            }
        });

       /* binding.RLFantasyPointSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","FANTASY POINT SYSTEM");
                i.putExtra("URL", Config.FANTASYPOINTSYSTEMURL);
                startActivity(i);
            }
        });*/
        binding.RLMoreHowToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","HOW TO PLAY");
                i.putExtra("URL",Config.HOWTOPLAYURL);
                startActivity(i);
            }
        });
        binding.RLMoreAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","ABOUT US");
                i.putExtra("URL",Config.ABOUTUSURL);
                startActivity(i);
            }
        });
        binding.RLMoreHelpDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","HELP DESK");
                i.putExtra("URL",Config.HELPDESKURL);
                startActivity(i);
            }
        });
        binding.RLPRICING.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","PRICING");
                i.putExtra("URL",Config.PRICING);
                startActivity(i);
            }
        });
        binding.RLMoreLegality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","LEGALITY");
                i.putExtra("URL",Config.LEGALITY);
                startActivity(i);
            }
        });
      /*  binding.RLMoreRefundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","REFUND POLICY");
                i.putExtra("URL",Config.REFUNDPOLICY);
                startActivity(i);
            }
        });
        binding.RLMoreWithdrawPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, WebviewAcitivity.class);
                i.putExtra("Heading","WITHDRAW POLICY");
                i.putExtra("URL",Config.WITHDRAWPOLICY);
                startActivity(i);
            }
        });
*/
        binding.RLLogout.setOnClickListener(view -> {
            logoutUser();
        });
        return binding.getRoot();
    }

    private void logoutUser() {
        SharedPreferences loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();

        LoginManager.getInstance().logOut();
        loginPrefsEditor.clear();
        loginPrefsEditor.apply();
        Auth.GoogleSignInApi.revokeAccess(HomeActivity.mGoogleApiClient).setResultCallback(status -> { });
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}
