package com.zxc.savad;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Context context;

    EditText editTextTextPersonName, editTextTextPassword;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);

        context = this;
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();


    }

    public void btnClicked(View view) {

        String username = editTextTextPersonName.getText().toString();
        String password = editTextTextPassword.getText().toString();

        ref.child("login").child(username).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                    if(task.getResult().child("pass").getValue().toString().equals(password)){
                        Intent intent = new Intent(context, MainActivity2.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "wrong password", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity2.class);

                    }
                }

        });
    }
}