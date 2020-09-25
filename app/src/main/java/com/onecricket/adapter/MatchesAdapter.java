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
        View listItem = layoutInflater.inflate(R.layout.row_matches_2, parent, false);
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
        if (teamA.length() > 3) {
            holder.teamAShortName.setText(teamA.substring(0, 3));
        }
        else {
            holder.teamAShortName.setText(teamA);
        }
        if (teamB.length() > 3) {
            holder.teamBShortName.setText(teamB.substring(0, 3));
        }
        else {
            holder.teamBShortName.setText(teamB);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClickListener(position);
                }
            }
        });


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

        public MatchesViewHolder(View itemView) {
            super(itemView);
            this.teamsTextView = itemView.findViewById(R.id.teams_name);
            this.teamATextView = itemView.findViewById(R.id.team_a);
            this.teamBTextView = itemView.findViewById(R.id.team_b);
            this.teamAShortName = itemView.findViewById(R.id.team_a_short_name);
            this.teamBShortName = itemView.findViewById(R.id.team_b_short_name);
            this.matchStatus = itemView.findViewById(R.id.play_status);

        }
    }

    public interface ClickListener {
        void onItemClickListener(int position);
    }

}
