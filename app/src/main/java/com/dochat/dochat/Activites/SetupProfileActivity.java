package com.dochat.dochat.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dochat.dochat.R;
import com.dochat.dochat.Models.User;
import com.dochat.dochat.databinding.ActivitySetupProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ImageView imageView;
    Uri selectedImage;
    int SELECT_PICTURE = 200;
    Button Setup_Profile_Btn;
    EditText nameBox;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_setup_profile);
        getSupportActionBar().hide();

        dialog  = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();
        nameBox = (EditText) findViewById(R.id.nameBox);
        imageView=(ImageView) findViewById(R.id.profileImage);
        Setup_Profile_Btn = (Button) findViewById(R.id.Setup_Profile);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageChooser();
            }
        });

        Setup_Profile_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // I am taking the input name into a string
                String name = (String) nameBox.getText().toString();

               // String name = nameBox.getText().toString();

                System.out.println("name="+name);
                if(name.isEmpty())
                {
                   nameBox.setError("Please Type a Name");
                    return;
                }
                dialog.show();
                if(selectedImage !=null)
                {
                    StorageReference reference = storage.getReference().child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                       String imageUrl = uri.toString();

                                       String uid = auth.getUid();
                                       String phone = auth.getCurrentUser().getPhoneNumber();
                                       String name = nameBox.getText().toString();

                                       User user = new User(uid,name,phone,imageUrl);

                                       database.getReference()
                                               .child("users")
                                               .child(uid)
                                               .setValue(user)
                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void unused) {
                                                       dialog.dismiss();
                                                       Intent intent = new Intent(SetupProfileActivity.this,MainActivity.class);
                                                       startActivity(intent);
                                                       finish();
                                                   }
                                               });
                                    }
                                });
                            }
                            else
                            {
                                String uid = auth.getUid();
                                String phone = auth.getCurrentUser().getPhoneNumber();

                                User user = new User(uid,name,phone,"No Image");

                                database.getReference()
                                        .child("users")
                                        .child(uid)
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(SetupProfileActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });

    }

          
    void imageChooser() {

        // create an instance of the 
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it 
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the

            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imageView.setImageURI(selectedImageUri);
                    selectedImage = data.getData();
                }
            }
        }
    }
}