package com.example.misaka.photoapplication.Profile;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.misaka.photoapplication.Model.User;
import com.example.misaka.photoapplication.R;
import com.google.firebase.auth.FirebaseAuth;

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

        Intent intent = getIntent();
        if(intent.hasExtra("calledFromSearch")){ //from SearchActivity
            Log.d(TAG, "init: searching for user object attached as intent extra");
            if(intent.hasExtra("intent_user")){
                Log.d(TAG, "finding user info");
                User user = (User) intent.getSerializableExtra("intent_user");
                Log.d(TAG,user.toString());
                if(!user.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Log.d(TAG, "init: inflating view profile");
                    ViewProfileFragment fragment = new ViewProfileFragment();
                    Bundle args = new Bundle();
                    args.putParcelable("intent_user",
                            intent.getParcelableExtra("intent_user"));
                    fragment.setArguments(args);
                    String str = "ViewProfile";
                    fragmentTransaction(fragment, str);
                }else{
                    Log.d(TAG, "init: inflating Profile");
                    ProfileFragment fragment = new ProfileFragment();
                    String str = "Profile";
                    fragmentTransaction(fragment, str);
                }
            }
        }else{
            Log.d(TAG, "init: inflating Profile");
            ProfileFragment fragment = new ProfileFragment();
            String str = "Profile";
            fragmentTransaction(fragment, str);
        }
    }

    // function to manage transaction of fragments
    public void fragmentTransaction (Fragment fragment, String str) {
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(str);
        transaction.commit();
    }
}
