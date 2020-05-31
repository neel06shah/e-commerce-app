package com.example.shreesaisugandhi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class signup extends AppCompatActivity {

    HorizontalDottedProgress loading;
    EditText etName, number, address,otp;
    Button register,btnOTP;
    FirebaseAuth firebaseAuth;
    DatabaseReference myReff;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.name);
        address = findViewById(R.id.address);
        number=findViewById(R.id.number);
        otp=findViewById(R.id.tvOTP);
        loading = findViewById(R.id.loading);

        loading.setVisibility(View.GONE);

        btnOTP=findViewById(R.id.otp);
        register=findViewById(R.id.register);

        firebaseAuth=FirebaseAuth.getInstance();
        myReff=FirebaseDatabase.getInstance().getReference().child("Users");

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(signup.this, "You will be receiving an OTP soon.\nPlease wait", Toast.LENGTH_SHORT).show();
                sendCode();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                verifyCode();
            }
        });
    }

    private void verifyCode() {
        String code = otp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String name = etName.getText().toString();
                            String add = address.getText().toString();
                            String num = number.getText().toString();

                            users s = new users(name,num,add);
                            myReff.child(num).setValue(s);
                            Intent i = new Intent(signup.this, Dash.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(signup.this, "Incorrect Verification Code", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }


    private void sendCode() {
        String phoneNumber = number.getText().toString();
        if(phoneNumber.length() < 10) {
            number.setError("Please enter valid number");
            number.requestFocus();
        }
        String no = "+91"+phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                no,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,       // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(signup.this, "Code Sent", Toast.LENGTH_SHORT).show();
            codeSent=s;
        }
    };
}
