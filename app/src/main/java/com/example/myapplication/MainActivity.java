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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
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
    Button delete,rename;
    LinearLayout functiondrawer;
    File[] files;
    ArrayList<String> selectedPositions=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;

    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.filesfound);
        com.example.myapplication.RecyclerViews.RecyclerViewAdapterHome adapter = new com.example.myapplication.RecyclerViews.RecyclerViewAdapterHome(MainActivity.this,fileList,functiondrawer,selectedPositions,rename);
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
        rename=findViewById(R.id.rename);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"All permissions given",3000).show();
            fileList=new ArrayList<String>();
            createNewViewOrRefresh();


        }
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder renameDialog=new AlertDialog.Builder(MainActivity.this);
                renameDialog.setTitle("Rename to");
                final EditText input=new EditText((MainActivity.this));
                input.setText(selectedPositions.get(0));
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                renameDialog.setView(input);
                renameDialog.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File dir = Environment.getExternalStorageDirectory();
                        if(dir.exists()){
                            File from = new File(dir,selectedPositions.get(0));
                            File to = new File(dir,String.valueOf(input.getText()));
                            if(from.exists())
                                from.renameTo(to);
                            createNewViewOrRefresh();
                        }
                    }
                });
                renameDialog.show();
            }
        });
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
                        createNewViewOrRefresh();


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

    private void createNewViewOrRefresh() {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        files = root.listFiles();
        fileList.clear();
        if (files != null) {
            for (File file : files) {
                fileList.add(file.getPath().substring(file.getPath().lastIndexOf('/') + 1));
            }
            Toast.makeText(this, fileList.get(0), Toast.LENGTH_SHORT).show();
            initRecyclerView();
        }
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