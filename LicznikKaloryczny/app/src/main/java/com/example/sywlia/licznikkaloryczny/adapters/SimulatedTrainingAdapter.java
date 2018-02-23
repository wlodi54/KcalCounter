package com.example.sywlia.licznikkaloryczny.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.models.SimulatedTraining;

import java.util.ArrayList;


public class SimulatedTrainingAdapter  extends ArrayAdapter {
    private ArrayList<SimulatedTraining> list= new ArrayList<>();
    private static final int[] odznaki={R.drawable.gold_medal,R.drawable.silver_medal,R.drawable.brown_medal};
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        HistoryHolder historyHolder;

        if(view==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =layoutInflater.inflate(R.layout.row_simulated_trainig,parent,false);
            historyHolder=new HistoryHolder();
            historyHolder.tx_tx1=(TextView) view.findViewById(R.id.best_history_time_textView);
            historyHolder.tx_tx2=(TextView) view.findViewById(R.id.best_history_distance_textView);
            historyHolder.odznakaImageView=(ImageView) view.findViewById(R.id.best_history_odznaka);
            view.setTag(historyHolder);
        }else{
            historyHolder=(HistoryHolder) view.getTag();
        }

        SimulatedTraining bestHistory=(SimulatedTraining) getItem(position);
        assert bestHistory != null;

        historyHolder.odznakaImageView.setImageResource(odznaki[position]);
        historyHolder.tx_tx1.setText(bestHistory.getKcalMinInString());
        historyHolder.tx_tx2.setText(bestHistory.getKcalMeterInString());

        return view;
    }

    public SimulatedTrainingAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(SimulatedTraining object) {
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    static class HistoryHolder{
        TextView tx_tx1,tx_tx2;
        ImageView odznakaImageView;
    }
}
