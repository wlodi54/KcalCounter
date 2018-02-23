package com.example.sywlia.licznikkaloryczny.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.data.DisciplineDAO;
import com.example.sywlia.licznikkaloryczny.data.TrainingDAO;
import com.example.sywlia.licznikkaloryczny.data.UserDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.helpers.UnitConversion;
import com.example.sywlia.licznikkaloryczny.interfaces.ITrainingDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.IUserDAO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.SimulatedTraining;
import com.example.sywlia.licznikkaloryczny.models.TrainingDTO;
import com.example.sywlia.licznikkaloryczny.models.UserDTO;
import com.example.sywlia.licznikkaloryczny.services.CounterThread;
import com.example.sywlia.licznikkaloryczny.services.LocationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sywlia on 2017-05-16.
 */


public class TrainingActivity extends AppCompatActivity {

    String disciplineName;
    private ArrayList<SimulatedTraining> listOfBestHistory;
    private int kcalSelectedSpinner;
    private boolean isChallangeMode;
    private static final int updateUIBtnStart = 1;
    private static final int updateUIBtnPause = 2;
    private static final int updateUIBtnCreate = 3;
    private static final int updateUIBtnDisable = 4;
    private Button startBtn, stopBtn, pauseBtn;
    private TextView timerTextView, kcalCounterTextView, speedTextView, gpsStatusTextView, disciplineTextView, distanceToTextView, tepoTextView, kcalMinTextView, avrSpeedTextView;
    private ImageView disciplineImageView;
    private TextView editText;
    private double latitude;
    private double longitude;
    protected ArrayList<LatLng> pointsMarker = new ArrayList<>();
    public Messenger messengerToService = null;
    final Messenger messangerFromService = new Messenger(new IncomingHandler());
    private Context context = this;
    private boolean isBind = false;
    private long idUser;
    private long idDiscipline;
    private int imgSrc;
    private CounterThread kcalCounter;
    private UserDTO userDTO;
    private DisciplineDTO disciplineDTO;
    private double currentKcal;
    private long currentTime;
    private double distance;
    private double avrSpeed;
    private double tepo;
    private ArrayList<Float> avrKcalMinArray=new ArrayList<>();
    private java.text.DecimalFormat dtime;

