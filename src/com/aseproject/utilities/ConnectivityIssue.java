package com.aseproject.utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aseproject.map.R;

/**
 * Activity Class which notifies the user that the connection is lost.
 * Responsible of letting the user turn wifi/gps back on
 * @author Socratis Michaelides 2012
 */
public class ConnectivityIssue extends Activity 
{
	Button settingsBtn;
    private static final String LOGTAG = "ConnectivityIssue";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        Log.d(LOGTAG,"Creating Activity");

		setContentView(R.layout.activity_conn_issues);
		TextView feedback = (TextView) findViewById(R.id.issue);
		
		if(((AseMapApplication) getApplication()).getNetworkStatus() ||((AseMapApplication) getApplication()).getGPSstatus() == false)
		{
			feedback.setText(R.string.network_problem);
			//settingsBtn.setText(R.string.fix_settings);
		}
		else
		{
			//settingsBtn.setText(R.string.return_to_app);
			feedback.setText(R.string.network_fixed);
		}
		
		settingsBtn = (Button) findViewById(R.id.button1);
		settingsBtn.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				if(((AseMapApplication) getApplication()).getNetworkStatus() ||((AseMapApplication) getApplication()).getGPSstatus() == false)
				{
					if(((AseMapApplication) getApplication()).getNetworkStatus() == false)
					{
						   // TODO Auto-generated method stub
						   Intent i = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
						   startActivity(i);
				    }
					else
					{
						Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			           	startActivity(i);					
					}			
				}
				else
				{
					finish();
					Log.d(LOGTAG,"go to map Activity");
				}
			}
		});
		
       if(((AseMapApplication) getApplication()).getNetworkStatus() && ((AseMapApplication) getApplication()).getGPSstatus() == true)
       {
	      finish();
       }	
   }
	@Override
	protected void onStart() 
	{
		// TODO Auto-generated method stub
		super.onStart();
		if(((AseMapApplication) getApplication()).getNetworkStatus() || ((AseMapApplication) getApplication()).getGPSstatus() == true)
	    {
			Log.d(LOGTAG,"Destroying Activity");
			finish();
		}
	 }
	
	@Override
	protected void onRestart() 
	{
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(LOGTAG,"Restarting Activity");
	}
	
	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() 
	{
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
    public void onBackPressed() 
    {
		//new AlertDialog.Builder(MainActivity.this).setTitle(" ").setMessage("Please use the Log Out button at the top to logout first!").setIcon(R.drawable.warning).setNeutralButton("Close", null).show();  			        
    }

}
	

        

