package com.elevendreamer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.View;

import com.elevendreamer.APICallingPackage.Class.APIRequestManager;
import com.elevendreamer.APICallingPackage.Interface.ResponseManager;
//import com.elevenbaazigars.CashfreePackage.CashfreeActivity;
import com.elevendreamer.R;
import com.elevendreamer.utils.SessionManager;
import com.elevendreamer.databinding.ActivityPaymentOptionBinding;

import org.json.JSONObject;

import static com.elevendreamer.APICallingPackage.Class.Validations.ShowToast;

public class PaymentOptionActivity extends AppCompatActivity implements ResponseManager {
    PaymentOptionActivity activity;
    Context context;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    SessionManager sessionManager;
    String IntentFinalAmount;
    ActivityPaymentOptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_option);
        context = activity = this;
        initViews();
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        IntentFinalAmount = getIntent().getStringExtra("FinalAmount");
        binding.tvPaymentFinalAmount.setText("₹ " + IntentFinalAmount);

        binding.RLPaytmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast(context, "Not Available in Demo Version");
               /* if (IntentFinalAmount.equals("")) {
                    ShowToast(context, "Please Select Correct Amount");
                } else {
                    Intent i = new Intent(activity, PaytmActivity.class);
                    i.putExtra("FinalAmount", IntentFinalAmount);
                    startActivity(i);
                }*/

            }
        });

        binding.RLPayUMoneyPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast(context, "Not Available in Demo Version");
                /*if (IntentFinalAmount.equals("")) {
                    ShowToast(context, "Please Select Correct Amount");
                } else {
                    Intent i = new Intent(activity, PayUMoneyPaymentActivity.class);
                    i.putExtra("FinalAmount", IntentFinalAmount);
                    startActivity(i);
                }*/

            }
        });
        binding.RLCashFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast(context, "Not Available in Demo Version");
                /*if (IntentFinalAmount.equals("")) {
                    ShowToast(context, "Please Select Correct Amount");
                } else {
                    Intent i = new Intent(activity, CashfreeActivity.class);
                    i.putExtra("FinalAmount", IntentFinalAmount);
                    startActivity(i);
                }*/

            }
        });

        binding.RLTrakNPayPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowToast(context, "Not Available in Demo Version");
                /*if (IntentFinalAmount.equals("")) {
                    ShowToast(context, "Please Select Correct Amount");
                } else {
                    Intent i = new Intent(activity, TrakNPayActivity.class);
                    i.putExtra("FinalAmount", IntentFinalAmount);
                    startActivity(i);
                }*/

            }
        });



    }

    public void initViews() {
        binding.head.tvHeaderName.setText(getResources().getString(R.string.pymnt_opt));
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
