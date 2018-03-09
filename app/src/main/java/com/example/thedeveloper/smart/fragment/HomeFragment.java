package com.example.thedeveloper.smart.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.thedeveloper.smart.R;
import com.example.thedeveloper.smart.activity.Main2Activity;
import com.example.thedeveloper.smart.model.ListHomeAdapter;
import com.example.thedeveloper.smart.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    RecyclerView mRecyclerView;
    ListHomeAdapter mAdapter;
    ArrayList<Post> postArrayList = new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    boolean b;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_home_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        databaseReference.child("UsersInformation").child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    b = true;
                } else {
                    b = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRecyclerView = rootView.findViewById(R.id.admin_list);
        mAdapter = new ListHomeAdapter(postArrayList);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference.child("UsersInformation").child("Posts")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Post model = dataSnapshot.getValue(Post.class);
                        postArrayList.add(model);
                        mRecyclerView.scrollToPosition(postArrayList.size() - 1);
                        mAdapter.notifyItemInserted(postArrayList.size() - 1);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        if (b) {
            Snackbar.make(rootView, "no posts added yet!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        return rootView;

    }

    private void refreshContent() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                 mSwipeRefreshLayout.setRefreshing(false);
//            });
//        }}
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                clear();
                databaseReference.child("UsersInformation").child("Posts")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Post model = dataSnapshot.getValue(Post.class);
                                postArrayList.add(model);
                                mRecyclerView.scrollToPosition(postArrayList.size() - 1);
                                mAdapter.notifyItemInserted(postArrayList.size() - 1);

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        }, 0);
    }

    public void clear() {
        final int size = postArrayList.size();
        postArrayList.clear();
        mAdapter.notifyItemRangeRemoved(0, size);
    }
}
