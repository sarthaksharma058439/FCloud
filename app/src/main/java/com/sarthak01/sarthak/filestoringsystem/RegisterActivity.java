package com.sarthak01.sarthak.filestoringsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText Email,Password;
    private Button RegisterNow;
    FirebaseAuth firebaseAuth;
    private ProgressDialog LoadingBar;
    DatabaseReference Ref;
    String email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email=(EditText)findViewById(R.id.email);
        Password=(EditText)findViewById(R.id.password);
        RegisterNow=(Button)findViewById(R.id.registernow);
        firebaseAuth=FirebaseAuth.getInstance();
        Ref=FirebaseDatabase.getInstance().getReference();
        LoadingBar=new ProgressDialog(this);


        RegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString();
                password = Password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "please enter email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "plaease enter password", Toast.LENGTH_SHORT).show();
                } else {

                    LoadingBar.setTitle("Loading");
                    LoadingBar.setMessage("Please Wait");
                    LoadingBar.show();

                    String myMailId = Email.getText().toString();
                    String value = myMailId.substring(0, myMailId.indexOf('@'));
                    value = value.replace(".", "");


                    final String finalValue = value;
                    (firebaseAuth.createUserWithEmailAndPassword(email, password))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    LoadingBar.dismiss();
                                    if (task.isSuccessful()) {
                                        Ref.child(finalValue).setValue("");

                                        Toast.makeText(RegisterActivity.this, "Registeration Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}
