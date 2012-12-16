package com.aseproject.profile;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aseproject.checkin.CheckIn;
import com.aseproject.checkin.CheckInAdapter;
import com.aseproject.map.R;
import com.aseproject.utilities.WebServiceConnector;

public class CheckInListProfileTask extends AsyncTask<Void, Void,ArrayList<CheckIn>>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<CheckIn> checkInList = new  ArrayList<CheckIn>();
    private Activity activity;
    ProfileCheckInAdapter adapter;
    ListView checkInListView;
    private String input;

	public CheckInListProfileTask(String input,Activity activity)
	{
		this.activity=activity;
		this.checkInListView = (ListView) activity.findViewById(R.id.ProfileCheckInsView);
		this.input=input;
	}
	
	protected void onPreExecute() {}
	
	@Override
	protected ArrayList<CheckIn> doInBackground(Void... params) 
	{
		try 
		{
			checkInList= ws.getCheckInsResponse(null,input);
		} 		
		catch (IOException e) {e.printStackTrace();}
				
		return checkInList;
	}
	
	protected void onPostExecute(ArrayList<CheckIn> ls)
    {
		if(checkInList.size()!=0)
		{
			adapter = new ProfileCheckInAdapter(activity,R.layout.profile_checkin_list_item,checkInList);
			checkInListView.setAdapter(adapter);
		}
		else
		{
			ArrayAdapter<String> noValues = new ArrayAdapter<String>(activity,R.layout.list_item);
			noValues.add("The user has no checkins!");
			checkInListView.setAdapter(noValues);
		}
	}
}
