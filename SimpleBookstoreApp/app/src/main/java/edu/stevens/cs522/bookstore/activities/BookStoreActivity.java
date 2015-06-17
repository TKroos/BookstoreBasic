package edu.stevens.cs522.bookstore.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.stevens.cs522.bookstore.R;
import edu.stevens.cs522.bookstore.adapter.BookAdapter;
import edu.stevens.cs522.bookstore.contracts.BookContract;
import edu.stevens.cs522.bookstore.entities.Book;
import edu.stevens.cs522.bookstore.managers.AsyncContentResolver;

public class BookStoreActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	// Use this when logging errors and warnings.
	@SuppressWarnings("unused")
	private static final String TAG = BookStoreActivity.class.getCanonicalName();// Name of the class
	
	// These are request codes for subactivity request calls
	static final private int ADD_REQUEST = 1;

    private static final int BOOKS_LOADER_ID = 1;
    private static final int AUTHORS_LOADER_ID = 2;
	@SuppressWarnings("unused")
	static final private int CHECKOUT_REQUEST = ADD_REQUEST + 1;

	// There is a reason this must be an ArrayList instead of a List.
	@SuppressWarnings("unused")
	private ArrayList<Book> shoppingCart = new ArrayList<Book>();

    private String savedInstanceStateID = "savedInstanceStateID";

    //private MyAdapter adapter;

    //private CartDbAdapter myAdapter;

    private ContentResolver contentResolver;

    public static CursorAdapter adapter;

    AsyncContentResolver queryHandler;

    //private Cursor cursor;

    //private ActionMode mActionMode;

    private ListView listView;

    private Book book;

    LoaderManager lm;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
		// TODO check if there is saved UI state, and if so, restore it (i.e. the cart contents)
        // Check whether we're recreating a previously destroyed instance
        /*if (savedInstanceState != null) {
            // Restore value of members from saved state
            shoppingCart = savedInstanceState.getParcelableArrayList(savedInstanceStateID);
        }
        Log.i("Restore", "after RestoreInstanceState shoppingcart size:" + shoppingCart.size());*/
        contentResolver = this.getContentResolver();
        queryHandler = new AsyncContentResolver(this.getContentResolver());
        /*cursor = provider.query(BookContract.BOOK_TABLE_URI, null, null, null, null);
        startManagingCursor(cursor);*/
        listView = getListView();
        adapter = new BookAdapter(this, null);// Initially no cursor
        setListAdapter(adapter);
        //setListShown(false);
        lm = getLoaderManager();
        lm.initLoader(BOOKS_LOADER_ID, null, BookStoreActivity.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = Uri.withAppendedPath(BookContract.BOOK_TABLE_URI, String.valueOf(l));
                book = new Book(contentResolver.query(uri, null, BookContract._ID + "=?", new String[]{String.valueOf(l)}, null));
                new AlertDialog.Builder(BookStoreActivity.this)
                        .setTitle("Book Detail")
                        .setMessage("ID: " + book.id + "\r\n"
                                + "Title: " + book.title + "\r\n"
                                + "Author: " + book.allAuthors + "\r\n"
                                + "ISBN: " + book.isbn + "\r\n"
                                + "Price: " + book.price)
                        .setPositiveButton("Confirm", null)
                        .show();
            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private ArrayList<Book> books = new ArrayList<Book>();
            public void onItemCheckedStateChanged(ActionMode m, int pos, long id, boolean checked){
                //Do something when items are selected/de-selected,
                if (checked == true) {
                    Uri uri = Uri.withAppendedPath(BookContract.BOOK_TABLE_URI, String.valueOf(id));
                    Cursor c = contentResolver.query(uri, null, BookContract._ID + "=?", new String[]{String.valueOf(id)}, null);
                    Book book = new Book(c);
                    books.add(book);
                }
                if (!checked){
                    Uri uri = Uri.withAppendedPath(BookContract.BOOK_TABLE_URI, String.valueOf(id));
                    Cursor c = contentResolver.query(uri, null, BookContract._ID + "=?", new String[]{String.valueOf(id)}, null);
                    Book book = new Book(c);
                    int index = 0;
                    for (int i = 0; i < books.size(); i++)
                        if (books.get(i).id == book.id) {
                            index =i;
                        }
                    books.remove(index);
                }
                if(listView.getCheckedItemCount() == 1)
                    m.setSubtitle("1 item selected");
                else
                    m.setSubtitle(listView.getCheckedItemCount() + " items selected");
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.delete:
                        Uri uri = Uri.withAppendedPath(BookContract.BOOK_TABLE_URI, "1");
                        for(int i = 0; i < books.size(); i++ ) {
                            contentResolver.delete(uri, BookContract._ID, new String[]{String.valueOf(books.get(i).id)});
                        }
                        lm.restartLoader(BOOKS_LOADER_ID, null, BookStoreActivity.this);
                        mode.finish();
                        return true;
                    /*case R.id.authors:
                        Intent intent = new Intent(BookStoreActivity.this, ShowAuthorsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(BookContract._ID, book.id);
                        intent.putExtras(bundle);
                        startActivity(intent, bundle);
                        mode.finish();
                        return true;*/
                    default:
                        return false;
                }
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "Action Mode Closed", Toast.LENGTH_SHORT).show();
                //mActionMode = null;
            }
            // Called when the action mode is created; startActionMode() was called
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.contextual_action_bar, menu);
                return true;
            }
            // Called when the user selects a contextual menu item

        });
        //registerForContextMenu(getListView());
	}
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        CursorLoader returnValue = null;
        switch (loaderID) {
            case BOOKS_LOADER_ID:
                returnValue = new CursorLoader(BookStoreActivity.this, BookContract.BOOK_TABLE_URI, null, null, null, null);
                break;
            case AUTHORS_LOADER_ID:
                returnValue = new CursorLoader(BookStoreActivity.this, BookContract.AUTHOR_TABLE_URI, null, null, null, null);
                break;
            default:
                break;    //An invalid id was passed in
        }
        return returnValue;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c){
        switch(loader.getId()) {
            case BOOKS_LOADER_ID:
                this.adapter.swapCursor(c);
                break;
            case AUTHORS_LOADER_ID:
                this.adapter.swapCursor(c);
                break;
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        this.adapter.swapCursor(null);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// TODO provide ADD, DELETE and CHECKOUT options
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookstore_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// TODO
        switch (item.getItemId()) {
            // ADD provide the UI for adding a book
            case R.id.add:
                Intent addIntent = new Intent(this, AddBookActivity.class);
                startActivityForResult(addIntent, ADD_REQUEST);
                return true;
            case R.id.checkout:
                Intent checkoutIntent = new Intent(this, CheckoutActivity.class);
                startActivityForResult(checkoutIntent, CHECKOUT_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
		//return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// TODO Handle results from the Search and Checkout activities.
        //adapter.swapCursor(cursor);
        //MyAdapter adapter = new MyAdapter(this, shoppingCart);
		// Use SEARCH_REQUEST and CHECKOUT_REQUEST codes to distinguish the cases.
		switch (requestCode) {
            // SEARCH: add the book that is returned to the shopping cart.
            case (ADD_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = intent.getExtras();
                    book = bundle.getParcelable(AddBookActivity.BOOK_RESULT_KEY);
                    ContentValues bookValues = new ContentValues();
                    book.writeToProvider(bookValues);
                    try {
                        contentResolver.insert(BookContract.BOOK_TABLE_URI, bookValues);
                    }
                    catch (Exception e){
                        Log.e("persist: ", "error");
                    }
                    lm.restartLoader(BOOKS_LOADER_ID, null, BookStoreActivity.this);
                    //shoppingCart.add(book);
                    //setListAdapter(adapter);
                }
                break;

            // CHECKOUT: empty the shopping cart.
            case (CHECKOUT_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    //shoppingCart.clear();
                    contentResolver.delete(BookContract.BOOK_TABLE_URI, null, null);
                    lm.restartLoader(BOOKS_LOADER_ID, null, BookStoreActivity.this);
                    //setListAdapter(adapter);
                    new AlertDialog.Builder(this)
                            .setTitle("Order Information")
                            .setMessage("Thanks for Your Order!")
                            .setPositiveButton("Confirm", null)
                            .show();
                }
                break;
        }
    }

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO save the shopping cart contents (which should be a list of parcelables).
        savedInstanceState.putParcelableArrayList(savedInstanceStateID, shoppingCart);
        Log.i("Save", "after saveinstancestate shoppingcart size:" + savedInstanceState.size());
        super.onSaveInstanceState(savedInstanceState);
	}
    // 写一个异步查询类
    private final class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            // 更新mAdapter的Cursor
            adapter.changeCursor(cursor);
        }
    }
}