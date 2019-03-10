package com.sarthak01.sarthak.filestoringsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity {

    private Button UploadFiles,Myfiles,Logout;
    String ID;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        UploadFiles=(Button)findViewById(R.id.uploadfiles);
        Myfiles=(Button)findViewById(R.id.myfiles);
        Logout=(Button)findViewById(R.id.logout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(OptionActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        ID=getIntent().getExtras().get("ID").toString();


        UploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(OptionActivity.this,MyFiles.class);
                i.putExtra("ID",ID);
                startActivity(i);

            }
        });

        Myfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(OptionActivity.this,FetchFiles.class);
                i.putExtra("ID",ID);
                startActivity(i);
            }
        });

    }
}
