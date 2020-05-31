package com.example.shreesaisugandhi.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.category;
import com.example.shreesaisugandhi.ui.product.ProductsFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeFragment() { }
    ViewFlipper viewFlipper;
    private RecyclerView list_view;
    private DatabaseReference reference, images;
    String gallery_grid_Images[];
    ArrayList<String> list;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);


        progressBar = view.findViewById(R.id.progressBar);
        linearLayout = view.findViewById(R.id.linearLayout);

        viewFlipper=view.findViewById(R.id.viewFlipper);
        images = FirebaseDatabase.getInstance().getReference().child("Slides");
        images.keepSynced(true);

        images.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    list.add(child.getValue(String.class));
                }
                for (int i =0; i < list.size(); i++) {
                    String downloadImageUrl = list.get(i);
                    setFlipperImage(downloadImageUrl);
                }
                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference= FirebaseDatabase.getInstance().getReference().child("Category");
        reference.keepSynced(true);

        list_view=view.findViewById(R.id.gridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        list_view.setLayoutManager(gridLayoutManager);
        return view;
    }
    private void setFlipperImage(String res) {
        ImageView image = new ImageView(getContext());
        Glide.with(getContext()).load(res).into(image);

        viewFlipper.addView(image);

        viewFlipper.setFlipInterval(2500);
        viewFlipper.setAutoStart(true);

        viewFlipper.startFlipping();
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
            Glide.with(mView.getContext()).load(image).into(imageView);
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