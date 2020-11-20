package com.game.onecricket.fragment;

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
import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.R;
import com.game.onecricket.pojo.Data;
import com.game.onecricket.pojo.MatchesInfo;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlertDialog progressAlertDialog;
    private Context context;
    private static final String TAG = "LeaderboardFragment";
    private AlertDialogHelper alertDialogHelper;
    private SessionManager sessionManager;
    private boolean isGlobalLeader;
    private String fId;
    private MatchesInfo matchesInfo;
    private TextView noDataView;

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
                boolean isContest = getArguments().getBoolean("IS_PRIVATE_CONTEST");
                fId = getArguments().getString("F_ID");
                matchesInfo = (MatchesInfo) getArguments().getSerializable("matchesInfo");
                if (isContest) {
                    callPrivateContestLeaderBoard();
                }
                else {
                    callLeaderBoardAPI();
                }

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
        noDataView          = view.findViewById(R.id.no_data_view);
    }

    private void dismissProgressDialog(AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }

    private void callPrivateContestLeaderBoard() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = ApiClient.BASE_URL +  "/myrest/user/private_contest_leaderboard";

        JSONObject inputJSON = new JSONObject();
        try {
            inputJSON.put("contest_id", matchesInfo.getContestId());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, inputJSON, response -> {
            dismissProgressDialog(progressAlertDialog);
            Log.d(TAG, response.toString());
            displayResponseData(response);
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

    private void callLeaderBoardAPI() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = ApiClient.BASE_URL +  "/myrest/user/game_leaderboard";
        System.out.println(url);
        JSONObject inputJSON = new JSONObject();
        try {
            inputJSON.put("home_team", matchesInfo.getHomeTeam());
            inputJSON.put("visitor_team", matchesInfo.getVisitorsTeam());
            inputJSON.put("match_date", matchesInfo.getDate());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, inputJSON, response -> {
            dismissProgressDialog(progressAlertDialog);
            Log.d(TAG, response.toString());
            displayResponseData(response);
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

    private void displayResponseData(JSONObject response) {
        if (response != null) {
            try {
                if (response.has("status")) {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        if (response.has("data")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            List<Data> leaderBoardList = new ArrayList<>(dataArray.length());
                            for (int i = 0; i< dataArray.length();i++) {
                                Data data = new Data();
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                if (dataObject.has("name")) {
                                    data.setName(dataObject.getString("name"));
                                }
                                if (dataObject.has("state")) {
                                    data.setState(dataObject.getString("state"));
                                }
                                if (dataObject.has("total_bet")) {
                                    data.setTotalBet(dataObject.getString("total_bet"));
                                }
                                if (dataObject.has("bet_amount")) {
                                    data.setBetAmount(dataObject.getString("bet_amount"));
                                }
                                if (dataObject.has("total_winning")) {
                                    data.setTotalWinning(dataObject.getString("total_winning"));
                                }
                                if (dataObject.has("roi")) {
                                    data.setRoi(dataObject.getString("roi"));
                                }
                                leaderBoardList.add(data);
                            }

                            if (leaderBoardList.size() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                noDataView.setVisibility(View.GONE);
                                LeaderBoardRecyclerViewAdapter adapter = new LeaderBoardRecyclerViewAdapter(leaderBoardList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                showNoDataAvailableView();
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showNoDataAvailableView();
            }
        }
    }

    private void showNoDataAvailableView() {
        recyclerView.setVisibility(View.GONE);
        noDataView.setVisibility(View.VISIBLE);
    }

}
