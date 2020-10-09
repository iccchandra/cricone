package com.onecricket.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onecricket.APICallingPackage.retrofit.globalleader.Last30Day;
import com.onecricket.APICallingPackage.retrofit.globalleader.Last7Day;
import com.onecricket.APICallingPackage.retrofit.globalleader.Today;
import com.onecricket.R;

import java.util.List;

public class GlobalLeaderFragment2 extends Fragment {


    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_global_ranking, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    public void setLastWeekList(List<Last7Day> last7DayList) {
        Last7DaysAdapter adapter = new Last7DaysAdapter(last7DayList);
        recyclerView.setAdapter(adapter);
    }

}
