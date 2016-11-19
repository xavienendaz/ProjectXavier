package com.example.xavier.projectxavier;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class QuestionDisplay extends AppCompatActivity {

    TextView textViewQuestionTitle, textViewQuestionContent, textViewAuthor;
    String myValueTopicSelected, myValueKeyAuthor;

    Cursor cursor;
    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton fab;
    int myValueKeyIdQuestion;
    Intent goBack;
    String usernameSharedPref;
    SharedPreferences sharedPref;
    ByteArrayInputStream imageStream;
    ImageView  imageView, imageAuthor;
    CollapsingToolbarLayout collapsingToolbarLayout;
    EditText etAddComment;
    ListView listViewComments;
    CommentAdapter commentAdapter;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);
         /* Recover Object Question from activity_question_list */
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(myValueTopicSelected);


        imageAuthor = (ImageView) findViewById(R.id.imgAuthor);
        etAddComment = (EditText) findViewById(R.id.etAddComment);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Find logo
        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activitySelected = getIntent().getExtras().getString("activitySelected");
                Intent goBack = new Intent(QuestionDisplay.this, QuestionList.class);
                goBack.putExtra("topicSelected", myValueTopicSelected);
                QuestionDisplay.this.startActivity(goBack);
      /*         if(activitySelected == "questionList")
               {
                   Intent goBack = new Intent(QuestionDisplay.this, QuestionList.class);
                   goBack.putExtra("topicSelected", myValueTopicSelected);
                   QuestionDisplay.this.startActivity(goBack);

               }else if(activitySelected.toString() == "favorite")
               {
                   /* when the user open this activity from favorites
                   Intent goBack = new Intent(QuestionDisplay.this, FavoriteList.class);
                   QuestionDisplay.this.startActivity(goBack);
               }else if(activitySelected == "profileList")
     /*          {
                   /* when the user open this activity from profile
                   Intent goBack = new Intent(QuestionDisplay.this, Profile.class);
                   QuestionDisplay.this.startActivity(goBack);
               }
*/

            }
        });


        textViewQuestionTitle = (TextView) findViewById(R.id.tvTitle);
        textViewQuestionContent = (TextView) findViewById(R.id.tvQuestionContent);
        textViewAuthor = (TextView) findViewById(R.id.tvAuthor);

        /* Recover Object Question from activity_question_list */
        String myValueTitle = getIntent().getExtras().getString("myValueKeyTitle");
        textViewQuestionTitle.setText(myValueTitle);

        String myValueContent = getIntent().getExtras().getString("myValueKeyContent");
        textViewQuestionContent.setText(myValueContent);

        myValueKeyAuthor = getIntent().getExtras().getString("myValueKeyAuthor");
        textViewAuthor.setText(myValueKeyAuthor);

        myValueKeyIdQuestion = getIntent().getExtras().getInt("myValueKeyIdQuestion");
        fab = (FloatingActionButton) findViewById(R.id.fabFavorite);
        dbHelper = new DbHelper(context);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");


        setFabImage();

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (dbHelper.verifyFavorite(usernameSharedPref, myValueKeyIdQuestion) == true) {

                    /* If the question is in favorites, we delete the row from table */
                    dbHelper.deleteFavorite(usernameSharedPref, myValueKeyIdQuestion, sqLiteDatabase);
                    Toast.makeText(getBaseContext(), "Favorite delete", Toast.LENGTH_SHORT).show();

                } else {
                    /* If the question is not in favorites */
                    sqLiteDatabase = dbHelper.getWritableDatabase();
                    dbHelper.addFavorite(usernameSharedPref, myValueKeyIdQuestion, sqLiteDatabase);
                    Toast.makeText(getBaseContext(), "Favorite added", Toast.LENGTH_SHORT).show();
                    dbHelper.close();
                }
                setFabImage();

            }
        });


        countUserPosts();
        setImage();
        readUserFromDatabase();
        //see the database
        final ImageView share = (ImageView) findViewById(R.id.imvShare);
        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                //send the content of the question

                /********** PPROBLEM HERE NOTHING SHARE***********/
                sendIntent.putExtra(Intent.EXTRA_TEXT, R.id.tvQuestionContent);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


        /* Clear the EditText when user click on clear */
        final Button clearComment = (Button) findViewById(R.id.btCancelComment);
        clearComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etAddComment.getText().clear();
                etAddComment.setHint(R.string.addComment);
            }
        });

        sqLiteDatabase = dbHelper.getWritableDatabase();


        /* Save a comment when user click on save */
        final Button saveComment = (Button) findViewById(R.id.btSaveComment);
        saveComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String comment = etAddComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(getBaseContext(), R.string.enterComment, Toast.LENGTH_SHORT).show();

                    /* add on click keybord diesappear*/
                }else{
                    SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
                    String currentTime = time.format(new Date());

                    String content = etAddComment.getText().toString();
                    String author = usernameSharedPref.toString();
                    /* Add the comment in database */
                    dbHelper.addComment(content, currentTime, author, myValueKeyIdQuestion, sqLiteDatabase);
                    Toast.makeText(getBaseContext(), R.string.commentAdded, Toast.LENGTH_SHORT).show();

                    /* Clear and set hint in EditText addComment */
                    etAddComment.getText().clear();
                    etAddComment.setHint(R.string.addComment);

                    /* Load the updated list */
                    setCommentList();
                }



            }
        });




  /*      ArrayList image_details = getListData();
        final ListView lv1 = (ListView) findViewById(R.id.custom_list);
        lv1.setFocusable(false);
        lv1.setAdapter(new CommentListAdapter(this, image_details));
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                Comment newsData = (Comment) o;
                //Toast.makeText(QuestionDisplay.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
            }
        });


        setListViewHeight(lv1);
        */

        setCommentList();



    }

    private void setCommentList() {

        listViewComments = (ListView) findViewById(R.id.listview_comments);
        commentAdapter = new CommentAdapter(getApplicationContext(), R.id.comment_list_layout);
        listViewComments.setAdapter(commentAdapter);
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();


        String id_question = String.valueOf(myValueKeyIdQuestion);
        cursor = dbHelper.getAllCommentsFromCurrentQuestion(id_question, sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                String date, content, username;
                int id;

                id = cursor.getInt(0);
                content = cursor.getString(1);
                date = cursor.getString(2);
                username = cursor.getString(3);
                myValueKeyIdQuestion = cursor.getInt(4);

                Comment c = new Comment(id, content, date, username, myValueKeyIdQuestion);
                c.toString();

                commentAdapter.add(c);

            } while (cursor.moveToNext());
        }


        /* Count nb comments */
        TextView t  = (TextView) findViewById(R.id.tvCommentsCpt);
        t.setText("("+commentAdapter.getCount()+")");

        /* Set list Height */
        setListViewHeight(listViewComments);
    }

    public static boolean setListViewHeight(ListView listView) {
        int position =0;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            /* height of items */
            int totalItemsHeight = 0;
            for ( position = 0; position < numberOfItems;position++) {
                View item = listAdapter.getView(position, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight ;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }


    private ArrayList getListData() {
        ArrayList<Comment> results = new ArrayList<Comment>();
        Comment newsData = new Comment();
        newsData.setContent("Dance of Democracy");
        newsData.setUsername("Pankaj Gupta");
        newsData.setDate("May 26, 2013, 13:35");
        results.add(newsData);
        results.add(newsData);
        results.add(newsData);
        results.add(newsData);
        results.add(newsData);
        results.add(newsData);
        results.add(newsData);
        results.add(newsData);


        String id_question = String.valueOf(myValueKeyIdQuestion);
        //String tmpStr10 = String.valueof(tmpInt);
        dbHelper.getAllCommentsFromCurrentQuestion(id_question, sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                String date, content, username;
                int id;

                id = cursor.getInt(0);
                content = cursor.getString(1);
                username = cursor.getString(2);
                date = cursor.getString(3);
                myValueKeyIdQuestion = cursor.getInt(4);

                Comment c = new Comment(id, content, username, date, myValueKeyIdQuestion);
                c.toString();

                results.add(c);

            } while (cursor.moveToNext());
        }



        // Add some more dummy data for testing
        return results;
    }




    private void readUserFromDatabase() {
        dbHelper = new DbHelper(context);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        /* read the author user from database */
        cursor = dbHelper.getOneUser(myValueKeyAuthor, sqLiteDatabase);

        if(cursor.moveToFirst())
        {
            do {
                String username;
                byte[] image;
                username = cursor.getString(0);
                image = cursor.getBlob(1);
                User u = new User(username, image);

                /* set profile image */
                byte[] data = u.getImage();
                imageStream = new ByteArrayInputStream(data);
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                imageAuthor.setImageBitmap(theImage);
            }while (cursor.moveToNext());
        }
    }



    private void setImage() {
        byte[] img = getIntent().getExtras().getByteArray("image");
        imageView = (ImageView) findViewById(R.id.iv_toolbar_detail);
        imageStream = new ByteArrayInputStream(img);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        imageView.setImageBitmap(theImage);
    }


    private void countUserPosts() {
        dbHelper = new DbHelper(context);
        TextView  textViewNbPost = (TextView) findViewById(R.id.nbPosts);
        int cpt = dbHelper.countUserQuestions(myValueKeyAuthor);
        textViewNbPost.setText(""+cpt);
    }

    public void setFabImage(){
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();
              /* verify database if the current has this question on his favorites */
        if(dbHelper.verifyFavorite(usernameSharedPref, myValueKeyIdQuestion) == true){

            /* in favorite*/
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
        }
        else{
            /* not in favorite*/
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
        }

    }






}

