package com.onecricket.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onecricket.Bean.BeanBanner;
import com.onecricket.Bean.BeanHomeMatches;
import com.onecricket.adapter.AdapterHomeBanner;
import com.onecricket.adapter.AdapterMyMatches;
import com.onecricket.databinding.FragmentFixturesBinding;
import com.onecricket.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onecricket.APICallingPackage.Class.APIRequestManager;
import com.onecricket.APICallingPackage.Interface.ResponseManager;
import com.onecricket.Bean.BeanHomeFixtures;
import com.onecricket.APICallingPackage.Config;
import com.onecricket.activity.ContestListActivity;
import com.onecricket.activity.HomeActivity;
import com.onecricket.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.onecricket.APICallingPackage.Class.Validations.ShowToast;
import static com.onecricket.APICallingPackage.Config.HOMEBANNER;
import static com.onecricket.APICallingPackage.Config.MYMATCHRECORD;
import static com.onecricket.APICallingPackage.Constants.HOMEBANNERTYPE;
import static com.onecricket.APICallingPackage.Constants.MYMATCHRECORDTYPE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentFixtures extends Fragment implements ResponseManager {
    HomeActivity activity;
    Context context;
    AdapterFixturesList adapterFixturesList;
    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    public static String TeamImage1, TeamImage2;
    FragmentFixturesBinding binding;
    private ImageView[] dots;
    private int dotscount;
    AdapterMyMatches adapterMyMatches;
    SessionManager sessionManager;

    AdapterHomeBanner adapterHomeBanner;
    private ImageView[] dots1;
    private int dotscount1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFixturesBinding.inflate(inflater, container, false);
        context = activity = (HomeActivity) getActivity();
        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);
        sessionManager = new SessionManager();
        //binding.RvHomeFixtures.setHasFixedSize(true);
        binding.RvHomeFixtures.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.RvHomeFixtures.setLayoutManager(mLayoutManager);
        binding.RvHomeFixtures.setItemAnimator(new DefaultItemAnimator());
        binding.VPBanner.setNestedScrollingEnabled(false);
        binding.VPMyMatches.setNestedScrollingEnabled(false);


        binding.swipeRefreshLayout.setRefreshing(true);
        callMyMatchRecord(false);
        callHomeBanner(false);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callMyMatchRecord(false);
            }
        });

        binding.tvMyMatchViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.getInstance().callTab();
            }
        });

        return binding.getRoot();
    }


    private void callMyMatchRecord(boolean isShowLoader) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            apiRequestManager.callAPI(MYMATCHRECORD,
                    jsonObject, context, activity, MYMATCHRECORDTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callHomeBanner(boolean isShowLoader) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", sessionManager.getUser(context).getUser_id());
            apiRequestManager.callAPI(HOMEBANNER,
                    jsonObject, getActivity(), getActivity(), HOMEBANNERTYPE,
                    isShowLoader, responseManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {

        if (type.equals(HOMEBANNERTYPE)) {
            try {
                JSONArray jsonArray = result.getJSONArray("data");
                if (jsonArray.length() > 0) {
                    binding.RLHomeBanner.setVisibility(View.VISIBLE);
                    List<BeanBanner> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(),
                            new TypeToken<List<BeanBanner>>() {
                            }.getType());
                    adapterHomeBanner = new AdapterHomeBanner(beanHomeFixtures, getActivity());
                    binding.VPBanner.setAdapter(adapterHomeBanner);
                    BannerDotView();
                } else {
                    binding.RLHomeBanner.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapterHomeBanner.notifyDataSetChanged();

        } else if (type.equals(MYMATCHRECORDTYPE)) {
            binding.swipeRefreshLayout.setRefreshing(false);
            try {
                JSONArray myMatch = result.getJSONArray("myMatch");
                if (myMatch.length() > 0) {
                    binding.RLMyMatchesHeading.setVisibility(View.VISIBLE);
                    binding.RLMyMatchVP.setVisibility(View.VISIBLE);
                    List<BeanHomeMatches> beanMyFixtures = new Gson().fromJson(myMatch.toString(),
                            new TypeToken<List<BeanHomeMatches>>() {
                            }.getType());
                    adapterMyMatches = new AdapterMyMatches(beanMyFixtures, activity);
                    binding.VPMyMatches.setAdapter(adapterMyMatches);
                    DotView();
                    adapterMyMatches.notifyDataSetChanged();
                } else {
                    binding.RLMyMatchesHeading.setVisibility(View.GONE);
                    binding.RLMyMatchVP.setVisibility(View.GONE);
                }

                JSONArray jsonArray = result.getJSONArray("fixtureMatch");
                if (jsonArray.length() > 0) {
                    binding.RvHomeFixtures.setVisibility(View.VISIBLE);
                    binding.tvUpcomingMatch.setVisibility(View.VISIBLE);
                    binding.tvNoDataAvailable.setVisibility(View.GONE);
                    List<BeanHomeFixtures> beanHomeFixtures = new Gson().fromJson(jsonArray.toString(),
                            new TypeToken<List<BeanHomeFixtures>>() {
                            }.getType());
                    adapterFixturesList = new AdapterFixturesList(beanHomeFixtures, activity);
                    binding.RvHomeFixtures.setAdapter(adapterFixturesList);
                    adapterFixturesList.notifyDataSetChanged();
                } else {
                    binding.RvHomeFixtures.setVisibility(View.GONE);
                    binding.tvUpcomingMatch.setVisibility(View.GONE);
                    binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(Context mContext, String type, String message) {
        binding.swipeRefreshLayout.setRefreshing(false);
        if (type.equals(HOMEBANNERTYPE)) {
            binding.RLHomeBanner.setVisibility(View.GONE);
        } else {
            binding.RvHomeFixtures.setVisibility(View.GONE);
            binding.tvUpcomingMatch.setVisibility(View.GONE);
            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
            binding.RLMyMatchesHeading.setVisibility(View.GONE);
            binding.RLMyMatchVP.setVisibility(View.GONE);
        }
    }

    public void DotView() {
        binding.SliderDots.removeAllViews();
        dotscount = adapterMyMatches.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            binding.SliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.active_dot));
        binding.VPMyMatches.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void BannerDotView() {
        dotscount1 = adapterHomeBanner.getCount();
        dots1 = new ImageView[dotscount1];

        for (int i = 0; i < dotscount1; i++) {

            dots1[i] = new ImageView(getActivity());
            dots1[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            binding.SliderDots1.addView(dots1[i], params);

        }

        dots1[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.active_dot));

        binding.VPBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount1; i++) {
                    dots1[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                            R.drawable.non_active_dot));
                }
                dots1[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class AdapterFixturesList extends RecyclerView.Adapter<AdapterFixturesList.MyViewHolder> {
        private List<BeanHomeFixtures> mListenerList;
        Context mContext;


        public AdapterFixturesList(List<BeanHomeFixtures> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TeamOneName, tv_TeamsName, tv_TimeRemained, tv_TeamTwoName, tv_MatchTime;
            ImageView im_Team1, im_Team2;
            CountDownTimer countDownTimer;

            public MyViewHolder(View view) {
                super(view);

                im_Team1 = view.findViewById(R.id.im_Team1);
                tv_TeamOneName = view.findViewById(R.id.tv_TeamOneName);
                tv_TeamsName = view.findViewById(R.id.tv_TeamsName);
                tv_TimeRemained = view.findViewById(R.id.tv_TimeRemained);
                im_Team2 = view.findViewById(R.id.im_Team2);
                tv_TeamTwoName = view.findViewById(R.id.tv_TeamTwoName);
                tv_MatchTime = view.findViewById(R.id.tv_MatchTime);

            }

        }

        @Override
        public int getItemCount() {
            return mListenerList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_fixtures_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final String match_id = mListenerList.get(position).getMatch_id();
            String teamid1 = mListenerList.get(position).getTeamid1();
            String match_status = mListenerList.get(position).getMatch_status();
            String type = mListenerList.get(position).getType();
            final int time = mListenerList.get(position).getTime();
            String teamid2 = mListenerList.get(position).getTeamid2();
            final String team_name1 = mListenerList.get(position).getTeam_name1();
            final String team_image1 = mListenerList.get(position).getTeam_image1();
            final String team_short_name1 = mListenerList.get(position).getTeam_short_name1();
            final String team_name2 = mListenerList.get(position).getTeam_name2();
            final String team_image2 = mListenerList.get(position).getTeam_image2();
            final String team_short_name2 = mListenerList.get(position).getTeam_short_name2();
            final String eleven_out = mListenerList.get(position).getEleven_out();

            if (eleven_out.equals("1")) {
                holder.tv_MatchTime.setVisibility(View.VISIBLE);
                holder.tv_MatchTime.setText("Lineup Out");
            } else {
                holder.tv_MatchTime.setVisibility(View.GONE);
            }

            holder.tv_TeamOneName.setText(team_short_name1.trim());
            holder.tv_TeamTwoName.setText(team_short_name2.trim());
            holder.tv_TeamsName.setText(type);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE + team_image1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team1);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE + team_image2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.im_Team2);

            if (match_status.equals("Fixture")) {
                holder.tv_TimeRemained.setCompoundDrawablesWithIntrinsicBounds(R.drawable.watch_icon_cont, 0, 0, 0);
                holder.tv_TimeRemained.setText(time + "");


                if (holder.countDownTimer != null) {
                    holder.countDownTimer.cancel();
                }

                try {

                    int FlashCount = time;
                    long millisUntilFinished = FlashCount * 1000;

                    holder.countDownTimer = new CountDownTimer(millisUntilFinished, 1000) {

                        public void onTick(long millisUntilFinished) {

                            long Days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                            long Hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished));
                            long Minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished));
                            long Seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                            String format = "%1$02d";
                            holder.tv_TimeRemained.setText(String.format(format, Days) + ":" + String.format(format, Hours) + ":" + String.format(format, Minutes) + ":" + String.format(format, Seconds));
                        }

                        public void onFinish() {
                            callMyMatchRecord(false);
                            holder.tv_TimeRemained.setText("Entry Over!");
                        }

                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (holder.tv_TimeRemained.getText().toString().equals("Entry Over!")) {
                            ShowToast(context, "Entry Over");
                        } else {
                            TeamImage1 = team_image1;
                            TeamImage2 = team_image2;
                            Intent k = new Intent(activity, ContestListActivity.class);
                            k.putExtra("MatchId", match_id);
                            k.putExtra("Time", time + "");
                            k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                            k.putExtra("TeamsOneName", team_short_name1);
                            k.putExtra("TeamsTwoName", team_short_name2);
                            k.putExtra("TeamsOneImg", team_image1);
                            k.putExtra("TeamsTwoImg", team_image2);
                            startActivity(k);
                        }
                    }
                });


            }

        }

    }

}
