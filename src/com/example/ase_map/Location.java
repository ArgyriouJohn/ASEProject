package com.example.ase_map;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Location {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_EMAIL = "email";
	
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
						+ KEY_ROWID + " INTEGER PRIMRAY KEY AUTOINCREMENT, "
						+ KEY_EMAIL + " TEXT NOT NULL, "
						+ KEY_LONGITUDE + " TEXT NOT NULL, "
						+ KEY_LATITUDE + " TEXT NOT NULL );"
						);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);			
		}		
	}	
	
	public Location(Context c) {
		ourContext = c;
	}
	
	public Location open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getReadableDatabase();
		return this;		
	}
	
	public void close() {
		ourHelper.close();
	}
	
	public long createEntry(String email, String longitude, String latitude) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_EMAIL, email);
		cv.put(KEY_LONGITUDE, longitude);
		cv.put(KEY_LATITUDE, latitude);
		/*try {
			//myConnector.getResponse(name, password, email);
			//System.out.println(myConnector.getResponse(name, password, email).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		*/
		return ourDatabase.insert(DATABASE_TABLE, null, cv);		
	}
	
	public String getData() {
		String[] columns = new String[]{ KEY_ROWID, KEY_EMAIL, KEY_LONGITUDE, KEY_LATITUDE};		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);		
		String result = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iEmail = c.getColumnIndex(KEY_EMAIL);
		int iLongitude = c.getColumnIndex(KEY_LONGITUDE);
		int iLatitude = c.getColumnIndex(KEY_LATITUDE);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iRow) + " " + c.getString(iEmail) + " " + c.getString(iLongitude) + " " + c.getString(iLatitude) + "\n";
		}		
		System.out.println(result);
		return result;
	}
	
	public void clearDb() {		
		String delete = "DELETE FROM " + DATABASE_TABLE;
		ourDatabase.execSQL(delete);				
	}
	

}
