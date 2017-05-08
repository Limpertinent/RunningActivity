package com.example.alex.testwithfragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import static com.example.alex.testwithfragment.R.id.map;
import static com.example.alex.testwithfragment.R.id.time;


public class MapsActivity extends FragmentActivity{

    private GoogleMap mMap;
    private Button button1;
    private Button button2;
    private LocationManager locationManager;
    private LocationListener listener;
    private Chronometer chrono;
    private TextView textView;
    long time=0;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        chrono= (Chronometer) findViewById(R.id.chronometer1);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView1);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textView.setText("Speed:"+(double)((int)(location.getSpeed()*100))/100+"m/s");
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        requestLocation();
    }

    @TargetApi(23)
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET
            },10);
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 50, listener);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.button1:
                        chrono.setBase(SystemClock.elapsedRealtime()+time);
                        chrono.start();
                        break;
                    case R.id.button2:
                        time = chrono.getBase() + SystemClock.elapsedRealtime();
                        chrono.stop();
                        break;
                }
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private GoogleMap setUpMap(GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(48.624894, 2.443230)).title("INT").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.624894, 2.443230), 14.9f));
        return mMap;

    }


    private void setUpMapIfNeeded() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = setUpMap(googleMap);
            }
        });
    }


}
