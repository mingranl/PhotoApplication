package com.example.misaka.photoapplication.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.misaka.photoapplication.Login.LoginActivity;
import com.example.misaka.photoapplication.Model.AccountInfo;
import com.example.misaka.photoapplication.Model.FeedItem;
import com.example.misaka.photoapplication.Model.User;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Util.NavigationBarActivate;
import com.example.misaka.photoapplication.Util.ProfileGridAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    private static final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private static final StorageReference storageReference = firebaseStorage.getReference();

    //result users list
    private List<AccountInfo> accList = null;
    //user_feed list
    private static List<FeedItem> feedList = null;

    //user_id and username
    private String user_id = null;
    private String currentUserName = null;

    //widgets
    private TextView accPosts, accFollowers, accFollowing, userName, infoName, accDes, accEmail;
    private GridView gridView;
    private ImageView accImg;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Context context;

    //bottom navi bar label
    private static final int SWITCH_LABEL = 4;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = view.findViewById(R.id.profileUserName);
        infoName = view.findViewById(R.id.display_name);
        accPosts = view.findViewById(R.id.tvPosts);
        accFollowers = view.findViewById(R.id.tvFollowers);
        accFollowing = view.findViewById(R.id.tvFollowing);
        accDes = view.findViewById(R.id.description);
        accEmail = view.findViewById(R.id.display_email);
        accImg = view.findViewById(R.id.userProfilePhoto);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNaviBar);
        gridView = view.findViewById(R.id.profileGridView);
        context = getActivity();
        accList = new ArrayList<AccountInfo>();

        // call method to setup BottomNavigationBar
        setupBottomNavigationView();

        // call method to get AccountInfo from firebase
        getAccountInfo();

        // call method to setup GridView
        setupGridView();

        // log out
        Button logOut = (Button) view.findViewById(R.id.logout_btn);
        logOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    //get account info
    private void getAccountInfo() {
        //acquire current user_id
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        accList.clear();
        Query query = dbReference.child("account_info").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleUser : dataSnapshot.getChildren()) {
                    accList.add(singleUser.getValue(AccountInfo.class));
                    Log.d(TAG, "acc info is: " + accList.get(0).toString());
                    userName.setText(String.valueOf(accList.get(0).getUsername()));
                    infoName.setText(String.valueOf(accList.get(0).getUsername()));
                    accPosts.setText(String.valueOf(accList.get(0).getPosts()));
                    accFollowers.setText(String.valueOf(accList.get(0).getFollowers()));
                    accFollowing.setText(String.valueOf(accList.get(0).getFollowing()));
                    accDes.setText(String.valueOf(accList.get(0).getDescription()));
                    accEmail.setText(String.valueOf(accList.get(0).getEmail()));

                    String imgPath = accList.get(0).getImg();
                    StorageReference photoRef = storageReference.child(imgPath);
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(photoRef)
                            .into(accImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //setup user posts view
    private void setupGridView() {
        //initialize feed list
        feedList = new ArrayList<FeedItem>();

        Query query = dbReference.child("userfeeds").orderByChild("feed_id");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: found userFeeds:" + ds.getValue(FeedItem.class).toString());
                    feedList.add(ds.getValue(FeedItem.class));
                }
                //get feeds of current user
                ArrayList<String> imgUrls = new ArrayList<String>();
                Log.d(TAG, "current username is (in gridView query 2): " + currentUserName);
                for (int i = 0; i < feedList.size(); i++) {
                    if (feedList.get(i).getUsername().equals("misaka")) {
                        imgUrls.add(feedList.get(i).getImg());
                    }
                }
                Log.d(TAG, "image list is: " + imgUrls.toString());
                gridView.setAdapter(new ProfileGridAdapter(context, R.layout.layout_profile_grid_item, imgUrls));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * BottomNavigationView
     */
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.setTextVisibility(false);
        NavigationBarActivate.activateNavi(context, getActivity(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(SWITCH_LABEL);
        menuItem.setChecked(true);
    }

}
