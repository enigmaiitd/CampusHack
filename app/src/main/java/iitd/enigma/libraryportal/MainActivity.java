package iitd.enigma.libraryportal;

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


    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(String... strings) {

            String host = "mailstore.iitd.ac.in";// change accordinglya


            try {
                // It is good to Use Tag Library to display dynamic content
                MailService mailService = new MailService();
                mailService.login(host, username, password);
                int messageCount = mailService.getMessageCount();

                //just for tutorial purpose
                if (messageCount > 5)
                    messageCount = 5;
                Message[] messages = mailService.getMessages();
                for (int i = 0; i < messageCount; i++) {
                    String subject = "";
                    if (messages[i].getSubject() != null) {
                        subject = messages[i].getSubject();
                    }
                    Address[] fromAddress = messages[i].getFrom();

                    Log.d("Email Check", subject);


                }
            } catch (Exception ex) {
                Log.e("Email Check", ex.toString());
            }
            return null;
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}
