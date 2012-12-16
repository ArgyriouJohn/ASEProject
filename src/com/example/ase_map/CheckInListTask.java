package com.example.ase_map;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

class CheckInListTask extends AsyncTask<Void, Void, ArrayAdapter<String>>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<CheckIn> checkInList =new  ArrayList<CheckIn>();
	private ArrayList<String> checkins = new ArrayList<String>();
    private Activity activity;
	ArrayAdapter<String> adapter;
    
    private String username;
    private String location;
    private Timestamp date;

     
	public CheckInListTask(String uName,String sqlLocation,Timestamp sqlDate,Activity activity)
	{
		this.username=uName;
		this.location=sqlLocation;
		this.date=sqlDate;
		this.activity=activity;
	}
	
	protected void onPreExecute() {}
	
	@Override
	protected ArrayAdapter<String> doInBackground(Void... params) 
	{
		try 
		{
			checkInList = ws.getCheckInsResponse(username,location,date);
		} 
		catch (IOException e) {e.printStackTrace();}
		
		for(CheckIn c : checkInList)
		{
			checkins.add(c.getUsername()+" checked in at "+c.getLocation()+" at "+c.getTimeDate());
		}
     
		adapter = new ArrayAdapter<String>(activity.getBaseContext(),android.R.layout.simple_list_item_1,checkins);
    	    	
		return adapter;
	}
	
	protected void onPostExecute(ArrayAdapter<String> adapter)
    {
		ListView checkInListView = (ListView) activity.findViewById(R.id.listView2);
		checkInListView.setAdapter(adapter);
	}
}
