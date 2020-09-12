package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.lang.*;
import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> fileList;
    Button delete;
    LinearLayout functiondrawer;
    File[] files;
    ArrayList<String> selectedPositions=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.filesfound);
        com.example.myapplication.RecyclerViews.RecyclerViewAdapterHome adapter = new com.example.myapplication.RecyclerViews.RecyclerViewAdapterHome(MainActivity.this,fileList,functiondrawer,selectedPositions);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        delete=findViewById(R.id.delete);
        functiondrawer=findViewById(R.id.functions);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"All permissions given",3000).show();
            fileList=new ArrayList<String>();
            File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            files = root.listFiles();
            fileList.clear();
            if (files != null) {
                for (File file : files) {
                    fileList.add(file.getPath().substring(file.getPath().lastIndexOf('/')+1));
                }
                Toast.makeText(this, fileList.get(0), Toast.LENGTH_SHORT).show();
                initRecyclerView();


//                recyclerView=findViewById(R.id.r1);
//                recyclerView.setHasFixedSize(true);
//                recyclerView.setLayoutManager(layoutManager);
//                mAdapter = new Adapter(getApplicationContext());
//                mAdapter.add(fileList);
//                recyclerView.setAdapter(mAdapter);
            }


        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder deleteDialog= new AlertDialog.Builder((MainActivity.this));
                deleteDialog.setTitle("Delete");
                deleteDialog.setMessage("Do you really want to delete?");
                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, String.valueOf(selectedPositions.get(0))+"  "+String.valueOf(fileList.size()), Toast.LENGTH_SHORT).show();
                            for(int i=0;i<fileList.size();i++){
                                if(selectedPositions.contains(fileList.get(i))){
                                    Toast.makeText(MainActivity.this, "Deleting"+fileList.get(i), Toast.LENGTH_SHORT).show();
                                    deleteFileorFolder(files[i]);
                                }
                            }
                        File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                        files = root.listFiles();
                        fileList.clear();
                        if (files != null) {
                            for (File file : files) {
                                fileList.add(file.getPath().substring(file.getPath().lastIndexOf('/')+1));
                            }
                            Toast.makeText(MainActivity.this, fileList.get(0), Toast.LENGTH_SHORT).show();
                            initRecyclerView();
                        }


                    }
                });
                deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                deleteDialog.show();
            }
        });

    }
    private void deleteFileorFolder(File fileorfolder){
        if(fileorfolder.isDirectory()){
            if(fileorfolder.list().length==0){
                boolean delete = fileorfolder.delete();
                Toast.makeText(this, "Success: "+delete, Toast.LENGTH_SHORT).show();
            }
            else{
                String files[]=fileorfolder.list();
                for(String temp:files){
                    File fileToDelete= new File(fileorfolder,temp);
                    deleteFileorFolder(fileToDelete);
                }
                if(fileorfolder.list().length==0){
                    boolean delete1 = fileorfolder.delete();
                    Toast.makeText(this, "Success: "+delete, Toast.LENGTH_SHORT).show();

                }
            }
        }
        else{
            boolean delete = fileorfolder.delete();
            Toast.makeText(this, "Success: "+delete, Toast.LENGTH_SHORT).show();

        }
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            layoutManager = new LinearLayoutManager(getApplicationContext());
            ArrayList<String> fileList=new ArrayList<String>();
            File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            File[] files = root.listFiles();
            fileList.clear();
            if (files != null) {
                for (File file : files) {
                    fileList.add(file.getPath());
                }
                Toast.makeText(this, fileList.get(0), Toast.LENGTH_SHORT).show();


            }

        }
    }
}