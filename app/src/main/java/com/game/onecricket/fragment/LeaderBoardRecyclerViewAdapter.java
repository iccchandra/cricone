package com.game.onecricket.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.game.onecricket.R;
import com.game.onecricket.pojo.Data;
import com.game.onecricket.ui.CircularTextView;

import java.util.List;

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
        holder.position.setText(String.valueOf(position + 1));
        holder.points.setText(userData.getRoi());

        holder.location.setText(String.format("%s %s", userData.getName(), userData.getState()));
        if (userData.getName() != null && userData.getName().trim().length() > 0) {
            String leaderName = userData.getName();
            holder.leaderName.setText(String.format("%s", leaderName.toUpperCase().charAt(0)));
            holder.name.setText(leaderName);
        }

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
        public CircularTextView leaderName;

        public LeaderBoardViewHolder(View itemView) {
            super(itemView);
            this.position = itemView.findViewById(R.id.position);
            this.name = itemView.findViewById(R.id.name);
            this.location = itemView.findViewById(R.id.location);
            this.points = itemView.findViewById(R.id.points);
            this.leaderName = itemView.findViewById(R.id.circular_leader);
        }
    }
}
