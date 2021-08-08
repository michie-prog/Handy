
package com.natalie.handy;


 import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class UserProfileActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextInputEditText full_name, email_address, phone_number, password;
    ImageButton profile_pic;
    Button logout, update;
    private StorageReference storageReference;
    //FirebaseFirestore db = FirebaseFirestore.getinstance;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
   // FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent data = getIntent();
         String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        //firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Hooks
        full_name = findViewById(R.id.fullname);
        email_address = findViewById(R.id.email);
        phone_number = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        logout = findViewById(R.id.btn_logout);
        update = findViewById(R.id.btn_update);
        profile_pic = findViewById(R.id.profile_pic);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        assert firebaseUser != null;
        email_address.setText(firebaseUser.getEmail());
        full_name.setText(firebaseUser.getDisplayName());
        phone_number.setText(firebaseUser.getPhoneNumber());
         //password.setText(firebaseUser.get);

        StorageReference profileRef = storageReference.child("users/" + firebaseAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profile_pic));

        profile_pic.setOnClickListener(this::onClick);
    update.setOnClickListener(v -> {
        if(full_name.getText().toString().isEmpty() || email_address.getText().toString().isEmpty() || phone_number.getText().toString().isEmpty()){
            Toast.makeText(UserProfileActivity.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        final String email1 = email_address.getText().toString();
        firebaseUser.updateEmail(email1).addOnSuccessListener(aVoid -> {
            DocumentReference docRef = firestore.collection("users").document(firebaseUser.getUid());
            Map<String,Object> edited = new HashMap<>();
            edited.put("email", email1);
            edited.put("fName",full_name.getText().toString());
            edited.put("phone",phone_number.getText().toString());
            docRef.update(edited).addOnSuccessListener(aVoid1 -> {
                Toast.makeText(UserProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            });
                Toast.makeText(UserProfileActivity.this,  "Email is changed.", Toast.LENGTH_SHORT).show();
    }).addOnFailureListener(e -> Toast.makeText(UserProfileActivity.this,   e.getMessage(), Toast.LENGTH_SHORT).show());


});
      email_address.setText(email);
        full_name.setText(fullName);
        phone_number.setText(phone);

        Log.d(TAG, "onCreate: " + fullName + " " + email + " " + phone);
}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri imageUri = data.getData();
                profile_pic.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);


            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profile_pic)));

    }

    private void onClick(View v) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 1000);
    }
}
