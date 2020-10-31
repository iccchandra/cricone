package com.game.onecricket.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.game.onecricket.databinding.FragmentLiveBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.game.onecricket.APICallingPackage.Class.APIRequestManager;
import com.game.onecricket.APICallingPackage.Interface.ResponseManager;
import com.game.onecricket.Bean.BeanHomeLive;
import com.game.onecricket.APICallingPackage.Config;
import com.game.onecricket.activity.HomeActivity;
import com.game.onecricket.activity.MyJoinedLiveContestListActivity;
import com.game.onecricket.R;
import com.game.onecricket.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.game.onecricket.APICallingPackage.Config.HOMEFIXTURES;
import static com.game.onecricket.APICallingPackage.Constants.LIVEHOMETYPE;


public class FragmentLive extends Fragment implements ResponseManager {


    HomeActivity activity;
    Context context;

    AdapterLiveList adapterLiveList;

    ResponseManager responseManager;
    APIRequestManager apiRequestManager;
    FragmentLiveBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentLiveBinding.inflate(inflater,container,false);
        context = activity = (HomeActivity)getActivity();

        responseManager = this;
        apiRequestManager = new APIRequestManager(activity);


        binding.RvHomeLive.setHasFixedSize(true);
        binding.RvHomeLive.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.RvHomeLive.setLayoutManager(mLayoutManager);
        binding.RvHomeLive.setItemAnimator(new DefaultItemAnimator());



        binding.swipeRefreshLayout.post(() -> {
            binding.swipeRefreshLayout.setRefreshing(true);
            callHomeLive(false);
        }
        );

        binding.swipeRefreshLayout.setOnRefreshListener(() -> callHomeLive(false));

        return binding.getRoot();
    }



    private void callHomeLive(boolean isShowLoader) {

        SessionManager sessionManager = new SessionManager();
        try {

            apiRequestManager.callAPIWithAuthorization(HOMEFIXTURES,
                    createRequestJson(), context, activity, LIVEHOMETYPE,
                    isShowLoader,responseManager, sessionManager.getUser(context).getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    JSONObject createRequestJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", "Live");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public void getResult(Context mContext, String type, String message, JSONObject result) {
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.RvHomeLive.setVisibility(View.VISIBLE);


        try {
            JSONArray jsonArray = result.getJSONArray("data");
            List<BeanHomeLive> beanHomeLive = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<BeanHomeLive>>() {
            }.getType());
            adapterLiveList = new AdapterLiveList(beanHomeLive, activity);
            binding.RvHomeLive.setAdapter(adapterLiveList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        adapterLiveList.notifyDataSetChanged();
    }


    @Override
    public void onError(Context mContext, String type, String message) {
       binding.swipeRefreshLayout.setRefreshing(false);
        binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
    }


    public class AdapterLiveList extends RecyclerView.Adapter<AdapterLiveList.MyViewHolder> {
        private List<BeanHomeLive> mListenerList;
        Context mContext;

        public AdapterLiveList(List<BeanHomeLive> mListenerList, Context context) {
            mContext = context;
            this.mListenerList = mListenerList;

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_TeamOneName,tv_TeamsName,tv_TimeRemained,tv_TeamTwoName,tv_TeamOneScore,tv_TeamTwoScore,
                    tv_TeamOneOver,tv_TeamTwoOver,tv_MatchResult;
            LinearLayout linearLayout;
            ImageView im_Team1,im_Team2;


            public MyViewHolder(View view) {
                super(view);

                im_Team1 = view.findViewById(R.id.im_Team1);
                tv_TeamOneName = view.findViewById(R.id.tv_TeamOneName);
                tv_TeamsName = view.findViewById(R.id.tv_TeamsName);
                tv_TimeRemained = view.findViewById(R.id.tv_TimeRemained);
                im_Team2 = view.findViewById(R.id.im_Team2);
                tv_TeamTwoName = view.findViewById(R.id.tv_TeamTwoName);
                tv_TeamOneScore=view.findViewById(R.id.tv_TeamOneScore);
                tv_TeamTwoScore=view.findViewById(R.id.tv_TeamTwoScore);
                tv_TeamOneOver=view.findViewById(R.id.tv_TeamOneOver);
                tv_TeamTwoOver=view.findViewById(R.id.tv_TeamTwoOver);
                tv_MatchResult=view.findViewById(R.id.tv_MatchResult);

                linearLayout=view.findViewById(R.id.linearlayout2);
                linearLayout.setVisibility(View.GONE);


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
            String team_name1 = mListenerList.get(position).getTeam_name1();
            final String team_image1 = mListenerList.get(position).getTeam_image1();
            final String team_short_name1 = mListenerList.get(position).getTeam_short_name1();
            String team_name2 = mListenerList.get(position).getTeam_name2();
            final String team_image2 = mListenerList.get(position).getTeam_image2();
            final String team_short_name2 = mListenerList.get(position).getTeam_short_name2();
            final String team_one_score=mListenerList.get(position).getTeam1Score();
            final String team_two_score=mListenerList.get(position).getTeam2Score();
            final String team_one_over=mListenerList.get(position).getTeam1Over();
            final  String team_two_over=mListenerList.get(position).getTeam2Over();
            final String match_status_note=mListenerList.get(position).getMatch_status_note();
            holder.tv_TeamOneName.setText(team_short_name1);
            holder.tv_TeamTwoName.setText(team_short_name2);
            holder.tv_TeamsName.setText(type);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image1)
                   .transition(new DrawableTransitionOptions()
                                            .crossFade())
                                            .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.im_Team1);
            Glide.with(getActivity()).load(Config.TEAMFLAGIMAGE+team_image2)
                   .transition(new DrawableTransitionOptions()
                                            .crossFade())
                                            .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.im_Team2);


            if (match_status.equals("Live")){
                holder.tv_TimeRemained.setText("Live");
            }
            holder.tv_TeamOneScore.setText("Score:-"+team_one_score);
            holder.tv_TeamTwoScore.setText("Score:-"+team_two_score);
            holder.tv_TeamOneOver.setText("Over:-"+team_one_over);
            holder.tv_TeamTwoOver.setText("Over:-"+team_two_over);
            holder.tv_MatchResult.setText(match_status_note);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent k = new Intent(activity, MyJoinedLiveContestListActivity.class);
                    k.putExtra("MatchId",match_id);
                    k.putExtra("Time",time+"");
                    k.putExtra("TeamsName", holder.tv_TeamsName.getText().toString());
                    k.putExtra("TeamsOneName", team_short_name1);
                    k.putExtra("TeamsTwoName", team_short_name2);
                    k.putExtra("T1Image", team_image1);
                    k.putExtra("T2Image", team_image2);
                    startActivity(k);
                }
            });


        }

    }



}
