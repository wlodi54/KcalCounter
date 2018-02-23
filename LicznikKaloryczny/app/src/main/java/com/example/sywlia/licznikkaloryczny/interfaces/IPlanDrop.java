package com.example.sywlia.licznikkaloryczny.interfaces;

import com.example.sywlia.licznikkaloryczny.models.Day;
import com.example.sywlia.licznikkaloryczny.models.TrainingInPlanDTO;

import java.util.ArrayList;

/**
 * Created by sywlia on 2017-05-16.
 */

public interface IPlanDrop {

    TrainingInPlanDTO opedDialog(TrainingInPlanDTO cwRecieved, ArrayList<Day> list, int day, boolean isNewCw, int index);

}
