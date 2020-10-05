package com.onecricket.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.Bean.BeanBanner;
import com.onecricket.BuildConfig;
import com.onecricket.R;
import com.onecricket.adapter.AdapterHomeBanner;
import com.onecricket.databinding.ActivityHomeBinding;
import com.onecricket.fragment.InProgressMatchesFragment;
import com.onecricket.fragment.MoreFragment;
import com.onecricket.fragment.MyContestFragment;
import com.onecricket.fragment.ProfileFragment;
import com.onecricket.fragment.UpcomingMatchesFragment;
import com.onecricket.location.AppConstants;
import com.onecricket.location.model.LocationServiceManager;
import com.onecricket.location.model.LocationServiceManagerImpl;
import com.onecricket.utils.CommonProgressDialog;
import com.onecricket.utils.NetworkState;
import com.onecricket.utils.SessionManager;
import com.onecricket.utils.crypto.AlertDialogHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.onecricket.APICallingPackage.Class.Validations.ShowToast;
import static com.onecricket.APICallingPackage.Config.APKNAME;
import static com.onecricket.APICallingPackage.Config.APKURL;
import static com.onecricket.APICallingPackage.Config.HOMEBANNER;
import static com.onecricket.APICallingPackage.Config.STATE_STATUS;
import static com.onecricket.APICallingPackage.Config.UPDATEAPP;
import static com.onecricket.APICallingPackage.Constants.HOMEBANNERTYPE;
import static com.onecricket.APICallingPackage.Constants.UPDATEAPPTYPE;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
                                                               ResponseManager,
                                                               LocationServiceManager.Listener
{

    private static final String PREFS_KEY_CURRENT_DATE = "key_current_date";
    private int[] tabIcons = {
            R.drawable.home_icon,
            R.drawable.contest_icon,
            R.drawable.profile_icon,
            R.drawable.more_icon
    };
    private Context context;
    private HomeActivity activity;
    public static HomeActivity homeActivity;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    //Auto Login
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private SharedPreferences.Editor loginPrefsEditor;
    public static GoogleApiClient mGoogleApiClient;
    Typeface LatoBold, LatoRegular, Ravenscroft;
    public static SessionManager sessionManager;

    int progressStatus = 0;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog pDialog;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    Dialog dialog;

    ActivityHomeBinding binding;
    AdapterHomeBanner adapterHomeBanner;
    private ImageView[] dots1;
    private int dotscount1;
    private androidx.appcompat.app.AlertDialog progressAlertDialog;
    private LocationServiceManager locationServiceManager;
    private AlertDialogHelper alertDialogHelper;

    public static HomeActivity getInstance() {
        return homeActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        context = activity = this;
        homeActivity = this;
        sessionManager = new SessionManager();
        if (sessionManager.getUser(context).getToken() != null) {
            Log.d("HomeActivity-Token", sessionManager.getUser(context).getToken());
        }

        locationServiceManager = new LocationServiceManagerImpl(this);
        locationServiceManager.setListener(this);
        locationServiceManager.startLocationService();

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);

        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        alertDialogHelper = AlertDialogHelper.getInstance();
        Animation shake = AnimationUtils.loadAnimation(activity, R.anim.shake);
        binding.imNotification.startAnimation(shake);
        binding.VPBanner.setNestedScrollingEnabled(false);
        binding.bonus.setOnClickListener(view -> {
            if (NetworkState.isNetworkAvailable(context)) {
                if (isBonusAvailable()) {
                    callBonusAPI(false);
                }
                else {
                    showBonusAlreadyCreditedAlert();
                }
            }
            else {
                if (!alertDialogHelper.isShowing()) {
                    alertDialogHelper.showAlertDialog(context, getString(R.string.internet_error_title), getString(R.string.no_internet_message));
                }
            }
        });


        binding.imNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, NotificationActivity.class);
                startActivity(i);
            }
        });
        binding.imHomewallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, MyAccountActivity.class);
                startActivity(i);
            }
        });

        Ravenscroft = Typeface.createFromAsset(this.getAssets(), "Ravenscroft.ttf");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);


        setupTabIcons1();

