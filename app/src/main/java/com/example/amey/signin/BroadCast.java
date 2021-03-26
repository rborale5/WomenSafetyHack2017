package com.example.amey.signin;

import android.*;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class BroadCast extends AppCompatActivity {
    public  int count=0;
    public FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mMessagesDatabaseReference1;
    //private DatabaseReference mMessagesDatabaseReference2;
    private TrackGPS gps;
    public Button abutton,signOut,customAlert,customSave;
    public String message="help!";
    EditText customMessage;
    double longitude;
    private boolean isAuthListenerSet = false;
    double latitude;
    public FirebaseAuth auth;
    public String num;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast);
        abutton = (Button) findViewById(R.id.abutton);
        customAlert = (Button) findViewById(R.id.customAlert);
        customMessage = (EditText) findViewById(R.id.customMessage);
        customSave = (Button) findViewById(R.id.customSave) ;
        signOut = (Button) findViewById(R.id.signOut);
        //  final TextView messg= (TextView) findViewById(R.id.messg);

        gps = new TrackGPS(BroadCast.this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mMessagesDatabaseReference1 = mFirebaseDatabase.getReference().child("user").child(user.getUid());
        GeoFire geofire = new GeoFire(mMessagesDatabaseReference);


        System.out.println(user.getClass());
        System.out.println(auth.getCurrentUser().getUid());


//....................getExtra from activity userdata............................
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
          num =(String) b.get("r1num");

        }
//............................................................................

        if (gps.canGetLocation()) {


            longitude = gps.getLongitude();
            latitude = gps.getLatitude();
            geofire.setLocation(user.getUid().toString(), new GeoLocation(latitude, longitude));

            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            gps.showSettingsAlert();
        }


//not working...giving null value.... for fetching value from firebase...........................................
      /*  mMessagesDatabaseReference1.child("r1num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                r1num=(String) dataSnapshot.getValue();
                Toast.makeText(getApplicationContext(),r1num , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mMessagesDatabaseReference1.child("r2num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                r2num=(String) dataSnapshot.getValue();
                Toast.makeText(getApplicationContext(),r2num, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/
    customAlert.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            customMessage.setVisibility(View.VISIBLE);
            customSave.setVisibility(View.VISIBLE);


        }
    });

        customSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message= customMessage.getText().toString();
                customMessage.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Saved!!" ,Toast.LENGTH_SHORT).show();
                customSave.setVisibility(View.INVISIBLE);

            }
        });





            abutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    GeoFire geofire = new GeoFire(mMessagesDatabaseReference);
                    GeoQuery geoQuery = geofire.queryAtLocation(new GeoLocation(latitude, longitude), 10);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {
      //                      System.out.println("Geofire" + key);
                            count++;
                            //Toast.makeText(getApplicationContext(),Integer.toString(count), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onKeyExited(String key) {

                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {

                        }
                    });



                        // carry on the normal flow, as the case of  permissions  granted.
                 //   LoginActivity login=new LoginActivity();


                   // Intent intent = getIntent();
                   // String phone= intent.getStringExtra("r1num");

                    //String phone1 = r2num;

                    //.......................sms......................................
                    if(num!=null)
                         sendSMS(num, message);   //using intentExtra now...unable to fetch child's value from firebase......
                    else
                        Toast.makeText(getApplicationContext(),"work in progress..plz register and try :D" ,Toast.LENGTH_SHORT).show();
                  //  ......................................................................




                    ////.................call.......................


                       //  Intent callIntent = new Intent(Intent.ACTION_CALL);
                       // callIntent.setData(Uri.parse("tel:8446299796"));

                       //  startActivity(callIntent);
                   //............................................................................................

                  //  String url = "http://54.174.31.149/firebase/push_notification.php";
                    try {
                    URL url = new URL( "http://54.174.31.149/firebase/push_notification.php");

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String webPage = "",data="";

                        while ((data = reader.readLine()) != null){
                            webPage += data + "\n";
                        }
                        Toast.makeText(getApplicationContext(),webPage,Toast.LENGTH_LONG).show();


                    }
                    catch(Exception e)
                    {



                    }
                  /*  //......not working..........for running .php...........................................
                    OkHttpClient client = new OkHttpClient();
                  //  RequestBody body = new FormBody.Builder().build();


                  //  System.out.println("In firebaseInstanceIDService");

                    Request request = new Request.Builder()
                            .url("http://54.174.31.149/firebase/push_notification.php")
                            .build();



                    try {
                        client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //......................................................................

                   */


                }
            });



     signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });



    }




// listens if user is authenticated or not.....
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                //User is signed in
            } else {

                startActivity(new Intent(BroadCast.this,LoginActivity.class));

                //User is signed out
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!isAuthListenerSet) {
            FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
            isAuthListenerSet = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
            isAuthListenerSet = false;
        }
    }



    public void signOut() {

        auth.signOut();
    }



    private void sendSMS(String phoneNumber, String message) {
        Toast.makeText(getApplicationContext(),"gaya", Toast.LENGTH_SHORT).show();
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }



}