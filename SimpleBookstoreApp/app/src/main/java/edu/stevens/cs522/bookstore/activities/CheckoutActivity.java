package edu.stevens.cs522.bookstore.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.entities.Book;

public class CheckoutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO display ORDER and CANCEL options.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.checkout_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
        switch (item.getItemId()) {
            // ORDER: display a toast message of how many books have been ordered and return
            case R.id.order:
                setResult(Activity.RESULT_OK, null);
                finish();
                return true;
            // CANCEL: just return with REQUEST_CANCELED as the result code
            case R.id.cancel:
                setResult(Activity.RESULT_CANCELED, null);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}
	
}