package com.example.xavier.projectxavier;

import android.provider.BaseColumns;
import android.widget.ImageView;

/**
 * Created by Xavier on 05.11.2016.
 */

public class DB_Contract {

    public static abstract class NewUserInfo implements BaseColumns
    {
        public static final String TABLE_NAME = "user_info";
        public static final String USER_NAME = "user_name";
        public static final String USER_PASSWORD = "user_password";
    }


    public static abstract class NewQuestion implements BaseColumns
    {
        public static final String KEY_ID = "id";
        public static final String TABLE_NAME = "question";
        public static final String TOPIC = "topic";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String USERNAME = "username";
        public static final String KEY_IMAGE = "image";
    }


    public static abstract class NewFavorite implements BaseColumns
    {
        public static final String TABLE_NAME = "favorite";
        public static final String KEY_ID = "id";
        public static final String USER_NAME = "user_name";
        public static final String KEY_QUESTION_ID = "id_question";
    }


}
