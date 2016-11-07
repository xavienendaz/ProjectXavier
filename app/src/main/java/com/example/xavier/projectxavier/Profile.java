package com.example.xavier.projectxavier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("profile");
    }







    /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
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

            case R.id.action_topics:
                Intent goTopics = new Intent(this, TopicsList.class);
                startActivity(goTopics);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}











