package com.aseproject.location;

import java.io.IOException;

import com.aseproject.utilities.WebServiceConnector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class defines provides the methods for creating the SQLite3 DB that sits locally on the device running the app. 
 * @author Thanos Irodotou 2012
 */
public class LocationsHandling 
{
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
	
	private static class DbHelper extends SQLiteOpenHelper 
	{
		
		public DbHelper(Context context) 
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" 
					+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ KEY_USERNAME + " TEXT NOT NULL, "
					+ KEY_LONGITUDE + " FLOAT, " 
					+ KEY_LATITUDE + " FLOAT);" 					
				);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);			
		}		
	}	
	
	/**
    * This method provides the Context required for the handler to work. 
    * @param c Context.
    */
	public LocationsHandling(Context c) 
	{
		ourContext = c;
	}
	
	/**
    * This method initialises the connection to the sqlite3 database by getting the Readable Database at each instance. 
    * @return this.
    */
	public LocationsHandling open() throws SQLException 
	{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getReadableDatabase();
		return this;		
	}
	
	/**
    * This method closes the connection to the sqlite3 database.    
    */
	public void close() 
	{
		ourHelper.close();
	}
	
	/**
    * This method receives the user's username and the changes on location values from the local sqlite3 db and creates a new record in the remote MySQL database, transfering the latest values from the GPS receiver. 
    * @param username user's username
    * @param longitude location's longitude
    * @param latitude location's latitude
    * @return rowID of the newly created row.
    */
	public long createEntry(String username, Double longitude, Double latitude) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
		ContentValues cv = new ContentValues();
		cv.put(KEY_USERNAME, username);
		cv.put(KEY_LONGITUDE, longitude);
		cv.put(KEY_LATITUDE, latitude);
		try 
		{
			//myConnector.getResponse(username, longitude, latitude);
			System.out.println(myConnector.getLocResponse(username, longitude, latitude).toString());
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return ourDatabase.insert(DATABASE_TABLE, null, cv);		
	}
	
	/**
    * This method is called whenever the location on the device changes. It creates a local record kept within the device's sqlite3 db file.
    * @param username user's username
    * @param longitude location's longitude
    * @param latitude location's latitude 
    * @return rowID of the newly created row.
    */
	public long createLocalEntry(String username, Double longitude, Double latitude) 
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_USERNAME, username);
		cv.put(KEY_LONGITUDE, longitude);
		cv.put(KEY_LATITUDE, latitude);
		
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	
	/**
    * This method retrieves the information stored in the local device depended sqlite3 database, by creating a cursor that moves through each and saves the stored values in a String result variable.  
    * @return result.
    */
	public String getData() 
	{
		String[] columns = new String[]{ KEY_ROWID, KEY_USERNAME, KEY_LONGITUDE, KEY_LATITUDE};		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);		
		String result = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iUsername = c.getColumnIndex(KEY_USERNAME);
		int iLongitude = c.getColumnIndex(KEY_LONGITUDE);
		int iLatitude = c.getColumnIndex(KEY_LATITUDE);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) 
		{
			result = result + c.getString(iRow) + " " + c.getString(iUsername) + " " + c.getString(iLongitude) + " " + c.getString(iLatitude) + "\n";
		}		
		//System.out.println(result);
		return result;
	}
	
	/**
    * This method performs a delete query to the sqlite3 database, wiping all its records. It was mostly used to wipe clean after testing.    
    */
	public void clearDb() 
	{		
		String delete = "DELETE FROM " + DATABASE_TABLE;
		ourDatabase.execSQL(delete);				
	}
}
