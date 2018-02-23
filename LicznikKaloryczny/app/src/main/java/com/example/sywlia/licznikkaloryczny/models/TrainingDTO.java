package com.example.sywlia.licznikkaloryczny.models;

import java.io.Serializable;


public class TrainingDTO implements Serializable {
    private long historyId;
    private long userId;
    private long disciplineId;
    private double kcal;
    private long time;
    private String mapJson;
    private double distance;
    private String disciplineName;
    private double avrKcalMin;
    private double avrKcalMeter;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    private int date;

    public double getAvrSpeed() {
        return avrSpeed;
    }

    public void setAvrSpeed(double avrSpeed) {
        this.avrSpeed = avrSpeed;
    }

    private double avrSpeed;
    private int odznaka;

    public int getOdznaka() {
        return odznaka;
    }

    public void setOdznaka(int odznaka) {
        this.odznaka = odznaka;
    }

    public double getAvrKcalMeter() {
        return avrKcalMeter;
    }

    public void setAvrKcalMeter(double avrKcalMeter) {
        this.avrKcalMeter = avrKcalMeter;
    }

    public double getAvrKcalMin() {
        return avrKcalMin;
    }

    public void setAvrKcalMin(double avrKcalMin) {
        this.avrKcalMin = avrKcalMin;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMapJson() {
        return mapJson;
    }

    public void setMapJson(String mapJson) {
        this.mapJson = mapJson;
    }

    public static final String HISTORY_ID="HISTORY_ID";
    public static final String DISCIPLINE_ID="DISCIPLINE_ID";
    public static final String HISTORY_NAME="HISTORY_NAME";
    public static final String HISTORY_KCAL="HISTORY_KCAL";
    public static final String HISTORY_TIME="HISTORY_TIME";
    public static final String HISTORY_MAP="HISTORY_MAP";
    public static final String HISTORY_DISTANCE="HISTORY_DISTANCE";
    public static final String HISTORY_TABLE="HISTORY";
    public static final String USER_ID="USER_ID";
    public static final String HISTORY_AVRKCALMIN="HISTORY_AVRKCALMIN";
    public static final String HISTORY_AVRKCALMETER="HISTORY_AVRKCALMETER";
    public static final String HISTORY_ODZNAKA="HISTORY_ODZNAKA";
    public static final String HISTORY_AVRSPEED="HISTORY_AVRSPEED";
    public static final String HISTORY_DATE="HISTORY_DATE";


}

