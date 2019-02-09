package iitd.enigma.libraryportal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageParser {
    public static BookInfo giveParsedObject(String messageString){
        //since function written according to whitespaces
        messageString = messageString.replace("\t", "        ");

        String accNo;
        String name;
        Date dueDate ;
        String IssuedTo ;
        String EntryNo;//if required

        //splitting string line by line
        String[] lines=messageString.split("\\r?\\n");
        int count=0;//counter for line no

        //checking for occurence of ISSUE
        for(;count<lines.length;count++)
        {
            if(lines[count].matches("(.*)ISSUE(.*)"))
                break;
        }

        count+=2;//name and no is given 2 lines after occurence of ISSUE
        String nameInfo=lines[count].trim(); //trimming whitespaces
        String [] NameAndNo=nameInfo.split("\\(|\\)");//splitting by ( or )
        IssuedTo=NameAndNo[0];//got issued to
        EntryNo=NameAndNo[1];//got entry no (if required)

        count++;//moving to next line
        //checking for occurence of S NO
        for(;count<lines.length;count++)
        {
            if(lines[count].matches("(.*)S NO(.*)"))
                break;
        }

        count+=2;//info is given 2 lines after occurence of S NO
        String infoLine=lines[count].trim();
        String [] info=infoLine.split("\\s{2,30}");//splitting by 2 or more whitespaces
        name=info[1].trim();//got some words of name
        accNo=info[2].trim();//got accno
        String dateStr= info[3].trim();//got date in string form

        //counting charcters(including whitespaces) before apperance of name of book in the current infoline
        //this is because if name of book is continued in next few lines then almost same whitespaces appear
        //before the name
        infoLine=lines[count];
        int noOfWhite=0;
        for(int i=0;i<infoLine.length();i++)
        {
            if(infoLine.charAt(i)!=name.charAt(0))
                noOfWhite++;
            else break;
        }

        count++; //next line
        for(;count<lines.length;count++)
        {
            infoLine=lines[count];
            if(infoLine == null || infoLine.isEmpty()) break;//break if empty string
            int noOfWhite_current=0;
            for(int i=0;i<infoLine.length();i++) {
                if (infoLine.charAt(i) == ' ') noOfWhite_current++;
                else break;
            }
            if(noOfWhite_current+1<noOfWhite) break;//break if no of leading whitespaces is lesser

            infoLine=infoLine.trim();
            name=name+" "+infoLine;//adding to name
        }



        //THIS IS GIVING SOME ERROR
        //String date1="11/10/2000"; for trial purpose
        dueDate=new SimpleDateFormat("dd MM yyyy").parse(dateStr);

        BookInfo bookInfo= new BookInfo(accNo,name,dueDate,IssuedTo);
        return bookInfo;

    }
}
