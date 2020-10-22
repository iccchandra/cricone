package com.onecricket.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.APICallingPackage.retrofit.APIService;
import com.onecricket.APICallingPackage.retrofit.ApiClient;
import com.onecricket.APICallingPackage.retrofit.SubmitToken;
import com.onecricket.Bean.UserDetails;
import com.onecricket.R;
import com.onecricket.databinding.ActivityEditProfileBinding;
import com.onecricket.utils.CommonProgressDialog;
import com.onecricket.utils.SessionManager;
import com.onecricket.utils.crypto.AlertDialogHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.onecricket.APICallingPackage.Class.Validations.ShowToast;
import static com.onecricket.APICallingPackage.Config.EDITPROFILE;
import static com.onecricket.APICallingPackage.Config.VIEWPROFILE;
import static com.onecricket.APICallingPackage.Constants.EDITPROFILETYPE;
import static com.onecricket.APICallingPackage.Constants.VIEWPROFILETYPE;

public class EditProfileActivity extends AppCompatActivity implements ResponseManager {

    private static final String TAG = "EditProfileActivity";
    private static final String TYPE_UPDATE_FCM_TOKEN = "update_token";
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;

    Context context;
    EditProfileActivity activity;
    SessionManager sessionManager;
    String name,mobile,email,image,teamName,favriteTeam,dob,gender
            ,address,city,pincode,state,country;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;

    ActivityEditProfileBinding binding;
    private AlertDialog progressAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        context = activity = this;
        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        initViews();
        sessionManager = new SessionManager();
        Log.d(TAG, sessionManager.getUser(context).getToken());
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        callViewProfile(true);

