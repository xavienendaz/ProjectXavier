package com.example.xavier.projectxavier;

import android.provider.BaseColumns;
import android.widget.ImageView;

/**
 * Created by Xavier on 05.11.2016.
 */

public class DB_Contract {

    public static abstract class User implements BaseColumns
    {
        public static final String TABLE_NAME = "user_info";
        public static final String KEY_ID = "id";
        public static final String USER_NAME = "user_name";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_IMAGE = "image";
    }


    public static abstract class Questions implements BaseColumns
    {
        public static final String TABLE_NAME = "question";
        public static final String KEY_ID = "id";
        public static final String TOPIC = "topic";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String USERNAME = "username";
        public static final String QUESTION_IMAGE = "image";
    }


    public static abstract class Favorite implements BaseColumns
    {
        public static final String TABLE_NAME = "favorite";
        public static final String KEY_ID = "id";
        public static final String USER_NAME = "user_name";
        public static final String KEY_QUESTION_ID = "id_question";
    }


    public static abstract class Comments implements BaseColumns
    {
        public static final String TABLE_NAME = "comments";
        public static final String KEY_ID = "id";
        public static final String CONTENT = "content";
        public static final String DATE = "date";
        public static final String USER_NAME = "user_name";
        public static final String KEY_QUESTION_ID = "id_question";
    }

}
