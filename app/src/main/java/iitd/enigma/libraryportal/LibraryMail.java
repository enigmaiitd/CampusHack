package iitd.enigma.libraryportal;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

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

    public static void get(String username, String password)
    {
        String host = "mailstore.iitd.ac.in";// change accordinglya

        try
        {
            // It is good to Use Tag Library to display dynamic content
            MailService mailService = new MailService();
            mailService.login(host, username, password);

            SearchTerm term = new SearchTerm()
            {
                public boolean match(Message message)
                {
                    try
                    {
                        Address[] addresses = message.getFrom();
                        if(addresses.length != 1)
                        {
                            return false;
                        }
                        String address = addresses[0].toString();
                        if(address.equals("library@iitd.ac.in"))
                        {
                            return true;
                        }
                    }
                    catch (MessagingException ex)
                    {
                        ex.printStackTrace();
                    }
                    return false;
                }
            };

            Message[] messages = mailService.search(term);

            for(Message message : messages)
            {
                processMessage(message);
            }

            mailService.logout();
        }
        catch (Exception ex)
        {
            Log.e("Email Check", ex.toString());
        }
    }

    private static void processMessage(Message message) throws MessagingException
    {
        String subject = message.getSubject();

        if(subject.equals("Central Library Book(s) Issue Slip"))
        {

        }
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
