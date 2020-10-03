package com.onecricket.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.APICallingPackage.retrofit.ApiClient;
import com.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.onecricket.APICallingPackage.retrofit.betlist.SubmittedBets;
import com.onecricket.R;
import com.onecricket.databinding.FragmentMyContestBinding;
import com.onecricket.utils.CommonProgressDialog;
import com.onecricket.utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyContestFragment extends Fragment implements ResponseManager{


    private static final String TAG = "MyContestFragment";
    private static final String API_TYPE_MY_BET_LIST = "my_bet_list";
    FragmentMyContestBinding binding;
    private ResponseManager responseManager;
    private APIRequestManager apiRequestManager;
    private SessionManager sessionManager;
    private Context context;
    private MyBetsUpcomingFragment myFixturesFragment;
    private FragmentMyLive myLiveFragment;
    private FragmentMyResults myResultsFragment;
    private AlertDialog progressAlertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyContestBinding.inflate(inflater, container, false);

        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        initialiseAPI();
        if (sessionManager != null &&
            sessionManager.getUser(context) != null &&
            sessionManager.getUser(context).getToken() != null &&
            sessionManager.getUser(context).getToken().trim().length() > 0) {
            callBetListAPI();
        }

        setupViewPager(binding.myviewpager);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.myviewpager, new MyBetsUpcomingFragment());
        transaction.commit();

        return binding.getRoot();
    }

    private void dismissProgressDialog(AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void callBetListAPI() {

        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        ApiInterface apiInterface = ApiClient.getClientWithAuthorisation(sessionManager.getUser(context).getToken()).create(ApiInterface.class);

        Observable<SubmittedBets> observable = apiInterface.getSubmittedBets();
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleSubmittedBetsAPI, this::handleSubmittedBetsError);
    }

    private void handleSubmittedBetsError(Throwable throwable) {
        dismissProgressDialog(progressAlertDialog);
        if (throwable.getMessage() != null) {
            Log.d(TAG, throwable.getMessage());
        }
    }

    private void handleSubmittedBetsAPI(SubmittedBets submittedBets) {
        dismissProgressDialog(progressAlertDialog);
        myFixturesFragment.setUpcomingBetsData(submittedBets.getData().getUpcoming());
        myLiveFragment.setLiveBetsData(submittedBets.getData().getInprogress());
        myResultsFragment.setFinishedBetsData(submittedBets.getData().getFinished());
        Log.d(TAG, submittedBets.toString());
    }

    private void initialiseAPI() {
        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());
    }

    private void setupViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        myFixturesFragment = new MyBetsUpcomingFragment();
        myLiveFragment = new FragmentMyLive();
        myResultsFragment = new FragmentMyResults();
        adapter.addFragment(myFixturesFragment, "UPCOMING");
        adapter.addFragment(myLiveFragment, "IN-PLAY");
        adapter.addFragment(myResultsFragment, "FINISHED");
        viewPager.setAdapter(adapter);

        binding.FragmentMyTab.setupWithViewPager(binding.myviewpager);

        for (int i = 0; i < binding.FragmentMyTab.getTabCount(); i++) {
            TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            binding.FragmentMyTab.getTabAt(i).setCustomView(tv);
        }
        binding.myviewpager.setOffscreenPageLimit(2);

    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        Log.d(TAG, result.toString());
        if (type.equals(API_TYPE_MY_BET_LIST)) {

        }
    }

    @Override
    public void onError(Context mContext, String type, String message) {
        Log.d(TAG, message);
    }


    class MyViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
