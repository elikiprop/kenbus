package com.example.kenbus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Booking extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kenbus-ae7e5-default-rtdb.firebaseio.com/");
    EditText inputFullNames, inputPhone, inputDeparture, inputDestination, inputDepartureTime, inputDate;
    Button Book;
    Intent intent;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

            inputFullNames = (EditText) findViewById(R.id.inputFullNames);
            inputPhone =(EditText)  findViewById(R.id.inputPhone);
            inputDeparture = (EditText) findViewById(R.id.Departure);
            inputDestination = (EditText) findViewById(R.id.Destination);
            inputDepartureTime = (EditText) findViewById(R.id.DepartureTime);
            inputDate = (EditText) findViewById(R.id.Date);
            Book = (Button) findViewById(R.id.Booking);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processBooking();
            }
        });
    }
    private void processBooking(){
        String Full_names = inputFullNames.getText().toString();
        String Phone_number = inputPhone.getText().toString();
        String Departure= inputDeparture.getText().toString();
        String Destination = inputDestination.getText().toString();
        String Departure_Time = inputDepartureTime.getText().toString();
        String Date = inputDate.getText().toString();

        progressDialog.setMessage("Please wait while booking completes...");
        progressDialog.setTitle("Booking");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String theUserId = mUser.getUid();
        databaseReference.child("tickets").child(theUserId).child("fullnames").setValue(Full_names);
        databaseReference.child("tickets").child(theUserId).child("phonenumber").setValue(Phone_number);
        databaseReference.child("tickets").child(theUserId).child("departure").setValue(Departure);
        databaseReference.child("tickets").child(theUserId).child("destination").setValue(Destination);
        databaseReference.child("tickets").child(theUserId).child("time").setValue(Departure_Time);
        databaseReference.child("tickets").child(theUserId).child("date").setValue(Date);
        databaseReference.child("tickets").child(theUserId).child("status").setValue("unpaidBooking");

        progressDialog.dismiss();
        sendUserToNextActivity();
        Toast.makeText(Booking.this, "Booking successful!", Toast.LENGTH_SHORT).show();
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(Booking.this,Payment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
