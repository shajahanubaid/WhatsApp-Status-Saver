package com.nimbuzzmasters.statussaver.Adapters;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.NativeExpressAdView;
import com.nimbuzzmasters.statussaver.Activities.MainActivity;
import com.nimbuzzmasters.statussaver.Model.StoryModel;
import com.nimbuzzmasters.statussaver.R;
import com.nimbuzzmasters.statussaver.Utils.Constants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shajahan on 9/1/2017.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;
    private Context context;
    private ArrayList<Object> filesList;

    public StoryAdapter(Context context, ArrayList<Object> filesList) {
        this.context = context;
        this.filesList = filesList;
    }

    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case MENU_ITEM_VIEW_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row,null,false);
                return new ViewHolder(view);
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
            default:
                View nativeExpressLayoutView = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.native_express_ad_container,
                        parent, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);
        }
    }

    @Override
    public void onBindViewHolder(StoryAdapter.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case MENU_ITEM_VIEW_TYPE:
                final StoryModel files = (StoryModel) filesList.get(position);
                final Uri uri = Uri.parse(files.getUri().toString());
                holder.userName.setText(files.getName());
                if(files.getUri().toString().endsWith(".mp4"))
                {
                    holder.playIcon.setVisibility(View.VISIBLE);
                }else{
                    holder.playIcon.setVisibility(View.INVISIBLE);
                }
                Glide.with(context)
                        .load(files.getUri())
                        .into(holder.savedImage);
                holder.downloadID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkFolder();
                        final String path = ((StoryModel) filesList.get(position)).getPath();
                        final File file = new File(path);
                        String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;
                        File destFile = new File(destPath);
                        try {
                            FileUtils.copyFileToDirectory(file,destFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        MediaScannerConnection.scanFile(
                                context,
                                new String[]{ destPath + files.getFilename()},
                                new String[]{ "*/*"},
                                new MediaScannerConnection.MediaScannerConnectionClient()
                                {
                                    public void onMediaScannerConnected()
                                    {
                                    }
                                    public void onScanCompleted(String path, Uri uri)
                                    {
                                        Log.d("path: ",path);
                                    }
                                });
                        Toast.makeText(context, "Saved to: "+ destPath + files.getFilename(), Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case NATIVE_EXPRESS_AD_VIEW_TYPE:
                // fall through
            default:
                try{
                    NativeExpressAdViewHolder nativeExpressHolder =
                            (NativeExpressAdViewHolder) holder;
                    NativeExpressAdView adView =
                            (NativeExpressAdView) filesList.get(position);
                    ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                    if (adCardView.getChildCount() > 0) {
                        adCardView.removeAllViews();
                    }
                    if (adView.getParent() != null) {
                        ((ViewGroup) adView.getParent()).removeView(adView);
                    }
                    // Add the Native Express ad to the native express ad view.
                    adCardView.addView(adView);
                }catch (Exception e){
                    e.printStackTrace();
                }
        }

    }

    public void checkFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME ;
        File dir = new File(path);
        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();
        }
        if (isDirectoryCreated) {
            Log.d("Folder", "Already Created");
        }
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class NativeExpressAdViewHolder extends StoryAdapter.ViewHolder {
        NativeExpressAdViewHolder(View view) {
            super(view);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return (position % MainActivity.ITEMS_PER_AD == 0) ? NATIVE_EXPRESS_AD_VIEW_TYPE
                : MENU_ITEM_VIEW_TYPE;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView savedImage;
        ImageView playIcon;
        ImageView downloadID;
        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.profileUserName);
            savedImage = (ImageView) itemView.findViewById(R.id.mainImageView);
            playIcon = (ImageView) itemView.findViewById(R.id.playButtonImage);
            downloadID = (ImageView) itemView.findViewById(R.id.downloadID);
        }
    }
}
