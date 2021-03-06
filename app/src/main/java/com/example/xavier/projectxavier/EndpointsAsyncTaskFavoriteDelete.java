package com.example.xavier.projectxavier;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xavier.myapplication.backend.favoriteApi.FavoriteApi;
import com.example.xavier.myapplication.backend.favoriteApi.model.*;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 10.01.2017.
 */

    public class EndpointsAsyncTaskFavoriteDelete extends AsyncTask<Void, Void, List<com.example.xavier.myapplication.backend.favoriteApi.model.Favorite>> {
        private static com.example.xavier.myapplication.backend.favoriteApi.FavoriteApi FavoriteApi = null;
        private static final String TAG = com.example.xavier.projectxavier.EndpointsAsyncTaskFavoriteDelete.class.getName();
        private com.example.xavier.myapplication.backend.favoriteApi.model.Favorite favorite;

        EndpointsAsyncTaskFavoriteDelete(){}

        EndpointsAsyncTaskFavoriteDelete(com.example.xavier.myapplication.backend.favoriteApi.model.Favorite favorite){
            this.favorite = favorite;
        }

        @Override
        protected List<com.example.xavier.myapplication.backend.favoriteApi.model.Favorite> doInBackground(Void... params) {

            if(FavoriteApi == null){
                // Only do this once
                FavoriteApi.Builder builder = new FavoriteApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
                FavoriteApi = builder.build();
            }

            try{
                // Call here the wished methods on the Endpoints
                // For instance insert
                if(favorite != null){
                    FavoriteApi.remove(favorite.getId()).execute();
                    Log.i(TAG, "insert vote");
                }
                // and for instance return the list of all employees
                return FavoriteApi.list().execute().getItems();

            } catch (IOException e){
                Log.e(TAG, e.toString());
                return new ArrayList<com.example.xavier.myapplication.backend.favoriteApi.model.Favorite>();
            }
        }

        //This method gets executed on the UI thread - The UI can be manipulated directly inside
        //of this method
        @Override
        protected void onPostExecute(List<com.example.xavier.myapplication.backend.favoriteApi.model.Favorite> result){

            if(result != null) {
                for (com.example.xavier.myapplication.backend.favoriteApi.model.Favorite fav : result) {


                }
            }
        }
    }

