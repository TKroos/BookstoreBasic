package edu.stevens.cs522.bookstore.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import edu.stevens.cs522.bookstore.contracts.BookContract;

public class Book implements Parcelable{

    public int id;

    public String title;

    //public ArrayList<Author> authors = new ArrayList<Author>();;//Seperated by '/'
    public Author[] authors;

    public String allAuthors;

    public String isbn;

    public String price;

    public void authorConvert(String author){
        String[] temp1 = author.split("/");
        this.authors = new Author[temp1.length];
        for(int i = 0; i<temp1.length; i++){
            authors[i] = new Author(temp1[i]);
            /*String[] temp = author.split(" ");
            if (temp.length == 2){
                authors[i].firstName = new String(temp[0]);
                authors[i].lastName = new String(temp[1]);
            }
            if (temp.length == 3) {
                authors[i].firstName = new String(temp[0]);
                authors[i].middleInitial = new String(temp[1]);
                authors[i].lastName = new String(temp[2]);
            }*/
        }
    }
    public Book(int id, String title, String author, String isbn, String price) {
        this.id = id;
        this.title = title;
        this.allAuthors = author;
        this.authorConvert(allAuthors);
        this.isbn = isbn;
        this.price = price;
    }
    // A constructor that initializes the fields from an input cursor,
    public Book(Cursor cursor){
        this.id = (int)Integer.parseInt(BookContract.getID(cursor));
        this.title = BookContract.getTitle(cursor);
        this.allAuthors = BookContract.getAuthors(cursor);
        this.authorConvert(allAuthors);
        this.isbn = BookContract.getISBN(cursor);
        this.price = BookContract.getPrice(cursor);
    }
    //a method that writes the fields of the entity to a ContentValues object
    public void writeToProvider(ContentValues values){
        BookContract.putID(values, String.valueOf(id));
        BookContract.putTitle(values, title);
        BookContract.putAuthors(values, allAuthors);
        BookContract.putISBN(values, isbn);
        BookContract.putPrice(values, price);
    }
    // TODO Modify this to implement the Parcelable interface.
    @Override
    public int describeContents()
    {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(id);
        out.writeString(title);
    //    out.writeTypedList(authors);
        out.writeString(allAuthors);
        out.writeString(isbn);
        out.writeString(price);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>()
    {
        public Book[] newArray(int size)
        {
            return new Book[size];
        }


        public Book createFromParcel(Parcel in)
        {
            return new Book(in);
        }
    };

    public Book(Parcel in)
    {
        id = in.readInt();
        title = in.readString();
        allAuthors = in.readString();
        this.authorConvert(allAuthors);
        //in.readTypedList(authors, Author.CREATOR);
        isbn = in.readString();
        price = in.readString();
    }

    // TODO redefine toString() to display book title and price (why?).
    @Override
    public String toString()
    {
        return " BookName: "+title + " Price: " + price;
    }

}