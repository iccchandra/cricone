package com.game.onecricket.adapter.expandablerecyclerview;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.game.onecricket.R;
import com.game.onecricket.pojo.MatchOdds;


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
        // I personally do not like adding this here. But, the requirements are making me do this.
        mMoviesTextView.setText(matchOdds.getName());
        oddsTextView.setText(matchOdds.getOdds());
    }

    public static String getFirstWord(String title) {
        if (title.toLowerCase().contains("bangalore")) {
            return "Bangalore";
        }
        else if (title.toLowerCase().contains("chennai")) {
            return "Chennai";
        }
        else if (title.toLowerCase().contains("sunrisers")) {
            return "Hyderabad";
        }
        else if (title.toLowerCase().contains("punjab")) {
            return "Punjab";
        }
        else if (title.toLowerCase().contains("rajasthan")) {
            return "Rajasthan";
        }
        else if (title.toLowerCase().contains("kolkata")) {
            return "Kolkata";
        }
        else if (title.toLowerCase().contains("dehhi")) {
            return "Delhi";
        }
        else if (title.toLowerCase().contains("mumbai")) {
            return "Mumbai";
        }
        return title;
    }

    public void disableSelection() {
        layout.setOnClickListener(null);
    }

    public void enableSelection() {
        layout.setOnClickListener(this);
        layout.setSelected(true);
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