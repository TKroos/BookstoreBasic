package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Author;
import edu.stevens.cs522.bookstore.entities.Book;

public class AddBookActivity extends Activity {
	
	// Use this as the key to return the book details as a Parcelable extra in the result intent.
	public static final String BOOK_RESULT_KEY = "book_result";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_book);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO provide SEARCH and CANCEL options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
		switch (item.getItemId()) {
            // SEARCH: return the book details to the BookStore activity
            case R.id.search:
                Book book = searchBook();
                Intent intent = new Intent(this, BookStoreActivity.class);
                /*Bundle bundle = new Bundle();
                bundle.putParcelable(BOOK_RESULT_KEY, book);*/
                intent.putExtra(BOOK_RESULT_KEY, book);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;

            // CANCEL: cancel the search request
            case R.id.cancel:
                setResult(Activity.RESULT_CANCELED, null);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
		//return false;
	}
	
	public Book searchBook() {
		/*
		 * Search for the specified book.
		 */
        // TODO Just build a Book object with the search criteria and return that.
        EditText editText = (EditText) findViewById(R.id.search_title);
        String title = editText.getText().toString();
        editText = (EditText) findViewById(R.id.search_author);
        String author = editText.getText().toString();
        /*String[] temp1 = author.split(",");
        int author_number = temp1.length;
        ArrayList<Author> authors = new ArrayList<Author>(author_number);
        Author temp;
        for (int i = 0; i<author_number; i++){
            temp = new Author(temp1[i]);
            authors.add(temp);
            *//*String[] temp2 = temp1[i].split(" ");
            if (temp2.length == 2){
                authors[i].firstName = temp2[0];
                authors[i].lastName = temp2[1];
            }
            if (temp2.length == 3){
                authors[i].firstName = temp2[0];
                authors[i].middleInitial = temp2[1];
                authors[i].lastName = temp2[2];
            }*//*
        }*/
        editText = (EditText) findViewById(R.id.search_isbn);
        String isbn = editText.getText().toString();
        editText = (EditText) findViewById(R.id.search_price);
        String price = editText.getText().toString();
        //Random random = new Random();
        //Integer price = random.nextInt(80);
        Book book = new Book(1, title, author, isbn, price);
        return book;
    }
}