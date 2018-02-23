package com.example.sywlia.licznikkaloryczny.models;


public class Statistic {
    private int kcal;
    private long time;
    private String discipline;
    private double distance;
    private int goldMedals;
    private int silverMedals;

    public Statistic(int kcal, long time, String discipline, double distance, int goldMedals, int silverMedals, int brownMedals) {
        this.kcal = kcal;
        this.time = time;
        this.discipline = discipline;
        this.distance = distance;
        this.goldMedals = goldMedals;
        this.silverMedals = silverMedals;
        this.brownMedals = brownMedals;

    }

    int brownMedals;


    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getGoldMedals() {
        return goldMedals;
    }

    public void setGoldMedals(int goldMedals) {
        this.goldMedals = goldMedals;
    }

    public int getSilverMedals() {
        return silverMedals;
    }

    public void setSilverMedals(int silverMedals) {
        this.silverMedals = silverMedals;
    }

    public int getBrownMedals() {
        return brownMedals;
    }

    public void setBrownMedals(int brownMedals) {
        this.brownMedals = brownMedals;
    }


}
