package com.example.misaka.photoapplication.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.misaka.photoapplication.Model.User;
import com.example.misaka.photoapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ResultUserAdapter extends ArrayAdapter<User> {

    private Context mContext=null;
    private int layoutResource=-1;
    private LayoutInflater mInflater=null;
    private ArrayList resultUsers=null;


    public ResultUserAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        mContext=context;
        layoutResource=resource;
        mInflater=LayoutInflater.from(mContext);
        resultUsers= (ArrayList) objects;
    }


    //custom view holder of user items
    private static final class ViewHolder{
        TextView username;
        TextView email;
    }

    //render the list with inflater


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=mInflater.inflate(layoutResource, parent, false);
            holder=new ViewHolder();
            holder.username=(TextView) convertView.findViewById(R.id.userItemName);
            holder.email=(TextView) convertView.findViewById(R.id.userItemEmail);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.username.setText(getItem(position).getUsername());
        holder.email.setText(getItem(position).getEmail());

        return convertView;
    }
}
