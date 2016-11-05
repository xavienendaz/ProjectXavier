package com.example.xavier.projectxavier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        // COUNT QUESTIONS
  /*    textView = (TextView) findViewById(R.id.tvQuestionCount);
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
}
