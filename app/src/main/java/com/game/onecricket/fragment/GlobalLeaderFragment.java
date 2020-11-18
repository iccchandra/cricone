package com.game.onecricket.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.game.onecricket.APICallingPackage.retrofit.globalleader.Data;
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Today;
import com.game.onecricket.R;
import com.game.onecricket.ui.CircularTextView;

import java.util.ArrayList;
import java.util.List;

public class GlobalLeaderFragment extends Fragment {


    private RecyclerView recyclerView;
    private TextView noDataView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_global_ranking, container, false);
        findViewsById(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        if (getArguments() != null && getArguments().getSerializable("Data") != null) {
            Data data = (Data) getArguments().getSerializable("Data");
            setData(data);
        }
        return view;
    }

    private void setData(Data data) {
        if (data != null && data.getTodays() != null && data.getTodays().size() > 0) {
            setGlobalLeaderData(data.getTodays());
        }
        else {
            noDataAvailable();
        }
    }

    private void findViewsById(View view) {
        noDataView           = view.findViewById(R.id.no_data_view);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    public void setGlobalLeaderData(List<Today> todayList) {
        noDataView.setVisibility(View.GONE);
        TodayAdapter adapter = new TodayAdapter(todayList);
        recyclerView.setAdapter(adapter);
    }

    public void noDataAvailable() {
        noDataView.setVisibility(View.VISIBLE);
    }
}
