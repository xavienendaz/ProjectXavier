package com.example.xavier.projectxavier;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {


    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    SharedPreferences sharedPref;
    String usernameSharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.action_settings);



    }

    public void deleteUser(View view) {

        /* Read username from sharedPreferences */
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");

        /* When the user click on delete_account, a confirmation alert is created */
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
                        dbHelper.deleteUser(usernameSharedPref, sqLiteDatabase);

                        /* clear sharedpreference values */
                        sharedPref.edit().remove("username");
                        sharedPref.edit().remove("password");
                        sharedPref.edit().clear();

                        /******* ICI supprimer contenu du shared preference *****/
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



