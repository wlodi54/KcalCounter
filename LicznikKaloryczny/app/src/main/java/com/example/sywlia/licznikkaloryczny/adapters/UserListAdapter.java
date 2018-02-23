package com.example.sywlia.licznikkaloryczny.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.data.LevelDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.ILevelDAO;
import com.example.sywlia.licznikkaloryczny.models.LevelDTO;
import com.example.sywlia.licznikkaloryczny.models.UserDTO;

import java.util.ArrayList;


public class UserListAdapter extends ArrayAdapter<UserDTO> {
    private Context context;
    public UserListAdapter(Context context, ArrayList<UserDTO> values) {
        super(context, R.layout.row_user_list,values);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        final View view=layoutInflater.inflate(R.layout.row_user_list,parent,false);
        UserDTO user=getItem(position);
        ILevelDAO poziomDAO= new LevelDAO(context);
        assert user != null;
        int iloscMedali=user.getMedalsGold()-user.getMedalsBrown();
        LevelDTO poziom;
        if(iloscMedali<0) {
            poziom = poziomDAO.getPoziom(0);
        }else{
            poziom=poziomDAO.getPoziom(iloscMedali);
        }
        ImageView imageView= (ImageView) view.findViewById(R.id.list_users_row_image);
        TextView textView=(TextView) view.findViewById(R.id.list_users_row_user_name);
        textView.setText(user.getName());
        imageView.setImageResource(poziom.getImageSrc());



        return view;
    }
}
