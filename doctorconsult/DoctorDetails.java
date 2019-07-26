package com.example.sahebojha.doctorconsult;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.zip.Inflater;

public class DoctorDetails extends AppCompatActivity implements QualificationLayout.GetFile {

    private Spinner specialist;
    private EditText aadhar;
    private Button add, remove, submit;
    private LinearLayout qualificationLayout;
    private ArrayList<QualificationLayout> datas;
    final int PDF_REQ_CODE = 111;
    private Uri filePath;
    private int currentIndex ;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        this.id = getIntent().getIntExtra("id", 0);
        add = (Button) findViewById(R.id.add);
        remove = (Button) findViewById(R.id.remove);
        submit = (Button) findViewById(R.id.submit);
        datas = new ArrayList<>();

        qualificationLayout = (LinearLayout) findViewById(R.id.qualificationLayout);

//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View rowView = inflater.inflate(R.layout.qualification_layout, null);
//        qualificationLayout.addView(rowView);
//        datas.add(rowView)



        /*PREVIOUS CODE*/
        QualificationLayout qLayout = new QualificationLayout(DoctorDetails.this, null);
        datas.add(qLayout);

        qualificationLayout.addView(qLayout);
        remove.setEnabled(false);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datas.size() == 5)
                {
                    add.setEnabled(false);

                }
                else{
                    remove.setEnabled(true);
                    QualificationLayout qLayout = new QualificationLayout(DoctorDetails.this, null);
                    datas.add(qLayout);
                    qualificationLayout.addView(qLayout);
                }

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datas.size() == 1)
                {
                     remove.setEnabled(false);
                }
                else {
                    add.setEnabled(true);
                    qualificationLayout.removeViewAt(qualificationLayout.getChildCount() - 1);
                    datas.remove(datas.size() - 1);
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmit(view);
                Toast.makeText(DoctorDetails.this,"OnSubmit()", Toast.LENGTH_SHORT ).show();
            }
        });

    }


    public void onSubmit(View view) {
        for(int i = 0;i<datas.size(); i++)
        {
            if(datas.get(i).getFileUri().getPath() != null){
                if(!datas.get(i).getQualification().getText().toString().equals(""))
                    uploadFile(String.valueOf(id), datas.get(i).getQualification().getText().toString(),
                        datas.get(i).getFileUri().getPath());
                else
                    Toast.makeText(getApplicationContext(), "Fill the field", Toast.LENGTH_SHORT).show();
            }

        }

        Toast.makeText(getApplicationContext(), "All files uploades succesfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void uploadFile(String id, String qualification, String path) {
        try{
            String uploadid = UUID.randomUUID().toString();
            new MultipartUploadRequest(this, uploadid, Constants.DOCTOR_QUALIFICATION)
                    .addFileToUpload(path, "qualificationPdf")
                    .addParameter("qualification", qualification)
                    .addParameter("id", id)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(3)
                    .startUpload();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            datas.get(this.currentIndex).setFileUri(filePath);
            Toast.makeText(getApplicationContext(), String.valueOf(this.currentIndex)+ "---" +datas.get(this.currentIndex).getFileUri().getPath(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public int onGetFile(View view) {

        int index = qualificationLayout.indexOfChild((View) view.getParent());
        this.currentIndex = index;

        Intent fileChooser = new Intent(Intent.ACTION_GET_CONTENT);
        fileChooser.addCategory(Intent.CATEGORY_OPENABLE);
        fileChooser.setType("application/pdf");
        startActivityForResult(Intent.createChooser(fileChooser, "Select pdf"), PDF_REQ_CODE);

        /*TODO....................................*/

        Log.d("index",String.valueOf(index));
        Toast.makeText(getApplicationContext(), datas.get(index).getQualification().getText().toString(), Toast.LENGTH_SHORT).show();
        return 1;
    }

//    public void chooseFile(View view) {
//        int index = qualificationLayout.indexOfChild((View) view.getParent());
//        Log.d("index",String.valueOf(index));
//        Toast.makeText(getApplicationContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();
//    }
}
