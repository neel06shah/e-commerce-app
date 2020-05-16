package com.example.shreesaisugandhi.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shreesaisugandhi.Dash;
import com.example.shreesaisugandhi.ForgotActivity;
import com.example.shreesaisugandhi.MainActivity;
import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    public ProfileFragment() { }
    TextView ProfNumber;
    EditText ProfName, ProfAddress;
    Button btnEdit, btnDone;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ProfName = view.findViewById(R.id.ProfName);
        ProfNumber=view.findViewById(R.id.ProfNumber);
        ProfAddress=view.findViewById(R.id.ProfAddress);
        btnEdit=view.findViewById(R.id.btnEdit);
        btnDone=view.findViewById(R.id.btnDone);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        final String email = firebaseAuth.getCurrentUser().getPhoneNumber();

        final String em = email.replace("+91","");
        ProfNumber.setText(email);

        databaseReference.child(em).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String type = dataSnapshot.child("address").getValue().toString();

                ProfName.setText(name);
                ProfAddress.setText(type);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = ProfName.getText().toString();
                final String newNumber = ProfAddress.getText().toString();

                databaseReference.child(em).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("name").setValue(newName);
                        dataSnapshot.getRef().child("address").setValue(newNumber);
                        Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Dash.class);
                startActivity(i);
            }
        });


        return view;
    }
}