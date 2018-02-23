package com.example.sywlia.licznikkaloryczny.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.IUserDAO;
import com.example.sywlia.licznikkaloryczny.models.PlanDTO;
import com.example.sywlia.licznikkaloryczny.models.TrainingDTO;
import com.example.sywlia.licznikkaloryczny.models.UserDTO;

import java.util.ArrayList;


public class UserDAO extends DBHelper implements IUserDAO {

    public UserDAO(Context ctx) {

        super(ctx,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void increaseMedals(long idUser,int odznaka) {
        UserDTO userDTO=getUser(idUser);
        int medals;
        if(odznaka==1){
            medals=userDTO.getMedalsGold();
            medals++;
            userDTO.setMedalsGold(medals);
            userDTO.setPunktyRozowuju(userDTO.getPunktyRozowuju()+1);
        }else if(odznaka==2){
            medals=userDTO.getMedalsSliver();
            medals++;
            userDTO.setMedalsSliver(medals);
        }else if(odznaka==3){
            medals=userDTO.getMedalsBrown();
            medals++;
            userDTO.setMedalsBrown(medals);
            userDTO.setPunktyRozowuju(userDTO.getPunktyRozowuju()-1);
        }else if(odznaka==4){
            userDTO.setPunktyRozowuju(userDTO.getPunktyRozowuju()-1);
        }

        try {
            insertUser(userDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
    }
    @Override
    public UserDTO getUserLoged(){
        UserDTO user=new UserDTO();
        String[] columns={UserDTO.USER_ID,UserDTO.USER_NAME,UserDTO.USER_WEIGHT,UserDTO.USER_HEIGHT};
        Cursor cursor= getReadableDatabase().query(UserDTO.USER_TABLE, columns, UserDTO.USER_IS_LOGGED + "=?",
                new String[]{String.valueOf(1)}, null, null, null, null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                user.setId(cursor.getLong(cursor.getColumnIndex(UserDTO.USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserDTO.USER_NAME)));
                user.setWeight(cursor.getDouble(cursor.getColumnIndex(UserDTO.USER_WEIGHT)));
                user.setHeight(cursor.getDouble(cursor.getColumnIndex(UserDTO.USER_HEIGHT)));
                user.setLogged(1);

            }while(cursor.moveToNext());
        }else{
            cursor.close();
            return null;
        }
        cursor.close();
        return user;
    }
    @Override
    public UserDTO getUser(long id){

        String[] columns={UserDTO.USER_ID,UserDTO.USER_NAME,UserDTO.USER_WEIGHT,UserDTO.USER_HEIGHT,UserDTO.USER_IS_LOGGED,UserDTO.USER_MEDALS_BROWN,
                UserDTO.USER_MEDALS_SILVER,UserDTO.USER_MEDALS_GOLD,UserDTO.USER_POZIOM,UserDTO.USER_PKT_ROZWOJU};
        UserDTO user=new UserDTO();
        Cursor cursor=getReadableDatabase().query(UserDTO.USER_TABLE,columns,UserDTO.USER_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    user.setName(cursor.getString(cursor.getColumnIndex(UserDTO.USER_NAME)));
                    user.setWeight(cursor.getDouble(cursor.getColumnIndex(UserDTO.USER_WEIGHT)));
                    user.setLogged(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_IS_LOGGED)));
                    user.setHeight(cursor.getDouble(cursor.getColumnIndex(UserDTO.USER_HEIGHT)));
                    user.setId(cursor.getLong(cursor.getColumnIndex(UserDTO.USER_ID)));
                    user.setMedalsBrown(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_MEDALS_BROWN)));
                    user.setMedalsSliver(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_MEDALS_SILVER)));
                    user.setMedalsGold(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_MEDALS_GOLD)));
                    user.setPoziom(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_POZIOM)));
                    user.setPunktyRozowuju(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_PKT_ROZWOJU)));

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return user;
    }
    @Override
    public void deleteUser(long id){

        getWritableDatabase().delete(UserDTO.USER_TABLE,UserDTO.USER_ID+"=?",new String[]{String.valueOf(id)});
        getWritableDatabase().delete(TrainingDTO.HISTORY_TABLE,TrainingDTO.USER_ID+"=?",new String[]{String.valueOf(id)});
        getWritableDatabase().delete(PlanDTO.PLAN_TABLICA,PlanDTO.ID_USER+"=?",new String[]{String.valueOf(id)});

    }
    @Override
    public ArrayList<UserDTO> getUsersList(){
        String selectQuery="SELECT * FROM "+UserDTO.USER_TABLE;

        Cursor cursor=getReadableDatabase().rawQuery(selectQuery,null);
        ArrayList<UserDTO> listUsers=new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                UserDTO user=new UserDTO();
                user.setId(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserDTO.USER_NAME)));
                user.setMedalsBrown(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_MEDALS_BROWN)));
                user.setMedalsSliver(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_MEDALS_SILVER)));
                user.setMedalsGold(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_MEDALS_GOLD)));
                user.setPoziom(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_POZIOM)));
                user.setPunktyRozowuju(cursor.getInt(cursor.getColumnIndex(UserDTO.USER_PKT_ROZWOJU)));
                listUsers.add(user);
            }while (cursor.moveToNext());
        }else{
            cursor.close();
            return null;
        }
        cursor.close();
        return listUsers;
    }
    @Override
    public long insertUser(UserDTO user) throws Exception{


        ContentValues cv=new ContentValues();
        cv.put(UserDTO.USER_IS_LOGGED,0);
        getWritableDatabase().update(UserDTO.USER_TABLE,cv,null,null);
        cv.clear();
        cv.put(UserDTO.USER_NAME,user.getName());
        cv.put(UserDTO.USER_WEIGHT,user.getWeight());
        cv.put(UserDTO.USER_IS_LOGGED,1);
        cv.put(UserDTO.USER_HEIGHT,user.getHeight());
        cv.put(UserDTO.USER_MEDALS_BROWN,user.getMedalsBrown());
        cv.put(UserDTO.USER_MEDALS_SILVER,user.getMedalsSliver());
        cv.put(UserDTO.USER_MEDALS_GOLD,user.getMedalsGold());
        cv.put(UserDTO.USER_POZIOM,user.getPoziom());
        cv.put(UserDTO.USER_PKT_ROZWOJU,user.getPunktyRozowuju());

        if(user.getId()!=0){

            getWritableDatabase().update(UserDTO.USER_TABLE,cv,UserDTO.USER_ID+"=?",new String[]{String.valueOf(user.getId())});

            return 0;
        }else{
            return getWritableDatabase().insert(UserDTO.USER_TABLE, null, cv);
        }

    }
    @Override
    public void loginUser(long idUser){

        ContentValues cvLogout=new ContentValues();
        cvLogout.put(UserDTO.USER_IS_LOGGED,0);
        getWritableDatabase().update(UserDTO.USER_TABLE,cvLogout,null,null);
        ContentValues cvLogin=new ContentValues();
        cvLogin.put(UserDTO.USER_IS_LOGGED,1);
        getWritableDatabase().update(UserDTO.USER_TABLE,cvLogin,UserDTO.USER_ID+"=?",new String[]{String.valueOf(idUser)});


    }
}
