package com.onecricket.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onecricket.APICallingPackage.retrofit.ApiClient;
import com.onecricket.R;
import com.onecricket.activity.MatchOddsTabsActivity;
import com.onecricket.adapter.MatchesAdapter;
import com.onecricket.pojo.MatchesInfo;
import com.onecricket.utils.CommonProgressDialog;
import com.onecricket.utils.NetworkState;
import com.onecricket.utils.crypto.AlertDialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InProgressMatchesFragment extends Fragment implements MatchesAdapter.ClickListener {

    private static final String TAG = "MatchesFragment";
    private RecyclerView recyclerView;
    private Context context;
    private AlertDialog progressAlertDialog;
    private List<MatchesInfo> matchesInfoList;
    private AlertDialogHelper alertDialogHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        recyclerView = view.findViewById(R.id.matches);
        alertDialogHelper = AlertDialogHelper.getInstance();

        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        if (NetworkState.isNetworkAvailable(context)) {
            callMatchesAPI();
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

    private void callMatchesAPI() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = ApiClient.BASE_URL +  ":4040/inplay/matches";
//        String URL = "https://api.b365api.com/v1/betfair/sb/inplay?sport_id=4&token=61925-2bBIpJrOkeLtND";

        Log.d(TAG, URL);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                                             URL ,
                                                             null,
                response -> {

/*                     String responseStr = "{\n" +
                            "  \"success\": 1,\n" +
                            "  \"pager\": {\n" +
                            "    \"page\": 1,\n" +
                            "    \"per_page\": 50,\n" +
                            "    \"total\": 2\n" +
                            "  },\n" +
                            "  \"results\": [\n" +
                            "    {\n" +
                            "      \"id\": \"29971937\",\n" +
                            "      \"sport_id\": \"4\",\n" +
                            "      \"time\": \"1598634000\",\n" +
                            "      \"time_status\": \"1\",\n" +
                            "      \"home\": {\n" +
                            "        \"id\": \"101516\",\n" +
                            "        \"name\": \"England\"\n" +
                            "      },\n" +
                            "      \"away\": {\n" +
                            "        \"id\": \"101132\",\n" +
                            "        \"name\": \"Pakistan\"\n" +
                            "      },\n" +
                            "      \"our_event_id\": \"2661294\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": \"29978134\",\n" +
                            "      \"sport_id\": \"4\",\n" +
                            "      \"time\": \"1598641200\",\n" +
                            "      \"time_status\": \"1\",\n" +
                            "      \"league\": {\n" +
                            "        \"id\": \"11893330\",\n" +
                            "        \"name\": \"T20 Blast\"\n" +
                            "      },\n" +
                            "      \"home\": {\n" +
                            "        \"id\": \"122456\",\n" +
                            "        \"name\": \"Birmingham Bears\"\n" +
                            "      },\n" +
                            "      \"away\": {\n" +
                            "        \"id\": \"113564\",\n" +
                            "        \"name\": \"Somerset\"\n" +
                            "      },\n" +
                            "      \"our_event_id\": \"2661553\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";*/

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

                            matchesInfo.setMatchInProgress(true);

                            JSONObject homeJSON = results.getJSONObject("home");
                            String name = homeJSON.getString("name");
                            matchesInfo.setHomeTeam(name);

                            JSONObject awayJSON = results.getJSONObject("away");
                            String away = awayJSON.getString("name");
                            matchesInfo.setVisitorsTeam(away);
                            matchesInfoList.add(matchesInfo);
                        }

                        if (matchesInfoList.size() > 0) {
                            MatchesAdapter adapter = new MatchesAdapter(matchesInfoList, "In-Play");
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setHasFixedSize(true);
                            adapter.setRecyclerViewItemClickListener(InProgressMatchesFragment.this);
                            recyclerView.setAdapter(adapter);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> {
                    dismissProgressDialog(progressAlertDialog);
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

    @Override
    public void onItemClickListener(int position) {
        Intent intent = new Intent(getActivity(), MatchOddsTabsActivity.class);
        intent.putExtra("MatchInfo", matchesInfoList.get(position));
        startActivity(intent);
    }
}
