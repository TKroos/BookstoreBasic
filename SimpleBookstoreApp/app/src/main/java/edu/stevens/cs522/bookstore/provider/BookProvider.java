package edu.stevens.cs522.bookstore.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.sql.SQLException;

import edu.stevens.cs522.bookstore.contracts.BookContract;

/**
 * Created by Xiang on 2015/2/21.
 */
public class BookProvider extends ContentProvider {

    // Create the constants used to differentiate
    // between the different URI requests.
    private static final int ALL_ROWS_BOOK = 1;
    private static final int SINGLE_ROW_BOOK = 2;
    private static final int ALL_ROWS_AUTHOR = 3;
    private static final int SINGLE_ROW_AUTHOR = 4;
    // Used to dispatch operation based on URI
    private static final UriMatcher uriMatcher;
    public static final String DATABASE_NAME = "Cart.db";
    public static final String BOOK_TABLE = "Books";
    public static final String AUTHOR_TABLE = "Authors";
    public static final int DATABASE_VERSION = 1;
    // The index (key) column name for use in where clauses.
    public static final String _ID = "_id";
    // Name and column index of each column in your database
    public static final String TITLE = "title";
    public static final String AUTHORS = "authors";
    public static final String AUTHOR = "author";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";
    public static final String BOOK_FK = "book_fk";
// uriMatcher.addURI(AUTHORITY, CONTENT_PATH, OPCODE)
    static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(BookContract.AUTHORITY, "Books", ALL_ROWS_BOOK);
    uriMatcher.addURI(BookContract.AUTHORITY, BookContract.BOOK_PATH_ITEM, SINGLE_ROW_BOOK);
    uriMatcher.addURI(BookContract.AUTHORITY, BookContract.AUTHOR_PATH, ALL_ROWS_AUTHOR);
    uriMatcher.addURI(BookContract.AUTHORITY, BookContract.AUTHOR_PATH_ITEM, SINGLE_ROW_AUTHOR);
    }
    public
    String getType (Uri _uri){
        switch (uriMatcher.match(_uri)){
            case ALL_ROWS_BOOK:
                return "book";
            case SINGLE_ROW_BOOK:
                return "book";
            case ALL_ROWS_AUTHOR:
                return "author";
            case SINGLE_ROW_AUTHOR:
                return "author";
            default:
                throw new IllegalArgumentException("Unsupported URI:" + _uri);
        }
    }

    private static final String DATABASE_CREATE2 =
            "create table " + AUTHOR_TABLE + " ("
                    + _ID + " integer primary key autoincrement, "
                    + AUTHOR + " text not null, "
                    + BOOK_FK + " integer not null, foreign key (book_fk) references Books(_id) on delete cascade);";
    //+ BOOK_FK + " integer not null);"; // If the project doesn't work properly, please delete the code last row and use the code this row!!It should work normally!!Thanks!
    private static final String DATABASE_CREATE1 =
            "create table " + BOOK_TABLE + " ("
                    + _ID + " integer primary key autoincrement, "
                    + TITLE + " text not null, "
                    + AUTHORS + " text not null, "
                    + ISBN + " text not null, "
                    + PRICE + " text not null);";
    private SQLiteDatabase db;
    //private final Context context = null;

    //A private static inner class called DatabaseHelper
    //You create a subclass implementing onCreate(SQLiteDatabase), onUpgrade(SQLiteDatabase, int, int) and optionally onOpen(SQLiteDatabase),
    // and this class takes care of opening the database if it exists, creating it if it does not, and upgrading it as necessary
    protected static final class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE1);
            _db.execSQL(DATABASE_CREATE2);
            _db.execSQL("create index AuthorsBooksIndex on Authors(book_fk);");
        }

        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            Log.w("CartDbAdapter", "Upgrading from version " + _oldVersion + "to " + _newVersion);
            _db.execSQL("DROP TABLE IF EXISTS " + BOOK_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS " + AUTHOR_TABLE);
            onCreate(_db);
        }
    }
    private DatabaseHelper dbHelper;
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        return true;
    }
    public BookProvider open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    };
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        Cursor cursor;
        String _selection;
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS_BOOK:
                //query the database
                cursor = db.query(BOOK_TABLE, new String[] {_ID, TITLE, AUTHORS, ISBN, PRICE}, null, null, null, null, null);
                return cursor;
            case SINGLE_ROW_BOOK:
                //query the database
                cursor = db.query(BOOK_TABLE, new String[] {_ID, TITLE, AUTHORS, ISBN, PRICE}, selection, selectionArgs, null, null, null);
                cursor.moveToFirst();
                return cursor;
            case ALL_ROWS_AUTHOR:
                //query the database
                cursor = db.query(AUTHOR_TABLE, new String[] {_ID, AUTHOR, BOOK_FK}, selection, selectionArgs, null, null, null);
                return cursor;
            case SINGLE_ROW_AUTHOR:
                cursor = db.query(AUTHOR_TABLE, new String[] {_ID, AUTHOR, BOOK_FK}, selection, selectionArgs, null, null, null);
                cursor.moveToFirst();
                return cursor;
            default: throw new IllegalArgumentException ("Unsupported URI:" + uri);
        }
    }
    @Override
    public Uri insert(Uri uri, ContentValues values){
        long row = 0;
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case ALL_ROWS_BOOK:
                row = db.insert(BOOK_TABLE, null, values);
                String title = values.getAsString(BookContract.TITLE);
                Cursor cursor = db.query(BOOK_TABLE, new String[]{_ID, TITLE, AUTHORS, ISBN, PRICE}, TITLE + "=?", new String[]{title}, null, null, null);
                cursor.moveToFirst();
                String id = BookContract.getID(cursor);
                String authors = values.getAsString(BookContract.AUTHORS);
                String[] author = authors.split("/");
                for (int i = 0; i < author.length; i++){
                    ContentValues authorValues = new ContentValues();
                    BookContract.putID(authorValues, null);
                    BookContract.putAuthor(authorValues, author[i]);
                    BookContract.putBookFK(authorValues, id);
                    db.insert(AUTHOR_TABLE, null, authorValues);
                }
                if(row > 0){
                    Uri instanceUri = Uri.withAppendedPath(BookContract.BOOK_TABLE_URI, String.valueOf(row));
                    ContentResolver cr = getContext().getContentResolver();
                    cr.notifyChange(instanceUri, null);
                    return instanceUri;
                }
            case ALL_ROWS_AUTHOR:
                row = db.insert(AUTHOR_TABLE, null, values);
                if(row > 0){
                    Uri instanceUri = Uri.withAppendedPath(BookContract.AUTHOR_TABLE_URI, String.valueOf(row));
                    ContentResolver cr = getContext().getContentResolver();
                    cr.notifyChange(instanceUri, null);
                    return instanceUri;
                }
            default: throw new IllegalArgumentException ("Unsupported URI:" + uri);
        }
    }
    @Override
    public int delete(Uri uri, String where, String[] whereArgs){
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case ALL_ROWS_BOOK:
                return db.delete(BOOK_TABLE, null, null);
            case SINGLE_ROW_BOOK:
                return db.delete(BOOK_TABLE, where + "=?", whereArgs);
            case ALL_ROWS_AUTHOR:
                return db.delete(AUTHOR_TABLE, null, null);
            case SINGLE_ROW_AUTHOR:
                return db.delete(AUTHOR_TABLE, where + "=?", whereArgs);
            default: throw new IllegalArgumentException ("Unsupported URI:" + uri);
        }
    }
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs){
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case ALL_ROWS_BOOK:
            case SINGLE_ROW_BOOK:
            case ALL_ROWS_AUTHOR:
            case SINGLE_ROW_AUTHOR:
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
    }
}