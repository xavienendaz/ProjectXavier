package com.example.xavier.projectxavier;

import android.content.Context;
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

public class Profile extends AppCompatActivity{

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView textViewUsername, tvEmptyUserList;
    String usernameSharedPref;
    Cursor cursor;
    ListDataAdapterProfile listDataAdapterProfile;
    SharedPreferences sharedPref;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profile);

        /* Read username from sharedPreferences */
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");
        textViewUsername = (TextView) findViewById(R.id.tvUsername);
        textViewUsername.setText(usernameSharedPref);


        countUserPosts();

        displayUserPostsList();



    }

    private void displayUserPostsList() {
        tvEmptyUserList = (TextView) findViewById(R.id.tvEmptyUserCurrentList);


        listView = (ListView) findViewById(R.id.listview_questionList_profile);
        listDataAdapterProfile = new ListDataAdapterProfile(getApplicationContext(), R.id.profile_list_layout);
        listView.setAdapter(listDataAdapterProfile);
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();


        cursor = dbHelper.getAllQuestionsFromCurrentUser(usernameSharedPref, sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                String topic, title, content, username;
                int id;

                id = cursor.getInt(0);
                topic = cursor.getString(1);
                title = cursor.getString(2);
                content = cursor.getString(3);
                username = cursor.getString(4);
                Question c = new Question(id, topic, title, content, username);
                c.toString();

                listDataAdapterProfile.add(c);

            } while (cursor.moveToNext());
        }

        /* ListeView handler: Display the selected question */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question item = (Question) listDataAdapterProfile.getItem(position);

                Intent i = new Intent(Profile.this, QuestionDisplay.class);
                /* put an Extra in the intent to use Title on the question activity */
                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                i.putExtra("myValueKeyIdQuestion", item.getId());
                i.putExtra("myValueKeyAuthor", item.getUsername());
                i.putExtra("topicSelected", item.getTopic());
                Profile.this.startActivity(i);
            }
        });
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



