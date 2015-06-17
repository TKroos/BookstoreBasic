package edu.stevens.cs522.bookstore.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.activities.BookStoreActivity;
import edu.stevens.cs522.bookstore.contracts.BookContract;

public class BookAdapter extends ResourceCursorAdapter {
    protected final static int ROW_LAYOUT = R.layout.cart_row;

    public BookAdapter(Context context, Cursor cursor) {
        super(context, ROW_LAYOUT, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cur, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(ROW_LAYOUT, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //view = newView(context, cursor, null);
        TextView titleLine = (TextView) view.findViewById(R.id.cart_row_title);
        TextView authorLine = (TextView) view.findViewById(R.id.cart_row_author);
        //cursor.moveToFirst();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(BookContract.TITLE));
        String[] temp = cursor.getString(cursor.getColumnIndexOrThrow(BookContract.AUTHORS)).split("/");
        titleLine.setText(title);
        authorLine.setText(temp[0]);
    }
}