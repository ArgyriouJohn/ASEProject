package com.aseproject.checkin;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aseproject.map.R;
import com.aseproject.utilities.Utils;
import com.aseproject.utilities.WebServiceConnector;

/**
 * This class defines an CheckInListTask AsyncTask used to get checkin data from the server asyncronously.
 * @author John Argyriou 2012
 * @see CheckIn
 * @see CheckInBitmap
 */
public class CheckInListTask extends AsyncTask<Void, Void,ArrayList<CheckInBitmap>>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<CheckInBitmap> checkInList = new  ArrayList<CheckInBitmap>();
    private Activity activity;
    private CheckInAdapter adapter;
    private ListView checkInListView;
    private String location;
  
    /**
    * This constructor creates an CheckIn object.
    * @param location checkin's location.
    * @param activity parent activity.
    */
	public CheckInListTask(String location,Activity activity)
	{
		this.location=location;
		this.activity=activity;
		this.checkInListView = (ListView) activity.findViewById(R.id.CheckInListView);
	}
	
	protected void onPreExecute() {}
	
	@Override
	protected ArrayList<CheckInBitmap> doInBackground(Void... params) 
	{
		try 
		{
			// get checkin data from the server.
			ArrayList<CheckIn> temp = ws.getCheckInsResponse(location, null);
			// create a CheckInBitmap for each CheckIn and assing it to the adapter.
			for(CheckIn c : temp)
			{
				CheckInBitmap cb = new CheckInBitmap(c.getUsername(),c.getLocation(),c.getTimeDate(),c.getProfilePic());
				if(!c.getProfilePic().equals(""))
				{
					Bitmap userPic = Utils.decodeImage(cb.getProfilePic());
				    Bitmap resizedPic = Utils.resizeBitmap(userPic,64,64);
					cb.setProfilePicBitmap(resizedPic);
				}
				checkInList.add(cb);
			}
		} 		
		catch (IOException e) {e.printStackTrace();}
				
		return checkInList;
	}
	
	protected void onPostExecute(ArrayList<CheckInBitmap> ls)
    {
		// handle the case the list is empty by displaying a message.
		if(checkInList.size()!=0)
		{
			adapter = new CheckInAdapter(activity,R.layout.checkin_list_item,checkInList);
			checkInListView.setAdapter(adapter);
		}
		else
		{
			ArrayAdapter<String> noValues = new ArrayAdapter<String>(activity,R.layout.list_item);
			noValues.add("This location has no checkins!");
			checkInListView.setAdapter(noValues);
		}
	}
}
