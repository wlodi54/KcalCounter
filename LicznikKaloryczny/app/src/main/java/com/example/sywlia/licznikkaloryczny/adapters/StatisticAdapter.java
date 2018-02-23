package com.example.sywlia.licznikkaloryczny.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.helpers.UnitConversion;
import com.example.sywlia.licznikkaloryczny.models.Statistic;

import java.util.ArrayList;


public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.RecyclerViewHolder> {
    private Context context;
    private ArrayList<Statistic> listOfStatistic;
    public StatisticAdapter(Context context, ArrayList<Statistic> listOfStatistic) {
        this.context=context;
        this.listOfStatistic=listOfStatistic;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(this.context).inflate(R.layout.row_statistic,parent,false);
        return new RecyclerViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        UnitConversion conversion=new UnitConversion();
        String timerString=conversion.conversionTime(listOfStatistic.get(position).getTime());
        String[] split=conversion.conversionMeters(listOfStatistic.get(position).getDistance()).split("_");
        holder.disciplineUnit.setText(split[1]);
        holder.distanceTextView.setText(split[0]);
        split=timerString.split("_");
        holder.timeTextView.setText(split[0]);
        holder.timeUnit.setText(split[1]);

        holder.kcalTextView.setText(String.valueOf(listOfStatistic.get(position).getKcal()));
        holder.disciplineTextView.setText(String.valueOf(listOfStatistic.get(position).getDiscipline()));
        holder.brownMedals.setText(String.valueOf(listOfStatistic.get(position).getBrownMedals()));
        holder.silverMedals.setText(String.valueOf(listOfStatistic.get(position).getSilverMedals()));
        holder.goldMedals.setText(String.valueOf(listOfStatistic.get(position).getGoldMedals()));

    }

    @Override
    public int getItemCount() {
        if(listOfStatistic.size()>0) {
            return listOfStatistic.size();
        }else
            return 0;

    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView kcalTextView,distanceTextView,timeTextView,goldMedals,silverMedals,brownMedals,disciplineTextView,timeUnit,disciplineUnit;
        Context context;
        public RecyclerViewHolder(View itemView, Context context) {
            super(itemView);
            this.context=context;
            kcalTextView=(TextView) itemView.findViewById(R.id.statistic_row_kcal);
            disciplineTextView=(TextView)itemView.findViewById(R.id.statistic_row_discipline);
            distanceTextView=(TextView)itemView.findViewById(R.id.statistic_row_distance);
            timeTextView=(TextView)itemView.findViewById(R.id.statistic_row_time);
            goldMedals=(TextView)itemView.findViewById(R.id.statistic_row_gold);
            silverMedals=(TextView)itemView.findViewById(R.id.statistic_row_silver);
            brownMedals=(TextView)itemView.findViewById(R.id.statistic_row_brown);
            timeUnit= (TextView) itemView.findViewById(R.id.statistic_row_timeUnit);
            disciplineUnit= (TextView) itemView.findViewById(R.id.statistic_row_distanceUnit);

        }
    }
}
