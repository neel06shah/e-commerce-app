package com.example.shreesaisugandhi.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shreesaisugandhi.adapters.AdapterClass;
import com.example.shreesaisugandhi.R;
import com.example.shreesaisugandhi.products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView list_view;
    private Query myRef;
    androidx.appcompat.widget.SearchView searchView;
    ArrayList<products> list;
    public SearchFragment() { }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        myRef=FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("activity").equalTo(true);

        list_view = view.findViewById(R.id.listView);
        searchView = view.findViewById(R.id.searchView);
        searchView.setIconified(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        list_view.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(myRef != null){
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                           list.add(ds.getValue(products.class));
                        }
                        AdapterClass adapterClass = new AdapterClass(list);
                        list_view.setAdapter(adapterClass);
                    }
                    if(searchView != null){
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                search(newText);
                                return true;
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void search(String s) {
        ArrayList<products> myList = new ArrayList();
        for (products object : list) {
            if(object.getName().toLowerCase().contains(s.toLowerCase()) || object.getDescription().toLowerCase().contains(s.toLowerCase()) || object.getRate().toLowerCase().contains(s.toLowerCase()) || object.getCategory().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(myList);
        list_view.setAdapter(adapterClass);
    }

}