package com.example.xavier.projectxavier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

/**
 * Created by Xavier on 05.11.2016.
 */

public class DbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "PROJECT.DB";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_QUERY_TBL_USER =
            "CREATE TABLE "+ DB_Contract.NewUserInfo.TABLE_NAME+"("
                    + DB_Contract.NewUserInfo.USER_NAME+" TEXT,"
                    + DB_Contract.NewUserInfo.USER_PASSWORD+" TEXT);";

/* private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";
*/

    private static final String CREATE_QUERY_TBL_QUESTIONS = "CREATE TABLE "
            + DB_Contract.NewQuestion.TABLE_NAME + "("
            + DB_Contract.NewQuestion.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DB_Contract.NewQuestion.TOPIC + " TEXT,"
            + DB_Contract.NewQuestion.TITLE + " TEXT,"
            + DB_Contract.NewQuestion.CONTENT + " TEXT,"
            + DB_Contract.NewQuestion.USERNAME + " TEXT" + ")";

    private static final String CREATE_QUERY_TBL_FAVORITE = "CREATE TABLE "
            + DB_Contract.NewFavorite.TABLE_NAME + "("
            + DB_Contract.NewFavorite.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DB_Contract.NewFavorite.USER_NAME + " TEXT,"
            + DB_Contract.NewFavorite.KEY_QUESTION_ID+ " INTEGER" + ")";

   /* private static final String CREATE_QUERY_TBL_QUESTIONS = "CREATE TABLE "
            + DB_Contract.NewQuestion.TABLE_NAME + "(" + DB_Contract.NewQuestion.TOPIC + " TEXT,"
            + DB_Contract.NewQuestion.TITLE + " TEXT," + DB_Contract.NewQuestion.CONTENT + " TEXT,"
            + DB_Contract.NewQuestion.USERNAME + " TEXT" + ")";
*/


    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.e("DATABASE OPERATIONS","Database created/opened.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY_TBL_USER);
        db.execSQL(CREATE_QUERY_TBL_QUESTIONS);
        db.execSQL(CREATE_QUERY_TBL_FAVORITE);
        Log.e("DATABASE OPERATIONS","Tables created.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.NewUserInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.NewQuestion.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_Contract.NewFavorite.TABLE_NAME);
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
        String[] projections = {
                DB_Contract.NewUserInfo.USER_NAME,
                DB_Contract.NewUserInfo.USER_PASSWORD};
        cursor = db.query(DB_Contract.NewUserInfo.TABLE_NAME,projections,null,null,null,null,null);
        return cursor;
    }



    /* verify if the user write username and password correctly */
    public boolean verifyUserLogin(String username, String password) {

        String query = "Select * FROM "
                + DB_Contract.NewUserInfo.TABLE_NAME
                + " WHERE " + DB_Contract.NewUserInfo.USER_NAME + " =  \"" + username + "\""
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

        String query = "Select * FROM "
                + DB_Contract.NewUserInfo.TABLE_NAME
                + " WHERE " + DB_Contract.NewUserInfo.USER_NAME + " =  '" + username + "'";

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
        String[] projections = {
                DB_Contract.NewUserInfo.USER_NAME,
                DB_Contract.NewUserInfo.USER_PASSWORD};
        String selection =  DB_Contract.NewUserInfo.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};
        Cursor cursor = sqLiteDatabase.query(DB_Contract.NewUserInfo.TABLE_NAME,projections,selection,selectionArg,null,null,null);
        return cursor;
    }


    //find one User
    public User findOneUser(String username) {
        String query = "Select * FROM "
                + DB_Contract.NewUserInfo.TABLE_NAME
                + " WHERE " + DB_Contract.NewUserInfo.USER_NAME + " =  \"" + username + "\"";

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
   /* public void addQuestion(String topic, String title, String content, String username, Byte[]image, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.NewQuestion.TOPIC,topic);
        contentValues.put(DB_Contract.NewQuestion.TITLE,title);
        contentValues.put(DB_Contract.NewQuestion.CONTENT,content);
        contentValues.put(DB_Contract.NewQuestion.USERNAME,username);
        db.insert(DB_Contract.NewQuestion.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One Question inserted");
    }


*/



    public void addQuestion(String topic, String title, String content, String username, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.NewQuestion.TOPIC,topic);
        contentValues.put(DB_Contract.NewQuestion.TITLE,title);
        contentValues.put(DB_Contract.NewQuestion.CONTENT,content);
        contentValues.put(DB_Contract.NewQuestion.USERNAME,username);
        db.insert(DB_Contract.NewQuestion.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One Question inserted");
    }


    public int countUserQuestions(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("SELECT COUNT (*) FROM " + DB_Contract.NewQuestion.TABLE_NAME +
                " WHERE " + DB_Contract.NewQuestion.USERNAME  + "=?",
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
                DB_Contract.NewQuestion.KEY_ID,
                DB_Contract.NewQuestion.TOPIC,
                DB_Contract.NewQuestion.TITLE,
                DB_Contract.NewQuestion.CONTENT,
                DB_Contract.NewQuestion.USERNAME};
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,null,null,null,null,null,null);
        return cursor;
    }

    /* Return all questions from selected topic */
    public Cursor getQuestionInfoFromTopic(String topicSelected, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {
                DB_Contract.NewQuestion.KEY_ID,
                DB_Contract.NewQuestion.TOPIC,
                DB_Contract.NewQuestion.TITLE,
                DB_Contract.NewQuestion.CONTENT,
                DB_Contract.NewQuestion.USERNAME};
        String selection =  DB_Contract.NewQuestion.TOPIC+" LIKE ? ";
        String [] topics = {topicSelected};
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,selection,topics,null,null,null,null);
        return cursor;
    }

    /* Return all questions from current user */
    public Cursor getAllQuestionsFromCurrentUser(String username, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {
                DB_Contract.NewQuestion.KEY_ID,
                DB_Contract.NewQuestion.TOPIC,
                DB_Contract.NewQuestion.TITLE,
                DB_Contract.NewQuestion.CONTENT,
                DB_Contract.NewQuestion.USERNAME};
        String selection =  DB_Contract.NewQuestion.USERNAME+" LIKE ? ";
        String [] topics = {username};
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,selection,topics,null,null,null,null);
        return cursor;
    }


    /*
     String[] projections = {DB_Contract.NewUserInfo.USER_NAME, DB_Contract.NewUserInfo.USER_PASSWORD};
        String selection =  DB_Contract.NewUserInfo.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};
        Cursor cursor = sqLiteDatabase.query(DB_Contract.NewUserInfo.TABLE_NAME,projections,selection,selectionArg,null,null,null);
        return cursor;


    */


    /**************** FavoriteList PART ****************/

    //add favorite
    public void addFavorite(String username, int question_id, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_Contract.NewFavorite.USER_NAME,username);
        contentValues.put(DB_Contract.NewFavorite.KEY_QUESTION_ID,question_id);
        db.insert(DB_Contract.NewFavorite.TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATIONS", "One Favorite created");
    }


    public Cursor getFavoriteQuestions(String username, int id_question, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {
                DB_Contract.NewQuestion.TOPIC,
                DB_Contract.NewQuestion.TITLE,
                DB_Contract.NewQuestion.CONTENT,
                DB_Contract.NewQuestion.USERNAME};
        String selection =  DB_Contract.NewFavorite.USER_NAME+" LIKE ? ";
        String [] topics = {username};
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,selection,topics,null,null,null);
        return cursor;

    }

    /*  This method verify if the user has one question on the favorite table */

    public boolean verifyFavorite(String username, int id_question) {
            String query = "Select * FROM " + DB_Contract.NewFavorite.TABLE_NAME
                    + " WHERE " + DB_Contract.NewFavorite.USER_NAME + " =  \"" + username + "\""
                    + " AND " + DB_Contract.NewFavorite.KEY_QUESTION_ID + " =  \"" + id_question + "\"";

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
                + DB_Contract.NewFavorite.TABLE_NAME
                + " WHERE " + DB_Contract.NewFavorite.USER_NAME + " =  \"" + username + "\""
                + " AND " + DB_Contract.NewFavorite.KEY_QUESTION_ID + " =  \"" + id_question + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            cursor.close();
        }
        db.close();

     /*   String selection =  DB_Contract.NewFavorite.USER_NAME+" LIKE ? ";
        String [] selectionArg = {username};


        sqLiteDatabase.delete(DB_Contract.NewFavorite.TABLE_NAME,selection,selectionArg);
*/


    }




    /*
    *
    *
    * */
/*
*    public Cursor getQuestionInfoFromTopic(String topicSelected, SQLiteDatabase db) {
        Cursor  cursor;
        String[] projectionsQuestion = {DB_Contract.NewQuestion.TOPIC, DB_Contract.NewQuestion.TITLE,
                DB_Contract.NewQuestion.CONTENT, DB_Contract.NewQuestion.USERNAME};
        String selection =  DB_Contract.NewQuestion.TOPIC+" LIKE ? ";
        String [] topics = {topicSelected};
        cursor = db.query(DB_Contract.NewQuestion.TABLE_NAME,projectionsQuestion,selection,topics,null,null,null);
        return cursor;

    }*/


  /*  public List<Question> getAllToDosByTag(String tag_name) {
        List<Question> todos = new ArrayList<Question>();

        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID;

        //Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Question q = new Question();
                q.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                q.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                q.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }
*/


}
