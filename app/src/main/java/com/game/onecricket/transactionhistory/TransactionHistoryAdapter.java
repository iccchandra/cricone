package com.game.onecricket.transactionhistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.game.onecricket.R;

import java.util.List;

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder> {

    private List<Movie> list;

    public TransactionHistoryAdapter(List<Movie> list) {
        this.list = list;
    }

    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new TransactionHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionHistoryViewHolder holder, int position) {
        Movie movie = list.get(position);

        holder.bind(movie);

        holder.itemView.setOnClickListener(v -> {
            boolean expanded = movie.isExpanded();
            movie.setExpanded(!expanded);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView genre;
        private TextView year;
        private View subItem;

        public TransactionHistoryViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_title);
            genre = itemView.findViewById(R.id.sub_item_genre);
            year = itemView.findViewById(R.id.sub_item_year);
            subItem = itemView.findViewById(R.id.sub_item);
        }

        private void bind(Movie movie) {
            boolean expanded = movie.isExpanded();

            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            title.setText(movie.getTitle());
            genre.setText("Genre: " + movie.getGenre());
            year.setText("Year: " + movie.getYear());
        }
    }
}