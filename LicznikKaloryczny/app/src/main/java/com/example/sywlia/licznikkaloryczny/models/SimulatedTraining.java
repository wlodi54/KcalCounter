package com.example.sywlia.licznikkaloryczny.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.sywlia.licznikkaloryczny.helpers.Constans;

import java.util.Locale;

public class SimulatedTraining implements Parcelable {
    private double kcalMin;
    private double kcalMeter;
    private long idDiscipline;
    private String kcalMinInString;
    private String kcalMeterInString;

    protected SimulatedTraining(Parcel in) {
        kcalMin = in.readDouble();
        kcalMeter = in.readDouble();
        idDiscipline = in.readLong();
        kcalMinInString = in.readString();
        kcalMeterInString = in.readString();
    }

    public static final Creator<SimulatedTraining> CREATOR = new Creator<SimulatedTraining>() {
        @Override
        public SimulatedTraining createFromParcel(Parcel in) {
            return new SimulatedTraining(in);
        }

        @Override
        public SimulatedTraining[] newArray(int size) {
            return new SimulatedTraining[size];
        }
    };

    public String getKcalMinInString() {
        return kcalMinInString;
    }


    public double getKcalMin() {
        return kcalMin;
    }



    public double getKcalMeter() {
        return kcalMeter;
    }



    public String getKcalMeterInString() {
        return kcalMeterInString;
    }


    public void setIdDiscipline(long idDiscipline) {
        this.idDiscipline = idDiscipline;
    }


    public SimulatedTraining(long idDiscipline, double kcalMeter, double kcalMin) {
        this.idDiscipline = idDiscipline;
        this.kcalMeter = kcalMeter;
        this.kcalMin=kcalMin;
        convertToTimeFormat(kcalMin);
        convertToMeterFormat(kcalMeter);

    }

    private void convertToMeterFormat(double kcalMeter) {
        if(kcalMeter>=1000){
            this.kcalMeterInString=String.format(Locale.ROOT,"%.2f km",kcalMeter/1000);

        }else{
            this.kcalMeterInString=String.valueOf((int)kcalMeter+" m");
        }

    }

    private void convertToTimeFormat(double kcalMin) {
        int hours = (int) kcalMin / 60;
        int minutes = (int) kcalMin;
        double seconds = ((kcalMin - minutes) * 60);
        int sec = (int) seconds;
        if (kcalMin != 0) {
            if (hours > 0 && minutes >= 10 && sec >= 10) {
                this.kcalMinInString = String.format(Locale.ROOT, "%1d:%2d:%2d h", hours, minutes, sec);
            } else if (hours > 0 && minutes < 10 && sec >= 10) {
                this.kcalMinInString = String.format(Locale.ROOT, "%1d:0%1d:%2d h", hours, minutes, sec);
            } else if (minutes >= 1 && sec >= 10) {
                this.kcalMinInString = String.format(Locale.ROOT, "%2d:%2d min", minutes, sec);
            } else if (minutes > 0 && sec < 10) {
                this.kcalMinInString = String.format(Locale.ROOT, "%2d:0%1d min", minutes, sec);
            } else if (minutes == 0 && sec >= 10) {
                this.kcalMinInString = String.format(Locale.ROOT, "%2d sec", sec);
            } else if (minutes == 0 && sec < 10) {
                this.kcalMinInString = String.format(Locale.ROOT, "%1d sec", sec);
            }
        } else {
            this.kcalMinInString = "brak";
        }

    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(kcalMin);
        dest.writeDouble(kcalMeter);
        dest.writeLong(idDiscipline);
        dest.writeString(kcalMinInString);
        dest.writeString(kcalMeterInString);
    }
}

