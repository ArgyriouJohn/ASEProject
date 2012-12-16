package com.aseproject.review;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import com.aseproject.map.R;
import com.aseproject.utilities.WebServiceConnector;

public class ReviewListTask extends AsyncTask<Void, Void,ReviewAdapter>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<Review> reviewList =new  ArrayList<Review>();
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
	protected ReviewAdapter doInBackground(Void... params) 
	{
		try 
		{
			reviewList = ws.getReviewsResponse(location, null);
		} 
		catch (IOException e) {e.printStackTrace();}
		
		adapter = new ReviewAdapter(activity,R.layout.review_list_item,reviewList);
		return adapter;
	}
	
	protected void onPostExecute(ReviewAdapter ad)
    {
		reviewListView.setAdapter(adapter);
	}
}
