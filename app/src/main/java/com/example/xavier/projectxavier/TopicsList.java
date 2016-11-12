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
            R.drawable.topic_protein,
            R.drawable.topic_bred,
            R.drawable.topic_drink,
            R.drawable.topic_bred,
            R.drawable.topic_drink,
            R.drawable.topic_dessert

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);
        setTitle("Topics");
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
        getMenuInflater().inflate(R.menu.topics_list, menu);
        return true;
    }

    /*Actionbar's actions*/
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                Intent goHome = new Intent(this, Home.class);
                startActivity(goHome);
                return true;

            case R.id.action_settings:
                Intent goSettings = new Intent(this, Settings.class);
                startActivity(goSettings);
                return true;

            case R.id.action_profile:
                Intent goProfile = new Intent(this, Profile.class);
                startActivity(goProfile);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}






