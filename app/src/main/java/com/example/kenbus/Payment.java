package com.example.kenbus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Payment extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://kenbus-ae7e5-default-rtdb.firebaseio.com/");
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    EditText mpesa;
    Button pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mpesa = findViewById(R.id.inputMpesaNumber);
        pay = findViewById(R.id.btnPay);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPayment();
            }
        });
    }

    private void finishPayment() {
        String userMpesaNumber = mpesa.getText().toString();
        progressDialog.setMessage("Please wait while payment completes...");
        progressDialog.setTitle("Payment");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String theUserId = mUser.getUid();
        databaseReference.child("tickets").child(theUserId).child("mpesaNumber").setValue(userMpesaNumber);
        databaseReference.child("tickets").child(theUserId).child("status").setValue("paid");

        progressDialog.dismiss();
        sendUserToNextActivity();
        Toast.makeText(Payment.this, "Payment successful!", Toast.LENGTH_SHORT).show();
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(Payment.this,ViewTicket.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}