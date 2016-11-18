package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

import static android.R.id.primary;

public class QuestionDisplay extends AppCompatActivity {

    TextView textViewQuestionTitle, textViewQuestionContent, textViewAuthor;
    String myValueTopicSelected, myValueKeyAuthor;

    Cursor cursor;
    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton fab;
    int myValueKeyIdQuestion;
    Intent goBack;
    String usernameSharedPref;
    SharedPreferences sharedPref;
    ByteArrayInputStream imageStream;
    ImageView  imageView, imageAuthor;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);
         /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(myValueTopicSelected);



        imageAuthor = (ImageView) findViewById(R.id.imgAuthor);








        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Find logo
        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activitySelected = getIntent().getExtras().getString("activitySelected");
                Intent goBack = new Intent(QuestionDisplay.this, QuestionList.class);
                goBack.putExtra("topicSelected", myValueTopicSelected);
                QuestionDisplay.this.startActivity(goBack);
      /*         if(activitySelected == "questionList")
               {
                   Intent goBack = new Intent(QuestionDisplay.this, QuestionList.class);
                   goBack.putExtra("topicSelected", myValueTopicSelected);
                   QuestionDisplay.this.startActivity(goBack);

               }else if(activitySelected.toString() == "favorite")
               {
                   /* when the user open this activity from favorites
                   Intent goBack = new Intent(QuestionDisplay.this, FavoriteList.class);
                   QuestionDisplay.this.startActivity(goBack);
               }else if(activitySelected == "profileList")
     /*          {
                   /* when the user open this activity from profile
                   Intent goBack = new Intent(QuestionDisplay.this, Profile.class);
                   QuestionDisplay.this.startActivity(goBack);
               }
*/

            }
        });


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
        readUserFromDatabase();
        //see the database
        final ImageView share = (ImageView) findViewById(R.id.imvShare);
        share.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                //send the content of the question

                /********** PPROBLEM HERE NOTHING SHARE***********/
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.id.tvQuestionContent);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });



    }



    private void readUserFromDatabase() {
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        /* read the author user from database */
        cursor = dbHelper.getOneUser(myValueKeyAuthor, sqLiteDatabase);

        if(cursor.moveToFirst())
        {
            do {
                String username;
                byte[] image;
                username = cursor.getString(0);
                image = cursor.getBlob(1);
                User u = new User(username, image);

                /* set profile image */
                byte[] data = u.getImage();
                imageStream = new ByteArrayInputStream(data);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                imageAuthor.setImageBitmap(theImage);
            }while (cursor.moveToNext());
        }
    }



    private void setImage() {
        byte[] img = getIntent().getExtras().getByteArray("image");
        imageView = (ImageView) findViewById(R.id.iv_toolbar_detail);
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


}

