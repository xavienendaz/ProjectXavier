package com.example.xavier.projectxavier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Xavier on 05.11.2016.
 */

public class DbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "PROJECT.DB";
    private static final int DATABASE_VERSION = 6;

    private static final String CREATE_QUERY_TBL_USER = "CREATE TABLE "
            + DB_Contract.User.TABLE_NAME+"("
                    + DB_Contract.Favorite.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DB_Contract.User.USER_NAME+" TEXT,"
                    + DB_Contract.User.USER_PASSWORD+" TEXT,"
                    + DB_Contract.User.USER_IMAGE + " BLOB" + ")";

    private static final String CREATE_QUERY_TBL_QUESTIONS = "CREATE TABLE "
            + DB_Contract.Questions.TABLE_NAME + "("
            + DB_Contract.Questions.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DB_Contract.Questions.TOPIC + " TEXT,"
            + DB_Contract.Questions.TITLE + " TEXT,"
            + DB_Contract.Questions.CONTENT + " TEXT,"
            + DB_Contract.Questions.USERNAME + " TEXT,"
            + DB_Contract.Questions.QUESTION_IMAGE + " BLOB" + ")";

    private static final String CREATE_QUERY_TBL_FAVORITE = "CREATE TABLE "
            + DB_Contract.Favorite.TABLE_NAME + "("
            + DB_Contract.Favorite.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DB_Contract.Favorite.USER_NAME + " TEXT,"
            + DB_Contract.Favorite.KEY_QUESTION_ID+ " INTEGER" + ")";

    private static final String CREATE_QUERY_TBL_COMMENTS = "CREATE TABLE "
            + DB_Contract.Comments.TABLE_NAME + "("
            + DB_Contract.Comments.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DB_Contract.Comments.CONTENT + " TEXT,"
            + DB_Contract.Comments.DATE + " TEXT,"
            + DB_Contract.Comments.USER_NAME + " TEXT,"
            + DB_Contract.Comments.KEY_QUESTION_ID+ " INTEGER" + ")";


    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("DATABASE OPERATIONS","Database created/opened.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY_TBL_USER);
        db.execSQL(CREATE_QUERY_TBL_QUESTIONS);
        db.execSQL(CREATE_QUERY_TBL_FAVORITE);
        db.execSQL(CREATE_QUERY_TBL_COMMENTS);
        Log.e("DATABASE OPERATIONS","Tables created.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.User.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.Questions.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.Favorite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.Comments.TABLE_NAME);
        onCreate(db);

    }



    /**************** USERS PART ****************/


    public void addUser(String username, String password, byte[] imageInByte, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.User.USER_NAME,username);
        contentValues.put(DB_Contract.User.USER_PASSWORD,password);
        contentValues.put(DB_Contract.User.USER_IMAGE, imageInByte);
        db.insert(DB_Contract.User.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One User inserted");
    }


    public void updateUser(String username, byte[] imageInByte, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(DB_Contract.User.USER_IMAGE, imageInByte);

        String selection =  DB_Contract.User.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};


        // updating row
        int count = db.update(
                DB_Contract.User.TABLE_NAME,
                values,
                selection,
                selectionArg);
    }

    /* delete user from database */
    public void deleteUser(String username, SQLiteDatabase sqLiteDatabase)
    {
        String selection =  DB_Contract.User.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};
        sqLiteDatabase.delete(DB_Contract.User.TABLE_NAME,selection,selectionArg);
    }


    /* read User info for listview . TO DELETE AFTER*/
    public Cursor getInfo(SQLiteDatabase db)
    {
        Cursor  cursor;
        String[] projections = {
                DB_Contract.User.USER_NAME,
                DB_Contract.User.USER_PASSWORD};
        cursor = db.query(DB_Contract.User.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }


    public Cursor getOneUser(String username, SQLiteDatabase db){

        Cursor  cursor;
        String[] projections = {
                DB_Contract.User.USER_NAME,
                DB_Contract.User.USER_IMAGE};

        String selection =  DB_Contract.User.USER_NAME+" LIKE ? ";
        String [] uname = {username};


        cursor = db.query(DB_Contract.User.TABLE_NAME,projections,selection,uname,null,null,null,null);
        return cursor;
    }


    /* verify if the user write username and password correctly */
    public boolean verifyUserLogin(String username, String password) {

        String query = "Select * FROM "
                + DB_Contract.User.TABLE_NAME
                + " WHERE " + DB_Contract.User.USER_NAME + " =  \"" + username + "\""
                + " AND " + DB_Contract.User.USER_PASSWORD + " =  \"" + password + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setUsername(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            cursor.close();
        } else {
            return false;
        }
        db.close();
        return true;
    }


    /* When a user register, verify that the username is not in database */
    public boolean verifyRegisterUsername(String username) {

        String query = "Select * FROM "
                + DB_Contract.User.TABLE_NAME
                + " WHERE " + DB_Contract.User.USER_NAME + " =  '" + username + "'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setUsername(cursor.getString(0));
            cursor.close();
        } else {
            //if the username is not in database
            db.close();
            return false;
        }
        db.close();
        //if the username is already in database
        return true;
    }




    /**************** QUESTIONS PART ****************/


    public void addQuestion(String topic, String title, String content,
                                 String username, byte[] imageInByte, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.Questions.TOPIC,topic);
        contentValues.put(DB_Contract.Questions.TITLE,title);
        contentValues.put(DB_Contract.Questions.CONTENT,content);
        contentValues.put(DB_Contract.Questions.USERNAME,username);
        contentValues.put(DB_Contract.Questions.QUESTION_IMAGE,imageInByte);
        db.insert(DB_Contract.Questions.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One Question inserted");

    }


    public int countUserQuestions(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT COUNT (*) FROM " + DB_Contract.Questions.TABLE_NAME +
                " WHERE " + DB_Contract.Questions.USERNAME  + "=?",
                new String[] { String.valueOf(username) });
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();

    db.close();
    return count;
    }


    /* Displaying all questions */
    public Cursor getQuestionInfo(SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {
                DB_Contract.Questions.KEY_ID,
                DB_Contract.Questions.TOPIC,
                DB_Contract.Questions.TITLE,
                DB_Contract.Questions.CONTENT,
                DB_Contract.Questions.USERNAME,
                DB_Contract.Questions.QUESTION_IMAGE};
        cursor = db.query(DB_Contract.Questions.TABLE_NAME,projectionsQuestion,null,null,null,null,null,null);
        return cursor;
    }


    /* Return all questions from selected topic */
    public Cursor getQuestionInfoFromTopic(String topicSelected, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {
                DB_Contract.Questions.KEY_ID,
                DB_Contract.Questions.TOPIC,
                DB_Contract.Questions.TITLE,
                DB_Contract.Questions.CONTENT,
                DB_Contract.Questions.USERNAME,
                DB_Contract.Questions.QUESTION_IMAGE
        };
        String selection =  DB_Contract.Questions.TOPIC+" LIKE ? ";
        String [] topics = {topicSelected};
        cursor = db.query(DB_Contract.Questions.TABLE_NAME,projectionsQuestion,selection,topics,null,null,null,null);
        return cursor;
    }


    /* Return all questions from current user */
    public Cursor getAllQuestionsFromCurrentUser(String username, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {
                DB_Contract.Questions.KEY_ID,
                DB_Contract.Questions.TOPIC,
                DB_Contract.Questions.TITLE,
                DB_Contract.Questions.CONTENT,
                DB_Contract.Questions.USERNAME,
                DB_Contract.Questions.QUESTION_IMAGE};
        String selection =  DB_Contract.Questions.USERNAME+" LIKE ? ";
        String [] val = {username};
        cursor = db.query(DB_Contract.Questions.TABLE_NAME,projectionsQuestion,selection,val,null,null,null,null);
        return cursor;
    }

    /**************** Favorite PART ****************/

    //add favorite
    public void addFavorite(String username, int question_id, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.Favorite.USER_NAME,username);
        contentValues.put(DB_Contract.Favorite.KEY_QUESTION_ID,question_id);
        db.insert(DB_Contract.Favorite.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One Favorite created");
    }

    /*  This method verify if the user has one question on the favorite table */
    public boolean verifyFavorite(String username, int id_question) {
            String query = "Select * FROM " + DB_Contract.Favorite.TABLE_NAME
                    + " WHERE " + DB_Contract.Favorite.USER_NAME + " =  \"" + username + "\""
                    + " AND " + DB_Contract.Favorite.KEY_QUESTION_ID + " =  \"" + id_question + "\"";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            Favorite f = new Favorite();
            f.toString();

            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                f.setUsername(cursor.getString(0));
                f.setId_question(cursor.getString(1));
                cursor.close();
            } else {
                return false;
            }
            db.close();
            return true;
        }


    public void deleteFavorite(String username, int id_question, SQLiteDatabase sqLiteDatabase) {
        String query = "Delete FROM "
                + DB_Contract.Favorite.TABLE_NAME
                + " WHERE " + DB_Contract.Favorite.USER_NAME + " =  \"" + username + "\""
                + " AND " + DB_Contract.Favorite.KEY_QUESTION_ID + " =  \"" + id_question + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            cursor.close();
        }
        db.close();
    }



    /**************** Comments PART ****************/

    public void addComment(String content, String date, String username,  int question_id, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.Comments.CONTENT,content);
        contentValues.put(DB_Contract.Comments.DATE,date);
        contentValues.put(DB_Contract.Comments.USER_NAME,username);
        contentValues.put(DB_Contract.Comments.KEY_QUESTION_ID,question_id);
        db.insert(DB_Contract.Comments.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One comment created");
    }

    /* Return all questions from current user */
    public Cursor getAllCommentsFromCurrentQuestion(String idQuestion, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsComment = {
                DB_Contract.Comments.KEY_ID,
                DB_Contract.Comments.CONTENT,
                DB_Contract.Comments.DATE,
                DB_Contract.Comments.USER_NAME,
                DB_Contract.Comments.KEY_QUESTION_ID};
        String selection =  DB_Contract.Comments.KEY_QUESTION_ID+" LIKE ? ";

        String [] val = {idQuestion};
        cursor = db.query(DB_Contract.Comments.TABLE_NAME,projectionsComment,selection,val,null,null,null,null);
        return cursor;
    }



}
