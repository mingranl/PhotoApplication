package com.example.misaka.photoapplication.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.misaka.photoapplication.Model.User;
import com.example.misaka.photoapplication.Profile.ProfileActivity;
import com.example.misaka.photoapplication.R;
import com.example.misaka.photoapplication.Util.ResultUserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchUserActivity extends AppCompatActivity {

    //used for log output
    private static final String TAG="SearchUserActivity";

    //activity context
    private Context context=SearchUserActivity.this;

    //widgets
    private SearchView mSearchView=null;
    private ListView mListView=null;

    //result users list
    private List<User> resultUsers=null;

    //adapter of result user list view
    private ResultUserAdapter resultUserAdapter=null;

    //database reference
    private static final DatabaseReference dbReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        mSearchView=findViewById(R.id.searchUbar);
        mListView=findViewById(R.id.searchUList);
    }

    private void searchTextListener(){
        resultUsers =new ArrayList<User>();

        //set query text listener
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //execute when click search button
            public boolean onQueryTextSubmit(String s) {
                searchUsers(s);
                return false;
            }

            @Override
            //execute when input change
            public boolean onQueryTextChange(String s) {
                searchUsers(s);
                return false;
            }
        });

    }

    public void searchUsers(String username){
        //clear result List
        resultUsers.clear();

        //if query text is not null, query the database
        if(username.length()!=0){
            Query query=dbReference.child("users").orderByChild("username").equalTo(username);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleUser: dataSnapshot.getChildren()){
                        resultUsers.add(singleUser.getValue(User.class));
                        //render the user list with adapter
                        mListView.setAdapter(new ResultUserAdapter(SearchUserActivity.this,R.layout.layout_user_item,resultUsers));
                        //listen click event on each list item
                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                // jump to user profile
                                Intent intent =new Intent(SearchUserActivity.this, ProfileActivity.class);
                                intent.putExtra("user", (Parcelable) resultUsers.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
