package com.example.sywlia.licznikkaloryczny.models;


public class TrainingInPlanDTO {
    public static String ID_PLAN="ID_PLAN";
    public static String CWICZENIE_DZIEN="CWICZENIE_DZIEN";
    public static String CWICZENIE_DYSTANS="PLAN_DYSTANS";
    public static String CWICZENIE_CZAS="CWICZENIE_CZAS";
    public static String CWICZENIE_TABELA="CWICZENIE_TABELA";
    public static String CWICZENIE_ID_DYSCYPLINY="CWICZENIE_ID_DYSCYPLINY";
    public static String CWICZENIE_NAZWA_DYSCYPLINY="CWICZENIE_NAZWA_DYSCYPLINY";

    private long idPlanu;
    private int dzienCwiczenia;
    private String dyscyplinaCwiczenia;
    private double dystansCwiczenia;
    private long idDyscypliny;
    private long czasCwiczenia;
    private boolean isDone;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public TrainingInPlanDTO(){

    }
    public TrainingInPlanDTO(int dzienCwiczenia, String dyscyplinaCwiczenia, float dystansCwiczenia, long idDyscypliny, long czasCwiczenia) {
        this.dzienCwiczenia = dzienCwiczenia;
        this.dyscyplinaCwiczenia = dyscyplinaCwiczenia;
        this.dystansCwiczenia = dystansCwiczenia;
        this.idDyscypliny = idDyscypliny;
        this.czasCwiczenia = czasCwiczenia;
    }

    public String getDyscyplinaCwiczenia() {
        return dyscyplinaCwiczenia;
    }

    public void setDyscyplinaCwiczenia(String dyscyplinaCwiczenia) {
        this.dyscyplinaCwiczenia = dyscyplinaCwiczenia;
    }

    public long getIdDyscypliny() {
        return idDyscypliny;
    }

    public void setIdDyscypliny(long idDyscypliny) {
        this.idDyscypliny = idDyscypliny;
    }

    public long getIdPlanu() {
        return idPlanu;
    }

    public void setIdPlanu(long idPlanu) {
        this.idPlanu = idPlanu;
    }

    public int getDzienCwiczenia() {
        return dzienCwiczenia;
    }

    public void setDzienCwiczenia(int dzienCwiczenia) {
        this.dzienCwiczenia = dzienCwiczenia;
    }

    public double getDystansCwiczenia() {
        return dystansCwiczenia;
    }

    public void setDystansCwiczenia(double dystansCwiczenia) {
        this.dystansCwiczenia = dystansCwiczenia;
    }

    public long getCzasCwiczenia() {
        return czasCwiczenia;
    }

    public void setCzasCwiczenia(long czasCwiczenia) {
        this.czasCwiczenia = czasCwiczenia;
    }


}