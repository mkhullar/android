package com.mayankkhullar.helpy;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    public MainActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        GpsService gps = new GpsService(this);
        if(gps.canGetLocation){
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
                Toast.makeText(this,"Address:"+address,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"City:"+city,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"state:"+state,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"country:"+country,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"postalCode:"+postalCode,Toast.LENGTH_LONG).show();
                Toast.makeText(this,"knownName:"+knownName,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
