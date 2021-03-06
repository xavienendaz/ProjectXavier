package com.example.xavier.projectxavier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.example.xavier.myapplication.backend.commentApi.CommentApi;
import com.example.xavier.myapplication.backend.commentApi.model.*;
import com.example.xavier.myapplication.backend.myApi.MyApi;
import com.example.xavier.myapplication.backend.voteApi.VoteApi;
import com.example.xavier.myapplication.backend.voteApi.model.*;
import com.example.xavier.myapplication.backend.voteApi.model.Vote;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 07.01.2017.
 */

    public class EndpointsAsyncTaskLike extends AsyncTask<Void, Void, List<Vote>> {
        private static VoteApi voteApi = null;
        private static final String TAG = com.example.xavier.projectxavier.EndpointsAsyncTaskLike.class.getName();
        private com.example.xavier.myapplication.backend.voteApi.model.Vote vote;

    EndpointsAsyncTaskLike(){}

    EndpointsAsyncTaskLike(com.example.xavier.myapplication.backend.voteApi.model.Vote vote){
            this.vote = vote;
        }

        @Override
        protected List<Vote> doInBackground(Void... params) {

            if(voteApi == null){
                // Only do this once
                VoteApi.Builder builder = new VoteApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
                voteApi = builder.build();
            }

            try{
                // Call here the wished methods on the Endpoints
                // For instance insert
                if(vote != null){
                    voteApi.insert(vote).execute();

                    Log.i(TAG, "insert vote");
                }
                // and for instance return the list of all employees
                return voteApi.list().execute().getItems();

            } catch (IOException e){
                Log.e(TAG, e.toString());
                return new ArrayList<Vote>();
            }
        }

        //This method gets executed on the UI thread - The UI can be manipulated directly inside
        //of this method
        @Override
        protected void onPostExecute(List<Vote> result){

            if(result != null) {
                for (Vote vote : result) {
                    Log.i(TAG, "Username: " + vote.getUsername() + " IdQuestion: "
                            + vote.getIdQuestion()+" Vote: " + vote.getVote());

          /*      for (Phone phone : comment.getPhones()) {
                    Log.i(TAG, "Phone number: " + phone.getNumber() + " Type: " + phone.getType());
                }
             */
                }
            }
        }
    }

