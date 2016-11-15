package com.example.xavier.projectxavier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 15.11.2016.
 */

public class ListDataAdapterProfile extends ArrayAdapter {

    List list = new ArrayList();
    ListDataAdapterQuestion.LayoutHandler layoutHandler;

    public ListDataAdapterProfile(Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler
    {
        TextView QUESTION_TITLE;
    }


    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.profile_list_layout, parent, false);
            layoutHandler = new ListDataAdapterQuestion.LayoutHandler();
            layoutHandler.QUESTION_TITLE = (TextView)row.findViewById(R.id.profile_question_title );
            row.setTag(layoutHandler);
        }
        else{
            //if the row is already existing
            layoutHandler = (ListDataAdapterQuestion.LayoutHandler)row.getTag();

        }

        Question question = (Question)this.getItem(position);


        layoutHandler.QUESTION_TITLE.setText(question.getTitle());;


        return row;
    }

}
