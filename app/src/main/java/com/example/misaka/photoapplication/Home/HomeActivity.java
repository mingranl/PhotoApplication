package com.example.misaka.photoapplication.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.misaka.photoapplication.Login.LoginActivity;
import com.example.misaka.photoapplication.Model.FeedItem;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Util.NavigationBarActivate;

import com.example.misaka.photoapplication.Util.UserFeedAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

/**
 * Home Activity
 * Process operations in home page
 */
public class HomeActivity extends AppCompatActivity {

    //used for log output
    private static final String TAG = "HomeActivity";
    //bottom navi bar label
    private static final int SWITCH_LABEL = 0;

    //home activity context
    private Context mContext = HomeActivity.this;

    //user feed title
    private TextView mTextView=null;
    //user feed list
    private ListView mListView=null;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //database reference
    private static final DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference();

    //feedItem list, comment list, like list
    private static List<FeedItem> feedList=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting.");
        mTextView =(TextView) findViewById(R.id.feed_title);
        mListView =(ListView) findViewById(R.id.feed_list);

        mAuth = FirebaseAuth.getInstance();

        setupBottomNavigationView();
        getUserFeeds();
    }

    /**
     * get user feeds
     */
    public void getUserFeeds(){

        feedList=new ArrayList<FeedItem>();

        Query query=dbReference.child("userfeeds").orderByChild("feed_id");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found userFeeds:" + ds.getValue(FeedItem.class).toString());

                    feedList.add(ds.getValue(FeedItem.class));
                    mListView.setAdapter(new UserFeedAdapter(mContext,R.layout.layout_userfeed_item,feedList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        NavigationBarActivate.activateNavi(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(SWITCH_LABEL);
        menuItem.setChecked(true);
    }

    /**
     * Update UI if is not signed in
     * @param user
     */
    private void updateUI(FirebaseUser user){
        if(user==null){
            Log.d(TAG, "onComplete: success.");
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }
}
