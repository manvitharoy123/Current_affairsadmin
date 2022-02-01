package com.example.edunachaladmin;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class MainActivity2 extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth=FirebaseAuth.getInstance();
        Button bf=findViewById(R.id.button26);
        Button teacher=findViewById(R.id.button7);
        teacher.setOnClickListener(View->{
Intent intent=new Intent(MainActivity2.this,ChoiceActivity.class);
startActivity(intent);
        });
        bf.setOnClickListener(View ->{
    Intent intent=new Intent(MainActivity2.this,uploadfile.class);
    startActivity(intent);
        });
        Button bf1=findViewById(R.id.button5);
        bf1.setOnClickListener(View ->{
            Intent intent=new Intent(MainActivity2.this,AddCurrentAffairs.class);
            startActivity(intent);
        });
        Button bf2=findViewById(R.id.button6);
        bf2.setOnClickListener(View ->{
            Intent intent=new Intent(MainActivity2.this,AppscQuiz.class);
            startActivity(intent);
        });
    }
}