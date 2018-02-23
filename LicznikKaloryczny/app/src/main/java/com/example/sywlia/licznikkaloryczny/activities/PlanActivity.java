package com.example.sywlia.licznikkaloryczny.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.adapters.PlanAdapter;
import com.example.sywlia.licznikkaloryczny.data.DisciplineDAO;
import com.example.sywlia.licznikkaloryczny.data.PlanDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.helpers.UnitConversion;
import com.example.sywlia.licznikkaloryczny.interfaces.IDisciplineDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.IPlanDrop;
import com.example.sywlia.licznikkaloryczny.models.Day;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.PlanDTO;
import com.example.sywlia.licznikkaloryczny.models.TrainingInPlanDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlanActivity extends AppCompatActivity implements IPlanDrop {



    private long idUser;
    private long idPlan;
    private boolean isNewPlan;
    @BindView(R.id.add_planRecyclerView)
    RecyclerView recyclerViewTop;
    @BindView(R.id.add_planRVButtons) RecyclerView recyclerViewBottom;
    @BindView(R.id.text_empty_list_top)
    TextView textEmptyListTop;
    @BindView(R.id.text_empty_list_bottom) TextView textEmptyListBottom;
    @BindView(R.id.add_plan_activeBtn)
    Button activeBtn;
    @BindView(R.id.add_plan_namePlan) TextView planNameTextView;
    PlanAdapter mTopListAdapter;
    private PlanDTO plan;
    ArrayList<Day> list;
    EditText dystans,czas;
    private boolean getExtras(){
        Intent intent=getIntent();
        if(intent.getExtras().isEmpty()){
            return false;
        }
        this.idUser=intent.getExtras().getLong("planIdUser",0);
        this.idPlan=intent.getExtras().getLong("planIdPlan",0);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        ButterKnife.bind(this);
        if (getExtras()) {
            LinearLayoutManager layoutManagerTop = new GridLayoutManager(this,1,LinearLayoutManager.HORIZONTAL,false);

            LinearLayoutManager layoutManagerBottom = new GridLayoutManager(this, 6);
            layoutManagerBottom.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerViewTop.setLayoutManager(layoutManagerTop);
            recyclerViewBottom.setLayoutManager(layoutManagerBottom);

            if(idPlan>0) {
                list = addDaysToPlan();
            }else {
                list = addDaysEmpty();
            }
            mTopListAdapter = new PlanAdapter(list, this, getBaseContext());
            recyclerViewTop.setAdapter(mTopListAdapter);
            IDisciplineDAO disciplineDAO = new DisciplineDAO(this);
            ArrayList<DisciplineDTO> l = disciplineDAO.getDisciplineList();
            PlanAdapter mBottomListAdapter = new PlanAdapter(this, l, getBaseContext());
            recyclerViewBottom.setAdapter(mBottomListAdapter);
        }
    }

    @NonNull
    private ArrayList<Day> addDaysToPlan() {

        PlanDAO planDAO=new PlanDAO(this);
        plan=planDAO.getPlan(idPlan);

        ArrayList<TrainingInPlanDTO> lista= planDAO.getListCwiczenieWPlanie(idPlan);
        ArrayList<Day> list = new ArrayList<>();
        for (int i = 0; i < Constans.dayCount; i++) {
            list.add(new Day(i, getResources().getStringArray(R.array.dni_tygodnia)[i]));
            for(int j=0;j<lista.size();j++){
                if(lista.get(j).getDzienCwiczenia()==i){
                    list.get(i).addCwiczenie(lista.get(j));
                }
            }
        }
        planNameTextView.setVisibility(View.VISIBLE);
        planNameTextView.setText(plan.getNazwaPlan());
        if(this.plan.getCzyAktywny()==0)
            activeBtn.setVisibility(View.VISIBLE);
        this.isNewPlan=false;

        return list;
    }
    @NonNull
    private ArrayList<Day> addDaysEmpty() {
        ArrayList<Day> list = new ArrayList<>();
        for (int i = 0; i < Constans.dayCount; i++) {
            list.add(new Day(i, getResources().getStringArray(R.array.dni_tygodnia)[i]));
        }
        this.isNewPlan=true;

        return list;
    }

    @Override
    public TrainingInPlanDTO opedDialog(final TrainingInPlanDTO cwRecieve, final ArrayList<Day> list, final int day, final boolean isNewCw, final int index) {
        final UnitConversion conversion=new UnitConversion();
        final ArrayList<Day> list2=this.list;
        View view=getLayoutInflater().inflate(R.layout.dialog_plan_add_item,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dystans=(EditText) view.findViewById(R.id.plan_dialog_dystansEditText);
        czas=(EditText) view.findViewById(R.id.plan_dialog_timeEditText);
        TextView dysc=(TextView) view.findViewById(R.id.plan_dialog_dyscTextView);
        Button saveBtn=(Button) view.findViewById(R.id.plan_dialog_btnSave);
        Button deleteBtn=(Button) view.findViewById(R.id.plan_dialog_btnDelete);
        DisciplineDAO dao=new DisciplineDAO(this);
        cwRecieve.setIdDyscypliny(dao.getDiscipline(cwRecieve.getDyscyplinaCwiczenia()).getId());
        if(cwRecieve.getDystansCwiczenia()!=0){
            dystans.setText(String.valueOf(conversion.coversionMetersToKm(cwRecieve.getDystansCwiczenia())));
        }
        if(cwRecieve.getCzasCwiczenia()!=0){
            czas.setText(String.valueOf(conversion.conversionMillisToMinutes(cwRecieve.getCzasCwiczenia())));
        }
        if(!cwRecieve.getDyscyplinaCwiczenia().isEmpty()){
            dysc.setText(cwRecieve.getDyscyplinaCwiczenia());
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String czasString= czas.getText().toString();
                String dystansString=dystans.getText().toString();
                String[] split=czasString.split(" ");
                String[] splitDystnans=dystansString.split(" ");
                if(czasString.isEmpty()){
                    cwRecieve.setCzasCwiczenia(0);
                }else
                    cwRecieve.setCzasCwiczenia(conversion.conversionMinutesTOMilis(split[0]));

                if(dystansString.isEmpty()){
                    cwRecieve.setDystansCwiczenia(0);
                }else
                    cwRecieve.setDystansCwiczenia(conversion.conversionKmToMeters(splitDystnans[0]));

                if(isNewCw){
                    list2.get(day).addCwiczenie(cwRecieve);
                }else{
                    list2.get(day).getLista().remove(index);
                    list2.get(day).getLista().add(index,cwRecieve);
                }
                for(int i=0;i<list2.size();i++){
                    for(int j=0;j<list2.get(i).getLista().size();j++) {
                        Log.i("x",String.valueOf(list2.get(i).getLista().get(j).getDzienCwiczenia()+" string to:"+list2.get(i).getLista().get(j).getDyscyplinaCwiczenia()));
                    }
                }
                mTopListAdapter.updateList(list2);
                mTopListAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isNewCw) {
                    list2.get(day).getLista().remove(index);
                }
                mTopListAdapter.updateList(list2);
                mTopListAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        return cwRecieve;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void savePlanClick(View view) {
        ArrayList<Day> listDays=mTopListAdapter.getListDay();
        final ArrayList<TrainingInPlanDTO> listCw=new ArrayList<>();
        for(int i=0;i<listDays.size();i++){
            if(listDays.get(i).getSize()>0){
                for(int j=0;j<listDays.get(i).getSize();j++){
                    listCw.add(listDays.get(i).getItem(j));
                }
            }
        }
        if(listCw.size()>0){
            final PlanDTO plan;
            if(this.plan!=null) {
                plan = this.plan;
            }else
                plan=new PlanDTO();
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            View v=getLayoutInflater().inflate(R.layout.dialog_save_plan,null);
            final EditText namePlanEditText=(EditText) v.findViewById(R.id.plan_dialog_save_dystansEditText);
            Button savePlanButton=(Button) v.findViewById(R.id.plan_dialog_save_btnSave);
            builder.setView(v);
            final AlertDialog dialog=builder.create();
            dialog.show();

            if(idPlan>0){
                namePlanEditText.setText(this.plan.getNazwaPlan());
            }
            savePlanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!namePlanEditText.getText().toString().isEmpty()){
                        Calendar calendar=Calendar.getInstance();
                        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
                        String date= df.format(calendar.getTime());
                        plan.setNazwaPlan(namePlanEditText.getText().toString());
                        plan.setCzyAktywny(1);
                        plan.setIdUser(idUser);
                        plan.setIloscCwiczen(listCw.size());
                        plan.setDataPlan(date);
                        PlanDAO planDAO=new PlanDAO(PlanActivity.this);
                        planDAO.insertPlan(listCw,plan,isNewPlan);
                        dialog.dismiss();
                        finish();
                    }
                }
            });
        }
    }

    public void activatePlanClick(View view) {
        PlanDAO planDAO=new PlanDAO(this);
        if(this.idPlan!=0){
            planDAO.activate(this.idPlan);
            finish();
        }else
            Toast.makeText(this, R.string.first_save_plan,Toast.LENGTH_SHORT).show();
    }
}