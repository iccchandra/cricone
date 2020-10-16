package com.onecricket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.onecricket.R;
import com.onecricket.fragment.ChatRoomFragment;
import com.onecricket.fragment.LeaderboardFragment;
import com.onecricket.fragment.MatchOddsFragment;
import com.onecricket.pojo.MatchesInfo;

public class MatchOddsTabsActivity extends AppCompatActivity implements MatchOddsFragment.Listener{

    private static final String TAG = "MatchOddsTabsActivity";
    private Toolbar toolbar;
    private MatchesInfo matchesInfo;
    private ChatRoomFragment chatRoomFragment;
    private String title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_odds_tabs);

        toolbar = findViewById(R.id.toolbar);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            matchesInfo = (MatchesInfo) extras.getSerializable("MatchInfo");
            if (matchesInfo != null) {
                Log.d(TAG, String.valueOf(matchesInfo.getId()));
/*
                toolbar.setTitle(matchesInfo.getHomeTeam().substring(0,3).toUpperCase() +
                                 " Vs " +
                                 matchesInfo.getVisitorsTeam().substring(0,3).toUpperCase());
*/

                String homeTeam = matchesInfo.getHomeTeam();
                String visitorTeam = matchesInfo.getVisitorsTeam();
                if (homeTeam.length() > 2) {
                    homeTeam = homeTeam.substring(0,3).toUpperCase();
                }

                if (visitorTeam.length() > 2) {
                    visitorTeam = visitorTeam.substring(0,3).toUpperCase();
                }
                title = homeTeam + " Vs " + visitorTeam;
                String contestName = matchesInfo.getContestName();
                if (contestName != null && contestName.length() > 0) {
                    title = title + "(" + contestName +")";
                }
                toolbar.setTitle(title);
            }
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Match Odds"));
        tabLayout.addTab(tabLayout.newTab().setText("Chat Room"));
        tabLayout.addTab(tabLayout.newTab().setText("Rankings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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

    @Override
    public void onBottomSheetExpanded() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onBottomSheetCollapsed() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:

                    bundle.putSerializable("MatchInfo", matchesInfo);
                    MatchOddsFragment matchOddsFragment = new MatchOddsFragment();
                    matchOddsFragment.setListener(MatchOddsTabsActivity.this);
                    matchOddsFragment.setArguments(bundle);
                    return matchOddsFragment;
                case 1:
                    if (chatRoomFragment == null) {
                        chatRoomFragment = new ChatRoomFragment();
                    }
                    return chatRoomFragment;
                case 2:
                    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
                    if (matchesInfo.getContestId() != null && matchesInfo.getContestId().trim().length() > 0) {
                        bundle.putBoolean("IS_PRIVATE_CONTEST", true);
                    }
                    else {
                        bundle.putBoolean("IS_PRIVATE_CONTEST", false);
                    }
                    bundle.putString("F_ID", matchesInfo.getId());
                    bundle.putSerializable("matchesInfo", matchesInfo);
                    leaderboardFragment.setArguments(bundle);
                    return leaderboardFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
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
            onShareClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onShareClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "I am betting on " + title + ". Join me if you are interested.");
        intent.setType("text/plain");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
        }

    }
}
