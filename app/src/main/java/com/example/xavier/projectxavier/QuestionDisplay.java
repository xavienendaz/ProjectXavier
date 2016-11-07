package com.example.xavier.projectxavier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class QuestionDisplay extends AppCompatActivity {

    TextView textViewQuestionTitle, textViewQuestionContent;
    String myValueTopicSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);
         /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");


        setTitle(myValueTopicSelected);
        textViewQuestionTitle = (TextView) findViewById(R.id.tvTitle);
        textViewQuestionContent = (TextView) findViewById(R.id.tvQuestionContent);

        /* Recover Object Question from activity_question_list */
        String myValueTitle = getIntent().getExtras().getString("myValueKeyTitle");
        textViewQuestionTitle.setText(myValueTitle);

        String myValueContent = getIntent().getExtras().getString("myValueKeyContent");
        textViewQuestionContent.setText(myValueContent);





    }





    /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_display, menu);
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

            case R.id.action_share:
                //redirect the user on loginActivity when he deleted his account
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                //send the content of the question
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.id.tvQuestionContent);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }







}
