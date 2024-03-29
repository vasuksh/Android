package com.example.acer.showinguserslocationonmaps;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }
//buhdfgcyusdhdb


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener =new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userloc= new LatLng(location.getLatitude(),location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(userloc).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userloc));
                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<android.location.Address> listAddress=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(listAddress != null && listAddress.size()>0)
                    {

                        Log.i("Place info=",listAddress.get(0).toString());
                        Toast.makeText(MapsActivity.this,listAddress.get(0).getAddressLine(0).toString(),Toast.LENGTH_SHORT);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
if(Build.VERSION.SDK_INT<23)
{
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
}
else {

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    } else {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location lastknownloc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LatLng userloc= new LatLng(lastknownloc.getLatitude(),lastknownloc.getLongitude());
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userloc).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userloc));
    }

}

    }
}
