package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity{

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView textViewUsername, tvEmptyUserList;
    String usernameSharedPref;
    Cursor cursor;
    ListDataAdapterQuestion listDataAdapterQuestion;
    SharedPreferences sharedPref;
    ListView listView;
    int SELECTED_IMAGE;
    byte imageInByte[];
    ImageView chooseImage;
    ByteArrayInputStream imageStream;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profile);

        /* Read username from sharedPreferences */
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");
        textViewUsername = (TextView) findViewById(R.id.tvUsername);
        textViewUsername.setText(usernameSharedPref);


        chooseImage = (ImageView) findViewById(R.id.imvAddUserPhoto);

        readUserFromDatabase();

        countUserPosts();

        displayUserPostsList();


           /* when the user click for add a photo */
        final ImageView im = (ImageView) findViewById(R.id.imvAddUserPhoto);
        im.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECTED_IMAGE);


            }
        });


    }

    private void readUserFromDatabase() {
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        cursor = dbHelper.getOneUser(usernameSharedPref, sqLiteDatabase);

        if(cursor.moveToFirst())
        {
            do {
                String username;
                byte[] image;
                username = cursor.getString(0);
                image = cursor.getBlob(1);
                User u = new User(username, image);
                u.toString();
            }while (cursor.moveToNext());
        }
        setImage();
    }

    private void setImage() {
        byte[] img = u.getImage();
       // u.getImage();
        imageStream = new ByteArrayInputStream(img);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        chooseImage.setImageBitmap(theImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECTED_IMAGE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    //Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    chooseImage.setImageURI(selectedImageUri);

                     /* convert bitmap to byte */
                    chooseImage.setDrawingCacheEnabled(true);
                    Bitmap image = Bitmap.createBitmap(chooseImage.getDrawingCache());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageInByte = stream.toByteArray();

                    sqLiteDatabase = dbHelper.getReadableDatabase();
                    dbHelper.updateUser(usernameSharedPref, imageInByte, sqLiteDatabase);

                }
            }
        }
    }


    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }












    private void displayUserPostsList() {
        tvEmptyUserList = (TextView) findViewById(R.id.tvEmptyUserCurrentList);


        listView = (ListView) findViewById(R.id.listview_questionList_profile);
        listDataAdapterQuestion = new ListDataAdapterQuestion(getApplicationContext(), R.id.profile_list_layout);
        listView.setAdapter(listDataAdapterQuestion);
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();


        cursor = dbHelper.getAllQuestionsFromCurrentUser(usernameSharedPref, sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                String topic, title, content, username;
                int id;
                byte [] image;

                id = cursor.getInt(0);
                topic = cursor.getString(1);
                title = cursor.getString(2);
                content = cursor.getString(3);
                username = cursor.getString(4);
                image = cursor.getBlob(5);
                Question c = new Question(id, topic, title, content, username, image);
                c.toString();

                listDataAdapterQuestion.add(c);

            } while (cursor.moveToNext());
        }

        /* ListeView handler: Display the selected question */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question item = (Question) listDataAdapterQuestion.getItem(position);

                Intent i = new Intent(Profile.this, QuestionDisplay.class);
                /* put an Extra in the intent to use Title on the question activity */
                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                i.putExtra("myValueKeyIdQuestion", item.getId());
                i.putExtra("myValueKeyAuthor", item.getUsername());
                i.putExtra("topicSelected", item.getTopic());
                i.putExtra("image", item.getImage());
                Profile.this.startActivity(i);
            }
        });
    }


    private void countUserPosts() {
        dbHelper = new DbHelper(context);
        TextView  textViewCount = (TextView) findViewById(R.id.tvNbQuestions);
        int cpt = dbHelper.countUserQuestions(usernameSharedPref);
        textViewCount.setText(""+cpt);
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



