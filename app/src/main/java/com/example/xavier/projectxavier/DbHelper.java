package com.example.xavier.projectxavier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import static android.provider.MediaStore.Images.Thumbnails.IMAGE_ID;

/**
 * Created by Xavier on 05.11.2016.
 */

public class DbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "PROJECT.DB";
    private static final int DATABASE_VERSION = 3;

    private static final String CREATE_QUERY_TBL_USER =
            "CREATE TABLE "+ DB_Contract.NewUserInfo.TABLE_NAME+"("+ DB_Contract.NewUserInfo.USER_NAME+" TEXT,"
                    + DB_Contract.NewUserInfo.USER_PASSWORD+" TEXT);";


    private static final String CREATE_QUERY_TBL_QUESTIONS = "CREATE TABLE "
            + DB_Contract.NewQuestion.TABLE_NAME + "(" + DB_Contract.NewQuestion.TOPIC + " TEXT,"
            + DB_Contract.NewQuestion.TITLE + " TEXT," + DB_Contract.NewQuestion.CONTENT + " TEXT,"
            + DB_Contract.NewQuestion.USERNAME + " TEXT," + DB_Contract.NewQuestion.IMAGE+ " TEXT" + ")";



    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("DATABASE OPERATIONS","Database created/opened.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY_TBL_USER);
        db.execSQL(CREATE_QUERY_TBL_QUESTIONS);
        Log.e("DATABASE OPERATIONS","Tables created.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.NewUserInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.NewQuestion.TABLE_NAME);
        // Create tables again
        onCreate(db);

    }



    /**************** USERS PART ****************/


    //add User
    public void addInfo(String username, String password, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.NewUserInfo.USER_NAME,username);
        contentValues.put(DB_Contract.NewUserInfo.USER_PASSWORD,password);
        db.insert(DB_Contract.NewUserInfo.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One User inserted");
    }


    //delete user from database
    public void deleteUser(String username, SQLiteDatabase sqLiteDatabase)
    {
        String selection =  DB_Contract.NewUserInfo.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};
        sqLiteDatabase.delete(DB_Contract.NewUserInfo.TABLE_NAME,selection,selectionArg);

    }


    //read User info from database in listview
    public Cursor getInfo(SQLiteDatabase db)
    {
        Cursor  cursor;
        String[] projections = {DB_Contract.NewUserInfo.USER_NAME, DB_Contract.NewUserInfo.USER_PASSWORD};
        cursor = db.query(DB_Contract.NewUserInfo.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }



    /* verify if the user write username and password correctly */
    public boolean verifyUserLogin(String username, String password) {

        String query = "Select * FROM " + DB_Contract.NewUserInfo.TABLE_NAME + " WHERE "
                + DB_Contract.NewUserInfo.USER_NAME + " =  \"" + username + "\""
                + " AND " + DB_Contract.NewUserInfo.USER_PASSWORD + " =  \"" + password + "\"";

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

        String query = "Select * FROM " + DB_Contract.NewUserInfo.TABLE_NAME + " WHERE "
                + DB_Contract.NewUserInfo.USER_NAME + " =  '" + username + "'";

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


    //get user from database
    public Cursor getUser(String username, SQLiteDatabase sqLiteDatabase){
        String[] projections = {DB_Contract.NewUserInfo.USER_NAME, DB_Contract.NewUserInfo.USER_PASSWORD};
        String selection =  DB_Contract.NewUserInfo.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};
        Cursor cursor = sqLiteDatabase.query(DB_Contract.NewUserInfo.TABLE_NAME,projections,selection,selectionArg,null,null,null);
        return cursor;
    }


    //find one User
    public User findOneUser(String username) {
        String query = "Select * FROM " + DB_Contract.NewUserInfo.TABLE_NAME + " WHERE "
                + DB_Contract.NewUserInfo.USER_NAME + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            //user.setID(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }


    /**************** QUESTIONS PART ****************/


    //add question
    public void addQuestion(String topic, String title, String content, String username, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.NewQuestion.TOPIC,topic);
        contentValues.put(DB_Contract.NewQuestion.TITLE,title);
        contentValues.put(DB_Contract.NewQuestion.CONTENT,content);
        contentValues.put(DB_Contract.NewQuestion.USERNAME,username);
        db.insert(DB_Contract.NewQuestion.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One Question inserted");
    }

    // Getting Question Count
    public int getQuestionCount() {
        String countQuery = "SELECT  * FROM " + DB_Contract.NewQuestion.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    /* For desplaying all questions */
    public Cursor getQuestionInfo(SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {DB_Contract.NewQuestion.TOPIC, DB_Contract.NewQuestion.TITLE,
                DB_Contract.NewQuestion.CONTENT, DB_Contract.NewQuestion.USERNAME};
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,null,null,null,null,null);
        return cursor;
    }


    public Cursor getQuestionInfoFromTopic(String topicSelected, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {DB_Contract.NewQuestion.TOPIC, DB_Contract.NewQuestion.TITLE,
                DB_Contract.NewQuestion.CONTENT, DB_Contract.NewQuestion.USERNAME};
        String selection =  DB_Contract.NewQuestion.TOPIC+" LIKE ? ";
        String [] topics = {topicSelected};
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,null,null,null,null,null);
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,selection,topics,null,null,null);
        return cursor;

    }

    /*
     String[] projections = {DB_Contract.NewUserInfo.USER_NAME, DB_Contract.NewUserInfo.USER_PASSWORD};
        String selection =  DB_Contract.NewUserInfo.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};
        Cursor cursor = sqLiteDatabase.query(DB_Contract.NewUserInfo.TABLE_NAME,projections,selection,selectionArg,null,null,null);
        return cursor;


    */


    /**************** IMAGE PART ****************/

    public void insetImage(Drawable dbDrawable, String imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_ID, imageId);
        Bitmap bitmap = ((BitmapDrawable)dbDrawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(DB_Contract.NewQuestion.IMAGE, stream.toByteArray());
        db.insert(DB_Contract.NewQuestion.TITLE, null, values);
        db.close();
    }




}
