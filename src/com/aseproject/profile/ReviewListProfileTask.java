package com.aseproject.profile;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aseproject.checkin.CheckIn;
import com.aseproject.map.R;
import com.aseproject.review.Review;
import com.aseproject.utilities.WebServiceConnector;

public class ReviewListProfileTask extends AsyncTask<Void, Void,ArrayList<Review>>
{
	private WebServiceConnector ws = new WebServiceConnector();		
	private ArrayList<Review> reviewList =new  ArrayList<Review>();
    private Activity activity;
    ProfileReviewAdapter adapter;
    ListView reviewListView;
    
    private String input;
 
	public ReviewListProfileTask(String input,Activity activity)
	{
		this.input=input;
		this.activity=activity;
		this.reviewListView = (ListView) activity.findViewById(R.id.ProfileReviewsView);
	}
	
	protected void onPreExecute() {}
	
	@Override
	protected ArrayList<Review> doInBackground(Void... params) 
	{
		try 
		{
			reviewList = ws.getReviewsResponse(null,input);		
		} 
		catch (IOException e) {e.printStackTrace();}
		
		return reviewList;
	}
	
	protected void onPostExecute(ArrayList<Review> ls)
    {
		if(reviewList.size()!=0)
		{
			adapter = new ProfileReviewAdapter(activity,R.layout.profile_review_list_item,reviewList);
			reviewListView.setAdapter(adapter);
		}
		else
		{
			ArrayAdapter<String> noValues = new ArrayAdapter<String>(activity,R.layout.list_item);
			noValues.add("The user has no reviews!");
			reviewListView.setAdapter(noValues);
		}
	}
}
