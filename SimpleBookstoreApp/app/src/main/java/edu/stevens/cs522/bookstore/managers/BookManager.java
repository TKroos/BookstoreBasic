package edu.stevens.cs522.bookstore.managers;

import android.content.ContentValues;
import android.net.Uri;

import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Book;

/**
 * Created by Xiang on 2015/2/27.
 */
public class BookManager extends Manager {
    public interface IContinue<T>{
        public void kontinue(T value);
    }
    public void insertAsync(Uri uri, ContentValues values, IContinue<Uri> callback){
        this.startInsert(0, callback, uri, values);
    }
    public void startInsert(int token, Object cookie, Uri uri, ContentValues values) {
    }
    //AsyncContentResolver asyncResolver = new AsyncContentResolver(context.getContentResolver());
    final static String CONTENT_URI = String.valueOf(BookContract.BOOK_TABLE_URI);
    public void persistAsync(final Book book) {
        ContentValues values = new ContentValues();
        /*book.writeToProvider(values);
        asyncResolver.insertAsync(CONTENT_URI, values, new IContinue<Uri>(){
            public void kontinue(Uri uri){
                 book.id = getId(uri);});*/
    }
}
