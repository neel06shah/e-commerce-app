package com.example.shreesaisugandhi.ui.productDetails;

import android.content.Intent;
import android.graphics.Color;
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

import com.example.shreesaisugandhi.ConfirmationActivity;
import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.SliderItem;
import com.example.shreesaisugandhi.adapters.SliderAdapterExample;
import com.example.shreesaisugandhi.cartProduct;
import com.example.shreesaisugandhi.products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class ProductDetails extends Fragment {

    public ProductDetails() { }
    TextView tvTile, tvMrp, tvRate, tvDiscount, tvDescription, tvQuantity,tvSave;
    String title,mrp,description,image,rate,quantity,discount,save;
    Button plus,minus,btnAddCart,btnWish,btnBuyNow;
    EditText total;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferences,photos;
    ArrayList<SliderItem> sliderItems ;

    SliderView sliderView;
    private SliderAdapterExample adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_prdoduct_details, container, false);

        sliderView = view.findViewById(R.id.imageSlider);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            description = bundle.getString("description");
            mrp = bundle.getString("mrp");
            rate = bundle.getString("rate");
            image = bundle.getString("image");
            quantity = bundle.getString("quantity");
            discount=bundle.getString("discount");
            save=bundle.getString("save");
        }

        double disc = Double.parseDouble(discount.trim());
        int dis = (int) disc;

        photos = FirebaseDatabase.getInstance().getReference().child("Products").child(title).child("images");
        photos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sliderItems = new ArrayList<>();
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        sliderItems.add(child.getValue(SliderItem.class));
                    }
                    adapter = new SliderAdapterExample(sliderItems);
                    sliderView.setSliderAdapter(adapter);

                    sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                    sliderView.setIndicatorSelectedColor(Color.parseColor("#4b0082"));
                    sliderView.setIndicatorUnselectedColor(Color.parseColor("#039be5"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tvTile=view.findViewById(R.id.tvTitle);
        tvTile.setText(title);
        tvTile.setAllCaps(true);

        tvMrp=view.findViewById(R.id.tvMrp);
        tvMrp.setText("\u20B9 "+mrp);

        tvDescription=view.findViewById(R.id.tvDescription);
        tvDescription.setText(description);

        tvRate=view.findViewById(R.id.tvRate);
        tvRate.setText(rate);

        tvQuantity=view.findViewById(R.id.tvQuantity);
        tvQuantity.setText(quantity);

        tvDiscount=view.findViewById(R.id.tvDiscount);
        tvDiscount.setText(dis+"%\noff");

        tvSave = view.findViewById(R.id.tvSave);
        tvSave.setText("Save \u20B9 "+save);

        total=view.findViewById(R.id.Count);
        plus=view.findViewById(R.id.btnPlus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(total.getText().toString());
                q=q+1;
                total.setText(String.valueOf(q));

            }
        });

        minus=view.findViewById(R.id.btnMinus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(total.getText().toString());
                int qn=q-1;
                total.setText(String.valueOf(qn));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        final String current = firebaseAuth.getCurrentUser().getPhoneNumber();

        databaseReferences= FirebaseDatabase.getInstance().getReference();
        btnAddCart=view.findViewById(R.id.btnAddCart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartProduct products = new cartProduct(title,description,quantity,image,mrp,rate,discount,"1");
                databaseReferences.child("Cart").child(current).child(title).setValue(products);
                Toast.makeText(view.getContext(), "Product Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        btnWish=view.findViewById(R.id.btnWish);
        btnWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products products = new products(title,description,quantity,image,mrp,rate,discount);
                databaseReferences.child("Wishlist").child(current).child(title).setValue(products);
                Toast.makeText(view.getContext(), "Product Added to WishList", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuyNow=view.findViewById(R.id.btnBuyNow);
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(System.currentTimeMillis());
                String t = total.getText().toString();

                int quan=Integer.parseInt(t);
                int r=Integer.parseInt(rate);
                int finalAmount = r*quan;

                cartProduct products = new cartProduct(title,description,quantity,image,mrp,rate,discount,t);
                databaseReferences.child("Order").child(id).child("Products").child(title).setValue(products);
                Intent i = new Intent(getContext(), ConfirmationActivity.class);
                i.putExtra("id",id);
                i.putExtra("total",String.valueOf(finalAmount));
                startActivity(i);
            }
        });




        return view;
    }
}