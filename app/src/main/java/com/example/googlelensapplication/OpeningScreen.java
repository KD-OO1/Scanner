package com.example.googlelensapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpeningScreen extends AppCompatActivity {
Button button;Button button2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);
        button=(Button) findViewById(R.id.button);
        button2=(Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    openActivity2();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();

            }
        });
    }

    private void openActivity() {
        Intent intent3 =new Intent(this,Textscanner.class);
        startActivity(intent3);

    }

    private void openActivity2() {
        Intent intent2 =new Intent(this,MainActivity.class);
        startActivity(intent2);

    }
}