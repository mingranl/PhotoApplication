package com.example.misaka.photoapplication.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.misaka.photoapplication.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final Boolean CHECK_IF_VERIFIED = false;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail, mPassword;
    private View mLoginFormView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // view
        mProgressBar = (ProgressBar) findViewById(R.id.login_progress);
        mLoginFormView = (View) findViewById(R.id.login_form);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mContext = LoginActivity.this;
        // Log.d(TAG, "onCreate: started.");

        // hide waiting elements
        mLoginFormView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        setupFirebaseAuth();
        init();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    // if user has signed in then redirect to home page
    private void updateUI(FirebaseUser currentUser){

    }

    private void signIn(){

    }

}
