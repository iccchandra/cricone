package com.game.onecricket.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.game.onecricket.APICallingPackage.retrofit.ApiClient;
import com.game.onecricket.APICallingPackage.retrofit.ApiInterface;
import com.game.onecricket.APICallingPackage.retrofit.group.CreateGroupResponse;
import com.game.onecricket.APICallingPackage.retrofit.joingroup.JoinGroupResponse;
import com.game.onecricket.R;
import com.game.onecricket.activity.MatchOddsTabsActivity;
import com.game.onecricket.adapter.MatchesAdapter;
import com.game.onecricket.pojo.MatchesInfo;
import com.game.onecricket.utils.CommonProgressDialog;
import com.game.onecricket.utils.NetworkState;
import com.game.onecricket.utils.SessionManager;
import com.game.onecricket.utils.crypto.AlertDialogHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PrivateMatchesFragment extends Fragment implements MatchesAdapter.ClickListener, View.OnClickListener {

    private static final String TAG = "PrivateMatchesFragment";
    private RecyclerView recyclerView;
    private Context context;
    private AlertDialog progressAlertDialog;
    private List<MatchesInfo> matchesInfoList;
    private SessionManager sessionManager;
    private AlertDialogHelper alertDialogHelper;
    public EditText entercode;
    public Button joincode;
    public RelativeLayout LL_CVCInfoHead;
    private TextView nodataView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        UpcomingMatchesFragment.contest = true;
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        joincode = view.findViewById(R.id.joincode);
        LL_CVCInfoHead = view.findViewById(R.id.LL_CVCInfoHead);
        LL_CVCInfoHead.setVisibility(View.VISIBLE);
        entercode = view.findViewById(R.id.entercode);
        nodataView   = view.findViewById(R.id.no_data_view);
        joincode.setOnClickListener(this);
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
                            matchesInfo.setContestName(leagueName);

                           // JSONObject homeJSON = results.getJSONObject("home");
                            String name = results.getString("home_team");
                            matchesInfo.setHomeTeam(name);

                            if (results.has("match_time")) {
                                String date = results.getString("match_date");
                                String time = results.getString("match_time");
                                matchesInfo.setDateTime(date + " " + time);

                                //matchesInfo.setDate(DateFormat.getReadableDateFormat(time));
                                //matchesInfo.setTime(DateFormat.getReadableTimeFormat(time));
                                //  matchesInfo.setDateTime(DateFormat.getReadableDateTimeFormat(time));
                            }



                            if (results.has("code")) {
                                String code = results.getString("code");
                                matchesInfo.setcode(code);
                            }

                            if (results.has("contest_id")) {
                                String contestId = results.getString("contest_id");
                                matchesInfo.setContestId(contestId);
                            }
                           // JSONObject awayJSON = results.getJSONObject("away");
                            String away = results.getString("visitor_team");
                            matchesInfo.setVisitorsTeam(away);
                            matchesInfoList.add(matchesInfo);
                        }

                        if (matchesInfoList.size() > 0) {
                            nodataView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            MatchesAdapter adapter = new MatchesAdapter(matchesInfoList, "private");
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setHasFixedSize(true);
                            adapter.setRecyclerViewItemClickListener(PrivateMatchesFragment.this);
                            recyclerView.setAdapter(adapter);
                        }
                        else {
                            nodataView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        nodataView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        e.printStackTrace();
                    }


                },
                error -> {
                    dismissProgressDialog(progressAlertDialog);
                    nodataView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
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

    @Override
    public void onCodeClicked(int position) {
        selectedPosition = position;

        new FancyGifDialog.Builder((Activity) context)
                .setTitle("Share or Copy")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Share Code")
                .setGifResource(R.drawable.common_gif)
                .isCancellable(true)
                .setNegativeBtnText("Copy code")
                .OnPositiveClicked(() -> {
                    onShareClicked(matchesInfoList.get(position).getcode());
                })
                .OnNegativeClicked(() -> {
                    copyCodeInClipBoard(this.getContext(), matchesInfoList.get(position).getcode(), "Copied");
                })

                .build();

    }
    public static void copyCodeInClipBoard(Context context, String text, String label) {
        if (context != null) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            if (clipboard == null || clip == null)
                return;
            clipboard.setPrimaryClip(clip);
            CharSequence text1 ="code Copied"+ "\n"+text;
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text1, duration);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

            toast.show();

        }
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
            if (throwable.getMessage() != null) {
                Log.d(TAG, throwable.getMessage());
            }
            if (!alertDialogHelper.isShowing()) {
                alertDialogHelper.showAlertDialog(context,
                        "Error",
                        getString(R.string.label_something_went_wrong));
            }
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
                        onShareClicked(responseBody.getData());
                    })

                    .build();

        }

        private void onShareClicked (String code) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "I made predictions on 1Cricket App. Join me if you are interested.  "+code);
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


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.joincode) {
            onJoinCodeClicked();
        }
    }

    private void onJoinCodeClicked() {
        String code = entercode.getText().toString().trim();
        if (code.length() > 0) {
            joinFriends(code);
        }
        else {
            Toast.makeText(context, "Please enter code", Toast.LENGTH_SHORT).show();
        }
    }


    private void joinFriends(String id) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        ApiInterface apiInterface = ApiClient.getClientWithAuthorisation(sessionManager.getUser(context).getToken()).create(ApiInterface.class);


        JSONObject inputJSON = new JSONObject();
        try {
            inputJSON.put("privateid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Observable<JoinGroupResponse> observable = apiInterface.joinContest(ApiClient.getRequestBody(inputJSON));
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::onSuccessResponse, this::onErrorResponse);

    }

    private void onSuccessResponse(JoinGroupResponse joinGroupResponse) {
        dismissProgressDialog(progressAlertDialog);
        entercode.setText("");
        new FancyGifDialog.Builder((Activity) context)
                .setTitle(joinGroupResponse.getStatus())
                .setMessage(joinGroupResponse.getMessage())
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("OK")
                .setGifResource(R.drawable.common_gif)
                .isCancellable(true)
                .build();
    }
}
