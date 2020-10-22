package com.onecricket.fragment;

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

import com.onecricket.APICallingPackage.retrofit.globalleader.Last7Day;
import com.onecricket.R;
import com.onecricket.ui.CircularTextView;
import com.onecricket.utils.crypto.AlertDialogHelper;

import java.util.ArrayList;
import java.util.List;

public class GlobalLeaderFragment2 extends Fragment {


    private RecyclerView recyclerView;
    private RelativeLayout headerLayout;
    private TextView noDataView;
    private TextView position2;
    private TextView position3;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView location1;
    private TextView location2;
    private TextView location3;
    private TextView points1;
    private TextView points2;
    private TextView points3;
    private RelativeLayout firstPositionLayout;
    private RelativeLayout secondPositionLayout;
    private RelativeLayout thirdPositionLayout;
    private CircularTextView circularTextView1;
    private CircularTextView circularTextView2;
    private CircularTextView circularTextView3;
    private AlertDialogHelper alertDialogHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_global_ranking, container, false);
        findViewsById(view);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        return view;
    }

    private void findViewsById(View view) {

        headerLayout        = view.findViewById(R.id.header_layout);
        noDataView          = view.findViewById(R.id.no_data_view);
        firstPositionLayout = view.findViewById(R.id.first_position_layout);
        secondPositionLayout = view.findViewById(R.id.second_position_layout);
        thirdPositionLayout = view.findViewById(R.id.third_position_layout);

        name1 = view.findViewById(R.id.name1);
        location1 = view.findViewById(R.id.location1);
        points1 = view.findViewById(R.id.points1);

        position2 = view.findViewById(R.id.position2);
        name2 = view.findViewById(R.id.name2);
        location2 = view.findViewById(R.id.location2);
        points2 = view.findViewById(R.id.points2);

        position3 = view.findViewById(R.id.position3);
        name3 = view.findViewById(R.id.name3);
        location3 = view.findViewById(R.id.location3);
        points3 = view.findViewById(R.id.points3);
        circularTextView1 = view.findViewById(R.id.circular_leader_one);
        circularTextView2 = view.findViewById(R.id.circular_leader_two);
        circularTextView3 = view.findViewById(R.id.circular_leader_three);

        firstPositionLayout.setVisibility(View.GONE);
        secondPositionLayout.setVisibility(View.GONE);
        thirdPositionLayout.setVisibility(View.GONE);
    }

    public void setLastWeekList(List<Last7Day> last7DayList) {
        List<Last7Day> last7DayListRemaining = new ArrayList<>();
        boolean isDataAvailable = false;
        for (int i = 0; i < last7DayList.size(); i++) {
            Last7Day last7Day = last7DayList.get(i);
            if (i == 0) {
                headerLayout.setVisibility(View.VISIBLE);
                isDataAvailable = true;
                showRankOne(last7Day);
                continue;
            } else if (i == 1) {
                isDataAvailable = true;
                showRankTwo(last7Day);
                continue;
            } else if (i == 2) {
                isDataAvailable = true;
                showRankThree(last7Day);
                continue;
            }
            last7DayListRemaining.add(last7Day);
        }

        if (isDataAvailable) {
            headerLayout.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.GONE);
        }
        else {
            headerLayout.setVisibility(View.GONE);
            noDataView.setVisibility(View.VISIBLE);
        }

        Last7DaysAdapter adapter = new Last7DaysAdapter(last7DayListRemaining);
        recyclerView.setAdapter(adapter);
    }

    private void showRankOne(Last7Day data) {
        firstPositionLayout.setVisibility(View.VISIBLE);
        if (data.getName().trim().length() > 0) {
            name1.setText(String.format("%s", data.getName()));
            circularTextView1.setText("1");
        }
        points1.setText(data.getRoi());
        location1.setText(data.getTotalWinning());
    }

    private void showRankTwo(Last7Day data) {
        secondPositionLayout.setVisibility(View.VISIBLE);
        if (data.getName().trim().length() > 0) {
            name2.setText(String.format("%s %s", data.getName(), data.getTotalWinning()));
            circularTextView2.setText(String.format("%s", data.getName().toUpperCase().charAt(0)));
        }
        points2.setText(data.getRoi());
        location2.setText(data.getTotalWinning());
        position2.setText("1");
    }

    private void showRankThree(Last7Day data) {
        thirdPositionLayout.setVisibility(View.VISIBLE);
        if (data.getName().trim().length() > 0) {
            name3.setText(String.format("%s %s", data.getName(), data.getTotalWinning()));
            circularTextView3.setText(String.format("%s", data.getName().toUpperCase().charAt(0)));
        }
        points3.setText(data.getRoi());
        location3.setText(data.getTotalWinning());
        position3.setText("1");
    }
}
