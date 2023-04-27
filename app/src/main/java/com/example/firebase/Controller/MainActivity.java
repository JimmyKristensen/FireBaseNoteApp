package com.example.firebase.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.firebase.R;
import com.example.firebase.Service.FirebaseService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ArrayAdapter<String> adapter;
    public static FirebaseService fs;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fs = new FirebaseService(this);
        adapter = new ArrayAdapter<>(this, R.layout.row, R.id.rowTextView, fs.getItems());
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void sendToCreatePage(View view){
        Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
        startActivity(intent);
    }

}