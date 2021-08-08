package com.natalie.handy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    TextInputEditText full_name, email_address, phone_number, password;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Hooks
        full_name = (TextInputEditText) view.findViewById(R.id.full_name);
        email_address = (TextInputEditText) view.findViewById(R.id.email);
        phone_number = (TextInputEditText) view.findViewById(R.id.phone);
        password = (TextInputEditText) view.findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        email_address.setText(firebaseUser.getEmail());

        return view;
    }
}