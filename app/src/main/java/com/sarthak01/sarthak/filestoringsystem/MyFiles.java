package com.sarthak01.sarthak.filestoringsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class MyFiles extends AppCompatActivity {

    private Button SelectFile,Upload;
    private EditText Name,Description;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    String ID;

    Uri pdfUri; //uri are actually urls for loacal storage          //global

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);

        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        Name=(EditText)findViewById(R.id.name);
        Description=(EditText)findViewById(R.id.description);

        SelectFile=(Button)findViewById(R.id.selectfile);
        Upload=(Button)findViewById(R.id.upload);

        ID=getIntent().getExtras().get("ID").toString();

        SelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectPdf();
            }
        });
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pdfUri!=null)
                    uploadPdf(pdfUri);
                else
                    Toast.makeText(MyFiles.this, "Select a File", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadPdf(Uri pdfUri) {

        String name1 = Name.getText().toString();
        String description1 = Description.getText().toString();

        if (TextUtils.isEmpty(name1)) {
            Toast.makeText(this, "please enter file name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description1)) {
            Toast.makeText(this, "please enter description", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog = new ProgressDialog(this);

            progressDialog.setTitle("uploading file");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();

            final String fileName = Name.getText().toString();
            final StorageReference storageReference = storage.getReference();

            storageReference.child("Uploads").child(fileName).putFile(pdfUri)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getDownloadUrl().toString();
                            DatabaseReference reference = database.getReference();

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("url", url);
                            map.put("Description", Description.getText().toString());


                            reference.child(ID).child(fileName).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(MyFiles.this, "file successfully upload", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MyFiles.this, "file not successfully upload", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }

                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(currentProgress);
                    if (currentProgress == 100) {
                        progressDialog.dismiss();
                    }
                }
            });

        }
    }

    private void SelectPdf() {

        Intent intent=new Intent();
        intent.setType("application/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==10 && resultCode==RESULT_OK && data!=null)
        {
            pdfUri=data.getData();
        }
        else
        {
            Toast.makeText(this, "Please select a file..", Toast.LENGTH_SHORT).show();
        }
    }
}
