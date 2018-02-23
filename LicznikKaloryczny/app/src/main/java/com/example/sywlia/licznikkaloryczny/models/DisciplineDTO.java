package com.example.sywlia.licznikkaloryczny.models;


import java.io.Serializable;

public class DisciplineDTO implements Serializable {
    public static final String DISCIPLINE_IMGSRCBIG="DISCIPLINE_IMGSRCBIG";
    public static final String DISCIPLINE_IMGSRC="DISCIPLINE_IMGSRC";
    public static final String DISCIPLINE_NAME="DISCIPLINE_NAME";
    public static final String DISCIPLINE_ID="DISCIPLINE_ID";
    public static final String DISCIPLINE_TABLE="DISCIPLINE";
    public static final String DISCIPLINE_MET="DISCIPLINE_MET";
    private long id;
    private String name;
    private int imgSrcBig;
    private double met;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getImgSrcBig() {
        return imgSrcBig;
    }

    public void setImgSrcBig(int imgSrcBig) {
        this.imgSrcBig = imgSrcBig;
    }

    public int getImgSrc() {
        return imgSrc;
    }


    public void setImgSrc(int imgSrc) {
        this.imgSrc = imgSrc;
    }

    private int imgSrc;


    @Override
    public String toString() {
        return getName();
    }


    public double getMet() {
        return met;
    }

    public void setMet(double met) {
        this.met = met;
    }





}
