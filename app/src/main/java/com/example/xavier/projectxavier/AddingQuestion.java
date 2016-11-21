package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddingQuestion extends AppCompatActivity {

    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText etTitle, etContent;
    Spinner spinner;
    int SELECTED_IMAGE;
    ImageView chooseImage;
    byte imageInByte[];
    TextView tvImgError, tvImgSelected;
    int cpt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_question);
        setTitle(R.string.newQuestion);


        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        chooseImage = (ImageView) findViewById(R.id.imChoose);
        tvImgError  = (TextView) findViewById(R.id.tvImgError);



        /* when the user click for add a photo */
        final ImageView im = (ImageView) findViewById(R.id.imChoose);
        im.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECTED_IMAGE);


            }
        });



        spinnerTopics();

    }

    private void spinnerTopics() {
        // topic spinner part
        spinner = (Spinner) findViewById(R.id.spinnerTopics);
        // Spinner click listener
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.topics_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        spinner.setSelection(setSelectedTopic(),true);

       // spinner.setSelection(); //set topic selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                String topic = adapter.getItemAtPosition(position).toString();
                // Showing selected spinner item
              //  Toast.makeText(getApplicationContext(),R.string.topicChoose + topic, Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private int setSelectedTopic() {
        int val = -1;

//       String myValueTopicSelected = getIntent().getExtras().getString("topicSelected");

        int tab = R.array.topics_array;

        //if(myValueTopicSelected == )

        return val;
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
                    tvImgError.setTextColor(getResources().getColor(R.color.greenLight));
                    tvImgError.setText(R.string.imgSelected);
                    /* in method below saveQuestion that the user has select the image*/
                    cpt = 1;
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

    public void verifySelectedImaage(){

        if(chooseImage.isSelected()==false){
            tvImgError.setText(R.string.imgErro);
            Toast.makeText(getBaseContext(), R.string.imgErro, Toast.LENGTH_SHORT).show();
            return;
            }else{
             /* If user select an image one time, cpt = 1 for verify in saveQuestion method*/
            cpt=1;
            }
    }

    /* redirect to questionList and save question */
    public void saveQuestion(View view) {

        String verifyTitle = etTitle.getText().toString();
        String verifyContent = etContent.getText().toString();

          /* Say to user that he needs to select an image
          *  If cpt = 1, he has already selected an image*/
        if(cpt<1){
            verifySelectedImaage();
            return;
        }


        /* display an error if the user let one field empty */
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


            if(etTitle.length()<5){
                etTitle.setError("characters: min 5");
                return;
            }
            if(etContent.length()<50){
                etContent.setError("characters: min 50");
                return;
            }



            //insert question into database
            String topic = spinner.getSelectedItem().toString();
            //                String.valueOf(mySpinner.getSelectedItem());
            //String topic = "SWD";
            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();


              /* Read username from sharedPreferences */
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String usernameSharedPref = sharedPref.getString("username", "");


            dbHelper = new DbHelper(context);


            /* convert bitmap to byte */
            chooseImage.setDrawingCacheEnabled(true);
            Bitmap image = Bitmap.createBitmap(chooseImage.getDrawingCache());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            imageInByte = stream.toByteArray();

            SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
            String currentTime = time.format(new Date());
            dbHelper.addQuestion(topic, title, content, usernameSharedPref, imageInByte, currentTime);


            Toast.makeText(getBaseContext(), R.string.questionCreated, Toast.LENGTH_SHORT).show();

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





