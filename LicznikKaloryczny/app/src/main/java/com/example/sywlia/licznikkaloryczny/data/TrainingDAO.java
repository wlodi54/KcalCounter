package com.example.sywlia.licznikkaloryczny.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.IDisciplineDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.ITrainingDAO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.SimulatedTraining;
import com.example.sywlia.licznikkaloryczny.models.Statistic;
import com.example.sywlia.licznikkaloryczny.models.TrainingDTO;
import com.example.sywlia.licznikkaloryczny.models.TrainingInPlanDTO;

import java.util.ArrayList;
import java.util.Calendar;

public class TrainingDAO extends DBHelper implements ITrainingDAO {
    private Context ctx;
    public TrainingDAO(Context ctx) {
        super(ctx,DB_NAME, null, DB_VERSION);
        this.ctx=ctx;
    }

    @Override
    public ArrayList<TrainingDTO> getHistoryList(long idUser) {

        String[] columns={TrainingDTO.HISTORY_ID, TrainingDTO.HISTORY_DISTANCE, TrainingDTO.HISTORY_MAP, TrainingDTO.HISTORY_KCAL,DisciplineDTO.DISCIPLINE_ID, TrainingDTO.HISTORY_TIME,
                TrainingDTO.USER_ID, TrainingDTO.HISTORY_ODZNAKA,TrainingDTO.HISTORY_DATE};
        Cursor cursor=getReadableDatabase().query(TrainingDTO.HISTORY_TABLE,columns, TrainingDTO.USER_ID+"=?",new String[]{String.valueOf(idUser)},null,null,null);
        ArrayList<TrainingDTO> historyDTOs=new ArrayList<>();
        if(cursor.getCount()>0){
            cursor.moveToLast();
            do {
                TrainingDTO historyDTO=new TrainingDTO();
                historyDTO.setHistoryId(cursor.getLong(cursor.getColumnIndex(TrainingDTO.HISTORY_ID)));
                historyDTO.setMapJson(cursor.getString(cursor.getColumnIndex(TrainingDTO.HISTORY_MAP)));
                historyDTO.setDistance(cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_DISTANCE)));
                historyDTO.setKcal(cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_KCAL)));
                historyDTO.setDisciplineId(cursor.getLong(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_ID)));
                historyDTO.setTime(cursor.getLong(cursor.getColumnIndex(TrainingDTO.HISTORY_TIME)));
                historyDTO.setUserId(cursor.getLong(cursor.getColumnIndex(TrainingDTO.USER_ID)));
                historyDTO.setOdznaka(cursor.getInt(cursor.getColumnIndex(TrainingDTO.HISTORY_ODZNAKA)));
                historyDTO.setDate(cursor.getInt(cursor.getColumnIndex(TrainingDTO.HISTORY_DATE)));

                historyDTOs.add(historyDTO);
            }while (cursor.moveToPrevious());
        }else {
            cursor.close();
            close();
            return null;
        }
        cursor.close();
        close();
        return historyDTOs;

    }

    public ArrayList<Statistic> getStatisticList(long idUser){
        ArrayList<Statistic> list=new ArrayList<>();
        long idDiscipline=1;
        String[] columns={TrainingDTO.HISTORY_DISTANCE, TrainingDTO.HISTORY_KCAL, TrainingDTO.DISCIPLINE_ID, TrainingDTO.HISTORY_TIME, TrainingDTO.HISTORY_ODZNAKA};
        Cursor cursor =getReadableDatabase().query(TrainingDTO.HISTORY_TABLE,columns, TrainingDTO.USER_ID+"=?",new String[]{String.valueOf(idUser)},null,null, TrainingDTO.DISCIPLINE_ID);
        if(cursor.getCount()>1) {
            double distance=0;
            long time=0;
            int kcal=0,goldMedals=0,silverMedals=0,brownMedals=0;

            IDisciplineDAO disciplineDAO = new DisciplineDAO(ctx);
            DisciplineDTO disciplineDTO;
            cursor.moveToFirst();
            do {

                if(idDiscipline!=cursor.getLong(cursor.getColumnIndex(TrainingDTO.DISCIPLINE_ID))){
                    disciplineDTO = disciplineDAO.getDiscipline(idDiscipline);
                    Statistic statistic=new Statistic(kcal,time,disciplineDTO.getName(),distance,goldMedals,silverMedals,brownMedals);
                    list.add(statistic);
                    idDiscipline=cursor.getLong(cursor.getColumnIndex(TrainingDTO.DISCIPLINE_ID));
                    distance=0;
                    time=0;
                    kcal=0;
                    goldMedals=0;
                    silverMedals=0;
                    brownMedals=0;

                }else {
                    distance += cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_DISTANCE));
                    time += cursor.getLong(cursor.getColumnIndex(TrainingDTO.HISTORY_TIME));
                    kcal += cursor.getInt(cursor.getColumnIndex(TrainingDTO.HISTORY_KCAL));
                    switch (cursor.getInt(cursor.getColumnIndex(TrainingDTO.HISTORY_ODZNAKA))) {
                        case 1: {
                            goldMedals++;
                            break;
                        }
                        case 2: {
                            silverMedals++;
                            break;
                        }
                        case 3: {
                            brownMedals++;
                            break;
                        }
                        default:
                            break;
                    }

                }
            } while (cursor.moveToNext());
            disciplineDTO = disciplineDAO.getDiscipline(idDiscipline);
            Statistic statistic=new Statistic(kcal,time,disciplineDTO.getName(),distance,goldMedals,silverMedals,brownMedals);
            list.add(statistic);
        }else {
            cursor.close();
            close();
            return null;
        }
        cursor.close();
        close();
        return list;
    }
    @Override
    public LongSparseArray<ArrayList<Double>> getBestHistory(Long kcalValue, long idDiscipline, long idUser) {
        ArrayList<SimulatedTraining> listOfBestHistory=new ArrayList<>();
        LongSparseArray<ArrayList<Double>> lista=new LongSparseArray<>();
        String[] columns={
                TrainingDTO.HISTORY_AVRKCALMIN, TrainingDTO.HISTORY_AVRKCALMETER};
        Cursor cursor=getReadableDatabase().query(TrainingDTO.HISTORY_TABLE,columns
                , TrainingDTO.USER_ID+"=?"+" and "+ TrainingDTO.DISCIPLINE_ID+" =? "+"and "+ TrainingDTO.HISTORY_KCAL+" >=?"+" and "
                        + TrainingDTO.HISTORY_AVRKCALMIN+" !=?"+" and "+ TrainingDTO.HISTORY_AVRKCALMETER+"!=?"
                ,new String[]{String.valueOf(idUser),String.valueOf(idDiscipline),String.valueOf(kcalValue),String.valueOf(0),String.valueOf(0)},null,null, TrainingDTO.HISTORY_AVRKCALMIN);
        int i=0;
        if(cursor.getCount()>1){
            cursor.moveToFirst();
            do{
                double avrKcalMeter=cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_AVRKCALMETER));
                double avrKcalMinute=cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_AVRKCALMIN));
                ArrayList<Double> arrayList=new ArrayList<>();
                arrayList.add(avrKcalMeter);
                arrayList.add(avrKcalMinute);
                lista.append(i,arrayList);
                SimulatedTraining SimulatedTraining=new SimulatedTraining(idDiscipline,avrKcalMeter,avrKcalMinute);
                listOfBestHistory.add(SimulatedTraining);
                i++;

            }while (cursor.moveToNext());
        }else{
            listOfBestHistory=null;
        }
        cursor.close();
        close();
        return lista;
    }

    @Override
    public long insertMapJson(TrainingDTO history) throws Exception{

        ContentValues cv=new ContentValues();
        cv.put(TrainingDTO.HISTORY_KCAL,history.getKcal());
        cv.put(TrainingDTO.HISTORY_TIME,history.getTime());
        cv.put(TrainingDTO.HISTORY_MAP,history.getMapJson());
        cv.put(TrainingDTO.DISCIPLINE_ID,history.getDisciplineId());
        cv.put(TrainingDTO.HISTORY_DISTANCE,history.getDistance());
        cv.put(TrainingDTO.USER_ID,history.getUserId());
        cv.put(TrainingDTO.HISTORY_AVRKCALMIN,history.getAvrKcalMin());
        cv.put(TrainingDTO.HISTORY_AVRKCALMETER,history.getAvrKcalMeter());
        cv.put(TrainingDTO.HISTORY_ODZNAKA,history.getOdznaka());
        cv.put(TrainingDTO.HISTORY_DATE,history.getDate());

        long cashId=getWritableDatabase().insert(TrainingDTO.HISTORY_TABLE,null,cv);

        history.setHistoryId(cashId);
        close();
        return cashId;


    }
    @Override
    public TrainingDTO getHistory(long id_history){
        TrainingDTO historyDTO;

        String[] columns={TrainingDTO.HISTORY_ID, TrainingDTO.HISTORY_MAP, TrainingDTO.HISTORY_DISTANCE, TrainingDTO.HISTORY_KCAL
                ,DisciplineDTO.DISCIPLINE_ID, TrainingDTO.HISTORY_TIME, TrainingDTO.USER_ID, TrainingDTO.HISTORY_AVRKCALMIN, TrainingDTO.HISTORY_AVRKCALMETER, TrainingDTO.HISTORY_ODZNAKA, TrainingDTO.HISTORY_AVRSPEED};
        Cursor cursor=getReadableDatabase().query(TrainingDTO.HISTORY_TABLE,columns, TrainingDTO.HISTORY_ID+"=?",
                new String[]{String.valueOf(id_history)},null,null,null,"1");
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                historyDTO=new TrainingDTO();
                historyDTO.setHistoryId(cursor.getLong(cursor.getColumnIndex(TrainingDTO.HISTORY_ID)));
                historyDTO.setMapJson(cursor.getString(cursor.getColumnIndex(TrainingDTO.HISTORY_MAP)));
                historyDTO.setDistance(cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_DISTANCE)));
                historyDTO.setKcal(cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_KCAL)));
                historyDTO.setDisciplineId(cursor.getLong(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_ID)));
                historyDTO.setTime(cursor.getLong(cursor.getColumnIndex(TrainingDTO.HISTORY_TIME)));
                historyDTO.setUserId(cursor.getLong(cursor.getColumnIndex(TrainingDTO.USER_ID)));
                historyDTO.setAvrKcalMeter(cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_AVRKCALMETER)));
                historyDTO.setAvrKcalMin(cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_AVRKCALMIN)));
                historyDTO.setOdznaka(cursor.getInt(cursor.getColumnIndex(TrainingDTO.HISTORY_ODZNAKA)));
                historyDTO.setAvrSpeed(cursor.getDouble(cursor.getColumnIndex(TrainingDTO.HISTORY_AVRSPEED)));
            }while(cursor.moveToNext());
        }else{
            historyDTO=null;
        }
        cursor.close();
        close();
        return historyDTO;
    }
}
