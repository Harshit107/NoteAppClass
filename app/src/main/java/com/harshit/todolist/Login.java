package com.harshit.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button login;
    EditText email;
    EditText pass;
    TextView forget;
    TextView signup;
    public static final String TAG = "Login";

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        forget = findViewById(R.id.forget);
        signup = findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we write our code here
                if (checkInput()) {
                    signinUser(email.getText().toString(), pass.getText().toString());
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You just clicked forget tv", Toast.LENGTH_LONG).show();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "You just clicked signup tv", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void signinUser(String email, String pass) {

        //signin
//        createUserWithEmailAndPassword
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Intent it = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(it);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private boolean checkInput() {

        String userEmail = email.getText().toString();
        String userPass = pass.getText().toString();

        if (userEmail.isEmpty()) {
            email.setError("Field is required");
            email.requestFocus();
            return false;
//            Toast.makeText(getApplicationContext(),"All Field is Required",Toast.LENGTH_LONG).show();
//            return;           re
        }
        if (userPass.isEmpty()) {
            pass.setError("Field is required");
            return false;
        }

        return true;

    }

}