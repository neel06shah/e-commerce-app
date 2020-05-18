package com.example.shreesaisugandhi.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderFragment extends Fragment {

    private RecyclerView list_view;
    private Query reference;
    private FirebaseAuth firebaseAuth;
    String title;

    public OrderFragment(){}
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        firebaseAuth= FirebaseAuth.getInstance();
        String current = firebaseAuth.getCurrentUser().getPhoneNumber();

        reference= FirebaseDatabase.getInstance().getReference().child("Order").orderByChild("contact").equalTo(current);
        reference.keepSynced(true);

        list_view=view.findViewById(R.id.listView);
        list_view.setHasFixedSize(true);
        list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<orders, ordersViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter <orders, ordersViewHolder>
                (orders.class,R.layout.list_order, ordersViewHolder.class,reference)
        {
            @Override
            protected void populateViewHolder(ordersViewHolder ordersViewHolder , orders orders, int i) {
                ordersViewHolder.setId(orders.getId());
                ordersViewHolder.setPayment(orders.getPayment());
                ordersViewHolder.setTotal(orders.getTotal());
                ordersViewHolder.setDate(orders.getDate());
                ordersViewHolder.setState(orders.getState());
            }
        };
        list_view.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ordersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ordersViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        public void setId(String id) {
            TextView Id = mView.findViewById(R.id.orderID);
            Id.setText(id);
        }

        public void setPayment(String payment) {
            TextView Id = mView.findViewById(R.id.orderType);
            Id.setText("Payment Type : "+payment);
        }

        public void setTotal(String total) {
            TextView Id = mView.findViewById(R.id.orderAmount);
            Id.setText("Total amount : \u20b9"+total);
        }
        public void setDate(String date) {
            TextView Id = mView.findViewById(R.id.orderDate);
            Id.setText("Date : "+date);
        }

        public void setState(String state) {
            TextView Id = mView.findViewById(R.id.orderState);
            Id.setText(state);
        }


    }
}
