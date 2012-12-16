package com.aseproject.profile;

import java.io.IOException;
import java.util.ArrayList;

import com.aseproject.checkin.CheckIn;
import com.aseproject.login.UserAuth;
import com.aseproject.map.R;
import com.aseproject.map.R.layout;
import com.aseproject.map.R.menu;
import com.aseproject.review.Review;
import com.aseproject.utilities.User;
import com.aseproject.utilities.Utils;
import com.aseproject.utilities.WebServiceConnector;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileActivity2 extends Activity {
	
	TextView privacyLabel, recentCheckIns, recentReviews, gender, dob;	
	ImageView profilePic;
	TextView username;
	EditText firstName, lastName;
	ListView ProfileCheckInsView;
	ListView ProfileReviewsView;
	ProfileCheckInAdapter adapterCheckIn;
	ProfileReviewAdapter adapterReview;
	private ArrayList<CheckIn> checkInList = new  ArrayList<CheckIn>();
	private ArrayList<Review> reviewList = new  ArrayList<Review>();
	private WebServiceConnector ws = new WebServiceConnector();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_activity2);
		
		Bundle extras = getIntent().getExtras();
        final String strvalue = extras.getString("username");
		username = (TextView) findViewById(R.id.userNameLabel);
		profilePic = (ImageButton) findViewById(R.id.profileImageButton);	
		firstName = (EditText) findViewById(R.id.firstName);
		lastName = (EditText) findViewById(R.id.lastName);
		firstName.setFocusable(false);
		lastName.setFocusable(false);
		gender = (TextView) findViewById(R.id.genderText);
		dob = (TextView) findViewById(R.id.dobLabel);
		privacyLabel = (TextView) findViewById(R.id.privacyLabel);
		recentCheckIns = (TextView) findViewById(R.id.checkInsLabel);
		recentReviews = (TextView) findViewById(R.id.reviewsLabel);
		profilePic.setEnabled(false);
		username.setFocusable(false);
		username.setText(strvalue);
        ProfileCheckInsView = (ListView) findViewById(R.id.ProfileCheckInsView);
        ProfileReviewsView = (ListView) findViewById(R.id.ProfileReviewsView);
		
		User entry = new User(ProfileActivity2.this);
		final UserAuth user = entry.retrieveProfileInfo(strvalue);
		
		if (user.getPicture().equals("")) {
        	profilePic = (ImageView) findViewById(R.id.profileImageButton);
        } else {
        	profilePic.setImageBitmap(Utils.decodeImage(user.getPicture()));
        }
		
		if(user.getVisibility() == 1) {
			firstName.setText("N/A");
			lastName.setText("N/A");
			gender.setText("N/A");
			recentCheckIns.setVisibility(View.INVISIBLE);
			recentReviews.setVisibility(View.INVISIBLE);
			dob.setText("Date of Birth: N/A");
			privacyLabel.setVisibility(View.VISIBLE);
			privacyLabel.setText(strvalue + " has chosen not to share any other information!");

		} else {
			firstName.setText(user.getFirstName());
			lastName.setText(user.getLastName());
			recentCheckIns.setVisibility(View.VISIBLE);
			recentReviews.setVisibility(View.VISIBLE);
			gender.setText(user.getGender());
			privacyLabel.setVisibility(View.INVISIBLE);
			dob.setText("Date of Birth: "+ user.getYear()+", " + user.getMonth()+", " + user.getDay());
	        //GET AND DISPLAY USER CHECKINS & REVIEWS
			try {
				checkInList = ws.getCheckInsResponse(null,strvalue);
				reviewList = ws.getReviewsResponse(null, strvalue);
				adapterCheckIn = new ProfileCheckInAdapter(this,R.layout.profile_checkin_list_item,checkInList);
				adapterReview = new ProfileReviewAdapter(this,R.layout.profile_review_list_item, reviewList);
				ProfileReviewsView.setAdapter(adapterReview);
				ProfileCheckInsView.setAdapter(adapterCheckIn);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_profile_activity2, menu);
		return true;
	}

}
