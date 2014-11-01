package edu.ustc.sse.mix;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MixContentProvider extends ContentProvider {
	
	@Override
	public boolean onCreate() {
		return false;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return getSQLiteDatabase().rawQuery("select * from person order by " + sortOrder, null);
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		getSQLiteDatabase().insert("person", null, values);
		
		return uri;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return getSQLiteDatabase().delete("person", selection, selectionArgs);
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return getSQLiteDatabase().update("person", values, selection, selectionArgs);
	}
	
	private SQLiteDatabase getSQLiteDatabase() {
		
		DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
		
		return databaseHelper.getReadableDatabase();
	}
}
