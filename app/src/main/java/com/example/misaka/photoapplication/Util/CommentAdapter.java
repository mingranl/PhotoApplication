package com.example.misaka.photoapplication.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.misaka.photoapplication.Model.Comment;
import com.example.misaka.photoapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Context mcontext=null;
    private int layoutResource=-1;
    private LayoutInflater mInflater=null;
    private ArrayList<Comment> commentList=null;


    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        mcontext=context;
        layoutResource=resource;
        mInflater=LayoutInflater.from(mcontext);
        commentList= (ArrayList<Comment>) objects;
    }

    private static final class ViewHolder{
        TextView commentUser;
        TextView commentText;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView=mInflater.inflate(layoutResource, parent, false);
            holder=new ViewHolder();
            holder.commentUser=(TextView) convertView.findViewById(R.id.cUsername);
            holder.commentText=(TextView) convertView.findViewById(R.id.cText);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        holder.commentUser.setText(getItem(position).getUsername());
        holder.commentText.setText(getItem(position).getComment());
        return convertView;

    }
}
