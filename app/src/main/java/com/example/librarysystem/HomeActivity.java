package com.example.librarysystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar hometoolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String usertype;
    boolean isLogin;
    LoadingDialog loadingDialog;
    private static final String SHARE_PREF = "sharePref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadingDialog = new LoadingDialog(this);
        sharedPreferences = getSharedPreferences(SHARE_PREF,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        usertype = sharedPreferences.getString("usertype","normaluser");
        System.out.println("usertype in home: "+usertype);

        hometoolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(hometoolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_bar_menu);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,hometoolbar,R.string.open_nav_bar,R.string.close_nav_bar);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SearchBookFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_item_home);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_item_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SearchBookFragment()).commit();
                break;
            case R.id.nav_item_branches:
                Intent intent = new Intent(HomeActivity.this,BranchesMapActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_item_uploadbook:
                if(!usertype.equals("manager")){
                    loadingDialog.setCancelable();
                    loadingDialog.setTitle(getString(R.string.alert_not_manager));
                    loadingDialog.startloadingDialog();
                    break;
                }
                Intent intent_uploadbook = new Intent(HomeActivity.this, UpLoadBooksActivity.class);
                startActivity(intent_uploadbook);
                break;
            case R.id.nav_item_shakebook:
                Intent intent_shakeBook = new Intent(HomeActivity.this,ShakeBookActivity.class);
                startActivity(intent_shakeBook);
                break;
            case R.id.nav_item_mybooks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyBookFragment()).commit();
                break;
            case R.id.nva_item_logout:
                editor.putBoolean("isLogin",false);
                editor.apply();
                Intent intent_main = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent_main);
                finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
