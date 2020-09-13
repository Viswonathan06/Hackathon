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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import com.example.myapplication.RecyclerViews.UI.DisplayFoldersLayer2;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.lang.*;
import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    public static String copyPath;
    public static Boolean move1;
    public static Boolean copied1=false;
    Boolean copiedLevel2=false;
    Boolean moveLevel2=false;
    String copyPathLevel2;
    ArrayList<String> fileList;
    Button delete,paste,rename,copy,move;
    LinearLayout functiondrawer;
    File[] files;
    ArrayList<String> selectedPositions=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;


    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.filesfound);
        com.example.myapplication.RecyclerViews.RecyclerViewAdapterHome adapter = new com.example.myapplication.RecyclerViews.RecyclerViewAdapterHome(MainActivity.this,fileList,functiondrawer,selectedPositions,rename,copy,paste);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }
    @Override
    protected void onResume() {

        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        copiedLevel2=preferences.getBoolean("copiedLevel2",false);
        copyPathLevel2=preferences.getString("copyPathLevel2","");
        moveLevel2=preferences.getBoolean("moveLevel2",false);
        if(copiedLevel2||moveLevel2){
            paste.setVisibility(View.VISIBLE);
            copy.setVisibility(View.GONE);
        }
        Toast.makeText(this, "Resuming with copy value "+copiedLevel2+" move value "+moveLevel2, Toast.LENGTH_SHORT).show();
        createNewViewOrRefresh();
    }
    @Override
    public void finish(){
        super.finish();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("copiedLevel2", false);
        editor.putString("copyPathLevel2","");
        editor.putBoolean("moveLevel2",false);
        editor.commit();

    }
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        delete=findViewById(R.id.delete);
        functiondrawer=findViewById(R.id.functions);
        rename=findViewById(R.id.rename);
        paste=findViewById(R.id.paste);
        paste.setVisibility(View.GONE);
        copy=findViewById(R.id.copy);
        move=findViewById(R.id.move);
        /*if(copied){
            paste.setVisibility(View.VISIBLE);
            copy.setVisibility(View.GONE);
        }

         */







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

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositions.isEmpty()){
                    Toast.makeText(MainActivity.this, "You have not selected a file!", Toast.LENGTH_SHORT).show();
                }else {
                    copyPath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/" +selectedPositions.get(0);
                    copied1=true;


                }
            }
        });
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositions.isEmpty()){
                    Toast.makeText(MainActivity.this, "You have not selected a file!", Toast.LENGTH_SHORT).show();
                }else {
                    copyPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + selectedPositions.get(0);
                    copied1 = false;
                    move1=true;
                    copy.setVisibility(View.GONE);
                }
            }
        });
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(copiedLevel2||moveLevel2) {
                    Toast.makeText(MainActivity.this, copyPathLevel2, Toast.LENGTH_SHORT).show();
                    File src = new File(copyPathLevel2);
                    File dest = new File(Environment.getExternalStorageDirectory().getAbsolutePath()  + copyPathLevel2.substring(copyPathLevel2.lastIndexOf('/')));
                    Toast.makeText(MainActivity.this, "destination= " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + copyPathLevel2.substring(copyPathLevel2.lastIndexOf('/')+1), Toast.LENGTH_SHORT).show();
                    copy(src,dest,moveLevel2);
                }

                createNewViewOrRefresh();

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
    private void copy(File sourceLocation, File targetLocation,Boolean move1){
        try {



            // moving the file to another directory
            if(move1==true){

                if(sourceLocation.renameTo(targetLocation)){
                    Toast.makeText(this, "File moves successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "File moves failed", Toast.LENGTH_SHORT).show();
                }

            }

            // we will copy the file
            else{

                // make sure the target file exists

                if(sourceLocation.exists()){

                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                    out.close();

                    Toast.makeText(this, "Copy File Successful", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Source File Missing", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (NullPointerException | FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "NFE", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

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