package com.example.firebase.Controller;

import static com.example.firebase.Controller.MainActivity.fs;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.firebase.R;

import java.io.ByteArrayOutputStream;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputText;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> launchGallery;
    private ActivityResultLauncher<Intent> launchCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        inputText = (EditText) findViewById(R.id.enterNoteText);
        imageView = (ImageView) findViewById(R.id.ImageForCreateNote);

        createGalleryLauncher();
        createCameraLauncher();
    }

    private void createGalleryLauncher() {
        launchGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        imageView.setImageURI(intent.getData());
                        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                        fs.saveImage(getBytes(bitmap));
                    }
                }
        );
    }

    private void createCameraLauncher() {
        launchCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        // capture image data
                        Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
                        imageView.setImageBitmap(bitmap);
                        fs.saveImage(getBytes(bitmap));
                    }
                }
        );
    }

    public void createNote(View view){
        fs.addNote(inputText.getText().toString());
    }

    public void cameraButtonPress(View view){
        Log.i("Image upload", "clicked camera");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launchCamera.launch(intent);
    }

    public void galleryOnClick(View view){
        Log.i("Image gallery", "clicked gallery");
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        launchGallery.launch(intent);
    }

    private byte[] getBytes(Bitmap bitmap){
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,bs);
        return bs.toByteArray();
    }
}