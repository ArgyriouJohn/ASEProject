package com.example.ase_map;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewAdapter extends ArrayAdapter<Review>
{
	private Activity activity; 
    ArrayList<Review> data;
   
    public ReviewAdapter(Activity activity, int layoutResourceId,ArrayList<Review> reviewList) 
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
           
        ImageView iv = (ImageView)v.findViewById(R.id.ReviewIcon);
        TextView rn = (TextView)v.findViewById(R.id.ReviewName);
        TextView rt = (TextView)v.findViewById(R.id.ReviewText);
        RatingBar rr = (RatingBar)v.findViewById(R.id.ReviewRating);
       
        Review review = data.get(position);
        
        rn.setText(review.getUsername());
        rt.setText(review.getReviewText());
        iv.setImageResource(R.drawable.android96);
        rr.setRating(review.getRating());

        return v;
    }
}
