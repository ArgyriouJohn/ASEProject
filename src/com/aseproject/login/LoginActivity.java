package com.aseproject.login;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SyncStateContract.Constants;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aseproject.map.MainActivity;
import com.aseproject.map.R;
import com.aseproject.utilities.AseMapApplication;
import com.aseproject.utilities.User;
import com.aseproject.utilities.WebServiceConnector;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.ProfilePictureView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;

/**
 * This class creates an CheckIn object used to store and retrieve checkin information from the server.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 * @author Socratis Michaelides 2012
 */
public class LoginActivity extends FacebookActivity implements OnClickListener 
{
	Button sqlRegister, sqlView, sqlLogin, sqlDelete, logOut;
	EditText sqlUsername, sqlPassword, sqlEmail;	
    String TAG = "DEBUG";
    //variables used for connecting with facebook login
	ImageButton sqlFblogin;
	LinearLayout fb;
	AseMapApplication app;
	private ProfilePictureView profilePictureView;
    Bitmap bmap;
    String name;
	
    /**
     * This method is initializes and sets the variables within the view. It is a constructor of a view.
     * @param Bundle savedInstanceState
     */
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
          app = (AseMapApplication) getApplication();
	      if(getIntent().hasExtra("fbLogout"))
		  {
	    	  app.setFBloginStatus(false);
	    	  Log.d(TAG, "onLogout");
	    	  this.closeSession();
	    	  getIntent().removeExtra("fbLogout");	    	  
		  }
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        
        if (isOnline()) System.out.println("INTERNET's FINE"); 
        else
        {
            try 
            {
            	new AlertDialog.Builder(LoginActivity.this).setTitle("Info").setMessage("No internet connection."+"\n"+ "Please check your internet settings!").setIcon(R.drawable.warning).setNeutralButton("Ok", null).show();
            }
            catch(Exception e) {Log.d(Constants.ACCOUNT_NAME, "Show Dialog: "+e.getMessage());}
        }
        
        sqlUsername = (EditText) findViewById(R.id.username);
        sqlPassword = (EditText) findViewById(R.id.password);
        sqlEmail = (EditText) findViewById(R.id.email);
        sqlEmail.setVisibility(View.INVISIBLE);
        
        sqlFblogin = (ImageButton) findViewById(R.id.fbloginButton);
        sqlFblogin.setOnClickListener(new View.OnClickListener() 
        {        	
			public void onClick(View v) 
			{
				sqlUsername.setEnabled(false);
				sqlPassword.setEnabled(false);
				sqlEmail.setEnabled(false);
				sqlLogin.setEnabled(false);
				sqlRegister.setEnabled(false);
				openSession();
			} 
		});
        
        sqlLogin = (Button) findViewById(R.id.loginButton);
        sqlLogin.setOnClickListener(new View.OnClickListener() 
        {			
			public void onClick(View v)
			{
				boolean didItWork = true;

				String username = sqlUsername.getText().toString();
				String password = sqlPassword.getText().toString();
				//String email = sqlEmail.getText().toString();
				
				User entry = new User(LoginActivity.this);
				entry.open();				
				if(didItWork == entry.checkUserLogin(username, password)) 
				{
			        Toast toast = Toast.makeText(getBaseContext(), "You are now logged in.", Toast.LENGTH_LONG);
					Intent mapIntent = new Intent(LoginActivity.this, MainActivity.class);
					String message = username;
					mapIntent.putExtra("username", message);
					startActivity(mapIntent);
					entry.close();
					
				} 
				else 
				{
					if (isOnline())
					{
			            System.out.println("INTERNET's FINE"); 
					}
			        else
			        { 
			            try 
			            {
			            		new AlertDialog.Builder(getBaseContext()).setTitle("Info").setMessage("No internet connection."+"\n"
			            		+ "Please check your internet settings!").setIcon(R.drawable.warning).setNeutralButton("Ok", null).show();
			            }
			            catch(Exception e) 
			            {
			            	Log.d(Constants.ACCOUNT_NAME, "Show Dialog: "+e.getMessage());
			            }
			        }
					new AlertDialog.Builder(LoginActivity.this).setTitle(" ").setMessage("We couldn't log you in!" + "\n" 
					+ "Please make sure you are registered," + "\n" + "or try again using a different username & password.").setIcon(R.drawable.warning).setNeutralButton("Close", null).show();  			        				
					entry.close();				
				}
			}						
		});	
                       
