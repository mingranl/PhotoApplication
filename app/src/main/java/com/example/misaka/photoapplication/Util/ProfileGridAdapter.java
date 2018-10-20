package com.example.misaka.photoapplication.Util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.misaka.photoapplication.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfileGridAdapter extends ArrayAdapter<String> {
    private static final String TAG = "ProfileGridAdapter";

    private Context context;
    private int resource_id;
    private ArrayList<String> imgUrl = new ArrayList<String>();

    //firebase storage reference for img download
    private static final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private static final StorageReference storageReference = firebaseStorage.getReference();

    public ProfileGridAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        Log.d(TAG, "constructing");
        this.context = context;
        this.resource_id = resource;
        this.imgUrl = objects;
        Log.d(TAG, "img url is: " + imgUrl.toString());
    }

    private class ViewHolder {
        ImageView imageView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.d(TAG, "getView method");
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource_id, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.profile_post_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String url = imgUrl.get(position);
        StorageReference photoRef = storageReference.child(url);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(photoRef)
                .into(holder.imageView);

        return convertView;
    }
}
