package com.example.xavier.projectxavier;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xavier.myapplication.backend.commentApi.CommentApi;
import com.example.xavier.myapplication.backend.commentApi.model.*;
import com.example.xavier.myapplication.backend.userApi.UserApi;
import com.example.xavier.myapplication.backend.userApi.model.*;
import com.example.xavier.myapplication.backend.userApi.model.User;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 08.01.2017.
 */

    public class EndpoitsAsyncTaskUser extends AsyncTask<Void, Void, List<com.example.xavier.myapplication.backend.userApi.model.User>> {
        private static UserApi userApi = null;
        private static final String TAG = com.example.xavier.projectxavier.EndpoitsAsyncTaskUser.class.getName();
        private User user;

    EndpoitsAsyncTaskUser(){}

    EndpoitsAsyncTaskUser(User user){
            this.user = user;
        }

        @Override
        protected List<User> doInBackground(Void... params) {

            if(userApi == null){
                // Only do this once
                UserApi.Builder builder = new UserApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
                userApi = builder.build();
            }

            try{
                // Call here the wished methods on the Endpoints
                // For instance insert
                if(user != null){
                    userApi.insert(user).execute();
                    Log.i(TAG, "insert user");
                }
                // and for instance return the list of all employees
                return userApi.list().execute().getItems();

            } catch (IOException e){
                Log.e(TAG, e.toString());
                return new ArrayList<User>();
            }
        }

        //This method gets executed on the UI thread - The UI can be manipulated directly inside
        //of this method
        @Override
        protected void onPostExecute(List<User> result){

            if(result != null) {
                for (User user : result) {
                    Log.i(TAG, "Username: " + user.getUsername());

                }
            }
        }
    }

