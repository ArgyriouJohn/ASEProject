package com.aseproject.review;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.aseproject.login.UserAuth;
import com.aseproject.map.R;
import com.aseproject.profile.ProfileActivity;
import com.aseproject.utilities.User;
import com.aseproject.utilities.Utils;
import com.aseproject.utilities.WebServiceConnector;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<ReviewBitmap>
{
	private Activity activity; 
    ArrayList<ReviewBitmap> data;
	String intent = "ReviewAdapter";
   
    public ReviewAdapter(Activity activity, int layoutResourceId,ArrayList<ReviewBitmap> reviewList) 
    {
        super(activity, layoutResourceId, reviewList);
        this.activity = activity;
        this.data = reviewList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View v = convertView;
       
    	LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.review_list_item,null);

        final ReviewBitmap review = data.get(position);

        ImageView iv = (ImageView)v.findViewById(R.id.ReviewIcon);
        iv.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{

				User entry = new User(activity);
				entry.open();
				final UserAuth user = entry.retrieveProfileInfo(review.getUsername());
				Intent profileIntent = new Intent(activity, ProfileActivity.class);
				profileIntent.putExtra("message", review.getUsername());
				profileIntent.putExtra("intent", intent);
				entry.close();
				activity.startActivity(profileIntent);
			}
		});
        TextView rn = (TextView)v.findViewById(R.id.ReviewName);
        TextView rt = (TextView)v.findViewById(R.id.ReviewText);
        RatingBar rr = (RatingBar)v.findViewById(R.id.ReviewRating);
        TextView like = (TextView)v.findViewById(R.id.LikeCount);
        TextView dislike = (TextView)v.findViewById(R.id.DislikeCount);
       
        
        final String username =review.getUsername();
        final String reviewText =review.getReviewText();
        final String location =review.getLocation();
        String profilePic =review.getProfPic();
        final int rating =review.getRating();
        final int likes = review.getLikes();
        final int dislikes =review.getDislikes();
            
        rn.setText(username);
        Timestamp date = review.getTimeDate();        
        SimpleDateFormat filter = new SimpleDateFormat ("dd-MM-yy");
        rt.setText(reviewText+" at "+filter.format(date));        
        rr.setRating(rating);
        like.setText(String.valueOf(likes));
        dislike.setText(String.valueOf(dislikes));
        
        if(!profilePic.equals(""))
        {
	        iv.setImageBitmap(review.getProfilePicBitmap());
        }
        else
        {
        	iv.setImageResource(R.drawable.android96);
        }   
        
        ImageButton LikeButton =(ImageButton) v.findViewById(R.id.LikeButton);
		
		LikeButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				WebServiceConnector ws = new WebServiceConnector();
				try 
				{
  					Calendar cal = Calendar.getInstance();
  				    Date utilDate = cal.getTime();
  					Timestamp sqlDate = new  Timestamp(utilDate.getTime());
					System.out.println("Updated likes: "+ws.getReviewResponse(username,location,reviewText,rating,likes+1,dislikes,sqlDate));
				} 
				catch (IOException e) {e.printStackTrace();}
			}
		});
		
		ImageButton DislikeButton =(ImageButton) v.findViewById(R.id.DislikeButton);
		
		DislikeButton.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) 
			{
				WebServiceConnector ws = new WebServiceConnector();
				try 
				{
					Calendar cal = Calendar.getInstance();
  				    Date utilDate = cal.getTime();
  					Timestamp sqlDate = new  Timestamp(utilDate.getTime());
					System.out.println("Updated dislikes: "+ws.getReviewResponse(username,location,reviewText,rating,likes,dislikes+1,sqlDate));
				} 
				catch (IOException e) {e.printStackTrace();}
			}
		});

        return v;
    }
}
