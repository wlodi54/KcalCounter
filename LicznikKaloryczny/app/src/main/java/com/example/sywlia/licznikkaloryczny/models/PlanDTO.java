package com.example.sywlia.licznikkaloryczny.models;

/**
 * Created by sywlia on 2017-05-16.
 */

public class PlanDTO {
    public static final String ID_PLAN="ID_PLAN";
    public static final String PLAN_NAZWA="PLAN_NAZWA";
    public static final String PLAN_RODZAJ="PLAN_RODZAJ";
    public static final String PLAN_DATA="PLAN_DATA";
    public static final String PLAN_TABLICA="PLAN";
    public static final String PLAN_CZYAKTYWNY="PLAN_CZYAKTYWNY";
    public static final String ID_USER="ID_USER";
    public static final String PLAN_ILOSC_CW="PLAN_ILOSC_CW";
    private String nazwaPlan;
    private String dataPlan;
    private int iloscCwiczen;
    private long idPlan;
    private long idUser;

    public int getIloscCwiczen() {
        return iloscCwiczen;
    }

    public void setIloscCwiczen(int iloscCwiczen) {
        this.iloscCwiczen = iloscCwiczen;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public int getCzyAktywny() {
        return czyAktywny;
    }

    public void setCzyAktywny(int czyAktywny) {
        this.czyAktywny = czyAktywny;
    }

    private int czyAktywny;

    public long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(long idPlan) {
        this.idPlan = idPlan;
    }

    public String getNazwaPlan() {
        return nazwaPlan;
    }

    public void setNazwaPlan(String nazwaPlan) {
        this.nazwaPlan = nazwaPlan;
    }


    public String getDataPlan() {
        return dataPlan;
    }

    public void setDataPlan(String dataPlan) {
        this.dataPlan = dataPlan;
    }
}
