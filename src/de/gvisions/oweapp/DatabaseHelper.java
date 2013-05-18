package de.gvisions.oweapp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Context context;
	
	public DatabaseHelper(Context fragmentList) {
		super(
		        fragmentList,
		        fragmentList.getResources().getString(R.string.dbname),
		        null,
		        Integer.parseInt(fragmentList.getResources().getString(R.string.version)));
		    this.context=fragmentList;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for(String sql : context.getResources().getStringArray(R.array.create))
		      db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		for(String sql : context.getResources().getStringArray(R.array.update_1))
		      db.execSQL(sql);

	}

}
