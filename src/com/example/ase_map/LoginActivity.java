package com.example.ase_map;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SyncStateContract.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class LoginActivity extends Activity implements OnClickListener {

	Button sqlRegister, sqlView, sqlLogin, sqlDelete, logOut;
	EditText sqlUsername, sqlPassword, sqlEmail;	
	
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_login);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        
        if (isOnline())
            System.out.println("INTERNET's FINE"); 
        else
            {
            try {
            		new AlertDialog.Builder(LoginActivity.this).setTitle("Info").setMessage("No internet connection."+"\n"
            		+ "Please check your internet settings!").setIcon(R.drawable.warning).setNeutralButton("Ok", null).show();
            }
            catch(Exception e) {
            	Log.d(Constants.ACCOUNT_NAME, "Show Dialog: "+e.getMessage());
            }
            }
        
        sqlUsername = (EditText) findViewById(R.id.username);
        sqlPassword = (EditText) findViewById(R.id.password);
        sqlEmail = (EditText) findViewById(R.id.email);
        sqlEmail.setVisibility(View.INVISIBLE);
        
        sqlLogin = (Button) findViewById(R.id.loginButton);
        sqlLogin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				boolean didItWork = true;
								
				
				String username = sqlUsername.getText().toString();
				String password = sqlPassword.getText().toString();
				//String email = sqlEmail.getText().toString();
				
				User entry = new User(LoginActivity.this);
				entry.open();
				System.out.println("USERAS: " + entry.getUsername(username));
				
				if(didItWork == entry.checkUserLogin(username, password)) {
			        Toast toast = Toast.makeText(getBaseContext(), "You are now logged in.", Toast.LENGTH_LONG);
					Intent mapIntent = new Intent(LoginActivity.this, MainActivity.class);
					String message = username;
					mapIntent.putExtra("username", message);
					startActivity(mapIntent);
					entry.close();
					
				} else {
					if (isOnline())
			            System.out.println("INTERNET's FINE"); 
			        else { 
			            try {
			            		new AlertDialog.Builder(getBaseContext()).setTitle("Info").setMessage("No internet connection."+"\n"
			            		+ "Please check your internet settings!").setIcon(R.drawable.warning).setNeutralButton("Ok", null).show();
			            }
			            catch(Exception e) {
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
        sqlRegister.setOnClickListener(new View.OnClickListener() {
			        	
			public void onClick(View v) {
				sqlRegister.setText("Create an Account");
				if(sqlEmail.getVisibility() == View.VISIBLE) {
					RegisterValidation checkInfo = new RegisterValidation();
					boolean didItWork = true;
					try {
						String username = sqlUsername.getText().toString();
						String password = sqlPassword.getText().toString();
						String email = sqlEmail.getText().toString();
						String firstName = "";
						String lastName = "";
						boolean isUsernameValid = checkInfo.checkUsername(username);
						boolean isPasswordValid = checkInfo.checkPasswords(password);
						boolean isEmailValid = checkInfo.checkEmail(email);
						User entry = new User(LoginActivity.this);
						entry.open();
						if(isUsernameValid & isPasswordValid & isEmailValid & didItWork == entry.checkUserRegistration(username, password, email, firstName, lastName)) {
							//entry.createEntry(username, password, email);
							entry.close();
							didItWork = true;							
						} else {
							didItWork = false;
							new AlertDialog.Builder(LoginActivity.this).setTitle(" ").setMessage("Something went really bad!" + "\n" 
							+ "Please make sure you are registered and all information above is correct!").setIcon(R.drawable.warning).setNeutralButton("Close", null).show();
							sqlEmail.setVisibility(View.VISIBLE);
							sqlRegister.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						
						String error = e.toString();
						Dialog d = new Dialog(LoginActivity.this);
						d.setTitle("Nahh");
						TextView tv = new TextView(LoginActivity.this);
						tv.setText(error);
						d.setContentView(tv);
						d.show();						
						
						didItWork = false;
					} finally {
						if(didItWork) {
							Dialog d = new Dialog(LoginActivity.this);
							d.setTitle(":)");
							TextView tv = new TextView(LoginActivity.this);
							tv.setText("Thank you for registeting!" + "\n" + "Now put that info again and click login!");
							d.setContentView(tv);
							d.show();
						}
					}					
				}
				if(sqlEmail.getVisibility() == View.INVISIBLE) {
					sqlEmail.setVisibility(View.VISIBLE);
					sqlRegister.setText("Register!");
				}
				else {
					//sqlEmail.setVisibility(View.INVISIBLE);
					//sqlRegister.setVisibility(View.INVISIBLE);
					sqlRegister.setText("Register!");
				}
				
			}
		});
                
        
        /*
         * Delete Button used for testing only to quickly wipe the DB row's in the table.
         * It will NOT be a function of the final commit.
         */
        /*sqlDelete = (Button) findViewById(R.id.deleteButton);
        sqlDelete.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				User entries = new User(LoginActivity.this);
				entries.open();
				
				entries.clearDb();
				entries.close();				
			}
		});
		*/
        
        /*
         * View Button used for testing and quickly retrieving the rows currently on the DB.
         * It will NOT be a function of the final commit.
         */
        /*sqlView = (Button) findViewById(R.id.viewButton);
        sqlView.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				User entries = new User(LoginActivity.this);
				entries.open();
				Dialog d = new Dialog(LoginActivity.this);
				d.setTitle("Registered Users");
				TextView tv = new TextView(LoginActivity.this);
				tv.setText(entries.getData());
				d.setContentView(tv);
				d.show();
//				String users = entries.getData();				
				entries.close();
			}
		});
		*/        	
    }
    
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            System.out.println("NO INTERNET CONNECTION!");
            return false;
        }
    return true; 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	public void onClick(DialogInterface dialog, int which) {
		
	}
	
	@Override
    public void onBackPressed() {    			
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
    }
}
