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
import com.example.sywlia.licznikkaloryczny.activities.PlanActivity;
import com.example.sywlia.licznikkaloryczny.data.PlanDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.models.PlanDTO;

import java.util.ArrayList;



public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.PlanListViewHolder> {
    private Context ctx;
    private ArrayList<PlanDTO> listOfPlans;

    public PlanListAdapter(Context ctx, ArrayList<PlanDTO> listOfPlans) {
        this.ctx=ctx;
        this.listOfPlans=listOfPlans;
    }
    @Override
    public PlanListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_list,parent,false);

        return new PlanListViewHolder(view,this.ctx,this.listOfPlans);
    }

    @Override
    public void onBindViewHolder(PlanListViewHolder holder, int position) {
        PlanDAO planDAO =new PlanDAO(ctx);

        if(listOfPlans.get(position).getCzyAktywny()==1){
            holder.isLogged.setImageResource(R.mipmap.check_mark);
            holder.isLogged.setVisibility(View.VISIBLE);
        }

        holder.dateTexView.setText(listOfPlans.get(position).getDataPlan());
        holder.nameTextView.setText(listOfPlans.get(position).getNazwaPlan());

        holder.iloscDniTextView.setText(String.valueOf(planDAO.getIloscDni(listOfPlans.get(position).getIdPlan())+" dni treningu"));
        holder.iloscTreningowTextView.setText(String.valueOf(listOfPlans.get(position).getIloscCwiczen()+" ćwiczeń."));

    }

    @Override
    public int getItemCount() {
        if(listOfPlans!=null) {
            return listOfPlans.size();
        }else return 0;
    }

    static class PlanListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        Context context;
        ArrayList<PlanDTO> list;
        TextView nameTextView,dateTexView,iloscDniTextView, iloscTreningowTextView;
        ImageView isLogged;

        PlanListViewHolder(View itemView,Context context, ArrayList<PlanDTO> list) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.context=context;
            this.list=list;
            nameTextView =(TextView) itemView.findViewById(R.id.list_plan_rowName);
            dateTexView=(TextView) itemView.findViewById(R.id.list_plan_rowDate);
            iloscDniTextView=(TextView) itemView.findViewById(R.id.list_plan_rowIloscDni);
            isLogged =(ImageView) itemView.findViewById(R.id.list_plan_rowIsLogged);
            iloscTreningowTextView=(TextView) itemView.findViewById(R.id.list_plan_rowIloscTreningow);

            Log.i(Constans.LOG_APP,"planListAdapter idplan:"+String.valueOf(list.get(0).getIdPlan()));

        }

        @Override
        public void onClick(View v) {
            Intent intent =new Intent(context,PlanActivity.class);
            intent.putExtra("planIdUser",list.get(getAdapterPosition()).getIdUser());
            intent.putExtra("planIdPlan",list.get(getAdapterPosition()).getIdPlan());
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
