package com.codescafe.kissanformer.supportteam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codescafe.kissanformer.Activities.SupportChat;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.Utils;
import com.codescafe.kissanformer.adapter.AdapterTeamChat;
import com.codescafe.kissanformer.auth.FarmerManager;
import com.codescafe.kissanformer.model.FarmerUserModel;
import com.codescafe.kissanformer.model.ModelTeamChat;
import com.codescafe.kissanformer.model.SupportTeamModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FarmerPersonalChat extends AppCompatActivity {

    //Permission
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int VIDEO_PICK_CODE = 1002;
    private static final int AUDIO_PICK_CODE = 1003;
    private static final int DOC_PICK_CODE = 1004;
    private static final int PERMISSION_CODE = 1001;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PERMISSION_REQ_CODE = 1 << 3;
    private final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    LinearLayout main;
    RecyclerView chatList;
    ArrayList<ModelTeamChat> nChat = new ArrayList<>();
    ImageView add;
    AdapterTeamChat adapterChat;
    private boolean notify = false;
    DatabaseReference databaseReference;
    String userId;
    Activity activity;
    String key;
    TextView name;
    ImageView back;
    EditText textBox;
    String hisId;
    ModelTeamChat modelTeamChat;
    FarmerManager farmerManager;
    FarmerUserModel farmerUserModel;
    String Usertype, adminId;
    SupportTeamModel supportTeamModel;
    TextView id;

    //
    ImageView voice_cancel, attachFiles, voice_img, message_send;
    CardView voice_send, voice;
    Chronometer timer;
    File Voice_file;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    ArrayList<String> infoList = new ArrayList<>();
    MediaRecorder mediaRecorder = null;
    RelativeLayout voice_layout, messgeLayout;
    ProgressDialog pd;
    Boolean isRecording = false;

    LinearLayoutManager layoutManager;
    private int firstVisible = -1;
    int ZERO = 0; // Don't change
    int ONE = 1; // Don't change
    int TWO = 2; //Don't edit this
    int THREE = 3; //Minimum groups member
    LinearLayout btnBottom;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_personal_chat);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        activity = this;
        farmerManager = new FarmerManager(activity);
        farmerUserModel = farmerManager.getUserDetails("userDetail");
        databaseReference = FirebaseDatabase.getInstance().getReference("Personal Chat");
        modelTeamChat = new ModelTeamChat("Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty", "Empty");


        // Usertype = getIntent().getStringExtra("type");
        hisId = getIntent().getStringExtra("model");
        // userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        userId = "103";


        back = findViewById(R.id.back);
        voice_img = findViewById(R.id.voice_img);
        message_send = findViewById(R.id.imageView10);
        textBox = findViewById(R.id.textBox);
        chatList = findViewById(R.id.chatList);
        voice_cancel = findViewById(R.id.voice_cancel);
        voice_img = findViewById(R.id.voice_img);
        voice_send = findViewById(R.id.voice_send);
        voice_layout = findViewById(R.id.voice_layout);
        messgeLayout = findViewById(R.id.messgeLayout);
        progressBar = findViewById(R.id.progressBar);
        voice = findViewById(R.id.voice);
        add = findViewById(R.id.add);
        btnBottom = findViewById(R.id.btnBottom);
        timer = findViewById(R.id.timer);

        chatList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity);
        layoutManager.setStackFromEnd(true);
        chatList.setLayoutManager(layoutManager);
        btnBottom.setVisibility(View.GONE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        message_send.setOnClickListener(v -> {
            key = databaseReference.push().getKey();
            if (textBox.getText().toString().isEmpty()) {
                Snackbar.make(v, "Type a message", Snackbar.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                modelTeamChat.setIsSeen("false");
                modelTeamChat.setTimestamp("" + System.currentTimeMillis());
                modelTeamChat.setSender(userId);
                modelTeamChat.setReceiver(hisId);
                modelTeamChat.setMsg(textBox.getText().toString());
                modelTeamChat.setKey(key);
                modelTeamChat.setType("text");
                assert key != null;
                databaseReference.child(userId).child(hisId).child(key).setValue(modelTeamChat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                databaseReference.child(hisId).child(userId).child(key).setValue(modelTeamChat);
                chatList();
                textBox.setText("");
            }
        });
        textBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    voice_img.setVisibility(View.VISIBLE);
                    message_send.setVisibility(View.GONE);
                } else {
                    voice_img.setVisibility(View.GONE);
                    message_send.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    if (firstVisible == -1)
                        firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
                    else
                        firstVisible = adapterChat.getItemCount() >= TWO ? adapterChat.getItemCount() - TWO : ZERO;
                } catch (Exception e) {
                    firstVisible = ZERO;
                }

                if (layoutManager.findLastVisibleItemPosition() < firstVisible) {
                    btnBottom.setVisibility(View.VISIBLE);
                } else {
                    btnBottom.setVisibility(View.GONE);
                }
            }
        });

        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (firstVisible != -1) {
                        chatList.smoothScrollToPosition(adapterChat.getItemCount() - ONE);
                    }
                    btnBottom.setVisibility(View.GONE);
                } catch (Exception ignored) {

                }
            }
        });

        voice_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.RECORD_AUDIO};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        if (validateMicAvailability()) {
                            record_voice();
                        } else {
                            Toast.makeText(activity, "Mic Already using", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (validateMicAvailability()) {
                        record_voice();
                    } else {
                        Toast.makeText(activity, "Mic Already using", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        voice_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder != null) {
                    try {
                        stopRecording();
                        Voice_file.delete();
                        voice_send.setVisibility(View.GONE);
                        voice.setVisibility(View.VISIBLE);
                        //voice_btm.setImageResource(R.drawable.ic_voice1);
                        voice_layout.setVisibility(View.GONE);
                        messgeLayout.setVisibility(View.VISIBLE);
                    } catch (Exception e) {

                    }

                }


            }
        });
        voice_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder != null) {
                    try {
                        stopRecording();
                        // voice_btm.setImageResource(R.drawable.ic_voice1);
                        voice_layout.setVisibility(View.GONE);
                        messgeLayout.setVisibility(View.VISIBLE);
                        voice_send.setVisibility(View.GONE);
                        voice.setVisibility(View.VISIBLE);
                        infoList = new ArrayList<>();
                        infoList.add("audio/mp3");
                        infoList.add(Voice_file.getName());
                        Uri data = Uri.fromFile(Voice_file);
                        sendFile1(data, infoList);
                    } catch (Exception e) {

                    }

                    //  Toast.makeText(getApplicationContext(),data.toString(),Toast.LENGTH_LONG).show();
                }

            }
        });
        //functions
        readMessage();
    }
    private void record_voice() {
        mediaRecorder = null;
        //Toast.makeText(activity, "before", Toast.LENGTH_SHORT).show();
        // Toast.makeText(activity, "before / after", Toast.LENGTH_SHORT).show();
        voice_layout.setVisibility(View.VISIBLE);
        messgeLayout.setVisibility(View.GONE);
        voice_send.setVisibility(View.VISIBLE);
        voice.setVisibility(View.GONE);
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/UniqueChatApp/Personal");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(dir);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Voice_file = new File(dir.toString(), "" + System.currentTimeMillis() + ".mp3");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecorder.setOutputFile(Voice_file);
        }
        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                // Toast.makeText(getApplicationContext(),""+what+""+extra,Toast.LENGTH_SHORT).show();
            }
        });
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                //Toast.makeText(getApplicationContext(),""+what+""+extra,Toast.LENGTH_SHORT).show();
            }
        });
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendFile1(final Uri chooseFileUri, final List<String> info) {

        progressBar.setVisibility(View.VISIBLE);
        StorageReference sref = FirebaseStorage.getInstance().
                getReference("Files").child("Voice").child(userId)
                .child(info.get(0).substring(0, info.get(0).indexOf("/") - 1));

        sref.putFile(chooseFileUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String downloadUri = Objects.requireNonNull(uriTask.getResult()).toString();
            if (uriTask.isSuccessful()) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
                String current_date = simpleDateFormat.format(calendar.getTime());
                simpleDateFormat = new SimpleDateFormat("hh:mm a");
                String current_time = simpleDateFormat.format(calendar.getTime());
                String key = ref.push().getKey();
                HashMap<String,Object> msgBody = new HashMap();
                msgBody.put("sender", userId);
                msgBody.put("date", current_date);
                msgBody.put("time", current_time);
                msgBody.put("key", key);
                msgBody.put("voice",timer.getText().toString().trim());
                msgBody.put("receiver", hisId);
                msgBody.put("msg", downloadUri.toString());
                msgBody.put("isSeen", "false");
                msgBody.put("type", "audio");

                databaseReference.child(userId).child(hisId).child(key).setValue(msgBody).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                databaseReference.child(hisId).child(userId).child(key).setValue(msgBody);

            }
        }).addOnFailureListener(e -> {

        });

    }

    private void chatList() {
        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("PersonalChatList")
                .child(userId)
                .child(hisId);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef1.child("key").setValue(hisId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("PersonalChatList")
                .child(hisId)
                .child(userId);

        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    chatRef2.child("key").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessage() {
        //Get
        FirebaseDatabase.getInstance().getReference("Personal Chat").child(userId).child(hisId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    nChat.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ModelTeamChat chat = ds.getValue(ModelTeamChat.class);
                        if (Objects.requireNonNull(chat).getReceiver().equals(userId) && chat.getSender().equals(hisId) ||
                                chat.getReceiver().equals(hisId) && chat.getSender().equals(userId)) {
                            nChat.add(chat);
                        }
                        adapterChat = new AdapterTeamChat(activity, nChat);
                        chatList.setAdapter(adapterChat);

//                        adapterChat.notifyItemInserted(nChat.size() - 1);
//                        chatList.scrollToPosition(nChat.size() - 1);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void stopRecording() {

        if (isRecording) {
            mediaRecorder.stop();
        }
        mediaRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
        mediaRecorder.release();// Now the object cannot be reused
        mediaRecorder = null;
        isRecording = false;

    }
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            Uri dp_uri = Objects.requireNonNull(data).getData();
            sendImage(dp_uri);
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            //Snackbar.make(main, "Please wait, Sending...", Snackbar.LENGTH_LONG).show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendImage(Uri dp_uri) {
        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("chat_photo/" + "" + System.currentTimeMillis());
        storageReference.putFile(dp_uri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            Uri downloadUri = uriTask.getResult();
            if (uriTask.isSuccessful()) {

                key = databaseReference.push().getKey();
                modelTeamChat.setIsSeen("false");
                modelTeamChat.setTimestamp("" + System.currentTimeMillis());
                modelTeamChat.setSender(userId);
                modelTeamChat.setReceiver(hisId);
                modelTeamChat.setMsg(downloadUri.toString());
                modelTeamChat.setKey(key);
                modelTeamChat.setType("image");
                assert key != null;
                //Toast.makeText(activity, "" + userId + "/" + hisId, Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Personal Chat").child(userId).child(hisId).child(key).setValue(modelTeamChat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            readMessage();
                        }
                    }
                });
                FirebaseDatabase.getInstance().getReference("Personal Chat").child(hisId).child(userId).child(key).setValue(modelTeamChat);
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                //Snackbar.make(main, "Sent", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean validateMicAvailability() {
        Boolean available = true;
        @SuppressLint("MissingPermission") AudioRecord recorder =
                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_DEFAULT, 44100);
        try {
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED) {
                available = false;

            }

            recorder.startRecording();
            if (recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
                recorder.stop();
                available = false;

            }
            recorder.stop();
        } finally {
            recorder.release();
            recorder = null;
        }

        return available;
    }

    private void CreateFeedbackNotification(String title, String count) {
        JSONObject data = new JSONObject();
        Gson gson = new Gson();
        try {
            data.put("NotificationType", "AdminMessageNotification");
            data.put("title", title);
            data.put("msg", "" + count);
            data.put("to", hisId);
            data.put("self", userId);
            notifiy(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notifiy(JSONObject data) {

        try {
            RequestQueue queue = Volley.newRequestQueue(activity);
            String url = "https://fcm.googleapis.com/fcm/send";
            JSONObject notification_data = new JSONObject();
            notification_data.put("data", data);
            notification_data.put("content_available", true);
            notification_data.put("to", "/topics/Message");
            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, response -> {

            }, error -> {

            }) {
                @Override
                public java.util.Map<String, String> getHeaders() {
                    String api_key_header_value = Utils.server_key;
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    Log.e("mytag", "header");
                    return headers;
                }
            };

            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("mytag", "notify  Exception " + e.toString());
        }
    }
}