package com.onecricket.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.R;
import com.onecricket.utils.SessionManager;
import com.onecricket.databinding.ActivityMyAccountBinding;

import org.json.JSONException;
import org.json.JSONObject;

import static com.onecricket.APICallingPackage.Class.Validations.ShowToast;
import static com.onecricket.APICallingPackage.Config.MYACCOUNT;
import static com.onecricket.APICallingPackage.Constants.MYACCOUNTTYPE;

public class MyAccountActivity extends AppCompatActivity implements ResponseManager {

    MyAccountActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;

    String TotalBalance,Deposited,Winnings,Bonus,PanStatus,AadhaarStatus;

    ActivityMyAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_account);
        context = activity = this;
        initViews();
        sessionManager = new SessionManager();

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        binding.tvAddBalance.setOnClickListener(view -> {
            Intent i = new Intent(activity, AddCashActivity.class);
            startActivity(i);
        });
        callMyAccount(true);

        binding.RLMyRecentTransactions.setOnClickListener(view -> {
            Intent i = new Intent(activity,MyTransactionActivity.class);
            startActivity(i);
        });

        binding.convertCoins.setOnClickListener(view -> {
            showCoinsConversionAlert(this);
        });

    }
    public void initViews() {

        binding.head.tvHeaderName.setText("Wallet");
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void callMyAccount(boolean isShowLoader) {
        try {
            apiRequestManager.callAPIWithAuthorization(MYACCOUNT,
                    createRequestJson(), context, activity, MYACCOUNTTYPE,
                    isShowLoader, responseManager,sessionManager.getUser(context).getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        try{
            TotalBalance = result.getString("total_amount");
            Deposited = result.getString("credit_amount");
            Winnings = result.getString("winning_amount");
            Bonus = result.getString("bonous_amount");
            AadhaarStatus = result.getString("aadhar_status");
            PanStatus = result.getString("pan_status");


           // binding.tvTotalBalance.setText("₹ "+TotalBalance);
            binding.tvDepositedAmount.setText("₹ "+Deposited);
          //  binding.tvWinningAmount.setText(Winnings);
            binding.tvBonusAmount.setText(Bonus);



    } catch (JSONException e) {
        e.printStackTrace();
    }

    }



    @Override
    public void onError(Context mContext, String type, String message) {

    }

    private void showCoinsConversionAlert(Context context) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_coins, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button submit = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button cancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        cancel.setOnClickListener(view -> dialogBuilder.dismiss());
        submit.setOnClickListener(view -> {
            // DO SOMETHINGS
            dialogBuilder.dismiss();
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

}
