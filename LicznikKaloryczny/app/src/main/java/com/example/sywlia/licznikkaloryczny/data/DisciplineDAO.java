package com.example.sywlia.licznikkaloryczny.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.IDisciplineDAO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineSubDTO;

import java.util.ArrayList;

public class DisciplineDAO extends DBHelper implements IDisciplineDAO {
    public DisciplineDAO(Context ctx) {
        super(ctx,DB_NAME, null, DB_VERSION);
    }


    public ArrayList<DisciplineSubDTO> getArraySubDiscipline(long idDiscipline){
        String[] columns={DisciplineSubDTO.DISCIPLINESUB_MET,DisciplineSubDTO.DISCIPLINESUB_MINSPEED,
                DisciplineSubDTO.DISCIPLINESUB_MAXSPEED};
        Cursor cursor=getReadableDatabase().query(DisciplineSubDTO.DISCIPLINESUB_TABLE,columns,DisciplineSubDTO.DISCIPLINE_ID+"=?",new String[]{String.valueOf(idDiscipline)},null,null,null);
        ArrayList<DisciplineSubDTO> list=new ArrayList<>();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                DisciplineSubDTO disciplineSub=new DisciplineSubDTO();

                disciplineSub.setDisciplineId(idDiscipline);
                disciplineSub.setMaxSpeed(cursor.getDouble(cursor.getColumnIndex(DisciplineSubDTO.DISCIPLINESUB_MAXSPEED)));
                disciplineSub.setMinSpeed(cursor.getDouble(cursor.getColumnIndex(DisciplineSubDTO.DISCIPLINESUB_MINSPEED)));
                disciplineSub.setMet(cursor.getDouble(cursor.getColumnIndex(DisciplineSubDTO.DISCIPLINESUB_MET)));
                list.add(disciplineSub);
            }while (cursor.moveToNext());
        }else {
            list = null;
        }
        cursor.close();
        return list;
    }
    public DisciplineDTO getDiscipline(long idDiscipline){
        String[] columns={DisciplineDTO.DISCIPLINE_ID,DisciplineDTO.DISCIPLINE_NAME,DisciplineDTO.DISCIPLINE_MET,DisciplineDTO.DISCIPLINE_IMGSRC,DisciplineDTO.DISCIPLINE_IMGSRCBIG};
        DisciplineDTO disciplineDTO=new DisciplineDTO();
        Cursor cursor=getReadableDatabase().query(DisciplineDTO.DISCIPLINE_TABLE,columns,DisciplineDTO.DISCIPLINE_ID+"=?",new String[]{String.valueOf(idDiscipline)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                disciplineDTO.setName(cursor.getString(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_NAME)));
                disciplineDTO.setId(cursor.getLong(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_ID)));
                disciplineDTO.setMet(cursor.getDouble(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_MET)));
                disciplineDTO.setImgSrc(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_IMGSRC)));
                disciplineDTO.setImgSrcBig(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_IMGSRCBIG)));


            }while(cursor.moveToNext());
        }
        cursor.close();

        return disciplineDTO;
    }
    public DisciplineDTO getDiscipline(String nameDiscypline){
        String[] columns={DisciplineDTO.DISCIPLINE_ID,DisciplineDTO.DISCIPLINE_NAME,DisciplineDTO.DISCIPLINE_MET,DisciplineDTO.DISCIPLINE_IMGSRC,DisciplineDTO.DISCIPLINE_IMGSRCBIG};
        DisciplineDTO disciplineDTO=new DisciplineDTO();
        Cursor cursor=getReadableDatabase().query(DisciplineDTO.DISCIPLINE_TABLE,columns,DisciplineDTO.DISCIPLINE_NAME+"=?",new String[]{nameDiscypline},null,null,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                disciplineDTO.setName(cursor.getString(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_NAME)));
                disciplineDTO.setId(cursor.getLong(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_ID)));
                disciplineDTO.setMet(cursor.getDouble(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_MET)));
                disciplineDTO.setImgSrc(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_IMGSRC)));
                disciplineDTO.setImgSrcBig(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_IMGSRCBIG)));


            }while(cursor.moveToNext());
        }else{
            disciplineDTO=null;
        }
        cursor.close();
        close();
        return disciplineDTO;
    }
    @Override
    public ArrayList<DisciplineDTO> getDisciplineList() {

        String selectQuery = "SELECT " +
                " * FROM " +
                DisciplineDTO.DISCIPLINE_TABLE;
        Cursor cursor = getReadableDatabase().rawQuery(selectQuery, null);
        ArrayList<DisciplineDTO> disciplineList = new ArrayList<>();
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    DisciplineDTO discipline = new DisciplineDTO();
                    discipline.setId(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_ID)));
                    discipline.setName(cursor.getString(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_NAME)));
                    discipline.setImgSrc(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_IMGSRC)));
                    discipline.setImgSrcBig(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_IMGSRCBIG)));
                    discipline.setMet(cursor.getInt(cursor.getColumnIndex(DisciplineDTO.DISCIPLINE_MET)));
                    disciplineList.add(discipline);
                } while (cursor.moveToNext());
            }
        }else
        {
            return null;
        }
        cursor.close();

        return disciplineList;
    }
    @Override
    public void insert(DisciplineDTO discipline){

        ContentValues cv=new ContentValues();
        cv.put(DisciplineDTO.DISCIPLINE_NAME,discipline.getName());

        getWritableDatabase().insert(DisciplineDTO.DISCIPLINE_TABLE,null,cv);

    }
}
