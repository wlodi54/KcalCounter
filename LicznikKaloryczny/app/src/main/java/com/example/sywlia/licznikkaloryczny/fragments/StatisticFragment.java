package com.example.sywlia.licznikkaloryczny.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.adapters.StatisticAdapter;
import com.example.sywlia.licznikkaloryczny.data.TrainingDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.models.Statistic;

import java.util.ArrayList;


public class StatisticFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    private long idUser;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public StatisticFragment() {

    }

    public static StatisticFragment newInstance(long param1) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUser = getArguments().getLong(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_statistic, container, false);
        if(getActivity().getActionBar()!=null)getActivity().getActionBar().setTitle("Statystyki");
        recyclerView=(RecyclerView) view.findViewById(R.id.statistic_recycler);
        TrainingDAO historyDAO=new TrainingDAO(getActivity());
        ArrayList<Statistic> list;
        list= historyDAO.getStatisticList(this.idUser);
        if(list!=null) {
            adapter = new StatisticAdapter(getActivity(), list);
            layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        }else{
            Toast.makeText(getContext(), R.string.brak_statystyk,Toast.LENGTH_LONG).show();
        }
        return view;
    }
}