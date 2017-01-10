package com.example.xavier.projectxavier;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xavier.myapplication.backend.commentApi.CommentApi;
import com.example.xavier.myapplication.backend.commentApi.model.Comment;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 04.01.2017.
 */

    public class EndpointsAsyncTaskCommentGet extends AsyncTask<Void, Void, List<Comment>> {
        private static CommentApi commentApi = null;
        private static final String TAG = EndpointsAsyncTaskCommentGet.class.getName();
        private Comment comment;

    EndpointsAsyncTaskCommentGet(){}

    EndpointsAsyncTaskCommentGet(Comment comment){
            this.comment = comment;
        }

        @Override
        protected List<Comment> doInBackground(Void... params) {

            if(commentApi == null){
                // Only do this once
                CommentApi.Builder builder = new CommentApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
                commentApi = builder.build();
            }

            try{
                // Call here the wished methods on the Endpoints
                // For instance insert
                if(comment != null){
                    commentApi.get(comment.getId()).execute();
                    Log.i(TAG, "insert comment");
                }
                // and for instance return the list of all employees
                return commentApi.list().execute().getItems();

            } catch (IOException e){
                Log.e(TAG, e.toString());
                return new ArrayList<Comment>();
            }
        }

        //This method gets executed on the UI thread - The UI can be manipulated directly inside
        //of this method
        @Override
        protected void onPostExecute(List<Comment> result){

            if(result != null) {
                for (Comment comment : result) {
                    Log.i(TAG, "Username: " + comment.getUsername() + " Content: "
                            + comment.getContent());

                }
            }
        }
    }

