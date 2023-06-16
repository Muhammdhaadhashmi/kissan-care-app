package com.codescafe.kissanformer.adapter;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.auth.FarmerManager;
import com.codescafe.kissanformer.auth.SupportTeamManager;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.model.ModelTeamChat;
import com.codescafe.kissanformer.model.SupportTeamModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


@SuppressWarnings("ALL")
public class AdapterTeamChat extends RecyclerView.Adapter<AdapterTeamChat.MyHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    String userID ;

    FarmerManager farmerManager;
    SupportTeamManager supportTeamManager;
    FarmerUserModel farmerUserModel;
    SupportTeamModel supportTeamModel;

    private final Context context;
    private final ArrayList<ModelTeamChat> modelChats;
    String postType;
    BottomSheetDialog reel_options;
    private final Handler handler=new Handler();
    Boolean isPlaying=false;
    MediaPlayer mediaPlayer=null;
    int duration=0;

    public AdapterTeamChat(Context context, ArrayList<ModelTeamChat> modelChats) {
        this.context = context;
        this.modelChats = modelChats;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left_list, parent, false);

            return new MyHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.chat_right_list, parent, false);
        return new MyHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {


        //Seen

        ModelTeamChat mChat = modelChats.get(position);

        if (modelChats.get(position).getType().equals("text")) {
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(mChat.getMsg());
        }
        if (modelChats.get(position).getType().equals("audio")) {
          //  holder.media.setVisibility(View.VISIBLE);
            holder.media_layout.setVisibility(View.VISIBLE);
            holder.timeDisplay.setText(""+modelChats.get(position).getVoice());
           // Picasso.get().load(modelChats.get(position).getMsg()).into(holder.media);
        }
        if (modelChats.get(position).getType().equals("image")) {
            holder.media.setVisibility(View.VISIBLE);
            holder.mediaLayout.setVisibility(View.VISIBLE);
            Picasso.get().load(modelChats.get(position).getMsg()).into(holder.media);
        }

        //holder.timeDisplay.setText(mChat.getMsg());
        holder.pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer==null){
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(mChat.getMsg());
                        mediaPlayer.prepare();
                        duration = mediaPlayer.getDuration();
                       // Toast.makeText(context, "du"+duration, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                if (!mediaPlayer.isPlaying()){
                    //Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    holder.pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                }
                else if (mediaPlayer.isPlaying()){
                    //Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                    holder.pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                }
                updateSeekbar(holder,holder.progressShow);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        holder.pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                        holder.progressShow.setProgress(0);
                    }
                });
            }
        });

        //("dd/MM/yy - h:mm a").
//        @SuppressLint("SimpleDateFormat") String value = new java.text.SimpleDateFormat("dd-h:mm a").
//                format(new java.util.Date(Long.parseLong(modelChats.get(position).getTimestamp()) * 1000));
//        holder.time.setText(value);
        //More
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage((CharSequence) "Are You Sure You Want To Delete");
//                builder.setPositiveButton(Html.fromHtml("<font color='#45B39C'>Delete</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                builder.setNegativeButton(Html.fromHtml("<font color='#45B39C'>Cancel</font>"), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                //builder.show();
//                return false;
//            }
//        });

    }

    private void updateSeekbar(MyHolder holder, SeekBar seekbar) {

        if (mediaPlayer!=null){
            if (mediaPlayer.isPlaying()){
                seekbar.setProgress((int) (((float)mediaPlayer.getCurrentPosition()/duration)*100));
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        updateSeekbar(holder,seekbar);
                    }
                };
                handler.postDelayed(runnable,500);
            }
        }
    }

    @Override
    public int getItemCount() {
        return modelChats.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        final TextView text,timeDisplay;
        RelativeLayout media_layout;
        CardView mediaLayout;
        TextView time;
        ImageView media,pausePlay;
        SeekBar progressShow;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            time = itemView.findViewById(R.id.time);
            media_layout = itemView.findViewById(R.id.relativeLayout10);
            pausePlay=itemView.findViewById(R.id.pausePlay);
            progressShow=itemView.findViewById(R.id.progressShow);
            timeDisplay=itemView.findViewById(R.id.timeDisplay);
            mediaLayout=itemView.findViewById(R.id.mediaLayout);
            media = itemView.findViewById(R.id.media);

        }

    }

    @Override
    public int getItemViewType(int position) {
        farmerManager = new FarmerManager(context);
        farmerUserModel = farmerManager.getUserDetails("userDetail");
        supportTeamManager = new SupportTeamManager(context);
        supportTeamModel = supportTeamManager.getUserDetails("userDetail");
        if (farmerManager.getBooleanData("farmer")){
            userID = farmerUserModel.getId();
        }else if (supportTeamManager.getBooleanData("support")){
            userID = supportTeamModel.getAdmin_id();
        }
        if (modelChats.get(position).getSender().equals(userID)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    private void downloadDoc(Context context, String directoryDownloads, String url, String extension) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri1 = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri1);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, directoryDownloads, extension);
        Objects.requireNonNull(downloadManager).enqueue(request);
    }

}

