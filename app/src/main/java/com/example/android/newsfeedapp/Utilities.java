package com.example.android.newsfeedapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Spalding on 4/12/2018.
 */

public final class Utilities {
    private static final String LOG_TAG = Utilities.class.getName();

    private Utilities() {
    }


    public static ArrayList<Article> getArticleData(String... urls) {
        URL url = Utilities.createUrl(urls[0]);
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making a connection");
        }
        ArrayList<Article> articleFeed = Utilities.parseResponse(jsonResponse);
        return articleFeed;
    }

    private static URL createUrl(String urls) {
        URL url = null;
        try {
            url = new URL(urls);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with url");
        }
        return url;
    }

    private static ArrayList<Article> parseResponse(String jsonResponse) {
        ArrayList<Article> articles = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject jsonRootObject = root.getJSONObject("response");
            JSONArray resultsArray = jsonRootObject.getJSONArray("results");
            Log.i(LOG_TAG, "Contents of resultsArray = " + resultsArray);
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);

                if (result.has("fields")) {

                    String title = result.getString("webTitle");
                    String webURL = result.getString("webUrl");
                    String section = result.getString("sectionName");
                    String date = result.getString("webPublicationDate");
                    String author = null;
                    JSONObject fields;
                    String thumbnailURL = null;
                    Bitmap thumbnailBitmap = null;
                    if (result.has("tags")) {
                        JSONArray tags = result.getJSONArray("tags");
                        if (tags.length() > 0) {

                            JSONObject webTitle = tags.getJSONObject(0);
                            author = webTitle.getString("webTitle");
                        }
                    }

                    fields = result.getJSONObject("fields");
                    thumbnailURL = fields.optString("thumbnail");

                    try {
                        URL url = new URL(thumbnailURL);
                        try {
                            thumbnailBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "problem loading Bitmap ");
                        }
                    } catch (MalformedURLException e) {
                        Log.e(LOG_TAG, "Problem with the URL");
                    } finally {
                        articles.add(new Article(title, section, date, author, webURL, thumbnailBitmap));
                    }
                }

            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of articles
        return articles;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
