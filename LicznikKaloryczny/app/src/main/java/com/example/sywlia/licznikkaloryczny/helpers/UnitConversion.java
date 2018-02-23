package com.example.sywlia.licznikkaloryczny.helpers;

import java.util.Locale;

/**
 * Created by sywlia on 2017-05-16.
 */

public class UnitConversion {
    public double longToHours(long currentTime){
        if(currentTime!=0){
            return ((int) (currentTime / 1000)) * 0.000277778;
        }else return -1;

    }
    public float longToMinutes(long currentTime){
        if(currentTime!=0) {
            return (float) ((currentTime*0.001)*0.0166667);
        }else return -1;
    }
    public long conversionMinutesTOMilis(String minutes){
        long minutesLong=Long.parseLong(minutes);
        return (minutesLong*60)*1000;
    }
    public double conversionKmToMeters(String km){
        double kmDouble=Double.parseDouble(km);
        return kmDouble*1000;

    }
    public double conversionMtoKm(double meters){
        if(meters!=0) {
            return meters / 1000;
        }else
            return 0;
    }
    public String conversionMillisToMinutes(long millis){
        int minutes=(int)(millis/1000)/60;
        String h;
        if(minutes>=60){
            if(minutes%60==0){
                h=String.format(Locale.ROOT, "%.0f h.", (double)minutes/60);
            }else{
                h=String.format(Locale.ROOT, "%.1f h.", (double)minutes/60);
            }
            return h;
        }
        return String.valueOf(minutes+" min.");

    }
    public String coversionMetersToKm(double meters){
        if(meters%1000==0){
            return String.format(Locale.ROOT, "%.0f km.", meters/1000);
        }
        return String.format(Locale.ROOT, "%.2f km.", meters/1000);


    }
    public String conversionMeters(double meters){
        String metersConverted;
        if(meters>=1000){
            metersConverted=String.format(Locale.ROOT,"%.2f_km",meters/1000);

        }else if(meters!=0){
            metersConverted=String.format(Locale.ROOT,"%d_m",(int)meters);
        }else{
            metersConverted="0.0 m";
        }
        return metersConverted;
    }
    public String conversionTime(long time){
        int sec=(int) (time/1000);
        int mins=sec/60;
        sec=sec%60;

        int hours=mins/60;
        mins=mins%60;
        String timeString="blad";
        if (time != 0) {
            if (hours > 0 && mins >= 10 && sec >= 10) {
                timeString= String.format(Locale.ROOT, "%1d:%2d:%2d_h", hours, mins, sec);
            } else if (hours > 0 && mins < 10 && sec >= 10) {
                timeString = String.format(Locale.ROOT, "%1d:0%1d:%2d_h", hours, mins, sec);
            } else if (mins >= 1 && sec >= 10) {
                timeString = String.format(Locale.ROOT, "%2d:%2d_min", mins, sec);
            } else if (mins > 0 && sec < 10) {
                timeString = String.format(Locale.ROOT, "%2d:0%1d_min", mins, sec);
            } else if (mins == 0 && sec >= 10) {
                timeString = String.format(Locale.ROOT, "%2d_sec", sec);
            } else if (mins == 0 && sec < 10) {
                timeString = String.format(Locale.ROOT, "%1d_sec", sec);
            }
        } else {
            timeString = "brak";
        }
        return timeString;
    }
}
