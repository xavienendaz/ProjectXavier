package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText etUsername, etPassword;
    CheckBox cbRememberLogin;
    SharedPreferences sharedPref;
    String SharedPrefUsername, SharedPrefPassword;
    Button bLogin;
    TextView tvNewAccount;
    LanguageLocalHelper languageLocalHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        languageLocalHelper.onCreate(this);

        //Create variables
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvNewAccount = (TextView) findViewById(R.id.tvRegister);



        cbRememberLogin = (CheckBox) findViewById(R.id.cbRememberLogin);



        verifyUserRememberLoginInSharePreference();


        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);
        registerLink.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //open the registerActivity when user click on registerLink
                Intent registerIntent = new Intent(login.this, Registration.class);
                login.this.startActivity(registerIntent);

            }
        });


    }


    private void updateTexts() {
        etUsername.setHint(R.string.username);
        etPassword.setHint(R.string.Password);
        cbRememberLogin.setText(R.string.rememberLogin);
        bLogin.setText(R.string.login);
        tvNewAccount.setText(R.string.createAccount);
        setTitle(R.string.registration);
    }




    private void verifyUserRememberLoginInSharePreference() {
         /* Read username and password from sharedPreferences */
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPrefUsername = sharedPref.getString("username", "");
        SharedPrefPassword = sharedPref.getString("password", "");

        /* If the user had selected remember login */
        if(SharedPrefPassword != "" ){
            cbRememberLogin.setChecked(true);
            etUsername.setText(SharedPrefUsername);
            etPassword.setText(SharedPrefPassword);

        }




    }



    /* When the user click on Login button */
    public void buttonLogin(View view) {

        String verifyPassword = etPassword.getText().toString();
        String verifyUsername = etUsername.getText().toString();
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        /* the method verifyUserLogin verify if the user write username and password correctly */
        if(dbHelper.verifyUserLogin(verifyUsername, verifyPassword) == true){


            /* Write username in sharedPreferences */
            sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();


            if(cbRememberLogin.isChecked()){
                editor.putString("username", verifyUsername);
                editor.putString("password", verifyPassword);
            }else{
                editor.putString("username", verifyUsername);
                /* set password for unchecked the checkbox and delete the values when the user reconnect */
                editor.putString("password","");
            }


            editor.commit();







            Intent i = new Intent(login.this, TopicsList.class);
            login.this.startActivity(i);
            Toast.makeText(login.this, R.string.loginSuccess, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getBaseContext(), "User Name or Password does not match", Toast.LENGTH_SHORT).show();
        }
    }

  /*Addid the actionbar*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_choose_language, menu);
        return true;
    }

    /*Actionbar's actions*/
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.language_english:
                languageLocalHelper.setLocale(login.this, "en");
                updateTexts();
                return true;

            case R.id.language_french:
                languageLocalHelper.setLocale(login.this, "fr");
                updateTexts();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }


}




