package com.example.sywlia.licznikkaloryczny.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.SimulateTrainingAsync;
import com.example.sywlia.licznikkaloryczny.activities.TrainingActivity;
import com.example.sywlia.licznikkaloryczny.adapters.DisciplineSpinnerAdapter;
import com.example.sywlia.licznikkaloryczny.adapters.KcalSpinnerAdapter;
import com.example.sywlia.licznikkaloryczny.data.DisciplineDAO;
import com.example.sywlia.licznikkaloryczny.data.UserDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.IAsyncResponse;
import com.example.sywlia.licznikkaloryczny.interfaces.IUserDAO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.SimulatedTraining;

import java.util.ArrayList;



public class SelectTrainingFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener,IAsyncResponse {
    private static final String ARG_PARAM1 = "param1";
    private long idUser;

    private Spinner spinner;
    private DisciplineDAO disciplineDAO;
    private ArrayList<DisciplineDTO> list;
    ArrayList<Integer> listOfKcalValues;
    private Button startTrainingBtn;
    private int positionItemSpinner;
    private RelativeLayout gridLayout;
    private ArrayList<SimulatedTraining> listBestHistory;
    private int kcalSelectedSpinner;


    public SelectTrainingFragment() {
    }


    public static SelectTrainingFragment newInstance(long param1) {
        SelectTrainingFragment fragment = new SelectTrainingFragment();
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
        listOfKcalValues=new ArrayList<>();
    }
    Spinner spinnerKcal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(container!=null){
            container.removeAllViews();
        }
        View view=inflater.inflate(R.layout.fragment_select_training, container, false);
        this.disciplineDAO=new DisciplineDAO(getActivity());
        gridLayout=(RelativeLayout) view.findViewById(R.id.training_relativeLayout);
        spinner=(Spinner)view.findViewById(R.id.select_training_spinner);
        spinnerKcal= (Spinner) view.findViewById(R.id.select_training_spinner_kcal);
        startTrainingBtn=(Button)view.findViewById(R.id.select_training_startBtn);
        startTrainingBtn.setOnClickListener(SelectTrainingFragment.this);
        setSpinner();
        setSpinnerKcal();

        return view;
    }

    private void setSpinnerKcal() {

        for(int i=0;i<8;i++){
            listOfKcalValues.add(i*100);
        }
        ArrayAdapter adapterKcal=new KcalSpinnerAdapter(getContext(),listOfKcalValues);
        adapterKcal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKcal.setAdapter(adapterKcal);
        spinnerKcal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                kcalSelectedSpinner=parent.getSelectedItemPosition();
                kcalSelectedSpinner= (int) parent.getItemAtPosition(kcalSelectedSpinner);

                SimulateTrainingAsync task=new SimulateTrainingAsync(getActivity(),SelectTrainingFragment.this);

                DisciplineDTO discipline2=list.get(positionItemSpinner);
                long id_discipline=discipline2.getId();
                if(kcalSelectedSpinner!=0) {
                    task.execute(1L, (long) kcalSelectedSpinner,id_discipline,idUser);
                    gridLayout.setVisibility(View.VISIBLE);
                }else {gridLayout.setVisibility(View.INVISIBLE);}

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpinner() {
        list=disciplineDAO.getDisciplineList();
        if(list!=null) {
            ArrayAdapter adapter2 = new DisciplineSpinnerAdapter(getContext(), list);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter2);
            spinner.setOnItemSelectedListener(SelectTrainingFragment.this);
        }
    }

    @Override
    public void onClick(View v) {

        if(positionItemSpinner>=0&&list.size()!=0){
            DisciplineDTO discipline2=list.get(positionItemSpinner);
            long id=discipline2.getId();
            Intent intent=new Intent(getContext(),TrainingActivity.class);
            Bundle bundle=new Bundle();
            bundle.putParcelableArrayList("ListOfBestHistory",listBestHistory);
            bundle.putLong("SelectedDiscipline",id);
            bundle.putLong("UserLogged",idUser);
            bundle.putInt("KcalSelected",kcalSelectedSpinner);
            intent.putExtras(bundle);
            startActivity(intent);
        }else
        {
            Toast.makeText(getContext(),"First Select the Discipline...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerKcal.setSelection(0);
        positionItemSpinner=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void getBestHistoryList(ArrayList<SimulatedTraining> output) {
        listBestHistory=output;
    }
}
