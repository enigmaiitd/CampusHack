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

import java.util.Date;

import iitd.enigma.libraryportal.BookInfo;
import iitd.enigma.libraryportal.R;
import iitd.enigma.libraryportal.UserBooksDB;

class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView mTextView;
    public MyViewHolder(View v){
        super(v);
        mTextView = (TextView) v.findViewById(R.id.Bookname_activitybr);
    }
}

public class CustomRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private UserBooksDB.BookInfo[] bookInfos;

    public CustomRecyclerAdapter(UserBooksDB.BookInfo[] Dataset){
        bookInfos = Dataset;
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
        myViewHolder.mTextView.setText(bookInfos[i].name);
    }

    @Override
    public int getItemCount() {
        return bookInfos.length;
    }
}
