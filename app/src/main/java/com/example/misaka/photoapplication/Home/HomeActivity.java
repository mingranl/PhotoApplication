package com.example.misaka.photoapplication.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.misaka.photoapplication.Login.LoginActivity;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Profile.ProfileActivity;
import com.example.misaka.photoapplication.Search.SearchUserActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private static final int HOME_FRAGMENT = 1;
    private static final int RESULT_ADD_NEW_STORY = 7891;
    private final static int CAMERA_RQ = 6969;
    private static final int REQUEST_ADD_NEW_STORY = 8719;

    private Context mContext = HomeActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    private ViewPager mViewPager;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);

        mAuth = FirebaseAuth.getInstance();

        setupBottomNavigationView();

    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());
    }

    /**
     * BottomNavigationView
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNaviBar);
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.setTextVisibility(false);
        activateNavi(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /**
     * Activate navigation to different activities
     * @param context
     * @param callingActivity
     * @param view
     */
    public static void activateNavi(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nb_moments:
                        Intent intent1 = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                        context.startActivity(intent1);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.nb_search:
                        Intent intent2  = new Intent(context, SearchUserActivity.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

//                    case R.id.nb_share:
//                        Intent intent3 = new Intent(context, ShareActivity.class);//ACTIVITY_NUM = 2
//                        context.startActivity(intent3);
////                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                        break;
//
//                    case R.id.nb_notification:
//                        Intent intent4 = new Intent(context, LikesActivity.class);//ACTIVITY_NUM = 3
//                        context.startActivity(intent4);
////                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                        break;

                    case R.id.nb_profile:
                        Intent intent5 = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 4
                        context.startActivity(intent5);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }


                return false;
            }
        });
    }

    // if user has signed in then redirect to home page
    private void updateUI(FirebaseUser user){
        if(user==null){
            Log.d(TAG, "onComplete: success.");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }
}
