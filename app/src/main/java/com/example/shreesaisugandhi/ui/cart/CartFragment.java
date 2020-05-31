package com.example.shreesaisugandhi.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreesaisugandhi.ConfirmationActivity;
import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.cartProduct;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CartFragment extends Fragment {

    public CartFragment() {}

    private RecyclerView list_view;
    private DatabaseReference reference,from,to;
    private FirebaseAuth firebaseAuth;
    TextView tvTotal;
    int finalAmount=0;
    Button buyNow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        final String current = firebaseAuth.getCurrentUser().getPhoneNumber();
        tvTotal=view.findViewById(R.id.tvTotal);

        reference= FirebaseDatabase.getInstance().getReference().child("Cart").child(current);
        reference.keepSynced(true);

        list_view=view.findViewById(R.id.listView);
        list_view.setHasFixedSize(true);
        list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        final String id = String.valueOf(System.currentTimeMillis());
        buyNow=view.findViewById(R.id.btnBuy);
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                from=FirebaseDatabase.getInstance().getReference().child("Cart").child(current);
                to=FirebaseDatabase.getInstance().getReference().child("Order").child(id);
                from.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        to.child("Products").setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                                if (firebaseError != null) {
                                    Toast.makeText(getContext(), "Please connect internet", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent i = new Intent(getContext(), ConfirmationActivity.class);
                                    i.putExtra("id",id);
                                    i.putExtra("total",String.valueOf(finalAmount));
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


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<cartProduct, cartproductsViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter <cartProduct, cartproductsViewHolder>
                (cartProduct.class,R.layout.cart_product, cartproductsViewHolder.class,reference)
        {
            @Override
            protected void populateViewHolder(cartproductsViewHolder productsViewHolder, cartProduct products, int i) {
                productsViewHolder.setName(products.getName());
                productsViewHolder.setQuantity(products.getQuantity());
                productsViewHolder.setMrp(products.getMrp());
                productsViewHolder.setRate(products.getRate());
                productsViewHolder.setDiscount(products.getDiscount());
                productsViewHolder.setDescription(products.getDescription());
                productsViewHolder.setImage(products.getImage());
                productsViewHolder.setTotal(products.getTotal());


                int price =Integer.parseInt(products.getRate());
                int q =Integer.parseInt(products.getTotal());
                int oneTyprProductTPrice = (int) (price*q);

                finalAmount = finalAmount + oneTyprProductTPrice;
                tvTotal.setText("Final Amount : \u20b9"+finalAmount);
            }
        };
        list_view.setAdapter(firebaseRecyclerAdapter);
    }

    public static class cartproductsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        String title,desc,Mrp,Rate,Discount,Image,quan;
        int m,r;
        FirebaseAuth firebaseAuth;
        Button plus,minus;
        ImageButton delete;
        EditText quantity;
        DatabaseReference myRef;

        public cartproductsViewHolder(final View itemView) {
            super(itemView);
            mView=itemView;

            quantity=itemView.findViewById(R.id.Count);
            firebaseAuth=FirebaseAuth.getInstance();
            final String current = firebaseAuth.getCurrentUser().getPhoneNumber();
            myRef=FirebaseDatabase.getInstance().getReference();

            plus=itemView.findViewById(R.id.btnPlus);
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q = Integer.parseInt(quantity.getText().toString());
                    q=q+1;
                    quantity.setText(String.valueOf(q));
                    myRef.child("Cart").child(current).child(title).child("total").setValue(String.valueOf(q));

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new CartFragment()).commit();
                }
            });

            minus=itemView.findViewById(R.id.btnMinus);
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q = Integer.parseInt(quantity.getText().toString());
                    int qn=q-1;
                    quantity.setText(String.valueOf(qn));
                    myRef.child("Cart").child(current).child(title).child("total").setValue(String.valueOf(qn));

                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                            new CartFragment()).commit();
                }
            });

            delete=itemView.findViewById(R.id.btnDelete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query applesQuery = ref.child("Cart").child(current).orderByChild("name").equalTo(title);

                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                                Toast.makeText(itemView.getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();

                                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                        new CartFragment()).commit();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }
            });
        }


        public void setImage(String image) {
            Image=image;
            ImageView imageView = mView.findViewById(R.id.imageView);
            Glide.with(mView.getContext()).load(image).into(imageView);
        }

        public void setName(String name) {
            title=name;
            TextView tvPrintTitle = mView.findViewById(R.id.textViewTitle);
            tvPrintTitle.setText(name);
        }
        public void setDescription(String description) {
            desc = description;
        }
        public void setQuantity(String quantity) {
            quan=quantity;
            TextView tvSd = mView.findViewById(R.id.textViewShortDesc);
            tvSd.setText(quantity);
        }
        public void setMrp(String mrp) {
            Mrp=mrp;
            m=Integer.parseInt(mrp);
            TextView tvM = mView.findViewById(R.id.textViewMRP);
            tvM.setText("\u20B9 "+mrp);
        }
        public void setRate(String rate) {
            Rate=rate;
            r=Integer.parseInt(rate);
            TextView tvR = mView.findViewById(R.id.textViewPrice);
            tvR.setText("\u20B9 "+rate);
        }
        public void setDiscount(String discount) {
            Discount=discount;
            TextView tvR = mView.findViewById(R.id.textViewDiscount);
            tvR.setText("You Save \u20B9"+(m-r)+" ("+discount+"%)");

            double d = Double.parseDouble(discount);
            int s = (int) d;
            TextView dis = mView.findViewById(R.id.tvDiscount);
            dis.setText(s+"%\noff");
        }
        public void setTotal(String total) {
            quantity.setText(total);
        }
    }
}
