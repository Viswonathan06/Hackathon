package com.example.myapplication.RecyclerViews.UI;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.myapplication.MainActivity;
import com.example.myapplication.RecyclerViews.RecyclerViewAdapterLevel2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class DisplayFoldersLayer2 extends AppCompatActivity {
    String folderName;
    ArrayList<String> fileList = new ArrayList<>();
    ArrayList<String> selectedPositions = new ArrayList<>();
    LinearLayout functionList;
    Button delete;

    void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.Level1recycle);
        RecyclerViewAdapterLevel2 adapter = new RecyclerViewAdapterLevel2(DisplayFoldersLayer2.this, fileList, functionList, selectedPositions);
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
                        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName);
                        File[] files = root.listFiles();
                        fileList.clear();
                        if (files != null) {
                            for (File file : files) {
                                fileList.add(file.getPath().substring(file.getPath().lastIndexOf('/') + 1));
                            }
                            Toast.makeText(DisplayFoldersLayer2.this, fileList.get(0), Toast.LENGTH_SHORT).show();
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


//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener()
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
