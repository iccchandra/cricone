package com.onecricket.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.APICallingPackage.retrofit.APIService;
import com.onecricket.APICallingPackage.retrofit.pojo.livescore.LiveScroreData;
import com.onecricket.R;
import com.onecricket.adapter.BottomsheetRecyclerViewAdapter;
import com.onecricket.adapter.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.onecricket.adapter.expandablerecyclerview.OddsCategoryAdapter;
import com.onecricket.pojo.MatchOdds;
import com.onecricket.pojo.MatchesInfo;
import com.onecricket.pojo.OddsCategory;
import com.onecricket.utils.CommonProgressDialog;
import com.onecricket.utils.NetworkState;
import com.onecricket.utils.SessionManager;
import com.onecricket.utils.crypto.AlertDialogHelper;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.onecricket.APICallingPackage.Config.MYACCOUNT;
import static com.onecricket.APICallingPackage.Constants.MYACCOUNTTYPE;

public class MatchOddsFragment extends Fragment implements OddsCategoryAdapter.ChildClickListener,
                                                           BottomsheetRecyclerViewAdapter.ItemChangeListener,
                                                           ResponseManager
{

    private static final String TAG = "MatchOddsFragment";
    private MatchesInfo matchesInfo;
    private AlertDialog progressAlertDialog;
    private OddsCategoryAdapter oddsCategoryAdapter;
    private RecyclerView recyclerView;
    private BottomSheetBehavior sheetBehavior;
    private RecyclerView bottomSheetRecyclerView;
    private BottomsheetRecyclerViewAdapter bottomsheetRecyclerViewAdapter;
    private TextView betsCountView;
    private Button placeBet;
    private Context context;
    private Listener listener;
    private RelativeLayout bottom_sheet;
    private APIService apiService;
    private Disposable disposable;
    private List<OddsCategory> oddsCategoryList;
    private TextView matchStatusText;
    private boolean canCallLiveScroeAPI = false;
    private String matchType = "";
    private RelativeLayout topLayout;
    private List<OddsCategory> oddsCategoryListFinal;
    private RelativeLayout matchStatusLayout;
    private TextView firstInningsTextView;
    private TextView secondInningsTextView;
    private ResponseManager responseManager;
    private APIRequestManager apiRequestManager;
    private SessionManager sessionManager;
    private float coinsRemaining;
    private float coinsRemainingOnBetAmountChanged;
    private TextView coinsRemainingTextView;
    private TextView coinsRemainingLabelTextView;
    private AlertDialogHelper alertDialogHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_odds, container, false);

        findViewsById(view);
        alertDialogHelper = AlertDialogHelper.getInstance();
        progressAlertDialog = CommonProgressDialog.getProgressDialog(context);
        if (getArguments() != null && getArguments().getSerializable("MatchInfo") != null) {
            matchesInfo = (MatchesInfo) getArguments().getSerializable("MatchInfo");
            if (matchesInfo != null) {
                Log.d(TAG, String.valueOf(matchesInfo.getId()));
            }
        }

        sessionManager = new SessionManager();
        responseManager = this;
        apiRequestManager = new APIRequestManager(getActivity());
        callMyAccountDetails(false);


        if (matchesInfo != null && matchesInfo.getId() != null && matchesInfo.getId().trim().length() > 0) {
            if (matchesInfo.isMatchInProgress()) {
                matchType = "inplay";
                callInPlayMatchOddsAPI(matchesInfo.getId(), matchType);
            }
            else {
                matchType = "upcoming";
                callUpcomingMatchOddsAPI(matchesInfo.getId());
            }
        }

        return view;
    }

    private void setRecyclerViewMargin(RelativeLayout view, float marginBottom) {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT
        );
        Resources r = context.getResources();
        params.bottomMargin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                marginBottom,
                r.getDisplayMetrics()
        );
        view.setLayoutParams(params);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void findViewsById(View view) {
        topLayout = view.findViewById(R.id.top_layout);
        recyclerView = view.findViewById(R.id.recycler_view_match_odds);
        bottomSheetRecyclerView = view.findViewById(R.id.bets_recycler_view);
        betsCountView = view.findViewById(R.id.bets_count);
        placeBet = view.findViewById(R.id.button_place_bet);
        matchStatusLayout = view.findViewById(R.id.match_status_layout);
        matchStatusLayout.setVisibility(View.GONE);
        matchStatusText = view.findViewById(R.id.post);
        firstInningsTextView = view.findViewById(R.id.first_innings);
        secondInningsTextView = view.findViewById(R.id.second_innings);
        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        bottom_sheet.setVisibility(View.GONE);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        setRecyclerViewMargin(topLayout, 0);
        placeBet.setOnClickListener(view1 ->  {
            if (NetworkState.isNetworkAvailable(context)) {
                onPlaceBetClicked();
            }
            else {
                if (!alertDialogHelper.isShowing()) {
                    alertDialogHelper.showAlertDialog(context,
                            getString(R.string.internet_error_title),
                            getString(R.string.no_internet_message));
                }
            }
        });
        sheetBehavior.addBottomSheetCallback(new BetSlipSheetCallBack());
        coinsRemainingTextView = view.findViewById(R.id.points_remaining);
        coinsRemainingLabelTextView = view.findViewById(R.id.points_remaining_label);
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        if (type.equals(MYACCOUNTTYPE)) {
            Log.d("getResult " + MYACCOUNTTYPE, result.toString());
            try {
                String bonus = result.getString("bonous_amount");
                coinsRemaining = Float.parseFloat(bonus);
                if (coinsRemaining > 0) {
                    coinsRemainingTextView.setText(bonus);
                    coinsRemainingLabelTextView.setVisibility(View.VISIBLE);
                }
                else {
                    coinsRemainingTextView.setText("No coins left to place bet");
                    coinsRemainingLabelTextView.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Context mContext, String type, String message) {
        Log.d(TAG, "getResult "+message);
    }

    private class BetSlipSheetCallBack extends BottomSheetBehavior.BottomSheetCallback {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:
                case BottomSheetBehavior.STATE_DRAGGING:
                case BottomSheetBehavior.STATE_SETTLING:
                case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    break;
                case BottomSheetBehavior.STATE_EXPANDED: {
                    if (listener != null) {
                        listener.onBottomSheetExpanded();
                    }
                }
                break;
                case BottomSheetBehavior.STATE_COLLAPSED: {
                    if (listener != null) {
                        listener.onBottomSheetCollapsed();
                    }
                }
                break;
            }
        }

        @Override public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
    }

    private void onPlaceBetClicked() {
        if (matchOddsList.size() == 0) {
            Toast.makeText(context, "Please add stakes", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean addedBetAmount = true;
            for (int i = 0; i < matchOddsList.size(); i++) {
                MatchOdds matchOdds = matchOddsList.get(i);
                if (matchOdds.isSelected() && matchOdds.getBetAmount() == 0) {
                    addedBetAmount = false;
                    break;
                }
            }

            if (addedBetAmount) {
                if (coinsRemaining > 0) {
                    if (coinsRemainingOnBetAmountChanged > 0) {
                        callPlaceBetAPI();
                    }
                    else {
                        showFailureAlert("You do not have sufficient coins to place this bet. Please remove few.");
                    }
                }
                else {
                    showFailureAlert("There are no coins for you to place bet.");
                }
            }
            else {
                Toast.makeText(context, "Please enter stakes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dismissProgressDialog(AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }

    private void callInPlayMatchOddsAPI(String id, String matchType) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

//        String url = "https://api.b365api.com/v2/bet365/prematch?token=61925-2bBIpJrOkeLtND&FI=" + id;


//        String url = "http://3.236.20.78:4000/" + matchType + "/preodds?FI=" + id;
//        String url = "http://3.236.20.78:4000/" + matchType + "/preodds?FI=" + id;
//        String url = "http://3.236.20.78:7000/inplay/overodds?FI=" + id;
        String url = "http://3.236.20.78:7000/" + matchType + "/overodds?FI=" + id;
//        String url = "http://3.236.20.78:7000/inplay/overodds?FI=93429263";
        Log.d(TAG, url);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url ,
                null,
                response -> {
                    dismissProgressDialog(progressAlertDialog);
                    Log.d(TAG, "callMatchesAPI response: " + response.toString());
                    onInPlayMatchOddsResponse(response);
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

    private void onInPlayMatchOddsResponse(JSONObject response) {
        try {
            JSONArray inplayoddsArray = response.getJSONArray("arr");
            if (inplayoddsArray.length() == 0) {
                return;
            }

            if (matchesInfo.isMatchInProgress()) {
                canCallLiveScroeAPI = true;
                initialiseRetrofit();
            }


            oddsCategoryList = new ArrayList<>();
            oddsCategoryListFinal = new ArrayList<>();
            String mgName = "";
            String previousMGName = "";
            List<MatchOdds> matchOddsList = new ArrayList<>();
            for (int i = 0; i < inplayoddsArray.length(); i++) {
                JSONObject oddsObject = inplayoddsArray.getJSONObject(i);

                if (oddsObject.getString("type").equalsIgnoreCase("MG")) {
                    Log.d(TAG, oddsObject.getString("NA"));
                    mgName = oddsObject.getString("NA");
                    if (!previousMGName.equalsIgnoreCase(mgName) && i > 0) {
                        oddsCategoryListFinal.add(new OddsCategory(previousMGName, matchOddsList));
                        matchOddsList = new ArrayList<>();
                    }
                } else if (oddsObject.getString("type").equalsIgnoreCase("PA")) {
                    previousMGName = mgName;
                    MatchOdds matchOdds = new MatchOdds();
                    if (oddsObject.has("NA")) {
                        matchOdds.setName(oddsObject.getString("NA"));

                        Log.d(TAG, oddsObject.getString("NA"));
                        if (oddsObject.has("OD")) {
                            String odds = oddsObject.getString("OD");
                            String[] oddsArray = odds.split("/");
                            float oddFloat = 1 + Float.parseFloat(oddsArray[0])/Float.parseFloat(oddsArray[1]);
                            DecimalFormat df = new DecimalFormat("#.00");
                            oddFloat = Float.parseFloat(df.format(oddFloat));
                            matchOdds.setOdds(String.valueOf(oddFloat));
                        }

                        if (oddsObject.has("ID")) {
                            String id1 = oddsObject.getString("ID");
                            matchOdds.setId(id1);
                        }
                    }
                    else {
                        matchOdds.setName("");
                    }

                    matchOddsList.add(matchOdds);
                    oddsCategoryList.add(new OddsCategory(mgName, matchOddsList));
                    if (i == inplayoddsArray.length() - 1) {
                        oddsCategoryListFinal.add(new OddsCategory(previousMGName, matchOddsList));
                    }
                }
            }

            Log.d(TAG, oddsCategoryListFinal.toString());

            oddsCategoryAdapter = new OddsCategoryAdapter(context, oddsCategoryListFinal);
            oddsCategoryAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
                @Override
                public void onListItemExpanded(int position) {
//                                OddsCategory expandedMovieCategory = oddsCategoryListFinal.get(position);
                }

                @Override
                public void onListItemCollapsed(int position) {
//                                OddsCategory collapsedMovieCategory = oddsCategoryListFinal.get(position);
                }
            });

            oddsCategoryAdapter.setChildClickListener(MatchOddsFragment.this);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    linearLayoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);


            recyclerView.setAdapter(oddsCategoryAdapter);

            List<MatchOdds> matchOdds = new ArrayList<>();
            bottomsheetRecyclerViewAdapter = new BottomsheetRecyclerViewAdapter(context, matchOdds);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
            bottomSheetRecyclerView.setLayoutManager(linearLayoutManager2);
            bottomSheetRecyclerView.addItemDecoration(new DividerItemDecoration(bottomSheetRecyclerView.getContext(),
                    linearLayoutManager2.getOrientation()));
            bottomsheetRecyclerViewAdapter.setItemChangeListener(MatchOddsFragment.this);
            bottomSheetRecyclerView.setAdapter(bottomsheetRecyclerViewAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void callUpcomingMatchOddsAPI(String id) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

//        String url = "https://api.b365api.com/v2/bet365/prematch?token=61925-2bBIpJrOkeLtND&FI=" + id;


        if (matchesInfo.isMatchInProgress()) {
            matchType = "inplay";
        }
        else {
            matchType = "upcoming";
        }
        String url = "http://3.236.20.78:4000/" + matchType + "/preodds?FI=" + id;
//        String url = "http://3.236.20.78:7000/inplay/overodds?FI=" + id;
//        String url = "http://3.236.20.78:7000/inplay/overodds?FI=93429263";
        Log.d(TAG, url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url ,
                null,
                response -> {

                    dismissProgressDialog(progressAlertDialog);
                    Log.d(TAG, "callMatchesAPI response: " + response.toString());
                    onUpcomingMatchResponse(response);
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

    private void onUpcomingMatchResponse(JSONObject response) {
        try {
            JSONArray resultsArray = response.getJSONArray("results");
            if (resultsArray.length() == 0) {
                return;
            }
            if (matchesInfo.isMatchInProgress()) {
                canCallLiveScroeAPI = true;
                initialiseRetrofit();
            }

            for (int i = 0; i < resultsArray.length(); i++) {
                Log.d(TAG, "Results Array " + resultsArray.get(i).toString());
                JSONObject resultsJSONObject = resultsArray.getJSONObject(i);
                if (resultsJSONObject != null) {
                    oddsCategoryList = new ArrayList<>();
                    JSONObject               mainJSONObject   = resultsJSONObject.getJSONObject("main");
                    JSONObject sPJSONObject = mainJSONObject.getJSONObject("sp");
                    if (sPJSONObject.has("to_win_the_match")) {
                        JSONObject toWinMatchObject = sPJSONObject.getJSONObject("to_win_the_match");
                        addWinMatchesToList(oddsCategoryList, toWinMatchObject);
                    }

                    if (sPJSONObject.has("to_win_the_toss")) {
                        JSONObject toWinTossObject = sPJSONObject.getJSONObject("to_win_the_toss");
                        addWinMatchesToList(oddsCategoryList, toWinTossObject);
                    }

                    if (sPJSONObject.has("top_team_batsman")) {
                        JSONObject top_team_batsman = sPJSONObject.getJSONObject("top_team_batsman");
                        addWinMatchesToList(oddsCategoryList, top_team_batsman);
                    }

                    if (sPJSONObject.has("1st_over_total_runs")) {
                        JSONObject first_over_total_runs = sPJSONObject.getJSONObject("1st_over_total_runs");
                        addWinMatchesToList(oddsCategoryList, first_over_total_runs);
                    }

                    if (sPJSONObject.has("total_runs_in_match")) {
                        JSONObject total_runs_in_match = sPJSONObject.getJSONObject("total_runs_in_match");
                        addWinMatchesToList(oddsCategoryList, total_runs_in_match);
                    }

                    if (sPJSONObject.has("top_team_bowler")) {
                        JSONObject top_team_bowler = sPJSONObject.getJSONObject("top_team_bowler");
                        addWinMatchesToList(oddsCategoryList, top_team_bowler);
                    }

                    if (sPJSONObject.has("total_match_sixes")) {
                        JSONObject total_match_sixes = sPJSONObject.getJSONObject("total_match_sixes");
                        addWinMatchesToList(oddsCategoryList, total_match_sixes);
                    }

                    if (sPJSONObject.has("team_total_match_sixes")) {
                        JSONObject team_total_match_sixes = sPJSONObject.getJSONObject("team_total_match_sixes");
                        addWinMatchesToList(oddsCategoryList, team_total_match_sixes);
                    }

                    if (sPJSONObject.has("batsman_match_runs")) {
                        JSONObject batsman_match_runs = sPJSONObject.getJSONObject("batsman_match_runs");
                        addWinMatchesToList(oddsCategoryList, batsman_match_runs);
                    }

                    if (sPJSONObject.has("total_match_fours")) {
                        JSONObject total_match_fours = sPJSONObject.getJSONObject("total_match_fours");
                        addWinMatchesToList(oddsCategoryList, total_match_fours);
                    }

                    if (sPJSONObject.has("team_total_match_fours")) {
                        JSONObject team_total_match_fours = sPJSONObject.getJSONObject("team_total_match_fours");
                        addWinMatchesToList(oddsCategoryList, team_total_match_fours);
                    }

                    if (sPJSONObject.has("1st_innings_score")) {
                        JSONObject first_innings_score = sPJSONObject.getJSONObject("1st_innings_score");
                        addWinMatchesToList(oddsCategoryList, first_innings_score);
                    }

                    if (sPJSONObject.has("player_of_the_match")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("player_of_the_match");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("total_match_sixes")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("total_match_sixes");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("team_total_match_sixes")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("team_total_match_sixes");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("batsman_match_runs")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("batsman_match_runs");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("total_match_fours")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("total_match_fours");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("team_total_match_fours")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("team_total_match_fours");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("1st_innings_score")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("1st_innings_score");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("player_of_the_match")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("player_of_the_match");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("player_performance")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("player_performance");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("batsman_total_match_sixes")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("batsman_total_match_sixes");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("batsman_total_match_fours")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("batsman_total_match_fours");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("batsman_to_score_a_fifty_in_the_match")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("batsman_to_score_a_fifty_in_the_match");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("highest_individual_score")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("highest_individual_score");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("most_match_sixes")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("most_match_sixes");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("most_match_fours")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("most_match_fours");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("player_to_score_most_sixes")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("player_to_score_most_sixes");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("runs_at_fall_of_1st_wicket")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("runs_at_fall_of_1st_wicket");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("team_to_make_highest_1st_6_overs_score")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("team_to_make_highest_1st_6_overs_score");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("a_hundred_to_be_scored_in_the_match")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("a_hundred_to_be_scored_in_the_match");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("a_fifty_to_be_scored_in_the_match")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("a_fifty_to_be_scored_in_the_match");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("race_to_10_runs")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("race_to_10_runs");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("batsman_matches_(most_runs)")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("batsman_matches_(most_runs)");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("1st_wicket_method")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("1st_wicket_method");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("highest_opening_partnership")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("highest_opening_partnership");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("to_go_to_super_over")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("to_go_to_super_over");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("most_run_outs")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("most_run_outs");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("match_handicap")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("match_handicap");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    if (sPJSONObject.has("match_handicap")) {
                        JSONObject player_of_the_match = sPJSONObject.getJSONObject("match_handicap");
                        addWinMatchesToList(oddsCategoryList, player_of_the_match);
                    }

                    oddsCategoryAdapter = new OddsCategoryAdapter(context, oddsCategoryList);
                    oddsCategoryAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
                        @Override public void onListItemExpanded(int position) {}
                        @Override public void onListItemCollapsed(int position) {}
                    });

                    oddsCategoryAdapter.setChildClickListener(MatchOddsFragment.this);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                            linearLayoutManager.getOrientation());
                    recyclerView.addItemDecoration(dividerItemDecoration);


                    recyclerView.setAdapter(oddsCategoryAdapter);

                    List<MatchOdds> matchOdds = new ArrayList<>();
                    bottomsheetRecyclerViewAdapter = new BottomsheetRecyclerViewAdapter(context, matchOdds);
                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
                    bottomSheetRecyclerView.setLayoutManager(linearLayoutManager2);
                    bottomSheetRecyclerView.addItemDecoration(new DividerItemDecoration(bottomSheetRecyclerView.getContext(),
                            linearLayoutManager2.getOrientation()));
                    bottomsheetRecyclerViewAdapter.setItemChangeListener(MatchOddsFragment.this);
                    bottomSheetRecyclerView.setAdapter(bottomsheetRecyclerViewAdapter);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addWinMatchesToList(List<OddsCategory> oddsCategoryList,
                                     JSONObject toWinMatchObject) throws JSONException
    {
        JSONArray       oddsArray        = toWinMatchObject.getJSONArray("odds");
        List<MatchOdds> winMatchOddsList = new ArrayList<>();
        String oddCategoryName = "";
        for (int j = 0; j < oddsArray.length();j++) {
            JSONObject jsonObject = oddsArray.getJSONObject(j);
            MatchOdds matchOdds = new MatchOdds();
            String id = jsonObject.getString("id");
            String odds = jsonObject.getString("odds");
            String name = jsonObject.getString("name");
            oddCategoryName = toWinMatchObject.getString("name");
            if (name.equalsIgnoreCase("1")) {
                name = matchesInfo.getHomeTeam();
            }
            else if (name.equalsIgnoreCase("2")){
                name = matchesInfo.getVisitorsTeam();
            }

            matchOdds.setId(id);
            matchOdds.setOdds(odds);
            matchOdds.setName(name);
            matchOdds.setCategoryName(oddCategoryName);
            winMatchOddsList.add(matchOdds);
        }
        if (winMatchOddsList.size() > 0) {
            OddsCategory oddsCategory = new OddsCategory(oddCategoryName, winMatchOddsList);
            oddsCategoryList.add(oddsCategory);
        }
    }


    @Override
    public void onChildClicked(MatchOdds matchOdds) {
        if (previousValue.equalsIgnoreCase("true")) {
            return;
        }
        matchOdds.setSelected(!matchOdds.isSelected());
        Log.d(TAG, "onChildClicked: " + matchOdds.toString());

        bottomsheetRecyclerViewAdapter.updateList(matchOdds);
        betsCountView.setText(String.valueOf(bottomsheetRecyclerViewAdapter.getItemCount()));
        if (bottomsheetRecyclerViewAdapter.getItemCount() > 0) {
            bottom_sheet.setVisibility(View.VISIBLE);
            setRecyclerViewMargin(topLayout, context.getResources().getDimension(R.dimen.odds_parent_layout_margin_bottom));
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else {
            bottom_sheet.setVisibility(View.GONE);
            setRecyclerViewMargin(topLayout, 0);
        }
        oddsCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRemoved(MatchOdds matchOdds) {
        bottomsheetRecyclerViewAdapter.removeItem(matchOdds);
        matchOdds.setSelected(false);
        betsCountView.setText(String.valueOf(bottomsheetRecyclerViewAdapter.getItemCount()));
        oddsCategoryAdapter.notifyDataSetChanged();
        if (bottomsheetRecyclerViewAdapter.getItemCount() == 0) {
            bottom_sheet.setVisibility(View.GONE);
            setRecyclerViewMargin(topLayout, 0);
        }
    }

    private List<MatchOdds> matchOddsList = new ArrayList<>();
    @Override
    public void onBetAmountChanged(MatchOdds matchOdds) {
        coinsRemainingOnBetAmountChanged = coinsRemaining;
        float betAmount = 0.0f;
        float returnAmount = 0.0f;

        if (matchOddsList.size() == 0) {
            matchOddsList.add(matchOdds);
            betAmount = matchOdds.getBetAmount();
            returnAmount = matchOdds.getReturnAmount();
        }
        else {
            boolean isNewOddAdded = true;
            for (int i = 0; i< matchOddsList.size();i++) {
                if (matchOddsList.get(i).getId().equals(matchOdds.getId())) {
                    isNewOddAdded = false;
                    break;
                }
            }

            if (isNewOddAdded) {
                matchOddsList.add(matchOdds);
            }


            for (int i = 0; i< matchOddsList.size(); i++) {
                betAmount += matchOddsList.get(i).getBetAmount();
                returnAmount += matchOddsList.get(i).getReturnAmount();
            }
        }
        Log.d(TAG, "onBetAmountAdded " + betAmount);

        float totalBetAmount = betAmount;
        float totalReturnAmount = returnAmount;
        placeBet.setText(String.format("Place Bet Coins. %s\n\nTotal To Return Coins. %s",
                         totalBetAmount,
                         totalReturnAmount));
        if (betAmount > 0) {
            coinsRemainingOnBetAmountChanged  = coinsRemainingOnBetAmountChanged - totalReturnAmount;
            coinsRemainingTextView.setText(String.valueOf(coinsRemainingOnBetAmountChanged));
        }
    }

    private void callPlaceBetAPI() {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final String URL = "http://cricket.atreatit.com/myrest/user/match_bet_details";
        JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, URL, getInputJSON(matchType),
                response -> {
                    Log.d(TAG, "PlaceBet Response "+response.toString());
                    dismissProgressDialog(progressAlertDialog);
                    if (response.length() > 0) {
                        onPlaceBetResponse(response);
                    }

                }, volleyError -> {
                    dismissProgressDialog(progressAlertDialog);
                    showFailureAlert("Something went wrong !");
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SessionManager sessionManager = new SessionManager();
                headers.put("Authorization", sessionManager.getUser(context).getToken());
                Log.d(TAG, sessionManager.getUser(context).getToken());
                return headers;
            }
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                String responseString;
                JSONArray array = new JSONArray();
                if (response != null) {

                    try {
                        responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        JSONObject obj = new JSONObject(responseString);
                        (array).put(obj);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //return array;
                return Response.success(array, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        requestQueue.add(request_json);
    }

    private void onPlaceBetResponse(JSONArray response) {
        try {
            JSONObject responseObject = response.getJSONObject(0);
            if (responseObject.has("responsecode")) {
                String responseCode = responseObject.getString("responsecode");
                if (responseCode.equals("200")) {
                    showBetSuccessAlert();
                }
                else {
                    showFailureAlert("Something went wrong !");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clearBetSlip() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        for (int i = 0; i < oddsCategoryList.size(); i++) {
            List<MatchOdds> matchOddsList = oddsCategoryList.get(i).getChildItemList();
            for (int j = 0; j < matchOddsList.size(); j++) {
                MatchOdds matchOdds = matchOddsList.get(j);
                matchOdds.setSelected(false);
                matchOdds.setReturnAmount(0);
                matchOdds.setBetAmount(0);
            }
        }
        oddsCategoryAdapter.notifyDataSetChanged();
        bottomsheetRecyclerViewAdapter.removeAll();
        betsCountView.setText("");
        bottom_sheet.setVisibility(View.GONE);
        setRecyclerViewMargin(topLayout, 0);
    }

    private void showBetSuccessAlert() {
        /*CFAlertDialog.Builder builder = new CFAlertDialog.Builder(context)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Success")
                .setMessage("Your bet has been placed successfully.")
                .setCancelable(false);

        builder.addButton("OK", Color.parseColor("#FFFFFF"),
                Color.parseColor("#429ef4"),
                CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) ->
                {
                    dialog.dismiss();
                    clearBetSlip();
                });

        builder.show();*/

        new FancyGifDialog.Builder((Activity) context)
                .setTitle("Success")
                .setMessage("Your bet has been placed successfully.")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("OK")
                .setGifResource(R.drawable.common_gif)
                .isCancellable(true)
                .OnPositiveClicked(() -> clearBetSlip())
                .build();
    }

    private void showFailureAlert(String message) {
        final FlatDialog flatDialog = new FlatDialog(context);
        flatDialog.setIcon(R.drawable.crying)
                .setTitle("Error")
                .setTitleColor(Color.parseColor("#000000"))
                .setSubtitle(message)
                .setSubtitleColor(Color.parseColor("#000000"))
                .setBackgroundColor(Color.parseColor("#a26ea1"))
                .setFirstButtonColor(Color.parseColor("#f18a9b"))
                .setFirstButtonTextColor(Color.parseColor("#000000"))
                .setFirstButtonText("OK")
                .withFirstButtonListner(view -> flatDialog.dismiss())
                .show();
    }

    private JSONArray getInputJSON(String matchType) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0;i< matchOddsList.size();i++) {
            MatchOdds matchOdds = matchOddsList.get(i);
            if (matchOdds.isSelected()) {
                JSONObject jsonParam = new JSONObject();

                /*{
                    "betid": "1004874033",
                        "fi_id": "93023807",
                        "matchname": "T20 Blast",
                        "home_team": "Kolkatta night riders",
                        "visitor_team": "delhi capitals",
                        "match_date": "212112",
                        "match_time": "212112",
                        "batting_team": "delhi capitals",
                        "team_name": "Kent-Sussex",
                        "status": "upcoming",
                        "bet_date": "212112",
                        "bet_time": "212112",
                        "odd_name": "Kent",
                        "odd_value": "1.90",
                        "bet_value": 3.799999952316284,
                        "bet_amount" : 500
                }*/
                try {
                    jsonParam.put("betid", matchOdds.getId());
                    if (matchesInfo != null) {
                        jsonParam.put("fi_id", matchesInfo.getId());
                        jsonParam.put("matchname", matchesInfo.getLeagueName());
                        jsonParam.put("match_date", "212112");
                        jsonParam.put("match_time", "212112");
                        jsonParam.put("team_name", matchesInfo.getHomeTeam() + "-" + matchesInfo.getVisitorsTeam());
                        jsonParam.put("status", matchType);
                        jsonParam.put("visitor_team", matchesInfo.getVisitorsTeam());
                        jsonParam.put("home_team", matchesInfo.getHomeTeam());
                    }

                    jsonParam.put("bet_date", "212112");
                    jsonParam.put("bet_time", "212112");
                    jsonParam.put("odd_name", matchOdds.getCategoryName());
                    jsonParam.put("odd_value", matchOdds.getOdds());
                    jsonParam.put("bet_value", String.valueOf(matchOdds.getName()));
                    jsonParam.put("bet_amount", String.valueOf(matchOdds.getReturnAmount()));
                    jsonParam.put("batting_team", "NA");
                    jsonArray.put(jsonParam);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, jsonArray.toString());
        }
        return jsonArray;
    }

    private void initialiseRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.236.20.78:4000")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(APIService.class);


        disposable = Observable.interval(1000, 5000, TimeUnit.MILLISECONDS)
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(this::callLiveScoreAPI, this::onError);

    }

    private void callLiveScoreAPI(Long aLong) {
        //http://3.236.20.78:4000/goalserve/live?hometeam=Gloucestershire&vistorteam=Birmingham%20Bears
        Observable<LiveScroreData> observable = apiService.getLiveScore(matchesInfo.getHomeTeam(),matchesInfo.getVisitorsTeam());
//        Observable<LiveScroreData> observable = apiService.getLiveScore("Gloucestershire","Birmingham Bears");
        observable.subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread())
                .map(result -> result)
                .subscribe(this::handleLiveScroreAPIResults, this::handleCallLiveScoreAPIError);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (canCallLiveScroeAPI &&
            matchesInfo != null &&
            matchesInfo.isMatchInProgress() &&
            disposable.isDisposed()) {
            disposable = Observable.interval(1000, 5000,
                    TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::callLiveScoreAPI, this::onError);
        }
    }

    private String previousValue = "false";
    private String firstInningsTeam;
    private String secondInningsTeam;
    private void handleLiveScroreAPIResults(LiveScroreData liveScoreData) {
        matchStatusText.setText("");
        Log.d(TAG, matchesInfo.toString());
        Log.d(TAG, "Overended: "+liveScoreData.getOverended());
        Log.d(TAG, "Overended: "+liveScoreData.toString());
        if (liveScoreData.getFirstinnings() != null && liveScoreData.getFirstinnings().getScore() != null) {
            Log.d(TAG, "First Innings: " + liveScoreData.getFirstinnings().toString());
            String firstInningsScore = liveScoreData.getFirstinnings().getScore();
            firstInningsTeam = liveScoreData.getFirstinnings().getTeam();
            if (firstInningsScore != null) {
                firstInningsTextView.setText(String.format("First Innings: %s", firstInningsScore));
            }
        }

        if (liveScoreData.getSecondinnings() != null && liveScoreData.getSecondinnings().getScore() != null) {
            Log.d(TAG, "Second Innings: " + liveScoreData.getSecondinnings().toString());
            String secondInningsScore = liveScoreData.getSecondinnings().getScore();
            secondInningsTeam = liveScoreData.getSecondinnings().getTeam();
            if (secondInningsScore != null) {
                secondInningsTextView.setText(String.format("Second Innings: %s", secondInningsScore));
            }
        }

        if (liveScoreData.getPost() != null) {
            String post = liveScoreData.getPost();
            Log.d(TAG, "Post: " + post);
            matchStatusText.setText(post);
        }

        matchStatusLayout.setVisibility(View.VISIBLE);
        String overEnded = liveScoreData.getOverended();

        if (!previousValue.equalsIgnoreCase(overEnded)) {
            previousValue = overEnded;
            if (overEnded.equalsIgnoreCase("true")) {
                suspendBet();
            }
            else {
                callInPlayMatchOddsAPI(matchesInfo.getId(), matchType);
            }
        }
    }

    private void suspendBet() {
        for (int i = 0; i < oddsCategoryListFinal.size(); i++) {
            List<MatchOdds> matchOddsList = oddsCategoryListFinal.get(i).getChildItemList();
            for (int j = 0; j < matchOddsList.size(); j++) {
                matchOddsList.get(j).setOdds("Suspended");
            }
        }
        oddsCategoryAdapter.notifyDataSetChanged();
        bottom_sheet.setVisibility(View.GONE);
    }

    private void onError(Throwable throwable) {
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
        if (throwable.getMessage() != null) {
            Log.d(TAG, throwable.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (canCallLiveScroeAPI && matchesInfo.isMatchInProgress()) {
            disposable.dispose();
        }
    }

    private void handleCallLiveScoreAPIError(Throwable t) {
        if (t.getMessage() != null) {
            Log.d(TAG, t.getMessage());
        }
        suspendBet();
    }

    private void callMyAccountDetails(boolean isShowLoader) {
        try {
            apiRequestManager.callAPIWithAuthorization(MYACCOUNT,
                    null, context, (Activity) context, MYACCOUNTTYPE,
                    isShowLoader, responseManager, sessionManager.getUser(context).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createRequestJsonWin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onBottomSheetExpanded();
        void onBottomSheetCollapsed();
    }
}
