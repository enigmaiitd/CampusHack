package iitd.enigma.libraryportal;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
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

    static void get(String username, String password, Context context, UserBooksDB userBooksDB)
    {
        String host = "mailstore.iitd.ac.in";// change accordingly

        try
        {
            MailService mailService = new MailService();
            mailService.login(host, username, password);

            String fileName = "lastReceivedDate";

            SearchTerm fromLibraryTerm = new FromTerm(new InternetAddress("library@iitd.ac.in"));
            SearchTerm subjectIssuedTerm = new SubjectTerm("Central Library Book(s) Issue Slip");
            SearchTerm subjectReturnedTerm = new SubjectTerm("Central Library Book(s) Return Slip");

            SearchTerm andTerm;

            try
            {
                FileInputStream fis = context.openFileInput(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lastReceivedDate = (Date) ois.readObject();

                SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.GE, lastReceivedDate);
                andTerm = new AndTerm(new SearchTerm[]{subjectIssuedTerm, fromLibraryTerm, olderThan});
            }
            catch (FileNotFoundException fe)
            {
                andTerm = new AndTerm(subjectIssuedTerm, fromLibraryTerm);
            }

            Message[] messages = mailService.search(subjectIssuedTerm);

            for(Message message : messages)
            {
                processIssuedMessage(message, userBooksDB);
            }

            lastReceivedDate = messages[messages.length - 1].getReceivedDate();

            messages = mailService.search(subjectReturnedTerm);

            for(Message message : messages)
            {
                processReturnedMessage(message, userBooksDB);
            }

            Date d = messages[messages.length - 1].getReceivedDate();
            if(d.compareTo(lastReceivedDate) > 0)
            {
                lastReceivedDate = d;
            }

            FileOutputStream outputStream;
            try
            {
                outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            }
            catch(FileNotFoundException fi)
            {
                outputStream = new FileOutputStream(context.getFilesDir().getPath() + "/" + fileName);
            }
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(lastReceivedDate);

            Log.i("LibraryMail", lastReceivedDate.toString());
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