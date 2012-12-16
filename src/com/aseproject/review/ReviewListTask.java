package com.aseproject.review;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aseproject.checkin.CheckIn;
import com.aseproject.checkin.CheckInBitmap;
import com.aseproject.map.R;
import com.aseproject.utilities.Utils;
import com.aseproject.utilities.WebServiceConnector;

/**
 * This class defines an ReviewListTask AsyncTask used to get review data from the server asyncronously.
 * @author John Argyriou 2012
 * @see Review
 * @see ReviewBitmap
 */
public class ReviewListTask extends AsyncTask<Void, Void,ArrayList<ReviewBitmap>>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<ReviewBitmap> reviewList =new  ArrayList<ReviewBitmap>();
    private Activity activity;
    ReviewAdapter adapter;
    ListView reviewListView;
    
    private String location;
 
    /**
    * This constructor creates an Review object.
    * @param sqlLocation review's location.
    * @param activity parent activity.
    */
	public ReviewListTask(String sqlLocation,Activity activity)
	{
		this.location=sqlLocation;
		this.activity=activity;
		this.reviewListView = (ListView) activity.findViewById(R.id.ReviewListView);
	}
	
	protected void onPreExecute() 
	{}
	
	@Override
	protected ArrayList<ReviewBitmap> doInBackground(Void... params) 
	{		
		try 
		{
			// get Review data from the server.
			ArrayList<Review> temp = ws.getReviewsResponse(location, null);
			// create a ReviewBitmap for each Review and assing it to the adapter.
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
		// handle the case the list is empty by displaying a message.
		if(reviewList.size()!=0)
		{
			adapter = new ReviewAdapter(activity,R.layout.review_list_item,reviewList);
			reviewListView.setAdapter(adapter);
		}
		else
		{
			ArrayAdapter<String> noValues = new ArrayAdapter<String>(activity,R.layout.list_item);
			noValues.add("This location has no reviews!");
			reviewListView.setAdapter(noValues);
		}		
	}
}
