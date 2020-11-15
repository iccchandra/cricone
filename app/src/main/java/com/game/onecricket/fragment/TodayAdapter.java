package com.game.onecricket.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.onecricket.APICallingPackage.retrofit.globalleader.Today;
import com.game.onecricket.R;
import com.game.onecricket.ui.CircularTextView;

import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.LeaderBoardViewHolder> {


    private List<Today> last7DayList;

    public TodayAdapter(List<Today> last7DayList) {
        this.last7DayList = last7DayList;
    }

    @NonNull
    @Override
    public LeaderBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem       = layoutInflater.inflate(R.layout.row_leader_board, parent, false);
        return new LeaderBoardViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderBoardViewHolder holder, int position) {
        Today userData = last7DayList.get(position);
        holder.position.setText(String.format("%d", position + 1));
        String roi = userData.getRoi();
        if (roi != null) {
            float roiFloat = Float.parseFloat(roi);
            holder.points.setText(String.format("%.2f", roiFloat));
        }

        String totalWinning = userData.getTotalWinning();
        if (totalWinning != null) {
            float totalWinningFloat = Float.parseFloat(totalWinning);
            holder.location.setText(String.format("%.2f", totalWinningFloat));
        }

        if (userData.getName() != null && userData.getName().trim().length() > 0) {
            String leaderName = userData.getName();
            holder.circularTextView.setText(String.format("%s", leaderName.toUpperCase().charAt(0)));
            holder.name.setText(leaderName);
        }
        else {
            holder.circularTextView.setText("L");
            holder.name.setText("Leader");
        }
    }

    @Override
    public int getItemCount() {
        return last7DayList.size();
    }

    public static class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        public TextView position;
        public TextView name;
        public TextView location;
        public TextView points;
        public CircularTextView circularTextView;

        public LeaderBoardViewHolder(View itemView) {
            super(itemView);
            this.position = itemView.findViewById(R.id.position);
            this.name = itemView.findViewById(R.id.name);
            this.location = itemView.findViewById(R.id.location);
            this.points = itemView.findViewById(R.id.points);
            this.circularTextView = itemView.findViewById(R.id.circular_leader);
        }
    }
}
