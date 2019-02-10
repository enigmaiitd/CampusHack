package iitd.enigma.libraryportal;

import java.util.Date;

public class BookInfo
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
