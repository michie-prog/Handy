package com.natalie.handy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    TextInputEditText full_name, email_address, phone_number, password;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button reset_password;
    AlertDialog.Builder reset_alert;

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

        reset_alert = new AlertDialog.Builder(getActivity());
        email_address.setText(firebaseUser.getEmail());
        reset_password = (Button) view.findViewById(R.id.btn_reset);
        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = inflater.inflate(R.layout.reset_pop, null);
                //start alert dialog
                reset_alert.setTitle("Reset Password ?").setMessage("Please enter your email address to get the password reset link").setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //validate the email address
                        EditText email = view.findViewById(R.id.email);
                        if (email.getText().toString().isEmpty()) {
                            email.setError("Required Field");
                            return;
                        }
                        //send the reset link
                        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Reset password email sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", null).setView(view).create().show();
            }
        });

        return view;
    }
}