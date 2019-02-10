package iitd.enigma.libraryportal;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import iitd.enigma.libraryportal.Adapters.CustomRecyclerAdapter;


public class Library_Info_Activity extends Activity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    String username;
    String password;

    UserBooksDB userBooksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_issues);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_acitivityli);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //String[] mDataSet = {"first", "second", "third"};
        //LibraryMail.get(username,password,getApplicationContext());
        UserBooksDB.BookInfo[] dummyInfo = LibraryMail.generateDummyInfo();

        mAdapter = new CustomRecyclerAdapter(dummyInfo);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        new RetrieveFeedTask().execute("");

        /*
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                LibraryMail.get(username, password, Library_Info_Activity.this, userBooksDB);
                UserBooksDB.BookInfo[] booksInfo = userBooksDB.getBooks();
                for (UserBooksDB.BookInfo bookInfo : booksInfo) {
                    Log.i("LibraryMail", bookInfo.name);
                }


            }
        });
        */


    }


    @Override
    protected void onResume() {
        super.onResume();
        userBooksDB = new UserBooksDB(getApplicationContext());
    }


    @Override
    protected void onPause() {
        super.onPause();
        userBooksDB.closeDB();
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(String... strings) {
            LibraryMail.get(username, password, Library_Info_Activity.this, userBooksDB);
            UserBooksDB.BookInfo[] booksInfo =  userBooksDB.getBooks();
            for(UserBooksDB.BookInfo bookInfo : booksInfo)
            {
                Log.i("LibraryMail", bookInfo.name);
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}
