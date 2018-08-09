package com.example.nguyenhuy.googlemap;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {
    public LocationManager locationManager;
    public MyLocationListener listener;


    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
   //     ActivityCompat.requestPermissions(this,new String[]{com.example.nguyenhuy.googlemap.Manifest.ACCESS_FINE_LOCATION}, 1);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 0, listener);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 0, listener);

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }


    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged(final Location location)
        {
           Log.e("*************","-"+"-.."+location);
            MapsActivity.latitude = location.getLatitude();
            MapsActivity.longitude = location.getLongitude();
            MapsActivity.mDatabase = FirebaseDatabase.getInstance().getReference();

            MapsActivity.mDatabase = FirebaseDatabase.getInstance().getReference();
            MapsActivity.sharedPreferences= getSharedPreferences("idThanhVien", MODE_PRIVATE);

            if(MapsActivity.sharedPreferences.getString("tenNhom", "b").equals("b")==false) {
                ThongTinThanhVien t = new ThongTinThanhVien(MapsActivity.sharedPreferences.getString("ten","?"), MapsActivity.latitude, MapsActivity.longitude,MapsActivity.sharedPreferences.getInt("id", 0),MapsActivity.sharedPreferences.getString("linkAnh","https://firebasestorage.googleapis.com/v0/b/map-82eb0.appspot.com/o/HOI.png?alt=media&token=42b16371-97b8-42e1-9887-20e7a546703d"));
                try {
                    MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).setValue(t);/////loi
                } catch (Exception e) {
                    MapsActivity.mDatabase = FirebaseDatabase.getInstance().getReference();
                    MapsActivity.sharedPreferences = getSharedPreferences("idThanhVien", MODE_PRIVATE);
                    MapsActivity.mDatabase.child(MapsActivity.sharedPreferences.getString("tenNhom", "b")).child(String.valueOf(MapsActivity.sharedPreferences.getInt("id", 0))).setValue(t);/////loi

                }
            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }
}