package iitd.enigma.libraryportal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import iitd.enigma.libraryportal.Adapters.CustomRecyclerAdapter;


public class Library_Info_Activity extends Activity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ImageButton mSettingButton;

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

        mSettingButton = (ImageButton) findViewById(R.id.settingsButton_activityli) ;
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPop();
            }
        });
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

        userBooksDB = new UserBooksDB(getApplicationContext());
        new RetrieveFeedTask().execute("");
    }

    void ShowPop(){
        //Creating the instance of PopupMenu
        PopupMenu popupMenu = new PopupMenu(Library_Info_Activity.this, mSettingButton);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater().inflate(R.menu.settings, popupMenu.getMenu());
        //registering popup with OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if("Log Out".equals(menuItem.getTitle())){
                    Toast.makeText(Library_Info_Activity.this,"You Clicked : " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("username", null).apply();
                    sharedPreferences.edit().putString("password", null).apply();
                    LibraryMail.cleanup(Library_Info_Activity.this, userBooksDB);
                    finish();
                }

                return false;
            }
        });

        popupMenu.show();
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

    class RetrieveFeedTask extends AsyncTask<String, Void, UserBooksDB.BookInfo[]> {

        private Exception exception;

        @Override
        protected UserBooksDB.BookInfo[] doInBackground(String... strings) {
            LibraryMail.get(username, password, Library_Info_Activity.this, userBooksDB);
            UserBooksDB.BookInfo[] booksInfo =  userBooksDB.getBooks();
            Log.i("LibraryMail", "I reached here " + booksInfo.length);
            for(UserBooksDB.BookInfo bookInfo : booksInfo)
            {
                Log.i("LibraryMail", bookInfo.name);
            }
            Log.i("LibraryMail", "I reached 2 here");
            return booksInfo;
        }

        protected void onPostExecute(UserBooksDB.BookInfo[] booksInfo) {
            // TODO: check this.exception
            // TODO: do something with the feed
            mAdapter = new CustomRecyclerAdapter(booksInfo);
            mRecyclerView.swapAdapter(mAdapter, false);
        }
    }

}
