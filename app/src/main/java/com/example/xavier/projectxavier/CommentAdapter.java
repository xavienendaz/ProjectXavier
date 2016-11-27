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
import java.util.List;

/**
 * Created by Xavier on 19.11.2016.
 */

public class CommentAdapter extends ArrayAdapter {

    List list = new ArrayList();
    LayoutHandler layoutHandler;
    ByteArrayInputStream imageStream;

    public CommentAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler
    {
        ImageView COMMENT_AUTHOR_IMAGE;
        TextView COMMENT_AUTHOR;
        TextView COMMENT_CONTENT;
        TextView COMMENT_DATE;
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
            row = layoutInflater.inflate(R.layout.comments_list_layout, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.COMMENT_AUTHOR_IMAGE = (ImageView) row.findViewById(R.id.imvCommentAuthor );
            layoutHandler.COMMENT_AUTHOR = (TextView)row.findViewById(R.id.author);
            layoutHandler.COMMENT_CONTENT = (TextView)row.findViewById(R.id.commentContent);
            layoutHandler.COMMENT_DATE = (TextView)row.findViewById(R.id.date);
            row.setTag(layoutHandler);
        }
        else{
             layoutHandler = (CommentAdapter.LayoutHandler)row.getTag();

        }

        Comment c = (Comment)this.getItem(position);

        // display the image in list

        byte[] img = c.getImage();
        ImageView imageView = (ImageView) row.findViewById(R.id.imvCommentAuthor );
        imageStream = new ByteArrayInputStream(img);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(theImage);


        layoutHandler.COMMENT_AUTHOR_IMAGE.setImageBitmap(theImage);
        layoutHandler.COMMENT_AUTHOR.setText(c.getUsername());
        layoutHandler.COMMENT_CONTENT.setText(c.getContent());
        layoutHandler.COMMENT_DATE.setText(c.getDate());


        return row;
    }
}
