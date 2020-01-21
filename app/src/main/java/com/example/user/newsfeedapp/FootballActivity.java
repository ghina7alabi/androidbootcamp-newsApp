package com.example.user.newsfeedapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FootballActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Football>> {

    private static final String LOG_TAG = FootballActivity.class.getName();

    private FootballAdapter mAdapter;

    private static final String USGS_REQUEST_URL = "https://content.guardianapis.com/search";

    private static final int FOOTBALL_LOADER_ID = 1;

    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football);

        ListView footballListview = findViewById(R.id.list);

        //input
        mAdapter = new FootballAdapter(this, new ArrayList<Football>());

        footballListview.setAdapter(mAdapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        footballListview.setEmptyView(mEmptyStateTextView);


        footballListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Football currentFootball = mAdapter.getItem(position);
                Uri footballUri = Uri.parse(currentFootball.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, footballUri);
                startActivity(websiteIntent);

            }
        });

        //network state
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //fetch data if possible
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(FOOTBALL_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<Football>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //string value from preferences and default
        String minResults = sharedPrefs.getString(getString(R.string.settings_min_results_key), getString(R.string.settings_min_result_default));
        String orderBy  = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        String topic  = sharedPrefs.getString(getString(R.string.settings_topic_key), getString(R.string.settings_topic_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        //parameter and value
        uriBuilder.appendQueryParameter("q", topic);
        uriBuilder.appendQueryParameter("api-key", "5f31e420-436c-49ee-b966-125aea32e1ad");
        uriBuilder.appendQueryParameter("page-size", minResults);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        // Return the completed uri `https://content.guardianapis.com/search?q=world%20cup&tag=football/football&order-by=newest&api-key=5f31e420-436c-49ee-b966-125aea32e1ad&show-tags=contributor
        return new FootballLoader(this, uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<List<Football>> loader, List <Football> footballs){

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.nothing_found);
        mAdapter.clear();

        if (footballs != null && !footballs.isEmpty()) {
            mAdapter.addAll(footballs);
        }
    }

    @Override
    public void onLoaderReset (Loader<List<Football>> loader) {

        mAdapter.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
