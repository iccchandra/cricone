package com.game.onecricket.adapter.expandablerecyclerview;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.game.onecricket.R;
import com.game.onecricket.pojo.MatchOdds;
import com.game.onecricket.pojo.OddsCategory;

import java.util.List;

public class OddsCategoryAdapter extends ExpandableRecyclerAdapter<OddsCategoryViewHolder, OddsViewHolder> {

    private LayoutInflater mInflator;
    private Context context;
    private ChildClickListener childClickListener;


    public OddsCategoryAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    public void setChildClickListener(ChildClickListener childClickListener) {
        this.childClickListener = childClickListener;
    }

    @Override
    public OddsCategoryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View movieCategoryView = mInflator.inflate(R.layout.movie_category_view, parentViewGroup, false);
        return new OddsCategoryViewHolder(movieCategoryView);
    }

    @Override
    public OddsViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View moviesView = mInflator.inflate(R.layout.movies_view, childViewGroup, false);
        return new OddsViewHolder(moviesView);
    }

    @Override
    public void onBindParentViewHolder(OddsCategoryViewHolder oddsCategoryViewHolder, int position, ParentListItem parentListItem) {
        System.out.println("Parent position:"+position);

        OddsCategory movieCategory = (OddsCategory) parentListItem;
        oddsCategoryViewHolder.bind(movieCategory);
    }

    private SparseBooleanArray selectedItems = new SparseBooleanArray();


    @Override
    public void onBindChildViewHolder(OddsViewHolder oddsViewHolder, final int position, Object childListItem) {
        System.out.println("position:"+position);

        final MatchOdds matchOdds = (MatchOdds) childListItem;
        oddsViewHolder.itemView.setOnClickListener(view -> {
            if (allowSelection) {
                if (childClickListener != null) {
                    childClickListener.onChildClicked(matchOdds);
                }

                /*if (selectedItems.get(position, false)) {
                    selectedItems.delete(position);
                    view.setSelected(false);
                } else {
                    selectedItems.put(position, true);
                    view.setSelected(true);
                }*/
            }
        });
        oddsViewHolder.itemView.setSelected(matchOdds.isSelected());
        oddsViewHolder.bind(matchOdds);
    }




    public interface ChildClickListener {
        void onChildClicked(MatchOdds matchOdds);
    }

    private boolean allowSelection = true;
    public void disableSelection() {
        allowSelection = false;
        notifyDataSetChanged();
    }

    public void enableSelection() {
        allowSelection = false;
        notifyDataSetChanged();
    }
}
