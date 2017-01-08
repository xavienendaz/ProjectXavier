package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavier.myapplication.backend.userApi.model.*;
import com.example.xavier.myapplication.backend.userApi.model.User;

import java.io.ByteArrayOutputStream;

public class Registration extends AppCompatActivity {

    LanguageLocalHelper languageLocalHelper;
    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText etUsername, etPassword, etConfirmPassword;
    SharedPreferences sharedPref;
    byte imageInByte[];
    ImageView defaultUserImg;
    TextView tvLogin;
    Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(R.string.registration);

        // with this line the keyboard doesn't open automatically
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        defaultUserImg = (ImageView) findViewById(R.id.imvProfile);
        tvLogin = (TextView) findViewById(R.id.tvAlreadyMember);
        btRegister = (Button) findViewById(R.id.bRegister);

        //go back to Login
        final TextView tv = (TextView) findViewById(R.id.tvAlreadyMember);
        tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //open the registerActivity when user click on registerLink
                Intent i = new Intent(Registration.this, Login.class);
                Registration.this.startActivity(i);
            }
        });

    }


       private void updateTexts() {
           etUsername.setHint(R.string.username);
           etPassword.setHint(R.string.Password);
           etConfirmPassword.setHint(R.string.confirmPassword);
           btRegister.setText(R.string.register);
           tvLogin.setText(R.string.login);
           setTitle(R.string.registration);
    }


    public void registerUser(View view) {
        String verifyPassword = etPassword.getText().toString();
        String verifyUsername = etUsername.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        dbHelper = new DbHelper(context);
        //show error message if the user let one field empty
        if (TextUtils.isEmpty(verifyPassword) || TextUtils.isEmpty(verifyUsername)) {
            if(TextUtils.isEmpty(verifyPassword) && TextUtils.isEmpty(verifyUsername)){
                etPassword.setError(context.getResources().getString(R.string.enterPassword));
                etUsername.setError(context.getResources().getString(R.string.enterUsername));
                return;
            } else if (TextUtils.isEmpty(verifyUsername)) {
                etUsername.setError(context.getResources().getString(R.string.enterUsername));
                return;
            } else  if (TextUtils.isEmpty(verifyPassword)) {
                etPassword.setError(context.getResources().getString(R.string.enterPassword));
                return;
            }
        }else {

            /* verify characters size tu username and password*/
            if(etUsername.length()<3){
                etUsername.setError(context.getResources().getString(R.string.unameResctriction));
                return;
            }

             /*Verify if password and confirm password are equals*/
            if (!verifyPassword.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(),
                        context.getResources().getString(R.string.passwordWrong), Toast.LENGTH_SHORT).show();
                return;
            } else {

                 /* verifyRegisterUsername verify if the user already exist in database */
                if(dbHelper.verifyRegisterUsername(verifyUsername) == false){

                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();

                    dbHelper = new DbHelper(context);
                    sqLiteDatabase = dbHelper.getWritableDatabase();

                     /* convert bitmap to byte */
                    defaultUserImg.setDrawingCacheEnabled(true);
                    Bitmap image = Bitmap.createBitmap(defaultUserImg.getDrawingCache());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageInByte = stream.toByteArray();

                    dbHelper.addUser(username, password, imageInByte);
                    Toast.makeText(getBaseContext(), R.string.thanksRegistration, Toast.LENGTH_SHORT).show();

                    /***** CLOUD *****/

                    com.example.xavier.myapplication.backend.userApi.model.User uBackend = new User();
                    uBackend.setUsername(username);
                    uBackend.setPassword(password);

                    new EndpoitsAsyncTaskUser(uBackend).execute();


                    /***** CLOUD *****/




                    dbHelper.close();

                    /* remove password and add username in sharedpreference */
                    sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();

                 /* delete password because when the user will restart the app, edittext password must be empty */
                    if(sharedPref.contains("password"))
                        sharedPref.edit().remove("password").commit();

                    editor.putString("username", username);
                    editor.commit();

                    Intent i = new Intent(Registration.this, TopicsList.class);
                    Registration.this.startActivity(i);
                }else{
                    //if the username is already in database
                    Toast.makeText(getBaseContext(), context.getResources().getString(R.string.usernameExist), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_choose_language, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.language_english:
                languageLocalHelper.setLocale(Registration.this, "en");
                updateTexts();
                Toast.makeText(getBaseContext(), R.string.english, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.language_french:
                languageLocalHelper.setLocale(Registration.this, "fr");
                updateTexts();
                Toast.makeText(getBaseContext(), R.string.french, Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




