package com.aseproject.intro;

import com.aseproject.login.*;
import com.aseproject.map.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;

/**
*Activity Class which displays the animation
* @author Socratis Michaelides 2012
*/
public class SplashActivity extends Activity 
{
	private ProgressDialog progDialog;

	/* Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		// create a new thread
		Thread helper = new Thread() 
		{
			public void run() 
			{	
			//we try to make it sleep for 5 seconds before the new activity starts and perhaps add calculations
			    try
			    {
					//sleep for 3 sec	
				    int introTimer = 0;
				    while(introTimer < 1000) 
				    {
					    sleep(100);
						introTimer = introTimer + 100;   
					}
					//remove dialog
				    //progDialog.dismiss();
				    //builder.
				    Intent login = new //switch to login screen
				    Intent(SplashActivity.this,LoginActivity.class); 
				    startActivity(login); 
			    } 
			    catch (Exception e) 
			    {
				//didnt set any exceptions yet
				}
				finally
			    {
			    	finish();
			    }
			}
		 };
		//begin the helper
		helper.start();
	}
	
	@Override
	protected void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();		
	}

	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStart() 
	{
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() 
	{
		// TODO Auto-generated method stub
		super.onStop();
	}
}

