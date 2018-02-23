package com.example.sywlia.licznikkaloryczny.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.activities.PlanActivity;
import com.example.sywlia.licznikkaloryczny.adapters.PlanListAdapter;
import com.example.sywlia.licznikkaloryczny.data.PlanDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.models.PlanDTO;

import java.util.ArrayList;

public class PlanListFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    private long idUser;
    private String mParam2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private PlanDAO planDAO;
    private Button newPlanButton;

    public PlanListFragment() {
    }

    public static PlanListFragment newInstance(long param1) {
        PlanListFragment fragment = new PlanListFragment();
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
        planDAO=new PlanDAO(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<PlanDTO> list=planDAO.getListOfPlans(this.idUser);
        adapter = new PlanListAdapter(getActivity(), list);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        newPlanButton=(Button) view.findViewById(R.id.list_plan_buttonNew);
        ArrayList<PlanDTO> list=planDAO.getListOfPlans(this.idUser);
        recyclerView=(RecyclerView) view.findViewById(R.id.fragment_list_plan_rw);
        if(list!=null) {
            adapter = new PlanListAdapter(getActivity(), list);
            manager = new LinearLayoutManager(getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
            recyclerView.setHasFixedSize(true);
        }else
            Toast.makeText(getActivity(),"Brak planów.",Toast.LENGTH_SHORT).show();

        newPlanButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getContext(),PlanActivity.class);
        intent.putExtra("planIdUser",idUser);
        intent.putExtra("planIdPlan",0);
        startActivity(intent);

    }
}
