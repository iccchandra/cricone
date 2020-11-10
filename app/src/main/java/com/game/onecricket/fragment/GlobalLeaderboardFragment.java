package com.game.onecricket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.game.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Data;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.GlobalLeaderResponse;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Last30Day;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Last7Day;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Today;
import com.game.onecricket.activity.GlobalLeaderActivity;
import com.google.android.material.tabs.TabLayout;
import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.R;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GlobalLeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";
    private AlertDialog progressAlertDialog;
    private Context context;

    private AlertDialogHelper alertDialogHelper;
    private SessionManager sessionManager;
    private GlobalLeaderFragment2 lastWeekFragment;
    private GlobalLeaderFragment todayFragment;
    private GlobalLeaderFragment3 lastMonthFragment;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_global_leader, container, false);

        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        alertDialogHelper = AlertDialogHelper.getInstance();
        sessionManager = new SessionManager();
        initialiseTabs(view);

        if (NetworkState.isNetworkAvailable(context)) {
            todayFragment = new GlobalLeaderFragment();
            lastWeekFragment = new GlobalLeaderFragment2();
            lastMonthFragment = new GlobalLeaderFragment3();
            callGlobalLeaderAPI();
        }
        else {
            if (!alertDialogHelper.isShowing()) {
                alertDialogHelper.showAlertDialog(context,
                        getString(R.string.internet_error_title),
                        getString(R.string.no_internet_message));
            }
        }

        return view;
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
            else {
                todayFragment.noDataAvailable();
            }

            if (last7DayList != null && last7DayList.size() > 0) {
                lastWeekFragment.setLastWeekList(last7DayList);
            }
            else {
                lastWeekFragment.noDataAvailable();
            }

            if (last30DayList != null && last30DayList.size() > 0) {
                lastMonthFragment.setLastMonthData(last30DayList);
            }
            else {
                lastMonthFragment.noDataAvailable();
            }
        }
    }


    private void initialiseTabs(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.monthly)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
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
                    return todayFragment;
                case 1:
                    return lastWeekFragment;
                case 2:
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
