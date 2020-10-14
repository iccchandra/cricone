package com.onecricket.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onecricket.APICallingPackage.retrofit.ApiClient;
import com.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.onecricket.APICallingPackage.retrofit.group.CreateGroupResponse;
import com.onecricket.R;
import com.onecricket.activity.MatchOddsTabsActivity;
import com.onecricket.adapter.MatchesAdapter;
import com.onecricket.pojo.MatchesInfo;
import com.onecricket.utils.CommonProgressDialog;
import com.onecricket.utils.DateFormat;
import com.onecricket.utils.NetworkState;
import com.onecricket.utils.SessionManager;
import com.onecricket.utils.crypto.AlertDialogHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PrivateMatchesFragment extends Fragment implements MatchesAdapter.ClickListener {

    private static final String TAG = "PrivateMatchesFragment";
    private RecyclerView recyclerView;
    private Context context;
    private AlertDialog progressAlertDialog;
    private List<MatchesInfo> matchesInfoList;
    private SessionManager sessionManager;
    private AlertDialogHelper alertDialogHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        UpcomingMatchesFragment.contest=true;
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        recyclerView = view.findViewById(R.id.matches);
        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        sessionManager = new SessionManager();
        alertDialogHelper = AlertDialogHelper.getInstance();
        if (NetworkState.isNetworkAvailable(context)) {
            callMatchesAPI(sessionManager.getUser(context).getToken());
        } else {
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void callMatchesAPIRetrofit() {
//        final ApiInterface      apiService = RetrofitClient.getClient().create(ApiInterface.class);
//        Call<ResponseBody> call       = apiService.getMatchesData();
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call,
//                                   @NonNull retrofit2.Response<ResponseBody> response)
//            {
//                Log.d(TAG, response.toString());
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                Log.d(TAG, "response.toString()");
//            }
//        });

    }

    private void callMatchesAPI(String token) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "https://api.b365api.com/v1/betfair/sb/upcoming?sport_id=4&token=61256-D7NpN8AgdxZCv5";
//        String URL = "https://api.b365api.com/v1/betfair/sb/upcoming?sport_id=4&token=61256-gf4iT7mN2rL324";
//        String URL = "https://api.b365api.com/v1/bet365/upcoming?sport_id=3&token=61925-2bBIpJrOkeLtND";
        String URL = ApiClient.BASE_URL + "/myrest/user/pivate_contest_list";
        Log.d(TAG, URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL,
                null,
                response -> {
                    dismissProgressDialog(progressAlertDialog);
                    Log.d(TAG, "callPrivateAPI response: " + response.toString());
                    matchesInfoList = new ArrayList<>();
                    try {
                        JSONArray resultsArray = response.getJSONArray("data");
                        for (int i = 0; i < resultsArray.length(); i++) {
                            MatchesInfo matchesInfo = new MatchesInfo();
                            JSONObject results = resultsArray.getJSONObject(i);

                            String id = results.getString("fi_id");
                            Log.d(TAG, "callPrivateAPI response: " + id);


                            matchesInfo.setId(id);

                            //JSONObject leagueJSON = results.getJSONObject("league");
                            String leagueName = results.getString("contest_name");
                            matchesInfo.setLeagueName(leagueName);

                           // JSONObject homeJSON = results.getJSONObject("home");
                            String name = results.getString("home_team");
                            matchesInfo.setHomeTeam(name);

                            if (results.has("match_time")) {
                                String date = results.getString("match_date");
                                String time = results.getString("match_time");
                                matchesInfo.setDateTime(date+" "+time);

                                //matchesInfo.setDate(DateFormat.getReadableDateFormat(time));
                                //matchesInfo.setTime(DateFormat.getReadableTimeFormat(time));
                              //  matchesInfo.setDateTime(DateFormat.getReadableDateTimeFormat(time));
                            }

                           // JSONObject awayJSON = results.getJSONObject("away");
                            String away = results.getString("visitor_team");
                            matchesInfo.setVisitorsTeam(away);
                            matchesInfoList.add(matchesInfo);
                        }

                        MatchesAdapter adapter = new MatchesAdapter(matchesInfoList,"private");
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setHasFixedSize(true);
                        adapter.setRecyclerViewItemClickListener(PrivateMatchesFragment.this);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    dismissProgressDialog(progressAlertDialog);
                    Log.e(TAG, "Error: " + error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", token);
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(jsonObjReq);

    }

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(getActivity(), MatchOddsTabsActivity.class);
        intent.putExtra("MatchInfo", matchesInfoList.get(position));
        startActivity(intent);
    }

    private int selectedPosition;

    @Override
    public void onCreateGroupClicked(int position) {
        selectedPosition = position;
       // showCreateGroupNameAlert(position);

/*        Intent intent = new Intent(getActivity(), MatchOddsTabsActivity.class);
        intent.putExtra("MatchInfo", matchesInfoList.get(position));
        startActivity(intent);*/
    }

    private void showCreateGroupNameAlert(int position) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.group_name);

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, id) -> {
                    String userInputString = userInput.getText().toString();
                   createGroup(userInputString, position);
                })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel()

                );


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void createGroup(String groupName, int position) {

        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        if (groupName.length() == 0) {
            Context context = getContext();
            CharSequence text = "please enter group name!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

            toast.show();
            dismissProgressDialog(progressAlertDialog);
            showCreateGroupNameAlert(position);
        }
        else{
        ApiInterface apiInterface = ApiClient.getClientWithAuthorisation(sessionManager.getUser(context).getToken()).create(ApiInterface.class);
        MatchesInfo matchesInfo = matchesInfoList.get(position);
        JSONObject inputJSON = new JSONObject();
        try {
            inputJSON.put("contest_name", groupName);
            inputJSON.put("home_team", matchesInfo.getHomeTeam());
            inputJSON.put("visitor_team", matchesInfo.getVisitorsTeam());
            inputJSON.put("match_date", matchesInfo.getDate());
            inputJSON.put("fi_id", matchesInfo.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Observable<CreateGroupResponse> observable = apiInterface.createGame(ApiClient.getRequestBody(inputJSON));
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::onSuccessResponse, this::onErrorResponse);
    }

}

        private void onErrorResponse (Throwable throwable){
            dismissProgressDialog(progressAlertDialog);
            Log.d(TAG, throwable.getMessage());
        }

        private void onSuccessResponse (CreateGroupResponse responseBody){
            dismissProgressDialog(progressAlertDialog);

            MatchesInfo matchesInfo = matchesInfoList.get(selectedPosition);
            matchesInfo.setContest("private");
            matchesInfo.setPrivateId(responseBody.getData());

            new FancyGifDialog.Builder((Activity) context)
                    .setTitle("Hooray!")
                    .setMessage("you have created an private game, ask your friends with code:" + responseBody.getData())
                    .setPositiveBtnBackground("#FF4081")
                    .setPositiveBtnText("Share Code")
                    .setGifResource(R.drawable.common_gif)
                    .isCancellable(true)
                    .OnPositiveClicked(() -> {
                        onShareClicked();
                    })

                    .build();

        }

        private void onShareClicked () {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "I made predictions on 1Cricket App. Join me if you are interested.");
            intent.setType("text/plain");
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
            }
        }

        private void gotoMatchOdds ( int position){
            Intent intent = new Intent(getActivity(), MatchOddsTabsActivity.class);
            intent.putExtra("MatchInfo", matchesInfoList.get(position));
            startActivity(intent);
        }

}
