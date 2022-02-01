package com.example.edunachaladmin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    String mEmail, mPassword;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        progressBar=findViewById(R.id.progressBar3);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            login();
        }
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void loginMe(View view) {
        mEmail=email.getText().toString().trim();
        mPassword=password.getText().toString().trim();
        if(mEmail.equals(""))
        {
            email.setError("Email cannot be Empty");
            return;
        }
        if(mPassword.equals(""))
        {
            password.setError("Password cannot be Empty");
            return;
        }
        if(mEmail.equals("manuroy@gmail.com"))
        {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        login();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        System.out.print(task.getException());
                        Toast.makeText(MainActivity.this, "Error While login: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Use only Admin account", Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {

        Intent intent = new Intent(this,MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}