package com.example.misaka.photoapplication.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.misaka.photoapplication.Model.AccountInfo;
import com.example.misaka.photoapplication.Model.User;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Util.NavigationBarActivate;
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

public class ViewProfileFragment extends Fragment {
    private static final String TAG = "ViewProfileFragment";

    //bottom navi bar label
    private static final int SWITCH_LABEL = 4;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;

    private static final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private static final StorageReference storageReference = firebaseStorage.getReference();

    //result users list
    private List<AccountInfo> accList = null;

    //widgets
    private TextView vPosts, vFollowers, vFollowing, vUserName, vInfoName, vDes, vEmail;
    private ImageView vImg;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Context context;

    //variables
    private User user;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        vPosts = view.findViewById(R.id.tvViewPosts);
        vFollowers = view.findViewById(R.id.tvViewFollowers);
        vFollowing = view.findViewById(R.id.tvViewFollowing);
        vUserName = view.findViewById(R.id.profileViewUserName);
        vInfoName = view.findViewById(R.id.display_view_name);
        vDes = view.findViewById(R.id.view_description);
        vEmail = view.findViewById(R.id.display_view_email);
        vImg = view.findViewById(R.id.userViewProfilePhoto);
        bottomNavigationViewEx = view.findViewById(R.id.bottomNaviBar);
        context = getActivity();
        accList = new ArrayList<AccountInfo>();

        user = getUserFromBundle();
        initWidgets();

        setupBottomNavigationView();

        return view;
    }

    // get user from bundle
    private User getUserFromBundle(){
        Bundle bundle = this.getArguments();
        return (User) bundle.getSerializable("intent_user");
    }

    private void initWidgets(){
        Query query = dbReference.child("account_info").orderByChild("user_id").equalTo(user.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleUser: dataSnapshot.getChildren()){
                    accList.add(singleUser.getValue(AccountInfo.class));
                    Log.d(TAG, "acc info is: " + accList.get(0).toString());
                    vUserName.setText(String.valueOf(accList.get(0).getUsername()));
                    vInfoName.setText(String.valueOf(accList.get(0).getUsername()));
                    vPosts.setText(String.valueOf(accList.get(0).getPosts()));
                    vFollowers.setText(String.valueOf(accList.get(0).getFollowers()));
                    vFollowing.setText(String.valueOf(accList.get(0).getFollowing()));
                    vDes.setText(String.valueOf(accList.get(0).getDescription()));
                    vEmail.setText(String.valueOf(accList.get(0).getEmail()));

                    String imgPath = accList.get(0).getImg();
                    StorageReference photoRef = storageReference.child(imgPath);
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(photoRef)
                            .into(vImg);
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
