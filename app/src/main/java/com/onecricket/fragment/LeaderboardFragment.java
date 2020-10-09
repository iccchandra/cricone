package com.onecricket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onecricket.APICallingPackage.retrofit.ApiClient;
import com.onecricket.R;
import com.onecricket.ui.CircularTextView;
import com.onecricket.utils.CommonProgressDialog;
import com.onecricket.utils.NetworkState;
import com.onecricket.utils.SessionManager;
import com.onecricket.utils.crypto.AlertDialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlertDialog progressAlertDialog;
    private Context context;
    private static final String TAG = "LeaderboardFragment";
    private TextView position1;
    private TextView position2;
    private TextView position3;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView location1;
    private TextView location2;
    private TextView location3;
    private TextView points1;
    private TextView points2;
    private TextView points3;
    private RelativeLayout firstPositionLayout;
    private RelativeLayout secondPositionLayout;
    private RelativeLayout thirdPositionLayout;
    private AlertDialogHelper alertDialogHelper;
    private CircularTextView circularTextView1;
    private CircularTextView circularTextView2;
    private CircularTextView circularTextView3;
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
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);

        sessionManager = new SessionManager();
        findViewsById(view);
        alertDialogHelper = AlertDialogHelper.getInstance();

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

    private void findViewsById(View view) {

        firstPositionLayout = view.findViewById(R.id.first_position_layout);
        secondPositionLayout = view.findViewById(R.id.second_position_layout);
        thirdPositionLayout = view.findViewById(R.id.third_position_layout);

        position1 = view.findViewById(R.id.position1);
        name1 = view.findViewById(R.id.name1);
        location1 = view.findViewById(R.id.location1);
        points1 = view.findViewById(R.id.points1);

        position2 = view.findViewById(R.id.position2);
        name2 = view.findViewById(R.id.name2);
        location2 = view.findViewById(R.id.location2);
        points2 = view.findViewById(R.id.points2);

        position3 = view.findViewById(R.id.position3);
        name3 = view.findViewById(R.id.name3);
        location3 = view.findViewById(R.id.location3);
        points3 = view.findViewById(R.id.points3);
        circularTextView1 = view.findViewById(R.id.circular_leader_one);
        circularTextView2 = view.findViewById(R.id.circular_leader_two);
        circularTextView3 = view.findViewById(R.id.circular_leader_three);
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
        String url = "";
        if (isGlobalLeaderBoard) {
            url = ApiClient.BASE_URL +  ":4040/global/leader?userid=" + sessionManager.getUser(context).getUser_id();
        }
        else {
            url = ApiClient.BASE_URL +  ":4040/game/leader?fi=" + fId;
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            dismissProgressDialog(progressAlertDialog);
            Log.d(TAG, response.toString());
            showResponseData(response);
        }, error -> {
            dismissProgressDialog(progressAlertDialog);
            if (error.getMessage() != null) {
                Log.d(TAG, error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void showResponseData(JSONObject response) {
        try {
            JSONArray result = response.getJSONArray("result");
            int length = result.length();
            if (length > 0) {
                switch (length) {
                    case 1:
                        showRankOne(result.getJSONObject(0));
                        showTempSecondRank();
                        showTempThirdRank();
                        showTempData();
                        break;
                    case 2:
                        showRankOne(result.getJSONObject(0));
                        showTwoRank(result.getJSONObject(1));
                        showTempThirdRank();
                        showTempData();
                        break;
                    case 3:
                        showRankOne(result.getJSONObject(0));
                        showTwoRank(result.getJSONObject(1));
                        showThreeRank(result.getJSONObject(2));
                        showTempData();
                        break;
                    default:
                        showDataInRecyclerView(result);
                        break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTempSecondRank() {
        name2.setText("Player");
        points2.setText("XX");
        location2.setText("Place");
        position2.setText("2");
        circularTextView2.setText("P");
    }

    private void showTempThirdRank() {
        name3.setText("Player");
        points3.setText("XX");
        location3.setText("Place");
        position3.setText("3");
        circularTextView3.setText("P");
    }

    private void showTempData() {
        List<UserData> userDataList = new ArrayList<>();
        userDataList.add(new UserData("4", "Player", "Place", "XX"));
        userDataList.add(new UserData("5", "Player", "Place", "XX"));
        userDataList.add(new UserData("6", "Player", "Place", "XX"));
        userDataList.add(new UserData("7", "Player", "Place", "XX"));
        userDataList.add(new UserData("8", "Player", "Place", "XX"));
        userDataList.add(new UserData("9", "Player", "Place", "XX"));
        userDataList.add(new UserData("10", "Player", "Place", "XX"));
        userDataList.add(new UserData("11", "Player", "Place", "XX"));
        userDataList.add(new UserData("12", "Player", "Place", "XX"));
        userDataList.add(new UserData("13", "Player", "Place", "XX"));
        userDataList.add(new UserData("14", "Player", "Place", "XX"));
        userDataList.add(new UserData("15", "Player", "Place", "XX"));
        userDataList.add(new UserData("16", "Player", "Place", "XX"));
        userDataList.add(new UserData("17", "Player", "Place", "XX"));
        LeaderBoardRecyclerViewAdapter adapter = new LeaderBoardRecyclerViewAdapter(userDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void showDataInRecyclerView(JSONArray result) {
        if (result.length() > 2) {
            List<UserData> userDataList = new ArrayList<>();
            for (int i = 3; i < result.length(); i++) {
                try {
                    JSONObject userInfo = result.getJSONObject(i);
                    String name = "";
                    String points = "";
                    String rank = "";
                    String location = "";
                    if (userInfo.has("name")) {
                        name = userInfo.getString("name");
                    }

                    if (userInfo.has("roi")) {
                        points = userInfo.getString("roi");
                    }

                    if (userInfo.has("rank")) {
                        rank = userInfo.getString("rank");
                    }

                    if (userInfo.has("place")) {
                        location = userInfo.getString("place");
                    }
                    UserData userData = new UserData(rank, name, location, points);
                    userDataList.add(userData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            LeaderBoardRecyclerViewAdapter adapter = new LeaderBoardRecyclerViewAdapter(userDataList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }

    private void showThreeRank(JSONObject response) {
        try {
            if (response.has("name")) {
                String leaderName = response.getString("name");
                if (leaderName.trim().length() > 0) {
                    name3.setText(leaderName);
                    circularTextView3.setText(String.format("%s", leaderName.toUpperCase().charAt(0)));
                }
            }

            if (response.has("roi")) {
                points3.setText(response.getString("roi"));
            }

            if (response.has("place")) {
                location3.setText(response.getString("place"));
            }

            if (response.has("rank")) {
                position3.setText(response.getString("rank"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showTwoRank(JSONObject response) {
        try {
            if (response.has("name")) {
                String leaderName = response.getString("name");
                if (leaderName.trim().length() > 0) {
                    name2.setText(leaderName);
                    circularTextView2.setText(String.format("%s", leaderName.toUpperCase().charAt(0)));
                }
            }

            if (response.has("roi")) {
                points2.setText(response.getString("roi"));
            }

            if (response.has("place")) {
                location2.setText(response.getString("place"));
            }

            if (response.has("rank")) {
                position3.setText(response.getString("rank"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRankOne(JSONObject response) {
        try {
            if (response.has("name")) {
                String leaderName = response.getString("name");
                if (leaderName.trim().length() > 0) {
                    name1.setText(leaderName);
                    circularTextView1.setText(String.format("%s", leaderName.toUpperCase().charAt(0)));
                }
            }

            if (response.has("roi")) {
                points1.setText(response.getString("roi"));
            }

            if (response.has("place")) {
                location1.setText(response.getString("place"));
            }

            if (response.has("rank")) {
                position1.setText(response.getString("rank"));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
