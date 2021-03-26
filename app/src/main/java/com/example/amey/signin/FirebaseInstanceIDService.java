package com.example.amey.signin;

/**
 * Created by roshan on 3/23/2017.
 */
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token);
    }
    public FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();


    private void registerToken(String token) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();


        System.out.println("In firebaseInstanceIDService");

        Request request = new Request.Builder()
                .url("http://54.174.31.149/firebase/register.php")
                .post(body)
                .build();



        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}