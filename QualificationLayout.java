package com.example.sahebojha.doctorconsult;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by sahebojha on 1/9/2018.
 */

public class QualificationLayout extends LinearLayout {

    private EditText qualification;
    private Button upload;
    Context context;
    final int PDF_REQ_CODE = 111;
    Uri fileUri;
    GetFile getFile;

    public QualificationLayout(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getFile = (GetFile) context;
        setOrientation(LinearLayout.HORIZONTAL);
//        setMinimumWidth(300);
//        setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        this.qualification = new EditText(context);
        this.qualification.setHint("Qualificaion");
        this.qualification.setWidth(500);
        addView(this.qualification);

        this.upload = new Button(context);
        this.upload.setText("Upload");
        addView(this.upload);
        upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                getFile.onGetFile(view);
//                Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public EditText getQualification(){
        return this.qualification;
    }

    public void setFileUri(Uri uri) {
        this.fileUri = uri;
    }

    public  Uri getFileUri(){
        return this.fileUri;
    }



    public interface GetFile {
        public int onGetFile(View view);
    }

}
