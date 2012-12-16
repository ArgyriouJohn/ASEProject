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

/**
 * This class defines the methods that provide the required data and context to the WebServiceConnector class which then interacts with the server to perform the requested action. 
 * It also instantiates the local SQLite3 database that sits on the device and is used to store location values upon change.
 * @see WebServiceConnector 
 * @author Thanos Irodotou 2012
 */
public class User 
{	
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
	
	private static class DbHelper extends SQLiteOpenHelper 
	{
		public DbHelper(Context context) 
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
	    * This method creates and initialises the local sqlite database that is stored in the device. 
	    * @param db SQLiteDatabase
	    */
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" 
						+ KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ KEY_USERNAME + " TEXT NOT NULL, " 
						+ KEY_PASSWORD + " TEXT NOT NULL, "
						+ KEY_EMAIL + " TEXT NOT NULL);" 					
					);
		}

		/**
	    * This method upgrades the database and stores any changes. 
	    * @param db SQLiteDatabase
	    * @param oldVersion int
	    * @param newVersion int
	    */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	/**
    * This constructore creates a new User handler object by provides to it the current context.  
    * @param c Context
    */
	public User(Context c)
	{
		ourContext = c;
	}
	
	/**
    * This method opens a connection to the database by assigining the context and retrieving the writable database; 
    * @throws SQLException
    */
	public User open() throws SQLException 
	{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this; 
	}
	
	/**
    * This method closes the connection.   
    */
	public void close() 
	{
		ourHelper.close();
	}

	/**
    * This method creates and stores a new UserAuth entry on our remote database. This method is called when a new user registers, in order to save his/hers info on our database. The row id is returned. 
    * @param name String
    * @param password String
    * @param email String
    * @return rowId long
    */
	public long createEntry(String name, String password, String email) 
	{
		// TODO Auto-generated method stub
		WebServiceConnector myConnector = new WebServiceConnector();
		ContentValues cv = new ContentValues();
		cv.put(KEY_USERNAME, name);
		cv.put(KEY_PASSWORD, password);
		cv.put(KEY_EMAIL, email);
		try 
		{
			myConnector.getLoginResponse(name, password, email);
			//System.out.println(myConnector.getResponse(name, password, email).toString());
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	/**
    * This method performs a check of the user's credentials he/she used to register to see if such a user already exists. If everything comes up fine the user is registered.  
    * @param username String
    * @param password String
    * @param email String
    * @return boolean
    */
	public boolean checkUserRegistration(String username, String password, String email) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
    		try 
    		{
				if(myConnector.getLoginResponse(username, password, email).equals("RegisterTrue")) 
				{
					return true;
				} else 
				{
					return false;
				}					
			} catch (IOException e) 
			{
				e.printStackTrace();
				return false;
			}  
	}
	
	/**
    * This method sends an update request to the server by providing all the required information the user has inputted on his/hers profile. This method is called when a user edits and saves his/hers profile.
    * @param userName String
    * @param lastName String
    * @param gender String
    * @param day int
    * @param month int
    * @param year int
    * @param image String
    * @param visibility int
    * @return boolean
    */
	public boolean updateUserInfo(String username, String firstName, String lastName, String gender, int day, int month, int year, String image, int visibility) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
		try 
		{
			System.out.println(myConnector.getUpdateResponse(username, firstName, lastName, gender, day, month, year, image, visibility));
			return true;						
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
    * This method sends a delete request to the server by providing the user's username. That user is deleted and all his/hers info are removed. This method is called when a user selects to delete his/hers account from his/hers profile view.  
    * @param username String
    * @return boolean
    */
	public boolean deleteUserInfo(String username) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
		try 
		{
			if(myConnector.getDeleteResponse(username).equals("true")) 
			{				
				System.out.println(myConnector.getDeleteResponse(username));
				return true;
			} else 
			{
				System.out.println(myConnector.getDeleteResponse(username));
				return false;
			}				
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
    * This method sends a deleteCheckIn request to the server providing the user that checked in and the date that check in was performed.  
    * @param username String
    * @param date Timestamp
    * @return boolean
    */
	public boolean deleteCheckIn(String username, Timestamp date) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
		try 
		{
			if(myConnector.getDeleteCheckInResponse(username, date).equals("true")) 
			{
				System.out.println(myConnector.getDeleteCheckInResponse(username, date));
				return true;
			} else 
			{
				return false;
			}				
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
    * This method sends a deleteReview request to the server providing the user that wrote the review and the date that that review was submitted.  
    * @param username String
    * @param date Timestamp
    * @return boolean
    */
	public boolean deleteReview(String username, Timestamp date) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
		try 
		{
			if(myConnector.getDeleteReviewResponse(username, date).equals("true")) 
			{
				System.out.println(myConnector.getDeleteReviewResponse(username, date));
				return true;
			} else 
			{
				return false;
			}				
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
    * This method sends a setVisibility request to the server providing the user's username and the visibility state set by the user.  
    * @param username String
    * @param visibility int
    * @return boolean
    */
	public boolean setVisibility(String username, int visibility) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
		try 
		{
			if(myConnector.getVisibilityChangeResponse(username, visibility).equals("true")) 
			{
				System.out.println(myConnector.getVisibilityChangeResponse(username, visibility));
				return true;
			} 
			else 
			{
				System.out.println(myConnector.getVisibilityChangeResponse(username, visibility));
				return false;
			}				
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/**
    * This method sends a retriveProfileInfo request to the server providing just the user's username. All info stored for that user are retrieved and a new user object is constructed containing that info. That object is later returned.  
    * @param username String
    * @return user UserAuth
    */
	public UserAuth retrieveProfileInfo(String username) 
	{
		WebServiceConnector myConnector = new WebServiceConnector();
		UserAuth user = new UserAuth();
		try 
		{
			user = myConnector.getRetrieveProfileResponse(username);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;		
	}
		
	/**
    * This method sends checks the login credentials of a user by sending a request to the server to check that info. If the info is matched the user is logged in.   
    * @param username String
    * @param password String
    * @return boolean
    */
	public boolean checkUserLogin(String username, String password) 
	{		
		WebServiceConnector myConnector = new WebServiceConnector();
		try 
		{
			if(myConnector.getLoginResponse(username, password, null).equals("LoginTrue")) 
			{
				System.out.println(myConnector.getLoginResponse(username, password, ""));
				return true;
			} else {
				System.out.println(myConnector.getLoginResponse(username, password, ""));
				return false;
			}				
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}			
	}	
	
	/**
    * This method sends runs a raw query to the local sqlite3 database in order to retrieve the username of the user which is then returned as a String.  
    * @param usernameFromTextField String
    * @return result String
    */
	public String getUsername(String usernameFromTextField) 
	{
		String result = "";
	    Cursor c=ourDatabase.rawQuery("SELECT "+ KEY_USERNAME + " FROM " + DATABASE_TABLE + " WHERE " + KEY_USERNAME + " =" + "'" + usernameFromTextField + "'",new String [] {});		    			    
	    	
	    int iUsername = c.getColumnIndex(KEY_USERNAME);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) 
		{
			result = c.getString(iUsername);
		}
		c.close();
		
		return result;																			
	}
	
	/**
    * This method clears the local sqlite3 database. Mostly used for debugging and testing purposes.  
    */
	public void clearDb()
	{		
		String delete = "DELETE FROM " + DATABASE_TABLE;
		ourDatabase.execSQL(delete);				
	}
	
}