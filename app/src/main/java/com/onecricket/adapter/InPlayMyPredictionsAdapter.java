package com.onecricket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onecricket.APICallingPackage.retrofit.betlist.InProgress;
import com.onecricket.APICallingPackage.retrofit.betlist.Upcoming;
import com.onecricket.R;

import java.util.List;

public class InPlayMyPredictionsAdapter extends RecyclerView.Adapter<InPlayMyPredictionsAdapter.MyPredectionsViewHolder>{


    private Context context;
    private List<InProgress> upcomingList;

    public InPlayMyPredictionsAdapter(Context context, List<InProgress> upcomingList) {
        this.context = context;
        this.upcomingList = upcomingList;
    }

    @NonNull
    @Override
    public MyPredectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_my_predictions, parent, false);
        return new InPlayMyPredictionsAdapter.MyPredectionsViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPredectionsViewHolder holder, int position) {
        InProgress upcoming = upcomingList.get(position);
        holder.matchNameTextView.setText(String.format("Match Name: %s", upcoming.getMatchname()));
        holder.betValueTextView.setText(String.format("Bet Value: %s", upcoming.getBetValue()));
        holder.betAmountTextView.setText(String.format("Bet Amount: %s", upcoming.getBetAmount()));
        holder.oddNameTextView.setText(String.format("Odd Name: %s", upcoming.getOddname()));
        holder.oddValueTextView.setText(String.format("Odd Value: %s", upcoming.getOddvalue()));
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
