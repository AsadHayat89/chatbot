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

public class AdminAddFoodActivity extends AppCompatActivity {
    String catId = "";
    AppCompatButton btnChoose, btnAdd, btnUpload;
    EditText etFname, etFprice, etFdesc;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference foods;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri saveUri;
    Bitmap bitmap;
    Food newFood = new Food();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_food);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar8);
        toolbar.setTitle("Add Food");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),AdminFoodListActivity.class);
                startActivity(backIntent);
            }
        });

        catId = getIntent().getStringExtra("CatId");
        btnChoose = findViewById(R.id.btnFChoose);
        btnUpload = findViewById(R.id.btnFUpload);
        btnAdd = findViewById(R.id.btnAddFood);
        etFname = findViewById(R.id.etAddFName);
        etFprice = findViewById(R.id.etAddFPrice);
        etFdesc = findViewById(R.id.etAddFDes);
        imageView = findViewById(R.id.ivFood);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");
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
                    ProgressDialog progressDialog = new ProgressDialog(AdminAddFoodActivity.this);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();

                    String imageName = UUID.randomUUID().toString();
                    StorageReference imageFolder = storageReference.child("images/"+imageName);
                    imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddFoodActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newFood.setImage(uri.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddFoodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AdminAddFoodActivity.this, "Please choose an image to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etFname.getText().toString();
                String price = etFprice.getText().toString();
                String desc = etFdesc.getText().toString();
                if (name.isEmpty())
                {
                    etFname.setError("Name is empty");
                    etFname.requestFocus();
                    return;
                }
                if (price.isEmpty())
                {
                    etFprice.setError("Price is empty");
                    etFprice.requestFocus();
                    return;
                }
                if (desc.isEmpty())
                {
                    etFdesc.setError("Description is empty");
                    etFdesc.requestFocus();
                    return;
                }
                if (newFood.getImage() == null)
                {
                    Toast.makeText(AdminAddFoodActivity.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newFood != null) {
                    newFood.setName(name);
                    newFood.setPrice(price);
                    newFood.setDescription(desc);
                    newFood.setMenuId(catId);
                    foods.push().setValue(newFood);
                    Toast.makeText(AdminAddFoodActivity.this,"New Food "+newFood.getName()+" was added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminAddFoodActivity.this, AdminFoodListActivity.class);
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




