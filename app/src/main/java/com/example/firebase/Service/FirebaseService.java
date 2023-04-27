package com.example.firebase.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.firebase.Controller.MainActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseService {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference root;
    private String collectionPath = "notes";
    List<String> items = new ArrayList<>();
    private MainActivity mainActivity;
    public FirebaseService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        root = storage.getReference();
        startListener();
    }

    public void addNote(String note){
        DocumentReference ref = db.collection(collectionPath).document();
        Map<String, String> map = new HashMap<>();
        map.put("text", note);
        ref.set(map);
    }

    public void updateNote(String text, String id){
        DocumentReference ref = db.collection(collectionPath).document(id);
        Map<String, String> map = new HashMap<>();
        map.put("text", text);
        ref.set(map);
    }

    public void deleteNote(String id){
        DocumentReference ref = db.collection(collectionPath).document(id);
        ref.delete();
    }

    public List<String> getItems(){
        return items;
    }

    public void startListener(){
        System.out.println("start called");
        db.collection(collectionPath).addSnapshotListener((snap,error) ->{
            items.clear();
            for(DocumentSnapshot doc: snap.getDocuments()){
                //System.out.println("Doc:" + doc.getData().get("text") + doc.getId());
                items.add(doc.getData().get("text").toString());
            }
            System.out.println(items);
            mainActivity.adapter.notifyDataSetChanged(); // will make the adapter reload
        });
    }

    public void saveImage(byte[] img){
        StorageReference imgRef = root.child(img.toString());
        UploadTask task = imgRef.putBytes(img);
        task.addOnSuccessListener(taskSnapshot -> {
            Log.i("FirebaseUpload Success", "Upload successful");
        });
        task.addOnFailureListener(exception -> {
            Log.i("FirebaseUpload", "Error uploading " + exception);
        });
    }

    public void downloadImage(String name, ImageView imageView){
        StorageReference imgRef = root.child(name);
        imgRef.getBytes(1000000)
                .addOnSuccessListener(bytes -> {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes,0 , bytes.length);
                    imageView.setImageBitmap(bm);
                });
    }
}
