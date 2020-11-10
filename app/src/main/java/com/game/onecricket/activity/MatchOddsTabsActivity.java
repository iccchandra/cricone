package com.game.onecricket.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.material.tabs.TabLayout;
import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.game.onecricket.APICallingPackage.retrofit.state.StateResponse;
import com.game.onecricket.R;
import com.game.onecricket.fragment.ChatRoomFragment;
import com.game.onecricket.fragment.LeaderboardFragment;
import com.game.onecricket.fragment.MatchOddsFragment;
import com.game.onecricket.location.AppConstants;
import com.game.onecricket.location.model.LocationServiceManager;
import com.game.onecricket.location.model.LocationServiceManagerImpl;
import com.game.onecricket.pojo.MatchesInfo;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MatchOddsTabsActivity extends AppCompatActivity implements MatchOddsFragment.Listener,
                                                                        LocationServiceManager.Listener{

    private static final String TAG = "MatchOddsTabsActivity";
    private Toolbar toolbar;
    private MatchesInfo matchesInfo;
    private ChatRoomFragment chatRoomFragment;
    private String title;
    private LocationServiceManager locationServiceManager;
    private AlertDialog progressAlertDialog;
    private Context context;
    private MatchOddsFragment matchOddsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_odds_tabs);

        toolbar = findViewById(R.id.toolbar);
        context = this;

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            matchesInfo = (MatchesInfo) extras.getSerializable("MatchInfo");
            if (matchesInfo != null) {
                Log.d(TAG, String.valueOf(matchesInfo.getId()));
/*
                toolbar.setTitle(matchesInfo.getHomeTeam().substring(0,3).toUpperCase() +
                                 " Vs " +
                                 matchesInfo.getVisitorsTeam().substring(0,3).toUpperCase());
*/

                String homeTeam = matchesInfo.getHomeTeam();
                String visitorTeam = matchesInfo.getVisitorsTeam();
                if (homeTeam.length() > 2) {
                    homeTeam = homeTeam.substring(0,3).toUpperCase();
                }

                if (visitorTeam.length() > 2) {
                    visitorTeam = visitorTeam.substring(0,3).toUpperCase();
                }
                title = homeTeam + " Vs " + visitorTeam;
                String contestName = matchesInfo.getContestName();
                if (contestName != null && contestName.length() > 0) {
                    title = title + "(" + contestName +")";
                }
                toolbar.setTitle(title);
            }
        }
        progressAlertDialog = CommonProgressDialog.getProgressDialog(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

        initialiseLocationService();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Match Odds"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat Room"));
        tabLayout.addTab(tabLayout.newTab().setText("Rankings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initialiseLocationService() {
        locationServiceManager = new LocationServiceManagerImpl(this);
        locationServiceManager.setListener(this);
        locationServiceManager.startLocationService();
    }

    @Override
    public void onBottomSheetExpanded() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onBottomSheetCollapsed() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    bundle.putSerializable("MatchInfo", matchesInfo);
                    matchOddsFragment = new MatchOddsFragment();
                    matchOddsFragment.setListener(MatchOddsTabsActivity.this);
                    matchOddsFragment.setArguments(bundle);
                    return matchOddsFragment;
                case 1:
                    if (chatRoomFragment == null) {
                        chatRoomFragment = new ChatRoomFragment();
                    }
                    return chatRoomFragment;
                case 2:
                    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
                    if (matchesInfo.getContestId() != null && matchesInfo.getContestId().trim().length() > 0) {
                        bundle.putBoolean("IS_PRIVATE_CONTEST", true);
                    }
                    else {
                        bundle.putBoolean("IS_PRIVATE_CONTEST", false);
                    }
                    bundle.putString("F_ID", matchesInfo.getId());
                    bundle.putSerializable("matchesInfo", matchesInfo);
                    leaderboardFragment.setArguments(bundle);
                    return leaderboardFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_odds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            onShareClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onShareClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "I am Gaming on " + title + ". Join me at https://play.google.com/store/apps/details?id=com.game.onecricket");
        intent.setType("text/plain");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
        }
    }

    @Override
    public void onGpsEnabled() {
        requestLocationPermissions();
    }

    @Override
    public void onGpsDisabled() {
    }

    @Override
    public void askGpsLocationPermission(Exception e) {
        askGpsLocation(e);
    }

    private void askGpsLocation(Exception exception) {
        try {
            ResolvableApiException rae = (ResolvableApiException) exception;
            rae.startResolutionForResult((Activity) this, AppConstants.GPS_REQUEST);
        } catch (IntentSender.SendIntentException sie) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onLocationSuccess(double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault()); //it is Geocoder
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            String locality = address.get(0).getAdminArea();
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            if (locality != null && locality.trim().length() > 0) {
                callStateStatusApi(locality);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.GPS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                requestLocationPermissions();
            } else {
                showGpsDisabledAlert(this, "Location not found", "Please enable location service");
            }
        }
    }

    public void showGpsDisabledAlert(Context context, String title, String message) {
        FlatDialog flatDialog = new FlatDialog(context);
        flatDialog.setCancelable(false);
        flatDialog.setCanceledOnTouchOutside(false);
        flatDialog.setIcon(R.drawable.crying)
                .setTitle(title)
                .setTitleColor(Color.parseColor("#000000"))
                .setSubtitle(message)
                .setSubtitleColor(Color.parseColor("#000000"))
                .setBackgroundColor(Color.parseColor("#a26ea1"))
                .setFirstButtonColor(Color.parseColor("#f18a9b"))
                .setFirstButtonTextColor(Color.parseColor("#000000"))
                .setFirstButtonText("Enable Location service")
                .withFirstButtonListner(view -> {
                    flatDialog.dismiss();
                    locationServiceManager.turnGpsOn();
                })
                .show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults)
    {
        if (requestCode == AppConstants.LOCATION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationServiceManager.getLastKnownLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dismissProgressDialog(androidx.appcompat.app.AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }


    private void callStateStatusApi(String locality) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        SessionManager sessionManager = new SessionManager();
        ApiInterface apiInterface = ApiClient.getClientWithAuthorisation(sessionManager.getUser(this).getToken()).create(ApiInterface.class);

        JSONObject inputJSON = new JSONObject();
        try {
            inputJSON.put("status", "blocked");
            inputJSON.put("state", locality);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Observable<StateResponse> observable = apiInterface.sendStateInfo(ApiClient.getRequestBody(inputJSON));
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::onSuccessResponse, this::onErrorResponse);
    }

    private void onErrorResponse(Throwable throwable) {
        dismissProgressDialog(progressAlertDialog);
    }

    private void onSuccessResponse(StateResponse stateResponse) {
        dismissProgressDialog(progressAlertDialog);
        if (stateResponse.getMessage() == null) {
            showAlertDialog(this, "Error", "Something went wrong. Please try again later.");
        }
        else if (stateResponse.getMessage().equalsIgnoreCase("No State Allowed") ) {
            showAlertDialog(this, "Sorry!", "Playing this game is not allowed in this state.");
        }
        else {
            matchOddsFragment.allowPlayingGame();
        }
    }

    public void showAlertDialog(Context context, String title, String message) {
        FlatDialog flatDialog = new FlatDialog(context);
        flatDialog.setCancelable(false);
        flatDialog.setCanceledOnTouchOutside(false);
        flatDialog.setIcon(R.drawable.crying)
                  .setTitle(title)
                  .setTitleColor(Color.parseColor("#000000"))
                  .setSubtitle(message)
                  .setSubtitleColor(Color.parseColor("#000000"))
                  .setBackgroundColor(Color.parseColor("#a26ea1"))
                  .setFirstButtonColor(Color.parseColor("#f18a9b"))
                  .setFirstButtonTextColor(Color.parseColor("#000000"))
                  .setFirstButtonText("OK")
                  .withFirstButtonListner(view -> {
                      flatDialog.dismiss();
                      finish();
                  })
                  .show();

    }

}
