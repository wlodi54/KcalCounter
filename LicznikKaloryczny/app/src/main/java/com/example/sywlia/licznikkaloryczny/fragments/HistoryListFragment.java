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
import com.example.sywlia.licznikkaloryczny.adapters.HistoryListAdapter;
import com.example.sywlia.licznikkaloryczny.data.TrainingDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.ITrainingDAO;
import com.example.sywlia.licznikkaloryczny.models.TrainingDTO;

import java.util.ArrayList;


public class HistoryListFragment extends Fragment {
    private static final String ARG_PARAM1 = "id_user";
    private long idUser;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public HistoryListFragment() {
    }
    public static HistoryListFragment newInstance(Long idUser) {
        HistoryListFragment fragment = new HistoryListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, idUser);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.idUser = getArguments().getLong(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history_list,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.history_recycler_view);
        if(getActivity().getActionBar()!=null)getActivity().getActionBar().setTitle("Historia");
        ITrainingDAO historyDAO=new TrainingDAO(getActivity());
        ArrayList<TrainingDTO> listOfHistory;
        listOfHistory=historyDAO.getHistoryList(this.idUser);
        if(listOfHistory!=null) {
            recyclerAdapter = new HistoryListAdapter(getContext(), listOfHistory);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
        }else{
            Toast.makeText(getContext(),"Brak Historii...",Toast.LENGTH_LONG).show();

        }

        return view;
    }
}
