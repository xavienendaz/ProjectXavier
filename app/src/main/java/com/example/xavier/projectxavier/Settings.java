package com.example.xavier.projectxavier;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavier.myapplication.backend.userApi.model.*;
import com.example.xavier.myapplication.backend.userApi.model.User;
import com.google.android.gms.common.api.GoogleApiClient;

public class Settings extends AppCompatActivity {

    LanguageLocalHelper languageLocalHelper;
    DbHelper dbHelper;
    SharedPreferences sharedPref;
    String usernameSharedPref;
    TextView deleteAccount, logOff, tvAccount, tvLanguages, tvEnglish, tvFrench;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.Settings);

        deleteAccount = (TextView) findViewById(R.id.tvDeleteAccount);
        logOff = (TextView) findViewById(R.id.tvLogOff);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvLanguages = (TextView) findViewById(R.id.tvLanguages);
        tvEnglish = (TextView) findViewById(R.id.tvEnglishInfo);
        tvFrench = (TextView) findViewById(R.id.tvFrenchInfo);

        final LinearLayout flagEnglish = (LinearLayout) findViewById(R.id.layoutEnglish);
        flagEnglish.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // change app language in LanguageLocalHelper and save data in SharePreferences
                languageLocalHelper.setLocale(Settings.this, "en");
                updateTexts();
                Toast.makeText(getBaseContext(), R.string.english, Toast.LENGTH_SHORT).show();
            }
        });


        final LinearLayout flagFrance = (LinearLayout) findViewById(R.id.layoutFrench);
        flagFrance.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // change app language in LanguageLocalHelper and save data in SharePreferences
                languageLocalHelper.setLocale(Settings.this, "fr");
                updateTexts();
                Toast.makeText(getBaseContext(), R.string.french, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateTexts() {
        deleteAccount.setText(R.string.deletAccount);
        logOff.setText(R.string.loggOff);
        tvAccount.setText(R.string.Account);
        tvLanguages.setText(R.string.Languages);
        tvEnglish.setText(R.string.English);
        tvFrench.setText(R.string.French);
        setTitle(R.string.Settings);
    }


    public void logOff(View view) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
         /* remove sharedpreference values */
        sharedPref.edit().remove("username").commit();
        sharedPref.edit().remove("password").commit();
        Intent i = new Intent(Settings.this, Login.class);
        Settings.this.startActivity(i);
        Toast.makeText(getBaseContext(), R.string.Loggof, Toast.LENGTH_SHORT).show();
    }


    public void deleteUser(View view) {

        /* Read username from sharedPreferences */
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");

        /* When the user click on delete_account, a confirmation alert is created */
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.deletAccount)
                .setMessage(R.string.deleteAccountConfirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper = new DbHelper(getApplicationContext());


                        /***** CLOUD *****/
                        com.example.xavier.projectxavier.User usr = null;
                       Cursor c1 = dbHelper.getOneUserSettings(usernameSharedPref);
                        if (c1.moveToFirst()) {
                            do {
                                int id;
                                String uname;

                                id = c1.getInt(0);
                                uname = c1.getString(0);
                                usr = new com.example.xavier.projectxavier.User(id, uname);
                            } while (c1.moveToNext());
                        }

                        User userCloud = new User();

                        Long idBackend = Long.valueOf(usr.getId());
                        userCloud.setId(idBackend);
                        userCloud.setUsername(usernameSharedPref);
                        // update password. I dont want to delete account
                        userCloud.setPassword("AccountDeleted");

                        new EndpointsAsyncTaskUserUpdate(userCloud).execute();

                        /***** CLOUD *****/


                        dbHelper.deleteUser(usernameSharedPref);

                        /* remove sharedpreference values */
                        sharedPref.edit().remove("username").commit();
                        sharedPref.edit().remove("password").commit();

                        Intent i = new Intent(Settings.this, Login.class);
                        Settings.this.startActivity(i);

                        Toast.makeText(getBaseContext(), R.string.accountDelete, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

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

