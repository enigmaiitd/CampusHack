package iitd.enigma.libraryportal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sun.mail.util.MailConnectException;

import javax.mail.AuthenticationFailedException;

import iitd.enigma.libraryportal.Adapters.CustomRecyclerAdapter;


public class Library_Info_Activity extends Activity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageButton settingButton;
    ImageButton addButton;
    RelativeLayout mainLayout;
    String username;
    String password;
    TextView textViewLoading; //TODO: Think of better name
    TextView textViewUsername;

    UserBooksDB userBooksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_issues);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        mainLayout = findViewById(R.id.parent_activityli);
        textViewLoading = findViewById(R.id.loading_activityli);
        textViewUsername = findViewById(R.id.userName_activityli);

        settingButton = findViewById(R.id.settingsButton_activityli) ;
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPop();
            }
        });

        addButton = findViewById(R.id.addButton_activityli);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), AddNewBook_Activity.class);
                startActivity(intent1);
            }
        });

        recyclerView = findViewById(R.id.recyclerView_acitivityli);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        userBooksDB = new UserBooksDB(getApplicationContext());
        new RetrieveFeedTask().execute("");

    }

    void ShowPop(){
        //Creating the instance of PopupMenu
        PopupMenu popupMenu = new PopupMenu(Library_Info_Activity.this, settingButton);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater().inflate(R.menu.settings, popupMenu.getMenu());
        //registering popup with OnMenuItemClickListener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if("Log Out".equals(menuItem.getTitle())){
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("username", null).apply();
                    sharedPreferences.edit().putString("password", null).apply();
                    LibraryMail.cleanup(Library_Info_Activity.this, userBooksDB);
                    Toast.makeText(Library_Info_Activity.this,"Logged out successfully", Toast.LENGTH_SHORT).show();
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

        private Exception exception = null;

        @Override
        protected UserBooksDB.BookInfo[] doInBackground(String... strings) {

            try {
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
            catch (Exception ex)
            {
                exception = ex;
            }
            return null;

        }

        protected void onPostExecute(UserBooksDB.BookInfo[] booksInfo) {
            // TODO: check this.exception
            // TODO: do something with the feed

            if(exception == null){

                if(booksInfo.length != 0){
                    textViewUsername.setText(booksInfo[0].issuedTo);
                    textViewLoading.setVisibility(View.GONE);
                }
                else{
                    textViewLoading.setText("No Books Issued");
                }


                adapter = new CustomRecyclerAdapter(booksInfo);
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(adapter);
            }else {
                textViewLoading.setVisibility(View.GONE);

                exception.printStackTrace();
                Log.e("Error",exception.getLocalizedMessage());

                if(exception instanceof AuthenticationFailedException){
                    Snackbar.make(mainLayout, "Authentication Failed. Check username and password" , Snackbar.LENGTH_LONG).show();
                }
                else if(exception instanceof MailConnectException){
                    Snackbar.make(mainLayout, "Check Internet Connection" , Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(mainLayout, "Some Exception occurred. Try again later" , Snackbar.LENGTH_LONG).show();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        }
    }

}