        binding.etEditDob.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);




            DOBListener dobListener = new DOBListener();
            DatePickerDialog dialog = new DatePickerDialog(activity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    dobListener, year, month, day);
            Calendar minimumCalendar = Calendar.getInstance();
            Calendar maximumCalendar = Calendar.getInstance();
            minimumCalendar.set(Calendar.YEAR, minimumCalendar.get(Calendar.YEAR) - 18);
            maximumCalendar.set(Calendar.YEAR, maximumCalendar.get(Calendar.YEAR) - 100);
            dialog.getDatePicker().setMaxDate(minimumCalendar.getTimeInMillis());
            dialog.getDatePicker().setMinDate(maximumCalendar.getTimeInMillis());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            dialog.show();
        });

        binding.tvEditMale.setOnClickListener(view -> {
            gender = "male";
            binding.tvEditMale.setBackgroundResource(R.drawable.roundbutton);
            binding.tvEditFeMale.setBackgroundResource(R.drawable.roundbutton_hover_back);
        });
        binding.tvEditFeMale.setOnClickListener(view -> {
            gender = "female";
            binding.tvEditFeMale.setBackgroundResource(R.drawable.roundbutton);
            binding.tvEditMale.setBackgroundResource(R.drawable.roundbutton_hover_back);
        });

        binding.tvEditUpdateProfile.setOnClickListener(view -> {
            if (isDateOfBirthSelected()) {
                callEditProfile(true);
            }
            else {
                AlertDialogHelper alertDialogHelper = AlertDialogHelper.getInstance();
                if (!alertDialogHelper.isShowing()) {
                    alertDialogHelper.showAlertDialog(context, getString(R.string.dob_needed__alert_title), getString(R.string.dob_needed__alert_Message));
                }
            }
        });
    }

    private boolean isDateOfBirthSelected() {
        return binding.etEditDob.getText().toString().length() > 0;
    }

    public void initViews() {
        binding.head.tvHeaderName.setText(getResources().getString(R.string.personal_details));
        binding.head.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.etEditCountry.setText(getResources().getString(R.string.India));
        binding.etEditCountry.setEnabled(false);
        binding.etEditCountry.setFocusable(false);
    }

    private void callViewProfile(boolean isShowLoader) {
        try {

            apiRequestManager.callAPIWithAuthorization(VIEWPROFILE,
                    createRequestJson(), context, activity, VIEWPROFILETYPE,
                    isShowLoader,responseManager, sessionManager.getUser(context).getToken());

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

    private void callEditProfile(boolean isShowLoader) {
        try {

            apiRequestManager.callAPIWithAuthorization(EDITPROFILE,
                    createEditProfileJson(), context, activity, EDITPROFILETYPE,
                    isShowLoader,responseManager, sessionManager.getUser(context).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createEditProfileJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            jsonObject.put("name", binding.etEditName.getText().toString());
            jsonObject.put("mobile", binding.etEditMobile.getText().toString());
            jsonObject.put("favriteTeam", binding.etEditFavouriteTeam.getText().toString());
            jsonObject.put("dob", binding.etEditDob.getText().toString());
            jsonObject.put("gender", gender);
            jsonObject.put("address", binding.etEditAddress.getText().toString());
            jsonObject.put("city", binding.etEditCity.getText().toString());
            jsonObject.put("state", binding.etEditState.getText().toString());
            jsonObject.put("country", binding.etEditCountry.getText().toString());
            jsonObject.put("pincode", binding.etEditPincode.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        if (type.equals(EDITPROFILETYPE)){

            ShowToast(context,message);
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.commit();
            UserDetails userDetails = new UserDetails();
            userDetails.setUser_id(sessionManager.getUser(context).getUser_id());
            userDetails.setName(binding.etEditName.getText().toString());
            userDetails.setMobile(binding.etEditMobile.getText().toString());
            userDetails.setEmail(sessionManager.getUser(context).getEmail());
            userDetails.setType(sessionManager.getUser(context).getType());
            userDetails.setReferral_code(sessionManager.getUser(context).getReferral_code());
            userDetails.setImage(sessionManager.getUser(context).getImage());
            userDetails.setAddress(binding.etEditAddress.getText().toString());
            userDetails.setCity(binding.etEditCity.getText().toString());
            userDetails.setPincode(binding.etEditPincode.getText().toString());
            userDetails.setState(binding.etEditState.getText().toString());
            userDetails.setToken(sessionManager.getUser(context).getToken());
            String dateOfBirth = binding.etEditDob.getText().toString();
            userDetails.setDateOfBirth(dateOfBirth);
            userDetails.setVerify("1");
            sessionManager.setUser(context, userDetails);
            callUpdateTokenAPI();
        }
        else if (type.equals(TYPE_UPDATE_FCM_TOKEN)) {
            gotoHomeScreen();
        }
        else {
            try {
                name = result.getString("name");
                mobile = result.getString("mobile");
                email = result.getString("email");
                image = result.getString("image");
                teamName = result.getString("teamName");
                favriteTeam = result.getString("favriteTeam");
                dob = result.getString("dob");
                gender = result.getString("gender");
                address = result.getString("address");
                city = result.getString("city");
                pincode = result.getString("pincode");
                state = result.getString("state");
                country = result.getString("country");




                if (name.equals("")) {

                } else {
                    binding.etEditName.setText(name);
                }
                binding.etEditEmail.setText(email);
                binding.etEditMobile.setText(mobile);
                binding.etEditFavouriteTeam.setText(favriteTeam);
                binding.etEditDob.setText(dob);
                binding.etEditAddress.setText(address);
                binding.etEditCity.setText(city);
                binding.etEditState.setText(state);
                binding.etEditPincode.setText(pincode);

                if (gender.equals("male")) {
                    binding.tvEditMale.setBackgroundResource(R.drawable.roundbutton);
                    binding.tvEditFeMale.setBackgroundResource(R.drawable.roundbutton_hover_back);
                } else if (gender.equals("female")) {
                    binding.tvEditFeMale.setBackgroundResource(R.drawable.roundbutton);
                    binding.tvEditMale.setBackgroundResource(R.drawable.roundbutton_hover_back);
                }

                if (!email.equals("")) {
                    binding.etEditEmail.setEnabled(false);
                    binding.etEditEmail.setFocusable(false);
                }
                if (!mobile.equals("")) {
                    binding.etEditMobile.setEnabled(false);
                    binding.etEditMobile.setFocusable(false);
                }


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        }


    @Override
    public void onError(Context mContext, String type, String message) {
        Log.d(TAG, message);
        if (type.equals(TYPE_UPDATE_FCM_TOKEN)) {
            gotoHomeScreen();
        }
    }



    class DOBListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(0);
                cal.set(year, month, dayOfMonth, 0, 0, 0);
                Date chosenDate = cal.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String df_medium_us_str = dateFormat.format(chosenDate);
                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, month, dayOfMonth-1);
                String dayOfWeek = simpledateformat.format(date);
                binding.etEditDob.setText(df_medium_us_str );
            }

        }
    }
    public interface DataBindingComponent {
    }

    private void gotoHomeScreen() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private APIService apiService;
    private void initialiseRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL + ":4040")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(APIService.class);
    }

    private void callUpdateTokenAPI() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        initialiseRetrofit();
        String userId = sessionManager.getUser(context).getUser_id();
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Observable<SubmitToken> observable = apiService.submitToken(userId, fcmToken);
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::onSuccessResponse, this::onErrorResponse);

    }

    private void onSuccessResponse(SubmitToken submitToken) {
        dismissProgressDialog(progressAlertDialog);
        gotoHomeScreen();

    }

    private void onErrorResponse (Throwable throwable){
        dismissProgressDialog(progressAlertDialog);
        if (throwable.getMessage() != null) {
            Log.d(TAG, throwable.getMessage());
        }

    }

    private void dismissProgressDialog(androidx.appcompat.app.AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }


}
