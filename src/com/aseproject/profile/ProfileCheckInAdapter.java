package com.aseproject.profile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aseproject.checkin.CheckIn;
import com.aseproject.map.R;

public class ProfileCheckInAdapter extends ArrayAdapter<CheckIn>
{
	private Activity activity; 
    ArrayList<CheckIn> data;
   
    public ProfileCheckInAdapter(Activity activity, int layoutResourceId,ArrayList<CheckIn> checkInList) 
    {
        super(activity, layoutResourceId, checkInList);
        this.activity = activity;
        this.data = checkInList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View v = convertView;
       
    	LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.profile_checkin_list_item,null);
           

        TextView rn = (TextView)v.findViewById(R.id.CheckInName);
        TextView rt = (TextView)v.findViewById(R.id.CheckInText);
       
        CheckIn checkIn = data.get(position);
        
        rn.setText(checkIn.getLocation());
        
        Timestamp date = checkIn.getTimeDate();        
        SimpleDateFormat filter = new SimpleDateFormat ("dd-MM-yy , hh:mm");
        
        rt.setText("Checked in at "+filter.format(date)+"!");

        return v;
    }
}
