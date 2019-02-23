package iitd.enigma.libraryportal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewBook_Activity extends Activity {
    EditText bookName;
    EditText accessionNumber;
    EditText issuedTo;
    EditText dueDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddialogbox);

        Button cancelButton = findViewById(R.id.cancelButton_activityadb);
        Button addButton = findViewById(R.id.addButton_activityadb);

        //TODO: Properly Implement adding books
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserBooksDB.BookInfo bookInfo = new UserBooksDB.BookInfo();
                bookInfo.name = bookName.getText().toString();
                bookInfo.accessionNumber = accessionNumber.getText().toString();
                bookInfo.issuedTo = issuedTo.getText().toString();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
