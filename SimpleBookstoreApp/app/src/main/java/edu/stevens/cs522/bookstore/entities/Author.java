package edu.stevens.cs522.bookstore.entities;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 */
public class Author implements Parcelable{

    // TODO Modify this to implement the Parcelable interface.

    // NOTE: middleInitial may be NULL!
    public static final String _ID = "_id";
    // Name and column index of each column in your database
    public static final String BOOK_FK = "book_fk";
    public static final String AUTHOR = "author";
    public String firstName;

    public String middleInitial;

    public String lastName;
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(firstName);
        if (middleInitial.length() == 0)
            out.writeString(middleInitial);
        out.writeString(lastName);
    }

    public Author (String author){// A constructor of Author
        String[] temp = author.split(" ");
        if (temp.length == 1){
            this.firstName = temp[0];
            this.middleInitial = "";
            this.lastName = "";
        }
        if (temp.length == 2){
            this.firstName = temp[0];
            this.middleInitial = "";
            this.lastName = temp[1];
        }
        if (temp.length == 3) {
            this.firstName = temp[0];
            this.middleInitial = temp[1];
            this.lastName = temp[2];
        }
    }

    public void writeToProvider(ContentValues contentValues, int book_fk){
        contentValues.put(_ID, (byte[]) null);
        contentValues.put(AUTHOR, this.firstName + " " + this.middleInitial + " " +this.lastName);
        contentValues.put(BOOK_FK, book_fk);
    }


    private Author(Parcel in)
    {
        firstName = in.readString();
        /*if (in.dataSize() == 2)
            middleInitial = in.readString();
        if (in.dataSize() == 1)
            lastName = in.readString();*/
        if (in.dataSize() > 2)
            middleInitial = in.readString();
        lastName = in.readString();
    }

    public static final Parcelable.Creator<Author> CREATOR = new Parcelable.Creator<Author>() {
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

}