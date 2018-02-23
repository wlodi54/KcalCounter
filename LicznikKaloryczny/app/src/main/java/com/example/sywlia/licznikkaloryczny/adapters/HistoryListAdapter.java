package com.example.sywlia.licznikkaloryczny.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.activities.HistoryDetailsActivity;
import com.example.sywlia.licznikkaloryczny.data.DisciplineDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.helpers.UnitConversion;
import com.example.sywlia.licznikkaloryczny.interfaces.IDisciplineDAO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.TrainingDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.RecyclerViewHolder> {
    private static final int GOLD_MEDAL = 1;
    private static final int SILVER_MEDAL = 2;
    private static final int BROWN_MEDAL = 3;
    private ArrayList<TrainingDTO> historyList;
    private Context context;
    public HistoryListAdapter(Context context,ArrayList<TrainingDTO> historyList) {
        this.historyList=historyList;
        this.context=context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_list,parent,false);
        return new RecyclerViewHolder(view,this.context,historyList);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        UnitConversion conversion=new UnitConversion();
        long id=historyList.get(position).getDisciplineId();
        IDisciplineDAO disciplineDAO=new DisciplineDAO(context);
        DisciplineDTO disciplineDTO=disciplineDAO.getDiscipline(id);
        Log.i(Constans.LOG_HISTORYADAPTER,String.valueOf("id of dicipline: "+disciplineDTO.getId()));
        String distance=conversion.conversionMeters(historyList.get(position).getDistance());
        String time=conversion.conversionTime(historyList.get(position).getTime());
        String[] split=distance.split("_");
        if(split.length>1) {
            holder.distanceTextView.setText(split[0]);
            holder.kmUnit.setText(split[1]);
        }
        split=time.split("_");
        if(split.length>1) {
            holder.timeTextView.setText(split[0]);
            holder.secUnit.setText(split[1]);
        }
        holder.iconImageView.setImageResource(disciplineDTO.getImgSrcBig());

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR,historyList.get(position).getDate());

        holder.dateTextView.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+"."+calendar.get(Calendar.MONTH)+"."+calendar.get(Calendar.YEAR)));
        holder.kcalTextView.setText(String.valueOf((int)historyList.get(position).getKcal()));

        holder.nameDisciplineTextView.setText(disciplineDTO.getName());
        switch (historyList.get(position).getOdznaka()){
            case GOLD_MEDAL:
                holder.odznakaImageView.setVisibility(View.VISIBLE);
                holder.odznakaImageView.setImageResource(R.drawable.gold_medal);
                break;
            case SILVER_MEDAL:
                holder.odznakaImageView.setVisibility(View.VISIBLE);
                holder.odznakaImageView.setImageResource(R.drawable.silver_medal);
                break;
            case BROWN_MEDAL:
                holder.odznakaImageView.setVisibility(View.VISIBLE);
                holder.odznakaImageView.setImageResource(R.drawable.brown_medal);
                break;
            default:
                break;
        }

    }
    @Override
    public int getItemCount() {
        if(historyList.size()>0) {
            return historyList.size();
        }else
            return 0;

    }
    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameDisciplineTextView,distanceTextView,kcalTextView,dateTextView,timeTextView,kcalUnit,kmUnit,secUnit;
        ImageView iconImageView,odznakaImageView;
        Context context;
        ArrayList<TrainingDTO> listOfHistory;
        RecyclerViewHolder(View itemView,Context context,ArrayList<TrainingDTO> listOfHistory) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.listOfHistory=listOfHistory;
            this.context=context;
            iconImageView=(ImageView) itemView.findViewById(R.id.history_recycler_row_img);
            nameDisciplineTextView=(TextView) itemView.findViewById(R.id.history_recycler_row_name);
            dateTextView =(TextView) itemView.findViewById(R.id.history_recycler_row_dateTextView);
            distanceTextView=(TextView)itemView.findViewById(R.id.history_recycler_row_distance);
            kcalTextView=(TextView)itemView.findViewById(R.id.history_recycler_row_kcal);
            odznakaImageView=(ImageView)itemView.findViewById(R.id.history_recycler_row_odznaka);
            timeTextView=(TextView) itemView.findViewById(R.id.history_recycler_row_time);
            kcalUnit= (TextView) itemView.findViewById(R.id.history_recycler_row_kcalUnit);
            kmUnit= (TextView) itemView.findViewById(R.id.history_recycler_row_km);
            secUnit= (TextView) itemView.findViewById(R.id.history_recycler_row_sec);

        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context,HistoryDetailsActivity.class);
            TrainingDTO historyDto=this.listOfHistory.get(getAdapterPosition());

            intent.putExtra("history_id",historyDto.getHistoryId());
            this.context.startActivity(intent);
        }
    }
}
