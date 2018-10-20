//package com.example.misaka.photoapplication.Share;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import com.example.misaka.photoapplication.Home.HomeActivity;
//import com.example.misaka.photoapplication.Login.LoginActivity;
//import com.example.misaka.photoapplication.Profile.ProfileActivity;
//import com.example.misaka.photoapplication.R;
//import com.example.misaka.photoapplication.Search.SearchUserActivity;
//import com.example.misaka.photoapplication.Util.NavigationBarActivate;
//
//import com.blankj.utilcode.constant.PermissionConstants;
//import com.blankj.utilcode.util.LogUtils;
//import com.blankj.utilcode.util.PermissionUtils;
//import com.blankj.utilcode.util.ScreenUtils;
//import com.blankj.utilcode.util.SpanUtils;
//
//import java.util.List;
//
//public class ShareActivity extends AppCompatActivity {
//    private static final String TAG = "ShareActivity";
//    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_share);
//
//        Log.d(TAG, "onCreate: Holy Shit.");
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_CONTACTS)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_CONTACTS},
//                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        }
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
////        if(checkPermissions(PERMISSIONS)){
////            setupViewPager();
//        Log.d(TAG, "onStart: Start.");
////        }
//    }
//}