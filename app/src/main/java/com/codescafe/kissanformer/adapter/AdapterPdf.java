package com.codescafe.kissanformer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codescafe.kissanformer.Activities.PdfViewer;
import com.codescafe.kissanformer.R;
import com.codescafe.kissanformer.model.AllMediaModel;

import java.util.ArrayList;

public class AdapterPdf extends RecyclerView.Adapter<AdapterPdf.MyHolder> {

    final Context context;
    final ArrayList<AllMediaModel> userList;

    public AdapterPdf(Context context, ArrayList<AllMediaModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_pdf, parent, false);
        return new MyHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if (userList.size() > 0) {
            //Toast.makeText(context, "smhj,dkj", Toast.LENGTH_SHORT).show();
            AllMediaModel postmodel = userList.get(position);
           /* if (position % 5 == 0) {
                AdLoader.Builder builder = new AdLoader.Builder(
                        context, "" + context.getResources().getString(R.string.admob_native));

                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(@NonNull UnifiedNativeAd unifiedNativeAd) {
                        holder.templateView.setNativeAd(unifiedNativeAd);
                    }

                }).forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        holder.templateView.setVisibility(View.VISIBLE);
                    }
                });
                final AdLoader adLoader = builder.build();
                adLoader.loadAd(new AdRequest.Builder().build());



            }*/

            holder.user_name.setText(postmodel.getFar_description());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PdfViewer.class);
                intent.putExtra("url", postmodel);
                context.startActivity(intent);
            });
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
