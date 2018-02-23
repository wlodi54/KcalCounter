package com.example.sywlia.licznikkaloryczny.interfaces;

import android.support.v4.util.LongSparseArray;

import com.example.sywlia.licznikkaloryczny.models.TrainingDTO;

import java.util.ArrayList;

/**
 * Created by sywlia on 2017-05-16.
 */

public interface ITrainingDAO {
    long insertMapJson(TrainingDTO history) throws Exception;

    TrainingDTO getHistory(long id_history);

    ArrayList<TrainingDTO> getHistoryList(long id_user);


    LongSparseArray<ArrayList<Double>> getBestHistory(Long kcalValue, long idDiscipline, long idUser);

}
