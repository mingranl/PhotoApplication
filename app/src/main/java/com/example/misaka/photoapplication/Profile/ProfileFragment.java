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
import android.widget.TextView;

import com.example.misaka.photoapplication.Login.LoginActivity;
import com.example.misaka.photoapplication.Model.AccountInfo;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Util.NavigationBarActivate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    //result users list
    private List<AccountInfo> accList = null;

    //widgets
    private TextView accPosts, accFollowers, accFollowing, userName, infoName;
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
        userName = (TextView) view.findViewById(R.id.profileUserName);
        infoName = (TextView) view.findViewById(R.id.display_name);
        accPosts = (TextView) view.findViewById(R.id.tvPosts);
        accFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        accFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        bottomNavigationViewEx = (BottomNavigationViewEx) view.findViewById(R.id.bottomNaviBar);
        context = getActivity();
        accList = new ArrayList<AccountInfo>();

        // call method to setup BottomNavigationBar
        setupBottomNavigationView();

        // call method to get AccountInfo from firebase
        getAccountInfo();

//        userName.setText(String.valueOf(accList.get(0).getUsername()));
//        infoName.setText(String.valueOf(accList.get(0).getUsername()));
//        accPosts.setText(String.valueOf(accList.get(0).getPosts()));
//        accFollowers.setText(String.valueOf(accList.get(0).getFollowers()));
//        accFollowing.setText(String.valueOf(accList.get(0).getFollowing()));


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
    private void getAccountInfo(){
        accList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("account_info").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleUser: dataSnapshot.getChildren()){
                    accList.add(singleUser.getValue(AccountInfo.class));
                    Log.d(TAG, "acc info is: " + accList.get(0).toString());
                    userName.setText(String.valueOf(accList.get(0).getUsername()));
                    infoName.setText(String.valueOf(accList.get(0).getUsername()));
                    accPosts.setText(String.valueOf(accList.get(0).getPosts()));
                    accFollowers.setText(String.valueOf(accList.get(0).getFollowers()));
                    accFollowing.setText(String.valueOf(accList.get(0).getFollowing()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
