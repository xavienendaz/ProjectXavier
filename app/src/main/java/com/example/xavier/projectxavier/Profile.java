package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Profile extends AppCompatActivity{

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView textViewUsername;
    String usernameSharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profile);

        /* Read username from sharedPreferences */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");
        textViewUsername = (TextView) findViewById(R.id.tvUsername);
        textViewUsername.setText(usernameSharedPref);


        countUserPosts();



    }

    private void countUserPosts() {
        dbHelper = new DbHelper(context);
        TextView  textViewCount = (TextView) findViewById(R.id.tvNbQuestions);
        int cpt = dbHelper.countUserQuestions(usernameSharedPref);
        textViewCount.setText(""+cpt);
    }







  /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*Actionbar's actions*/
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_topics:
                Intent goHome = new Intent(this, TopicsList.class);
                startActivity(goHome);
                return true;

            case R.id.action_favorite:
                Intent goFavorite = new Intent(this, ListFavorite.class);
                startActivity(goFavorite);
                return true;

            case R.id.action_add_question:
                Intent goAdd = new Intent(this, AddingQuestion.class);
                startActivity(goAdd);
                return true;

            case R.id.action_profile:
                Intent goProfile = new Intent(this, Profile.class);
                startActivity(goProfile);
                return true;

            case R.id.action_settings:
                Intent goSettings = new Intent(this, Settings.class);
                startActivity(goSettings);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}



