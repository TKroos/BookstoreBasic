package edu.stevens.cs522.bookstore.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Xiang on 2015/2/21.
 */
public class BookContract {
    public static final String AUTHORITY = "edu.stevens.cs522.bookstore";
    public static final String BOOK_TABLE = "Books";
    public static final String AUTHOR_TABLE = "Authors";
    public static final Uri BOOK_TABLE_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).path(BOOK_TABLE).build();
    public static final Uri AUTHOR_TABLE_URI = new Uri.Builder().scheme("content").authority(AUTHORITY).path(AUTHOR_TABLE).build();
    public static final String BOOK_TYPE = "vnd.android.cursor.dir/vnd.edu.stevens.cs522.bookstore.books";
    public static final String BOOK_ITEM_TYPE = "vnd.android.cursor.item/vnd.edu.stevens.cs522.bookstore.book";
    public static final String AUTHOR_TYPE = "vnd.android.cursor.dir/vnd.edu.stevens.cs522.bookstore.authors";
    public static final String AUTHOR_ITEM_TYPE = "vnd.android.cursor.item/vnd.edu.stevens.cs522.bookstore.author";
    public static final String BOOK_PATH = "Books";
    public static final String BOOK_PATH_ITEM = "Books/#";
    public static final String AUTHOR_PATH = "Authors";
    public static final String AUTHOR_PATH_ITEM = "Authors/#";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String AUTHORS = "authors";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";
    public static final String AUTHOR = "author";
    public static final String BOOK_FK = "book_fk";
    public static Uri withExtendedPath(Uri uri, String... path){
        Uri.Builder builder = uri.buildUpon();
        for(String p : path)
            builder.appendPath(p);
        return builder.build();
    }
    public static final String getID(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(_ID));
    }
    public static final String getTitle(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
    }
    public static final String getAuthors(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(AUTHORS));
    }
    public static final String getISBN(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(ISBN));
    }
    public static final String getPrice(Cursor cursor){
        return cursor.getString(cursor.getColumnIndexOrThrow(PRICE));
    }
    public static final void putID(ContentValues values, String id){
        values.put(_ID, (byte[]) null);
    }
    public static final void putTitle(ContentValues values, String title){
        values.put(TITLE, title);
    }
    public static final void putAuthors(ContentValues values, String authors){
        values.put(AUTHORS, authors);
    }
    public static final void putAuthor(ContentValues values, String author){
        values.put(AUTHOR, author);
    }
    public static final void putBookFK(ContentValues values, String book_fk){
        values.put(BOOK_FK, book_fk);
    }
    public static final void putISBN(ContentValues values, String isbn){
        values.put(ISBN, isbn);
    }
    public static final void putPrice(ContentValues values, String price){
        values.put(PRICE, price);
    }
}
