package com.example.xavier.projectxavier;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class Settings extends AppCompatActivity {


    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    String currentUsername = "salut";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");









    }

    public void deleteUser(View view) {
        //When the user click on delete_account, a confirmation alert is created
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete account")
                .setMessage("Are you sure you want delete your account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new DbHelper(getApplicationContext());
                        sqLiteDatabase = dbHelper.getReadableDatabase();
                        dbHelper.deleteUser(currentUsername, sqLiteDatabase);

                        //redirect the user on loginActivity when he deleted his account
                        Intent i = new Intent(Settings.this, login.class);
                        Settings.this.startActivity(i);

                        Toast.makeText(getBaseContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

/*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    /*Actionbar's actions*/
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                Intent goHome = new Intent(this, Home.class);
                startActivity(goHome);
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



