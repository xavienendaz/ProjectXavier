package com.example.xavier.projectxavier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Xavier on 05.11.2016.
 */

public class ListDataAdapterQuestion extends ArrayAdapter {

    List list = new ArrayList();
    LayoutHandler layoutHandler;
    ByteArrayInputStream imageStream;
    public ListDataAdapterQuestion(Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler
    {

        ImageView  QUESTION_IMAGE;
        TextView QUESTION_TITLE;
        TextView QUESTION_DATE;
        TextView QUESTION_LIKE;
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
    public void clear() {
        list.clear();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;


        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.question_list_layout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.QUESTION_IMAGE = (ImageView) row.findViewById(R.id.imvQuestionList );
            layoutHandler.QUESTION_TITLE = (TextView)row.findViewById(R.id.question_title );
            layoutHandler.QUESTION_LIKE = (TextView)row.findViewById(R.id.nbLike );
            layoutHandler.QUESTION_DATE = (TextView)row.findViewById(R.id.quesionDate );
            row.setTag(layoutHandler);
        }
        else{

            layoutHandler = (ListDataAdapterQuestion.LayoutHandler)row.getTag();

        }



        Question question = (Question)this.getItem(position);


        /* set image in list */

        byte[] img = question.getImage();
        ImageView imageView = (ImageView) row.findViewById(R.id.imvQuestionList );
        imageStream = new ByteArrayInputStream(img);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(theImage);


        layoutHandler.QUESTION_IMAGE.setImageBitmap(theImage);
        layoutHandler.QUESTION_TITLE.setText(question.getTitle());
        layoutHandler.QUESTION_LIKE.setText(question.getNkLike());
        layoutHandler.QUESTION_DATE.setText(question.getDate());


        return row;
    }

}
