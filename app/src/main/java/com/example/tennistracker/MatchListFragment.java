package com.example.tennistracker;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tennistracker.classes.ListAdapter;
import com.example.tennistracker.classes.Match;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MatchListFragment extends Fragment {

    private static final String url = "jdbc:mysql://10.0.2.2:3306/androidApp";
    private static final String user = "root";
    private static final String pass = "password";
    private static final String selectRequest = "SELECT DISTINCT * FROM tennis_match";

    List<Match> elements = new ArrayList<>();

    public MatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_list, container, false);
        connectionDB();
        init(view);
        return view;
    }

    public void init(View view) {
        ListAdapter listAdapter = new ListAdapter(elements, this.getContext());
        RecyclerView recyclerView = view.findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    public void addElementToList(String id, String player1, String player2, String player1_score, String player2_score, String locations) {
        elements.add(new Match(id, player1, player2, player1_score, player2_score, locations));
    }

    public void connectionDB(){
        new Thread(() -> {
            try {
                Connection connection = DriverManager.getConnection(url, user, pass);

                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(selectRequest);

                elements.clear();
                while (rs.next())
                    addElementToList(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}