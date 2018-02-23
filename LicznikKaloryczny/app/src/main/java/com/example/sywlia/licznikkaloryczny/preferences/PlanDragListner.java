package com.example.sywlia.licznikkaloryczny.preferences;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.adapters.PlanAdapter;
import com.example.sywlia.licznikkaloryczny.interfaces.IPlanDrop;
import com.example.sywlia.licznikkaloryczny.models.Day;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.TrainingInPlanDTO;

import java.util.ArrayList;
import java.util.List;

public class PlanDragListner implements View.OnDragListener,View.OnClickListener {
    private boolean isDropped = false;
    private IPlanDrop mListener;
    private int index;
    public PlanDragListner(IPlanDrop listener, int index) {
        mListener = listener;
        this.index=index;
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                isDropped = true;
                int positionTarget = -1;

                View viewSource = (View) dragEvent.getLocalState();

                if (view.getId() == R.id.row_plan || view.getId() == R.id.text_empty_list_bottom
                        || view.getId() == R.id.rl1) {
                    RecyclerView target;

                    if (view.getId() == R.id.text_empty_list_top) {
                        target = (RecyclerView) view.getRootView()
                                .findViewById(R.id.add_planRecyclerView);
                    } else if (view.getId() == R.id.add_planRVButtons) {
                        return false;
                    } else {
                        target = (RecyclerView) view.getParent();
                        if(target.getId()==R.id.add_planRVButtons)
                            break;
                        Log.i("x",String.valueOf(view.getId()));
                        positionTarget = (int) view.getTag();
                    }

                    RecyclerView source = (RecyclerView) viewSource.getParent();

                    PlanAdapter adapterSource = (PlanAdapter) source.getAdapter();
                    int positionSource = (int) viewSource.getTag();

                    List<DisciplineDTO> listSource = adapterSource.getListDiscipline();

                    PlanAdapter adapterTarget = (PlanAdapter) target.getAdapter();
                    ArrayList<Day> customListTarget = adapterTarget.getListDay();

                    if (positionTarget >= 0) {

                        TrainingInPlanDTO cw=new TrainingInPlanDTO();
                        cw.setDzienCwiczenia(positionTarget);
                        cw.setDyscyplinaCwiczenia(listSource.get(positionSource).getName());

                        mListener.opedDialog(cw,customListTarget,positionTarget,true,index);
                    } else {
                        TrainingInPlanDTO cw=new TrainingInPlanDTO();
                        cw.setDzienCwiczenia(positionTarget);
                        cw.setDyscyplinaCwiczenia(listSource.get(positionSource).getName());

                        cw=mListener.opedDialog(cw,customListTarget,positionTarget,true,index);
                        Log.i("x",String.valueOf("onDrag in if loop dystans to:"+cw.getDystansCwiczenia()));

                    }

                    view.setVisibility(View.VISIBLE);

                }
                break;
        }

        if (!isDropped) {
            ((View) dragEvent.getLocalState()).setVisibility(View.VISIBLE);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        int positionTarget = -1;

        if (v.getId() == R.id.rl1 || v.getId() == R.id.rl2
                || v.getId() == R.id.rl3) {
            RecyclerView target;

            if (v.getId() == R.id.text_empty_list_top) {
                target = (RecyclerView) v.getRootView()
                        .findViewById(R.id.add_planRecyclerView);
            } else if (v.getId() == R.id.text_empty_list_bottom) {
                target = (RecyclerView) v.getRootView()
                        .findViewById(R.id.add_planRVButtons);
            } else {

                RelativeLayout rl=(RelativeLayout) v.getParent();

                target = (RecyclerView) rl.getParent();
                positionTarget = (int) v.getTag();
            }

            PlanAdapter adapterTarget = (PlanAdapter) target.getAdapter();
            ArrayList<Day> customListTarget = adapterTarget.getListDay();
            if (positionTarget >= 0) {
                TrainingInPlanDTO cw=customListTarget.get(positionTarget).getItem(index);
                mListener.opedDialog(cw,customListTarget,positionTarget,false,index);

            } else {
                TrainingInPlanDTO cw=customListTarget.get(positionTarget).getItem(index);
                mListener.opedDialog(cw,customListTarget,positionTarget,false,index);

            }
            adapterTarget.updateList(customListTarget);
            adapterTarget.notifyDataSetChanged();
            v.setVisibility(View.VISIBLE);

        }
    }
}
