package com.example.xavier.projectxavier;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xavier.myapplication.backend.commentApi.CommentApi;
import com.example.xavier.myapplication.backend.commentApi.model.*;
import com.example.xavier.myapplication.backend.commentApi.model.Comment;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 04.01.2017.
 */

    public class EndpointsAsyncTaskComment extends AsyncTask<Void, Void, List<com.example.xavier.myapplication.backend.commentApi.model.Comment>> {
        private static CommentApi commentApi = null;
        private static final String TAG = EndpointsAsyncTaskComment.class.getName();
        private Comment comment;

    EndpointsAsyncTaskComment(){}

    EndpointsAsyncTaskComment(Comment comment){
            this.comment = comment;
        }

        @Override
        protected List<Comment> doInBackground(Void... params) {

            if(commentApi == null){
                // Only do this once
                CommentApi.Builder builder = new CommentApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        // if you deploy on the cloud backend, use your app name
                        // such as https://<your-app-id>.appspot.com
                        //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setRootUrl("https://nutrituo:8080/_ah/api/");
                     /*   .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });*/
                commentApi = builder.build();
            }

            try{
                // Call here the wished methods on the Endpoints
                // For instance insert
                if(comment != null){
                    commentApi.insert(comment).execute();
                    Log.i(TAG, "insert comment");
                }
                else{
                    Log.i(TAG, "empty list");
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

          /*      for (Phone phone : comment.getPhones()) {
                    Log.i(TAG, "Phone number: " + phone.getNumber() + " Type: " + phone.getType());
                }
             */
                }
            }
        }
    }

