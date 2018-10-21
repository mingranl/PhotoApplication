package com.example.misaka.photoapplication.Share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.net.Uri;

import com.example.misaka.photoapplication.Home.HomeActivity;
import com.example.misaka.photoapplication.Model.User;
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
import java.util.UUID;
import java.io.File;

public class ProcessActivity extends AppCompatActivity {

    private static final String TAG = "ProcessActivity";

    private final int FILE_URI = 1;
    private final int FILE_BITMAP = 2;

    //view
    private Button post;
    private EditText description;

    //vars
    private String mAppend = "file:/";
    private int imageCount = 0;
    private String imgUrl;
    private Uri uri;
    private Bitmap photo;
    private Intent intent;
    private double mPhotoUploadProgress = 0;
    private String context;
    private Context mContext;
    private int file_type = -1;

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

        post = (Button) findViewById(R.id.post_photo);
        description = (EditText) findViewById(R.id.description);

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
        final String imagePath = "imgs/"+user.getUid() + "/Moments/"+filename+".jpg";
        imgs = mStorageReference.child(imagePath);

        intent = getIntent();
        context = intent.getStringExtra("context");
        if(intent.hasExtra(getString(R.string.selected_image))){
            uri = (Uri) intent.getParcelableExtra(getString(R.string.selected_image));
            file_type = FILE_URI;
        }
        if(intent.hasExtra(getString(R.string.selected_bitmap))){
            photo = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
            file_type = FILE_BITMAP;
        }

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rDescription = description.getText().toString();
                writeUserFeed(user.getUid(),imagePath,rDescription);
                Toast.makeText(ProcessActivity.this, "Attempting to upload new photo", Toast.LENGTH_SHORT).show();
                uploadNewPhoto();
                redirect(context);
            }
        });
    }

    public void uploadNewPhoto(){
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod new photo.");

        if(file_type == FILE_BITMAP){
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
                    Toast.makeText(mContext, "Photo upload failed.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d(TAG, "onFailure: Photo upload succeed.");
                    Toast.makeText(mContext, "Photo upload succeed.", Toast.LENGTH_SHORT).show();
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
            });
        }
        if(file_type == FILE_URI){
            Log.d(TAG, "uri: " + uri);
            UploadTask uploadTask = imgs.putFile(uri);

//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
//            BitmapFactory.decodeFile(uri, options);
//            // 计算缩放比
//            options.inSampleSize = calculateInSampleSize(options, 480, 800);
//            // 完整解析图片返回bitmap
//            options.inJustDecodeBounds = false;
//            return BitmapFactory.decodeFile(filePath, options);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d(TAG, "onFailure: Photo upload failed.");
                    Toast.makeText(mContext, "Photo upload failed.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d(TAG, "onFailure: Photo upload succeed.");
                    Toast.makeText(mContext, "Photo upload succeed", Toast.LENGTH_SHORT).show();
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
            });
        }
    }

    private void writeToDatabase(String description,String imagePath,String username){
        String feed_id = UUID.randomUUID().toString();
        myRef.child("userfeeds").child(feed_id).child("description").setValue(description);
        myRef.child("userfeeds").child(feed_id).child("feed_id").setValue(feed_id);
        myRef.child("userfeeds").child(feed_id).child("username").setValue(username);
        myRef.child("userfeeds").child(feed_id).child("like_count").setValue(0);
        myRef.child("userfeeds").child(feed_id).child("img").setValue(imagePath);
        myRef.child("userfeeds").child(feed_id).child("current_like").setValue(false);
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

    /**
     * Check is @param username already exists in teh database
     * @param uuid
     * @param imagePath
     * @param description
     */
    private void writeUserFeed(final String uuid, final String imagePath, final String description) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("users")
                .orderByChild("user_id")
                .equalTo(uuid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        String userName = singleSnapshot.getValue(User.class).getUsername();
                        writeToDatabase(description,imagePath,userName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
