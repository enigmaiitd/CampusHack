package iitd.enigma.libraryportal;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

class DateConverter {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}

public class UserBooksDB
{
    @Entity
    @TypeConverters(DateConverter.class)
    public static class BookInfo
    {
        @NonNull
        @PrimaryKey
        public String accessionNumber = "INVALID";

        @ColumnInfo(name = "book_name")
        public String name;

        @ColumnInfo(name = "issued_to")
        public String issuedTo;

        @ColumnInfo(name = "due_date")
        public Date dueDate;

        BookInfo(){}

        BookInfo(@NonNull String accessionNumber, String name, String issueTo, Date dueDate)
        {
            this.accessionNumber = accessionNumber;
            this.name = name;
            this.issuedTo = issueTo;
            this.dueDate = dueDate;
        }
    }

    @Dao
    interface BookDao
    {
        @Query("SELECT * FROM BookInfo")
        BookInfo[] getAll();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertAll(BookInfo... books);

        @Delete
        void delete(BookInfo book);
    }

    @Database(entities = {BookInfo.class}, version = 1, exportSchema = false)
    static abstract class AppDatabase extends RoomDatabase {
        abstract BookDao bookDao();
    }

    private AppDatabase db;

    UserBooksDB(Context context)
    {
        db = Room.databaseBuilder(context,
                AppDatabase.class, "books-info").build();
    }

    void addBooks(BookInfo[] booksInfo)
    {
        db.bookDao().insertAll(booksInfo);
    }

    BookInfo[] getBooks()
    {
        return db.bookDao().getAll();
    }

    void deleteBooks(BookInfo[] booksInfo)
    {
        for(BookInfo bookInfo : booksInfo)
        {
            db.bookDao().delete(bookInfo);
        }
    }

    void closeDB()
    {
        db.close();
    }
}