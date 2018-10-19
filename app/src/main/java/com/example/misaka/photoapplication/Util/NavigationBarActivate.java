package com.example.misaka.photoapplication.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.misaka.photoapplication.Home.HomeActivity;
import com.example.misaka.photoapplication.Profile.ProfileActivity;
import com.example.misaka.photoapplication.Share.ShareActivity;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Search.SearchUserActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class NavigationBarActivate {
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
                        Intent intent1 = new Intent(context, HomeActivity.class);//SWITCH_LABEL = 0
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.nb_search:
                        Intent intent2  = new Intent(context, SearchUserActivity.class);//SWITCH_LABEL = 1
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.nb_share:
                        Intent intent3 = new Intent(context, ShareActivity.class);//SWITCH_LABEL = 2
                        intent3.putExtra("caller",context.getClass().toString());
                        context.startActivity(intent3);
//                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
//
//                    case R.id.nb_notification:
//                        Intent intent4 = new Intent(context, LikesActivity.class);//SWITCH_LABEL = 3
//                        context.startActivity(intent4);
////                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                        break;

                    case R.id.nb_profile:
                        Intent intent5 = new Intent(context, ProfileActivity.class);//SWITCH_LABEL = 4
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }


                return false;
            }
        });
    }
}
