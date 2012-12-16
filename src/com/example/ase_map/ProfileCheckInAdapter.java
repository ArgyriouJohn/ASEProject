package com.example.ase_map;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        
        rn.setText(checkIn.getUsername());
        rt.setText("Checked in at "+checkIn.getLocation() +"\n"+checkIn.getTimeDate()+"!");

        return v;
    }
}
