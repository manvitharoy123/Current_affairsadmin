package com.example.edunachaladmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddCurrentAffairs extends AppCompatActivity {
    View view;
    EditText editText, editText2, editText3;
    String title, tag, content;
    String date;
    Button button, button2;
    ImageButton imageButton;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_current_affairs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view = findViewById(R.id.addCurrentAffairsTemp);
        progressBar = findViewById(R.id.progressBar6);
        editText = view.findViewById(R.id.editTextTextPersonName5);
        editText2 = view.findViewById(R.id.editTextTextPersonName6);
        editText3 = view.findViewById(R.id.editTextTextMultiLine3);
        button = view.findViewById(R.id.button14);
        button2 = view.findViewById(R.id.button15);
        button2.setText("Save");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("current_affairs").child("universal");
        imageButton = view.findViewById(R.id.imageButton3);
        imageButton.setEnabled(false);
        imageButton.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCurrentAffairs.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date=dayOfMonth+" "+(month+1)+" "+year;
                        button.setText(date.replace(" ","/"));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker();
                datePickerDialog.show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editText.getText().toString().trim();
                tag = editText2.getText().toString().trim();
                content = editText3.getText().toString().trim();
                if(title.isEmpty())
                {
                    editText.setError("Required field to be displayed as heading of current affairs");
                    return;
                }
                if(tag.isEmpty()){
                    editText2.setError("Required field (e.g. National, International, Sport)");
                    return;
                }
                if(content.isEmpty()){
                    editText3.setError("Required field to be displayed when user click to expand the particular current affair");
                    return;
                }
                if(date==null || date.isEmpty())
                {
                    Toast.makeText(AddCurrentAffairs.this, "Please Select the date before moving forward", Toast.LENGTH_SHORT).show();
                    return;
                }
                button2.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                Map map = new HashMap();
                map.put("title",title);
                map.put("tag",tag);
                map.put("content",content);
                map.put("timestamp", ServerValue.TIMESTAMP);
                DatabaseReference pushRefrence = databaseReference.child(date).push();
                String pushId = pushRefrence.getKey();
                Map map1 = new HashMap();
                map1.put(pushId,map);
                map1.put("timestamp",ServerValue.TIMESTAMP);
                databaseReference.child(date).updateChildren(map1, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error == null)
                        {
                            button.setText("Select Date");
                            editText.setText("");
                            editText2.setText("");
                            editText3.setText("");
                            Toast.makeText(AddCurrentAffairs.this, "Updated", Toast.LENGTH_SHORT).show();
                            button2.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Toast.makeText(AddCurrentAffairs.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            button2.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }
}