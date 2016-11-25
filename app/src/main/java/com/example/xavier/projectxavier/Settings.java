package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.renderscript.Sampler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    LanguageLocalHelper languageLocalHelper;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    SharedPreferences sharedPref;
    String usernameSharedPref;
    TextView deleteAccount, logOff, tvAccount, tvLanguages;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */




    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.Settings);
        languageLocalHelper.onCreate(this);

        deleteAccount = (TextView) findViewById(R.id.tvDeleteAccount);
        logOff = (TextView) findViewById(R.id.tvLogOff);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvLanguages = (TextView) findViewById(R.id.tvLanguages);



        final ImageButton flagEnglish = (ImageButton) findViewById(R.id.imbFlagEnglish);
        flagEnglish.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                languageLocalHelper.setLocale(Settings.this, "en");
                updateTexts();


            }
        });

        final ImageButton flagFrance = (ImageButton) findViewById(R.id.imbFlagFrance);
        flagFrance.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //open the registerActivity when user click on registerLink
               // PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "fr").commit();
                // setLangRecreate("fr");

                languageLocalHelper.setLocale(Settings.this, "fr");
                updateTexts();


            }
        });
    }


    private void updateTexts() {

        deleteAccount.setText(R.string.deletAccount);
        logOff.setText(R.string.loggOff);
        tvAccount.setText(R.string.Account);
        tvLanguages.setText(R.string.Languages);
        setTitle(R.string.Settings);



    }



    public void logOff(View view) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
         /* remove sharedpreference values */
        sharedPref.edit().remove("username").commit();
        sharedPref.edit().remove("password").commit();
        Intent i = new Intent(Settings.this, login.class);
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

                        dbHelper.deleteUser(usernameSharedPref);

                        /* remove sharedpreference values */
                        sharedPref.edit().remove("username").commit();
                        sharedPref.edit().remove("password").commit();

                        Intent i = new Intent(Settings.this, login.class);
                        Settings.this.startActivity(i);

                        Toast.makeText(getBaseContext(), R.string.accountDelete, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }


}



