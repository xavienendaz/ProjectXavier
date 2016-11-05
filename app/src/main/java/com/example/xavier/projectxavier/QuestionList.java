package com.example.xavier.projectxavier;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        setTitle("Questions list");


        listView = (ListView)findViewById(R.id.listview_questionList);
        listDataAdapterQuestion = new ListDataAdapterQuestion(getApplicationContext(),R.id.question_list_layout);
        listView.setAdapter(listDataAdapterQuestion);
        dbHelper =  new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();

        /* get info from databse */
        cursor =  dbHelper.getQuestionInfo(sqLiteDatabase);
        if(cursor.moveToFirst())
        {
            do{
                String topic, title, content, username;
                topic = cursor.getString(0);
                title = cursor.getString(1);
                content = cursor.getString(2);
                username = cursor.getString(3);
                Question c = new Question(topic, title, content, username);
                listDataAdapterQuestion.add(c);

            }while(cursor.moveToNext());
        }

        /* ListeView handler */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question item = (Question) listDataAdapterQuestion.getItem(position);

                Intent i = new Intent(QuestionList.this, QuestionDisplay.class);
                /* put an Extra in the intent to use Title on the question activity */
                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                QuestionList.this.startActivity(i);




            }
        });




        /* button adding question */
        final ImageButton ib = (ImageButton) findViewById(R.id.ibAddQuestion);
        ib.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent i = new Intent(QuestionList.this, AddingQuestion.class);
                QuestionList.this.startActivity(i);





            }
        });

    }
}
