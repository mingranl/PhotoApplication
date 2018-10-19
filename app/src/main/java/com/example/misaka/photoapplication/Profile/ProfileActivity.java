package com.example.misaka.photoapplication.Profile;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.misaka.photoapplication.R;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
                Log.d(TAG, "onCreate: started.");

        init();
    }

    // initialize Profile activity
    private void init(){

        ProfileFragment fragment = new ProfileFragment();
        String str = "Profile";
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(str);
        transaction.commit();


        //fragmentTransaction(fragment, str);
    }


    // function to manage transaction of fragments
    public void fragmentTransaction (Fragment fragment, String str) {
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(str);
        transaction.commit();
    }
}
