package com.example.sywlia.licznikkaloryczny.interfaces;

import com.example.sywlia.licznikkaloryczny.models.SimulatedTraining;

import java.util.ArrayList;

/**
 * Created by sywlia on 2017-05-16.
 */

public interface IAsyncResponse {
    void getBestHistoryList(ArrayList<SimulatedTraining> output);
}