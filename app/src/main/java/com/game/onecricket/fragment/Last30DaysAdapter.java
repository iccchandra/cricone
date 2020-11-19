package com.game.onecricket.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.onecricket.APICallingPackage.retrofit.globalleader.Last30Day;
import com.game.onecricket.R;
import com.game.onecricket.ui.CircularTextView;

import java.util.List;

public class Last30DaysAdapter extends RecyclerView.Adapter<Last30DaysAdapter.LeaderBoardViewHolder> {


    private List<Last30Day> last30DayList;

    public Last30DaysAdapter(List<Last30Day> last30DayList) {
        this.last30DayList = last30DayList;
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
        Last30Day userData = last30DayList.get(position);
      //  holder.position.setText(String.format("%d", position + 1));
        String roi = userData.getRoi();
        String location = userData.getstate();
        if (roi != null) {
            float roiFloat = Float.parseFloat(roi);
            holder.points.setText(String.format("%.2f", roiFloat));
        }

        String totalWinning = userData.getTotalWinning();
        if (totalWinning != null) {
            float totalWinningFloat = Float.parseFloat(totalWinning);
            if(isNullOrEmpty(location)){
                holder.location.setText("Location");
            }
            else {
                holder.location.setText(location);
            }
        }

        if (userData.getName() != null && userData.getName().trim().length() > 0) {
            String leaderName = userData.getName();


            int imgint= holder.getAdapterPosition();
            System.out.println("imgint:"+ position);

            if(imgint==0||imgint==1||imgint==2){
                holder.crown1.setVisibility(View.VISIBLE);
                holder.circularTextView.setVisibility(View.GONE);
                // imgint= imgint+1;

            }

            else{
                holder.crown1.setVisibility(View.GONE);
                holder.circularTextView.setVisibility(View.VISIBLE);
                holder.circularTextView.setText(String.format("%s", String.format("%d", position + 1)));

            }



            holder.name.setText(leaderName);
        }
        else {

            int imgint= holder.getAdapterPosition();
            System.out.println("imgint:"+ position);

            if(imgint==0||imgint==1||imgint==2){
                holder.crown1.setVisibility(View.VISIBLE);
                holder.circularTextView.setVisibility(View.GONE);
                // imgint= imgint+1;

            }

            else{
                holder.crown1.setVisibility(View.GONE);
                holder.circularTextView.setVisibility(View.VISIBLE);
                holder.circularTextView.setText(String.format("%s", String.format("%d", position + 1)));

            }

            holder.name.setText("Anonymous");
        }

    }

    @Override
    public int getItemCount() {
        return last30DayList.size();
    }

    public static class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        public TextView position;
        public TextView name;
        public TextView location;
        public TextView points;
        public CircularTextView circularTextView;
        public ImageView crown1;

        public LeaderBoardViewHolder(View itemView) {
            super(itemView);
            this.position = itemView.findViewById(R.id.position);
            this.name = itemView.findViewById(R.id.name);
            this.location = itemView.findViewById(R.id.location);
            this.points = itemView.findViewById(R.id.points);
            this.circularTextView = itemView.findViewById(R.id.circular_leader);
            this.crown1=itemView.findViewById(R.id.crown1);
        }
    }
    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }
}
