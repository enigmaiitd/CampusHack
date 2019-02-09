package iitd.enigma.libraryportal;

import android.util.Log;

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
    public Date due_date;
    public String issuedTo;

    BookInfo(String accessionNumber, String name, Date due_date, String issuedTo)
    {
        this.accessionNumber = accessionNumber;
        this.name = name;
        this.due_date = due_date;
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
            /*int messageCount = mailService.getMessageCount();

            //just for tutorial purpose
            if (messageCount > 5)   messageCount = 5;
            Message[] messages = mailService.getMessages();

            for (int i = 0; i < messageCount; i++)
            {
                String subject = "";
                if (messages[i].getSubject() != null)
                {
                    subject = messages[i].getSubject();
                }

                Address[] fromAddress = messages[i].getFrom();

                Log.d("Email Check", subject);
            }*/

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

    private static void processMessage(Message message)
    {
        String subject = message.getSubject();

        if(subject.equals("Central Library Book(s) Issue Slip"))
        {

        }
    }
}
