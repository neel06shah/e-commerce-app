package com.example.shreesaisugandhi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.shreesaisugandhi.ui.cart.CartFragment;
import com.example.shreesaisugandhi.ui.home.HomeFragment;
import com.example.shreesaisugandhi.ui.orders.OrderFragment;
import com.example.shreesaisugandhi.ui.profile.ProfileFragment;
import com.example.shreesaisugandhi.ui.wish.WishList;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Dash extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    TextView tvDashName, tvDashType;
    FirebaseAuth firebaseAuth;
    DatabaseReference dataref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();
        dataref = FirebaseDatabase.getInstance().getReference("Users");

        NavigationView navigationView = findViewById(R.id.nav_view);
        final View headerView = navigationView.getHeaderView(0);

        tvDashName = headerView.findViewById(R.id.UpName);
        tvDashType = headerView.findViewById(R.id.UpMobile);


        String email = firebaseAuth.getCurrentUser().getPhoneNumber();
        String ph = email.replace("+91","");
        tvDashType.setText(ph);

        dataref.child(ph).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                tvDashName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dash.this, "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);

        MenuItem menuItem;
        menuItem = menu.findItem(R.id.action_cart);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new CartFragment()).commit();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_orders:
                //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        //new OrderFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_setting:
                Intent k = new Intent(Dash.this, MainActivity.class);
                startActivity(k);
                finish();
                break;

            case R.id.nav_call:
                String phone = "+919136774455";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;

            case R.id.nav_call2:
                String phone2 = "+917666855031";
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone2, null));
                startActivity(intent2);
                break;

            case R.id.nav_send :
                Intent j = new Intent(Intent.ACTION_VIEW);
                j.setData(Uri.parse("http://"+"wa.me/+919136774455"));
                startActivity(j);
                break;

            case R.id.nav_send2 :
                Intent s = new Intent(Intent.ACTION_VIEW);
                s.setData(Uri.parse("http://"+"wa.me/+917666855031"));
                startActivity(s);
                break;

            case  R.id.nav_cart :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new CartFragment()).commit();
                break;
            case R.id.nav_wish :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new WishList()).commit();
                break;


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
    /*DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth firebaseAuth;
    DatabaseReference dataref;
    TextView tvDashName, tvDashEmail, tvDashType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        final View headerView = navigationView.getHeaderView(0);

        tvDashName = headerView.findViewById(R.id.UpName);
        tvDashEmail = headerView.findViewById(R.id.UpEmail);
        tvDashType = headerView.findViewById(R.id.UpMobile);

        firebaseAuth = FirebaseAuth.getInstance();
        dataref = FirebaseDatabase.getInstance().getReference("Users");

        // Passing each menu ID as a set of Ids because each

        String email = firebaseAuth.getCurrentUser().getEmail();
        String em = email.replace(".", "_dot_");
        tvDashEmail.setText(email);

        dataref.child(em).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String type = dataSnapshot.child("phone").getValue().toString();

                tvDashName.setText(name);
                tvDashType.setText(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dash.this, "Invalid Login", Toast.LENGTH_SHORT).show();
            }
        });

        // menu should be considered as top level destinations.
        drawer = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);

        MenuItem menuItem;
        menuItem = menu.findItem(R.id.action_cart);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new CartFragment()).commit();
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new OrderFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_setting:
                Intent k = new Intent(Dash.this, MainActivity.class);
                startActivity(k);
                finish();
                break;

            case R.id.nav_call:
                String phone = "+919136774455";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                break;

            case R.id.nav_call2:
                String phone2 = "+917666855031";
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone2, null));
                startActivity(intent2);
                break;

            case R.id.nav_send :
                Intent j = new Intent(Intent.ACTION_VIEW);
                j.setData(Uri.parse("http://"+"wa.me/+919136774455"));
                startActivity(j);
                break;

            case R.id.nav_send2 :
                Intent s = new Intent(Intent.ACTION_VIEW);
                s.setData(Uri.parse("http://"+"wa.me/+917666855031"));
                startActivity(s);
                break;

            case  R.id.nav_cart :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        new CartFragment()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}*/