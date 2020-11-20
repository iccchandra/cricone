package com.game.onecricket.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.onecricket.R;
import com.game.onecricket.pojo.Data;
import com.game.onecricket.ui.CircularTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderBoardRecyclerViewAdapter extends RecyclerView.Adapter<LeaderBoardRecyclerViewAdapter.LeaderBoardViewHolder> {


    private List<Data> dataList;

    /*public LeaderBoardRecyclerViewAdapter(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }*/

    public LeaderBoardRecyclerViewAdapter(List<Data> dataList) {
        this.dataList = dataList;
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
        Data userData = dataList.get(position);
        String roi = userData.getRoi();
        String location = userData.getState();



        if (roi != null) {
            float roiFloat = Float.parseFloat(roi);
            holder.points.setText(String.format("%.1f", roiFloat)+"  %");
        }

        String totalWinning = userData.getTotalWinning();
        if (totalWinning != null) {
            if(isNullOrEmpty(location)){
                holder.location.setText("Location unknown");
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
            }

            else{
                holder.crown1.setVisibility(View.GONE);
                holder.circularTextView.setVisibility(View.VISIBLE);
                holder.circularTextView.setText(String.format("%s", String.format("%d", position + 1)));
            }

            holder.name.setText("Anonymous");
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class LeaderBoardViewHolder extends RecyclerView.ViewHolder {
        public TextView position;
        public TextView name;
        public TextView location;
        public TextView points;
        public CircularTextView circularTextView;
        public RelativeLayout   rlleader;
        public ImageView        crown1;


        public LeaderBoardViewHolder(View itemView) {
            super(itemView);
            this.position = itemView.findViewById(R.id.position);
            this.name = itemView.findViewById(R.id.name);
            this.location = itemView.findViewById(R.id.location);
            this.points = itemView.findViewById(R.id.points);
            this.circularTextView = itemView.findViewById(R.id.circular_leader);
            this.rlleader = itemView.findViewById(R.id.rlleader);
            this.crown1=itemView.findViewById(R.id.crown1);
        }
    }
}
