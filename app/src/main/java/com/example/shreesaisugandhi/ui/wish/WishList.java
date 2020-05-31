package com.example.shreesaisugandhi.ui.wish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.cartProduct;
import com.example.shreesaisugandhi.products;
import com.example.shreesaisugandhi.ui.productDetails.ProductDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class WishList extends Fragment {
    public WishList() {}

        FirebaseAuth firebaseAuth;
        private RecyclerView list_view;
        private Query reference;
        String title;

        public View onCreateView (@NonNull LayoutInflater inflater,
                ViewGroup container, Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_wish_list, container, false);

            firebaseAuth =FirebaseAuth.getInstance();
            String user = firebaseAuth.getCurrentUser().getPhoneNumber();
            reference = FirebaseDatabase.getInstance().getReference().child("Wishlist").child(user);


            list_view = view.findViewById(R.id.listView);
            list_view.setHasFixedSize(true);
            list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
            return view;
        }

        @Override
        public void onStart () {
            super.onStart();
            FirebaseRecyclerAdapter<products, productsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<products, productsViewHolder>
                    (products.class, R.layout.product_item, productsViewHolder.class, reference) {
                @Override
                protected void populateViewHolder(productsViewHolder productsViewHolder, products products, int i) {
                    productsViewHolder.setName(products.getName());
                    productsViewHolder.setQuantity(products.getQuantity());
                    productsViewHolder.setMrp(products.getMrp());
                    productsViewHolder.setRate(products.getRate());
                    productsViewHolder.setDiscount(products.getDiscount());
                    productsViewHolder.setDescription(products.getDescription());
                    productsViewHolder.setImage(products.getImage());
                }
            };
            list_view.setAdapter(firebaseRecyclerAdapter);
        }

        public static class productsViewHolder extends RecyclerView.ViewHolder {
            View mView;
            String title, desc, Mrp, Rate, Discount, Image, quan;
            int m, r;
            DatabaseReference firebaseDatabase;
            FirebaseAuth firebaseAuth;
            String current;
            Button cart;

            public productsViewHolder(final View itemView) {
                super(itemView);
                mView = itemView;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment myFragment = new ProductDetails();

                        int save = m - r;
                        String s = String.valueOf(save);

                        Bundle arguments = new Bundle();
                        arguments.putString("title", title);
                        arguments.putString("description", desc);
                        arguments.putString("mrp", Mrp);
                        arguments.putString("rate", Rate);
                        arguments.putString("discount", Discount);
                        arguments.putString("image", Image);
                        arguments.putString("quantity", quan);
                        arguments.putString("save", s);

                        myFragment.setArguments(arguments);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
                    }
                });

                cart = mView.findViewById(R.id.btnAddCart);
                firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();
                current = firebaseAuth.getCurrentUser().getPhoneNumber();


                cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartProduct products = new cartProduct(title, desc, quan, Image, Mrp, Rate, Discount, "1");
                        firebaseDatabase.child("Cart").child(current).child(title).setValue(products);
                        Toast.makeText(itemView.getContext(), "Product Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void setImage(String image) {
                Image = image;
                ImageView imageView = mView.findViewById(R.id.imageView);
                Glide.with(mView.getContext()).load(image).into(imageView);
            }

            public void setName(String name) {
                title = name;
                TextView tvPrintTitle = mView.findViewById(R.id.textViewTitle);
                tvPrintTitle.setText(name);
            }

            public void setDescription(String description) {
                desc = description;
            }

            public void setQuantity(String quantity) {
                quan = quantity;
                TextView tvSd = mView.findViewById(R.id.textViewShortDesc);
                tvSd.setText(quantity);
            }

            public void setMrp(String mrp) {
                Mrp = mrp;
                m = Integer.parseInt(mrp);
                TextView tvM = mView.findViewById(R.id.textViewMRP);
                tvM.setText("\u20B9 " + mrp);
            }

            public void setRate(String rate) {
                Rate = rate;
                r = Integer.parseInt(rate);
                TextView tvR = mView.findViewById(R.id.textViewPrice);
                tvR.setText("\u20B9 " + rate);
            }

            public void setDiscount(String discount) {
                Discount = discount;
                TextView tvR = mView.findViewById(R.id.textViewDiscount);
                tvR.setText("You Save \u20B9" + (m - r) + " (" + discount + "%)");

                double d = Double.parseDouble(discount);
                int s = (int) d;
                TextView dis = mView.findViewById(R.id.tvDiscount);
                dis.setText(s+"%\noff");
            }
        }
    }
