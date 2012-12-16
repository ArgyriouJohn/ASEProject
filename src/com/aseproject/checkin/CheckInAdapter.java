package com.aseproject.checkin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseproject.map.R;
import com.aseproject.profile.ProfileActivity;
import com.aseproject.utilities.Utils;

public class CheckInAdapter extends ArrayAdapter<CheckInBitmap>
{
	private Activity activity; 
    ArrayList<CheckInBitmap> data;
	String intent = "CheckInAdapter";
   
    public CheckInAdapter(Activity activity, int layoutResourceId,ArrayList<CheckInBitmap> checkInList) 
    {
        super(activity, layoutResourceId, checkInList);
        this.activity = activity;
        this.data = checkInList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View v = convertView;
        final CheckInBitmap checkIn = data.get(position);
    	LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.checkin_list_item,null);
           
        ImageView iv = (ImageView)v.findViewById(R.id.CheckInIcon);
        iv.setOnClickListener(new View.OnClickListener() 
        {		
			@Override
			public void onClick(View v) 
			{
				Intent profileIntent = new Intent(activity, ProfileActivity.class);
				profileIntent.putExtra("message", checkIn.getUsername());
				profileIntent.putExtra("intent", intent);
				activity.startActivity(profileIntent);
			}
		});
        
        TextView rn = (TextView)v.findViewById(R.id.CheckInName);
        TextView rt = (TextView)v.findViewById(R.id.CheckInText);
              
        rn.setText(checkIn.getUsername());
        
        Timestamp date = checkIn.getTimeDate();        
        SimpleDateFormat filter = new SimpleDateFormat ("dd-MM-yy , hh:mm");
        rt.setText("Checked in at "+filter.format(date)+" !");
        
        String profilePic =checkIn.getProfilePic();
        
        if(!profilePic.equals(""))
        {
	        iv.setImageBitmap(checkIn.getProfilePicBitmap());
        }
        else
        {
        	iv.setImageResource(R.drawable.android96);
        }   

        return v;
    }
}
