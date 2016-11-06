package com.example.jaikh.trubian;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewDebug.IntToString;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MainPage extends AppCompatActivity {
    private FirebaseUser mUser;
    private TextView user_name_tv;
    private ImageView user_picture_iv;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //Navigation Bar Toggle settings
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fetching details of Authenticated user
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        //setting up selected item listener
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        itemClicked(menuItem.toString());
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // We can now look up items within the header if needed
        user_name_tv = (TextView) headerLayout.findViewById(R.id.user_name_tv);
        user_picture_iv = (ImageView) headerLayout.findViewById(R.id.user_picture_iv);
        displayUserDetails();
    }

    //Method that binds toggle button to the navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayUserDetails()
    {
        user_name_tv.setText(mUser.getDisplayName());
        Picasso.with(getApplicationContext())
                .load(mUser.getPhotoUrl())
                .into(user_picture_iv);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void itemClicked(String selected)
    {
      switch(selected)
      {
          case "Log Out":
              FirebaseAuth.getInstance().signOut();
              Toast.makeText(this, "Log Out successful", Toast.LENGTH_SHORT).show();
              startActivity(new Intent(MainPage.this,MainActivity.class));
              /*Intent i = getBaseContext().getPackageManager()
                      .getLaunchIntentForPackage( getBaseContext().getPackageName() );
              i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(i);*/
              break;
          case "Academic Calender":
              AcademicCalenderFragment academicCalenderFragment = new AcademicCalenderFragment();
              getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, academicCalenderFragment).commit();
              break;
      }
    }
}
