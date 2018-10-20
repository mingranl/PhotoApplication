package com.example.misaka.photoapplication.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.misaka.photoapplication.Home.HomeActivity;
import com.example.misaka.photoapplication.Model.Comment;
import com.example.misaka.photoapplication.Model.FeedItem;
import com.example.misaka.photoapplication.Model.Like;
import com.example.misaka.photoapplication.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UserFeedAdapter extends ArrayAdapter<FeedItem>{

    private Context mcontext=null;
    private int layoutResource=-1;
    private LayoutInflater mInflater=null;
    private ArrayList<FeedItem> feedlist=null;
    private ArrayList<Comment> commentlist=new ArrayList<Comment>();;
    private ArrayList<Like> likelist=null;
    private ViewHolder holder=null;
    boolean loginUserLike=false;

    //firebase storage reference for img download
    private static final FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private static final StorageReference storageReference= firebaseStorage.getReference();
    private static final DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference();


    public UserFeedAdapter(@NonNull Context context, int resource, @NonNull List<FeedItem> objects) {
        super(context, resource, objects);
        mcontext=context;
        layoutResource=resource;
        mInflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //LayoutInflater.from(mcontext);
        feedlist= (ArrayList<FeedItem>) objects;
    }

    private static  final class ViewHolder{
        TextView feed_username;
        ImageView feed_img;
        TextView description;
        ImageButton feed_like;
        ImageButton feed_comment;
        ListView users_comment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if(convertView==null){
            convertView=mInflater.inflate(layoutResource, parent, false);
            holder=new ViewHolder();
            holder.feed_username=convertView.findViewById(R.id.feed_username);
            holder.feed_img=convertView.findViewById(R.id.feed_img);
            holder.description=convertView.findViewById(R.id.feed_description);
            holder.feed_like=convertView.findViewById(R.id.feed_like);
            holder.feed_comment=convertView.findViewById(R.id.feed_comment);
            holder.users_comment=convertView.findViewById(R.id.users_comment);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        Log.d("UserFeedAdapter", "Username" + getItem(position).getUsername());

        Log.d("UserFeedAdapter", "Description" + getItem(position).getDescription());

        //set username
        holder.feed_username.setText(getItem(position).getUsername());
        //set description
        holder.description.setText(getItem(position).getDescription());

        //set image
        String imgPath=getItem(position).getImg();
        StorageReference photoRef=storageReference.child(imgPath);
        Glide.with(mcontext)
                .using(new FirebaseImageLoader())
                .load(photoRef)
                .into(holder.feed_img);

        //set comment
        String feedID=getItem(position).getFeed_id();
        Query query=dbReference.child("comments").orderByChild("feed_id").equalTo(feedID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentlist.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    commentlist.add(ds.getValue(Comment.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.users_comment.setAdapter(new CommentAdapter(mcontext, R.layout.layout_comment_item,commentlist));

        //set like button listener
        Log.d("feed_like", "loginUserLike" + loginUserLike);

        holder.feed_like.setImageResource(android.R.drawable.star_big_off);
//        ImageButtonOnclick likeListener=new ImageButtonOnclick(position);
//        holder.feed_like.setOnClickListener(likeListener);



        holder.feed_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginUserLike==false){
                    holder.feed_like.setImageResource(android.R.drawable.star_big_on);
                    loginUserLike=true;
                }else{
                    holder.feed_like.setImageResource(android.R.drawable.star_big_off);
                    loginUserLike=false;
                }
            }
        });


        return convertView;
    }

//    class ImageButtonOnclick implements View.OnClickListener{
//
//        private int position;
//
//        public ImageButtonOnclick(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View view) {
//            if(loginUserLike==false){
//                holder.feed_like.setImageResource(android.R.drawable.star_big_on);
//                loginUserLike=true;
//            }else{
//                holder.feed_like.setImageResource(android.R.drawable.star_big_off);
//                loginUserLike=false;
//            }
//        }
//    }
}
