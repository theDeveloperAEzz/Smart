package com.example.thedeveloper.smart.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.thedeveloper.smart.activity.Main2Activity;
import com.example.thedeveloper.smart.activity.MainActivity;
import com.example.thedeveloper.smart.model.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.thedeveloper.smart.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpFragment extends Fragment {

    private Button btnSignUp;
    private EditText editTextName, editTextEmail, editTextPassword, editTextRepeatPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
//    StorageReference storageRef, imageRef;
//    DBHelper dbHelper;
    String Name;
    String Email;
    String Avatar = "null";
    String AdminPass = "";
    String Password;
    String RepeatPassword;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
//        dbHelper = new DBHelper(getContext());
        btnSignUp = rootView.findViewById(R.id.btn_sign_up);
        editTextName = rootView.findViewById(R.id.et_name);
        editTextEmail = rootView.findViewById(R.id.et_email);
        editTextPassword = rootView.findViewById(R.id.et_password);
        editTextRepeatPassword = rootView.findViewById(R.id.et_repeat_password);
        progressDialog = new ProgressDialog(getActivity());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Name = editTextName.getText().toString();
                Email = editTextEmail.getText().toString();
                Password = editTextPassword.getText().toString();
                RepeatPassword = editTextRepeatPassword.getText().toString();
                if (TextUtils.isEmpty(Name)) {
                    progressDialog.hide();
                    editTextName.setError("enter your name");
                    return;

                }
                if (Name.length() <= 2) {
                    progressDialog.hide();
                    editTextName.setError("enter First & Last name");
                    return;

                }
                if (TextUtils.isEmpty(Email)) {
                    progressDialog.hide();
                    editTextEmail.setError(" enter your Email");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    progressDialog.hide();
                    editTextPassword.setError("enter your password");
                    return;
                }
                if (Password.length() < 6) {
                    progressDialog.hide();
                    editTextPassword.setError("password should be 6 chars or more");
                    return;
                }

                if (TextUtils.isEmpty(RepeatPassword)) {
                    progressDialog.hide();
                    editTextRepeatPassword.setError("repeat your password");
                    return;

                }
                if (validate(Email, Password, RepeatPassword)) {

                        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            final UserInformation userInformation = new UserInformation(Name, Email, Avatar, AdminPass);
                                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                databaseReference = FirebaseDatabase.getInstance().getReference("UsersInformation");
                                                                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(userInformation);
                                                                Toast.makeText(getContext(), "Verification send to :" + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
                                                                progressDialog.hide();
                                                                startActivity(new Intent(getContext(), Main2Activity.class));
                                                            } else {
                                                                Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                                progressDialog.hide();
                                                editTextEmail.setError("malformed-wrong email");
                                                // TODO: Take your action
                                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                                progressDialog.hide();
                                                editTextEmail.setError("really exist email");
                                                // TODO: Take your action
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                });


                } else {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "Invalid email or not match password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }


    private boolean validate(String emailStr, String password, String repeatPassword) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return password.length() > 0 && repeatPassword.equals(password) && matcher.find();
    }



}
