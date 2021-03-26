package com.example.amey.signin;

/*
*
* Project Name: 	Smart Bus Tracking System
* Author List: 		Amey Karmarkar
* Filename: 		MapsActivity1.java
* Functions: 		onCreate(), initializaMap(), attachDatabaseReadListener()
* Global Variables:	    inputEmail, inputPassword, auth, progressBar, btnSignup, btnLogin, btnReset
*
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    private ChildEventListener mChildEventListener;
    private PolylineOptions mPolylineOptions;

    private static final int INITIAL_ZOOM_LEVEL = 18;

    public double l=0, lng=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent myIntent = getIntent(); // gets the previously created intent
        String RouteID = myIntent.getStringExtra("route_id");
        System.out.println(RouteID);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Location").child(RouteID);
        System.out.println(mMessagesDatabaseReference);



        Context context = getApplicationContext();




    }

    private void initializeMap() {
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(Color.DKGRAY).width(10);
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
        GeoFire geoFire;
        initializeMap();
        attachDatabaseReadListener();
    }

private void attachDatabaseReadListener(){
    /*
    *
    * Function Name: 	attachDatabaseReadListener
    * Input: 		none
    * Output: 		none
    * Logic: 		This function is used to get the locations of Bus from database
    * Example Call:		attachDatabaseReadListener();
    *called every time data is changed at the specified database reference, including changes to children
    */
    mMessagesDatabaseReference.addValueEventListener(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            System.out.println("In Datasnapshot");
            //   if (mChildEventListener == null) {

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Location trial = dataSnapshot.getValue(Location.class);
                        System.out.println(trial.getlatitude());//get latitude value of location
                    System.out.println(trial.getlongitude());//get longitude value of location
                    LatLng marker = new LatLng(trial.getlatitude(),trial.getlongitude());
//setting marker to the location
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).position(marker).title(mMessagesDatabaseReference.getKey()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, INITIAL_ZOOM_LEVEL));

                 /*   mMap.clear();
                    mMap.addPolyline(mPolylineOptions.add(marker));
                    mMap.addMarker(new MarkerOptions().position(marker));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 14));
*/

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //called every time data is changed at the specified database reference, including changes to children
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //called every time data is removed at the specified database reference, including changes to children
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    //called every time data is moved at the specified database reference, including changes to children
                }

                public void onCancelled(DatabaseError databaseError) {
                    //called if error occurs
                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);//call childeventlistener
        }
        //}


        @Override
        public void onCancelled(DatabaseError databaseError) {
            System.out.println("The read failed: " + databaseError.getCode());
        }





    });


}





}


