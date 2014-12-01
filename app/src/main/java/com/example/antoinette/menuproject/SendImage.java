/*Makes the call to the OCR Service API and then displays the response*/
package com.example.antoinette.menuproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class SendImage extends Activity {
    private ProgressBar spinner;
    private ImageView takenPhoto;
    private final int RESPONSE_OK = 200;
    private String apiKey="NUSVa5snB7";
    private String langCode="en";
    private Bitmap pic;
    private File imageFile;
    private TextView pictureName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);
        pictureName = (TextView) findViewById(R.id.textView);
        spinner = (ProgressBar)findViewById(R.id.progress);
        takenPhoto = (ImageView) findViewById(R.id.imageview);
        spinner.setVisibility(View.VISIBLE);
        Bitmap thumbnail = (Bitmap)getIntent().getExtras().get("name");
        pic = thumbnail;
        display(thumbnail);

    }
    private void display(Bitmap thumbnail){
        spinner.setVisibility(View.GONE);
        ((TextView)findViewById(R.id.loading_text)).setVisibility(View.GONE);
        takenPhoto.setImageBitmap(thumbnail);
        imageFile = saveImage();
       // sendImage();
    }
    private File saveImage(){
        File filesDir = getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir,"temp.jpg");
      //  Bitmap resizedPic = Bitmap.createScaledBitmap(pic,(int)(pic.getWidth()*0.8),(int)(pic.getHeight()*0.8),true);
       // Matrix matrix = new Matrix();
        //matrix.postRotate(90);
        //Bitmap rotated = Bitmap.createBitmap(resizedPic,0,0,resizedPic.getWidth(),resizedPic.getHeight(),matrix,false);

        OutputStream os;
        try{
            os = new FileOutputStream(imageFile);
            pic.compress(Bitmap.CompressFormat.JPEG,100,os);
            os.flush();
            os.close();
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        pictureName.setText(imageFile.getAbsolutePath());
        return imageFile;
    }
    private void sendImage(){
        final ProgressDialog dialog = ProgressDialog.show(this,"Loading...","Converting to text.",true,false);
        final Thread thread = new Thread(new Runnable(){
           @Override
            public void run(){

               final OCRServiceAPI apiClient = new OCRServiceAPI(apiKey);
               apiClient.convertToText("en",imageFile.getAbsolutePath());

               runOnUiThread(new Runnable(){
                   @Override
                    public void run(){
                       dialog.dismiss();

                       final AlertDialog.Builder alert = new AlertDialog.Builder(SendImage.this);
                       alert.setMessage(apiClient.getResponseText());
                       alert.setPositiveButton(
                               "OK",
                               new DialogInterface.OnClickListener() {

                                   public void onClick(DialogInterface dialog, int id) {

                                   }
                               });
                       if(apiClient.getResponseCode() == RESPONSE_OK){
                           alert.setTitle("Success");
                       }else{
                           alert.setTitle("Failed");
                       }
                       alert.show();
                   }
               });
           }

        });
        thread.start();

    }




}
