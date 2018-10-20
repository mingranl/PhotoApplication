package com.example.misaka.photoapplication.Share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.misaka.photoapplication.Home.HomeActivity;
import com.example.misaka.photoapplication.Profile.ProfileActivity;
import com.example.misaka.photoapplication.Search.SearchUserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.misaka.photoapplication.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessActivity extends AppCompatActivity {

    private static final String TAG = "ProcessActivity";

    //vars
    private String mAppend = "file:/";
    private int imageCount = 0;
    private String imgUrl;
    private Bitmap photo;
    private Intent intent;
    private double mPhotoUploadProgress = 0;
    private String context;
    private Context mContext;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    private StorageReference imgs;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        Log.d(TAG, "onCreate: started.");

        mContext = ProcessActivity.this;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        long currentTime=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date=new Date(currentTime);
        String filename=format.format(date);
        String imagePath = "imgs/"+user.getUid() + "/Moments/"+filename+".jpg";
        imgs = mStorageReference.child(imagePath);

        intent = getIntent();
        context = intent.getStringExtra("context");
        photo = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));

        redirect(context);
        Toast.makeText(ProcessActivity.this, "Attempting to upload new photo", Toast.LENGTH_SHORT).show();
        uploadNewPhoto();


    }

    public void uploadNewPhoto(){
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod new photo.");

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgs.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d(TAG, "onFailure: Photo upload failed.");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                if(progress - 15 > mPhotoUploadProgress){
                    Toast.makeText(ProcessActivity.this, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                    mPhotoUploadProgress = progress;
                }

                Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
            }
        });;
    }

    private void redirect(String context){
        switch (context){
            case ShareActivity.HOME_ACTIVITY:
                Intent intent1 = new Intent(ProcessActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;

            case ShareActivity.SEARCH_ACTIVITY:
                Intent intent2 = new Intent(ProcessActivity.this, ShareActivity.class);
                startActivity(intent2);
                break;

//                    case NOTIFY_ACTIVITY:
//                        Intent intent3 = new Intent(ShareActivity.this, LoginActivity.class);
//                        startActivity(intent3);
//                        break;

            case ShareActivity.PROFILE_ACTIVITY:
                Intent intent4 = new Intent(ProcessActivity.this, ProfileActivity.class);
                startActivity(intent4);
                break;
        }
    }
}
