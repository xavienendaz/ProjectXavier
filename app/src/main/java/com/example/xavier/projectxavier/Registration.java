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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class Registration extends AppCompatActivity {


    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText etUsername, etPassword, etConfirmPassword;
    SharedPreferences sharedPref;
    byte imageInByte[];
    ImageView defaultUserImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(R.string.registration);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        defaultUserImg = (ImageView) findViewById(R.id.imvProfile);

        //go back to login
        final TextView tv = (TextView) findViewById(R.id.tvAlreadyMember);
        tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //open the registerActivity when user click on registerLink
                Intent i = new Intent(Registration.this, login.class);
                Registration.this.startActivity(i);



            }
        });

    }

    public void registerUser(View view) {
        String verifyPassword = etPassword.getText().toString();
        String verifyUsername = etUsername.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        dbHelper = new DbHelper(context);
        //show error message if the user let one field empty
        if (TextUtils.isEmpty(verifyPassword) || TextUtils.isEmpty(verifyUsername)) {
            if(TextUtils.isEmpty(verifyPassword) && TextUtils.isEmpty(verifyUsername)){
                etPassword.setError(""+R.string.enterPassword);
                etUsername.setError(""+R.string.enterUsername);
                return;
            } else if (TextUtils.isEmpty(verifyUsername)) {
                etUsername.setError(""+R.string.enterUsername);
                return;
            } else  if (TextUtils.isEmpty(verifyPassword)) {
                etPassword.setError(""+R.string.enterPassword);
                return;
            }
        }else {

            /* verify characters size tu username and password*/
            if(etUsername.length()<3){
                etUsername.setError(""+R.string.unameResctriction);
                return;
            }



             /*Verify if password and confirm password are equals*/
            if (!verifyPassword.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(),
                        R.string.passwordWrong, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getBaseContext(), R.string.usernameExist, Toast.LENGTH_SHORT).show();
                }

            }


        }


    }



}

