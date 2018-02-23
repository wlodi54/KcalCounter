package com.example.sywlia.licznikkaloryczny.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.models.PlanDTO;
import com.example.sywlia.licznikkaloryczny.models.TrainingInPlanDTO;

import java.util.ArrayList;


public class PlanDAO extends DBHelper {


    public PlanDAO(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);

    }

    public void insertPlan(ArrayList<TrainingInPlanDTO> plan, PlanDTO planObj, boolean isNewPlan) {
        ContentValues cv = new ContentValues();
        cv.put(PlanDTO.PLAN_DATA, planObj.getDataPlan());
        cv.put(PlanDTO.PLAN_NAZWA, planObj.getNazwaPlan());
        cv.put(PlanDTO.PLAN_ILOSC_CW,planObj.getIloscCwiczen());
        disactivePlan();
        cv.put(PlanDTO.PLAN_CZYAKTYWNY, 1);
        cv.put(PlanDTO.ID_USER, planObj.getIdUser());
        long id=planObj.getIdPlan();
        if (isNewPlan) {
            id = getWritableDatabase().insert(PlanDTO.PLAN_TABLICA, null, cv);
        }else {
            cv.put(PlanDTO.ID_PLAN,planObj.getIdPlan());
            getWritableDatabase().update(PlanDTO.PLAN_TABLICA, cv, PlanDTO.ID_PLAN + "=?", new String[]{String.valueOf(planObj.getIdPlan())});
            getWritableDatabase().delete(TrainingInPlanDTO.CWICZENIE_TABELA, TrainingInPlanDTO.ID_PLAN + "=?", new String[]{String.valueOf(planObj.getIdPlan())});

        }
        cv.clear();
        for (int i = 0; i < plan.size(); i++) {
            cv.put(TrainingInPlanDTO.CWICZENIE_CZAS, plan.get(i).getCzasCwiczenia());
            cv.put(TrainingInPlanDTO.CWICZENIE_DYSTANS, plan.get(i).getDystansCwiczenia());
            cv.put(TrainingInPlanDTO.CWICZENIE_DZIEN, plan.get(i).getDzienCwiczenia());
            cv.put(TrainingInPlanDTO.CWICZENIE_ID_DYSCYPLINY, plan.get(i).getIdDyscypliny());
            cv.put(TrainingInPlanDTO.ID_PLAN, id);

            getWritableDatabase().insert(TrainingInPlanDTO.CWICZENIE_TABELA, null, cv);

        }
        close();

    }

    private void disactivePlan() {

        ContentValues cv = new ContentValues();
        cv.put(PlanDTO.PLAN_CZYAKTYWNY, 0);
        getWritableDatabase().update(PlanDTO.PLAN_TABLICA, cv, PlanDTO.PLAN_CZYAKTYWNY + "=?", new String[]{String.valueOf(1)});
    }

    public void activate(long idPlan){
        disactivePlan();
        ContentValues cv=new ContentValues();
        cv.put(PlanDTO.PLAN_CZYAKTYWNY, 1);
        getWritableDatabase().update(PlanDTO.PLAN_TABLICA, cv, PlanDTO.ID_PLAN + "=?", new String[]{String.valueOf(idPlan)});


    }
    public int getIloscDni(long idPlan) {
        int iloscDni = 0;
        Cursor cursor = getReadableDatabase().query(TrainingInPlanDTO.CWICZENIE_TABELA, new String[]{TrainingInPlanDTO.CWICZENIE_DZIEN}, TrainingInPlanDTO.ID_PLAN + "=?",
                new String[]{String.valueOf(idPlan)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String dayTemp = cursor.getString(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_DZIEN));
            do {
                if (!cursor.getString(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_DZIEN)).equals(dayTemp)) {
                    iloscDni++;
                }
                dayTemp = cursor.getString(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_DZIEN));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return iloscDni+1;
    }

    public ArrayList<PlanDTO> getListOfPlans(long idUser){
        ArrayList<PlanDTO> list=new ArrayList<>();
        String[] colums={PlanDTO.PLAN_NAZWA,PlanDTO.ID_USER,PlanDTO.ID_PLAN,PlanDTO.PLAN_CZYAKTYWNY,PlanDTO.PLAN_DATA,PlanDTO.PLAN_ILOSC_CW};
        Cursor cursor=getReadableDatabase().query(PlanDTO.PLAN_TABLICA,colums,PlanDTO.ID_USER+"=?",new String[]{String.valueOf(idUser)},null,null,PlanDTO.PLAN_CZYAKTYWNY+" DESC");
        if(cursor.getCount()>0){
            Log.i(Constans.LOG_HISTORYADAPTER,String.valueOf(cursor.getCount()));

            cursor.moveToFirst();
            do {
                PlanDTO plan=new PlanDTO();
                plan.setNazwaPlan(cursor.getString(cursor.getColumnIndex(PlanDTO.PLAN_NAZWA)));
                plan.setIdUser(cursor.getLong(cursor.getColumnIndex(PlanDTO.ID_USER)));
                plan.setCzyAktywny(cursor.getInt(cursor.getColumnIndex(PlanDTO.PLAN_CZYAKTYWNY)));
                plan.setDataPlan(cursor.getString(cursor.getColumnIndex(PlanDTO.PLAN_DATA)));
                plan.setIdPlan(cursor.getLong(cursor.getColumnIndex(PlanDTO.ID_PLAN)));
                plan.setIloscCwiczen(cursor.getInt(cursor.getColumnIndex(PlanDTO.PLAN_ILOSC_CW)));
                list.add(plan);

            }   while (cursor.moveToNext());
        }else
            list=null;

        cursor.close();
        close();
        return list;
    }
    public PlanDTO getPlan(long idPlan){
        String[] colums={PlanDTO.PLAN_NAZWA,PlanDTO.ID_USER,PlanDTO.ID_PLAN,PlanDTO.PLAN_CZYAKTYWNY,PlanDTO.PLAN_DATA};
        Cursor cursor=getReadableDatabase().query(PlanDTO.PLAN_TABLICA,colums,PlanDTO.ID_PLAN+"=?",new String[]{String.valueOf(idPlan)},
                null,null,null);
        PlanDTO plan;
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                plan=new PlanDTO();
                plan.setNazwaPlan(cursor.getString(cursor.getColumnIndex(PlanDTO.PLAN_NAZWA)));
                plan.setIdUser(cursor.getLong(cursor.getColumnIndex(PlanDTO.ID_USER)));
                plan.setCzyAktywny(cursor.getInt(cursor.getColumnIndex(PlanDTO.PLAN_CZYAKTYWNY)));
                plan.setDataPlan(cursor.getString(cursor.getColumnIndex(PlanDTO.PLAN_DATA)));
                plan.setIdPlan(cursor.getLong(cursor.getColumnIndex(PlanDTO.ID_PLAN)));

            }   while (cursor.moveToNext());
        }else
            plan=null;

        cursor.close();
        close();
        return plan;
    }
    public ArrayList<TrainingInPlanDTO> getListCwiczenieWPlanie(long idPlan){

        ArrayList<TrainingInPlanDTO> list;
        String[] columns={TrainingInPlanDTO.CWICZENIE_ID_DYSCYPLINY,TrainingInPlanDTO.CWICZENIE_DYSTANS,TrainingInPlanDTO.CWICZENIE_CZAS,
                TrainingInPlanDTO.CWICZENIE_DZIEN,TrainingInPlanDTO.ID_PLAN};
        Cursor cursor=getReadableDatabase().query(TrainingInPlanDTO.CWICZENIE_TABELA,columns,TrainingInPlanDTO.ID_PLAN+"=?",
                new String[]{String.valueOf(idPlan)},null,null,null);
        Log.i(Constans.LOG_HISTORYADAPTER,String.valueOf("idPlan to: "+cursor.getCount()));
        if(cursor.getCount()>0){
            list=new ArrayList<>();
            cursor.moveToFirst();
            do {
                TrainingInPlanDTO cw=new TrainingInPlanDTO();
                cw.setIdDyscypliny(cursor.getLong(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_ID_DYSCYPLINY)));
                cw.setIdPlanu(cursor.getLong(cursor.getColumnIndex(TrainingInPlanDTO.ID_PLAN)));
                cw.setDystansCwiczenia(cursor.getFloat(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_DYSTANS)));
                cw.setDzienCwiczenia(cursor.getInt(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_DZIEN)));
                cw.setCzasCwiczenia(cursor.getLong(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_CZAS)));
                //cw.setDyscyplinaCwiczenia(cursor.getString(cursor.getColumnIndex(TrainingInPlanDTO.CWICZENIE_NAZWA_DYSCYPLINY)));
                list.add(cw);

            } while(cursor.moveToNext());
        }else
            list=null;

        cursor.close();
        close();
        return list;
    }
}
