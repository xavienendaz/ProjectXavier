package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class QuestionDisplay extends AppCompatActivity {

    TextView textViewQuestionTitle, textViewQuestionContent, textViewAuthor;
    String myValueTopicSelected, myValueKeyAuthor;

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton fab;
    int myValueKeyIdQuestion;

    String usernameSharedPref;
    SharedPreferences sharedPref;
    ByteArrayInputStream imageStream;
    ImageView  imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);
         /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");
        setTitle(myValueTopicSelected);



        textViewQuestionTitle = (TextView) findViewById(R.id.tvTitle);
        textViewQuestionContent = (TextView) findViewById(R.id.tvQuestionContent);
        textViewAuthor= (TextView) findViewById(R.id.tvAuthor);

        /* Recover Object Question from activity_question_list */
        String myValueTitle = getIntent().getExtras().getString("myValueKeyTitle");
        textViewQuestionTitle.setText(myValueTitle);

        String myValueContent = getIntent().getExtras().getString("myValueKeyContent");
        textViewQuestionContent.setText(myValueContent);

        myValueKeyAuthor = getIntent().getExtras().getString("myValueKeyAuthor");
        textViewAuthor.setText(myValueKeyAuthor);

        myValueKeyIdQuestion = getIntent().getExtras().getInt("myValueKeyIdQuestion");
        fab = (FloatingActionButton) findViewById(R.id.fabFavorite);
        dbHelper = new DbHelper(context);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");


        setFabImage();

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



        countUserPosts();

        setImage();





    }

    private void setImage() {
        byte[] img = getIntent().getExtras().getByteArray("image");
        imageView = (ImageView) findViewById(R.id.imageViewImageQuestionDisplay);
        imageStream = new ByteArrayInputStream(img);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(theImage);
    }


    private void countUserPosts() {
        dbHelper = new DbHelper(context);
        TextView  textViewNbPost = (TextView) findViewById(R.id.nbPosts);
        int cpt = dbHelper.countUserQuestions(myValueKeyAuthor);
        textViewNbPost.setText(""+cpt);
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
        getMenuInflater().inflate(R.menu.menu_question_display, menu);
        return true;
    }

    /*Actionbar's actions*/
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_return:
                Intent goBack = new Intent(this, QuestionList.class);
                goBack.putExtra("topicSelected", myValueTopicSelected);
                startActivity(goBack);
                return true;

            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                //send the content of the question

                /********** PPROBLEM HERE NOTHING SHARE***********/
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.id.tvQuestionContent);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}

