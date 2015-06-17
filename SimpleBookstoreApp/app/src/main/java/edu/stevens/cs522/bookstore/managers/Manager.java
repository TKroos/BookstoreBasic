package edu.stevens.cs522.bookstore.managers;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

/**
 * Created by Xiang on 2015/2/27.
 */
public abstract class Manager<T> {
    private ContentResolver syncResolver;
    private AsyncContentResolver asyncResolver;
    private Context context = this.context;
    protected ContentResolver getSyncResolver() {
        if (syncResolver == null)
            syncResolver = context.getContentResolver();
        return syncResolver;
    }

    protected AsyncContentResolver getAsyncResolver() {
        if (asyncResolver == null)
            asyncResolver = new AsyncContentResolver(context.getContentResolver());
        return asyncResolver;
    }

    protected void executeSimpleQuery(Uri uri, SimpleQueryBuilder.ISimpleQueryListener<T> listener) {
        //SimpleQueryBuilder.executeQuery((Activity) context, uri, creator, listener);
    }

    protected void executeSimpleQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, SimpleQueryBuilder.ISimpleQueryListener<T> listener) {
        //SimpleQueryBuilder.executeQuery((Activity) context, uri, projection, selection, selectionArgs, creator, listener);
    }

    protected void executeQuery(Uri uri, SimpleQueryBuilder.ISimpleQueryListener<T> listener) {
       // SimpleQueryBuilder.executeQuery(tag, (Activity) context, uri, loaderID, creator, listener);
    }

    protected void executeQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, SimpleQueryBuilder.ISimpleQueryListener<T> listener) {
        //SimpleQueryBuilder.executeQuery(tag, (Activity) context, uri, loaderID, projection, selection, selectionArgs, creator, listener);
    }

    protected void reexecuteQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, SimpleQueryBuilder.ISimpleQueryListener<T> listener) {
        //SimpleQueryBuilder.reexecuteQuery(tag, (Activity) context, uri, loaderID, projection, selection, selectionArgs, creator, listener);
    }
}