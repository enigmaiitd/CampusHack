package iitd.enigma.libraryportal.Adapters;

import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import iitd.enigma.libraryportal.BookInfo;
import iitd.enigma.libraryportal.R;
import iitd.enigma.libraryportal.UserBooksDB;

class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView mBookName;
    public TextView mAccNo;
    public TextView mDueDate;
    public MyViewHolder(View v){
        super(v);
        mBookName = (TextView) v.findViewById(R.id.Bookname_activitybr);
        mAccNo = (TextView) v.findViewById(R.id.AccNo_activitybr);
        mDueDate = (TextView) v.findViewById(R.id.Duedata_activitybr);
    }
}

public class CustomRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private UserBooksDB.BookInfo[] bookInfos;
    DateFormat dateFormat;
    public CustomRecyclerAdapter(UserBooksDB.BookInfo[] Dataset){
        bookInfos = Dataset;
        dateFormat = new SimpleDateFormat("dd-MMM-yyyy EE");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mainLayout = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_recyler_item, parent, false);
        MyViewHolder vh = new MyViewHolder(mainLayout);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mBookName.setText(bookInfos[i].name);
        myViewHolder.mAccNo.setText(bookInfos[i].accessionNumber);
        myViewHolder.mDueDate.setText(dateFormat.format(bookInfos[i].dueDate));
    }

    @Override
    public int getItemCount() {
        return bookInfos.length;
    }
}
