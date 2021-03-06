package com.example.myapplication.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViews.UI.DisplayFoldersLayer2;

import java.io.File;
import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

public class RecyclerViewAdapterLevel2 extends RecyclerView.Adapter<RecyclerViewAdapterLevel2.ViewHolder>{
    ArrayList<String> fileList=new ArrayList<>();
    LinearLayout functions;
    ImageButton delete,rename;



//Set animation to elements

    ArrayList<String> foldernames=new ArrayList<>();
    ArrayList<String> selectedPositions=new ArrayList<>();

    Context context;
    Boolean atleastOneIsSelected;
    public RecyclerViewAdapterLevel2(Context context, ArrayList<String> foldernames, LinearLayout functionDrawer, ArrayList<String> selectedPositions, ImageButton rename) {
        this.foldernames = foldernames;
        this.context=context;
        this.functions=functionDrawer;
        this.selectedPositions=selectedPositions;
        this.rename=rename;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.level2recycler,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        if(selectedPositions.contains(foldernames.get(position))){
            Toast.makeText(context, "This is there!", Toast.LENGTH_SHORT).show();
        }
        holder.title.setText(foldernames.get(position));
        if (foldernames.get(position).contains(".mp4") || foldernames.get(position).contains(".amr") || foldernames.get(position).contains(".mp3")){
            Glide.with(context)
                    .load(R.drawable.music)
                    .apply(new RequestOptions().override(300,300))
                    .apply(new RequestOptions().circleCrop())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.imageView);
        }
        else if (foldernames.get(position).contains(".pdf") ){
            Glide.with(context)
                    .load(R.drawable.pdf)
                    .apply(new RequestOptions().override(300,300))
                    .apply(new RequestOptions().circleCrop())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.imageView);
        }
        else if (foldernames.get(position).charAt(0)=='.'){
            Glide.with(context)
                    .load(R.drawable.file_icon_1320191242906531645)
                    .apply(new RequestOptions().override(300,300))
                    .apply(new RequestOptions().circleCrop())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.imageView);
        }
        else if (foldernames.get(position).contains(".jpg") || foldernames.get(position).contains(".jpeg") || foldernames.get(position).contains(".png")){
            Glide.with(context)
                    .load(R.drawable.image)
                    .apply(new RequestOptions().override(300,300))
                    .apply(new RequestOptions().circleCrop())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.imageView);
        }
        else{
            Glide.with(context)
                    .load(R.drawable.folder)
                    .apply(new RequestOptions().override(300,300))
                    .apply(new RequestOptions().circleCrop())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.imageView);
        }

        final Animation topAnim = AnimationUtils.loadAnimation(context, R.anim.top_animation);
        final Animation bottomAnim = AnimationUtils.loadAnimation(context, R.anim.bottom_animation);
        holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File root=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+foldernames.get(position));
                File[] files = root.listFiles();
                fileList.clear();
                Toast.makeText(context, foldernames.get(position)+" Clicked", Toast.LENGTH_SHORT).show();
                if(selectedPositions.contains(foldernames.get(position))){
                    holder.parentlayout.setBackgroundResource(R.drawable.recycle_curved);
                    selectedPositions.remove(foldernames.get(position));
                    if(selectedPositions.size()==1){
                        rename.setVisibility(View.VISIBLE);
                    }
                    else{
                        rename.setVisibility(View.GONE);
                    }
                }
                else{
                    if(files==null||files.length==0){
                        Toast.makeText(context, "Item clicked is not a folder/empty", Toast.LENGTH_SHORT).show();

                    }else {
                        Intent intent = new Intent(context, DisplayFoldersLayer2.class);
                        intent.putExtra("Name of folder", foldernames.get(position));
                        functions.setAnimation(topAnim);
                        functions.setVisibility(View.GONE);
                        context.startActivity(intent);
                        CustomIntent.customType(context, "fadein-to-fadeout");
                    }
                }
//                if(holder.checkBox.isChecked()){
//                    holder.checkBox.setChecked(false);
//                    holder.parentlayout.setBackgroundResource(R.drawable.recycle_curved);
//
//                }
//                else if(!holder.checkBox.isChecked()&&files!=null&&files.length!=0){
//
//
//                }
//                else{
//                    Toast.makeText(context, "Item clicked is not a folder/empty", Toast.LENGTH_SHORT).show();
//
//                }
//
            }
        });
        holder.parentlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!selectedPositions.contains(foldernames.get(position))) {
                    Toast.makeText(context, foldernames.get(position) + " Long clicked", Toast.LENGTH_SHORT).show();
                    selectedPositions.add(foldernames.get(position));
                    holder.parentlayout.setBackgroundResource(R.drawable.curved_layout);
                    if(selectedPositions.size()==1){
                        rename.setVisibility(View.VISIBLE);
                    }
                    else{
                        rename.setVisibility(View.GONE);
                    }
                }
//                holder.checkBox.setVisibility(View.VISIBLE);
//                holder.checkBox.setChecked(true);
//                holder.parentlayout.setBackgroundResource(R.drawable.curved_layout);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return foldernames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        LinearLayout parentlayout;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.folderName);
            imageView=itemView.findViewById(R.id.folderImage);
            parentlayout=itemView.findViewById(R.id.folder_item);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}
