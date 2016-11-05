package com.example.xavier.projectxavier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TopicsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_list);
        setTitle("Topics");

        final String [] topics = getResources().getStringArray(R.array.topics_array);
        ListView list;


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, topics){

            // Call for every entry in the ArrayAdapter
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                //If View doesn't exist create a new view
                if (convertView == null) {
                    // Create the Layout
                    LayoutInflater inflater = getLayoutInflater();
                    view = inflater.inflate(R.layout.listview_layout, parent, false);
                } else {
                    view = convertView;
                }

                //Add Text to the layout
                TextView textView1 = (TextView) view.findViewById(R.id.listview);
                textView1.setText(topics[position]);

                return view;
            }
        };

        //ListView
        list = (ListView) findViewById(R.id.main_listview);
        list.setAdapter(adapter);

        //ListeView handler
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //Toast.makeText(this, "You have selected: " + topics[position], Toast.LENGTH_LONG).show();

                Intent i = new Intent(TopicsList.this, QuestionList.class);
                TopicsList.this.startActivity(i);

            }
        });

    }
}
