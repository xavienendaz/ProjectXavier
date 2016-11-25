package com.example.xavier.projectxavier;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteList extends AppCompatActivity {




    TextView textView;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DbHelper dbHelper;
    Cursor cursor;
    QuestionListDataAdapter questionListDataAdapter;
    int cpt=0;
    FloatingActionButton favDelete;
    String usernameSharedPref;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        setTitle(R.string.Favorite);
     /* Recover Object Question from activity_question_list */


        textView = (TextView) findViewById(R.id.tvEmptyFavList);

        listView = (ListView) findViewById(R.id.listview_questionList_favorite);
        questionListDataAdapter = new QuestionListDataAdapter(getApplicationContext(), R.id.question_list_layout);
        listView.setAdapter(questionListDataAdapter);
        dbHelper = new DbHelper(getApplicationContext());



         /* Read username from sharedPreferences */
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");


        setFavoritesList();



        favDelete = (FloatingActionButton) findViewById(R.id.fabDeleteAllFavorite);
        favDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Verify if the user has some favorites
                if(questionListDataAdapter.isEmpty()){
                    Toast.makeText(getBaseContext(), R.string.NofavoriDeleted, Toast.LENGTH_SHORT).show();
                }else{
                    // ask the user if he is sure to delete all his favorites
                    new AlertDialog.Builder(FavoriteList.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.deleteAllFavorites)
                            .setMessage(R.string.deleteAllFavoriteMessage)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // delete all user favorites
                                    dbHelper.deleteAllFavorites(usernameSharedPref);
                                    Toast.makeText(getBaseContext(), R.string.favoriDeleted, Toast.LENGTH_SHORT).show();

                                    // clear and notify the ListAdapter
                                    questionListDataAdapter.clear();
                                    questionListDataAdapter.notifyDataSetChanged();

                                    // show emptyList message
                                    textView.setText(R.string.emptyList);

                                }

                            })
                            .setNegativeButton(R.string.no, null)
                            .show();



                }
            }
        });







    }




    private void setFavoritesList() {
         /* get info from databse */
        cursor = dbHelper.getQuestionInfo();
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


                /*  Here we need to verify if the user has put the question, c, in his favorites */
                if(dbHelper.verifyFavorite(usernameSharedPref, c.getId()) == true){
                    questionListDataAdapter.add(c);
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

                Question item = (Question) questionListDataAdapter.getItem(position);

                Intent i = new Intent(FavoriteList.this, QuestionDisplay.class);
                /* put an Extra in the intent to use Title on the question activity */
                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                i.putExtra("myValueKeyIdQuestion", item.getId());
                i.putExtra("myValueKeyAuthor", item.getUsername());
                i.putExtra("topicSelected", item.getTopic());
                i.putExtra("image", item.getImage());
                i.putExtra("date", item.getDate());
                i.putExtra("activitySelected", "favorite");
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
                Intent goAdd = new Intent(this, QuestionAdd.class);
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






