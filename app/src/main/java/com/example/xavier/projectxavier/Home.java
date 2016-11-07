package com.example.xavier.projectxavier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    TextView textView;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

   /*     // COUNT QUESTIONS
      textView = (TextView) findViewById(R.id.tvQuestionCount);
        int count = dbHelper.getQuestionCount();
       String count2 = ""+dbHelper.getQuestionCount()+"";
        textView.setText(count);
*/

    }

    //redirect the user on different pages
    public void goAddquestion(View view) {
        Intent i = new Intent(Home.this, AddingQuestion.class);
        Home.this.startActivity(i);
    }

    public void goTopics(View view) {
        Intent i = new Intent(Home.this, TopicsList.class);
        Home.this.startActivity(i);
    }

    public void goProfile(View view) {
        Intent i = new Intent(Home.this, Profile.class);
        Home.this.startActivity(i);
    }

    public void goSettings(View view) {
        Intent i = new Intent(Home.this, Settings.class);
        Home.this.startActivity(i);
    }


   /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    /*Actionbar's actions*/
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

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











