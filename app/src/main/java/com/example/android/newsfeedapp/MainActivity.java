package com.example.android.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String REQUEST_URL = "https://content.guardianapis.com/search?q=science&api-key=0c592fdb-980d-4b7f-b14f-30bf11abdbdd&show-fields=thumbnail&show-tags=contributor&page-size=20";
    private static final int NEWS_LOADER_ID = 1;
    private static final String LOG_TAG = MainActivity.class.getName();
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyStateView;
    private ArticleAdapter mAdapter;
    private Loader<List<Article>> mLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progress_bar);
        mEmptyStateView = findViewById(R.id.empty_state_text);
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        mLoader = getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
        Log.i(LOG_TAG,"TEST: .initLoader method called");
        ListView listView = (ListView) findViewById(R.id.list);
        final Context context = this;

        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        final boolean mIsConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (mIsConnected) {
            mLoader.forceLoad();
            Log.i(LOG_TAG,"TEST: forceLoad() on loader object called");
            mEmptyStateView.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }

        mEmptyStateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                final boolean mIsConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
                if (mIsConnected) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mLoader.forceLoad();
                    Log.i(LOG_TAG,"TEST: forceLoad() on loader object called");
                    mEmptyStateView.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article article = mAdapter.getItem(i);
                Uri url = Uri.parse(article.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Log.i(LOG_TAG, "TEST url opened: " + url);
                intent.setData(url);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG,"TEST: onCreateLoader method called.");
        return new ArticleLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        Log.i(LOG_TAG,"TEST: onLoadFinished method called.");
        mProgressBar.setVisibility(View.GONE);
        mAdapter.clear();
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.i(LOG_TAG,"TEST: onLoaderReset() method called");
        mAdapter.clear();
    }
}

