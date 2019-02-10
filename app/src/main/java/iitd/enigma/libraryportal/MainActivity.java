package iitd.enigma.libraryportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
        final CheckBox Rememberme_Checkbox = findViewById(R.id.Rememberme_checkBox_activitymain);

        final SharedPreferences sharedPreferences = this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        if(username != null){
            password = sharedPreferences.getString("password", null);
            Intent intent = new Intent(getApplicationContext(), Library_Info_Activity.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
        }

        PushDownAnim.setPushDownAnimTo( mLogin)
        .setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick( View view ){
                username = mEmail.getText().toString();// change accordingly
                password = mPassword.getText().toString();// change accordingly

                if (Rememberme_Checkbox.isChecked()){
                    sharedPreferences.edit().putString("username", username).apply();
                    sharedPreferences.edit().putString("password", password).apply();

                }

                //new RetrieveFeedTask().execute("");
                Intent intent = new Intent(getApplicationContext(), Library_Info_Activity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);

                //Toast.makeText( MainActivity.this, "PUSH DOWN !!", Toast.LENGTH_SHORT ).show();
            }

        });
    }






}
