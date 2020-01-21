package com.example.user.newsfeedapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;

/**
 * Created by User on 08-Jul-18.
 */

public class FootballLoader extends AsyncTaskLoader<List<Football>> {

    private static final String LOG_TAG = FootballLoader.class.getName();
    private String mUrl;


    public FootballLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //background thread
    @Override
    public List<Football> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        //network request and list
        List<Football> footballs = Utils.fetchFootballData(mUrl);
        return footballs;
    }
}
