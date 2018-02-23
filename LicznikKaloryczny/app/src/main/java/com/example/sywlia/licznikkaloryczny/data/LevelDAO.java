package com.example.sywlia.licznikkaloryczny.data;


import android.content.Context;
import android.database.Cursor;
import com.example.sywlia.licznikkaloryczny.interfaces.ILevelDAO;
import com.example.sywlia.licznikkaloryczny.models.LevelDTO;

public class LevelDAO extends DBHelper implements ILevelDAO {

    public LevelDAO(Context ctx) {
        super(ctx,DB_NAME, null, DB_VERSION);
    }

    @Override
    public LevelDTO getPoziom(int iloscMedali){

        String[] columns={LevelDTO.POZIOM_ID,LevelDTO.POZIOM_IMAGE_SRC,LevelDTO.POZIOM_NAZWA};
        Cursor cursor=getReadableDatabase().query(LevelDTO.POZIOM_TABLE,columns,LevelDTO.POZIOM_ODZNAKI_MIN+"<=? and "+LevelDTO.POZIOM_ODZNAKI_MAX
                +">? ",new String[]{String.valueOf(iloscMedali),String.valueOf(iloscMedali)},null,null,null,String.valueOf(1));
        LevelDTO poziom=new LevelDTO();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                poziom.setId(cursor.getLong(cursor.getColumnIndex(LevelDTO.POZIOM_ID)));
                poziom.setImageSrc(cursor.getInt(cursor.getColumnIndex(LevelDTO.POZIOM_IMAGE_SRC)));
                poziom.setImie(cursor.getString(cursor.getColumnIndex(LevelDTO.POZIOM_NAZWA)));

            }while (cursor.moveToNext());
        }else{
            poziom=null;
        }
        close();
        cursor.close();
        return poziom;
    }
    public int getImgSrc(long id){
        int imgSrc=0;
        String [] colums={LevelDTO.POZIOM_IMAGE_SRC};
        Cursor cursor=getReadableDatabase().query(LevelDTO.POZIOM_TABLE,colums,LevelDTO.POZIOM_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                imgSrc=cursor.getInt(cursor.getColumnIndex(LevelDTO.POZIOM_IMAGE_SRC));

            }while (cursor.moveToNext());
        }else{
            imgSrc=0;
        }
        cursor.close();
        close();
        return imgSrc;
    }
}
