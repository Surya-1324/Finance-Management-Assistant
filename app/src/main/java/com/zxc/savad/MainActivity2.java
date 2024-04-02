
package com.zxc.savad;
import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayout listView;

    TextView textView5;
    ImageView imageView2;

    Context context;
    TextToSpeech textToSpeech;
    NavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        context = this;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();


        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        imageView2 = findViewById(R.id.imageView2);
        textView5 = findViewById(R.id.textView5);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        navigation_view = findViewById(R.id.navigation_view);
        View myfab = findViewById(R.id.fab);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listView);


//        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/savad-416a5.appspot.com/o/photo-1525877442103-5ddb2089b2bb.jpg?alt=media&token=ac8a7ef2-ec77-478d-b8c7-851714b7104f").into(imageView2);



        navigation_view.setNavigationItemSelectedListener(item -> {
            switch(item.getTitle().toString()){

                case "My Account":
                    Intent intent = new Intent(context, MainActivity2.class);
                    startActivity(intent);
                    break;


            }
            return true;
        });

        // Read from the database
        ref.child("value").addValueEventListener(new ValueEventListener() {
            int tmp = 0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageView2.setVisibility(View.VISIBLE);
                int t = Integer.parseInt(dataSnapshot.getValue().toString());
                if(tmp < t) {
                    imageView2.setBackgroundColor(0xFF00FF00);
                    imageView2.setImageResource(R.drawable.ic_round_arrow_upward_24);
                } else {
                    imageView2.setBackgroundColor(0xFFFF0000);
                    imageView2.setImageResource(R.drawable.ic_round_arrow_downward_24);
                }
                tmp = t;
                textView5.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(context, "nope", Toast.LENGTH_SHORT).show();
            }
        });


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View header = inflater.inflate(R.layout.nav_header, null);
        ImageView im = header.findViewById(R.id.imageView);


        ref.child("user").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (DataSnapshot data : task.getResult().getChildren()) {
                    View view = inflater.inflate(R.layout.tile, null);
                   // TextView tv = view.findViewById(R.id.textView6);
                    //tv.setText(data.getKey());
                    TextView tv = view.findViewById(R.id.textView6);
                    tv.setText(data.child("from").getValue().toString());
                    TextView tv1 = view.findViewById(R.id.textView7);
                    tv1.setText(data.child("amount").getValue().toString());

                    listView.addView(view);
                }
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        myfab.setOnClickListener(
                view -> {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"hi speak something");


                    try{

                        startActivityForResult(intent,1000);

                    }catch (Exception e){
                        System.out.println("hi\n\n");
                        System.out.println(e);

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,   Uri.parse("https://market.android.com/details?id=com.google.android.googlequicksearchbox"));
                        startActivity(browserIntent);
                        Toast.makeText(this,"hello error",Toast.LENGTH_SHORT).show();

                    }
                });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode==1000 && resultCode==RESULT_OK){

        process(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));

    }
    }

    private void process(String a) {


        if(a.equalsIgnoreCase("hello sam")){


say("hello suriya");
        }
        else if(a.equalsIgnoreCase("show transaction")){
     say("opening transaction page");

        }
        else if(a.equalsIgnoreCase("show my payment pending")){
            say("showing debts");

        }
        else if(a.equalsIgnoreCase("show my account number")){
            say("showing your account number");

        }

    }



    private void say(String a) {

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS)
                    textToSpeech.setLanguage(Locale.US);

                textToSpeech.speak(a,TextToSpeech.QUEUE_ADD,null);
            }
        });


    }


    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }






}
