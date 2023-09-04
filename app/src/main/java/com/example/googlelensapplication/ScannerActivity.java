package com.example.googlelensapplication;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

public class ScannerActivity extends AppCompatActivity {
    private ImageView CaptureIV;
    private TextView resultsTV;
    private Button snapBtn,detectBtn;
    private Bitmap imagebitmap;
    static final int REQUEST_IMAGE_CAPTURE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        CaptureIV = (ImageView) findViewById(R.id.idTVCaptureImage);
        resultsTV = (TextView) findViewById(R.id.idTVDetectedText);
        snapBtn = (Button) findViewById(R.id.idBtnSnap);
        detectBtn = (Button) findViewById(R.id.idBtnDetect);
        Button copyBtn = (Button) findViewById(R.id.idBtnCopy);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyTextToClipboard();
            }
        });
        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detecttext();
            }
        });
        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissions()){
                    captureimage();
                }else{
                    requestPermission();
                }

            }
        });

    }

    private boolean checkPermissions(){
        int camerPermision= ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return camerPermision== PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermission(){
        int PERMISSION_CODE=200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);

    }
    public void captureimage(){
        Intent takePicture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraPermission =grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                captureimage();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void copyTextToClipboard() {

        String textToCopy = resultsTV.getText().toString();
        if (!textToCopy.isEmpty()) {
            android.content.ClipboardManager clipboardManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                clipboardManager = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            }
            android.content.ClipData clipData = android.content.ClipData.newPlainText("Text", textToCopy);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No text to copy", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            imagebitmap=(Bitmap) extras.get("data");
            CaptureIV.setImageBitmap(imagebitmap);
        }
    }

    private void detecttext() {
        InputImage image=InputImage.fromBitmap(imagebitmap,0);
        TextRecognizer recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result=recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                StringBuilder result =new StringBuilder();
                for(Text.TextBlock block:text.getTextBlocks()){
                    String blockText= block.getText();
                    Point[] blockCornerPoint=block.getCornerPoints();
                    Rect blockFrame=block.getBoundingBox();
                    for(Text.Line line:block.getLines())
                    {
                        String lineText=line.getText();
                        Point[] lineCornerPoint =line.getCornerPoints();
                        Rect lineRect=line.getBoundingBox();
                        for(Text.Element element:line.getElements()){
                            String elementText=element.getText();
                            result.append(elementText);
                        }
                        resultsTV.setText(blockText);
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ScannerActivity.this, "Failure to detect image from text"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}