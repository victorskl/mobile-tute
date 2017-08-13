package com.sankholin.customlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    OptimizedUserAdapter adapter;
    List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create 5 User objects
        final User user1 = new User("Ronaldo", "2/8/2016");
        User user2 = new User("Messi", "2/8/2016");
        User user3 = new User("Rivaldo", "2/8/2015");
        User user4 = new User("Neymar", "2/8/2016");
        User user5 = new User("Ji-sung Park", "2/8/2016");
        User user6 = new User("Pique", "2/8/2016");
        User user7 = new User("David Luis", "2/8/2016");
        User user8 = new User("Ibrahimovic", "2/8/2016");
        User user9 = new User("Beckham", "2/8/2016");
        User user10 = new User("David Seaman", "2/8/2016");
        // add this User objects to your users ArrayList
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);
        users.add(user8);
        users.add(user9);
        users.add(user10);
        listView = (ListView) findViewById(R.id.userList);
        // define your new UserAdapter
        // the first argument is the context. in this case, it is your MainActivity class
        // the second argument is the resourceId of your XML layout
        // the third one is the arraylist of users
        adapter = new OptimizedUserAdapter(MainActivity.this, R.layout.singe_user_item_layout, users);
        //set the adapter for your ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // remove the clicked item from arraylist
                users.remove(users.get(position));
                //notifies that the data has been changed and any View reflecting the data set
                // should refresh itself
                adapter.notifyDataSetChanged();
            }
        });
    }
}
