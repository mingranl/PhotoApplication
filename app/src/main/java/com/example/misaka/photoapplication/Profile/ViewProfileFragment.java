package com.example.misaka.photoapplication.Profile;

import android.content.Context;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ViewProfileFragment extends Fragment {
    private static final String TAG = "ViewProfileFragment";

    //widgets
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Context context;

    //bottom navi bar label
    private static final int SWITCH_LABEL = 4;

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

        bottomNavigationViewEx = (BottomNavigationViewEx) view.findViewById(R.id.bottomNaviBar);

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
