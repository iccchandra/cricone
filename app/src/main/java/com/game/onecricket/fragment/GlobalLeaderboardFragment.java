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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.R;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;

import java.util.HashMap;
import java.util.Map;

public class GlobalLeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlertDialog progressAlertDialog;
    private Context context;
    private static final String TAG = "LeaderboardFragment";

    private AlertDialogHelper alertDialogHelper;
    private SessionManager sessionManager;
    private boolean isGlobalLeader;
    private String fId;

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

        sessionManager = new SessionManager();
        initialiseTabs(view);

        if (NetworkState.isNetworkAvailable(context)) {
            if (getArguments() != null) {
                boolean isGlobalLeaderBoard = getArguments().getBoolean("IS_GLOBAL_LEADERBOARD");
                fId = getArguments().getString("F_ID");
                callLeaderBoardAPI(isGlobalLeaderBoard);
            }
        }
        else {
            if (!alertDialogHelper.isShowing()) {
                alertDialogHelper.showAlertDialog(context,
                        getString(R.string.internet_error_title),
                        getString(R.string.no_internet_message));
            }
        }


/*
        userDataList.add(new UserData(4, "Zacharaya", "Bangalore", 220));
        userDataList.add(new UserData(5, "Louis", "Chennai", 215));
        userDataList.add(new UserData(6, "Zuren", "Pune", 210));
        userDataList.add(new UserData(7, "Larrson", "Chandigargh", 205));
        userDataList.add(new UserData(8, "Josie Ho", "Bangalore", 200));
        userDataList.add(new UserData(9, "Daria", "Kolkata", 195));
        userDataList.add(new UserData(10, "Matt Damon", "Ahmedabad", 190));
        userDataList.add(new UserData(11, "Griffin Kane", "Jaipur", 185));
        userDataList.add(new UserData(12, "Jude Law", "Lucknow", 180));
        userDataList.add(new UserData(13, "Teri McEvoy", "Surat", 175));
        userDataList.add(new UserData(14, "Sue Redman", "Indore", 170));
        userDataList.add(new UserData(15, "Stef Tovar", "Bhopal", 165));
        userDataList.add(new UserData(16, "Grace Rex", "Varanasi", 160));
        userDataList.add(new UserData(17, "Armin Rohde", "Kochi", 155));
*/


        return view;
    }

    private void initialiseTabs(View view) {
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Today"));
        tabLayout.addTab(tabLayout.newTab().setText("Last Week"));
        tabLayout.addTab(tabLayout.newTab().setText("Last Month"));
        alertDialogHelper = AlertDialogHelper.getInstance();

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());
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
            Bundle bundle = new Bundle();
/*            switch (position) {
                case 0:
                    return matchOddsFragment;
                case 1:

                    return chatRoomFragment;
                case 2:
                    return leaderboardFragment;
                default:
                    return null;
            }*/

            return null;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

    private void findViewsById(View view) {


    }

    private void dismissProgressDialog(AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }

    private void callLeaderBoardAPI(boolean isGlobalLeaderBoard) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = ApiClient.BASE_URL + "/myrest/user/leaderboard";;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            dismissProgressDialog(progressAlertDialog);
            Log.d(TAG, response.toString());

        }, error -> {
            dismissProgressDialog(progressAlertDialog);
            if (error.getMessage() != null) {
                Log.d(TAG, error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SessionManager sessionManager = new SessionManager();
                headers.put("Authorization", sessionManager.getUser(context).getToken());
                Log.d(TAG, sessionManager.getUser(context).getToken());
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}
