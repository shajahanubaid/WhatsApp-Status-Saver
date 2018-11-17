package com.nimbuzzmasters.statussaver.Model;

import android.net.Uri;

import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by shajahan on 9/1/2017.
 */

public class StoryModel {
    private String name;
    private Uri uri;
    private String path;
    private String filename;
    private NativeExpressAdView adView;

    public StoryModel(String name, Uri uri, String path, String filename, NativeExpressAdView adView) {
        this.name = name;
        this.uri = uri;
        this.path = path;
        this.filename = filename;
        this.adView = adView;
    }

    public StoryModel() {
    }

    public NativeExpressAdView getAdView() {
        return adView;
    }

    public void setAdView(NativeExpressAdView adView) {
        this.adView = adView;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
