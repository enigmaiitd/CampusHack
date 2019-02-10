package iitd.enigma.libraryportal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddNewBook_Activity extends Activity {
    EditText mBookName;
    EditText mAccno;
    EditText mIssuedTo;
    EditText mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddialogbox);


        Button cancelButton_activityadb = (Button) findViewById(R.id.cancelButton_activityadb);
        Button addButton_activityadb = (Button) findViewById(R.id.addButton_activityadb);

        addButton_activityadb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserBooksDB.BookInfo bookInfo = new UserBooksDB.BookInfo();
                bookInfo.name = mBookName.getText().toString();
                bookInfo.accessionNumber = mAccno.getText().toString();
                bookInfo.issuedTo = mIssuedTo.getText().toString();

            }
        });

        cancelButton_activityadb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
