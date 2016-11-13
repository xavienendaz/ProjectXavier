package com.example.xavier.projectxavier;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

public class QuestionList extends AppCompatActivity {

    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DbHelper dbHelper;
    Cursor cursor;
    ListDataAdapterQuestion listDataAdapterQuestion;
    String myValueTopicSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

      /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");
        setTitle(myValueTopicSelected);


        listView = (ListView) findViewById(R.id.listview_questionList);
        listDataAdapterQuestion = new ListDataAdapterQuestion(getApplicationContext(), R.id.question_list_layout);
        listView.setAdapter(listDataAdapterQuestion);
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();

        /* get info from databse */
        cursor = dbHelper.getQuestionInfoFromTopic(myValueTopicSelected, sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                String topic, title, content, username;
                topic = cursor.getString(0);
                title = cursor.getString(1);
                content = cursor.getString(2);
                username = cursor.getString(3);
                Question c = new Question(topic, title, content, username);
                listDataAdapterQuestion.add(c);

            } while (cursor.moveToNext());
        }

        /* ListeView handler: Display the selected question */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question item = (Question) listDataAdapterQuestion.getItem(position);

                Intent i = new Intent(QuestionList.this, QuestionDisplay.class);
                /* put an Extra in the intent to use Title on the question activity */
                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                i.putExtra("myValueKeyIdQuestion", item.getId());
                i.putExtra("topicSelected", myValueTopicSelected);
                QuestionList.this.startActivity(i);


            }
        });
    }






  /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_list, menu);
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
                Intent goFavorite = new Intent(this, Favorite.class);
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

            case R.id.action_add:
                Intent i = new Intent(QuestionList.this, AddingQuestion.class);
                QuestionList.this.startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}

