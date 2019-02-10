package iitd.enigma.libraryportal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import iitd.enigma.libraryportal.Adapters.CustomRecyclerAdapter;


public class Library_Info_Activity extends Activity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_issues);

        //Intent intent = getIntent();
        //String username = intent.getStringExtra("username");
        //String password = intent.getStringExtra("password");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_acitivityli);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String[] mDataSet = {"first", "second", "third"};
        //LibraryMail.get(username,password,getApplicationContext());

        UserBooksDB.BookInfo[] dummyInfo = LibraryMail.generateDummyInfo();
        mAdapter = new CustomRecyclerAdapter(dummyInfo);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

    }
}
