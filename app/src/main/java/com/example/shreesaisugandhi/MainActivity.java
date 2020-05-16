package com.example.shreesaisugandhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText number,otp;
    Button register,btnOTP,login;
    FirebaseAuth firebaseAuth;
    DatabaseReference myReff;
    String codeSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number=findViewById(R.id.number);
        otp=findViewById(R.id.tvOTP);

        btnOTP=findViewById(R.id.otp);
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);

        firebaseAuth=FirebaseAuth.getInstance();
        myReff= FirebaseDatabase.getInstance().getReference().child("Users");

        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,signup.class);
                startActivity(i);
            }
        });
    }

    private void verifyCode() {
        String code = otp.getText().toString();
        final PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        myReff.orderByChild("phone").equalTo(number.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Intent i = new Intent(MainActivity.this, Dash.class);
                                startActivity(i);
                            }
                            else {
                                if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(MainActivity.this, "Incorrect Verification Code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Please register your account.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Please connect to an internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendCode() {
        Toast.makeText(this, "You will be getting a message soon. \n Please Wait", Toast.LENGTH_SHORT).show();
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
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otp.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent=s;
        }

        private void verifyVerificationCode(String code) {
            //creating the credential
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);

            //signing the user
            signInWithPhoneAuthCredential(credential);
        }
    };
}