package com.mayankkhullar.helpy;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    protected LocationManager locationManager;
    private Handler mHandler = new Handler();
    Boolean gpsClicked = false;
    Boolean gpsAccept = false;
    public MainActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        GPSEnabled();

    }
    /*public void GPSEnabled() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!statusOfGPS && ! gpsClicked){
            showDialogGPS();
        }else {
            getGPSLocation();

        }
    }*/

    public void GPSEnabled() {
        final int FIVE_SECONDS = 5000;
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if(!statusOfGPS && !gpsClicked){
                        showDialogGPS();
                    }else {
                        getGPSLocation();
                    }
                    mHandler.postDelayed(this, FIVE_SECONDS);
                }
            }, FIVE_SECONDS);
    }

    public void getGPSLocation(){
    GpsService gps = new GpsService(this);
    if(gps.canGetLocation){
        Intent intent = new Intent(this,GpsService.class);
        startService(intent);
        Location location = gps.getLocation();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            EditText ed = (EditText) findViewById(R.id.Add);
            ed.setText(address);
            EditText ed1 = (EditText) findViewById(R.id.Pin);
            ed1.setText(postalCode);
              /*  Toast.makeText(this,"Address:"+address,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"City:"+city,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"state:"+state,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"country:"+country,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"postalCode:"+postalCode,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"knownName:"+knownName,Toast.LENGTH_LONG).show();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    /**
     * Show a dialog to the user requesting that GPS be enabled
     */
    private void showDialogGPS() {
        gpsClicked = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
                gpsClicked = false;
                GPSEnabled();
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                gpsClicked = false;
                GPSEnabled();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
