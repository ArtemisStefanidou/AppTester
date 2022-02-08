package com.example.apptester;

import androidx.fragment.app.FragmentActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        /**
         * Ένα GoogleMap πρέπει να αποκτηθεί χρησιμοποιώντας getMapAsync(OnMapReadyCallback) και
         * γι αυτο το λόγο χρησιμοποιώ και το implements
         * Στην ουσία το mapFragment είναι ένας απλός τρόπος για να τοποθετήσουμε έναν χάρτη στην εφαρμογή μας
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);



    }

    /**
     * Καλείται όταν ο χάρτης είναι έτοιμος για χρήση
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //θέτουμε έναν listener που θα ενεργοποιηθεί όταν πατηθεί το κουμπί με id getData
        findViewById(R.id.getData).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                /**-----Communicate with the provider of the other app-----*/
                ContentResolver resolver = getContentResolver();

                Uri uri = Uri.parse("content://com.example.ergtserpe/COORDINATE");

                /**-----Cursor to get the list of geofence-----*/
                Cursor cursor = resolver.query(uri,null,null,null,null);
                if (cursor.moveToFirst()){
                    do{

                        /**-----Add Marker-----*/
                        LatLng userLocation = new LatLng( cursor.getDouble(1), cursor.getDouble(2));
                        mMap.addMarker(new MarkerOptions().position(userLocation)).setTitle(userLocation.latitude+","+userLocation.longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));

                        /**-----Debug-----*/
                        Log.d("Cursor", cursor.getString(1));

                    }while(cursor.moveToNext());
                }else{

                    Toast.makeText(MapsActivity.this, "No items in the DataBase", Toast.LENGTH_SHORT).show();
                    Log.d("error", "run: no items in the cursor");

                }

                cursor.close();
            }

        });
    }

}