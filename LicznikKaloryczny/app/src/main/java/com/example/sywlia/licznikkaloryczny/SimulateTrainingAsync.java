package com.example.sywlia.licznikkaloryczny;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.adapters.SimulatedTrainingAdapter;
import com.example.sywlia.licznikkaloryczny.data.TrainingDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.IAsyncResponse;
import com.example.sywlia.licznikkaloryczny.interfaces.ITrainingDAO;
import com.example.sywlia.licznikkaloryczny.models.SimulatedTraining;

import java.util.ArrayList;

public class SimulateTrainingAsync extends AsyncTask<Long,SimulatedTraining,String> {
    private Activity activity;
    private Context ctx;
    private SimulatedTrainingAdapter adapter;
    private ListView listView;
    private ArrayList<SimulatedTraining> listOfBestHistory;
    public IAsyncResponse asyncResponse=null;
    private LongSparseArray<ArrayList<Double>> lista;

    public SimulateTrainingAsync(Context ctx, IAsyncResponse asyncResponse) {
        this.ctx=ctx;
        this.activity=(Activity)ctx;
        this.asyncResponse=asyncResponse;
    }

    @Override
    protected String doInBackground(Long... params) {
        Long valueKcal=params[0];
        ITrainingDAO historyDAO= new TrainingDAO(ctx);

        listView=(ListView) activity.findViewById(R.id.select_discipline_listView);
        listOfBestHistory=new ArrayList<>();
        lista =new LongSparseArray<>();
        if(valueKcal==1)
        {
            adapter=new SimulatedTrainingAdapter(ctx,R.layout.row_simulated_trainig);
            lista=historyDAO.getBestHistory(params[1],params[2],params[3]);
            if(listOfBestHistory!=null){
                double suma_dystansu=0;
                double suma_czasu=0;
                double metr;
                double czas;
                for(int i=lista.size()-1;i>=0;i--) {
                    suma_czasu+=lista.get(i).get(1);
                    suma_dystansu+=lista.get(i).get(0);
                    if(i==lista.size()-1){
                        metr=params[1]/lista.get(i).get(0);
                        czas=params[1]/lista.get(i).get(1);
                        publishProgress(new SimulatedTraining(params[2],metr,czas));
                    }else if(i==0){
                        double sredniaCzasu=params[1]/(suma_czasu/lista.size());
                        double sredniaOdleglosci=params[1]/(suma_dystansu/lista.size());
                        publishProgress(new SimulatedTraining(params[2],sredniaOdleglosci,sredniaCzasu));
                        metr=params[1]/lista.get(i).get(0);
                        czas=params[1]/lista.get(i).get(1);
                        publishProgress(new SimulatedTraining(params[2],metr,czas));
                    }
                }
                return "its_OK";
            }else
                return "wrong";
        }
        return "wrong";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String aVoid) {
        if(aVoid!=null) {
            switch (aVoid) {
                case "its_OK":{

                    listView.setAdapter(adapter);
                    asyncResponse.getBestHistoryList(listOfBestHistory);
                    break;}
                case "wrong": {
                    Toast.makeText(ctx, R.string.no_data_to_simulate, Toast.LENGTH_SHORT).show();
                    listView.setVisibility(View.INVISIBLE);
                    break;
                }
                case "closeListView":
                    listView.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onProgressUpdate(SimulatedTraining... values) {
        adapter.add(values[0]);
        super.onProgressUpdate(values);
    }

    public ArrayList<SimulatedTraining> getBestHistoryList(){
        if(listOfBestHistory.isEmpty()){
            return null;
        }else {
            return listOfBestHistory;
        }
    }
}
