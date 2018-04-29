package com.example.android.newsfeedapp;

import android.graphics.Bitmap;

/**
 * Created by Spalding on 4/12/2018.
 */

public class Article {
    private String mTitleName;
    private String mSectionName;
    private String mDatePublished;
    private String mAuthorName;
    private String mWebUrl;
    private Bitmap mThumbnail;

    public Article(String titleName, String sectionName, String datePublished, String authorName, String webURL, Bitmap thumbnail) {
        mTitleName = titleName;
        mSectionName = sectionName;
        mDatePublished = datePublished;
        mAuthorName = authorName;
        mWebUrl = webURL;
        mThumbnail = thumbnail;
    }

    public String getTitleName() {
        return mTitleName;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getDatePublished() {
        return mDatePublished;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

}
