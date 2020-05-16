package com.example.shreesaisugandhi.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.category;
import com.example.shreesaisugandhi.ui.product.ProductsFragment;
import com.example.shreesaisugandhi.ui.search.SearchFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

    public HomeFragment() { }
    ViewFlipper viewFlipper;
    Button agarbatti;
    private RecyclerView list_view;
    private DatabaseReference reference;
    int gallery_grid_Images[] = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3,
            R.drawable.slide4,R.drawable.slide5};
    EditText etSearch;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewFlipper=view.findViewById(R.id.viewFlipper);

        etSearch=view.findViewById(R.id.etSearch);
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new SearchFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();

                return false;
            }
        });

        for(int i=0; i<gallery_grid_Images.length; i++){
            // This will create dynamic image views and add them to the ViewFlipper.
            setFlipperImage(gallery_grid_Images[i]);
        }

        reference= FirebaseDatabase.getInstance().getReference().child("Category");
        reference.keepSynced(true);

        list_view=view.findViewById(R.id.gridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        list_view.setLayoutManager(gridLayoutManager);
        return view;
    }
    private void setFlipperImage(int res) {
        ImageView image = new ImageView(getContext());
        image.setBackgroundResource(res);
        viewFlipper.addView(image);
        viewFlipper.setFlipInterval(2500);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(getContext(),R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(),R.anim.slide_out_right);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<category, categoryViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter <category, categoryViewHolder>
                (category.class,R.layout.grid_item, categoryViewHolder.class,reference) {
            @Override
            protected void populateViewHolder(categoryViewHolder categoryViewHolder, category category, int i) {
                categoryViewHolder.setTitle(category.getTitle());
                categoryViewHolder.setImage(category.getImage());
                categoryViewHolder.setDescription(category.getDescription());
            }
        };
        list_view.setAdapter(firebaseRecyclerAdapter);
    }

    public static class categoryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        String tv;

        public categoryViewHolder(final View itemView) {
            super(itemView);
            mView=itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new ProductsFragment();

                    Bundle arguments = new Bundle();
                    arguments.putString("title", tv);

                    myFragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
                }
            });
        }

        public void setImage(String image) {
            ImageView imageView = mView.findViewById(R.id.ivCategory);
            Picasso.get().load(image).fit().into(imageView);
        }

        public void setTitle(String title) {
            tv=title;
            TextView Title = mView.findViewById(R.id.tvCategory);
            Title.setText(title);
        }
        public void setDescription (String description) {
            TextView Title = mView.findViewById(R.id.tvDescription);
            Title.setText(description);
        }
    }
}