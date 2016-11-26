package com.example.xavier.projectxavier;


import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class QuestionList extends AppCompatActivity {

    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DbHelper dbHelper;
    Cursor cursor, cursorEN, cursorFR;
    QuestionListDataAdapter questionListDataAdapter;
    String myValueTopicSelected, topicFromListView,  questionClickUsername;
    Button b1, b2;
    LanguageLocalHelper languageLocalHelper;
    String currentLanguage;
    TextView tvCurrentTopic, tvCommentCpt,tvQuestionAuthor;
    Context context = this;
    ByteArrayInputStream imageStream;
    ImageView imageView, imageAuthor;
    Question q;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

      /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");



        listView = (ListView) findViewById(R.id.listview_questionList);
        questionListDataAdapter = new QuestionListDataAdapter(getApplicationContext(), R.id.question_list_layout);
        listView.setAdapter(questionListDataAdapter);
        dbHelper = new DbHelper(getApplicationContext());

        currentLanguage = languageLocalHelper.getLanguage(QuestionList.this).toString();


        setQuestionListFromDate();




        /* Set title and count nb questions */
        setTitle(myValueTopicSelected + " ("+ questionListDataAdapter.getCount()+")");











    }


    private void setQuestionListFromDate() {
        /* all the question from the last ID to first ID (order by date) */
        cursor = dbHelper.getQuestionInfoFromTopic(myValueTopicSelected);
        if (cursor.moveToLast()) {
            do {
                int id;
                String topic, title, content, username, nbLike, date;
                byte[] image;
                id = cursor.getInt(0);
                topic = cursor.getString(1);
                title = cursor.getString(2);
                content = cursor.getString(3);
                username = cursor.getString(4);
                image = cursor.getBlob(5);
                date = cursor.getString(6);

                nbLike = String.valueOf(dbHelper.countPositiveVote(id));

                Question c = new Question(id, topic, title, content, username, image, nbLike, date);
                questionListDataAdapter.add(c);

            } while (cursor.moveToPrevious());
        }


        listViewOnClickListener();

        listViewOnLongClickListener();

    }


    public void listViewOnLongClickListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                q = (Question) questionListDataAdapter.getItem(position);

                // count nb comment for selected question
                int nbComment = dbHelper.countQuestionComments(q.getId());

                // count nb question for selected question author
                int nbUserQuestion = dbHelper.countUserQuestions(q.getUsername());

                // instantiate an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionList.this);

                // set dialog message
                builder.setTitle(q.getTopic());
                builder.setMessage(
                        R.string.comment+" "+nbComment+
                                "\n"+
                                "\n"+
                                R.string.author +"  "+q.getUsername()+
                                "\n"+ R.string.nb_posts +"  "+nbUserQuestion)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true; // true because I dont want to be redirected on the activity Quetiondisplay
            }
        });

    }

    public void listViewOnClickListener(){
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {

            Question item = (Question) questionListDataAdapter.getItem(position);

            Intent i = new Intent(QuestionList.this, QuestionDisplay.class);
            /* put Extra in the intent for displaying question in QuestionDisplay*/
            i.putExtra("myValueKeyTitle", item.getTitle());
            i.putExtra("myValueKeyContent", item.getContent());
            i.putExtra("myValueKeyIdQuestion", item.getId());
            i.putExtra("myValueKeyAuthor", item.getUsername());
            i.putExtra("topicSelected", item.getTopic());
            i.putExtra("image", item.getImage());
            i.putExtra("date", item.getDate());
            i.putExtra("activitySelected", "questionList");
            QuestionList.this.startActivity(i);

        }
    });
}


  /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question_list, menu);
        return true;
    }


    /*Actionbar's actions*/
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.menu_sortTime:
                    questionListDataAdapter.clear();
                    setQuestionListFromDate();
                    return true;

                case R.id.menu_sortTimeOld:
                    questionListDataAdapter.clear();
                    cursor = dbHelper.getQuestionInfoFromTopic(myValueTopicSelected);
                    if (cursor.moveToFirst()) {
                        do {
                            int id;
                            String topic, title, content, username, nbLike, date;
                            byte [] image;
                            id = cursor.getInt(0);
                            topic = cursor.getString(1);
                            title = cursor.getString(2);
                            content = cursor.getString(3);
                            username = cursor.getString(4);
                            image = cursor.getBlob(5);
                            date = cursor.getString(6);
                            nbLike = String.valueOf(dbHelper.countPositiveVote(id));

                            Question c = new Question(id, topic, title, content, username, image, nbLike, date);
                            questionListDataAdapter.add(c);

                        } while (cursor.moveToNext());
                    }
                    listViewOnClickListener();
                    return true;

                case R.id.menu_sortASC:
                    questionListDataAdapter.clear();
                    cursor = dbHelper.getQuestionInfoFromTopicASC(myValueTopicSelected);
                    if (cursor.moveToFirst()) {
                        do {
                            int id;
                            String topic, title, content, username, nbLike, date;
                            byte [] image;
                            id = cursor.getInt(0);
                            topic = cursor.getString(1);
                            title = cursor.getString(2);
                            content = cursor.getString(3);
                            username = cursor.getString(4);
                            image = cursor.getBlob(5);
                            date = cursor.getString(6);
                            nbLike = String.valueOf(dbHelper.countPositiveVote(id));

                            Question c = new Question(id, topic, title, content, username, image, nbLike, date);
                            questionListDataAdapter.add(c);

                        } while (cursor.moveToNext());
                    }
                    listViewOnClickListener();
                    return true;

                case R.id.menu_sortDESC:
                    questionListDataAdapter.clear();
                    cursor = dbHelper.getQuestionInfoFromTopicDESC(myValueTopicSelected);
                    if (cursor.moveToFirst()) {
                        do {
                            int id;
                            String topic, title, content, username, nbLike, date;
                            byte [] image;
                            id = cursor.getInt(0);
                            topic = cursor.getString(1);
                            title = cursor.getString(2);
                            content = cursor.getString(3);
                            username = cursor.getString(4);
                            image = cursor.getBlob(5);
                            date = cursor.getString(6);
                            nbLike = String.valueOf(dbHelper.countPositiveVote(id));

                            Question c = new Question(id, topic, title, content, username, image, nbLike, date);
                            questionListDataAdapter.add(c);

                        } while (cursor.moveToNext());
                    }
                    listViewOnClickListener();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);

            }
        }
    }


