package com.aseproject.checkin;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import com.aseproject.map.R;
import com.aseproject.utilities.WebServiceConnector;

public class CheckInListTask extends AsyncTask<Void, Void,CheckInAdapter>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<CheckIn> checkInList = new  ArrayList<CheckIn>();
    private Activity activity;
    CheckInAdapter adapter;
    ListView checkInListView;

    private String location;
  
	public CheckInListTask(String sqlLocation,Activity activity)
	{
		this.location=sqlLocation;
		this.activity=activity;
		this.checkInListView = (ListView) activity.findViewById(R.id.CheckInListView);
	}
	
	protected void onPreExecute() {}
	
	@Override
	protected CheckInAdapter doInBackground(Void... params) 
	{
		try 
		{
			checkInList = ws.getCheckInsResponse(location, null);
		} 
		catch (IOException e) {e.printStackTrace();}
		
		adapter = new CheckInAdapter(activity,R.layout.checkin_list_item,checkInList);
		return adapter;
	}
	
	protected void onPostExecute(CheckInAdapter adapter)
    {
		checkInListView.setAdapter(adapter);
	}
}
