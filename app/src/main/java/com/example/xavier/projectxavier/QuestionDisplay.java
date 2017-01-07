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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavier.myapplication.backend.myApi.model.MyBean;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionDisplay extends AppCompatActivity {

    TextView textViewQuestionTitle, textViewQuestionContent, textViewAuthor, cptPositiveVote, cptNegativeVote, questionDate;
    String myValueTopicSelected, myValueKeyAuthor;
    User u;
    byte[] imgCommentAuthor;
    Cursor cursor, cursorUser;
    Context context = this;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton fab;
    int myValueKeyIdQuestion;
    String usernameSharedPref,  myValueContent, myValueTitle, date, activityFrom;
    SharedPreferences sharedPref;
    ByteArrayInputStream imageStream;
    ImageView  imageView, imageAuthor;
    CollapsingToolbarLayout collapsingToolbarLayout;
    EditText etAddComment;
    ListView listViewComments;
    CommentAdapter commentAdapter;
    Toolbar toolbar;
    Button clearComment;
    ImageView like, likeNot;
    Button saveComment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);

        // with this line the keyboard doesn't open automatically
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        myValueTopicSelected = getIntent().getExtras().getString("topicSelected");



        collapsingToolbarLayout.setTitle(myValueTopicSelected);

        imageAuthor = (ImageView) findViewById(R.id.imgAuthor);
        etAddComment = (EditText) findViewById(R.id.etAddComment);
        textViewQuestionTitle = (TextView) findViewById(R.id.tvTitle);
        textViewQuestionContent = (TextView) findViewById(R.id.tvQuestionContent);
        textViewAuthor = (TextView) findViewById(R.id.tvAuthor);
        cptPositiveVote = (TextView) findViewById(R.id.nbVotePositive);
        cptNegativeVote = (TextView) findViewById(R.id.nbVoteNegative);
        questionDate = (TextView) findViewById(R.id.questionDate);

        // Recover values from object Question in activity_question_list

        myValueTitle = getIntent().getExtras().getString("myValueKeyTitle");
        textViewQuestionTitle.setText(myValueTitle);

        myValueContent = getIntent().getExtras().getString("myValueKeyContent");
        textViewQuestionContent.setText(myValueContent);

        myValueKeyAuthor = getIntent().getExtras().getString("myValueKeyAuthor");
        textViewAuthor.setText(myValueKeyAuthor);

        myValueKeyIdQuestion = getIntent().getExtras().getInt("myValueKeyIdQuestion");

        activityFrom = getIntent().getExtras().getString("activitySelected");

        date = getIntent().getExtras().getString("date");
        questionDate.setText(questionDate.getText()+" "+date);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usernameSharedPref = sharedPref.getString("username", "");

        dbHelper = new DbHelper(context);



        // favorite float action button

        fab = (FloatingActionButton) findViewById(R.id.fabFavorite);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (dbHelper.verifyFavorite(usernameSharedPref, myValueKeyIdQuestion) == true) {
                    /* If the question is in favorites, we delete the row from table */
                    dbHelper.deleteFavorite(usernameSharedPref, myValueKeyIdQuestion);
                    Toast.makeText(getBaseContext(), R.string.favoriteDeleted, Toast.LENGTH_SHORT).show();

                } else {
                    /* If the question is not in favorites */
                    dbHelper.addFavorite(usernameSharedPref, myValueKeyIdQuestion);
                    Toast.makeText(getBaseContext(), R.string.favoriteAdded, Toast.LENGTH_SHORT).show();
                    dbHelper.close();
                }
                setFabImage();
            }
        });


        // clear the EditText when user click on clear
        clearComment = (Button) findViewById(R.id.btCancelComment);
        clearComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                etAddComment.getText().clear();
                etAddComment.setHint(R.string.addComment);
            }
        });


        //save a comment when user click on save
        saveComment = (Button) findViewById(R.id.btSaveComment);
        saveComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String comment = etAddComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(getBaseContext(), R.string.enterComment, Toast.LENGTH_SHORT).show();

                }else{
                    SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy - HH:mm");
                    String currentTime = time.format(new Date());
                    String content = etAddComment.getText().toString();
                    String author = usernameSharedPref.toString();

                    // add comment in database
                    dbHelper.addComment(content, currentTime, author, myValueKeyIdQuestion);
                    Toast.makeText(getBaseContext(), R.string.commentAdded, Toast.LENGTH_SHORT).show();

                    /***** CLOUD *****/

                    com.example.xavier.myapplication.backend.commentApi.model.Comment c = new com.example.xavier.myapplication.backend.commentApi.model.Comment();
                    c.setUsername(author);
                    c.setContent(content);
                    c.setDate(currentTime);
                    c.setIdQuestion(myValueKeyIdQuestion);

                    MyBean m = new MyBean();
                    m.setData("sd");


                    new EndpointsAsyncTaskComment(c).execute();

                    /***** CLOUD *****/

                    // Clear and set hint in EditText addComment
                    etAddComment.getText().clear();
                    etAddComment.setHint(R.string.addComment);


                    // Close the keyboard on save comment
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    ///Load the updated list
                    setCommentList();



                }
            }
        });


        like = (ImageView) findViewById(R.id.imvVotePositive);
        like.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /* verify database if the current user has already liked or not this question */
                if(dbHelper.verifyIfUserHasVoted(usernameSharedPref, myValueKeyIdQuestion) == true){

                       /* verify if it's a positive or negative vote */
                    if(dbHelper.verifyLike(usernameSharedPref, myValueKeyIdQuestion)==true){

                     /* positive vote */
                        dbHelper.deleteVote(usernameSharedPref, myValueKeyIdQuestion);
                    }
                    else {
                      /* negative vote */
                        String id_question = String.valueOf(myValueKeyIdQuestion);
                        dbHelper.updateVote(usernameSharedPref, "like", id_question);
                    }
                }
                else{
                 /* If the User has not vote this question, we add a new positive vote */
                    dbHelper.addVote(usernameSharedPref, "like", myValueKeyIdQuestion);
                    dbHelper.close();
                     }

                setVote_Icons_Numbers();
            }
        });


        likeNot = (ImageView) findViewById(R.id.imvVoteNegative);
        likeNot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /* verify database if the current user has already liked or not this question */
                if(dbHelper.verifyIfUserHasVoted(usernameSharedPref, myValueKeyIdQuestion) == true){

                        /* verify if it's a positive or negative vote */
                    if(dbHelper.verifyLike(usernameSharedPref, myValueKeyIdQuestion)==true){

                     /* positive vote */
                        String id_question = String.valueOf(myValueKeyIdQuestion);
                        dbHelper.updateVote(usernameSharedPref, "likeNot", id_question);
                    }
                    else {
                      /* negative vote */
                        dbHelper.deleteVote(usernameSharedPref, myValueKeyIdQuestion);
                    }

                    /* the user has voted */
                    String id_question = String.valueOf(myValueKeyIdQuestion);
                    dbHelper.updateVote(usernameSharedPref, "likeNot", id_question);
                }
                else{

                 /* If the User has not vote this question, we add a new positive vote */
                    dbHelper.addVote(usernameSharedPref, "likeNot", myValueKeyIdQuestion);
                    dbHelper.close();
                }

                setVote_Icons_Numbers();
            }
        });


        setFabImage();
        setToolBar();
        countUserPosts();
        setAuthorImage();
        readUserFromDatabase();
        setCommentList();
        setVote_Icons_Numbers();

    }


    private void setToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Because the user cans arrive in this activity from 3 differents ways,
                 * when he clicks the back arrow he wants to return on the good activity */

                if(activityFrom.equalsIgnoreCase("questionList")){
                    Intent goBack = new Intent(QuestionDisplay.this, QuestionList.class);
                    goBack.putExtra("topicSelected", myValueTopicSelected);
                    QuestionDisplay.this.startActivity(goBack);
                }
                else if(activityFrom.equalsIgnoreCase("profile")){
                    Intent goBack = new Intent(QuestionDisplay.this, Profile.class);
                    QuestionDisplay.this.startActivity(goBack);
                }
                else if(activityFrom.equalsIgnoreCase("favorite")){
                    Intent goBack = new Intent(QuestionDisplay.this, FavoriteList.class);
                    QuestionDisplay.this.startActivity(goBack);
                }
            }
        });
    }


    private void setCommentList() {

        listViewComments = (ListView) findViewById(R.id.listview_comments);
        commentAdapter = new CommentAdapter(getApplicationContext(), R.id.comment_list_layout);
        listViewComments.setAdapter(commentAdapter);
        dbHelper = new DbHelper(getApplicationContext());

        String id_question = String.valueOf(myValueKeyIdQuestion);
        cursor = dbHelper.getAllCommentsFromCurrentQuestion(id_question);
        if (cursor.moveToFirst()) {
            do {
                String date, content, username;
                int id;

                id = cursor.getInt(0);
                content = cursor.getString(1);
                date = cursor.getString(2);
                username = cursor.getString(3);
                myValueKeyIdQuestion = cursor.getInt(4);

              // read the question's author from database
                cursorUser = dbHelper.getOneUser(username);

                if(cursorUser.moveToFirst())
                {
                        String uname;
                        byte[] image;
                        uname = cursorUser.getString(0);
                        image = cursorUser.getBlob(1);
                        User u = new User(uname, image);
                        imgCommentAuthor = u.getImage();
                }

                Comment c = new Comment(id, content, date, username, myValueKeyIdQuestion,  imgCommentAuthor);
                commentAdapter.add(c);

            } while (cursor.moveToNext());
        }

        // Count nb comments
        TextView t  = (TextView) findViewById(R.id.tvCommentsCpt);
        t.setText(""+commentAdapter.getCount());

        // Set list Height
        setListViewHeight(listViewComments);
    }


    public static boolean setListViewHeight(ListView listView) {
        int position =0;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int nbItems = listAdapter.getCount();

            int totalItemsHeight = 0;
            for ( position = 0; position < nbItems;position++) {
                View item = listAdapter.getView(position, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            int totalDividersHeight = listView.getDividerHeight() *
                    (nbItems - 1);

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalItemsHeight/4 ;
            listView.setLayoutParams(params);
            listView.requestLayout();


            return true;
        } else {
            return false;
        }
    }


    private void readUserFromDatabase() {
        dbHelper = new DbHelper(context);

        //read the author user from database
        cursor = dbHelper.getOneUser(myValueKeyAuthor);

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


    private void setAuthorImage() {
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

              /* verify database if the current user has this question on his favorites */
        if(dbHelper.verifyFavorite(usernameSharedPref, myValueKeyIdQuestion) == true){

            /* question in user favorites */
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite));
        }
        else{
            /* question not in user favorite*/
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border));
        }
    }


    public void setVote_Icons_Numbers(){
        dbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = dbHelper.getReadableDatabase();

              /* verify database if the current user has already liked or not this question */
        if(dbHelper.verifyIfUserHasVoted(usernameSharedPref, myValueKeyIdQuestion) == true){

            /* the user has voted */

            /* verify if it's a positive or negative vote */
           if(dbHelper.verifyLike(usernameSharedPref, myValueKeyIdQuestion)==true){

               /* positive vote */
               like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_like_green));
               likeNot.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_dontlike_gray));

           }
           else {
               /* negative vote */
               likeNot.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_dontlike_green));
               like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_like_gray));
           }

        }
        else{
            /* the User has not vote this question */
            like.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_like_gray));
            likeNot.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_dontlike_gray));
        }

        /* Count positives and negatives votes for current question */
        cptPositiveVote.setText(""+dbHelper.countPositiveVote(myValueKeyIdQuestion));
        cptNegativeVote.setText(""+dbHelper.countNegativeVote(myValueKeyIdQuestion));

    }
}

