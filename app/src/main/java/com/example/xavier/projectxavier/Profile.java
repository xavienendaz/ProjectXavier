package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Profile extends AppCompatActivity{


    Context context = this;
    DbHelper dbHelper;
    TextView textViewUsername, tvEmptyUserList;
    String usernameSharedPref;
    Cursor cursor;
    QuestionListDataAdapter questionListDataAdapter;
    SharedPreferences sharedPref;
    ListView listViewProfileQuestions;
    int SELECTED_IMAGE;
    byte imageInByte[];
    ImageView chooseImage;
    ByteArrayInputStream imageStream;
    Question q;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle(R.string.profile);

        chooseImage = (ImageView) findViewById(R.id.imvAddUserPhoto);
        listViewProfileQuestions = (ListView) findViewById(R.id.listview_questionList_profile);

        // Read username from sharedPreferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");

        textViewUsername = (TextView) findViewById(R.id.tvUsername);
        textViewUsername.setText(usernameSharedPref);

        // user can change his photo on click
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearChangeUserPhoto);
        linearLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, SELECTED_IMAGE);
            }
        });



        readUserFromDatabase();
        countUserQuestions();
        displayUserPostsList();
        setListViewHeight(listViewProfileQuestions);

    }


    // when the user click for changing his photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECTED_IMAGE) {
                // get url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    String path = getPathFromURI(selectedImageUri);
                    // Set the image in ImageView
                    chooseImage.setImageURI(selectedImageUri);
                    // convert bitmap to byte
                    chooseImage.setDrawingCacheEnabled(true);
                    Bitmap image = Bitmap.createBitmap(chooseImage.getDrawingCache());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    imageInByte = stream.toByteArray();

                    dbHelper.updateUser(usernameSharedPref, imageInByte);
                    Toast.makeText(getBaseContext(), R.string.photochange, Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    // path from the URI
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


    // set the list height automatically
    public static boolean setListViewHeight(ListView listView) {
        int position =0;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // height of items
            int totalItemsHeight = 0;
            for ( position = 0; position < numberOfItems;position++) {
                View item = listAdapter.getView(position, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;
        } else {
            return false;
        }
    }



    private void readUserFromDatabase() {
        dbHelper = new DbHelper(context);
        cursor = dbHelper.getOneUser(usernameSharedPref);

        if(cursor.moveToFirst())
        {
            do {
                String username;
                byte[] image;
                username = cursor.getString(0);
                image = cursor.getBlob(1);
                User u = new User(username, image);

                // set profile image
                byte[] data = u.getImage();
                imageStream = new ByteArrayInputStream(data);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                chooseImage.setImageBitmap(theImage);
            }while (cursor.moveToNext());
        }
    }


    private void displayUserPostsList() {
        tvEmptyUserList = (TextView) findViewById(R.id.tvEmptyUserCurrentList);
        listViewProfileQuestions = (ListView) findViewById(R.id.listview_questionList_profile);
        questionListDataAdapter = new QuestionListDataAdapter(getApplicationContext(), R.id.profile_list_layout);
        listViewProfileQuestions.setAdapter(questionListDataAdapter);
        dbHelper = new DbHelper(getApplicationContext());

        cursor = dbHelper.getAllQuestionsFromCurrentUser(usernameSharedPref);
        if (cursor.moveToLast()) {
            do {
                int id;
                String topic, title, content, username, nbLike, date;
                byte [] image;
                id = cursor.getInt(0);
                topic = cursor.getString(1);
                title = cursor.getString(2);
                content = cursor.getString(3);
                username = cursor.getString(4);
                image = cursor.getBlob(5);
                date = cursor.getString(6);
                nbLike = String.valueOf(dbHelper.countPositiveVote(id));

                Question c = new Question(id, topic, title, content, username, image, nbLike, date);
                questionListDataAdapter.add(c);

            } while (cursor.moveToPrevious());
        }

        /* ListeView handler: Display the selected question */
        listViewProfileQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Question item = (Question) questionListDataAdapter.getItem(position);

                Intent i = new Intent(Profile.this, QuestionDisplay.class);
                //put an Extra in the intent to use Title on the question activity
                i.putExtra("myValueKeyTitle", item.getTitle());
                i.putExtra("myValueKeyContent", item.getContent());
                i.putExtra("myValueKeyIdQuestion", item.getId());
                i.putExtra("myValueKeyAuthor", item.getUsername());
                i.putExtra("topicSelected", item.getTopic());
                i.putExtra("image", item.getImage());
                i.putExtra("date", item.getDate());
                i.putExtra("activitySelected", "profile");
                Profile.this.startActivity(i);
            }
        });

        listViewOnLongClickListener();

    }


    // when user click long on a questions, several information are show in a dialog
    public void listViewOnLongClickListener(){
        listViewProfileQuestions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                q = (Question) questionListDataAdapter.getItem(position);

                // count nb comment for selected question
                int nbComment = dbHelper.countQuestionComments(q.getId());

                // count nb question for selected question author
                int nbUserQuestion = dbHelper.countUserQuestions(q.getUsername());

                // instantiate an AlertDialog
                builder = new AlertDialog.Builder(Profile.this);

                // set dialog message
                builder.setTitle(q.getTopic());
                builder.setMessage(
                                context.getResources().getString(R.string.commentInfo)+" "+nbComment+
                                "\n"+
                                "\n"+
                                context.getResources().getString(R.string.author) +"  "+q.getUsername()+
                                "     "+
                                context.getResources().getString(R.string.nb_posts) +"  "+nbUserQuestion)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true; // true because I dont want to be redirected on the activity Quetiondisplay
            }
        });

    }


    private void countUserQuestions() {
        dbHelper = new DbHelper(context);

        // count User questions
        TextView  tvCptQuestions = (TextView) findViewById(R.id.tvNbQuestions);
        int cptQ = dbHelper.countUserQuestions(usernameSharedPref);
        tvCptQuestions.setText(""+cptQ);

        // count User comments
        TextView  tvCptComments = (TextView) findViewById(R.id.tvNbComments);
        int cptC = dbHelper.countUserComments(usernameSharedPref);
        tvCptComments.setText(""+cptC);

        // count User favorites
        TextView  tvCptFavorites = (TextView) findViewById(R.id.tvNbFavorites);
        int cptF = dbHelper.countUserFavorites(usernameSharedPref);
        tvCptFavorites.setText(""+cptF);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


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
                Intent goAdd = new Intent(this, QuestionAdd.class);
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



