package com.game.onecricket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.onecricket.APICallingPackage.retrofit.betlist.Upcoming;
import com.game.onecricket.R;
import com.game.onecricket.utils.TeamName;

import java.util.List;

public class MyPredictionsAdapter extends RecyclerView.Adapter<MyPredictionsAdapter.MyPredectionsViewHolder>{


    private Context context;
    private List<Upcoming> upcomingList;

    public MyPredictionsAdapter(Context context, List<Upcoming> data) {
        this.context = context;
        upcomingList = data;
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
        String teams = "";
        String date = "";
        Upcoming upcoming = upcomingList.get(position);
        matchName = upcoming.getMatchname();
        betValue = TeamName.getFirstWord(upcoming.getBetValue());
        betAmount = upcoming.getBetAmount();
        oddName = upcoming.getOddname();
        oddValue = upcoming.getOddvalue();
        teams = TeamName.getFirstWord(upcoming.getHomeTeam()) + " Vs " + TeamName.getFirstWord(upcoming.getVisitorTeam());
        date = upcoming.getMatchDate() + " " + upcoming.getMatchTime();

       // holder.matchNameTextView.setText(String.format("Match Name: %s", matchName));
        holder.betValueTextView.setText(String.format(betValue));
        holder.betAmountTextView.setText(String.format(betAmount));
        holder.oddNameTextView.setText(String.format(oddName));
        holder.oddValueTextView.setText(String.format(oddValue));
       /* holder.delete.setOnClickListener(view -> {
            if (deleteItemListener != null) {
                deleteItemListener.onDeleteClicked(position);
            }
        });*/

       // holder.share.setOnClickListener(view -> onShareClicked());
        holder.matchDate.setText(String.format(date));
        holder.teamsTextView.setText(teams);
    }

    private void onShareClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "I made predictions on 1Cricket App. Join me if you are interested.");
        intent.setType("text/plain");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name)));
        }
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
        private TextView teamsTextView;
        private ImageView delete;
        private TextView matchDate;
        private ImageView share;

        public MyPredectionsViewHolder(@NonNull View itemView) {
            super(itemView);
           // this.matchNameTextView = itemView.findViewById(R.id.match_name);
            this.betValueTextView = itemView.findViewById(R.id.bet_value);
            this.betAmountTextView = itemView.findViewById(R.id.bet_amount);
            this.oddNameTextView = itemView.findViewById(R.id.oddname);
            this.oddValueTextView = itemView.findViewById(R.id.oddvalue);
            this.teamsTextView = itemView.findViewById(R.id.teams);
            this.matchDate = itemView.findViewById(R.id.matchdate);
          //  this.delete = itemView.findViewById(R.id.delete);
          //  this.share = itemView.findViewById(R.id.share);
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
