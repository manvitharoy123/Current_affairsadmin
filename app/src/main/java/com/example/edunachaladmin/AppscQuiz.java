package com.example.edunachaladmin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppscQuiz extends AppCompatActivity {
    List<AppscModelQuiz> appscModelQuizList;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    AppscQuizAdaptor appscQuizAdaptor;
    ProgressBar progressBar;
    String mquestion, moption1, moption2, moption3, moption4, mmonth, mexplanation;
    int mcorrect;
    String extraFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appsc_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar2);
        linearLayoutManager=new LinearLayoutManager(this);
        extraFlag = getIntent().getStringExtra("flag");

                databaseReference = FirebaseDatabase.getInstance().getReference().child("current_affairs").child("appsc");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        recyclerView.addItemDecoration(itemDecorator);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        appscModelQuizList = new ArrayList<>();
        appscQuizAdaptor = new AppscQuizAdaptor(appscModelQuizList,this,extraFlag);
        recyclerView.setAdapter(appscQuizAdaptor);
        databaseReference.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        mquestion=ds.getKey();
                        moption1=ds.child("options").child("a").getValue().toString();
                        moption2=ds.child("options").child("b").getValue().toString();
                        moption3=ds.child("options").child("c").getValue().toString();
                        moption4=ds.child("options").child("d").getValue().toString();
                        mexplanation=ds.child("explanation").getValue().toString().replace("\\n","\n");
                        mcorrect=Integer.parseInt(ds.child("answer").getValue().toString());
                        mmonth=ds.child("month").getValue().toString();
                        AppscModelQuiz appscModelQuiz = new AppscModelQuiz(mquestion,moption1,moption2,moption3,moption4,mexplanation,mmonth,mcorrect);
                        appscModelQuizList.add(appscModelQuiz);
                        appscQuizAdaptor.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(AppscQuiz.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AppscQuiz.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void addNewQues(View view) {
        Intent intent=new Intent(this,AddQuizAppsc.class);
        intent.putExtra("flag",extraFlag);
        startActivity(intent);
    }
}