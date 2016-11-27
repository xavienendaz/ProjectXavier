package com.example.xavier.projectxavier;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class QuestionList extends AppCompatActivity {

    ListView listView;
    DbHelper dbHelper;
    Cursor cursor;
    QuestionListDataAdapter questionListDataAdapter;
    String myValueTopicSelected;
    LanguageLocalHelper languageLocalHelper;
    String currentLanguage;
    Context context = this;
    Question q;
    AlertDialog.Builder builder;


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


        // if the user is in all questions
        if(myValueTopicSelected.equalsIgnoreCase("All") ||
                myValueTopicSelected.equalsIgnoreCase("Tout")){
            //display all questions
            setAllQuestions();

        }else{
            //if the user select a topic
            displayQuestionsFromTopicSortNew();
        }





        /* Set title and count nb questions */
        setTitle(myValueTopicSelected + " ("+ questionListDataAdapter.getCount()+")");











    }


    //by default all the questions are sorted by New in date
    private void setAllQuestions() {

            //if the user has selected all questions
            if (myValueTopicSelected.equalsIgnoreCase("All")) {
                // if the app is in English, we want only the english questions
                cursor = dbHelper.getAllQuestionsEN();
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

            } else if (myValueTopicSelected.equalsIgnoreCase("Tout")) {
                // if the app is in french
                cursor = dbHelper.getAllQuestionsFR();
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
            } else {
                //if the user has selected a topic
                displayQuestionsFromTopicSortNew();
            }


            listViewOnClickListenerAllQuestions();
            listViewOnLongClickListener();

    }


    private void displayQuestionsFromTopicSortNew() {
        /* all the question from the last ID to first ID (order by date) */
        cursor = dbHelper.getAllQuestionsFromTopic(myValueTopicSelected);
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

    // when user click long on a questions, several information are show in a dialog
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
                builder = new AlertDialog.Builder(QuestionList.this);

                // set dialog message
                builder.setTitle(q.getTopic());
                builder.setMessage(
                        context.getResources().getString(R.string.commentInfo)+" "+nbComment+
                                "\n"+
                                "\n"+
                                context.getResources().getString(R.string.author) +"  "+q.getUsername()+
                                "     "+
                                context.getResources().getString(R.string.nb_posts) +"  "+nbUserQuestion)
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


        /*
         listViewOnClickListenerAllQuestions() change topic selected by all topics
         because when a User click the back arrow from QuestionDisplay activity, he wants to be redirected correctly
         in all topics and not in question topic
          */

    public void listViewOnClickListenerAllQuestions(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question item = (Question) questionListDataAdapter.getItem(position);

                Intent i = new Intent(QuestionList.this, QuestionDisplay.class);

                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                i.putExtra("myValueKeyIdQuestion", item.getId());
                i.putExtra("myValueKeyAuthor", item.getUsername());
                //change item.getTopic() in myValueTopicSelected
                i.putExtra("topicSelected", myValueTopicSelected);
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

                case R.id.menu_add_question:
                    Intent goHome = new Intent(this, QuestionAdd.class);
                    startActivity(goHome);
                    return true;

                case R.id.menu_sortTimeNew:
                    questionListDataAdapter.clear();
                    setAllQuestions();
                    return true;

                case R.id.menu_sortTimeOld:
                    questionListDataAdapter.clear();

                    // if the user is in all questions
                    if (myValueTopicSelected.equalsIgnoreCase("All")) {
                        // if the app is in English, we want only the english questions
                        cursor = dbHelper.getAllQuestionsEN();
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListenerAllQuestions();

                    } else if (myValueTopicSelected.equalsIgnoreCase("Tout")) {
                        // if the app is in french
                        cursor = dbHelper.getAllQuestionsFR();
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListenerAllQuestions();

                    }else{
                        //if the user has selected a topic
                        cursor = dbHelper.getAllQuestionsFromTopic(myValueTopicSelected);
                        //order by last with inverse cursor
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
                    }

                    listViewOnLongClickListener();

                    return true;


                case R.id.menu_sortASC:
                    questionListDataAdapter.clear();

                    // if the user is in all questions
                    if (myValueTopicSelected.equalsIgnoreCase("All")) {
                        // if the app is in English, we want only the english questions
                        cursor = dbHelper.getAllQuestionsFromTopicSortASCEN();
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListenerAllQuestions();

                    } else if (myValueTopicSelected.equalsIgnoreCase("Tout")) {
                        // if the app is in french
                        cursor = dbHelper.getAllQuestionsFromTopicSortASCFR();
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListenerAllQuestions();

                    }else {

                        // if the User has selected a topis
                        cursor = dbHelper.getAllQuestionsFromTopicSortASC(myValueTopicSelected);
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListener();
                    }

                    listViewOnLongClickListener();
                    return true;

                case R.id.menu_sortDESC:
                    questionListDataAdapter.clear();

                    // if the user is in all questions
                    if (myValueTopicSelected.equalsIgnoreCase("All")) {
                        // if the app is in English, we want only the english questions
                        cursor = dbHelper.getAllQuestionsFromTopicSortDESCEN();
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListenerAllQuestions();

                    } else if (myValueTopicSelected.equalsIgnoreCase("Tout")) {
                        // if the app is in french
                        cursor = dbHelper.getAllQuestionsFromTopicSortDESCFR();
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListenerAllQuestions();

                    }else {

                        // if the User has selected a topis
                        cursor = dbHelper.getAllQuestionsFromTopicSortDESC(myValueTopicSelected);
                        if (cursor.moveToFirst()) {
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

                            } while (cursor.moveToNext());
                        }
                        listViewOnClickListener();
                    }

                    listViewOnLongClickListener();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);

            }
        }
    }


