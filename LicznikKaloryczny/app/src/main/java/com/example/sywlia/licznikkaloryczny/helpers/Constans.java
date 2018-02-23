package com.example.sywlia.licznikkaloryczny.helpers;

/**
 * Created by sywlia on 2017-05-16.
 */

public class Constans {
    public static final String LOG_APP="Tag";
    public static final String LOG_HISTORYADAPTER="Tag2";
    public static final double MIN_VAL_ONE_SEC = 0.0166667;
    public static final int timerMsgWhat = 2;
    public static final int kcalMsgWhat = 1;

    public static final long UPDATE_INTERVAL_IN_MILISECONDS =1000;
    public static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL_IN_MILISECONDS / 2;
    public static final int ACCURACY_THRESHOLD = 10;

    public static final int recieveMessageWhatFromActivitieStart=5;
    public static final int recieveMessageWhatFromActivitiePause=6;
    public static final int recieveMessageWhatFromActivitieStop=3;
    public static final int locationToActivityWhat=7;

    public static final int kcalToActivityWhat=8;
    public static final int timerToActivityWhat=9;
    public static final int dayCount = 7;

}
