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
import com.example.misaka.photoapplication.Model.User;
import com.example.misaka.photoapplication.R;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserFeedAdapter extends ArrayAdapter<FeedItem>{

    private Context mcontext=null;
    private int layoutResource=-1;
    private LayoutInflater mInflater=null;
    private ArrayList<FeedItem> feedlist=null;
    //private static ArrayList<Comment> commentlist=null;;
    private ArrayList<Like> likelist=null;
    private ViewHolder holder=null;
    private static String currentUsername="";
    private static boolean currentUserLike;
    private static Like currentLike=new Like();
    private static final String starOn="android:drawable/star_big_on";
    private static final String starOff="android:drawable/star_big_off";

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
        TextView like_count;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if(convertView==null){
            convertView=mInflater.inflate(layoutResource, parent, false);
            holder=new ViewHolder();
            holder.feed_username=convertView.findViewById(R.id.feed_username);
            holder.feed_img=convertView.findViewById(R.id.feed_img);
            holder.description=convertView.findViewById(R.id.feed_description);
            holder.feed_like=convertView.findViewById(R.id.feed_like);
            holder.feed_comment=convertView.findViewById(R.id.feed_comment);
            holder.users_comment=convertView.findViewById(R.id.users_comment);
            holder.like_count=convertView.findViewById(R.id.feed_likecount);
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
        //set like_count
        holder.like_count.setText(String.valueOf(getItem(position).getLike_count()));

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
            ArrayList<Comment> commentlist=new ArrayList<Comment>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentlist.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    commentlist.add(ds.getValue(Comment.class));
                    holder.users_comment.setAdapter(new CommentAdapter(mcontext, R.layout.layout_comment_item,commentlist));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //initialize like button
        if(judgelike(getItem(position).getFeed_id())==false){
            Log.d("judge like----->","currentUser: "+getCurrentUsername()+" feed_id: "+getItem(position).getFeed_id()+" like? "+ false+ " button off");
            holder.feed_like.setImageResource(android.R.drawable.star_big_off);
            holder.feed_like.setTag(position);
        }else{
            Log.d("judge like----->","currentUser: "+getCurrentUsername()+" feed_id: "+getItem(position).getFeed_id()+" like? "+ true+ " button on");
            holder.feed_like.setImageResource(android.R.drawable.star_big_on);
            holder.feed_like.setTag(position);
        }



        holder.feed_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(getItem(position).isCurrent_like()==false){
//                    holder.feed_like.setImageResource(android.R.drawable.star_big_on);
//                    getItem(position).setCurrent_like(true);
//                    dbReference.child("userfeeds").child(getItem(position).getFeed_id()).child("current_like").setValue(true);
//                    dbReference.child("userfeeds").child(getItem(position).getFeed_id()).child("like_count").setValue(getItem(position).getLike_count()+1);
//                    Log.d("like_onclick", "in " + getItem(position).isCurrent_like() +" position "+ position + "drawable "+holder.feed_like.getResources());
//
//                }else if(getItem(position).isCurrent_like()==true){
//                    holder.feed_like.setImageResource(android.R.drawable.star_big_off);
//                    getItem(position).setCurrent_like(false);
//                    dbReference.child("userfeeds").child(getItem(position).getFeed_id()).child("current_like").setValue(false);
//                    dbReference.child("userfeeds").child(getItem(position).getFeed_id()).child("like_count").setValue(getItem(position).getLike_count()-1);
//                    Log.d("like_onclick", "in " + getItem(position).isCurrent_like() +" position "+ position + "drawable " +holder.feed_like.getResources());
//
//                }else{
//                    Log.d("like_onclick", "in " + getItem(position).isCurrent_like() +" position "+ position + "drawable " +holder.feed_like.getResources());
//                }

                if(judgelike(getItem(position).getFeed_id())==false){

                    Log.d("onclick---->","1. not like----->2.like--->button on"+" position "+position+" feed_id "+getItem(position).getFeed_id());
                    holder.feed_like.setImageResource(android.R.drawable.star_big_on);
                    String like_id=UUID.randomUUID().toString();
                    Like like=new Like(like_id,getItem(position).getFeed_id(),getCurrentUsername());
                    dbReference.child("likes").child(like_id).setValue(like);
                    dbReference.child("userfeeds").child(getItem(position).getFeed_id()).child("like_count").setValue(getItem(position).getLike_count()+1);

                }else if(judgelike(getItem(position).getFeed_id())==true){
                    Log.d("onclick---->","1.like----->2.not like--->button off"+" position "+position+" feed_id "+getItem(position).getFeed_id());
                    holder.feed_like.setImageResource(android.R.drawable.star_big_off);
                    Like like=getCurrentLike(getItem(position).getFeed_id());
                    Log.d("getlike",getItem(position).getFeed_id());
                    Log.d("currentLike",getCurrentLike(getItem(position).getFeed_id())+"");
                    dbReference.child("likes").child(like.getLike_id()).setValue(null);
                    dbReference.child("userfeeds").child(getItem(position).getFeed_id()).child("like_count").setValue(getItem(position).getLike_count()-1);
                }else{

                }
            }
        });


        return convertView;
    }

    public String getCurrentUsername(){

        Query query=dbReference.child("users").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    currentUsername=ds.getValue(User.class).getUsername();
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return currentUsername;
    }

    public Like getCurrentLike(String feed_id){
        Log.d("getcuin",feed_id);
        Query query=dbReference.child("likes").orderByChild("feed_id").equalTo(feed_id);
        Log.d("query",query.toString());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("datachage","datachange");
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getValue(Like.class).getUsername().equals(getCurrentUsername())){
                        currentLike=ds.getValue(Like.class);
                        Log.d("currentlike---->",currentLike.toString());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return currentLike;
    }

    public boolean judgelike(String feed_id){
        //currentUserLike=false;
       Log.d("judgelike---->","feedID: "+feed_id+" user "+getCurrentUsername());
        Query query=dbReference.child("likes").orderByChild("feed_id").equalTo(feed_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserLike=false;
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Log.d("ds username---->"+ds.getValue(Like.class).getUsername(), "currentUsername----->"+getCurrentUsername());
                    if(ds.getValue(Like.class).getUsername().equals(getCurrentUsername())){
                        Log.d("TTTTTTTTT","TTTTTTTT");
                        currentUserLike=true;
                        break;
                    }else{
                        currentUserLike=false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.d("now----->",""+currentUserLike);
        return currentUserLike;
    }

}
