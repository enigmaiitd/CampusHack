package iitd.enigma.libraryportal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageParser {
    public static BookInfo[] infoIssue(String messageString) {
        //since function written according to whitespaces
        messageString = messageString.replace("\t", "        ");

        String accNo;
        String name;
        Date dueDate = new Date();
        String IssuedTo;
        String EntryNo;//if required

        String[] lines;
        String infoLine;
        String[] info;
        int count = 0;//counter for line no
        int noOfBooks = 0;

        //splitting string line by line
        lines = messageString.split("\\r?\\n");

        //checking for occurence of ISSUE
        for (; count < lines.length; count++) {
            if (lines[count].matches("(.*)ISSUE(.*)"))
                break;
        }

        count++;
        while (count < lines.length)//search for nearest non-empty line
        {
            if (lines[count].trim().isEmpty())
                count++;
            else break;
        }


        String nameInfo = lines[count].trim(); //trimming whitespaces
        String[] NameAndNo = nameInfo.split("\\(|\\)");//splitting by ( or )
        IssuedTo = NameAndNo[0];
        EntryNo = NameAndNo[1];

        count++;
        //checking for occurence of S NO
        for (; count < lines.length; count++) {
            if (lines[count].matches("(.*)S NO(.*)"))
                break;
        }

        count++;
        while (count < lines.length)//search for nearest non-empty line
        {
            if (lines[count].trim().isEmpty())
                count++;
            else break;
        }
        int firstBook = count;//storing line no of data of first book
        while (count < lines.length)//counting no of books
        {
            infoLine = lines[count].trim();
            if (infoLine.isEmpty()) {
                count++;
                continue;
            }
            info = infoLine.split("\\s{2,30}");//splitting by 2 or more whitespaces
            if (info.length >= 4) noOfBooks++;
            count++;
        }

        BookInfo[] bookInfo = new BookInfo[noOfBooks];

        count = firstBook;

        for (int i = 0; i < noOfBooks; i++) {
            while (count < lines.length) {//reaching the line where data of ith book begins
                infoLine = lines[count].trim();
                if (infoLine.isEmpty()) {
                    count++;
                    continue;
                }
                info = infoLine.split("\\s{2,30}");//splitting by 2 or more whitespaces
                if (info.length >= 4) break;
                count++;
            }

            infoLine = lines[count].trim();
            info = infoLine.split("\\s{2,30}");//splitting by 2 or more whitespaces
            name = info[1].trim();//got some words of name
            accNo = info[2].trim();//got accno
            String dateStr = info[3].trim();//got date in string form

            //counting charcters(including whitespaces) before appearance of name of book in the current line
            infoLine = lines[count];
            int noOfWhite = 0;
            for (int k = 0; k < infoLine.length(); k++) {
                if (infoLine.charAt(k) != name.charAt(0))
                    noOfWhite++;
                else break;
            }

            for (count++; count < lines.length; count++) {
                infoLine = lines[count];
                if (infoLine == null || infoLine.isEmpty()) break;
                int noOfWhite_current = 0;
                for (int k = 0; k < infoLine.length(); k++) {
                    if (infoLine.charAt(k) == ' ') noOfWhite_current++;
                    else break;
                }
                if (noOfWhite_current + 1 != noOfWhite && noOfWhite_current!=noOfWhite)
                    break;//break if no of leading whitespaces is lesser

                infoLine = infoLine.trim();
                name = name + " " + infoLine;//adding to name of book
            }

            //string to date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {

                dueDate = formatter.parse(dateStr);
            } catch (ParseException e) {
            }

            bookInfo[i] = new BookInfo(accNo, name, dueDate, IssuedTo);
        }

        return bookInfo;
    }

    public static BookInfo[] infoReturn(String messageString) {
        //since function written according to whitespaces
        messageString = messageString.replace("\t", "        ");

        String accNo;
        String name;
        Date dueDate = new Date();
        String IssuedTo;
        String EntryNo;//if required

        String[] lines;
        String infoLine;
        String[] info;
        int count = 0;//counter for line no
        int noOfBooks = 0;

        //splitting string line by line
        lines = messageString.split("\\r?\\n");

        //checking for occurence of RETURN
        for (; count < lines.length; count++) {
            if (lines[count].matches("(.*)RETURN(.*)"))
                break;
        }

        count++;
        while (count < lines.length)//search for nearest non-empty line
        {
            if (lines[count].trim().isEmpty())
                count++;
            else break;
        }


        String nameInfo = lines[count].trim(); //trimming whitespaces
        String[] NameAndNo = nameInfo.split("\\(|\\)");//splitting by ( or )
        IssuedTo = NameAndNo[0];
        EntryNo = NameAndNo[1];

        count++;
        //checking for occurence of ACCN NO
        for (; count < lines.length; count++) {
            if (lines[count].matches("(.*)ACCN NO(.*)"))
                break;
        }

        count++;
        while (count < lines.length)//search for nearest non-empty line
        {
            if (lines[count].trim().isEmpty())
                count++;
            else break;
        }
        int firstBook = count;//storing line no of data of first book
        while (count < lines.length)//counting no of books
        {
            infoLine = lines[count].trim();
            if (infoLine.isEmpty()) {
                count++;
                continue;
            }
            info = infoLine.split("\\s{2,30}");//splitting by 2 or more whitespaces
            if (info.length >= 3) noOfBooks++;
            count++;
        }

        BookInfo[] bookInfo = new BookInfo[noOfBooks];

        count = firstBook;

        for (int i = 0; i < noOfBooks; i++) {
            while (count < lines.length) {//reaching the line where data of ith book begins
                infoLine = lines[count].trim();
                if (infoLine.isEmpty()) {
                    count++;
                    continue;
                }
                info = infoLine.split("\\s{2,30}");//splitting by 2 or more whitespaces
                if (info.length >= 3) break;
                count++;
            }

            infoLine = lines[count].trim();
            info = infoLine.split("\\s{2,30}");//splitting by 2 or more whitespaces
            name = info[1].trim();//got some words of name
            accNo = info[0].trim();//got accno
            String dateStr = info[2].trim();//got date in string form

            //counting charcters(including whitespaces) before appearance of name of book in the current line
            infoLine = lines[count];
            int noOfWhite = 0;
            for (int k = 0; k < infoLine.length(); k++) {
                if (infoLine.charAt(k) != name.charAt(0))
                    noOfWhite++;
                else break;
            }

            for (count++; count < lines.length; count++) {
                infoLine = lines[count];
                if (infoLine == null || infoLine.isEmpty()) break;
                int noOfWhite_current = 0;
                for (int k = 0; k < infoLine.length(); k++) {
                    if (infoLine.charAt(k) == ' ') noOfWhite_current++;
                    else break;
                }
                if (noOfWhite_current + 1 != noOfWhite && noOfWhite_current!=noOfWhite)
                    break;//break if no of leading whitespaces is lesser

                infoLine = infoLine.trim();
                name = name + " " + infoLine;//adding to name of book
            }

            //string to date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {

                dueDate = formatter.parse(dateStr);
            } catch (ParseException e) {
            }

            bookInfo[i] = new BookInfo(accNo, name, dueDate, IssuedTo);
        }

        return bookInfo;
    }

}
