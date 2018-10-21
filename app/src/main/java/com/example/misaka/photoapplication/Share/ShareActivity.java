package com.example.misaka.photoapplication.Share;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.misaka.photoapplication.Home.HomeActivity;
import com.example.misaka.photoapplication.Profile.ProfileActivity;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Search.SearchUserActivity;

/**
 * ShareActivity
 * Implement share relevant methods
 */
public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "ShareActivity";

    private static final int ACTIVITY_NUM = 2;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 22;

    private ViewPager mViewPager;

    private Context mContext = ShareActivity.this;

    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private boolean ifPermissionGranted = true;

    public static final String HOME_ACTIVITY = "class com.example.misaka.photoapplication.Home.HomeActivity";
    public static final String SEARCH_ACTIVITY = "class com.example.misaka.photoapplication.Search.SearchUserActivity";
    //    public static final String NOTIFY_ACTIVITY = "class com.example.misaka.photoapplication.Home.HomeActivity";
    public static final String PROFILE_ACTIVITY = "class com.example.misaka.photoapplication.Profile.ProfileActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");

        if(checkPermissions(PERMISSIONS)){
            setupViewPager();
            Log.d(TAG, "checkPermissions: Succeed.");
        }else{
            verifyPermissions(PERMISSIONS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(checkPermissions(PERMISSIONS)){
            Log.d(TAG, "onStart: Start.");
        }
    }

    /**
     * setup viewpager for manager the tabs
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter =  new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());

        mViewPager = (ViewPager) findViewById(R.id.share_viewpager_container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.share_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.photo));

    }

    /**
     * return the current tab number
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * @return
     */
    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    /**
     * Verify permissions
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                this,
                permissions,
                PERMISSIONS_REQUEST_CODE
        );
    }

    /**
     * Check permissions granted
     * @param permissions
     * @return Boolean
     */
    public boolean checkPermissions(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String permission = permissions[i];
            if(!checkPermission(permission)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a permission has been granted
     * @param permission
     * @return Boolean
     */
    public boolean checkPermission(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ContextCompat.checkSelfPermission(ShareActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // if granted
                    Log.d(TAG, "grantResults: PERMISSION_GRANTED");
                    afterVerification(true);
                } else {
                    // if not granted
                    Log.d(TAG, "grantResults: PERMISSION_NOT_GRANTED");
                    afterVerification(false);
                }
            }
        }
    }

    /**
     * Back to the caller activity if permissions be denied
     * @param result
     */
    private void afterVerification(boolean result){
        if(!result){
            redirect();
        }
        else{

        }
    }

    /**
     * Redirect to caller activity
     */
    private void redirect(){
        String caller = getIntent().getStringExtra("caller");
        switch (caller){
            case HOME_ACTIVITY:
                Intent intent1 = new Intent(ShareActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;

            case SEARCH_ACTIVITY:
                Intent intent2 = new Intent(ShareActivity.this, SearchUserActivity.class);
                startActivity(intent2);
                break;

//                    case NOTIFY_ACTIVITY:
//                        Intent intent3 = new Intent(ShareActivity.this, LoginActivity.class);
//                        startActivity(intent3);
//                        break;

            case PROFILE_ACTIVITY:
                Intent intent4 = new Intent(ShareActivity.this, ProfileActivity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