//        replaceFragment(new FragmentFixtures());
        if (isTokenAvailable(context, sessionManager)) {
            onTokenAvailable();
        }
        else {
            logout();
        }
    }

    private void onTokenAvailable() {
        callHomeBanner(false);
        callBlockedStates(false);
        replaceFragment(new UpcomingMatchesFragment());

        binding.tablayout.addTab(binding.tablayout.newTab().setText("Upcoming"));
        binding.tablayout.addTab(binding.tablayout.newTab().setText("In-Progress"));
        binding.tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new UpcomingMatchesFragment());
                    binding.head.setVisibility(View.VISIBLE);
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new InProgressMatchesFragment());
                    binding.head.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                                                  @Override
                                                  public void onTabSelected(TabLayout.Tab tab) {
                                                      int tabIconColor = ContextCompat.getColor(activity, R.color.tabtextselected);
                                                      tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

                                                      if (tab.getPosition() == 0) {
//                            replaceFragment(new FragmentFixtures());
                                                          binding.tablayout.setVisibility(View.VISIBLE);
                                                          replaceFragment(new UpcomingMatchesFragment());
                                                          binding.head.setVisibility(View.VISIBLE);
                                                          binding.bonus.setVisibility(View.VISIBLE);
                                                          binding.RLHomeBanner.setVisibility(View.VISIBLE);
                                                      } else if (tab.getPosition() == 1) {
                                                          binding.tablayout.setVisibility(View.GONE);
                                                          binding.RLHomeBanner.setVisibility(View.GONE);
                                                          binding.bonus.setVisibility(View.GONE);
                                                          replaceFragment(new MyContestFragment());
                                                          binding.head.setVisibility(View.VISIBLE);
                                                      } else if (tab.getPosition() == 2) {
                                                          binding.tablayout.setVisibility(View.GONE);
                                                          binding.RLHomeBanner.setVisibility(View.GONE);
                                                          binding.bonus.setVisibility(View.GONE);
                                                          replaceFragment(new ProfileFragment());
                                                          binding.head.setVisibility(View.GONE);
                                                      } else {
                                                          binding.tablayout.setVisibility(View.GONE);
                                                          binding.RLHomeBanner.setVisibility(View.GONE);
                                                          binding.bonus.setVisibility(View.GONE);
                                                          replaceFragment(new MoreFragment());
                                                          binding.head.setVisibility(View.VISIBLE);
                                                      }
                                                  }

                                                  @Override
                                                  public void onTabUnselected(TabLayout.Tab tab) {

                                                      int tabIconColor = ContextCompat.getColor(activity, R.color.tabtextunselected);
                                                      tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                                                  }

                                                  @Override
                                                  public void onTabReselected(TabLayout.Tab tab) {

                                                  }
                                              }
        );

        //Uncomment Below Line for In-App-Update
        callCheckUpdateVersion(false);
        if (getCurrentDate() > getSavedDate(loginPreferences)) {
            enableBonusButton();
        } else {
            disableBonusButton();
        }
    }

    private boolean isBonusAvailable = false;
    private void disableBonusButton() {
        setBonusAvailable(false);
        binding.bonus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bonus_disabled));
    }

    private void enableBonusButton() {
        setBonusAvailable(true);
        binding.bonus.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bonus));
    }

    private static final String TYPE_BONUS = "Bonus";

    private void dismissProgressDialog(androidx.appcompat.app.AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }


    private void showBonusCreditedAlert() {
        new FancyGifDialog.Builder(this)
                .setTitle("Your Daily Bonus Credited.")
                .setMessage("Looks like you can earn more.")
                .setPositiveBtnText("Invite Friends")
                .setPositiveBtnBackground("#FF4081")
                .setNegativeBtnText("Close")
                .setNegativeBtnBackground("#FF4081")
                .setGifResource(R.drawable.bonus_credited)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(() -> {
                    Intent i = new Intent(HomeActivity.this, InviteFriendsActivity.class);
                    startActivity(i);
                })
                .build();
    }

    private void showBonusAlreadyCreditedAlert() {
        new FancyGifDialog.Builder(this)
                .setTitle("Your Daily Bonus Already Credited.")
                .setMessage("Looks like you can earn more.")
                .setPositiveBtnText("Invite Friends")
                .setPositiveBtnBackground("#FF4081")
                .setNegativeBtnText("Close")
                .setNegativeBtnBackground("#FF4081")
                .setGifResource(R.drawable.common_gif)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(() -> {
                    Intent i = new Intent(HomeActivity.this, InviteFriendsActivity.class);
                    startActivity(i);
                })
                .build();
    }


    private void callBonusAPI(boolean isShowLoader) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://13.232.85.74:4040/daily/update?userid=" +
                sessionManager.getUser(context).getUser_id() +
                "&token=" +
                sessionManager.getUser(context).getToken();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            dismissProgressDialog(progressAlertDialog);
            saveCurrentDateInPrefs(loginPreferences);
            disableBonusButton();
            showBonusCreditedAlert();
        }, error -> {
            dismissProgressDialog(progressAlertDialog);
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void saveCurrentDateInPrefs(SharedPreferences loginPreferences) {
        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.putInt(PREFS_KEY_CURRENT_DATE, getCurrentDate());
        editor.apply();
    }

    private int getCurrentDate() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    private int getSavedDate(SharedPreferences loginPreferences) {
        if (loginPreferences.contains(PREFS_KEY_CURRENT_DATE)) {
            return loginPreferences.getInt(PREFS_KEY_CURRENT_DATE, 0);
        }
        return 0;
    }

    private void callHomeBanner(boolean isShowLoader) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            apiRequestManager.callAPIWithAuthorization(HOMEBANNER,
                    jsonObject,
                    this,
                    this,
                    HOMEBANNERTYPE,
                    isShowLoader,
                    responseManager,
                    sessionManager.getUser(context).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static final String TYPE_STATES = "states";
    private void callBlockedStates(boolean isShowLoader) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", "blocked");
            apiRequestManager.callAPIWithAuthorization(STATE_STATUS,
                    jsonObject,
                    this,
                    this,
                    TYPE_STATES,
                    isShowLoader,
                    responseManager,
                    sessionManager.getUser(context).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void callCheckUpdateVersion(boolean isShowLoader) {
        try {
            apiRequestManager.callAPIWithAuthorization(UPDATEAPP,
                    createRequestJson(), context, activity, UPDATEAPPTYPE,
                    isShowLoader, responseManager, sessionManager.getUser(context).getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (type.equals(UPDATEAPPTYPE)) {
            try {
                String Note = result.getString("note");
                String OldV = result.getString("old_version");
                String NewV = result.getString("new_version");
                String maintenance_status = result.getString("maintenance_status");
                //0 ok
                //1 Under maintenance
                String MobileVName = BuildConfig.VERSION_NAME;
                if (maintenance_status.equals("0")) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                    ab.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            finish();
                        }
                    });
                    AlertDialog alert = ab.create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.setMessage("App is under maintenance. Please check after some time.");
                    alert.show();
                } else if (!MobileVName.equals(NewV)) {
                    Dialog(Note, "Update", "What's new");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (type.equals(HOMEBANNERTYPE)) {
            onBannerResult(result);
        }
    }

    private void onBannerResult(JSONObject result) {
        try {
            JSONArray jsonArray = result.getJSONArray("data");
            if (jsonArray.length() > 0) {
                binding.RLHomeBanner.setVisibility(View.VISIBLE);
                List<BeanBanner> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BeanBanner>>() {}.getType());
                adapterHomeBanner = new AdapterHomeBanner(beanHomeFixtures, HomeActivity.this);
                binding.VPBanner.setAdapter(adapterHomeBanner);
                BannerDotView();
            } else {
                binding.RLHomeBanner.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterHomeBanner.notifyDataSetChanged();
    }

    public void BannerDotView() {
        dotscount1 = adapterHomeBanner.getCount();
        dots1 = new ImageView[dotscount1];

        for (int i = 0; i < dotscount1; i++) {

            dots1[i] = new ImageView(HomeActivity.this);
            dots1[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            binding.SliderDots1.addView(dots1[i], params);

        }

        dots1[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.active_dot));

        binding.VPBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount1; i++) {
                    dots1[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.non_active_dot));
                }
                dots1[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public void onError(Context mContext, String type, String message) {
        String mes = message;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file..");
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    public void Dialog(String UpdateNote, String UpdateorInstall, String WhatsnewHead) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);

        final TextView tv_DClose = dialog.findViewById(R.id.tv_DClose);
        final TextView tv_UpdateNote = dialog.findViewById(R.id.tv_UpdateNote);
        final TextView tv_UpdateApp = dialog.findViewById(R.id.tv_UpdateApp);
        final TextView tv_WhatsNewHead = dialog.findViewById(R.id.tv_WhatsNewHead);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        tv_UpdateNote.setText(UpdateNote);
        tv_UpdateApp.setText(UpdateorInstall);
        tv_WhatsNewHead.setText(WhatsnewHead);

        tv_DClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tv_UpdateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_UpdateApp.getText().toString().equals("Update")) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        int result;
                        List<String> listPermissionsNeeded = new ArrayList<>();
                        for (String p : permissions) {
                            result = ContextCompat.checkSelfPermission(activity, p);
                            if (result != PackageManager.PERMISSION_GRANTED) {
                                listPermissionsNeeded.add(p);
                            }
                        }
                        if (!listPermissionsNeeded.isEmpty()) {
                            ActivityCompat.requestPermissions(activity,
                                    listPermissionsNeeded.toArray(
                                            new String[listPermissionsNeeded.size()]), 100);

                        } else {
                            new DownloadFileFromURL().execute(APKURL);
                        }
                    } else {
                        new DownloadFileFromURL().execute(APKURL);
                    }
                } else {


                    File apk = new File(Environment.getExternalStorageDirectory().toString()
                            + "/" + APKNAME);

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (Build.VERSION.SDK_INT >= 23) {
                            Uri photoURI = FileProvider.getUriForFile(activity,
                                    activity.getApplicationContext().getPackageName()
                                            + ".provider", apk);

                            intent.setDataAndType(photoURI,
                                    "application/vnd.android.package-archive");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {

                            intent.setDataAndType(Uri.fromFile(apk),
                                    "application/vnd.android.package-archive");

                        }
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


            }
        });
    }

    private void setupTabIcons1() {
        binding.tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.tabtextselected), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.tabtextunselected), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.tabtextunselected), PorterDuff.Mode.SRC_IN);
        binding.tabs.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.tabtextunselected), PorterDuff.Mode.SRC_IN);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {

        int selectedTabPosition = binding.tabs.getSelectedTabPosition();
        if (selectedTabPosition == 1 || selectedTabPosition == 2) {
            binding.tabs.selectTab(binding.tabs.getTabAt(0), true);
        }
        else {
            /*AlertDialog.Builder ab = new AlertDialog.Builder(activity);
            ab.setPositiveButton("Exit", (dialog, id) -> {
                finishAffinity();
                finish();
            });
            AlertDialog alert = ab.create();
            alert.setMessage("Are you sure, you want to exit?");
            alert.show();*/
            showLogoutAlert();

        }

    }

    private void showLogoutAlert() {
        new FancyGifDialog.Builder(this)
                .setTitle("Are you sure, you want to exit?")
                //.setMessage("This is a granny eating chocolate dialog box. This library is used to help you easily create fancy gify dialog.")
                .setPositiveBtnText("Exit")
                .setPositiveBtnBackground("#FF4081")
                .setGifResource(R.drawable.common_gif)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(() -> {
                    finishAffinity();
                    finish();
                })
                .build();
    }

    private void showInvalidTokenAlert() {
        new FancyGifDialog.Builder(this)
                .setTitle("Something went wrong. Please login to continue.")
                .setPositiveBtnText("Logout")
                .setPositiveBtnBackground("#FF4081")
                .setGifResource(R.drawable.common_gif)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(this::logout)
                .build();
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                status -> {

                });
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
        else if (requestCode == AppConstants.LOCATION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationServiceManager.getLastKnownLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isBonusAvailable() {
        return isBonusAvailable;
    }

    public void setBonusAvailable(boolean bonusAvailable) {
        isBonusAvailable = bonusAvailable;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.dismiss();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream(
                        Environment.getExternalStorageDirectory().toString() + "/" + APKNAME);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/" + APKNAME;
            ShowToast(activity, "" + imagePath);
            Dialog("Your Update is ready to install", "Install", "Install App");
        }

    }

    public void callTab() {
        try {
            binding.tabs.getTabAt(1).select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface DataBindingComponent {
    }

    @Override
    public void onGpsDisabled() {

    }

    @Override
    public void onGpsEnabled() {
        requestLocationPermissions();
    }

    @Override
    public void askGpsLocationPermission(Exception e) {
        try {
            ResolvableApiException rae = (ResolvableApiException) e;
            rae.startResolutionForResult((Activity) context, AppConstants.GPS_REQUEST);
        } catch (IntentSender.SendIntentException sie) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.GPS_REQUEST) {
                requestLocationPermissions();
            }
        }
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                                              new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                                                            Manifest.permission.ACCESS_COARSE_LOCATION }, AppConstants.LOCATION_REQUEST);
        }
        else {
            locationServiceManager.getLastKnownLocation();
        }
    }


    @Override
    public void onLocationSuccess(double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault()); //it is Geocoder
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            String locality = address.get(0).getLocality();
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            Toast.makeText(context, locality, Toast.LENGTH_SHORT).show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isTokenAvailable(Context context, SessionManager sessionManager) {
        return sessionManager != null &&
               sessionManager.getUser(context) != null &&
               sessionManager.getUser(context).getToken() != null &&
               sessionManager.getUser(context).getToken().trim().length() > 0;
    }
}


