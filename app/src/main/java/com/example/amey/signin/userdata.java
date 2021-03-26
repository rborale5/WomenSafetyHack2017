package com.example.amey.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class userdata extends AppCompatActivity {
    public FirebaseDatabase mfirebasedatabse;
    public DatabaseReference mfirebaserefernence;
    public EditText name,add,pnum,r1num,r2num;
    public Button save;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);

        mfirebasedatabse = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();




        name= (EditText) findViewById(R.id.name);

        add= (EditText) findViewById(R.id.add);

        pnum= (EditText) findViewById(R.id.pnum);

        r1num= (EditText) findViewById(R.id.r1num);

        r2num= (EditText) findViewById(R.id.r2num);

        save= (Button) findViewById(R.id.save);
       // Toast.makeText(getApplicationContext(),"pankaj", Toast.LENGTH_SHORT).show();

       // User u1= new User("pankaj","mumbai","67367","76873","7976984");



        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();



        save.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {

                String u = name.getText().toString();


                User u1= new User(name.getText().toString(),add.getText().toString(),pnum.getText().toString(),r1num.getText().toString(),r2num.getText().toString());

FirebaseUser user = auth.getCurrentUser();

                mfirebaserefernence = mfirebasedatabse.getReference().child("users").child(user.getUid().toString());
                mfirebaserefernence.setValue(u1);



               // final DatabaseReference ref = firebaseDatabase.getReference().child("users").child(u);
                 //       ref.setValue(u1);
               // System.out.println("Aftre ref");
              //  Toast.makeText(getApplicationContext(),"pankaj best of luck",Toast.LENGTH_SHORT).show();
                String s= r1num.getText().toString();
                Intent ii=new Intent(userdata.this,BroadCast.class);
                ii.putExtra("r1num", s);
                startActivity(ii);









            }
        });


    }
}
