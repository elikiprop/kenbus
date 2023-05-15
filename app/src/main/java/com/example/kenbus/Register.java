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

public class Register extends AppCompatActivity {
    EditText inputEmail, inputNewPassword, inputConfirmPassword, inputPhone, inputFullNames;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView logoTxt;

            logoTxt = findViewById(R.id.login);
            inputEmail = findViewById(R.id.inputEmail);
            inputNewPassword = findViewById(R.id.inputNewPassword);
            inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
            inputPhone = findViewById(R.id.inputPhone);
            inputFullNames = findViewById(R.id.inputFullNames);
            btnRegister = findViewById(R.id.btnRegister);

            progressDialog = new ProgressDialog(this);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

            logoTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Register.this, MainActivity.class));

                }
            });

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PerformAuth();
                }


            });
        }
        private void PerformAuth() {
            String email = inputEmail.getText().toString();
            String password = inputNewPassword.getText().toString();
            String confirmPassword = inputConfirmPassword.getText().toString();


            if (!email.matches(emailPattern)) {
                inputEmail.setError("Enter Correct Email");
            } else if (password.isEmpty()||password.length() < 6) {
                inputNewPassword.setError("Enter proper Password");

            } else if (!password.equals(confirmPassword)) {
                inputConfirmPassword.setError("Password Not match Both Field");
            } else {
                progressDialog.setMessage("Please wait while Registration...");
                progressDialog.setTitle("Registration");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sendUserToNextActivity();
                            Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }


                });

            }
        }

        private void sendUserToNextActivity() {
            Intent intent = new Intent(Register.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


}