package com.example.android.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Spalding on 4/17/2018.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private String LOG_TAG = ArticleLoader.class.getName();
    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        Log.i(LOG_TAG,"TEST: ArticleLoader constructor called.");
        mUrl = url;
    }

    @Override
    public void onStartLoading() {
        Log.i(LOG_TAG,"TEST: onStartLoading method called.");
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        Log.i(LOG_TAG,"TEST: loadInBackground method called.");
        if (mUrl == null) {
            return null;
        }
        return Utilities.getArticleData(mUrl);
    }
}
