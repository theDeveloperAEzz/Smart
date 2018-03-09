package com.example.thedeveloper.smart.fragment;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thedeveloper.smart.R;
import com.example.thedeveloper.smart.activity.MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class WelcomeFragment extends Fragment {
    TextView textViewTile;
    Button buttonSignIn;
    Button buttonSignUp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        textViewTile = rootView.findViewById(R.id.welcome);
        final SignInFragment signInFragment = new SignInFragment();
        final SignUpFragment signUpFragment = new SignUpFragment();
        buttonSignIn = rootView.findViewById(R.id.signIn);
        buttonSignUp = rootView.findViewById(R.id.signUp);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft.replace(R.id.content_frame, signInFragment, "findThisFragment3")
                        .addToBackStack(null)
                        .commit();


            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft.replace(R.id.content_frame, signUpFragment, "findThisFragment3")
                        .addToBackStack(null)
                        .commit();

            }
        });

        return rootView;
    }

    protected void applyBlurMaskFilter(TextView tv, BlurMaskFilter.Blur style) {

        float radius = tv.getTextSize() / 10;

        // Initialize a new BlurMaskFilter instance
        BlurMaskFilter filter = new BlurMaskFilter(radius, style);

        // Set the TextView layer type
        tv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // Finally, apply the blur effect on TextView text
        tv.getPaint().setMaskFilter(filter);
    }

}
