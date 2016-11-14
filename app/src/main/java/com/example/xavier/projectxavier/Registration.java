package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {


    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText etUsername, etPassword, etConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(R.string.registration);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);

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
                etPassword.setError("Enter a password");
                etUsername.setError("Enter a username");
                return;
            } else if (TextUtils.isEmpty(verifyUsername)) {
                etUsername.setError("Enter a username");
                return;
            } else  if (TextUtils.isEmpty(verifyPassword)) {
                etPassword.setError("Enter a password");
                return;
            }
        }else {

            /* verify characters size tu username and password*/
            if(etUsername.length()<3 || verifyUsername.length()>15){
                etUsername.setError("characters: min 3, max 15");
                return;
            }
            if(etPassword.length()<3){
                etPassword.setError("characters: min 3");
                return;
            }


             /*Verify if password and confirm password are equals*/
            if (!verifyPassword.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(),
                        "Password does not match", Toast.LENGTH_SHORT).show();
                return;
            } else {

                 /* verifyRegisterUsername verify if the user already exist in database */
                if(dbHelper.verifyRegisterUsername(verifyUsername) == false){

                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();

                    dbHelper = new DbHelper(context);
                    sqLiteDatabase = dbHelper.getWritableDatabase();
                    dbHelper.addInfo(username, password, sqLiteDatabase);
                    Toast.makeText(getBaseContext(), "User created", Toast.LENGTH_SHORT).show();
                    dbHelper.close();

                    //open the loginActivity when user register
                    Intent i = new Intent(Registration.this, login.class);
                    Registration.this.startActivity(i);
                }else{
                    //if the username is already in database
                    Toast.makeText(getBaseContext(), "Username already exist", Toast.LENGTH_SHORT).show();
                }

            }


        }


    }



}

