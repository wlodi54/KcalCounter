package com.example.sywlia.licznikkaloryczny.models;

import java.io.Serializable;

/**
 * Created by sywlia on 2017-05-16.
 */


public class UserDTO implements Serializable {
    public static final String USER_ID="USER_ID";
    public static final String USER_NAME="USER_NAME";
    public static final String USER_WEIGHT="USER_WEIGHT";
    public static final String USER_HEIGHT="USER_HEIGHT";
    public static final String USER_TABLE="USER";
    public static final String USER_IS_LOGGED="USER_IS_LOGGED";
    public static final String USER_POZIOM="USER_POZIOM";
    public static final String USER_MEDALS_GOLD="USER_MEDALS_GOLD";
    public static final String USER_MEDALS_SILVER="USER_MEDALS_SILVER";
    public static final String USER_MEDALS_BROWN="USER_MEDALS_BROWN";
    public static final String USER_PKT_ROZWOJU="USER_PUNKTY_ROZWOJU";

    private long id;
    private String name;
    private Double weight;
    private Double height;
    private int medalsGold;
    private int medalsSliver;
    private int medalsBrown;
    private int isLogged;
    private int poziom;
    private int punktyRozowuju;

    public int getPoziom() {
        return poziom;
    }

    public void setPoziom(int poziom) {
        this.poziom = poziom;
    }



    public int getPunktyRozowuju() {
        return punktyRozowuju;
    }

    public void setPunktyRozowuju(int punktyRozowuju) {
        this.punktyRozowuju = punktyRozowuju;
    }

    public int getMedalsBrown() {
        return medalsBrown;
    }

    public void setMedalsBrown(int medalsBrown) {
        this.medalsBrown = medalsBrown;
    }

    public int getMedalsGold() {
        return medalsGold;
    }

    public void setMedalsGold(int medalsGold) {
        this.medalsGold = medalsGold;
    }

    public int getMedalsSliver() {
        return medalsSliver;
    }

    public void setMedalsSliver(int medalsSliver) {
        this.medalsSliver = medalsSliver;
    }



    public int isLogged() {
        return isLogged;
    }

    public void setLogged(int logged) {
        isLogged = logged;
    }

    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight)
    {
        this.weight=weight;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

}
