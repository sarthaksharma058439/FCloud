package com.sarthak01.sarthak.filestoringsystem;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;

public class DisplayActivity extends AppCompatActivity {

    String SelectItem,ID,url;
    DatabaseReference ref;
    private TextView description,myurl;
    private Button open;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        SelectItem=getIntent().getExtras().get("Selected").toString();
        ID=getIntent().getExtras().get("ID").toString();
        description=(TextView)findViewById(R.id.displaydescription);
        open=(Button)findViewById(R.id.open);
        myurl=(TextView)findViewById(R.id.myurl);



        ref=FirebaseDatabase.getInstance().getReference();

        ref.child(ID).child(SelectItem).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                description.setText("File Description : \n"+dataSnapshot.child("Description").getValue().toString());
                url=dataSnapshot.child("url").getValue().toString();

                myurl.setText(url);
                myurl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ClipboardManager cm = (ClipboardManager)DisplayActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(myurl.getText());
                        Toast.makeText(DisplayActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}
