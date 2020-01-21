package com.example.user.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 08-Jul-18.
 */

public class FootballAdapter extends ArrayAdapter<Football>{


    public FootballAdapter (Context context, List<Football> footballs)
    {
        super(context, 0, footballs);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.football_listitem, parent, false);
        }

        Football currentFootball = getItem(position);

        //title
        String title = currentFootball.getTitle();

        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(title);

        //sectionName
        String sectionName = currentFootball.getSectionName();
        TextView sectionNameView =  listItemView.findViewById(R.id.sectionName);
        sectionNameView.setText(sectionName);

        //author
        String author = currentFootball.getAuthor();
        TextView authorView =  listItemView.findViewById(R.id.author);
        authorView.setText(author);

        //date

        String dateObject = new String(currentFootball.getTime());

        //date
        TextView dateView =  listItemView.findViewById(R.id.date);
        String formattedDate = null;
        try {
            formattedDate = convertDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", "LLL, dd, yyyy", dateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateView.setText(formattedDate);


        //time
        TextView timeView = listItemView.findViewById(R.id.time);
        String formattedTime = null;
        try {
            formattedTime = convertDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", "h:mm a", dateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        timeView.setText(formattedTime);


        return listItemView;
    }


    public static String convertDateFormat(String oldPattern, String newPattern, String dateString) throws ParseException {
        SimpleDateFormat oldTimeFormat = new SimpleDateFormat(oldPattern);
        Date date = null;
        date = oldTimeFormat.parse(dateString);
        SimpleDateFormat newTimeFormat = new SimpleDateFormat(newPattern);

        String newFormat = newTimeFormat.format(date);
        return newFormat;
    }


}
