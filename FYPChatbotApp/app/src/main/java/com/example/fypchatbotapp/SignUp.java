package com.example.fypchatbotapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {
    EditText etUserName, etPassword, etName, etEmail,etPhone;
    TextView tvLogAcc;
    AppCompatButton signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        etUserName = findViewById(R.id.etUser1);
        etName = findViewById(R.id.etName);
        tvLogAcc = findViewById(R.id.logAcc);
        etPassword = findViewById(R.id.etPassword1);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        signUp = findViewById(R.id.btnSignUp1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUserName.getText().toString();
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhone.getText().toString();

                if(username.isEmpty())
                {
                    etUserName.setError("Username is empty");
                    etUserName.requestFocus();
                    return;
                }

                if(name.isEmpty())
                {
                    etName.setError("Name is empty");
                    etName.requestFocus();
                    return;
                }

                if(email.isEmpty())
                {
                    etEmail.setError("Email is empty");
                    etEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    etEmail.setError("Enter the valid email address");
                    etEmail.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    etPassword.setError("Password is empty");
                    etPassword.requestFocus();
                    return;
                }
                if(phone.isEmpty())
                {
                    etPhone.setError("Phone number is empty");
                    etPhone.requestFocus();
                    return;
                }

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(etUserName.getText().toString()).exists())
                        {
                            Toast.makeText(SignUp.this, "User name already exists", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            User user = new User(name,password,email,phone);
                            table_user.child(etUserName.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Sign Up successful!", Toast.LENGTH_SHORT).show();
                            Common.currentUser = user;
                            Intent intent = new Intent(SignUp.this, ChatActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        tvLogAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, SignIn.class);
                startActivity(i);
            }
        });
    }
}