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
import com.game.onecricket.APICallingPackage.retrofit.globalleader.Last7Day;
import com.game.onecricket.R;
import com.game.onecricket.ui.CircularTextView;
import com.game.onecricket.utils.crypto.AlertDialogHelper;

import java.util.ArrayList;
import java.util.List;

public class GlobalLeaderFragment2 extends Fragment {


    private RecyclerView recyclerView;
    private RelativeLayout headerLayout;
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
        if (data != null && data.getLast7Days() != null && data.getLast7Days().size() > 0) {
            setLastWeekList(data.getLast7Days());
        }
        else {
            noDataAvailable();
        }
    }

    private void findViewsById(View view) {
        //headerLayout = view.findViewById(R.id.header_layout);
        noDataView = view.findViewById(R.id.no_data_view);
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    public void setLastWeekList(List<Last7Day> last7DayList) {
     //   headerLayout.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.GONE);
        Last7DaysAdapter adapter = new Last7DaysAdapter(last7DayList);
        recyclerView.setAdapter(adapter);
    }

    public void noDataAvailable() {
      //  headerLayout.setVisibility(View.GONE);
        noDataView.setVisibility(View.VISIBLE);
    }
}
