package com.example.fypchatbotapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    EditText etUser, etPassword;
    TextView tvCreateAcc;
    AppCompatButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        tvCreateAcc = findViewById(R.id.createAcc);
        btnSignIn = findViewById(R.id.btnSignIn1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUser.getText().toString();
                String password = etPassword.getText().toString();

                if(username.isEmpty())
                {
                    etUser.setError("Please enter a username");
                    etUser.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    etPassword.setError("Please enter password");
                    etPassword.requestFocus();
                    return;
                }

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(username).exists())
                        {
                            User user = snapshot.child(username).getValue(User.class);
                            if (username.equalsIgnoreCase("admin"))
                            {
                                if (user.getPassword().equals(password))
                                {
                                    Intent adminIntent = new Intent(SignIn.this,AdminMenuActivity.class);
                                    Common.currentUser = user;
                                    startActivity(adminIntent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(SignIn.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                if (user.getPassword().equals(password))
                                {
                                    Intent homeIntent = new Intent(SignIn.this,ChatActivity.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(SignIn.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(SignIn.this, "User does not exists!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        tvCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
            }
        });
    }
}
