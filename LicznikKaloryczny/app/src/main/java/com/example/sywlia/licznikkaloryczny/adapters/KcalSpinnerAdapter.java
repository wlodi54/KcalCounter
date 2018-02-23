package com.example.sywlia.licznikkaloryczny.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;

import java.util.ArrayList;



public class KcalSpinnerAdapter extends ArrayAdapter<Integer> {
    private ArrayList<Integer> resource;
    public KcalSpinnerAdapter(Context context, ArrayList<Integer> resource) {

        super(context, R.layout.row_spinner_select_kcal,resource);
        this.resource=resource;

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View view=layoutInflater.inflate(R.layout.row_spinner_select_kcal,parent,false);
        int value=getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.spinnerKcal_row_textView);
        if(value!=0) {
            textView.setText(String.valueOf(value+" kcal"));
        }else {
            textView.setText(R.string.spinner_kcal_label);
        }
        return view;
    }
}
