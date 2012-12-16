package com.aseproject.checkin;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ListView;

import com.aseproject.map.R;
import com.aseproject.utilities.Utils;
import com.aseproject.utilities.WebServiceConnector;

public class CheckInListTask extends AsyncTask<Void, Void,ArrayList<CheckInBitmap>>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<CheckInBitmap> checkInList = new  ArrayList<CheckInBitmap>();
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
	protected ArrayList<CheckInBitmap> doInBackground(Void... params) 
	{
		try 
		{
			ArrayList<CheckIn> temp = ws.getCheckInsResponse(location, null);
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
		adapter = new CheckInAdapter(activity,R.layout.checkin_list_item,checkInList);
		if(!(checkInListView.getAdapter()==adapter))
		{
			checkInListView.setAdapter(adapter);
		}
	}
}
