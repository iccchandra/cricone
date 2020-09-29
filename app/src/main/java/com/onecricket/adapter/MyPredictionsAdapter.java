package com.onecricket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onecricket.APICallingPackage.retrofit.betlist.Data;
import com.onecricket.APICallingPackage.retrofit.betlist.Finished;
import com.onecricket.APICallingPackage.retrofit.betlist.InProgress;
import com.onecricket.APICallingPackage.retrofit.betlist.Upcoming;
import com.onecricket.R;

import java.util.List;

public class MyPredictionsAdapter extends RecyclerView.Adapter<MyPredictionsAdapter.MyPredectionsViewHolder>{


    private Context context;
    private Data data;
    private Predictions predictions;
    private List<Upcoming> upcomingList;
    private List<InProgress> inProgressList;
    private List<Finished> finishedList;

    public MyPredictionsAdapter(Context context, Data data, Predictions predictions) {
        this.context = context;
        this.data = data;
        this.predictions = predictions;
        switch (predictions) {
            case FINISHED:
                finishedList = data.getFinished();
                break;
            case INPROGRESS:
                inProgressList = data.getInprogress();
                break;
            case UPCOMING:
                upcomingList = data.getUpcoming();
                break;
        }
    }

    @NonNull
    @Override
    public MyPredectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_my_predictions, parent, false);
        return new MyPredictionsAdapter.MyPredectionsViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPredectionsViewHolder holder, int position) {
        String matchName = "";
        String betValue = "";
        String betAmount = "";
        String oddName = "";
        String oddValue = "";
        // TODO This logic needs to be changed. This code will degrade the performance
        if (upcomingList != null) {
            Upcoming upcoming = upcomingList.get(position);
            matchName = upcoming.getMatchname();
            betValue = upcoming.getBetValue();
            betAmount = upcoming.getBetAmount();
            oddName = upcoming.getOddname();
            oddValue = upcoming.getOddvalue();
        }
        else if (inProgressList != null) {
            InProgress upcoming = inProgressList.get(position);
            matchName = upcoming.getMatchname();
            betValue = upcoming.getBetValue();
            betAmount = upcoming.getBetAmount();
            oddName = upcoming.getOddname();
            oddValue = upcoming.getOddvalue();
        }
        else if (finishedList != null) {
            Finished upcoming = finishedList.get(position);
            matchName = upcoming.getMatchname();
            betValue = upcoming.getBetValue();
            betAmount = upcoming.getBetAmount();
            oddName = upcoming.getOddname();
            oddValue = upcoming.getOddvalue();
        }

        holder.matchNameTextView.setText(String.format("Match Name: %s", matchName));
        holder.betValueTextView.setText(String.format("Bet Value: %s", betValue));
        holder.betAmountTextView.setText(String.format("Bet Amount: %s", betAmount));
        holder.oddNameTextView.setText(String.format("Odd Name: %s", oddName));
        holder.oddValueTextView.setText(String.format("Odd Value: %s", oddValue));
    }

    @Override
    public int getItemCount() {
        return upcomingList.size();
    }

    public static class MyPredectionsViewHolder extends RecyclerView.ViewHolder {
        private TextView matchNameTextView;
        private TextView betValueTextView;
        private TextView betAmountTextView;
        private TextView oddNameTextView;
        private TextView oddValueTextView;

        public MyPredectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.matchNameTextView = itemView.findViewById(R.id.match_name);
            this.betValueTextView = itemView.findViewById(R.id.bet_value);
            this.betAmountTextView = itemView.findViewById(R.id.bet_amount);
            this.oddNameTextView = itemView.findViewById(R.id.oddname);
            this.oddValueTextView = itemView.findViewById(R.id.oddvalue);
        }
    }
}
