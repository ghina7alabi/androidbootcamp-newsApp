package com.example.user.newsfeedapp;

import android.text.TextUtils;
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
import java.util.List;

/**
 * Created by User on 08-Jul-18.
 */

public final class Utils {

    private static String LOG_TAG = Utils.class.getName();


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
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

            //success or not
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the football JSON results.", e);
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



    private Utils() {


    }
    private static List<Football> extractFeatureFromJson(String footballJSON) {
        //return early
        if (TextUtils.isEmpty(footballJSON)) {
            return null;
        }

        //empty arraylist for news
        List<Football> footballs = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(footballJSON);

            JSONObject footballObject = baseJsonResponse.getJSONObject("response");
            JSONArray footballArray = footballObject.getJSONArray("results");


            for (int i = 0; i < footballArray.length(); i++) {

                JSONObject currentFootball = footballArray.getJSONObject(i);

                String title = currentFootball.getString("webTitle");
                String sectionName = currentFootball.getString("sectionName");
                String time = currentFootball.getString("webPublicationDate");
                String url = currentFootball.getString("webUrl");
                String author = null;

                JSONArray tags = currentFootball.getJSONArray("tags");

                for (int j = 0; j < tags.length(); j++) {
                    JSONObject currentTag = tags.getJSONObject(j);
                    author = currentTag.getString("webTitle");

                }

                Football football = new Football(title, sectionName, author, time, url);

                footballs.add(football);

            }

        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the football JSON results", e);
        }

        //return list
        return footballs;
    }

    public static List<Football> fetchFootballData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Football> footballs = extractFeatureFromJson(jsonResponse);

        return footballs;
    }
}
