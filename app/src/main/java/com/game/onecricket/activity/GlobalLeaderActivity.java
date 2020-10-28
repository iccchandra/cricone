package com.game.onecricket.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Data;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.GlobalLeaderResponse;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Last30Day;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Last7Day;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Today;
import com.game.onecricket.R;
import com.game.onecricket.fragment.GlobalLeaderFragment;
import com.game.onecricket.fragment.GlobalLeaderFragment2;
import com.game.onecricket.fragment.GlobalLeaderFragment3;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GlobalLeaderActivity extends AppCompatActivity {

    private static final String TAG = "GlobalLeaderActivity";
    private Toolbar toolbar;
    private AlertDialog progressAlertDialog;
    private SessionManager sessionManager;
    private AlertDialogHelper alertDialogHelper;
    private GlobalLeaderFragment2 lastWeekFragment;
    private GlobalLeaderFragment todayFragment;
    private GlobalLeaderFragment3 lastMonthFragment;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_leader);

        toolbar = findViewById(R.id.toolbar);
        context = this;

        toolbar.setTitle("Global Ranking");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressAlertDialog = CommonProgressDialog.getProgressDialog(this);
        alertDialogHelper = AlertDialogHelper.getInstance();

        sessionManager = new SessionManager();

        initialiseTabs();

        toolbar.setNavigationOnClickListener(view -> finish());

        callGlobalLeaderAPI();
    }

    private void dismissProgressDialog(AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }


    private void callGlobalLeaderAPI() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();

        ApiInterface apiInterface = ApiClient.getClientWithAuthorisation(sessionManager.getUser(context).getToken()).create(ApiInterface.class);

        Observable<GlobalLeaderResponse> observable = apiInterface.getGlobalLeaderBoardList();
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::onSuccessResponse, this::onErrorResponse);
    }

    private void onErrorResponse(Throwable throwable) {
        dismissProgressDialog(progressAlertDialog);
    }

    private void onSuccessResponse(GlobalLeaderResponse globalLeaderResponse) {
        dismissProgressDialog(progressAlertDialog);
        if (globalLeaderResponse.getData() != null) {
            Data data = globalLeaderResponse.getData();
            List<Last7Day> last7DayList = data.getLast7Days();
            List<Today> todayList = data.getTodays();
            List<Last30Day> last30DayList = data.getLast30Days();

            if (todayList != null && todayList.size() > 0) {
                todayFragment.setGlobalLeaderData(todayList);
            }

            if (last7DayList != null && last7DayList.size() > 0) {
                lastWeekFragment.setLastWeekList(last7DayList);
            }

            if (last30DayList != null && last30DayList.size() > 0) {
                lastMonthFragment.setLastMonthData(last30DayList);
            }
        }
    }


    private void sendDataToTabs(JSONObject response) {
        try {
            JSONObject dataJson = response.getJSONObject("data");
            JSONArray todaysObject = dataJson.getJSONArray("todays");
            JSONArray lastWeek = dataJson.getJSONArray("last_7_days");
            JSONArray lastMonth = dataJson.getJSONArray("last_30_days");

            if (lastWeek.length() > 0) {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initialiseTabs() {
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Last Week"));
        tabLayout.addTab(tabLayout.newTab().setText("Last Month"));

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

    private class PagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    todayFragment = new GlobalLeaderFragment();
                    return todayFragment;
                case 1:
                    lastWeekFragment = new GlobalLeaderFragment2();
                    return lastWeekFragment;
                case 2:
                    lastMonthFragment = new GlobalLeaderFragment3();
                    return lastMonthFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
