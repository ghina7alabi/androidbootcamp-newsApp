package com.example.user.newsfeedapp;

/**
 * Created by User on 08-Jul-18.
 */
public class Football {

    private String mTitle;
    private String mSectionName;
    private String mAuthor;
    private String mTime;
    private String mURL;

    public Football(String title, String sectionName, String author, String time, String url) {
        mTitle = title;
        mSectionName= sectionName;
        mAuthor = author;
        mTime = time;
        mURL = url;
    }

    public String getTitle() {return mTitle;}

    public String getSectionName() {return mSectionName;}

    public String getAuthor() {return mAuthor;}

    public String getTime() {return mTime;}

    public String getUrl() { return mURL; }
}
