package com.example.amey.signin;

/*
*
* Project Name: 	Smart Bus Tracking Systemn
* Author List: 		Amey Karmarkar
* Filename: 		QRcode.java
* Functions: 		onCreate(), setOnClickListener()
* Global Variables:	   inputEmail, btnReset, btnBack, auth, progressBar

*
*/

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetpasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

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
        setContentView(R.layout.activity_resetpassword);

        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();//finish current activity
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                               /*
    *
    * Function Name: 	sendPasswordResetEmail
    * Input: 		Email id of username
    * Output: 		none (void)
    * Logic: 		Firebase function to send reset password request to user with email
    *
    */
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetpasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetpasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

}
