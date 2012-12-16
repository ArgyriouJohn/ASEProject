package com.aseproject.review;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ListView;

import com.aseproject.checkin.CheckIn;
import com.aseproject.checkin.CheckInAdapter;
import com.aseproject.checkin.CheckInBitmap;
import com.aseproject.map.R;
import com.aseproject.utilities.Utils;
import com.aseproject.utilities.WebServiceConnector;

public class ReviewListTask extends AsyncTask<Void, Void,ArrayList<ReviewBitmap>>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<ReviewBitmap> reviewList =new  ArrayList<ReviewBitmap>();
    private Activity activity;
    ReviewAdapter adapter;
    ListView reviewListView;
    
    private String location;
 
	public ReviewListTask(String sqlLocation,Activity activity)
	{
		this.location=sqlLocation;
		this.activity=activity;
		this.reviewListView = (ListView) activity.findViewById(R.id.ReviewListView);
	}
	
	protected void onPreExecute() {}
	
	@Override
	protected ArrayList<ReviewBitmap> doInBackground(Void... params) 
	{
		try 
		{
			ArrayList<Review> temp = ws.getReviewsResponse(location, null);
			for(Review c : temp)
			{
				ReviewBitmap cb = new ReviewBitmap(c.getUsername(),c.getLocation(),c.getReviewText(),c.getRating(),c.getLikes(),c.getDislikes(),c.getProfPic(),c.getTimeDate());
				if(!c.getProfPic().equals(""))
				{
					Bitmap userPic = Utils.decodeImage(cb.getProfPic());
				    Bitmap resizedPic = Utils.resizeBitmap(userPic,64,64);
					cb.setProfilePicBitmap(resizedPic);
				}
				reviewList.add(cb);
			}
		} 
		catch (IOException e) {e.printStackTrace();}
		
		return reviewList;
	}
	
	protected void onPostExecute(ArrayList<ReviewBitmap> ls)
    {
		adapter = new ReviewAdapter(activity,R.layout.review_list_item,reviewList);
		if(!(reviewListView.getAdapter()==adapter))
		{
			reviewListView.setAdapter(adapter);
		}
	}
}
