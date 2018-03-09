package com.example.thedeveloper.smart.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thedeveloper.smart.R;
import com.example.thedeveloper.smart.model.Post;
import com.example.thedeveloper.smart.model.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    TextView textViewMyName, textViewEmail, textViewHowManyPostsIShared,
            textViewHowManyPersonsSharedMyPosts, textViewMySalary;
    int manyPostsIShared = 0;
    double salaryOfAllPostsIShared = 0;
    int manyPersonsSharedMyPosts = 0;
    double salaryOfAllPostsIPost = 0;
    ValueEventListener mlistener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            manyPostsIShared=0;
            manyPersonsSharedMyPosts=0;
            salaryOfAllPostsIPost=0;
            salaryOfAllPostsIShared=0;
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                if (dataSnapshot1.child("uid").getValue().toString().equals(user.getUid())
                        && dataSnapshot1.child("status").getValue().toString().equals("shared")) {
                    manyPostsIShared++;
                    salaryOfAllPostsIShared += Double.parseDouble(dataSnapshot1.child("salary").getValue().toString());
                }
                if (dataSnapshot1.child("uid").getValue().toString().equals(user.getUid())
                        && dataSnapshot1.child("status").getValue().toString().equals("posted")) {
                    manyPersonsSharedMyPosts = +Integer.parseInt(dataSnapshot1.child("numberOfShares").getValue().toString());
                    salaryOfAllPostsIPost += Double.parseDouble(dataSnapshot1.child("salary").getValue().toString());
                }

            }
            textViewHowManyPostsIShared.setText("you have shared : "
                    + manyPostsIShared + " posts\nand it collected : " + salaryOfAllPostsIShared + " $");
            textViewHowManyPersonsSharedMyPosts.setText("there were : " + manyPersonsSharedMyPosts +
                    " persons shared your posts" + "\nand it collected : " + salaryOfAllPostsIPost + " $");
            textViewMySalary.setText("total salary : " + (salaryOfAllPostsIPost + salaryOfAllPostsIShared) + " $");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_profile_swipe_refresh_layout);

        textViewMyName = rootView.findViewById(R.id.my_name);
        textViewEmail = rootView.findViewById(R.id.my_email);
        textViewHowManyPostsIShared = rootView.findViewById(R.id.how_many_posts_i_shared);
        textViewHowManyPersonsSharedMyPosts = rootView.findViewById(R.id.how_many_persons_shared_my_posts);
        textViewMySalary = rootView.findViewById(R.id.my_salary);
        databaseReference.child("UsersInformation").child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                        textViewMyName.setText(userInformation.getName());
                        textViewEmail.setText(userInformation.getEmail());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        databaseReference.child("UsersInformation").child("Posts").addValueEventListener(mlistener);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });


        return rootView;
    }

    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                databaseReference.child("UsersInformation").child("Posts").removeEventListener(mlistener);
                manyPostsIShared=0;
                manyPersonsSharedMyPosts=0;
                salaryOfAllPostsIPost=0;
                salaryOfAllPostsIShared=0;
                databaseReference.child("UsersInformation").child("Posts").addValueEventListener(mlistener);


            }
        }, 0);

    }


}
