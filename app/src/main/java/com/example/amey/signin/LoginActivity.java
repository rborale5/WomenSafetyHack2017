package com.example.amey.signin;

/*
*
* Project Name: 	Smart Bus Tracking System
* Author List: 		Amey Karmarkar
* Filename: 		LoginActivity.java
* Functions: 		onCreate(), setOnClickListener(), addOnCompleteListener()
* Global Variables:	    inputEmail, inputPassword, auth, progressBar, btnSignup, btnLogin, btnReset
*
*/

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
    *
    * Function Name: 	onCreate
    * Input: 		Bundle savedInstanceState  stores - provides you with a Bundle containing the activity's previously frozen state, if there was one.
    * Output: 		none (void)
    * Logic: 		This is where you should do all of your normal static set up: create views, bind data to lists, etc.
    * Example Call:		Called when activity is created.
    *
    */
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        System.out.println(auth.getCurrentUser());
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));//starts new Activity if user is authorised user
            finish();
        }

        // set the view
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);// Initialise editable textview used  for inputEmail
        inputPassword = (EditText) findViewById(R.id.password);// Initialise editable textview used for password
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


       if( checkAndRequestPermissions())
        {


            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //launch SignupActivity if user is new user
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                }
            });

            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // launch ResetpasswordActivity to reset the passord
                    startActivity(new Intent(LoginActivity.this, ResetpasswordActivity.class));
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Function to verify authentication details of user
                    String email = inputEmail.getText().toString();
                    final String password = inputPassword.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        //Show warning if email address field is empty
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        //Show warning if password field is empty
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    //authenticate user
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            inputPassword.setError(getString(R.string.minimum_password));
                                        } else {
                                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, BroadCast.class);

                                        startActivity(intent);//launch new activity if user is valid user
                                        finish();
                                    }
                                }
                            });
                }
            });
        }
    }


    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS);
        int permissionCallPhone = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE);
        int permissionGPS = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (permissionCallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }
        if (permissionGPS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
