package com.aseproject.review;


import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.aseproject.map.R;
import com.aseproject.utilities.WebServiceConnector;

public class ReviewDialog extends Activity {
	
	Button submitReview;
	EditText textReview;	
	RatingBar reviewRating;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_review);
        
        submitReview = (Button) findViewById(R.id.reviewSubmitButton);
        textReview = (EditText) findViewById(R.id.reviewTextField);
        reviewRating = (RatingBar) findViewById(R.id.reviewRatingBar);
        
        
      //Submit Review Button Handler
      		submitReview.setOnClickListener(new View.OnClickListener() {
      			
      			public void onClick(View v) {
      				Intent intent = getIntent();
      				String review = textReview.getText().toString();
      		        String locValue = intent.getStringExtra("locName");
      		        String userValue = intent.getStringExtra("userName");
      		        System.out.println(locValue+userValue);

      				if (review.equals("") || review.equals(null)) {	
//    			        Toast toast = Toast.makeText(getBaseContext(), "Please type your review first!", Toast.LENGTH_LONG);
//                  		new AlertDialog.Builder(getBaseContext()).setTitle("Info").setMessage("Please type your review first!").setIcon(R.drawable.warning).setNeutralButton("Ok", null).show();
      				} else {
      					
      				try {
  
      					WebServiceConnector ws = new WebServiceConnector();
      					System.out.println("Created Review: " + ws.getReviewResponse(userValue, locValue, textReview.getText().toString(), (int)reviewRating.getRating(),0,0));
      					} catch (IOException e) {
      					// TODO Auto-generated catch block
      					e.printStackTrace();
      					}
    			        finish();
      				}
      			}
      		});
      		
      		
//      		//Cancel Review Button Handler
//      		cancelReview.setOnClickListener(new View.OnClickListener() {
//      			
//      			public void onClick(View v) {
//      				finish();				
//      			}
//      		});
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dialog_review, menu);
        return true;
    }
}
