package com.example.sywlia.licznikkaloryczny.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;

import java.util.ArrayList;


public class DisciplineSpinnerAdapter  extends ArrayAdapter<DisciplineDTO> {
    public DisciplineSpinnerAdapter(Context context, ArrayList<DisciplineDTO> values) {
        super(context, R.layout.row_spinner_select_discipline,values);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        View view=layoutInflater.inflate(R.layout.row_spinner_select_discipline,parent,false);
        DisciplineDTO disciplineDTO=getItem(position);
        TextView textView= (TextView) view.findViewById(R.id.spinner_row_textView);
        assert disciplineDTO != null;
        textView.setText(disciplineDTO.getName());

        return view;
    }
}
