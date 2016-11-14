package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionDisplay extends AppCompatActivity {

    TextView textViewQuestionTitle, textViewQuestionContent;
    String myValueTopicSelected;

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton fab;
    int myValueKeyIdQuestion;

    String usernameSharedPref;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);
         /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");


        setTitle(myValueTopicSelected);
        textViewQuestionTitle = (TextView) findViewById(R.id.tvTitle);
        textViewQuestionContent = (TextView) findViewById(R.id.tvQuestionContent);

        /* Recover Object Question from activity_question_list */
        String myValueTitle = getIntent().getExtras().getString("myValueKeyTitle");
        textViewQuestionTitle.setText(myValueTitle);

        String myValueContent = getIntent().getExtras().getString("myValueKeyContent");
        textViewQuestionContent.setText(myValueContent);


        myValueKeyIdQuestion = getIntent().getExtras().getInt("myValueKeyIdQuestion");
        fab = (FloatingActionButton) findViewById(R.id.fabFavorite);
        dbHelper = new DbHelper(context);


        setFabImage();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");


        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
 /* Read username from sharedPreferences */

                //myValueKeyIdQuestion = getIntent().getExtras().getInt("myValueKeyIdQuestion");

                if(dbHelper.verifyFavorite(usernameSharedPref, myValueKeyIdQuestion) == true){

                    /* If the question is in favorites, we delete the row from table */
                    dbHelper.deleteFavorite(usernameSharedPref, myValueKeyIdQuestion, sqLiteDatabase);
                    Toast.makeText(getBaseContext(), "Favorite delete", Toast.LENGTH_SHORT).show();

                }else{
                    /* If the question is not in favorites */


                    sqLiteDatabase = dbHelper.getWritableDatabase();
                    dbHelper.addFavorite(usernameSharedPref, myValueKeyIdQuestion, sqLiteDatabase);
                    Toast.makeText(getBaseContext(), "Favorite added", Toast.LENGTH_SHORT).show();
                    dbHelper.close();


                }
                setFabImage();

            }
        });




    }

    public void setFabImage(){
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
              /* verify database if the current has this question on his favorites */
        if(dbHelper.verifyFavorite(usernameSharedPref, myValueKeyIdQuestion) == true){

            /* in favorite*/
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
        }
        else{
            /* not in favorite*/
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
        }

    }




  /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_display, menu);
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

            case R.id.action_share:
                //redirect the user on loginActivity when he deleted his account
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                //send the content of the question
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.id.tvQuestionContent);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}

