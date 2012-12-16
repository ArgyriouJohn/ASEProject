package com.aseproject.checkin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseproject.map.R;
import com.aseproject.profile.ProfileActivity;

/**
 * This class defines an CheckIn adapter used to manipulate data in the CheckIn listview.
 * @author John Argyriou 2012
 * @author Thanos Irodotou 2012
 * @see CheckIn 
 * @see CheckInBitmap
 */
public class CheckInAdapter extends ArrayAdapter<CheckInBitmap>
{	
	private Activity activity; 
    ArrayList<CheckInBitmap> checkInList;
	String intent = "CheckInAdapter";
   
   /**
    * This constructor creates an CheckInAdapter object.
    * @param activity parent activity
    * @param layoutResourceId resource id.
    * @param checkInList CheckInBitmap list.
    */
    public CheckInAdapter(Activity activity, int layoutResourceId,ArrayList<CheckInBitmap> checkInList) 
    {
        super(activity, layoutResourceId, checkInList);
        this.activity = activity;
        this.checkInList = checkInList;
    }

    /**
     * This method updates the current listview.
     * @param position item position.
     * @param convertView parent view.
     * @param parent parent group views.
     * @return View current view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
    	// prepare view for update.
        View v = convertView;
        final CheckInBitmap checkIn = checkInList.get(position);
    	LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.checkin_list_item,null);
        
        // create objects for list manipulation
        ImageView imageView = (ImageView)v.findViewById(R.id.CheckInIcon);
        imageView.setOnClickListener(new View.OnClickListener() 
        {		
			@Override
			public void onClick(View v) 
			{
				// on click go to another users account.
				Intent profileIntent = new Intent(activity, ProfileActivity.class);
				profileIntent.putExtra("message", checkIn.getUsername());
				profileIntent.putExtra("intent", intent);
				activity.startActivity(profileIntent);
			}
		});        
        TextView checkInName = (TextView)v.findViewById(R.id.CheckInName);
        TextView checkInText = (TextView)v.findViewById(R.id.CheckInText);
        
        // assign username.
        checkInName.setText(checkIn.getUsername());
        
        // assign time and date.
        Timestamp date = checkIn.getTimeDate();        
        SimpleDateFormat filter = new SimpleDateFormat ("dd-MM-yy , hh:mm");
        checkInText.setText("Checked in at "+filter.format(date)+" !");
        
        // assign profile pic.
        String profilePic =checkIn.getProfilePic();        
        if(!profilePic.equals(""))
        {
	        imageView.setImageBitmap(checkIn.getProfilePicBitmap());
        }
        else
        {
        	imageView.setImageResource(R.drawable.android96);
        }   

        return v;
    }
}
