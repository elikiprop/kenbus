package com.example.kenbus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView Register;
    EditText inputEmail, inputPassword;
    Button buttonLogin;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Register = findViewById(R.id.createAccount);

            inputEmail = findViewById(R.id.inputEmail);
            inputPassword = findViewById(R.id.inputPassword);
            buttonLogin = findViewById(R.id.buttonLogin);

            progressDialog = new ProgressDialog(this);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, Register.class));
                }
            });
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PerformLogin();
                }
            });
        }

        private void PerformLogin() {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();


            if (!email.matches(emailPattern)) {
                inputEmail.setError("Enter Correct Email");
            } else if (password.isEmpty() || password.length() < 6) {
                inputPassword.setError("Enter proper Password");


            } else {
                progressDialog.setMessage("Please wait while Login...");
                progressDialog.setTitle("Login");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sendUserToNextActivity();

                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }


                });
            }

        }

        private void sendUserToNextActivity() {
            Intent intent = new Intent(MainActivity.this, Booking.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

    }
}