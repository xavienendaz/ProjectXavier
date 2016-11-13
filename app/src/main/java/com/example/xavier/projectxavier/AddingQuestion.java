package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.MediaStore.Images.Thumbnails.IMAGE_ID;

public class AddingQuestion extends AppCompatActivity {

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText etTitle, etContent;
    Spinner spinner;
    int SELECTED_IMAGE;
    ImageView chooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_question);
        setTitle("New question");


        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        chooseImage = (ImageView) findViewById(R.id.imChoose);



        //gimage gallery
        final ImageView im = (ImageView) findViewById(R.id.imChoose);
        im.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECTED_IMAGE);


            }
        });







        // topic spinner part
        spinner = (Spinner) findViewById(R.id.spinnerTopics);
        // Spinner click listener
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.topics_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String topic = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item
                Toast.makeText(getApplicationContext(),
                        "Topic selected: " + topic, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });



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



    //redirect on questionlist and saving data
    public void saveQuestion(View view) {
        String verifyTitle = etTitle.getText().toString();
        String verifyContent = etContent.getText().toString();

        /***************
         * we need to verify if a topic is selected
         *********************/


        //set error if the user let one field empty
        if (TextUtils.isEmpty(verifyTitle) || TextUtils.isEmpty(verifyContent)) {
            if (TextUtils.isEmpty(verifyTitle) && TextUtils.isEmpty(verifyContent)) {
                etTitle.setError("Enter a title");
                etContent.setError("Enter a content");
                return;
            } else if (TextUtils.isEmpty(verifyContent)) {
                etContent.setError("Enter a content");
                return;
            } else if (TextUtils.isEmpty(verifyTitle)) {
                etTitle.setError("Enter a title");
                return;
            }
        } else {



            //insert question into database
            String topic = spinner.getSelectedItem().toString();
            //                String.valueOf(mySpinner.getSelectedItem());
            //String topic = "SWD";
            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();
            String username = "test"; //trouver le moyen de savoir le username depuis login


            dbHelper = new DbHelper(context);
            sqLiteDatabase = dbHelper.getWritableDatabase();



           dbHelper.addQuestion(topic, title, content, username, sqLiteDatabase);
            Toast.makeText(getBaseContext(), "Question created", Toast.LENGTH_LONG).show();
            dbHelper.close();



            /* redirect to questionlist with selected topic */
            Intent i = new Intent(AddingQuestion.this, QuestionList.class);
            i.putExtra("topicSelected", topic);
            AddingQuestion.this.startActivity(i);

        }
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
                Intent goFavorite = new Intent(this, Favorite.class);
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