        sqlRegister = (Button) findViewById(R.id.registerButton);
        sqlRegister.setOnClickListener(new View.OnClickListener() 
        {
			        	
			public void onClick(View v) 
			{
				sqlRegister.setText("Create an Account");
				if(sqlEmail.getVisibility() == View.VISIBLE) 
				{
					RegisterValidation checkInfo = new RegisterValidation();
					boolean didItWork = true;
					try 
					{
						String username = sqlUsername.getText().toString();
						String password = sqlPassword.getText().toString();
						String email = sqlEmail.getText().toString();						
						boolean isUsernameValid = checkInfo.checkUsername(username);
						boolean isPasswordValid = checkInfo.checkPasswords(password);
						boolean isEmailValid = checkInfo.checkEmail(email);
						User entry = new User(LoginActivity.this);
						entry.open();
						if(isUsernameValid && isPasswordValid && isEmailValid && didItWork)
						{
							entry.checkUserRegistration(username, password, email);
							entry.close();
							didItWork = true;							
						}
						else 
						{
							didItWork = false;
							new AlertDialog.Builder(LoginActivity.this).setTitle(" ").setMessage("Something went really bad!" + "\n" 
							+ "Please make sure you are registered and all information above is correct!").setIcon(R.drawable.warning).setNeutralButton("Close", null).show();
							sqlEmail.setVisibility(View.VISIBLE);
							sqlRegister.setVisibility(View.VISIBLE);
						}
					} 
					catch (Exception e) 
					{						
						String error = e.toString();
						Dialog d = new Dialog(LoginActivity.this);
						d.setTitle("Nahh");
						TextView tv = new TextView(LoginActivity.this);
						tv.setText(error);
						d.setContentView(tv);
						d.show();												
						didItWork = false;
					} 
					finally 
					{
						if(didItWork) 
						{
							new AlertDialog.Builder(LoginActivity.this).setTitle(" ").setMessage("Thank you for registeting!" + "\n" + "Now just click login!").setIcon(R.drawable.success).setNeutralButton("Close", null).show();
						}
					}					
				}
				if(sqlEmail.getVisibility() == View.INVISIBLE) 
				{
					sqlEmail.setVisibility(View.VISIBLE);
					sqlRegister.setText("Register!");
				}
				else 
				{
					//sqlEmail.setVisibility(View.INVISIBLE);
					//sqlRegister.setVisibility(View.INVISIBLE);
					sqlRegister.setText("Register!");
				}
				
			}
		});  	
    }
    
	  @Override
	  protected void onSessionStateChange(SessionState state, Exception exception) 
	  {
	    // user has either logged in or not ...	
		Log.d(TAG,state.name());
	    if (state.isOpened()) 
	    {   	
		    if(!getIntent().hasExtra("fbLogout"))
		    {
				Log.d(TAG, "on session state change");	
				// make request to the /me API
				Request request = Request.newMeRequest(this.getSession(), new Request.GraphUserCallback() 
				{
					// callback after Graph API response with user object
					public void onCompleted(final GraphUser user, Response response) 
					{
						if (user != null) 
						{
							//fb.setVisibility(View.VISIBLE);
							TextView welcome = (TextView) findViewById(R.id.welcome);
							welcome.setText("Hello " + user.getName() + "!");
							name = user.getName();
							//profilePictureView.setVisibility(View.VISIBLE);
							profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
							profilePictureView.setUserId(user.getId());
					    	app.setFBloginStatus(true);
					    	
					    	// create a new thread
							Thread helper = new Thread() 
							{
								public void run() 
								{			
									//we try to make it sleep for 5 seconds before the new activity starts and perhaps add calculations
									try
									{
										//sleep for 1 sec	
										int introTimer = 0;
										while(introTimer < 1000) 
										{
	
											sleep(100);
										   	introTimer = introTimer + 100;   
										}
										
									   Log.d(TAG, "exited thread");
									   //set fb status
									   profilePictureView.buildDrawingCache();
									   bmap = profilePictureView.getDrawingCache();
									   Intent i = new Intent(LoginActivity.this, MainActivity.class);
									   ByteArrayOutputStream bs = new ByteArrayOutputStream();
									   bmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
									   byte [] b=bs.toByteArray();						
									   String temp = Base64.encodeToString(b, Base64.DEFAULT);
									   
									   String name = user.getFirstName();
									   String username = user.getUsername();
									   String birthday = user.getBirthday();
	
									   i.putExtra("name", name); 
									   i.putExtra("username", username); 
									   i.putExtra("fbBitmap", temp);
									   i.putExtra("fbLogin", "login");
									   //System.out.println(birthday);
									   
									   WebServiceConnector ws = new WebServiceConnector();
									   String loginfb= ws.getLoginResponse(username,username+"@facebook.com", username+"@facebook.com");
									   if(loginfb.equals("RegisterFalse"))
									   {
										   ws.getLoginResponse(username,username+"@facebook.com",null);										   
									   }
									   else 
									   {
										   ws.getLoginResponse(username,username+"@facebook.com",null);
										   ws.getUpdateResponse(username, user.getFirstName(), user.getLastName(),"",0,0,0,temp,1);
									   }
									   startActivity(i);
									  } 
									catch (Exception e) {e.printStackTrace();}									
								}
					      };
					      		//begin the helper
					      		helper.start();
						}
					}
				});
				Request.executeBatchAsync(request);
		    }
	    }
	 }
    
	/**
	* This method checks if the device is online and connected to the internet
	* @return true or false
	*/
    public boolean isOnline() 
    {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable())
        {
            System.out.println("NO INTERNET CONNECTION!");
            return false;
        }
    return true; 
    }

	/**
	 * This method is a standard android activity method which creates the menu for this activity.
	 * @return true or false
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onClick(DialogInterface dialog, int which) {}
	
	/**
	 * This method overrides the default onBackPressed() android activity method in order to change the functionality when the user presses the back button on his/hers device.
	 * @return void
	 */
	@Override
    public void onBackPressed() 
	{    			
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
    }
}
