package com.example.fypchatbotapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fypchatbotapp.common.Common;
import com.example.fypchatbotapp.model.ChatMessage;
import com.example.fypchatbotapp.viewholder.ChatMessageAdapter;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ActionBarDrawerToggle actionBarDrawerToggle;
    TextView txtFullName;
    private ListView listView;
    private ImageButton buttonSend;
    private EditText etMessage;
    private ChatMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
//                .setDrawerLayout(drawer)
//                .build();

        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        buttonSend = (ImageButton) findViewById(R.id.btnSend);
        etMessage = (EditText) findViewById(R.id.et_message);
        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        listView = (ListView) findViewById(R.id.message_view);

        listView.setAdapter(adapter);

        botReply("Hi "+Common.currentUser.getName()+"! How can I help you?");

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (message.isEmpty()) {
                    etMessage.setError("Please type a text message");
                    etMessage.requestFocus();
                    return;
                } else {
                    ChatMessage chatMessage = new ChatMessage(true,message);
                    adapter.add(chatMessage);
                    etMessage.setText("");
                    listView.setSelection(adapter.getCount() - 1);

                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder().add("question",message).add("username",Common.currentUser.getName()).add("userphone", Common.currentUser.getPhone()).build();
                    Request request = new Request.Builder().url("http://192.168.1.144:5000/").post(formBody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(ChatActivity.this, "Exception :" + e, Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        public void onResponse(Call call, final Response response) throws IOException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String reply = response.body().string();
                                        botReply(reply);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    });

                }
            }
        });
    }
//    private void sendMessage(String message) {
//        ChatMessage chatMessage = new ChatMessage(true,message);
//        mAdapter.add(chatMessage);
//        //respond as Helloworld
//        botReply("HelloWorld");
//    }

        private void botReply(String message) {
            ChatMessage chatMessage = new ChatMessage( false,message);
            adapter.add(chatMessage);
        }
//
//    private void sendMessage() {
//        ChatMessage chatMessage = new ChatMessage( true,null);
//        mAdapter.add(chatMessage);
//
//        botReply();
//    }
//
//    private void botReply() {
//        ChatMessage chatMessage = new ChatMessage(false,null);
//        mAdapter.add(chatMessage);
//    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_cat) {
            Intent menuIntent = new Intent(ChatActivity.this, MenuActivity.class);
            startActivity(menuIntent);
        }
        else if (id == R.id.nav_deal) {
            Intent dealsIntent = new Intent(ChatActivity.this, DealCategoryActivity.class);
            startActivity(dealsIntent);
        }
//        else if (id == R.id.nav_cart) {
//            Intent cartIntent = new Intent(ChatActivity.this, CartActivity.class);
//            startActivity(cartIntent);
//        }
        else if (id == R.id.nav_food_orders) {
            Intent fOrderIntent = new Intent(ChatActivity.this, Simple_Food_Order.class);
            startActivity(fOrderIntent);
        }
        else if (id == R.id.nav_table_orders) {
            Intent tOrderIntent = new Intent(ChatActivity.this, TableBookingOrders.class);
            startActivity(tOrderIntent);
        }
        else if (id == R.id.nav_event_orders) {
            Intent eOrderIntent = new Intent(ChatActivity.this, EventBookingOrders.class);
            startActivity(eOrderIntent);
        }
        else if (id == R.id.nav_catering_orders) {
            Intent cOrderIntent = new Intent(ChatActivity.this, CateringOrders.class);
            startActivity(cOrderIntent);
        }
        else if (id == R.id.nav_logout) {
            Intent signIntent = new Intent(ChatActivity.this,SignIn.class);
            signIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIntent);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}