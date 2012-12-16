package com.aseproject.review;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.aseproject.checkin.CheckIn;
import com.aseproject.checkin.CheckInBitmap;
import com.aseproject.map.R;
import com.aseproject.utilities.WebServiceConnector;

/**
 * This class defines an activity view converted into an android Dialog view and supports the creation of a new review by having the user write text for his/hers review and choose a rating from 1 to 5.
 * @author Thanos Irodotou 2012
 */
public class ReviewDialog extends Activity 
{	
	Button submitReview;
	EditText textReview;	
	RatingBar reviewRating;

	/**
	 * This method is called when the activity is first started and initialises all the UI elements that appear in the view.
	 * @param savedInstanceState view's saved instance at a given time.
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_review);
        
        submitReview = (Button) findViewById(R.id.reviewSubmitButton);
        textReview = (EditText) findViewById(R.id.reviewTextField);
        reviewRating = (RatingBar) findViewById(R.id.reviewRatingBar);
                
      //Submit Review Button Handler
   		submitReview.setOnClickListener(new View.OnClickListener() 
   		{    			
  			public void onClick(View v) 
  			{
  				Intent intent = getIntent();
  				String review = textReview.getText().toString();
  		        String locValue = intent.getStringExtra("locName");
  		        String userValue = intent.getStringExtra("userName");
  		        System.out.println(locValue+userValue);

  				if (review.equals("") || review.equals(null)) 
  				{} 
  				else 
  				{      					
  					try 
      				{
      					WebServiceConnector ws = new WebServiceConnector();
      					Calendar cal = Calendar.getInstance();
      				    Date utilDate = cal.getTime();
      					Timestamp sqlDate = new  Timestamp(utilDate.getTime());
      					System.out.println("Created Review: " + ws.getReviewResponse(userValue, locValue, textReview.getText().toString(), (int)reviewRating.getRating(),0,0,sqlDate));
      					} catch (IOException e) {e.printStackTrace();}
    			        finish();
    				    Toast.makeText(getApplicationContext(), "Your review has been successfully submitted!", Toast.LENGTH_LONG).show();
      				}
  			}
      	});
    }

    /**
     * This method initialises standar options menu for this activity.
     * @param menu the standar options menu defined in the menu xml.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_dialog_review, menu);
        return true;
    }
}
