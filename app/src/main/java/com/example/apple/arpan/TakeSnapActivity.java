package com.example.apple.arpan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakeSnapActivity extends AppCompatActivity {

    private ImageView imageHolder;
    private final int requestCode = 20;
    private EditText className;
    private DatabaseReference rootRef;
    private int counter = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_snap);
        className = findViewById(R.id.className);
        Button takeSnapButotn = findViewById(R.id.snap);
        SharedPreferences prefs = getSharedPreferences("totalEntry", MODE_PRIVATE);
        counter = prefs.getInt("counter",0);
        //database reference pointing to root of database
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node
        takeSnapButotn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, requestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);

            SharedPreferences.Editor editor = getSharedPreferences("totalEntry", MODE_PRIVATE).edit();
            editor.putInt("counter", counter++);
            editor.apply();
            // display the image from SD Card to ImageView Control
            String storeFilename = "photo_" + partFilename + ".jpg";
            DataModel dataModel = new DataModel(className.getText().toString(),storeFilename);
            rootRef.push().setValue(dataModel);

            Intent intent = new Intent(TakeSnapActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
            /* Showing bitmap
            Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
            imageHolder.setImageBitmap(mBitmap);*/
        }
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate) {
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
