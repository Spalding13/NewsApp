package com.example.android.newsfeedapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Spalding on 4/12/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {
    private String LOG_TAG = ArticleAdapter.class.getName();

    public ArticleAdapter(Context context, ArrayList<Article> earthquakes) {
        super(context, 0, earthquakes);
    }

    private int getSectionBackgroundColor(String section) {
        int backgroundColorResId;
        switch (section) {
            case "Science":
                backgroundColorResId = R.color.Science;
                break;
            case "World":
                backgroundColorResId = R.color.World;
                break;
            case "Travel":
                backgroundColorResId = R.color.Travel;
                break;
            case "Environment":
                backgroundColorResId = R.color.Environment;
                break;
            case "Books":
                backgroundColorResId = R.color.Books;
                break;
            case "UK":
                backgroundColorResId = R.color.UK;
                break;
            case "Cities":
                backgroundColorResId = R.color.Cities;
                break;
            default:
                backgroundColorResId = R.color.Misc;
                break;
        }
        return ContextCompat.getColor(getContext(), backgroundColorResId);
    }

    private String formatDate(String dateObject) {
        String parser;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd / HH:mm");
        try {
            parser = dateFormat2.format(dateFormat1.parse(dateObject));
        } catch (ParseException e) {
            parser = "Error fetching date";
            Log.e(LOG_TAG, "Failed forming date");
        }
        return parser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Article currentArticle = getItem(position);

        String section = currentArticle.getSectionName();
        LinearLayout sectionLayout = (LinearLayout) listItemView.findViewById(R.id.sectionlayout);
        sectionLayout.setBackgroundColor(getSectionBackgroundColor(section));
        TextView sectionText = (TextView) listItemView.findViewById(R.id.section);
        sectionText.setText(section);


        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        titleText.setText(currentArticle.getTitleName());

        String dateObject = currentArticle.getDatePublished();
        String dateFormat = formatDate(dateObject);
        TextView dateText = (TextView) listItemView.findViewById(R.id.date);
        dateText.setText(dateFormat);

        String author = currentArticle.getAuthorName();
        TextView contributorText = (TextView) listItemView.findViewById(R.id.contributor);
        if (author != null) {
            contributorText.setText(author);
        } else {
            contributorText.setVisibility(View.GONE);
        }

        Bitmap articleImage = currentArticle.getThumbnail();
        ImageView articlleImageView = (ImageView) listItemView.findViewById(R.id.thumbnail_image_view);
        articlleImageView.setImageBitmap(articleImage);

        return listItemView;
    }
}
