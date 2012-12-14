package com.example.ase_map;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationStuff {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_USERNAME = "username";
	
	private static final String DATABASE_NAME = "Locations";
	private static final String DATABASE_TABLE = "locationsTable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper {
		
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" 
					+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_USERNAME + " TEXT NOT NULL, "
					+ KEY_LONGITUDE + " FLOAT, " 
					+ KEY_LATITUDE + " FLOAT);" 					
				);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);			
		}		
	}	
	
	public LocationStuff(Context c) {
		ourContext = c;
	}
	
	public LocationStuff open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getReadableDatabase();
		return this;		
	}
	
	public void close() {
		ourHelper.close();
	}
	
	public long createEntry(String username, Double longitude, Double latitude) {
		WebServiceConnector myConnector = new WebServiceConnector();
		ContentValues cv = new ContentValues();
		cv.put(KEY_USERNAME, username);
		cv.put(KEY_LONGITUDE, longitude);
		cv.put(KEY_LATITUDE, latitude);
		try {
//			myConnector.getResponse(username, longitude, latitude);
			System.out.println(myConnector.getResponse(username, longitude, latitude).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return ourDatabase.insert(DATABASE_TABLE, null, cv);		
	}		
	
	public String getData() {
		String[] columns = new String[]{ KEY_ROWID, KEY_USERNAME, KEY_LONGITUDE, KEY_LATITUDE};		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);		
		String result = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iUsername = c.getColumnIndex(KEY_USERNAME);
		int iLongitude = c.getColumnIndex(KEY_LONGITUDE);
		int iLatitude = c.getColumnIndex(KEY_LATITUDE);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iRow) + " " + c.getString(iUsername) + " " + c.getString(iLongitude) + " " + c.getString(iLatitude) + "\n";
		}		
		System.out.println(result);
		return result;
	}
	
	
	public void clearDb() {		
		String delete = "DELETE FROM " + DATABASE_TABLE;
		ourDatabase.execSQL(delete);				
	}
	

}
