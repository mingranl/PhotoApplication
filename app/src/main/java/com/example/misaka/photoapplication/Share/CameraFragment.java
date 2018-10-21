package com.example.misaka.photoapplication.Share;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.misaka.photoapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CameraFragment
 * Display share layout and process file
 */
public class CameraFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constant
    private static final int PHOTO_FRAGMENT_NUM = 0;
    private static final int  GALLERY_REQUEST_CODE = 21;
    private static final int  CAMERA_REQUEST_CODE = 22;

    //var
    private Button btnOpenCamera;
    private Button btnFileChoose;
    private ImageView cancel;

    //storage path for the photo
    private String mTempPhotoPath;
    //image's uri path
    private Uri imageUri;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private static final String CAMERA_PERMISSION =  Manifest.permission.CAMERA;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        Log.d(TAG, "onCreateView: started.");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        cancel = (ImageView)view.findViewById(R.id.ivCloseShare);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                getActivity().finish();
            }
        });
        btnFileChoose = (Button) view.findViewById(R.id.file_choose);
        btnFileChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: open gallery.");

                choosePhoto();
            }
        });
        btnOpenCamera = (Button) view.findViewById(R.id.open_camera);
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: open camera.");

                takePhoto();
            }
        });

        return view;
    }

    /**
     * Call the camera to take photo
     */
    private void takePhoto(){

        String authorities = "com.example.misaka.photoapplication";

        long currentTime=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        Date date=new Date(currentTime);
        String filename=format.format(date);

        if(((ShareActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM){
            if(((ShareActivity)getActivity()).checkPermission(CAMERA_PERMISSION)){
                Log.d(TAG, "takePhoto: starting camera");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                // save the image to this path
                mTempPhotoPath =  user.getUid()
                        + File.separator + filename
                        + ".jpg";

                File imagePath = new File(getActivity().getFilesDir(), "PhotoApplication/");
                File newFile = new File(imagePath, mTempPhotoPath);

                // get image's uri path
                imageUri = FileProvider.getUriForFile(getActivity(),
                        authorities,
                        newFile);

                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                // take photo and store
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, newFile.getAbsolutePath());
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            }else{
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    /**
     * Call the gallery to choose file
     */
    private void choosePhoto(){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        // add postfix to "image/" to limit upload image type
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "requestCode:"+requestCode);
        Log.d(TAG, "resultCode:"+resultCode);
        if(requestCode == CAMERA_REQUEST_CODE){
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");

            try{
                Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
                Intent intent = new Intent(getActivity(), ProcessActivity.class);
                intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                intent.putExtra("context", getActivity().getIntent().getStringExtra("caller"));
                startActivity(intent);
//                getActivity().finish();
            }catch (NullPointerException e){
                Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
            }
        }
        if(requestCode == GALLERY_REQUEST_CODE){
            try {
                Log.d(TAG,"GALLERY_REQUEST_CODE"+GALLERY_REQUEST_CODE);
//                Uri selectedImageUri = data.getData();
                if(data!=null) {
                    Uri selectedImageUri = data.getData();
                    String filestring = selectedImageUri.getPath();
//                    Bitmap bitmap = BitmapFactory.decodeFile(filestring);
//                    Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImageUri));

//                    Log.d(TAG,"selectedImageUri:"+selectedImageUri);
//                    Log.d(TAG,"filestring:"+filestring);
                    Intent intent = new Intent(getActivity(), ProcessActivity.class);
                    intent.putExtra(getString(R.string.selected_image), selectedImageUri);
                    intent.putExtra("context", getActivity().getIntent().getStringExtra("caller"));
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
