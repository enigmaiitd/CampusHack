package iitd.enigma.libraryportal;

import java.util.Calendar;
import java.util.Date;

public class DummyInfo {
    static UserBooksDB.BookInfo[] generateInfo()
    {
        UserBooksDB.BookInfo[] bookInfos = new UserBooksDB.BookInfo[10];
        for (int i = 0; i < 10; i++) {
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

    static void addIssueInfo(UserBooksDB.BookInfo[] booksInfo, UserBooksDB userBooksDB)
    {
        userBooksDB.addBooks(booksInfo);
    }

    static void addReturnInfo(UserBooksDB.BookInfo[] booksInfo, UserBooksDB userBooksDB)
    {
        userBooksDB.deleteBooks(booksInfo);
    }
}
