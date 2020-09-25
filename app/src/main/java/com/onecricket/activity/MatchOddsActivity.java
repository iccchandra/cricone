package com.onecricket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onecricket.R;
import com.onecricket.adapter.BottomsheetRecyclerViewAdapter;
import com.onecricket.adapter.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.onecricket.adapter.expandablerecyclerview.OddsCategoryAdapter;
import com.onecricket.chatroom.ChatRoomActivity;
import com.onecricket.pojo.MatchOdds;
import com.onecricket.pojo.MatchesInfo;
import com.onecricket.pojo.OddsCategory;
import com.onecricket.utils.CommonProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchOddsActivity extends AppCompatActivity implements OddsCategoryAdapter.ChildClickListener,
        BottomsheetRecyclerViewAdapter.ItemChangeListener
{

    private static final String                         TAG = "MatchOddsActivity";
    private MatchesInfo matchesInfo;
    private              AlertDialog                    progressAlertDialog;
    private              OddsCategoryAdapter            oddsCategoryAdapter;
    private              RecyclerView                   recyclerView;
    private              BottomSheetBehavior            sheetBehavior;
    private              Toolbar                        toolbar;
    private              RecyclerView                   bottomSheetRecyclerView;
    private              BottomsheetRecyclerViewAdapter bottomsheetRecyclerViewAdapter;
    private              TextView                       betsCountView;
    private Button                                      placeBet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_odds);

        findViewsById();

        toolbar.setTitle("Match Odds");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressAlertDialog = CommonProgressDialog.getProgressDialog(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            matchesInfo = (MatchesInfo) extras.getSerializable("MatchInfo");
            if (matchesInfo != null) {
                Log.d(TAG, String.valueOf(matchesInfo.getId()));
            }
        }



        if (matchesInfo != null && matchesInfo.getId() != null && matchesInfo.getId().trim().length() > 0) {
            callMatchInfoAPI(matchesInfo.getId());
        }

        RelativeLayout bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        toolbar.setVisibility(View.GONE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void findViewsById() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view_match_odds);
        bottomSheetRecyclerView = findViewById(R.id.bets_recycler_view);
        FloatingActionButton chatFab = findViewById(R.id.fab_chat);
        betsCountView = findViewById(R.id.bets_count);
        placeBet = findViewById(R.id.button_place_bet);
        chatFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MatchOddsActivity.this, ChatRoomActivity.class));
            }
        });
        placeBet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (matchOddsList.size() == 0) {
                    Toast.makeText(MatchOddsActivity.this, "Please add stakes", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void dismissProgressDialog(AlertDialog progressAlertDialog) {
        if (progressAlertDialog != null && progressAlertDialog.isShowing()) {
            progressAlertDialog.dismiss();
        }
    }


    private void callMatchInfoAPI(String id) {
        dismissProgressDialog(progressAlertDialog);
        progressAlertDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.b365api.com/v2/bet365/prematch?token=61925-2bBIpJrOkeLtND&FI=" + id;
        Log.d(TAG, url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url ,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgressDialog(progressAlertDialog);
                        Log.d(TAG, "callMatchesAPI response: " + response.toString());
                        try {
                            JSONArray resultsArray = response.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                Log.d(TAG, "Results Array " + resultsArray.get(i).toString());
                                JSONObject resultsJSONObject = resultsArray.getJSONObject(i);
                                if (resultsJSONObject != null) {
                                    List<OddsCategory> oddsCategoryList = new ArrayList<>();
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

                                    oddsCategoryAdapter = new OddsCategoryAdapter(MatchOddsActivity.this, oddsCategoryList);
                                    oddsCategoryAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
                                        @Override
                                        public void onListItemExpanded(int position) {
                                            OddsCategory expandedMovieCategory = oddsCategoryList.get(position);
                                            String toastMsg = getResources().getString(R.string.expanded, expandedMovieCategory.getName());
//                                         Toast.makeText(MatchOddsActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onListItemCollapsed(int position) {
                                            OddsCategory collapsedMovieCategory = oddsCategoryList.get(position);
                                            String toastMsg = getResources().getString(R.string.collapsed, collapsedMovieCategory.getName());
//                                         Toast.makeText(MatchOddsActivity.this, toastMsg, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    oddsCategoryAdapter.setChildClickListener(MatchOddsActivity.this);

                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MatchOddsActivity.this);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                            linearLayoutManager.getOrientation());
                                    recyclerView.addItemDecoration(dividerItemDecoration);


                                    recyclerView.setAdapter(oddsCategoryAdapter);

                                    List<MatchOdds> matchOdds = new ArrayList<>();
                                    bottomsheetRecyclerViewAdapter = new BottomsheetRecyclerViewAdapter(MatchOddsActivity.this, matchOdds);
                                    LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(MatchOddsActivity.this);
                                    bottomSheetRecyclerView.setLayoutManager(linearLayoutManager2);
                                    bottomSheetRecyclerView.addItemDecoration(new DividerItemDecoration(bottomSheetRecyclerView.getContext(),
                                            linearLayoutManager2.getOrientation()));
                                    bottomsheetRecyclerViewAdapter.setItemChangeListener(MatchOddsActivity.this);
                                    bottomSheetRecyclerView.setAdapter(bottomsheetRecyclerViewAdapter);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog(progressAlertDialog);
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                })
        {

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(jsonObjReq);
    }

    private void addWinMatchesToList(List<OddsCategory> oddsCategoryList,
                                     JSONObject toWinMatchObject) throws JSONException
    {
        JSONArray       oddsArray        = toWinMatchObject.getJSONArray("odds");
        List<MatchOdds> winMatchOddsList = new ArrayList<>();
        for (int j = 0; j < oddsArray.length();j++) {
            JSONObject jsonObject = oddsArray.getJSONObject(j);
            MatchOdds matchOdds = new MatchOdds();
            String id = jsonObject.getString("id");
            String odds = jsonObject.getString("odds");
            String name = jsonObject.getString("name");
            matchOdds.setId(id);
            matchOdds.setOdds(odds);
            matchOdds.setName(name);
            winMatchOddsList.add(matchOdds);
        }
        if (winMatchOddsList.size() > 0) {
            OddsCategory oddsCategory = new OddsCategory(toWinMatchObject.getString("name"), winMatchOddsList);
            oddsCategoryList.add(oddsCategory);
        }
    }


    @Override
    public void onChildClicked(MatchOdds matchOdds) {
        if (matchOdds.isSelected()) {
            matchOdds.setSelected(false);
            Toast.makeText(this, matchOdds.getName() + " unselected", Toast.LENGTH_SHORT).show();
        }
        else {
            matchOdds.setSelected(true);
            Toast.makeText(this, matchOdds.getName() + " selected", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "onChildClicked: " + matchOdds.toString());
        bottomsheetRecyclerViewAdapter.updateList(matchOdds);
        betsCountView.setText(String.valueOf(bottomsheetRecyclerViewAdapter.getItemCount()));
    }

    @Override
    public void onItemRemoved(MatchOdds matchOdds) {
        bottomsheetRecyclerViewAdapter.removeItem(matchOdds);
        betsCountView.setText(String.valueOf(bottomsheetRecyclerViewAdapter.getItemCount()));
    }


    private List<MatchOdds> matchOddsList = new ArrayList<>();


    private float totalBetAmount = 0.0f;
    private float totalReturnAmount = 0.0f;
    @Override
    public void onBetAmountChanged(MatchOdds matchOdds) {
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

        totalBetAmount = betAmount;
        totalReturnAmount = returnAmount;
        placeBet.setText(String.format("Place Bet Rs. %s\n\nTotal To Return Rs. %s", totalBetAmount,
                totalReturnAmount));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.match_odds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            Toast.makeText(getApplicationContext(), "Item 1 Selected", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
