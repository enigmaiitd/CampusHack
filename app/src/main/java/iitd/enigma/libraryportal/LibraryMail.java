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
            SearchTerm subjectTerm = new AndTerm(new SubjectTerm("Central Library Book(s) Issue Slip"), fromLibraryTerm);
            SearchTerm andTerm;

            try
            {
                FileInputStream fis = context.openFileInput(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lastReceivedDate = (Date) ois.readObject();

                SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.GE, lastReceivedDate);
                andTerm = new AndTerm(subjectTerm, olderThan);
            }
            catch (FileNotFoundException fe)
            {
                andTerm = subjectTerm;
            }

            Message[] messages = mailService.search(new AndTerm(fromLibraryTerm, subjectTerm));

            for(Message message : messages)
            {
                processIssuedMessage(message, userBooksDB);
            }

            lastReceivedDate = messages[messages.length - 1].getReceivedDate();

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
        Log.i("LibraryMail", "Processed Issue Message");
        String subject = message.getSubject();

        String messageString = new ReadMessage().getTextFromMessage(message);
        UserBooksDB.BookInfo[] booksInfo = MessageParser.infoIssue(messageString);

        userBooksDB.addBooks(booksInfo);
    }

    public static UserBooksDB.BookInfo[] generateDummyInfo()
    {
        UserBooksDB.BookInfo[] bookInfos = new UserBooksDB.BookInfo[10];
        for(int i = 0; i < 10; i++)
        {
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
