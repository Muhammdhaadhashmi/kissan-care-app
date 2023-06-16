package com.codescafe.kissanformer.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codescafe.kissanformer.Activities.VideoView;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.AllMediaModel;

import java.io.IOException;
import java.util.ArrayList;

public class AdapterAudio extends RecyclerView.Adapter<AdapterAudio.MyHolder> {

    final Context context;
    final ArrayList<AllMediaModel> userList;
    private final Handler handler=new Handler();
    Boolean isPlaying=false;
    MediaPlayer mediaPlayer=null;


    public AdapterAudio(Context context, ArrayList<AllMediaModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_audio, parent, false);
        return new MyHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if (userList.size() > 0) {
            //Toast.makeText(context, "smhj,dkj", Toast.LENGTH_SHORT).show();
            AllMediaModel postmodel = userList.get(position);

            holder.user_name.setText(postmodel.getFar_description());

            holder.itemView.setOnClickListener(v -> {

//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.audioplaydialoge);
//               // dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//
//                SeekBar progressShow = dialog.findViewById(R.id.progressShow);
//                TextView timeDisplay = dialog.findViewById(R.id.timeDisplay);
//                ImageView pausePlay = dialog.findViewById(R.id.pausePlay);
//                pausePlay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (mediaPlayer == null) {
//                            try {
//                                mediaPlayer = new MediaPlayer();
//                                mediaPlayer.setDataSource(postmodel.getFar_type());
//                                mediaPlayer.prepare();
//                                duration = mediaPlayer.getDuration();
//                                Toast.makeText(context, "du" + duration, Toast.LENGTH_SHORT).show();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Toast.makeText(context, "" + e.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        if (!mediaPlayer.isPlaying()) {
//                            //Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
//                            mediaPlayer.start();
//                            pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
//                        } else if (mediaPlayer.isPlaying()) {
//                            //Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
//                            mediaPlayer.pause();
//                            pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
//                        }
//                        updateSeekbar(holder, progressShow);
//                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//
//                                pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
//                                progressShow.setProgress(0);
//                            }
//                        });
//                    }
//                });
//                dialog.show();
                Intent intent = new Intent(context, VideoView.class);
                intent.putExtra("url","https://kissancare.reedspak.org/upload/audios/"+postmodel.getFar_src());
                context.startActivity(intent);

            });
        }

    }
            int duration=0;

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
        return userList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        final TextView user_name;
        //TemplateView templateView;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);

        }

    }

}
