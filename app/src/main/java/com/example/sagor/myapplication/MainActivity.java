package com.example.sagor.myapplication;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_STORAGE = 10000;
    private static final int READ_REQUEST_CODE = 10000;

     Button b_ReadFile;
    TextView tv_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request permission

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

                != getPackageManager().PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        }

        b_ReadFile = (Button) findViewById(R.id.b_ReadFile);
        tv_output = (TextView) findViewById(R.id.tv_textView);
        b_ReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           performFileSearch();

            }
        });

    }
    // read content of File

    private String readText(String input) {
        File file = new File(input);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text.toString();
    }
    //select File from storage

    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                Uri uri = data.getData();
                String path = uri.getPath();
                path = path.substring(path.indexOf(":" + 1));
                Toast.makeText(this, " " + path, Toast.LENGTH_SHORT).show();
                tv_output.setText(readText(path));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission_Granted", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(this, "Not permission_Granted", Toast.LENGTH_SHORT).show();

            }
        }
    }
}

