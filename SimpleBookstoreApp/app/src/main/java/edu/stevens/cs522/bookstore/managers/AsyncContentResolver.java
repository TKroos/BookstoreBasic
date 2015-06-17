package edu.stevens.cs522.bookstore.managers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import edu.stevens.cs522.bookstore.activities.BookStoreActivity;

/**
 * Created by Xiang on 2015/2/27.
 */
public final class AsyncContentResolver extends AsyncQueryHandler {
        public AsyncContentResolver(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor c) {
            super.onQueryComplete(token, cookie, c);
            BookStoreActivity.adapter.swapCursor(c);
        }
        protected void onInsertComplete(int token, Object cookie, Uri uri) {
            super.onInsertComplete(token, cookie, uri);
        }

        @Override
        protected void onUpdateComplete(int token, Object cookie, int result) {
            super.onUpdateComplete(token, cookie, result);
        }

        @Override
        protected void onDeleteComplete(int token, Object cookie, int result) {
            super.onDeleteComplete(token, cookie, result);
        }

}
