package com.game.onecricket.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.game.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.game.onecricket.APICallingPackage.retrofit.group.CreateGroupResponse;
import com.game.onecricket.R;
import com.game.onecricket.activity.MatchOddsTabsActivity;
import com.game.onecricket.adapter.MatchesAdapter;
import com.game.onecricket.pojo.MatchesInfo;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.DateFormat;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.SimpleItemAnimator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CLIPBOARD_SERVICE;

public class UpcomingMatchesFragment extends Fragment implements MatchesAdapter.ClickListener {

    private static final String TAG = "UpcomingMatchesFragment";
    private RecyclerView recyclerView;
    private Context context;
    private AlertDialog progressAlertDialog;
    private List<MatchesInfo> matchesInfoList;
    private SessionManager sessionManager;
    private AlertDialogHelper alertDialogHelper;
    public static boolean  contest=false;
    public static String timediffhelp;
    private TextView nodataView;
    private MatchesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        recyclerView = view.findViewById(R.id.matches);
        nodataView   = view.findViewById(R.id.no_data_view);
        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        sessionManager = new SessionManager();
        contest=false;
        alertDialogHelper = AlertDialogHelper.getInstance();
        if (NetworkState.isNetworkAvailable(context)) {
            callInProgressMatchesAPI();
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

    private void callInProgressMatchesAPI() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = ApiClient.BASE_URL +  ":4040/inplay/matches";
        Log.d(TAG, URL);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                                             URL ,
                                                             null,
             response -> {
                dismissProgressDialog(progressAlertDialog);
                 Log.d(TAG, "callPointsScanAPI response: " + response.toString());
                 matchesInfoList = new ArrayList<>();
                 try {
//                         JSONObject responseJson = new JSONObject(responseStr);
                     JSONArray resultsArray = response.getJSONArray("results");
                     for (int i = 0; i < resultsArray.length(); i++) {
                         MatchesInfo matchesInfo = new MatchesInfo();
                         JSONObject results = resultsArray.getJSONObject(i);

                         if (results.has("id")) {
                             String id = results.getString("id");
                             matchesInfo.setId(id);
                         }

                         if (results.has("league")) {
                             JSONObject leagueJSON = results.getJSONObject("league");
                             String leagueName = leagueJSON.getString("name");
                             matchesInfo.setLeagueName(leagueName);
                         }
                         else {
                             matchesInfo.setLeagueName("");
                         }

                         if (results.has("time")) {
                             String time = results.getString("time");
                             matchesInfo.setTime(DateFormat.getReadableTimeFormat(time));
                             matchesInfo.setDate(DateFormat.getReadableDateFormat(time));
                             matchesInfo.setDateTime(DateFormat.getReadableDateFormat(time));
                         }

                         matchesInfo.setMatchInProgress(true);

                         JSONObject homeJSON = results.getJSONObject("home");
                         String name = homeJSON.getString("name");
                         matchesInfo.setHomeTeam(name);

                         JSONObject awayJSON = results.getJSONObject("away");
                         String away = awayJSON.getString("name");
                         matchesInfo.setVisitorsTeam(away);
                         matchesInfo.setMatchType("InPlay");
                         matchesInfoList.add(matchesInfo);
                     }

                     adapter = new MatchesAdapter(matchesInfoList, "InPlay");
                     recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                     recyclerView.setHasFixedSize(true);
                     adapter.setRecyclerViewItemClickListener(UpcomingMatchesFragment.this);
                     recyclerView.setAdapter(adapter);
                     adapter.notifyDataSetChanged();



                     nodataView.setVisibility(View.GONE);
                     recyclerView.setVisibility(View.VISIBLE);
                     callMatchesAPI(sessionManager.getUser(context).getToken());

                 } catch (JSONException e) {
                     e.printStackTrace();
                     nodataView.setVisibility(View.VISIBLE);
                     recyclerView.setVisibility(View.GONE);
                 }


             },
             error -> {
                 dismissProgressDialog(progressAlertDialog);
                 nodataView.setVisibility(View.VISIBLE);
                 recyclerView.setVisibility(View.GONE);
                 Log.e(TAG, "Error: " + error.getMessage());
             })
        {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(jsonObjReq);

    }


    private void callMatchesAPI(String token) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        String URL = "https://api.b365api.com/v1/betfair/sb/upcoming?sport_id=4&token=61256-D7NpN8AgdxZCv5";
//        String URL = "https://api.b365api.com/v1/betfair/sb/upcoming?sport_id=4&token=61256-gf4iT7mN2rL324";
//        String URL = "https://api.b365api.com/v1/bet365/upcoming?sport_id=3&token=61925-2bBIpJrOkeLtND";
        String URL = ApiClient.BASE_URL + ":4040/upcoming/matches";
        Log.d(TAG, URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                URL,
                null,
                response -> {
                    dismissProgressDialog(progressAlertDialog);
                    Log.d(TAG, "callMatchesAPI response: " + response.toString());
                    try {
                        JSONArray resultsArray = response.getJSONArray("results");
                        for (int i = 0; i < resultsArray.length(); i++) {
                            MatchesInfo matchesInfo = new MatchesInfo();
                            JSONObject results = resultsArray.getJSONObject(i);

                            String id = results.getString("id");
                            matchesInfo.setId(id);

                            JSONObject leagueJSON = results.getJSONObject("league");
                            String leagueName = leagueJSON.getString("name");
                            matchesInfo.setLeagueName(leagueName);

                            JSONObject homeJSON = results.getJSONObject("home");
                            String name = homeJSON.getString("name");
                            matchesInfo.setHomeTeam(name);

                            if (results.has("time")) {
                                String time = results.getString("time");
                                timediffhelp = results.getString("time");
                                matchesInfo.setDate(DateFormat.getReadableDateFormat(time));
                                matchesInfo.setTime(DateFormat.getReadableTimeFormat(time));
                                matchesInfo.setDateTime(DateFormat.getReadableDateTimeFormat(time));
                            }

                            JSONObject awayJSON = results.getJSONObject("away");
                            String away = awayJSON.getString("name");
                            matchesInfo.setVisitorsTeam(away);
                            matchesInfo.setMatchType("Upcoming");
                            matchesInfoList.add(matchesInfo);
                        }

                        if (matchesInfoList.size() > 0) {
                            nodataView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.setMatchesInfoList(matchesInfoList);
                            adapter.notifyDataSetChanged();


                        }
                        else {
                            nodataView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        nodataView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


                },
                error -> {
                    dismissProgressDialog(progressAlertDialog);
                    nodataView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    Log.e(TAG, "Error: " + error.getMessage());
                }) {

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
      //  showCreateGroupNameAlert(position);



/*        Intent intent = new Intent(getActivity(), MatchOddsTabsActivity.class);
        intent.putExtra("MatchInfo", matchesInfoList.get(position));
        startActivity(intent);*/
    }

    @Override
    public void onCodeClicked(int position) {

    }


}
