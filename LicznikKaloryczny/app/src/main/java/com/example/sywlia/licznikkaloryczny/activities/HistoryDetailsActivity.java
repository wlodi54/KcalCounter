package com.example.sywlia.licznikkaloryczny.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sywlia.licznikkaloryczny.R;
import com.example.sywlia.licznikkaloryczny.data.DisciplineDAO;
import com.example.sywlia.licznikkaloryczny.data.TrainingDAO;
import com.example.sywlia.licznikkaloryczny.helpers.UnitConversion;
import com.example.sywlia.licznikkaloryczny.interfaces.IDisciplineDAO;
import com.example.sywlia.licznikkaloryczny.interfaces.ITrainingDAO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.TrainingDTO;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class HistoryDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Context context = this;
    GoogleMap mGoogleMap;
    private PolylineOptions polylineOptions;
    private ArrayList<LatLng> listOfPoints = new ArrayList<>();
    FrameLayout frameLayout;
    int imgSrc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getActionBar()!=null)getActionBar().setTitle("Trening");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        Intent intent = getIntent();
        frameLayout=(FrameLayout) findViewById(R.id.frameLayoutHistoryDetails);
        long id = intent.getExtras().getLong("history_id");
        TrainingDTO historyDto = getHistoryDTO(id);
        if (historyDto != null) {
            DisciplineDTO disciplineDTO=getDisciplineName(historyDto.getDisciplineId());
            historyDto.setDisciplineName(disciplineDTO.getName());
            imgSrc=disciplineDTO.getImgSrcBig();
            fillTextViews(historyDto);
        } else {
            Toast.makeText(context, R.string.empty_current_history, Toast.LENGTH_SHORT).show();
        }
        if(historyDto.getMapJson()!=null) {
            getCoordinates(historyDto.getMapJson());
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
            mapFragment.getMapAsync(this);

        }else{
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void fillTextViews(TrainingDTO historyDto) {
        UnitConversion conversion=new UnitConversion();
        Long time=historyDto.getTime();
        TextView distanceTextView = (TextView) findViewById(R.id.history_details_disctanceVal);
        TextView timeTextView = (TextView) findViewById(R.id.history_details_timeVal);
        TextView kcalTextView = (TextView) findViewById(R.id.history_details_kcalVal);
        TextView avrageSpeedTextView = (TextView) findViewById(R.id.history_details_avrSpeedVal);
        TextView disciplineTextView = (TextView) findViewById(R.id.history_details_disciplineTextView);
        TextView avrKcalMinutesTextView=(TextView) findViewById(R.id.history_details_kcalMin);
        TextView avrKcalMetersTextView= (TextView) findViewById(R.id.history_details_kcal_meter);
        ImageView odznakaImageView= (ImageView) findViewById(R.id.history_details_medalImg);
        TextView odznakaTextViewLabel= (TextView) findViewById(R.id.history_details_odznakaLabel);
        ImageView disciplineImageView=(ImageView) findViewById(R.id.history_details_disciplineImg);

        TextView timeUnit=(TextView) findViewById(R.id.history_details_secUnit);
        TextView distanceUnit=(TextView) findViewById(R.id.history_details_kmUnit);
        /***************************Set the TextViews****************************************/
        String timerString=conversion.conversionTime(time);
        String[] split=conversion.conversionMeters(historyDto.getDistance()).split("_");
        distanceTextView.setText(split[0]);
        distanceUnit.setText(split[1]);
        split=timerString.split("_");
        timeTextView.setText(split[0]);
        timeUnit.setText(split[1]);
        double avrSpeed=(historyDto.getDistance()/1000)/conversion.longToHours(historyDto.getTime());

        String kcalString=String.format(Locale.ROOT,"%.2f",historyDto.getKcal());
        kcalTextView.setText(kcalString);
        avrageSpeedTextView.setText(String.format(Locale.ROOT,"%.2f",avrSpeed));
        disciplineTextView.setText(historyDto.getDisciplineName());
        disciplineImageView.setImageResource(imgSrc);
        avrKcalMinutesTextView.setText(String.format(Locale.ROOT,"%.2f",historyDto.getAvrKcalMin()));
        avrKcalMetersTextView.setText(String.format(Locale.ROOT,"%.2f",historyDto.getAvrKcalMeter()));
        switch (historyDto.getOdznaka())
        {
            case 1:
                odznakaImageView.setImageResource(R.drawable.gold_medal);
                break;
            case 2:
                odznakaImageView.setImageResource(R.drawable.silver_medal);
                break;
            case 3:
                odznakaImageView.setImageResource(R.drawable.brown_medal);
                break;
            default:
            {
                odznakaImageView.setVisibility(View.INVISIBLE);
                odznakaTextViewLabel.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    private void getCoordinates(String mapJson) {
        JSONArray jsonArray;
        polylineOptions = new PolylineOptions();
        polylineOptions.width(4).color(Color.BLUE);
        try {
            jsonArray = new JSONArray(mapJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String latitude = jsonObject.getString("latitude");
                String longitude = jsonObject.getString("longitude");
                LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                listOfPoints.add(latLng);
                polylineOptions.add(latLng);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private TrainingDTO getHistoryDTO(long id) {
       ITrainingDAO historyDAO = new TrainingDAO(context);
        return historyDAO.getHistory(id);
    }

    private DisciplineDTO getDisciplineName(long id) {
        IDisciplineDAO disciplineDAO= new DisciplineDAO(context);

        return disciplineDAO.getDiscipline(id);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        LatLng latLngStart= listOfPoints.get(0);
        LatLng latLngStop=listOfPoints.get(listOfPoints.size()-1);
        mGoogleMap.addMarker(new MarkerOptions().position(latLngStart).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mGoogleMap.addMarker(new MarkerOptions().position(latLngStop).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mGoogleMap.addPolyline(polylineOptions);
        setMap(listOfPoints.get(listOfPoints.size()/2),12f);
    }

    private void setMap(LatLng lanLng, float zoom) {
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setIndoorEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lanLng, zoom);
        mGoogleMap.moveCamera(cameraUpdate);
    }
}
