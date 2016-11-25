package com.example.xavier.projectxavier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class main  extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> friendsList;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;

    private final static String TAG = main.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list_item);

        friendsList = new ArrayList<>();
        String line = "ISJDOISDPISAUDPUSAPDUB";

        friendsList.add(line); // add line to array list
        friendsList.add(line); // add line to array list
        friendsList.add(line); // add line to array lisst
        friendsList.add(line); // add line to array list
        friendsList.add(line); // add line to array lisst
        friendsList.add(line); // add line to array lisst
        friendsList.add(line); // add line to array list
        friendsList.add(line); // add line to array lisst
        friendsList.add(line); // add line to array list
        friendsList.add(line); // add line to array list


        setSwipeViewFeatures();

        setListViewAdapter();
    }



  /*
  setListViewHeader();
  private void setListViewHeader() {
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.header_listview, listView, false);
        totalClassmates = (TextView) header.findViewById(R.id.total);
        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures();
        listView.addHeaderView(header);
    }
*/
    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i(TAG, "onClose");
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.i(TAG, "on swiping");
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i(TAG, "on start open");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally show");
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i(TAG, "the BottomView totally close");
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }

    private void setListViewAdapter() {
        adapter = new ListViewAdapter(this, R.layout.item_listview, friendsList);
        listView.setAdapter(adapter);

        totalClassmates.setText("(" + friendsList.size() + ")");
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged(); //update adapter
        totalClassmates.setText("(" + friendsList.size() + ")"); //update total friends in list
    }
}

