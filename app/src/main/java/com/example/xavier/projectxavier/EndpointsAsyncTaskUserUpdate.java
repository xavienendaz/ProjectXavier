package com.example.xavier.projectxavier;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xavier.myapplication.backend.userApi.UserApi;
import com.example.xavier.myapplication.backend.userApi.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 10.01.2017.
 */

    public class EndpointsAsyncTaskUserUpdate extends AsyncTask<Void, Void, List<com.example.xavier.myapplication.backend.userApi.model.User>> {
        private static UserApi userApi = null;
        private static final String TAG = com.example.xavier.projectxavier.EndpointsAsyncTaskUserUpdate.class.getName();
        private com.example.xavier.myapplication.backend.userApi.model.User user;

        EndpointsAsyncTaskUserUpdate(){}
        EndpointsAsyncTaskUserUpdate(com.example.xavier.myapplication.backend.userApi.model.User user){
            this.user = user;
        }

        @Override
        protected List<com.example.xavier.myapplication.backend.userApi.model.User> doInBackground(Void... params) {

            if(userApi == null){
                UserApi.Builder builder = new UserApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
                userApi = builder.build();
            }
            try{
                if(user != null){
                    userApi.update(user.getId(),user).execute();
                    Log.i(TAG, "update user");
                }
                return userApi.list().execute().getItems();

            } catch (IOException e){
                Log.e(TAG, e.toString());
                return new ArrayList<com.example.xavier.myapplication.backend.userApi.model.User>();
            }
        }

        @Override
        protected void onPostExecute(List<com.example.xavier.myapplication.backend.userApi.model.User> result){

            if(result != null) {
                for (com.example.xavier.myapplication.backend.userApi.model.User user : result) {
                    Log.i(TAG, "Username: " + user.getUsername());

                }
            }
        }
    }

