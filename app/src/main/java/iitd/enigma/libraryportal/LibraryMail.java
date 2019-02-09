package iitd.enigma.libraryportal;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

class BookInfo
{
    public String accessionNumber;
    public String name;
    public Date dueDate;
    public String issuedTo;

    BookInfo(String accessionNumber, String name, Date dueDate, String issuedTo)
    {
        this.accessionNumber = accessionNumber;
        this.name = name;
        this.dueDate = dueDate;
        this.issuedTo = issuedTo;
    }

}

public class LibraryMail
{
    public static BookInfo[] bookInfos;
    private static Date lastReceivedDate;

    static void get(String username, String password, Context context)
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

            Message[] messages = mailService.search(andTerm);

            for(Message message : messages)
            {
                processIssueMessage(message);
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

    private static void processIssueMessage(Message message) throws MessagingException, IOException
    {
        Log.i("LibraryMail", "Processed Issue Message");
        String subject = message.getSubject();
            String messageString = new ReadMessage().getTextFromMessage(message);
            BookInfo[] b = MessageParser.infoIssue(messageString);
            /* MessageParser to be used here. TODO */


    }

    public static BookInfo[] generateDummyInfo()
    {
        BookInfo[] bookInfos = new BookInfo[10];
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
