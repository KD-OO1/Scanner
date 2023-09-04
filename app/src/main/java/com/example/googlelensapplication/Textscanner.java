package com.example.googlelensapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Textscanner extends AppCompatActivity {
    private Button capturebtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textscanner);
        capturebtn=(Button) findViewById(R.id.idBtnCapture);
        capturebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Textscanner.this,ScannerActivity.class);
                startActivity(i);
            }
        });

    }
}