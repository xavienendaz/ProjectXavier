package com.example.xavier.projectxavier;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.xavier.myapplication.backend.questionApi.QuestionApi;
import com.example.xavier.myapplication.backend.questionApi.model.*;
import com.example.xavier.myapplication.backend.questionApi.model.Question;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 29.12.2016.
 */

public class EndpointsAsyncTaskQuestion extends AsyncTask<Void, Void, List<Question>> {
    private static QuestionApi questionApi = null;
    private static final String TAG = EndpointsAsyncTaskQuestion.class.getName();
    private com.example.xavier.myapplication.backend.questionApi.model.Question question;

    EndpointsAsyncTaskQuestion(){}

    EndpointsAsyncTaskQuestion(Question question){
        this.question = question;
    }

    @Override
    protected List<Question> doInBackground(Void... params) {

        if(questionApi == null){
            // Only do this once
            QuestionApi.Builder builder = new QuestionApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    // if you deploy on the cloud backend, use your app name
                    // such as https://<your-app-id>.appspot.com
                    //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
              /*     .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });*/
            questionApi = builder.build();
        }

        try{
            // Call here the wished methods on the Endpoints
            // For instance insert
            if(question != null){
                questionApi.insert(question).execute();
                Log.i(TAG, "insert question");
            }
            // and for instance return the list of all employees
            return questionApi.list().execute().getItems();

        } catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Question>();
        }
    }

    //This method gets executed on the UI thread - The UI can be manipulated directly inside
    //of this method
    @Override
    protected void onPostExecute(List<Question> result){

        if(result != null) {
            for (Question question : result) {
                Log.i(TAG, "Title: " + question.getTitle() + " Topic: "
                        + question.getTopic());

          /*      for (Phone phone : question.getPhones()) {
                    Log.i(TAG, "Phone number: " + phone.getNumber() + " Type: " + phone.getType());
                }
             */
            }
        }
    }
}
