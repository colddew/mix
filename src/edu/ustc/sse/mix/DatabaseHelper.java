package edu.ustc.sse.mix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static String DB_NAME = "mix.db";
	private static int VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists person (personid integer primary key autoincrement, age integer, name varchar(20))");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists person");
		onCreate(db);
	}
}