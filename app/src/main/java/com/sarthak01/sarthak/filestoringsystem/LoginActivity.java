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

public class LoginActivity extends AppCompatActivity {

    private Button UserLogin;
    private EditText UserEmail,UserPassword;
    private ProgressDialog LoadingBar;
    FirebaseAuth firebaseAuth;
    String Phone,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserEmail=(EditText)findViewById(R.id.userEmail);
        UserPassword=(EditText)findViewById(R.id.userPassword);
        UserLogin=(Button)findViewById(R.id.userLogin);
        LoadingBar=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();




        UserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = UserEmail.getText().toString();
                password = UserPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "please enter email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
                } else {

                    String myMailEmailId = UserEmail.getText().toString();
                    String value = myMailEmailId.substring(0, myMailEmailId.indexOf('@'));
                    value = value.replace(".", "");

                    LoadingBar.setTitle("Loading");
                    LoadingBar.setMessage("Please Wait");
                    LoadingBar.show();
                    final String finalValue = value;
                    (firebaseAuth.signInWithEmailAndPassword(email, password))
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    LoadingBar.dismiss();

                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(LoginActivity.this, OptionActivity.class);
                                        i.putExtra("ID", finalValue);
                                        startActivity(i);

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
