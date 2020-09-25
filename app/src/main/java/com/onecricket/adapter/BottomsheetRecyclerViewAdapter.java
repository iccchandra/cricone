package com.onecricket.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.onecricket.R;
import com.onecricket.pojo.MatchOdds;

import java.util.List;

public class BottomsheetRecyclerViewAdapter extends RecyclerView.Adapter<BottomsheetRecyclerViewAdapter.BottomsheetViewHolder> {


    private Context context;
    private List<MatchOdds> matchOddsList;
    private ItemChangeListener itemChangeListener;
    private float              totalBetAmount;
    private float              totalReturnAmount;

    public BottomsheetRecyclerViewAdapter(Context context, List<MatchOdds> matchOddsList) {
        this.context = context;
        this.matchOddsList = matchOddsList;
    }

    public void updateList(MatchOdds matchOdds) {
        if (matchOdds.isSelected()) {
            matchOddsList.add(matchOdds);
        }
        else {
            matchOddsList.remove(matchOdds);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BottomsheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.row_bet_slip, parent, false);
        return new BottomsheetViewHolder(listItem, new RecyclerViewEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull final BottomsheetViewHolder holder, final int position) {
        final MatchOdds matchOdds = matchOddsList.get(position);
        holder.name.setText(matchOdds.getName());
        holder.points.setText(matchOdds.getOdds());
        /*if (matchOdds.getBetAmount() > 0) {
            holder.stakeInput.setText(String.valueOf(matchOdds.getBetAmount()));
        }*/
        if (matchOdds.getReturnAmount() > 0) {
            holder.totalBetAmountText.setText(String.format("To Return Coins. %s", matchOdds.getReturnAmount()));
        }
        else {
            holder.totalBetAmountText.setText("");
        }
        holder.close.setOnClickListener(view -> {
            if (itemChangeListener != null) {
                matchOdds.setBetAmount(0);
                matchOdds.setReturnAmount(0);
                if (itemChangeListener != null) {
                    itemChangeListener.onBetAmountChanged(matchOdds);
                    itemChangeListener.onItemRemoved(matchOdds);
                }
            }
        });

        holder.recyclerViewEditTextListener.updatePosition(holder.getAdapterPosition(), holder);
        int stake = matchOddsList.get(holder.getAdapterPosition()).getBetAmount();
        if (stake > 0) {
            holder.stakeInput.setText(String.valueOf(stake));
        }
        else {
            holder.stakeInput.setText("");
        }



/*        holder.stakeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isOnTextChanged = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isOnTextChanged) {
                    isOnTextChanged = false;
                    if (editable.toString().length() > 0) {
                        float betAmount    = Float.parseFloat(editable.toString());
                        float returnAmount = betAmount * Float.parseFloat(matchOdds.getOdds());
                        holder.totalBetAmountText.setText(String.format("To Return Rs. %s", returnAmount));
                        matchOdds.setBetAmount(betAmount);
                        matchOdds.setReturnAmount(returnAmount);
                    }
                    else {
                        matchOdds.setBetAmount(0);
                        matchOdds.setReturnAmount(0);
                        holder.totalBetAmountText.setText("");
                    }
                    if (itemChangeListener != null) {
                        itemChangeListener.onBetAmountChanged(matchOdds);
                    }
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return matchOddsList.size();
    }

    public void removeItem(MatchOdds matchOdds) {
        matchOdds.setReturnAmount(0.0f);
        matchOdds.setBetAmount(0);
        matchOddsList.remove(matchOdds);
        notifyDataSetChanged();
    }

    public void removeAll() {
        matchOddsList.clear();
        notifyDataSetChanged();
    }

    public static class BottomsheetViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView points;
        private ImageButton close;
        private EditText stakeInput;
        private TextView totalBetAmountText;
        private RecyclerViewEditTextListener recyclerViewEditTextListener;

        public BottomsheetViewHolder(View itemView, RecyclerViewEditTextListener recyclerViewEditTextListener) {
            super(itemView);
            this.name = itemView.findViewById(R.id.qr_code);
            this.points = itemView.findViewById(R.id.points);
            this.close    = itemView.findViewById(R.id.close);
            this.stakeInput = itemView.findViewById(R.id.stake);
            this.totalBetAmountText = itemView.findViewById(R.id.result);
            this.recyclerViewEditTextListener = recyclerViewEditTextListener;
            this.stakeInput.addTextChangedListener(recyclerViewEditTextListener);
        }
    }

    public void setItemChangeListener(ItemChangeListener itemChangeListener) {
        this.itemChangeListener = itemChangeListener;
    }

    public interface ItemChangeListener {
        void onItemRemoved(MatchOdds matchOdds);
        void onBetAmountChanged(MatchOdds matchOdds);
    }

    private class RecyclerViewEditTextListener implements TextWatcher {
        private int position;
        private BottomsheetViewHolder holder;
        private boolean isOnTextChanged;


        public void updatePosition(int position, BottomsheetViewHolder holder) {
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            isOnTextChanged = true;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (isOnTextChanged) {
                isOnTextChanged = false;
                if (editable.toString().length() > 0) {
                    int betAmount      = Integer.parseInt(editable.toString());
                    float returnAmount = betAmount * Float.parseFloat(matchOddsList.get(position).getOdds());
                    matchOddsList.get(position).setBetAmount(betAmount);
                    holder.totalBetAmountText.setText(String.format("To Return Coins. %s", returnAmount));
                    matchOddsList.get(position).setReturnAmount(returnAmount);
                }
                else {
                    matchOddsList.get(position).setBetAmount(0);
                    matchOddsList.get(position).setReturnAmount(0);
                    holder.totalBetAmountText.setText("");
                }

                if (itemChangeListener != null) {
                    itemChangeListener.onBetAmountChanged(matchOddsList.get(position));
                }
            }
        }
    }
}
