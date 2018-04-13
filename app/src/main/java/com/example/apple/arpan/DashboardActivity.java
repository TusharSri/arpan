package com.example.apple.arpan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    List albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        albumList = new ArrayList<>();
        settingDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void settingDatabase() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
//        mFirebaseInstance.setPersistenceEnabled(true);
        mFirebaseDatabase = mFirebaseInstance.getReference();
        mFirebaseDatabase.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                albumList.clear();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    DataModel note = noteDataSnapshot.getValue(DataModel.class);
                    albumList.add(note);
                }
                adapter = new CustomAdapter(albumList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}