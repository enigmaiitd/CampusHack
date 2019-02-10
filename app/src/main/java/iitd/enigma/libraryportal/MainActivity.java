package iitd.enigma.libraryportal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

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



        PushDownAnim.setPushDownAnimTo( mLogin)
        .setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                username = mEmail.getText().toString();// change accordingly
                password = mPassword.getText().toString();// change accordingly
                //new RetrieveFeedTask().execute("");
                Intent intent = new Intent(getApplicationContext(), Library_Info_Activity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);

                Toast.makeText( MainActivity.this, "PUSH DOWN !!", Toast.LENGTH_SHORT ).show();
            }

        });
    }






}
