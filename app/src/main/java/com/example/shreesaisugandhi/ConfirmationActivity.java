package com.example.shreesaisugandhi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationActivity extends AppCompatActivity {

    TextView conOrder, conNumber, conTotal;
    EditText conAddress;
    Button btnSubmit;
    ListView conProducts;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,from,to,clear;
    RadioGroup rdGroup;
    String payment,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        conOrder=findViewById(R.id.conOrder);
        conNumber=findViewById(R.id.conNumber);
        conTotal=findViewById(R.id.conTotal);
        conAddress=findViewById(R.id.conAddress);
        btnSubmit=findViewById(R.id.btnSubmit);
        conProducts=findViewById(R.id.conProducts);
        rdGroup = findViewById(R.id.rdGroup);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        final String number = firebaseAuth.getCurrentUser().getPhoneNumber();
        String add = number.replace("+91","");

        databaseReference.child("Users").child(add).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String address = dataSnapshot.child("address").getValue(String.class);
                name = dataSnapshot.child("name").getValue(String.class);
                conAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        final String total = i.getStringExtra("total");

        conOrder.setText("Order ID : "+id);
        conNumber.setText("Customer no. : "+number);
        conTotal.setText("Total amount : \u20B9"+total);

        databaseReference.child("Cart").child(number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("name").getValue(String.class);
                    String quantity = areaSnapshot.child("total").getValue(String.class);
                    String rate = areaSnapshot.child("rate").getValue(String.class);

                    int q = Integer.parseInt(quantity);
                    int r = Integer.parseInt(rate);
                    int amt = q*r;

                    areas.add(areaName+"\n"+"Quantity : ("+quantity+"*"+rate+") = "+amt);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(ConfirmationActivity.this, android.R.layout.simple_list_item_1, areas);
                conProducts.setAdapter(areasAdapter);
                setListViewHeightBasedOnChildren(conProducts);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbCOD) {
                    payment="Cash on delivery";
                }
                else if (checkedId == R.id.rbUPI) {
                    payment="UPI";
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = conAddress.getText().toString();

                from=FirebaseDatabase.getInstance().getReference().child("Cart").child(number);
                to=FirebaseDatabase.getInstance().getReference().child("Order").child(id);

                to.child("id").setValue(id);
                to.child("name").setValue(name);
                to.child("address").setValue(a);
                to.child("contact").setValue(number);
                to.child("payment").setValue(payment);
                to.child("total").setValue(total);
                to.child("state").setValue("Waiting for conformation");


                from.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        to.child("Products").setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                                        if (firebaseError != null) {
                                            Toast.makeText(ConfirmationActivity.this, "Please connect internet", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ConfirmationActivity.this, "Your Order is submitted.\nPlease wait for conformation", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(ConfirmationActivity.this,Dash.class);
                                            startActivity(i);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
