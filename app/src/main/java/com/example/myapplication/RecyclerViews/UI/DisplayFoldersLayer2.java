package com.example.myapplication.RecyclerViews.UI;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.MainActivity;
import com.example.myapplication.RecyclerViews.RecyclerViewAdapterLevel2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.FileUtils;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class DisplayFoldersLayer2 extends AppCompatActivity {
    public static String copyPathLevel2;
    public static Boolean copiedLevel2=false;
    String folderName;
    ArrayList<String> fileList = new ArrayList<>();
    ArrayList<String> selectedPositions = new ArrayList<>();
    LinearLayout functionList;
    FloatingActionButton fab ;
    Button delete,rename,copy,paste;

    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.Level1recycle);
        RecyclerViewAdapterLevel2 adapter = new RecyclerViewAdapterLevel2(DisplayFoldersLayer2.this, fileList, functionList, selectedPositions,rename);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_folders_layer2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        delete = findViewById(R.id.deletelevel1);
        setSupportActionBar(toolbar);
        rename=findViewById(R.id.rename);
        paste=findViewById(R.id.paste);
        paste.setVisibility(View.GONE);
        copy=findViewById(R.id.copy);
        if(MainActivity.copied1){
            paste.setVisibility(View.VISIBLE);
            copy.setVisibility(View.GONE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        folderName = getIntent().getStringExtra("Name of folder");
        fileList = new ArrayList<String>();
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName);
        final File[] files = root.listFiles();
        fileList.clear();
        if (files != null) {
            for (File file : files) {
                fileList.add(file.getPath().substring(file.getPath().lastIndexOf('/') + 1));
            }
            Toast.makeText(this, fileList.get(0), Toast.LENGTH_SHORT).show();
            initRecyclerView();
        }
        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder renameDialog=new AlertDialog.Builder(DisplayFoldersLayer2.this);
                renameDialog.setTitle("Rename to");
                final EditText input=new EditText((DisplayFoldersLayer2.this));
                input.setText(selectedPositions.get(0));
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                renameDialog.setView(input);
                renameDialog.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File dir = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
                        if(dir.exists()){
                            File from = new File(dir,selectedPositions.get(0));
                            File to = new File(dir,String.valueOf(input.getText()));
                            if(from.exists())
                                from.renameTo(to);
                            RefreshViewOrNewView();
                        }
                    }
                });
                renameDialog.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DisplayFoldersLayer2.this, "Adding new folder!", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder newFolderDialog=new AlertDialog.Builder(DisplayFoldersLayer2.this);
                newFolderDialog.setTitle("New Folder");
                final EditText input=new EditText(DisplayFoldersLayer2.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                newFolderDialog.setView(input);
                newFolderDialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final File newFolder=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName+"/"+input.getText());
                        if(!newFolder.exists()){
                            boolean mkdir = newFolder.mkdir();
                            RefreshViewOrNewView();
                            Toast.makeText(DisplayFoldersLayer2.this, "Folder created: "+mkdir, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                newFolderDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newFolderDialog.show();


            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPositions.isEmpty()){
                    Toast.makeText(DisplayFoldersLayer2.this, "You have not selected a file!", Toast.LENGTH_SHORT).show();
                }else {
                    copyPathLevel2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+folderName+"/" + selectedPositions.get(0);
                    copiedLevel2=true;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DisplayFoldersLayer2.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("copiedLevel2", copiedLevel2);
                    editor.putString("copyPathLevel2",copyPathLevel2);
                    editor.commit();

                }
            }
        });
        paste.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(MainActivity.copied1) {
                    Toast.makeText(DisplayFoldersLayer2.this, MainActivity.copyPath, Toast.LENGTH_SHORT).show();
                    File src = new File(MainActivity.copyPath);

                    File dest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + MainActivity.copyPath.substring(MainActivity.copyPath.lastIndexOf('/')+1));
                    Toast.makeText(DisplayFoldersLayer2.this, "destination= " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + MainActivity.copyPath.substring(MainActivity.copyPath.lastIndexOf('/') + 1), Toast.LENGTH_SHORT).show();

                    copy(src, dest,MainActivity.move1);
                    MainActivity.copied1=false;
                    MainActivity.copyPath="";
                }
                RefreshViewOrNewView();
            }

        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder deleteDialog = new AlertDialog.Builder((DisplayFoldersLayer2.this));
                deleteDialog.setTitle("Delete");
                deleteDialog.setMessage("Do you really want to delete?");
                deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DisplayFoldersLayer2.this, String.valueOf(selectedPositions.get(0)) + "  " + String.valueOf(fileList.size()), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < fileList.size(); i++) {
                            if (selectedPositions.contains(fileList.get(i))) {
                                Toast.makeText(DisplayFoldersLayer2.this, "Deleting" + fileList.get(i), Toast.LENGTH_SHORT).show();
                                deleteFileorFolder(files[i]);
                            }
                        }
                        fileList = new ArrayList<String>();
                        RefreshViewOrNewView();
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




    private void RefreshViewOrNewView() {
        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName);
        final File[] files = root.listFiles();
        fileList.clear();
        if (files != null) {
            for (File file : files) {
                fileList.add(file.getPath().substring(file.getPath().lastIndexOf('/') + 1));
            }
            Toast.makeText(DisplayFoldersLayer2.this, fileList.get(0), Toast.LENGTH_SHORT).show();
            initRecyclerView();
        }
    }

    private void deleteFileorFolder(File fileorfolder) {
        if (fileorfolder.isDirectory()) {
            if (fileorfolder.list().length == 0) {
                boolean delete = fileorfolder.delete();
                Toast.makeText(this, "Success: " + delete, Toast.LENGTH_SHORT).show();
            } else {
                String files[] = fileorfolder.list();
                for (String temp : files) {
                    File fileToDelete = new File(fileorfolder, temp);
                    deleteFileorFolder(fileToDelete);
                }
                if (fileorfolder.list().length == 0) {
                    boolean delete1 = fileorfolder.delete();
                    Toast.makeText(this, "Success: " + delete, Toast.LENGTH_SHORT).show();

                }
            }
        } else {
            boolean delete = fileorfolder.delete();
            Toast.makeText(this, "Success: " + delete, Toast.LENGTH_SHORT).show();

        }
    }



//
//    {
//        @Override
//        public void onClick (View view){
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
//    }
//    });
//}

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"fadein-to-fadeout");

    }


}
