package com.sarthak01.sarthak.filestoringsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FetchFiles extends AppCompatActivity {


    private ListView ListOfFiles;
    String ID,url;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list=new ArrayList<>();

    DatabaseReference mRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_files);

        ID=getIntent().getExtras().get("ID").toString();

        ListOfFiles=(ListView)findViewById(R.id.listoffiles);


        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        ListOfFiles.setAdapter(arrayAdapter);

        mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Set<String> set=new HashSet<String>();
                Iterator i=dataSnapshot.getChildren().iterator();
                while (i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                list.clear();
                list.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ListOfFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String selectedFromList = (String) ListOfFiles.getItemAtPosition(position);
                Intent i=new Intent(FetchFiles.this,DisplayActivity.class);
                i.putExtra("Selected",selectedFromList);
                i.putExtra("ID",ID);
                startActivity(i);
            }
        });

        ListOfFiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {



                final String deleteditem=(String) ListOfFiles.getItemAtPosition(position);


                final AlertDialog.Builder builder=new AlertDialog.Builder(FetchFiles.this);
                builder.setTitle("Do You Want To Delete File ..? ");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mRef.child(ID).child(deleteditem).removeValue();


                    }
                });
                builder.show();

                arrayAdapter.remove(deleteditem);
                arrayAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}
