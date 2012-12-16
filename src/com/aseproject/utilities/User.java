package com.aseproject.utilities;

import java.io.IOException;
import java.sql.Timestamp;

import com.aseproject.login.UserAuth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class User {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_EMAIL = "email";	
	private static final String DATABASE_NAME = "Users";
	private static final String DATABASE_TABLE = "usersTable";
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
						+ KEY_PASSWORD + " TEXT NOT NULL, "
						+ KEY_EMAIL + " TEXT NOT NULL);" 					
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	public User(Context c) {
		ourContext = c;
	}
	
	public User open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this; 
	}
	
	public void close() {
		ourHelper.close();
	}

	public long createEntry(String name, String password, String email) {
		// TODO Auto-generated method stub
		WebServiceConnector myConnector = new WebServiceConnector();
		ContentValues cv = new ContentValues();
		cv.put(KEY_USERNAME, name);
		cv.put(KEY_PASSWORD, password);
		cv.put(KEY_EMAIL, email);
		try {
			myConnector.getLoginResponse(name, password, email);
			//System.out.println(myConnector.getResponse(name, password, email).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	public String getData() {
		String[] columns = new String[]{ KEY_ROWID, KEY_USERNAME, KEY_PASSWORD, KEY_EMAIL };		
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);		
		String result = "";
		
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iUsername = c.getColumnIndex(KEY_USERNAME);
		int iPassword = c.getColumnIndex(KEY_PASSWORD);
		int iEmail = c.getColumnIndex(KEY_EMAIL);
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iRow) + " " + c.getString(iUsername) + " " + c.getString(iPassword) + " " + c.getString(iEmail) + "\n";
		}		
		System.out.println(result);
		c.close();
		return result;
	}
	
	public boolean checkUserRegistration(String username, String password, String email) {
		WebServiceConnector myConnector = new WebServiceConnector();
    		try {
				if(myConnector.getLoginResponse(username, password, email).equals("RegisterTrue")) {
					return true;
				} else {
					return false;
				}					
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}  
	}
	
	public boolean updateUserInfo(String username, String firstName, String lastName, String gender, int day, int month, int year, String image, int visibility) {
		WebServiceConnector myConnector = new WebServiceConnector();
		try {
				System.out.println(myConnector.getUpdateResponse(username, firstName, lastName, gender, day, month, year, image, visibility));
				return true;						
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}
	
	public boolean deleteUserInfo(String username) {
		WebServiceConnector myConnector = new WebServiceConnector();
		try {
			if(myConnector.getDeleteResponse(username).equals("true")) {
				System.out.println(myConnector.getDeleteResponse(username));
				return true;
			} else {
				System.out.println(myConnector.getDeleteResponse(username));
				return false;
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean deleteCheckIn(String username, Timestamp date) {
		WebServiceConnector myConnector = new WebServiceConnector();
		try {
			if(myConnector.getDeleteCheckInResponse(username, date).equals("true")) {
				System.out.println(myConnector.getDeleteCheckInResponse(username, date));
				return true;
			} else {
				return false;
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteReview(String username, Timestamp date) {
		WebServiceConnector myConnector = new WebServiceConnector();
		try {
			if(myConnector.getDeleteReviewResponse(username, date).equals("true")) {
				System.out.println(myConnector.getDeleteReviewResponse(username, date));
				return true;
			} else {
				return false;
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setVisibility(String username, int visibility) {
		WebServiceConnector myConnector = new WebServiceConnector();
		try {
			if(myConnector.getVisibilityChangeResponse(username, visibility).equals("true")) {
				System.out.println(myConnector.getVisibilityChangeResponse(username, visibility));
				return true;
			} else {
				System.out.println(myConnector.getVisibilityChangeResponse(username, visibility));
				return false;
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	
	public UserAuth retrieveProfileInfo(String username) {
		WebServiceConnector myConnector = new WebServiceConnector();
		UserAuth user = new UserAuth();
		try {
			user = myConnector.getRetrieveProfileResponse(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;		
	}
	
	
	public boolean checkUserLogin(String username, String password) {
		
		WebServiceConnector myConnector = new WebServiceConnector();
		//Cursor myCursor = ourDatabase.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
												//new String[]{username, password});
		//if(myCursor != null) {
			//if(myCursor.getCount() > 0) {
				try {
					if(myConnector.getLoginResponse(username, password, null).equals("LoginTrue")) {
						System.out.println(myConnector.getLoginResponse(username, password, ""));
						return true;
					} else {
						System.out.println(myConnector.getLoginResponse(username, password, ""));
						return false;
					}
						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
				//return true;
			//}
		//}				
	}	
	
	public String getUsername(String usernameFromTextField) {
			String result = "";
		    Cursor c=ourDatabase.rawQuery("SELECT "+ KEY_USERNAME + " FROM " + DATABASE_TABLE + " WHERE " + KEY_USERNAME 
				   + " =" + "'" + usernameFromTextField + "'",new String [] {});		    			    
		    	
	    		int iUsername = c.getColumnIndex(KEY_USERNAME);
	    		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
	    			result = c.getString(iUsername);
	    		}
	    		c.close();
		   return result;																			
	}
	
	public void clearDb() {		
		String delete = "DELETE FROM " + DATABASE_TABLE;
		ourDatabase.execSQL(delete);				
	}
	
}