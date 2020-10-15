package com.onecricket.adapter;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.onecricket.R;
import com.onecricket.fragment.UpcomingMatchesFragment;
import com.onecricket.pojo.MatchesInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>{


    private List<MatchesInfo> matchesInfoList;
    public String matchType;
    private ClickListener clickListener;

    public MatchesAdapter(List<MatchesInfo> matchesInfoList, String matchType) {
        this.matchesInfoList = matchesInfoList;
        this.matchType = matchType;
    }

    public void setRecyclerViewItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_matches_3, parent, false);
        return new MatchesViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, final int position) {
        MatchesInfo matchesInfo = matchesInfoList.get(position);
        String teamA = matchesInfo.getHomeTeam();
        String teamB = matchesInfo.getVisitorsTeam();
        String league = matchesInfo.getLeagueName();
        holder.teamsTextView.setText(league);
        holder.teamATextView.setText(teamA);
        holder.teamBTextView.setText(teamB);
//        holder.matchStatus.setText(matchType);
        if (matchType.equalsIgnoreCase("upcoming")) {

            if( UpcomingMatchesFragment.contest==true) {

                holder.matchTimeTextView.setText(String.format(matchesInfo.getDateTime()));
                holder.createGroup.setText("Joined");}

            else {


                String timeleft = String.format(matchesInfo.getDateTime());
                System.out.println(timeleft);
                int hoursToGo = Integer.parseInt(timeleft.substring(0, timeleft.indexOf("h")));
                int minutesToGo = Integer.parseInt(timeleft.substring(timeleft.indexOf("h") + 1, timeleft.indexOf("m")));
                int secondsToGo = Integer.parseInt(timeleft.substring(timeleft.indexOf("m") + 1, timeleft.indexOf("s")));
                ;

                int millisToGo = secondsToGo * 1000 + minutesToGo * 1000 * 60 + hoursToGo * 1000 * 60 * 60;

                new CountDownTimer(millisToGo, 1000) {

                    @Override
                    public void onTick(long millis) {
                        int seconds = (int) (millis / 1000) % 60;
                        int minutes = (int) ((millis / (1000 * 60)) % 60);
                        int hours = (int) ((millis / (1000 * 60 * 60)));
                        // int days   = (int) ((millis / (1000*60*60*60)) % 24);
                        String text = String.format("%s %s %s", hours + "h", minutes + "m", seconds + "s");
                        holder.matchTimeTextView.setText(text);
                    }

                    @Override
                    public void onFinish() {
                        holder.matchTimeTextView.setText("Playing");

                    }
                }.start();

            }
           /* }
            else{

                String timeleft=String.format(matchesInfo.getDateTime());
                int hoursToGo = Integer.parseInt(timeleft.substring(0,timeleft.indexOf("h")));
                int minutesToGo = Integer.parseInt(timeleft.substring(timeleft.indexOf("h")+1,timeleft.indexOf("m")));
                int secondsToGo = Integer.parseInt(timeleft.substring(timeleft.indexOf("m")+1,timeleft.indexOf("s")));;

                int millisToGo = secondsToGo*1000+minutesToGo*1000*60+hoursToGo*1000*60*60;

                new CountDownTimer(millisToGo,1000) {

                    @Override
                    public void onTick(long millis) {
                        int seconds = (int) (millis / 1000) % 60 ;
                        int minutes = (int) ((millis / (1000*60)) % 60);
                        int hours   = (int) ((millis / (1000*60*60)));
                        // int days   = (int) ((millis / (1000*60*60*60)) % 24);
                        String text = String.format("%s %s %s",hours+"h",minutes+"m",seconds+"s");
                        holder.matchTimeTextView.setText(text);
                    }

                    @Override
                    public void onFinish() {
                        holder.matchTimeTextView.setText("Playing");
                    }
                }.start();

            }*/}

        else if (matchType.equalsIgnoreCase("private")) {


                // holder.matchTimeTextView.setText(String.format(matchesInfo.getDateTime()));
                holder.createGroup.setText("Joined");
            holder.matchTimeTextView.setText(String.format(matchesInfo.getDateTime()));
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date Datetime = null;
            try {
                Datetime = timeSdf.parse(matchesInfo.getDateTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date currentTime = Calendar.getInstance().getTime();
            long diff=Datetime.getTime()-currentTime.getTime();



            new CountDownTimer(diff, 1000) {

                @Override
                public void onTick(long millis) {
                    int seconds = (int) (millis / 1000) % 60;
                    int minutes = (int) ((millis / (1000 * 60)) % 60);
                    int hours = (int) ((millis / (1000 * 60 * 60)));
                    // int days   = (int) ((millis / (1000*60*60*60)) % 24);
                    String text = String.format("%s %s %s", hours + "h", minutes + "m", seconds + "s");
                    holder.matchTimeTextView.setText(text);
                    holder.code.setVisibility(View.VISIBLE);
                    holder.code.setText(matchesInfo.getcode());
                }

                @Override
                public void onFinish() {

                    matchesInfoList.get(position).setplaying(true);
                    holder.matchTimeTextView.setText("Playing");


                }
            }.start();

        }
        else {
            holder.matchTimeTextView.setText("inplay");

        }

        if (teamA.length() > 0) {
            holder.circularTextTeamA.setText(String.format("%s", teamA.charAt(0)));
        }

        if (teamB.length() > 0) {
          //  holder.circularTextTeamB.setText(String.format("%s", teamB.charAt(0)));
        }
       // holder.teamAShortName.setText(capitailizeWord(teamA));
        //holder.teamBShortName.setText(capitailizeWord(teamB));

        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClickListener(position);
            }
        });

        holder.createGroup.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onCreateGroupClicked(position);
            }
        });

        holder.code.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onCodeClicked(position);
            }
        });


    }

    private  String capitailizeWord(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        final String EMPTY_SPACE = " ";
        if (str.contains(EMPTY_SPACE)) {
            String[] myName = str.split(EMPTY_SPACE);
            for (String input : myName) {
                stringBuilder.append(input.charAt(0));
            }
        }
        else {
            if (str.length() > 3) {
                return str.substring(0,3);
            }
            else {
                return str;
            }

        }
        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return matchesInfoList.size();
    }

    public static class MatchesViewHolder extends RecyclerView.ViewHolder {
        private TextView teamsTextView;
        private TextView teamATextView;
        private TextView teamBTextView;
        private TextView teamAShortName;
        private TextView teamBShortName;
        private TextView matchStatus;
        private TextView circularTextTeamA;
        private TextView circularTextTeamB;
        private TextView matchTimeTextView;
        private TextView createGroup;
        private TextView code;

        public MatchesViewHolder(View itemView) {
            super(itemView);
            this.teamsTextView = itemView.findViewById(R.id.teams_name);
            this.teamATextView = itemView.findViewById(R.id.team_a);
            this.teamBTextView = itemView.findViewById(R.id.team_b);
            this.teamAShortName = itemView.findViewById(R.id.team_a_short_name);
            this.teamBShortName = itemView.findViewById(R.id.team_b_short_name);
            this.matchStatus    = itemView.findViewById(R.id.play_status);
            this.circularTextTeamA = itemView.findViewById(R.id.circular_team_a);
           // this.circularTextTeamB = itemView.findViewById(R.id.circular_team_b);
            this.matchTimeTextView = itemView.findViewById(R.id.match_time);
            this.createGroup       = itemView.findViewById(R.id.create_group);
            this.code       = itemView.findViewById(R.id.code);

        }
    }

    public interface ClickListener {
        void onItemClickListener(int position);
        void onCreateGroupClicked(int position);
        void onCodeClicked(int position);
    }

    // countdowntimer is an abstract class, so extend it and fill in methods

}
