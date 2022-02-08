package com.example.apptester;

import androidx.fragment.app.FragmentActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

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

                ContentResolver resolver = getContentResolver();
                Uri uri = Uri.parse("content://com.example.ergtserpe/COORDINATE");
                Cursor cursor = resolver.query(uri,null,null,null,null);
                if (cursor.moveToFirst()){
                    do{

                        LatLng userLocation = new LatLng( cursor.getDouble(1), cursor.getDouble(2));
                        mMap.addMarker(new MarkerOptions().position(userLocation));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));


                        Log.d("Cursor", cursor.getString(1));
                    }while(cursor.moveToNext());
                }else{
                    Log.d("error", "run: no items in the cursor");
                }
                cursor.close();
            }

        });
    }

}