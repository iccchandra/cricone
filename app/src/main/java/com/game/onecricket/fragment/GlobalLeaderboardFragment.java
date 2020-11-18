package com.game.onecricket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Data;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.GlobalLeaderResponse;
import com.game.onecricket.R;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;
import com.google.android.material.tabs.TabLayout;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GlobalLeaderboardFragment extends Fragment {

    private AlertDialog progressAlertDialog;
    private Context context;

    private AlertDialogHelper alertDialogHelper;
    private SessionManager sessionManager;
    private View view;

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
        view = inflater.inflate(R.layout.fragment_global_leader, container, false);

        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        alertDialogHelper = AlertDialogHelper.getInstance();
        sessionManager = new SessionManager();
        if (NetworkState.isNetworkAvailable(context)) {
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
            initialiseTabs(view, globalLeaderResponse.getData());
        }
    }

    private void initializeTabs(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.monthly)));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

   private Data data;
   private void initialiseTabs(View view, Data data) {
        this.data = data;
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Weekly"));
        tabLayout.addTab(tabLayout.newTab().setText(context.getString(R.string.monthly)));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = view.findViewById(R.id.pager);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private int mNumOfTabs;
        public ViewPagerAdapter(FragmentManager fragmentManager, int tabsCount) {
            super(fragmentManager, tabsCount);
            mNumOfTabs = tabsCount;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Data", data);
                    GlobalLeaderFragment todayFragment = new GlobalLeaderFragment();
                    todayFragment.setArguments(bundle);
                    return todayFragment;
                case 1:
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("Data", data);
                    GlobalLeaderFragment2 lastWeekFragment = new GlobalLeaderFragment2();
                    lastWeekFragment.setArguments(bundle2);
                    return lastWeekFragment;
                case 2:
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("Data", data);
                    GlobalLeaderFragment3 lastMonthFragment = new GlobalLeaderFragment3();
                    lastMonthFragment.setArguments(bundle3);
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
