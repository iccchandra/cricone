package com.onecricket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onecricket.APICallingPackage.retrofit.betlist.Data;
import com.onecricket.APICallingPackage.retrofit.betlist.Finished;
import com.onecricket.APICallingPackage.retrofit.betlist.InProgress;
import com.onecricket.APICallingPackage.retrofit.betlist.Upcoming;
import com.onecricket.R;

import java.util.List;

public class MyInPlayPredictionsAdapter extends RecyclerView.Adapter<MyInPlayPredictionsAdapter.MyPredectionsViewHolder>{


    private Context context;
    private List<InProgress> inProgressList;

    public MyInPlayPredictionsAdapter(Context context, List<InProgress> data) {
        this.context = context;
        this.inProgressList = data;
    }

    @NonNull
    @Override
    public MyPredectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_my_predictions, parent, false);
        return new MyInPlayPredictionsAdapter.MyPredectionsViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPredectionsViewHolder holder, int position) {
        String matchName = "";
        String betValue = "";
        String betAmount = "";
        String oddName = "";
        String oddValue = "";
        String teams = "";
        String date = "";
         if (inProgressList != null) {
            InProgress inProgress = inProgressList.get(position);
            matchName = inProgress.getMatchname();
            betValue = inProgress.getBetValue();
            betAmount = inProgress.getBetAmount();
            oddName = inProgress.getOddname();
            oddValue = inProgress.getOddvalue();
            teams = inProgress.getHomeTeam() + " Vs " + inProgress.getVisitorTeam();
            date = inProgress.getMatchDate() + " " + inProgress.getMatchTime();
        }

        holder.matchNameTextView.setText(String.format("Match Name: %s", matchName));
        holder.betValueTextView.setText(String.format("Bet Value: %s", betValue));
        holder.betAmountTextView.setText(String.format("Bet Amount: %s", betAmount));
        holder.oddNameTextView.setText(String.format("Odd Name: %s", oddName));
        holder.oddValueTextView.setText(String.format("Odd Value: %s", oddValue));
        holder.matchDate.setText(String.format("Date: %s", date));
        holder.teamsTextView.setText(teams);
    }

    @Override
    public int getItemCount() {
        int size = inProgressList.size();
        return size;
    }


    public static class MyPredectionsViewHolder extends RecyclerView.ViewHolder {
        private TextView matchNameTextView;
        private TextView betValueTextView;
        private TextView betAmountTextView;
        private TextView oddNameTextView;
        private TextView oddValueTextView;
        private TextView teamsTextView;
        private ImageView delete;
        private TextView matchDate;

        public MyPredectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.matchNameTextView = itemView.findViewById(R.id.match_name);
            this.betValueTextView = itemView.findViewById(R.id.bet_value);
            this.betAmountTextView = itemView.findViewById(R.id.bet_amount);
            this.oddNameTextView = itemView.findViewById(R.id.oddname);
            this.oddValueTextView = itemView.findViewById(R.id.oddvalue);
            this.teamsTextView = itemView.findViewById(R.id.teams);
            this.matchDate = itemView.findViewById(R.id.matchdate);
            this.delete = itemView.findViewById(R.id.delete);
        }
    }

    private DeleteItemListener deleteItemListener;

    public void setDeleteItemListener(DeleteItemListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }


    public interface DeleteItemListener {
        void onDeleteClicked(int position);
    }
}