    class kcalCounterRecieve extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constans.kcalMsgWhat:{
                    currentKcal += Double.parseDouble(msg.obj.toString());
                    kcalCounterTextView.setText(String.format(Locale.ROOT,"%.2f",currentKcal));
                    break;
                }
                case Constans.timerMsgWhat:{
                    currentTime =Long.parseLong(msg.obj.toString());

                    int sec = (int) (currentTime / 1000);
                    int mins = sec / 60;
                    sec = sec % 60;
                    int hours = mins / 60;
                    timerTextView.setText(String.format(Locale.ROOT, "%02d:%02d:%02d", hours, mins, sec));
                    break;

                }
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        listOfBestHistory = new ArrayList<>();
        editText = (TextView) findViewById(R.id.editText);
        startBtn = (Button) findViewById(R.id.buttonStart);
        stopBtn = (Button) findViewById(R.id.buttonStop);
        pauseBtn = (Button) findViewById(R.id.buttonPause);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        kcalCounterTextView = (TextView) findViewById(R.id.kcalCounerTtextView);
        speedTextView = (TextView) findViewById(R.id.speedTextView);
        gpsStatusTextView = (TextView) findViewById(R.id.training_gps_statusTextView);
        disciplineTextView = (TextView) findViewById(R.id.training_disciplineName);
        disciplineImageView = (ImageView) findViewById(R.id.training_disciplineImg);
        distanceToTextView=(TextView) findViewById(R.id.training_tepo_label);
        tepoTextView=(TextView) findViewById(R.id.training_tepo);
        kcalMinTextView=(TextView) findViewById(R.id.training_kcalmin);
        avrSpeedTextView=(TextView) findViewById(R.id.training_avr_speed);
        getIntentExtras();
        kcalCounter=new CounterThread(this.disciplineDTO.getMet(),new kcalCounterRecieve(),this.userDTO.getWeight());
        updateUIBtn(updateUIBtnDisable);
        disciplineTextView.setText(this.disciplineName);
        disciplineImageView.setImageResource(this.imgSrc);
        setDecimalFormat();
    }

    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messengerToService = new Messenger(service);
            isBind = true;
            Message messageToService = Message.obtain(null, 20);
            messageToService.replyTo = messangerFromService;
            try {
                messengerToService.send(messageToService);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            messengerToService = null;
            Log.i(Constans.LOG_APP,"onSerciceDiscineted");

        }
    };

    private void updateUIBtn(int value) {
        int colorTextEnable = ContextCompat.getColor(context, R.color.fontLight);
        int colorTextDisable = ContextCompat.getColor(context, R.color.buttonColor);
        switch (value) {
            case updateUIBtnCreate: {
                stopBtn.setEnabled(false);
                pauseBtn.setEnabled(false);
                startBtn.setEnabled(true);
                startBtn.setTextColor(colorTextEnable);
                pauseBtn.setTextColor(colorTextDisable);
                stopBtn.setTextColor(colorTextDisable);
                break;
            }
            case updateUIBtnStart: {
                startBtn.setEnabled(false);
                startBtn.setTextColor(colorTextDisable);
                stopBtn.setEnabled(true);
                stopBtn.setTextColor(colorTextEnable);
                pauseBtn.setEnabled(true);
                pauseBtn.setTextColor(colorTextEnable);
                break;
            }
            case updateUIBtnPause: {
                startBtn.setEnabled(true);
                stopBtn.setEnabled(true);
                pauseBtn.setEnabled(false);
                pauseBtn.setTextColor(colorTextDisable);
                stopBtn.setTextColor(colorTextEnable);
                startBtn.setTextColor(colorTextEnable);
                break;
            }
            case updateUIBtnDisable: {
                startBtn.setEnabled(false);
                stopBtn.setEnabled(false);
                pauseBtn.setEnabled(false);
                pauseBtn.setTextColor(colorTextDisable);
                stopBtn.setTextColor(colorTextDisable);
                startBtn.setTextColor(colorTextDisable);
                break;
            }
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private void getIntentExtras() {
        Intent intent = new Intent(this, LocationService.class);
        Intent intentGetExtras = getIntent();
        Bundle bundle = intentGetExtras.getExtras();
        if(bundle.getParcelableArrayList("ListOfBestHistory")==null){
            isChallangeMode=false;

        }else {
            listOfBestHistory = bundle.getParcelableArrayList("ListOfBestHistory");
            isChallangeMode=true;
        }
        this.idDiscipline = bundle.getLong("SelectedDiscipline");
        this.idUser = bundle.getLong("UserLogged");
        this.kcalSelectedSpinner = bundle.getInt("KcalSelected");
        DisciplineDAO disciplineDAO = new DisciplineDAO(context);
        disciplineDTO = disciplineDAO.getDiscipline(this.idDiscipline);
        this.disciplineName = disciplineDTO.getName();
        this.imgSrc = disciplineDTO.getImgSrcBig();
        IUserDAO userDAO = new UserDAO(context);
        userDTO = userDAO.getUser(this.idUser);
        intent.putExtra("SelectedDisciplineToService", disciplineDTO);
        intent.putExtra("LoggedUser", this.idUser);
        intent.putExtra("UserWeight", userDTO.getWeight());

        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void addMarker(LatLng latLng) {
        pointsMarker.add(latLng);

    }

    public void startClick(View view) {

        updateUIBtn(updateUIBtnStart);
        kcalCounter.startCounting(true);
        Message messageToService = Message.obtain(null, Constans.recieveMessageWhatFromActivitieStart);
        messageToService.replyTo = messangerFromService;
        try {
            messengerToService.send(messageToService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pauseClick(View view) {

        updateUIBtn(updateUIBtnPause);
        kcalCounter.pauseCounting();
        Message messageToService = Message.obtain(null, Constans.recieveMessageWhatFromActivitiePause);
        messageToService.replyTo = messangerFromService;
        try {
            messengerToService.send(messageToService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void stopClick(View view) {


        kcalCounter.stopCounting();
        if (isBind) {
            Message messageToService = Message.obtain(null, Constans.recieveMessageWhatFromActivitieStop);
            messageToService.replyTo = messangerFromService;
            try {
                messengerToService.send(messageToService);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mConn);
            stopService(new Intent(this, LocationService.class));
            isBind = false;
            Intent intent = new Intent(getBaseContext(), HistoryDetailsActivity.class);
            long id = insertTrainingToDB();
            intent.putExtra("history_id", id);
            startActivity(intent);
            finish();
        }
    }

    private long insertTrainingToDB() {

        TrainingDTO history = new TrainingDTO();
        long id_history = 0;
        ITrainingDAO historyDAO = new TrainingDAO(context);
        history.setDisciplineId(idDiscipline);
        history.setKcal(currentKcal);
        history.setTime(currentTime);
        history.setUserId(idUser);
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_YEAR);
        history.setDate(day);
        if (distance != 0) {
            history.setAvrKcalMeter(currentKcal / distance);
        } else {
            history.setAvrKcalMeter(0);
        }
        float suma=0;
        for (int i=0;i<avrKcalMinArray.size();i++){
            suma+=avrKcalMinArray.get(i);
        }
        history.setAvrKcalMin(suma/avrKcalMinArray.size()+1);
        history.setDistance(Double.parseDouble(dtime.format(this.distance)));
        if (pointsMarker.size() > 2) {
            Gson gson = new Gson();
            String inputString = gson.toJson(pointsMarker);
            history.setMapJson(inputString);
        } else {
            history.setMapJson(null);
        }
        int odznaka=-1;
        if(isChallangeMode) {
            if (this.currentKcal >= this.kcalSelectedSpinner) {
                for (int i = 0; i < listOfBestHistory.size(); i++) {
                    if (this.distance >= listOfBestHistory.get(i).getKcalMeter() && this.currentTime <= listOfBestHistory.get(i).getKcalMin()) {
                        history.setOdznaka(i + 1);
                        odznaka = i + 1;
                        break;
                    } else {
                        history.setOdznaka(4);
                        odznaka = 4;
                    }
                }
            } else {
                history.setOdznaka(0);
                odznaka = 0;
            }
        }
        IUserDAO userDAO = new UserDAO(context);
        if (odznaka != -1) {
            userDAO.increaseMedals(this.idUser, odznaka);
        }
        try {
            history.setOdznaka(odznaka);
            id_history = historyDAO.insertMapJson(history);
            history.setHistoryId(id_history);
        } catch (Exception e) {
            id_history = 0;
            e.printStackTrace();
        }
        return id_history;
    }


    private void setDecimalFormat() {
        DecimalFormatSymbols symbols;

        symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        dtime = new java.text.DecimalFormat("#####.##", symbols);
    }

    class IncomingHandler extends Handler {
        UnitConversion conversion=new UnitConversion();
        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if (b != null) {
                switch (msg.what) {
                    case Constans.locationToActivityWhat: {
                        float kcalMin;
                        distance=b.getDouble("Distance");
                        latitude = b.getDouble("Latitude");
                        longitude = b.getDouble("Longitude");
                        editText.setText(String.format(Locale.ROOT, "%.2f", conversion.conversionMtoKm(distance)));
                        Double speed = b.getDouble("Speed");
                        speedTextView.setText(String.format(Locale.ROOT, "%.2f", speed));
                        avrSpeed=conversion.conversionMtoKm(distance)/conversion.longToHours(currentTime);
                        avrSpeedTextView.setText(String.format(Locale.ROOT,"%.2f",avrSpeed));
                        if(distance!=0) {
                            tepo = conversion.longToMinutes(currentTime) / conversion.conversionMtoKm(distance);
                            int minuta=(int)tepo;
                            double sekunda=tepo%1*60;
                            tepoTextView.setText(String.format(Locale.ROOT, "%.2f", tepo));
                            tepoTextView.setText(String.valueOf(minuta)+":"+(int)sekunda);
                        }else
                            tepoTextView.setText("0");
                        addMarker(new LatLng(latitude, longitude));
                        kcalMin=(float) (currentKcal/conversion.longToMinutes(currentTime));
                        avrKcalMinArray.add(kcalMin);

                        kcalMinTextView.setText(String.format(Locale.ROOT, "%.2f", kcalMin));
                        break;
                    }
                    case Constans.kcalToActivityWhat: {
                        currentKcal += Double.parseDouble(b.getString("KcalFromService"));
                        kcalCounterTextView.setText(String.valueOf(currentKcal));
                        break;
                    }
                    case Constans.timerToActivityWhat: {
                        currentTime = Long.valueOf(b.getString("TimerFromService"));
                        timerTextView.setText(String.valueOf(currentTime));
                        int sec = (int) (currentTime / 1000);
                        int mins = sec / 60;
                        sec = sec % 60;
                        int hours = mins / 60;
                        timerTextView.setText(String.format(Locale.ROOT, "%02d:%02d:%02d", hours, mins, sec));
                        break;
                    }

                    case 10: {
                        Toast.makeText(getBaseContext(), "Gps is ok!!!", Toast.LENGTH_SHORT).show();
                        gpsStatusTextView.setText(R.string.gps_status_ok);
                        updateUIBtn(updateUIBtnCreate);
                        break;
                    }
                    case 11:{
                        kcalCounter.setMet(b.getDouble("Met_updated"));
                        break;
                    }
                    default: {
                        super.handleMessage(msg);
                        break;
                    }
                }
            }
        }

    }
}