package com.example.sywlia.licznikkaloryczny.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.data.DisciplineDAO;
import com.example.sywlia.licznikkaloryczny.helpers.Constans;
import com.example.sywlia.licznikkaloryczny.interfaces.IDisciplineDAO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineSubDTO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import static com.example.sywlia.licznikkaloryczny.helpers.Constans.FASTEST_UPDATE_INTERVAL;
import static com.example.sywlia.licznikkaloryczny.helpers.Constans.UPDATE_INTERVAL_IN_MILISECONDS;
import static com.example.sywlia.licznikkaloryczny.helpers.Constans.recieveMessageWhatFromActivitiePause;
import static com.example.sywlia.licznikkaloryczny.helpers.Constans.recieveMessageWhatFromActivitieStart;
import static com.example.sywlia.licznikkaloryczny.helpers.Constans.recieveMessageWhatFromActivitieStop;


public class LocationService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    public static final int STABILSIGNALWHAT = 10;
    public String TAG="location_updates";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    private boolean isConnected=false;
    private boolean isFirstStart=true;
    private double distance=0;
    private double distanceDistanceTo=0;
    private double speed=0;
    protected CounterThread kcalCounter;
    final Messenger mMessengerFromActivite=new Messenger(new RecieveHandler());
    Messenger messengerToActivite=null;
    boolean isStepCountStarted=false;
    private DisciplineDTO disciplineDTO;
    private ArrayList<DisciplineSubDTO> listOfSub;
    boolean isFirstLocation=false;
    private boolean isGoodGpsSignal=false;



    @Override
    public IBinder onBind(Intent intent) {

        Bundle bundle=intent.getExtras();
        this.disciplineDTO= (DisciplineDTO) bundle.getSerializable("SelectedDisciplineToService");
        assert disciplineDTO != null;
        setMetArray();
        return mMessengerFromActivite.getBinder();
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreateService");
        super.onCreate();
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();
    }

    private void setMetArray(){
        IDisciplineDAO disciplineDAO=new DisciplineDAO(getApplication());
        ArrayList<DisciplineSubDTO> list = disciplineDAO.getArraySubDiscipline(this.disciplineDTO.getId());
        if(list!=null){
            listOfSub=list;
        }else
        {
            Log.i(Constans.LOG_APP,"list is null");
        }

    }
    class RecieveHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            messengerToActivite=msg.replyTo;
            if(msg.what==recieveMessageWhatFromActivitiePause)
            {
                stopLocationUpdate();
                isConnected=false;
                isFirstStart=false;
            } else if(msg.what==recieveMessageWhatFromActivitieStart){

                isConnected=true;
            }else if(msg.what==recieveMessageWhatFromActivitieStop){
                LocationService.super.onDestroy();
            }
        }
    }

    private void sendToActivity() {
        Bundle b=new Bundle();
        b.putDouble("Latitude",mCurrentLocation.getLatitude());
        b.putDouble("Longitude",mCurrentLocation.getLongitude());
        b.putDouble("Distance",distance);
        b.putDouble("DistanceTo",distanceDistanceTo);
        b.putDouble("Speed",speed);
        Message msgLocationToActivity=Message.obtain(null,Constans.locationToActivityWhat);
        msgLocationToActivity.setData(b);
        try {
            messengerToActivite.send(msgLocationToActivity);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
        createLocationRequest();



    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Constans.LOG_APP,"onStart comand");
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {

        if(mGoogleApiClient.isConnected()) {
            Log.i(TAG,"onDestroyLocation");
            kcalCounter.stopCounting();
            isConnected=false;
            stopLocationUpdate();
            mGoogleApiClient.disconnect();
            kcalCounter=null;
            stopSelf();
        }
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (mCurrentLocation == null) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling                Toast.makeText(this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();

                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            startLocationUpdate();
        }

    }

    protected void startLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Włącz uprawnienia lokaliacji", Toast.LENGTH_LONG).show();

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
    protected void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if(isConnected&&isGoodGpsSignal) {
            if (location.getAccuracy()<=Constans.ACCURACY_THRESHOLD&&location.hasSpeed()) {
                if (this.speed == 0) {
                    this.speed = location.getSpeed()*3.6;
                } else {
                    this.speed=((speed+(location.getSpeed()*3.6))/2);
                }
                if (isFirstLocation) {
                    updateDistance(location);
                    changeMet();
                }
                sendToActivity();
                isFirstLocation=true;
                isStepCountStarted=false;
            }else{
                if(this.speed!=0){
                    this.speed=this.speed*0.65;
                }
                sendToActivity();
                isStepCountStarted=true;

            }
        }else if(location.getAccuracy()< Constans.ACCURACY_THRESHOLD) {
            Toast.makeText(getBaseContext(), "Gps is ok!!!", Toast.LENGTH_SHORT).show();
            sendToActivityStabilSignal();
            isGoodGpsSignal = true;
        }
    }

    private void sendToActivityStabilSignal() {
        Message msgLocationToActivity=Message.obtain(null, STABILSIGNALWHAT);
        try {
            messengerToActivite.send(msgLocationToActivity);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void changeMet() {
        Bundle b=new Bundle();
        if(listOfSub.size()>0) {
            Message msgMetUpdate=Message.obtain(null,11);

            for (int i = 0; i < listOfSub.size(); i++) {
                if (speed > listOfSub.get(i).getMinSpeed() && speed <= listOfSub.get(i).getMaxSpeed()) {
                    b.putDouble("Met_updated",listOfSub.get(i).getMet());
                    break;
                }else
                {
                    b.putDouble("Met_updated",this.disciplineDTO.getMet());
                }
            }
            msgMetUpdate.setData(b);
            try {
                messengerToActivite.send(msgMetUpdate);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateDistance(Location myLocation) {
        double distance;
        if(this.distance!=0){
            distance=distanceCount(this.mCurrentLocation.getLatitude(),this.mCurrentLocation.getLongitude(),myLocation.getLatitude(),myLocation.getLongitude());
            this.distance += distance;
        }else{
            this.distance=1;
        }
        this.mCurrentLocation = myLocation;
    }

    private static double distanceCount(Double latDouble, Double logDouble, Double latCelDouble,Double logCelDouble){
        Double dlon=Math.toRadians(logCelDouble-logDouble);
        Double dlat=Math.toRadians(latCelDouble-latDouble);
        Double latCelRadius=Math.toRadians(latCelDouble);
        Double latDoubleRadius=Math.toRadians(latDouble);
        Double a=(Math.sin(dlat/2)*Math.sin(dlat/2))+Math.cos(latDoubleRadius)*Math.cos(latCelRadius)
                *(Math.sin(dlon/2)*Math.sin(dlon/2));
        Double c=2*(Math.atan2(Math.sqrt(a),Math.sqrt(1-a)));

        return (6379.1*c)*1000;
    }

}