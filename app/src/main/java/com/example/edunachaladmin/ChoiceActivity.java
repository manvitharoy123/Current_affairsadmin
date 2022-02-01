package com.example.edunachaladmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ChoiceActivity extends AppCompatActivity {
    private Button openTeachersActivityBtn,openUploadActivityBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        openTeachersActivityBtn = findViewById ( R.id.openTeachersActivityBtn );
        openUploadActivityBtn = findViewById ( R.id.openUploadActivityBtn );

        openTeachersActivityBtn.setOnClickListener(view -> {
            Intent i = new Intent(ChoiceActivity.this, ItemsActivity.class);
            startActivity(i);
        });
        openUploadActivityBtn.setOnClickListener(view -> {
            Intent i = new Intent(ChoiceActivity.this, UploadActivity.class);
            startActivity(i);
        });
    }
}