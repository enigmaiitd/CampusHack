package iitd.enigma.libraryportal;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import javax.mail.Address;
import javax.mail.Message;

public class MainActivity extends AppCompatActivity {

    String username;
    String password;

    UserBooksDB userBooksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView mEmail = (TextView) findViewById(R.id.email_activitymain);
        final TextView mPassword = (TextView) findViewById(R.id.password_activitymain);
        TextView mLogin = (TextView) findViewById(R.id.login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = mEmail.getText().toString();// change accordingly
                password = mPassword.getText().toString();// change accordingly

                new RetrieveFeedTask().execute("");

            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        userBooksDB = new UserBooksDB(getApplicationContext());
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        userBooksDB.closeDB();
    }


    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(String... strings) {
            LibraryMail.get(username, password, MainActivity.this, userBooksDB);
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
