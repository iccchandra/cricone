package com.onecricket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onecricket.APICallingPackage.retrofit.betlist.Finished;
import com.onecricket.R;

import java.util.List;

public class MyPredictionsFinishedAdapter extends RecyclerView.Adapter<MyPredictionsFinishedAdapter.MyPredectionsViewHolder>{


    private Context context;
    private List<Finished> finishedList;

    public MyPredictionsFinishedAdapter(Context context, List<Finished> data) {
        this.context = context;
        this.finishedList = data;
    }

    @NonNull
    @Override
    public MyPredectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_my_predictions, parent, false);
        return new MyPredictionsFinishedAdapter.MyPredectionsViewHolder(listItem);
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
         if (finishedList != null) {
            Finished finished = finishedList.get(position);
            matchName = finished.getMatchname();
            betValue = getFirstWord(finished.getBetValue());
            betAmount = finished.getBetAmount();
            oddName = finished.getOddname();
            oddValue = finished.getOddvalue();
            teams = getFirstWord(finished.getHomeTeam()) + " Vs " + getFirstWord(finished.getVisitorTeam());
            date = finished.getMatchDate() + " " + finished.getMatchTime();
            if (finished.getStatus().equals("lost")) {
                holder.betStatus.setImageResource(R.drawable.lost);
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setText("Lost");

            }
            else {
                holder.betStatus.setImageResource(R.drawable.winner);
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setText("Winner");
            }
        }

      //  holder.matchNameTextView.setText(String.format("Match Name: %s", matchName));
        holder.betValueTextView.setText(String.format(betValue));
        holder.betAmountTextView.setText(String.format(betAmount));
        holder.oddNameTextView.setText(String.format(oddName));
        holder.oddValueTextView.setText(String.format(oddValue));
        holder.matchDate.setText(String.format(date));
        holder.teamsTextView.setText(teams);

//        holder.share.setOnClickListener(view -> onShareClicked());
    }

    private String getFirstWord(String title) {
        if (title.toLowerCase().contains("bangalore")) {
            return "Bangalore";
        }
        else if (title.toLowerCase().contains("chennai")) {
            return "Chennai";
        }
        else if (title.toLowerCase().contains("sunrisers")) {
            return "Hyderabad";
        }
        else if (title.contains(" ")) {
            String[] titleArray = title.split(" ");
            return titleArray[0];
        }
        return title;
    }

    @Override
    public int getItemCount() {
        return finishedList.size();
    }


    public static class MyPredectionsViewHolder extends RecyclerView.ViewHolder {
        private TextView matchNameTextView;
        private TextView betValueTextView;
        private TextView betAmountTextView;
        private TextView oddNameTextView;
        private TextView oddValueTextView;
        private TextView teamsTextView;
        private TextView result;
        private ImageView delete;
        private TextView matchDate;
        private ImageView betStatus;
        private ImageView share;

        public MyPredectionsViewHolder(@NonNull View itemView) {
            super(itemView);
          //  this.matchNameTextView = itemView.findViewById(R.id.match_name);
            this.betValueTextView = itemView.findViewById(R.id.bet_value);
            this.betAmountTextView = itemView.findViewById(R.id.bet_amount);
            this.oddNameTextView = itemView.findViewById(R.id.oddname);
            this.oddValueTextView = itemView.findViewById(R.id.oddvalue);
            this.teamsTextView = itemView.findViewById(R.id.teams);
            this.matchDate = itemView.findViewById(R.id.matchdate);
            this.result = itemView.findViewById(R.id.result);
           // this.delete = itemView.findViewById(R.id.delete);
            this.betStatus = itemView.findViewById(R.id.bet_status);
            this.share = itemView.findViewById(R.id.share);
        }
    }

    private void onShareClicked() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "I made predictions on 1Cricket App. Join me if you are interested.");
        intent.setType("text/plain");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name)));
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
