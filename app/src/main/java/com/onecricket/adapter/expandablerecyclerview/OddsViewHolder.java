package com.onecricket.adapter.expandablerecyclerview;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onecricket.R;
import com.onecricket.pojo.MatchOdds;


public class OddsViewHolder extends ChildViewHolder implements View.OnClickListener{

    private TextView mMoviesTextView;
    private TextView oddsTextView;
    private RelativeLayout layout;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public OddsViewHolder(final View itemView) {
        super(itemView);
        mMoviesTextView = itemView.findViewById(R.id.tv_movies);
        oddsTextView = itemView.findViewById(R.id.tv_movies_odd);
        layout = itemView.findViewById(R.id.child_layout);
        //enableSelection();
    }

    public void bind(MatchOdds matchOdds) {
        mMoviesTextView.setText(matchOdds.getName());
        oddsTextView.setText(matchOdds.getOdds());
    }

    public void disableSelection() {
        layout.setOnClickListener(null);
    }

    public void enableSelection() {
        layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.child_layout) {
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                layout.setSelected(false);
            } else {
                selectedItems.put(getAdapterPosition(), true);
                layout.setSelected(true);
            }
        }
    }
}