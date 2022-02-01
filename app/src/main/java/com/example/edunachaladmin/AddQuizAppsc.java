package com.example.edunachaladmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class AddQuizAppsc extends AppCompatActivity {
    EditText question, option1, option2, option3, option4, month, explanation, correct;
    String mquestion, moption1, moption2, moption3, moption4, mmonth, mexplanation, mcorrect;
    Button button;
    ImageButton imageButton;
    ProgressBar progressBar;
    String extraFlag;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz_appsc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view = findViewById(R.id.quizTemp);
        extraFlag = getIntent().getStringExtra("flag");

                databaseReference = FirebaseDatabase.getInstance().getReference().child("current_affairs").child("appsc");
        imageButton=view.findViewById(R.id.imageButton);
        imageButton.setVisibility(View.INVISIBLE);
        imageButton.setEnabled(false);
        button=view.findViewById(R.id.button3);
        question=view.findViewById(R.id.editTextTextMultiLine);
        option1=view.findViewById(R.id.editTextTextPersonName);
        option2=view.findViewById(R.id.editTextTextPersonName2);
        option3=view.findViewById(R.id.editTextTextPersonName3);
        option4=view.findViewById(R.id.editTextTextPersonName4);
        explanation=view.findViewById(R.id.editTextTextMultiLine2);
        progressBar=view.findViewById(R.id.progressBar);
        month=view.findViewById(R.id.editTextDate);
        correct=view.findViewById(R.id.editTextNumberDecimal);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mquestion=question.getText().toString();
                moption1=option1.getText().toString();
                moption2=option2.getText().toString();
                moption3=option3.getText().toString();
                moption4=option4.getText().toString();
                mmonth=month.getText().toString();
                mcorrect=correct.getText().toString();
                mexplanation=explanation.getText().toString();
                if(mquestion.isEmpty() || mcorrect.isEmpty() || mexplanation.isEmpty() || mmonth.isEmpty() || moption1.isEmpty() || moption2.isEmpty() || moption3.isEmpty() || moption4.isEmpty())
                {
                    Toast.makeText(AddQuizAppsc.this, "Some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(mcorrect)>4 || Integer.parseInt(mcorrect)<1)
                {
                    correct.setError("Option value should be limited to 1-4");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                Map map = new HashMap();
                map.put("a",moption1);
                map.put("b",moption2);
                map.put("c",moption3);
                map.put("d",moption4);

                Map finalMap = new HashMap();
                finalMap.put("options",map);
                finalMap.put("timestamp", ServerValue.TIMESTAMP);
                finalMap.put("month", mmonth);
                finalMap.put("answer", Integer.parseInt(mcorrect));
                finalMap.put("explanation",mexplanation);
                databaseReference.child(mquestion).setValue(finalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(AddQuizAppsc.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            question.setText("");
                            option1.setText("");
                            option2.setText("");
                            option3.setText("");
                            option4.setText("");
                            month.setText("");
                            correct.setText("");
                            explanation.setText("");
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else {
                            Toast.makeText(AddQuizAppsc.this, "Error occured "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
    }
}