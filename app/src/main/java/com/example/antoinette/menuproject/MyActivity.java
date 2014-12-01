package com.example.antoinette.menuproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class MyActivity extends Activity {
    Button takePhotoBtn;
    ImageView takenPhoto;
    private static final int CAM_REQUEST=1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        takePhotoBtn = (Button)findViewById(R.id.button_capture);

        takePhotoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAM_REQUEST);

            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAM_REQUEST){
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            Intent i = new Intent(this, SendImage.class);
            i.putExtra("name",thumbnail);
            startActivity(i);
            finish();
            // Convert thumbnail to file
          //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
          //  thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
          //  byte[] byteArray = stream.toByteArray();

        //    FileInputStream fileInputStream = null;
         //   File file = new File("temp");
          //  byteArray = new byte[(int)file.length()];



            //takenPhoto.setImageBitmap(thumbnail);
        }
    }

}
