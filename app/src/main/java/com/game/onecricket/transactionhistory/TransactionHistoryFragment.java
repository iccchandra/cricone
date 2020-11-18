package com.game.onecricket.transactionhistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.game.onecricket.R;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        loadRecyclerView(view);
        return view;
    }

    private void loadRecyclerView(View view) {
        List<Movie> movieList = new ArrayList<>();

        movieList.add(new Movie("Schindler's List", "Biography, Drama, History", 1993));
        movieList.add(new Movie("Pulp Fiction", "Crime, Drama", 1994));
        movieList.add(new Movie("No Country for Old Men", "Crime, Drama, Thriller", 2007));
        movieList.add(new Movie("Léon: The Professional", "Crime, Drama, Thriller", 1994));
        movieList.add(new Movie("Fight Club", "Drama", 1999));
        movieList.add(new Movie("Forrest Gump", "Drama, Romance", 1994));
        movieList.add(new Movie("The Shawshank Redemption", "Crime, Drama", 1994));
        movieList.add(new Movie("The Godfather", "Crime, Drama", 1972));
        movieList.add(new Movie("A Beautiful Mind", "Biography, Drama", 2001));
        movieList.add(new Movie("Good Will Hunting", "Drama", 1997));

        TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(movieList);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }


}
