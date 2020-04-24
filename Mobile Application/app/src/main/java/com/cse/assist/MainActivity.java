package com.cse.assist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference Botstatus=database.getReference("Status");
    EditText Status, ed1,ed2,ed3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Retriving Data From Firebase
        Status = findViewById(R.id.status);

        Botstatus.addValueEventListener(new ValueEventListener() {
            final String temp[]=new String[1];
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    temp[i] = data.getValue().toString();
                    i++;
                }
                Status.setText(temp[0]);
            }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    });

        //PUSH NOTIFICATION
        //ed1=(EditText)findViewById(R.id.status);

        Button b1=(Button)findViewById(R.id.utton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tittle="First Notification";
                String subject="First Notification";
                String body= "HELLO WORLD";

                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification.Builder
                        (getApplicationContext()).setContentTitle(tittle).setContentText(body).
                        setContentTitle(subject).setSmallIcon(R.drawable.abc).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);
            }
        });
    }
}
