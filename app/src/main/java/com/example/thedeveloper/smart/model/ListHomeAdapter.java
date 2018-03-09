package com.example.thedeveloper.smart.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thedeveloper.smart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ListHomeAdapter extends RecyclerView.Adapter<ListHomeAdapter.ViewHolder> {
    Context context;
    ArrayList<Post> objects;
    String participantName;
    DatabaseReference databaseReference;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    SharedPreferences numberOfSharesPreference, getNumberOfSharesPreference;
    Integer x = 0;
    boolean b;


    public ListHomeAdapter(ArrayList<Post> objects) {
        this.objects = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_home_item, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post post = objects.get(position);
        holder.textViewAuthorName.setText(post.getAuthorName());
        holder.textViewStatus.setText(post.getStatus());
        if (!post.getSupName().equals("")) {
            holder.textViewSupName.setText(post.getSupName() + "'s post");
        } else {
            holder.textViewSupName.setText(post.getSupName());
        }
        holder.textViewContent.setText(post.getContent());
        holder.textViewNumberOfShare.setText(post.getNumberOfShares() + " shared this");
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        databaseReference.child("UsersInformation").child("Users").child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        participantName = dataSnapshot.child("name").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Key = post.getPushKey();
                if (!participantName.equals(post.getAuthorName()) && !participantName.equals(post.getSupName())) {
                    String Key1 = databaseReference.child("UsersInformation").child("Posts").push().getKey();
                    x = post.getNumberOfShares();
                    x += 1;
                    post.setNumberOfShares(x);
                    if (post.getStatus().equals("posted")) {
                        post.setSalary(post.getCounter() * post.getNumberOfShares());
                    } else if (post.getStatus().equals("shared")) {
                        switch (post.getNumberOfShares()) {
                            case 1:
                                post.setSalary(post.getCounter() * 2);
                                databaseReference.child("UsersInformation").child("Posts").child(Key).child("salary").setValue(post.getSalary() * 2);
                                break;
                            default:
                                post.setSalary(post.getCounter() + post.getSalary());
                                break;
                        }
                    }
                    Map<String, Object> Values = post.ToMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(Key, Values);
                    databaseReference.child("UsersInformation")
                            .child("Posts")
                            .updateChildren(childUpdates);
                    Post post1 = new Post(participantName, "shared", post.getAuthorName(), post.getContent(), user.getUid(), Key1, 0, (post.getCounter() / 2));
                    databaseReference.child("UsersInformation").child("Posts")
                            .child(Key1).setValue(post1);
                    if (post1.getNumberOfShares() == 1) {

                    }
                } else {
                    Toast.makeText(view.getContext(), "you cant share your post", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button btnShare;
        TextView textViewAuthorName, textViewStatus, textViewSupName, textViewContent, textViewNumberOfShare;

        ViewHolder(View v) {
            super(v);
            textViewAuthorName = (TextView) itemView.findViewById(R.id.author_name);
            textViewStatus = (TextView) itemView.findViewById(R.id.status);
            textViewSupName = (TextView) itemView.findViewById(R.id.sup_name);
            textViewContent = (TextView) itemView.findViewById(R.id.content);
            textViewNumberOfShare = (TextView) itemView.findViewById(R.id.num_of_share);
            btnShare = (Button) itemView.findViewById(R.id.btn_share);

        }
    }

}
