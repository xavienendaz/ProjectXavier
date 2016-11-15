package com.example.xavier.projectxavier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class TopicsList extends AppCompatActivity {

    ListView list;
    String [] topics;
    TopicsCustomList adapter;

    Integer[] imgTopicsId = {
            R.drawable.topic_general,
            R.drawable.topic_fruit,
            R.drawable.topic_vegetable,
            R.drawable.topic_protein,
            R.drawable.topic_bred,
            R.drawable.topic_vegan,
            R.drawable.topic_fastfood,
            R.drawable.topic_drink,
            R.drawable.topic_dessert,
            R.drawable.topic_candies
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);
        setTitle(R.string.titleTopics);


        topics = getResources().getStringArray(R.array.topics_array);
       adapter = new TopicsCustomList(TopicsList.this, topics, imgTopicsId);
                list=(ListView)findViewById(R.id.listviewTopics);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view,
                                                                        int position, long id) {
                                                    // Toast.makeText(TopicsList.this, "You Clicked at " +topics[+ position], Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(TopicsList.this, QuestionList.class);
                                                    i.putExtra("topicSelected", adapter.getItem(position));
                                                    TopicsList.this.startActivity(i);
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
                Intent goTopics = new Intent(this, TopicsList.class);
                startActivity(goTopics);
                return true;

            case R.id.action_favorite:
                Intent goFavorite = new Intent(this, FavoriteList.class);
               // goFavorite.putExtra("myValueKeyUsername", myValueKeyUsername);
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






