package com.example.shreesaisugandhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
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
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText number, otp;
    Button register, btnOTP, login;
    FirebaseAuth firebaseAuth;
    DatabaseReference myReff;
    String verificationCode;
    HorizontalDottedProgress horizontalDottedProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number = findViewById(R.id.number);
        otp = findViewById(R.id.tvOTP);

        btnOTP = findViewById(R.id.otp);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        horizontalDottedProgress = findViewById(R.id.loading);

        firebaseAuth = FirebaseAuth.getInstance();
        myReff = FirebaseDatabase.getInstance().getReference().child("Users");


        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number.getText().toString().length() == 10) {
                    myReff.orderByChild("phone").equalTo(number.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Toast.makeText(MainActivity.this, "You will receive an OTP soon.\nPlease Wait", Toast.LENGTH_SHORT).show();
                                sendVerificationCodeToUser("+91"+number.getText().toString());
                            } else {
                                DynamicToast.Config.getInstance().setTextSize(20).apply();
                                Toast dynamicToast = DynamicToast.makeWarning(MainActivity.this, "Please register your account.");
                                dynamicToast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                                dynamicToast.show();

                                Intent i = new Intent(MainActivity.this, signup.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    number.setError("Please enter valid mobile number");
                    number.requestFocus();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().toString().isEmpty()) {
                    otp.setError("PLease enter OTP");
                    otp.requestFocus();
                } else {
                    horizontalDottedProgress.setVisibility(View.VISIBLE);
                    verifyCode(otp.getText().toString());
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, signup.class);
                startActivity(i);
            }
        });
    }

    private void sendVerificationCodeToUser(String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                s,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
        }
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,code);
        signInTheUser(credential);
    }

    private void signInTheUser(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()) {
                   Intent i = new Intent(MainActivity.this, Dash.class);
                   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   startActivity(i);
               }
               else {
                   Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}
        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, signup.class);
                startActivity(i);
            }
        });
        btnOTP.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          if (number.getText().toString().length() == 10) {
                                              myReff.orderByChild("phone").equalTo(number.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                  @SuppressLint("WrongConstant")
                                                  @Override
                                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                      if (dataSnapshot.getValue() != null) {
                                                          Toast.makeText(MainActivity.this, "You will receive an OTP soon.\nPlease Wait", Toast.LENGTH_SHORT).show();
                                                          sendVerificationCode();
                                                      } else {
                                                          DynamicToast.Config.getInstance().setTextSize(20).apply();
                                                          Toast dynamicToast = DynamicToast.makeWarning(MainActivity.this, "Please register your account.");
                                                          dynamicToast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                                                          dynamicToast.show();

                                                          Intent i = new Intent(MainActivity.this, signup.class);
                                                          startActivity(i);
                                                      }
                                                  }

                                                  @Override
                                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                                  }
                                              });
                                          }

                                        else {
                                              number.setError("Please enter valid mobile number");
                                              number.requestFocus();
                                          }
                                      }

        });
    }
    private void verifySignInCode(){
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
                            //here you can open new activity
                            Toast.makeText(getApplicationContext(),
                                    "Login Successfull", Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode(){

        String phone = number.getText().toString();

        if(phone.isEmpty()){
            number.setError("Phone number is required");
            number.requestFocus();
            return;
        }

        if(phone.length() < 10 ){
            number.setError("Please enter a valid phone");
            number.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "91"+phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
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

            codeSent = s;
        }
    };
}*/