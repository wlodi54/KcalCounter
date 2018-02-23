package com.example.sywlia.licznikkaloryczny.models;

import java.util.ArrayList;


public class Day {
    private ArrayList<TrainingInPlanDTO> lista;
    private int numbOfDay;
    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Day(int numbOfDay,String day){
        this.numbOfDay=numbOfDay;
        this.day=day;

        lista=new ArrayList<>();
    }


    public ArrayList<TrainingInPlanDTO> getLista(){
        if(lista!=null){
            return lista;
        }
        return null;
    }
    public TrainingInPlanDTO getItem(int index){
        return lista.get(index);
    }
    public int getSize(){
        return lista.size();
    }
    public void addCwiczenie(TrainingInPlanDTO cwiczenie){
        lista.add(cwiczenie);
    }
    public void removeCwiczenie(int index){
        lista.remove(index);
    }
}
