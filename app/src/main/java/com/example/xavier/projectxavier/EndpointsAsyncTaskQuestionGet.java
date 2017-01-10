package com.example.xavier.projectxavier;

import android.os.AsyncTask;
import android.util.Log;

import com.example.xavier.myapplication.backend.questionApi.QuestionApi;
import com.example.xavier.myapplication.backend.questionApi.model.Question;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 29.12.2016.
 */
/*
public class EndpointsAsyncTaskQuestionGet extends AsyncTask<Void, Void, Question> {
    private static QuestionApi questionApi = null;
    private static final String TAG = EndpointsAsyncTaskQuestionGet.class.getName();
    private Question question;

    EndpointsAsyncTaskQuestionGet(){}

    EndpointsAsyncTaskQuestionGet(Question question){
        this.question = question;
    }

    @Override
    protected Question doInBackground(Void... params) {

        if(questionApi == null){
            // Only do this once
            QuestionApi.Builder builder = new QuestionApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
            questionApi = builder.build();
        }

        try{
            // Call here the wished methods on the Endpoints
            // For instance insert
            if(question != null){
                questionApi.get(question.getId()).execute();
                Log.i(TAG, "get question");
            }
            // and for instance return the list of all employees
            return question;

        } catch (IOException e){
            Log.e(TAG, e.toString());
            return new Question();
        }
    }

    //This method gets executed on the UI thread - The UI can be manipulated directly inside
    //of this method
    @Override
    protected void onPostExecute(Question result){

    Question q = new Question();

    }

    public Question getQuestion(){
    Question q = new Question();
        q.setId(question.getId());
        q.setTitle(question.getTitle());
        q.setContent(question.getContent());
        return q;
    }
}

*/




public class EndpointsAsyncTaskQuestionGet extends AsyncTask<Void, Void, List<Question>> {
    private static QuestionApi questionApi = null;
    private static final String TAG = EndpointsAsyncTaskQuestionGet.class.getName();
    private Question question;

    EndpointsAsyncTaskQuestionGet(){}

    EndpointsAsyncTaskQuestionGet(Question question){
        this.question = question;
    }

    @Override
    protected List<Question> doInBackground(Void... params) {

        if(questionApi == null){
            // Only do this once
            QuestionApi.Builder builder = new QuestionApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://nutrituo-152708.appspot.com/_ah/api/");
            questionApi = builder.build();
        }

        try{
            // Call here the wished methods on the Endpoints
            // For instance insert
            if(question != null){
                questionApi.get(question.getId()).execute();
                Log.i(TAG, "get question");
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
        Long val = 5715999101812736L;

        if(result != null) {
            for (Question question : result) {
                Log.i(TAG, "Title: " + question.getTitle() + " Topic: "
                        + question.getTopic());

            }
        }
    }
}
