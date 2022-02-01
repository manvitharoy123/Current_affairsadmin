package com.example.edunachaladmin;

import android.app.Activity;
import android.content.Context;
import android.text.style.IconMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edunachaladmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppscQuizAdaptor extends RecyclerView.Adapter<AppscQuizAdaptor.MyViewHolder>{
    List<AppscModelQuiz> appscModelQuizs;
    Context context;
    String extraFlag;

    public AppscQuizAdaptor(List<AppscModelQuiz> appscModelQuizs, Context context, String extraFlag) {
        this.appscModelQuizs = appscModelQuizs;
        this.context = context;
        this.extraFlag = extraFlag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appsc_quiz_temp,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppscModelQuiz appscModelQuiz = appscModelQuizs.get(position);
        holder.question.setText(appscModelQuiz.getQuestion());
        holder.option1.setText(appscModelQuiz.getOption1());
        holder.option2.setText(appscModelQuiz.getOption2());
        holder.option3.setText(appscModelQuiz.getOption3());
        holder.option4.setText(appscModelQuiz.getOption4());
        holder.explanation.setText(appscModelQuiz.getExplanation());
        holder.answer.setText(String.valueOf(appscModelQuiz.getCorrect()));
        holder.month.setText(appscModelQuiz.getMonth());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.databaseReference.child(appscModelQuiz.getQuestion()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(context, "DataRemoved", Toast.LENGTH_SHORT).show();
                            appscModelQuizs.remove(position);
                            notifyDataSetChanged();
                            holder.progressBar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "An Error Occurred: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moption1,moption2,moption3,moption4,mmonth,mcorrect,mexplanation;
                moption1=holder.option1.getText().toString().trim();
                moption2=holder.option2.getText().toString().trim();
                moption3=holder.option3.getText().toString().trim();
                moption4=holder.option4.getText().toString().trim();
                mmonth=holder.month.getText().toString().trim();
                mcorrect=holder.answer.getText().toString().trim();
                mexplanation=holder.explanation.getText().toString().trim();
                if(mcorrect.isEmpty() || mexplanation.isEmpty() || mmonth.isEmpty() || moption1.isEmpty() || moption2.isEmpty() || moption3.isEmpty() || moption4.isEmpty())
                {
                    Toast.makeText(context, "Some fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(mcorrect)>4 || Integer.parseInt(mcorrect)<1)
                {
                    holder.answer.setError("Option value should be limited to 1-4");
                }
                else
                {
                    holder.progressBar.setVisibility(View.VISIBLE);
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

                    holder.databaseReference.child(appscModelQuiz.getQuestion()).updateChildren(finalMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(context, "Values Updated", Toast.LENGTH_SHORT).show();
                                appscModelQuizs.get(position).setCorrect(Integer.parseInt(mcorrect));
                                appscModelQuizs.get(position).setExplanation(mexplanation);
                                appscModelQuizs.get(position).setMonth(mmonth);
                                appscModelQuizs.get(position).setOption1(moption1);
                                appscModelQuizs.get(position).setOption2(moption2);
                                appscModelQuizs.get(position).setOption3(moption3);
                                appscModelQuizs.get(position).setOption4(moption4);
                                notifyDataSetChanged();
                                holder.progressBar.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                holder.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(context, "Failed to update values: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appscModelQuizs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        EditText question, month, answer, option1, option2, option3, option4, explanation;
        Button button;
        ImageButton imageButton;
        DatabaseReference databaseReference;
        ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("current_affairs").child("appsc");
            imageButton=itemView.findViewById(R.id.imageButton);
            progressBar=itemView.findViewById(R.id.progressBar);
            button=itemView.findViewById(R.id.button3);
            question= itemView.findViewById(R.id.editTextTextMultiLine);
            question.setKeyListener(null);
            question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You can not edit question now\nYou can delete this and re upload it again", Toast.LENGTH_SHORT).show();
                }
            });
            option1=itemView.findViewById(R.id.editTextTextPersonName);
            option2=itemView.findViewById(R.id.editTextTextPersonName2);
            option3=itemView.findViewById(R.id.editTextTextPersonName3);
            option4=itemView.findViewById(R.id.editTextTextPersonName4);
            explanation=itemView.findViewById(R.id.editTextTextMultiLine2);
            month=itemView.findViewById(R.id.editTextDate);
            answer=itemView.findViewById(R.id.editTextNumberDecimal);
        }
    }
}