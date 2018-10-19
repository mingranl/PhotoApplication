package com.example.misaka.photoapplication.Profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Util.NavigationBarActivate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    //widgets
    private TextView accPosts, accFollowers, accFollowing, userName, infoName;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Context context;

    //vars
    private int accPostsNum = 0;
    private int accFollowersNum = 0;
    private int accFollowingNum = 0;

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
        userName = (TextView) view.findViewById(R.id.profileUserName);
        infoName = (TextView) view.findViewById(R.id.display_name);
        accPosts = (TextView) view.findViewById(R.id.tvPosts);
        accFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        accFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        bottomNavigationViewEx = (BottomNavigationViewEx) view.findViewById(R.id.bottomNaviBar);
        context = getActivity();

        setupBottomNavigationView();

        return view;
    }

    /**
     * BottomNavigationView
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.setTextVisibility(false);
        NavigationBarActivate.activateNavi(context, getActivity(), bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(SWITCH_LABEL);
        menuItem.setChecked(true);
    }

}
