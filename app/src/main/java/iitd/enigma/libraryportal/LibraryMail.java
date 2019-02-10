package iitd.enigma.libraryportal;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;


public class LibraryMail
{
    public static UserBooksDB.BookInfo[] bookInfos;
    private static Date lastReceivedDate;
    private static String SharedPrefName = "SyncRecord";
    private static String lastSyncedKeyName = "lastSynced";


    static void get(String username, String password, Context context, UserBooksDB userBooksDB)
    {
        String host = "mailstore.iitd.ac.in";// change accordingly

        try
        {
            MailService mailService = new MailService();
            mailService.login(host, username, password);


            SearchTerm fromLibraryTerm = new FromTerm(new InternetAddress("library@iitd.ac.in"));
            SearchTerm subjectIssuedTerm = new SubjectTerm("Central Library Book(s) Issue Slip");
            SearchTerm subjectReturnedTerm = new SubjectTerm("Central Library Book(s) Return Slip");

            SearchTerm andTermIssue,
                    andTermReturn;

            SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);

            if(sharedPreferences.contains(lastSyncedKeyName))
            {
                Long lastSyncedDate = sharedPreferences.getLong(lastSyncedKeyName, 0);
                SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.GE, new Date(lastSyncedDate));
                andTermIssue = new AndTerm(new SearchTerm[]{subjectIssuedTerm, olderThan});
                andTermReturn = new AndTerm(new SearchTerm[]{subjectReturnedTerm, olderThan});
                //TODO: Add fromLibraryTerm
            }
            else
            {
                andTermIssue = subjectIssuedTerm;
                andTermReturn = subjectReturnedTerm;
                //TODO: Add fromLibraryTerm
            }

            Message[] messages = mailService.search(andTermIssue);

            for(Message message : messages)
            {
                processIssuedMessage(message, userBooksDB);
            }

            lastReceivedDate = new Date();

            messages = mailService.search(andTermReturn);

            for(Message message : messages)
            {
                processReturnedMessage(message, userBooksDB);
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(lastSyncedKeyName, new Date().getTime());
            editor.apply();
            mailService.logout();
        }
        catch (Exception ex)
        {
            Log.e("LibraryMail", ex.getLocalizedMessage());
        }
    }

    private static void processIssuedMessage(Message message, UserBooksDB userBooksDB)
            throws MessagingException, IOException
    {
        Log.i("LibraryMail", "Processing Issue Message");

        String messageString = new ReadMessage().getTextFromMessage(message);
        UserBooksDB.BookInfo[] booksInfo = MessageParser.infoIssue(messageString);

        userBooksDB.addBooks(booksInfo);
    }

    private static void processReturnedMessage(Message message, UserBooksDB userBooksDB)
            throws MessagingException, IOException
    {
        Log.i("LibraryMail", "Processing Return Message");

        String messageString = new ReadMessage().getTextFromMessage(message);
        UserBooksDB.BookInfo[] booksInfo = MessageParser.infoReturn(messageString);

        userBooksDB.deleteBooks(booksInfo);
    }

    static void cleanup(Context context, UserBooksDB userBooksDB)
    {
        userBooksDB.deleteAllBooks();
        SharedPreferences sharedPref = context.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(lastSyncedKeyName);
        editor.apply();
    }

    public static UserBooksDB.BookInfo[] generateDummyInfo()
    {
        UserBooksDB.BookInfo[] bookInfos = new UserBooksDB.BookInfo[10];
        for(int i = 0; i < 10; i++)
        {
            bookInfos[i] = new UserBooksDB.BookInfo();
            bookInfos[i].issuedTo = "ABC xyz";
            bookInfos[i].dueDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.set(2019, 2, 13);
            bookInfos[i].dueDate = calendar.getTime();

            bookInfos[i].accessionNumber = "1234";
            bookInfos[i].name = "BOOK Name!";
        }
        return bookInfos;
    }
}