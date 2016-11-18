package com.example.xavier.projectxavier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class FavoriteList extends AppCompatActivity {




    TextView textView;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DbHelper dbHelper;
    Cursor cursor;
    ListDataAdapterQuestion listDataAdapterQuestion;
    int cpt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        setTitle(R.string.Favorite);
     /* Recover Object Question from activity_question_list */


        textView = (TextView) findViewById(R.id.tvEmptyFavList);


        listView = (ListView) findViewById(R.id.listview_questionList_favorite);
        listDataAdapterQuestion = new ListDataAdapterQuestion(getApplicationContext(), R.id.question_list_layout);
        listView.setAdapter(listDataAdapterQuestion);
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();



         /* Read username from sharedPreferences */
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String usernameSharedPref = sharedPref.getString("username", "");




        /* get info from databse */
        cursor = dbHelper.getQuestionInfo(sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                int id;
                String topic, title, content, username;
                byte [] image;
                id = cursor.getInt(0);
                topic = cursor.getString(1);
                title = cursor.getString(2);
                content = cursor.getString(3);
                username = cursor.getString(4);
                image = cursor.getBlob(5);
                Question c = new Question(id, topic, title, content, username, image);




                /*  Here we need to verify if the user has put the question, c, in his favorites */
                if(dbHelper.verifyFavorite(usernameSharedPref, c.getId()) == true){
                    listDataAdapterQuestion.add(c);
                    cpt=1;
                }



            } while (cursor.moveToNext());
        }

         /* Display message when list is empty*/
        if(cpt==0){
            textView.setText(R.string.emptyList);
        }

        /* ListeView handler: Display the selected question */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question item = (Question) listDataAdapterQuestion.getItem(position);

                Intent i = new Intent(FavoriteList.this, QuestionDisplay.class);
                /* put an Extra in the intent to use Title on the question activity */
                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                i.putExtra("myValueKeyIdQuestion", item.getId());
                i.putExtra("myValueKeyAuthor", item.getUsername());
                i.putExtra("topicSelected", item.getTopic());
                i.putExtra("image", item.getImage());
        //        i.putExtra("activitySelected", "favorite");
                FavoriteList.this.startActivity(i);


            }
        });
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
                Intent goFavorite = new Intent(this, FavoriteList.class);
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






