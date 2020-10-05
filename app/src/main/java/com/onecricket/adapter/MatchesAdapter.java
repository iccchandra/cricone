package com.onecricket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.onecricket.R;
import com.onecricket.pojo.MatchesInfo;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>{


    private List<MatchesInfo> matchesInfoList;
    private String matchType;
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
        holder.matchStatus.setText(matchType);

        if (teamA.length() > 0) {
            holder.circularTextTeamA.setText(String.format("%s", teamA.charAt(0)));
        }

        if (teamB.length() > 0) {
            holder.circularTextTeamB.setText(String.format("%s", teamB.charAt(0)));
        }
        holder.teamAShortName.setText(capitailizeWord(teamA));
        holder.teamBShortName.setText(capitailizeWord(teamB));

        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClickListener(position);
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
        private TextView linesout;
        private TextView circularTextTeamA;
        private TextView circularTextTeamB;

        public MatchesViewHolder(View itemView) {
            super(itemView);
            this.teamsTextView = itemView.findViewById(R.id.teams_name);
            this.teamATextView = itemView.findViewById(R.id.team_a);
            this.teamBTextView = itemView.findViewById(R.id.team_b);
            this.teamAShortName = itemView.findViewById(R.id.team_a_short_name);
            this.teamBShortName = itemView.findViewById(R.id.team_b_short_name);
            this.matchStatus    = itemView.findViewById(R.id.play_status);
            this.linesout    = itemView.findViewById(R.id.linesout);
            this.circularTextTeamA = itemView.findViewById(R.id.circular_team_a);
            this.circularTextTeamB = itemView.findViewById(R.id.circular_team_b);

        }
    }

    public interface ClickListener {
        void onItemClickListener(int position);
    }

}
