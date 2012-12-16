package com.aseproject.profile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aseproject.checkin.CheckIn;
import com.aseproject.login.LoginActivity;
import com.aseproject.map.R;
import com.aseproject.utilities.User;

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
       final CheckIn checkIn = data.get(position);
    	LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = vi.inflate(R.layout.profile_checkin_list_item,null);        

        TextView rn = (TextView)v.findViewById(R.id.CheckInName);
        TextView rt = (TextView)v.findViewById(R.id.CheckInText);
        rn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getContext());
				myAlertDialog.setMessage("You are about to delete your check in.\n"+"This action cannot be undone!\n\n"+ "Do you want to proceed?");
				myAlertDialog.setIcon(R.drawable.warning);
				myAlertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						User entry = new User(activity.getBaseContext());
						entry.open();
						final Timestamp checkInDate = checkIn.getTimeDate();
						final String username = checkIn.getUsername();
						entry.deleteCheckIn(username, checkInDate);
						entry.close();					 
				 }});
				 
				myAlertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {				      
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.cancel();
				  }});
				myAlertDialog.show();				
			}
		});
       
        
        rn.setText(checkIn.getLocation());
        
        Timestamp date = checkIn.getTimeDate();        
        SimpleDateFormat filter = new SimpleDateFormat ("dd-MM-yy , hh:mm");
        
        rt.setText("Checked in at "+filter.format(date)+"!");

        return v;
    }
}
