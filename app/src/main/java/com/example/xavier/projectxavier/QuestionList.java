package com.example.xavier.projectxavier;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.Collections;

public class QuestionList extends AppCompatActivity {

    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DbHelper dbHelper;
    Cursor cursor;
    ListDataAdapterQuestion listDataAdapterQuestion;
    String myValueTopicSelected, topicFromListView;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

      /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");



        listView = (ListView) findViewById(R.id.listview_questionList);
        listDataAdapterQuestion = new ListDataAdapterQuestion(getApplicationContext(), R.id.question_list_layout);
        listView.setAdapter(listDataAdapterQuestion);
        dbHelper = new DbHelper(getApplicationContext());


        setQuestionListFromDate();




        /* Set title and count nb questions */
        setTitle(myValueTopicSelected + " ("+listDataAdapterQuestion.getCount()+")");










    }


    private void setQuestionListFromDate() {
                /* get info from databse */
        cursor = dbHelper.getQuestionInfoFromTopic(myValueTopicSelected);
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
                i.putExtra("myValueKeyAuthor", item.getUsername());
                i.putExtra("topicSelected", item.getTopic());
                i.putExtra("image", item.getImage());
                //         i.putExtra("activitySelected", "questionList");
                QuestionList.this.startActivity(i);

            }
        });

    }






  /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question_list, menu);

        // add this

        return true;
    }


    /*Actionbar's actions*/
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.action_add:
                    Intent i = new Intent(QuestionList.this, AddingQuestion.class);
                    i.putExtra("topicSelected", topicFromListView);
                    QuestionList.this.startActivity(i);
                    return true;

                case R.id.backArrow:
                    Intent goHome = new Intent(this, TopicsList.class);
                    startActivity(goHome);
                    return true;

                case R.id.menu_sort:
                    //Intent goHome = new Intent(this, TopicsList.class);
                   // startActivity(goHome);



                   // listView.setAdapter(listDataAdapterQuestion);
                    //listDataAdapterQuestion = new ListDataAdapterQuestion(getApplicationContext(), R.id.question_list_layout);
                  //  listDataAdapterQuestion.notifyDataSetChanged();
                    listView.setAdapter(null);

                    ListDataAdapterQuestion
                            listDataAdapterQuestion2 = new
                            ListDataAdapterQuestion(getApplicationContext(), R.id.question_list_layout);

                    listView.setAdapter(listDataAdapterQuestion2);

                    dbHelper.getQuestionInfoFromTopicASC(myValueTopicSelected);
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
                        listDataAdapterQuestion2.add(c);

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
                            i.putExtra("myValueKeyAuthor", item.getUsername());
                            i.putExtra("topicSelected", item.getTopic());
                            i.putExtra("image", item.getImage());
                            //         i.putExtra("activitySelected", "questionList");
                            QuestionList.this.startActivity(i);

                        }
                    });
                    

                    return true;

                default:
                    return super.onOptionsItemSelected(item);

            }

        }

    }


