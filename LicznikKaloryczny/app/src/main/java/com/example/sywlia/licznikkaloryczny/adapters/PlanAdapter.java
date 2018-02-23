package com.example.sywlia.licznikkaloryczny.adapters;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.data.DisciplineDAO;
import com.example.sywlia.licznikkaloryczny.helpers.UnitConversion;
import com.example.sywlia.licznikkaloryczny.interfaces.IDisciplineDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.IPlanDrop;
import com.example.sywlia.licznikkaloryczny.models.Day;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.preferences.PlanDragListner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlanAdapter  extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private ArrayList<Day> listDay;
    private IPlanDrop listener;
    private ArrayList<DisciplineDTO> listDiscipline;
    private boolean isDay;
    Context ctx;
    public ArrayList<Day> getListDay() {
        return listDay;
    }

    public ArrayList<DisciplineDTO> getListDiscipline() {

        return listDiscipline;
    }
    public void updateListInt(ArrayList<DisciplineDTO> list) {
        listDiscipline = list;
    }
   public void updateList(ArrayList<Day> list) {
        listDay = null;
        listDay=list;
    }

    public PlanAdapter(IPlanDrop listener, ArrayList<DisciplineDTO> listDiscipline,Context ctx) {
        this.listener = listener;
        this.listDiscipline = listDiscipline;
        isDay=false;
        this.ctx=ctx;
    }

    public PlanAdapter(ArrayList<Day> listDay, IPlanDrop listener,Context ctx) {
        this.listDay = listDay;
        this.listener = listener;
        isDay=true;
        this.ctx=ctx;
    }

    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan,parent,false);
        return new PlanViewHolder(view);
    }
    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {
        holder.frameLayoutDay.setTag(position);
        holder.frameLayoutDay.setOnDragListener(new PlanDragListner(listener,0));
        if(isDay){
            UnitConversion conversion=new UnitConversion();
            holder.frameLayoutDay.setBackgroundColor(ContextCompat.getColor(ctx, R.color.backgroundLighter));
            holder.frameLayoutDay.setTag(position);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(String.valueOf(listDay.get(position).getDay()));
            holder.img.setVisibility(View.GONE);

            if(listDay.get(position).getSize()==0){
                holder.rel1.setVisibility(View.INVISIBLE);
                holder.rel2.setVisibility(View.INVISIBLE);
                holder.rel3.setVisibility(View.INVISIBLE);
            }
            for(int i=0;i<listDay.get(position).getSize();i++){
                switch (i){
                    case 0:
                        if(listDay.get(position).getItem(i).getCzasCwiczenia()!=0){
                            holder.czas1.setText(String.valueOf(conversion.conversionMillisToMinutes(listDay.get(position).getItem(i).getCzasCwiczenia())));
                        }else holder.czas1.setVisibility(View.GONE);
                        if(listDay.get(position).getItem(i).getDystansCwiczenia()!=0){
                            holder.dystans1.setText(String.valueOf(conversion.coversionMetersToKm(listDay.get(position).getItem(i).getDystansCwiczenia())));
                        }else holder.dystans1.setVisibility(View.GONE);
                        IDisciplineDAO disciplineDAO=new DisciplineDAO(ctx);
                        DisciplineDTO disciplineDTO=disciplineDAO.getDiscipline(listDay.get(position).getItem(i).getIdDyscypliny());
                        holder.dysc1.setText(String.valueOf(disciplineDTO.getName()));

                        holder.rel1.setTag(position);
                        holder.rel1.setOnClickListener(new PlanDragListner(listener,0));
                        holder.rel1.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        if(listDay.get(position).getItem(i).getCzasCwiczenia()!=0){
                            holder.czas2.setText(String.valueOf(conversion.conversionMillisToMinutes(listDay.get(position).getItem(i).getCzasCwiczenia())));
                        }else holder.czas2.setVisibility(View.GONE);
                        if(listDay.get(position).getItem(i).getDystansCwiczenia()!=0){
                            holder.dystans2.setText(String.valueOf(conversion.coversionMetersToKm(listDay.get(position).getItem(i).getDystansCwiczenia())));
                        }else holder.czas2.setVisibility(View.GONE);
                        holder.dysc2.setText(String.valueOf(listDay.get(position).getItem(i).getDyscyplinaCwiczenia()));
                        holder.rel2.setTag(position);
                        holder.rel2.setOnClickListener(new PlanDragListner(listener,1));
                        holder.rel2.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        if(listDay.get(position).getItem(i).getCzasCwiczenia()!=0){
                            holder.czas3.setText(String.valueOf(conversion.conversionMillisToMinutes(listDay.get(position).getItem(i).getCzasCwiczenia())));
                        } else holder.czas3.setVisibility(View.GONE);
                        if(listDay.get(position).getItem(i).getDystansCwiczenia()!=0){
                            holder.dystans3.setText(String.valueOf(conversion.coversionMetersToKm(listDay.get(position).getItem(i).getDystansCwiczenia())));
                        }else holder.dystans3.setVisibility(View.GONE);
                        holder.dysc3.setText(String.valueOf(listDay.get(position).getItem(i).getDyscyplinaCwiczenia()));
                      holder.rel3.setTag(position);
                        holder.rel3.setOnClickListener(new PlanDragListner(listener,2));
                        holder.rel3.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

        }else {
            holder.rel1.setVisibility(View.GONE);
            holder.rel2.setVisibility(View.GONE);
            holder.rel3.setVisibility(View.GONE);
            holder.img.setVisibility(View.VISIBLE);
            holder.img.setImageResource(listDiscipline.get(position).getImgSrcBig());
            holder.frameLayoutDay.setTag(position);
            holder.border.setVisibility(View.INVISIBLE);
            holder.frameLayoutDay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ClipData data = ClipData.newPlainText("", "");
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                view.startDragAndDrop(data, shadowBuilder, view, 0);
                            } else {
                                view.startDrag(data, shadowBuilder, view, 0);
                            }
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    PlanDragListner getDragInstance() {
        if (listener != null) {
            return new PlanDragListner(listener,0);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        if(isDay){
            return listDay.size();
        }else
            return listDiscipline.size();

    }
    static class PlanViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.text1)
        TextView text;
        @BindView(R.id.row_plan)
        RelativeLayout frameLayoutDay;
        @BindView(R.id.czas1) TextView czas1;
        @BindView(R.id.czas2) TextView czas2;
        @BindView(R.id.czas3) TextView czas3;

        @BindView(R.id.dystans1) TextView dystans1;
        @BindView(R.id.dystans2)
        TextView dystans2;
        @BindView(R.id.dystans3) TextView dystans3;

        @BindView(R.id.dysc1) TextView dysc1;
        @BindView(R.id.dysc2) TextView dysc2;
        @BindView(R.id.dysc3) TextView dysc3;
        @BindView(R.id.imageView)
        ImageView img;

        @BindView(R.id.rl1) RelativeLayout rel1;
        @BindView(R.id.rl2) RelativeLayout rel2;
        @BindView(R.id.rl3) RelativeLayout rel3;
        @BindView(R.id.plan_border)View border;
        public PlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
