package com.rainbow.teacheriiy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.rainbow.teacheriiy.ui.LeaveRequests.StudentsLeaveRequests;
import com.rainbow.teacheriiy.ui.TeacherAttendance.TeacherAttendanceActivity;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    private SharedPreferences sharedPreferences;
    private String uid, namee;
    private String teacherId;
    private DrawerLayout drawer;

    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        uid = sharedPreferences.getString("id","NULL");
        namee = sharedPreferences.getString("name","NOT FOUND");

        if (uid.equals("NULL") && namee.equals("NOT FOUND")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String id = prefs.getString("id" , null);
            teacherId = "T"+id;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)){
            drawer.closeDrawer(Gravity.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_students_leave_requests:{
                startActivity(new Intent(MainActivity.this, StudentsLeaveRequests.class));
                break;
            }
            case R.id.action_teacher_calendar: {
                startActivity(new Intent(MainActivity.this, TeacherAttendanceActivity.class));
                break;
            }
        }
        return true;
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }
    public void showActionBar(){
        getSupportActionBar().show();
    }
}
