package iitd.enigma.libraryportal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
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
        mPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
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
                username = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if (Rememberme_Checkbox.isChecked()){
                    sharedPreferences.edit().putString("username", username).apply();
                    sharedPreferences.edit().putString("password", password).apply();

                }

                Intent intent = new Intent(getApplicationContext(), Library_Info_Activity.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            }

        });
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '•'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    };




}
