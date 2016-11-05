package com.example.xavier.projectxavier;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class UserAreaActivity extends AppCompatActivity {

    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    DbHelper dbHelper;
    Cursor cursor;
    ListDataAdapterUser listDataAdapterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        setTitle("Database");
        listView = (ListView)findViewById(R.id.listview_userarea);
        listDataAdapterUser = new ListDataAdapterUser(getApplicationContext(),R.id.row_layout);
        listView.setAdapter(listDataAdapterUser);
        dbHelper =  new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
        //get info from databse
        cursor =  dbHelper.getInfo(sqLiteDatabase);
        if(cursor.moveToFirst())
        {
            do{
                String username,password;
                username =  cursor.getString(0);
                password = cursor.getString(1);

                User user = new User(username, password);
                listDataAdapterUser.add(user);

            }while(cursor.moveToNext());
        }


    }

}


