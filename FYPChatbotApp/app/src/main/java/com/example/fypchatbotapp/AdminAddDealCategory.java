package com.example.fypchatbotapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.Category;
import com.example.fypchatbotapp.model.Deal_Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AdminAddDealCategory extends AppCompatActivity {
    EditText etCatName;
    AppCompatButton btnChoose, btnAdd, btnUpload;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference deal_category;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveUri;
    Bitmap bitmap;
    Deal_Category newCategory = new Deal_Category();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_deal_category);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbarDealCatAd);
        toolbar.setTitle("Add Deal Category");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),AdminDealCategoryActivity.class);
                startActivity(backIntent);
            }
        });
        btnChoose = findViewById(R.id.btnCatChoose);
        btnAdd = findViewById(R.id.btnAddDCat);
        btnUpload = findViewById(R.id.btnCatUpload);
        etCatName = findViewById(R.id.etAddCatName);
        imageView = findViewById(R.id.ivDCat);

        database = FirebaseDatabase.getInstance();
        deal_category = database.getReference("Deal_Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveUri != null)
                {
                    ProgressDialog progressDialog = new ProgressDialog(AdminAddDealCategory.this);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();

                    String imageName = UUID.randomUUID().toString();
                    StorageReference imageFolder = storageReference.child("images/"+imageName);
                    imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddDealCategory.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newCategory.setImage(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddDealCategory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+progress+" %");

                        }
                    });
                }
                else
                {
                    Toast.makeText(AdminAddDealCategory.this, "Please choose an image to upload!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etCatName.getText().toString();
                if (name.isEmpty())
                {
                    etCatName.setError("Name is empty");
                    etCatName.requestFocus();
                    return;
                }
                if (newCategory.getImage() == null)
                {
                    Toast.makeText(AdminAddDealCategory.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                newCategory.setName(name);
                if (newCategory != null) {
                    deal_category.push().setValue(newCategory);
                    Toast.makeText(AdminAddDealCategory.this,"New Category "+newCategory.getName()+" was added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminAddDealCategory.this, AdminDealCategoryActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), saveUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }
}