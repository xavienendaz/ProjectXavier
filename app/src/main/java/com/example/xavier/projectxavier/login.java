package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Create variables
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView registerLink = (TextView) findViewById(R.id.tvRegister);

        registerLink.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //open the registerActivity when user click on registerLink
                Intent registerIntent = new Intent(login.this, Registration.class);
                login.this.startActivity(registerIntent);

            }
        });

        //see the database
        final TextView tv = (TextView) findViewById(R.id.tvUserArea);
        tv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //open the registerActivity when user click on registerLink
                Intent i = new Intent(login.this, UserAreaActivity.class);
                login.this.startActivity(i);
            }
        });


        //see homepage
        final TextView t = (TextView) findViewById(R.id.tvHome);
        t.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //open the registerActivity when user click on registerLink
                Intent i = new Intent(login.this, Home.class);
                login.this.startActivity(i);
            }
        });




    }

    /* When the user click on Login button */
    public void buttonLogin(View view) {

        String verifyPassword = etPassword.getText().toString();
        String verifyUsername = etUsername.getText().toString();
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        /* the method verifyUserLogin verify if the user write username and password correctly */
        if(dbHelper.verifyUserLogin(verifyUsername, verifyPassword) == true){
            Intent i = new Intent(login.this, Home.class);
            login.this.startActivity(i);
            Toast.makeText(login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getBaseContext(), "User Name or Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}
