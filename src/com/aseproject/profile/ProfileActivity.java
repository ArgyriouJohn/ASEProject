package com.aseproject.profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aseproject.checkin.CheckIn;
import com.aseproject.login.LoginActivity;
import com.aseproject.login.UserAuth;
import com.aseproject.map.MainActivity;
import com.aseproject.map.R;
import com.aseproject.review.Review;
import com.aseproject.utilities.User;
import com.aseproject.utilities.Utils;
import com.aseproject.utilities.WebServiceConnector;

@SuppressLint("NewApi")
public class ProfileActivity extends Activity 
{	
	//Profile fields
	EditText firstName, lastName;
	ImageButton editProfileButton;
	ImageButton deleteProfileButton;
	RadioButton maleRadioButton, femaleRadioButton;
	RadioGroup genderRadioGroup;
	DatePicker dobPicker;
	ImageButton datePickerButton;
	TextView dobLabel;
	int myYear, myMonth, myDay;
	static final int ID_DATEPICKER = 0;
	ListView ProfileCheckInsView;
	ListView ProfileReviewsView;
	ProfileCheckInAdapter adapterCheckIn;
	ProfileReviewAdapter adapterReview;
	
	//camera variables
	int cameraResults;
	Bitmap bmp;
	final static int CAMERA_PHOTO = 1;
	ImageView pic;
	Intent camintent;
	private static final int SELECT_PHOTO = 3;
	private static final int CROP_FROM_CAMERA = 2;
	private Uri mImageCaptureUri;
	private ArrayList<CheckIn> checkInList = new  ArrayList<CheckIn>();
	private ArrayList<Review> reviewList =new  ArrayList<Review>();
	private WebServiceConnector ws = new WebServiceConnector();		
	String pushedUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		Bundle extras = getIntent().getExtras();
        final String strvalue = extras.getString("message");
        final String pushedIntent = extras.getString("intent");
        
        if(pushedIntent.equals("ReviewAdapter") || pushedIntent.equals("CheckInAdapter")) {
        	Intent profile2Intent = new Intent(ProfileActivity.this, ProfileActivity2.class);
        	profile2Intent.putExtra("username", strvalue);
        	startActivity(profile2Intent);
        	finish();
        }

        ProfileCheckInsView = (ListView) findViewById(R.id.ProfileCheckInsView);
        ProfileReviewsView = (ListView) findViewById(R.id.ProfileReviewsView);
        firstName = (EditText) findViewById(R.id.firstName);
        firstName.setFocusable(false);
		lastName = (EditText) findViewById(R.id.lastName);
		lastName.setFocusable(false);
		firstName.setEnabled(false);
		lastName.setEnabled(false);	
    	pic = (ImageView) findViewById(R.id.profileImageButton);
        		
        User entry = new User(ProfileActivity.this);
        entry.open();
        final UserAuth user = entry.retrieveProfileInfo(strvalue);         
        entry.close();
        
        
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
		
		
		int day = user.getYear();
        int month = user.getMonth();
        int year = user.getDay();    
        myDay = day;
        myMonth = month;
        myYear = year;                
		
        // decode image.
        if (user.getPicture().equals("")) {
        	pic = (ImageView) findViewById(R.id.profileImageButton);
        } else {
        	pic.setImageBitmap(Utils.decodeImage(user.getPicture()));
        }
           					
		firstName.setText(user.getFirstName());
		lastName.setText(user.getLastName());
		String labelDate = String.valueOf(user.getYear() + " , " + String.valueOf(user.getMonth()) + " , " + String.valueOf(user.getDay()));
						
		final String [] items= new String [] {"Take from camera", "Select from gallery"};				
		ArrayAdapter<String> adapter= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder= new AlertDialog.Builder(this);
		
		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() 
		{
			public void onClick( DialogInterface dialog, int item ) 
			{ //pick from camera
				if (item == 0) 
				{
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					
					mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
									   "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, CAMERA_PHOTO);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { //pick from file
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), SELECT_PHOTO);
				}
			}
		} );
		
		final AlertDialog dialog = builder.create();		
		final ImageButton button = (ImageButton) findViewById(R.id.profileImageButton);		
		button.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
		
        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);        
        maleRadioButton = (RadioButton) findViewById(R.id.maleRadioButton);
        maleRadioButton.setEnabled(false);
        button.setEnabled(false);
        femaleRadioButton = (RadioButton) findViewById(R.id.femaleRadioButton);
        femaleRadioButton.setEnabled(false);
        dobLabel = (TextView) findViewById(R.id.dobLabel);
        dobLabel.setText("Date of Birth: "+ labelDate);
        datePickerButton = (ImageButton) findViewById(R.id.dobPickerButton);  
        datePickerButton.setEnabled(false);
        datePickerButton.setOnClickListener(datePickerButtonOnClickListener);
        
		if(user.getGender().equals("Male")) {
			maleRadioButton.setChecked(true);
		} else {
			femaleRadioButton.setChecked(true);
		}
        		
		editProfileButton = (ImageButton) findViewById(R.id.editProfileButton);
		editProfileButton.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) 
			{
				if (firstName.isEnabled() == false && lastName.isEnabled() == false) 
				{
					firstName.setEnabled(true);
					lastName.setEnabled(true);
					genderRadioGroup.setEnabled(true);
					maleRadioButton.setEnabled(true);
					femaleRadioButton.setEnabled(true);
					maleRadioButton.setSelected(false);
					datePickerButton.setEnabled(false);							
					datePickerButton.setEnabled(true);
					button.setEnabled(true);
					editProfileButton.setImageResource(R.drawable.savechanges);									
				}
				else 
				{					
					editProfileButton.setImageResource(R.drawable.edit);
					firstName.setEnabled(false);
					lastName.setEnabled(false);
					genderRadioGroup.setEnabled(false);
					maleRadioButton.setEnabled(false);
					femaleRadioButton.setEnabled(false);
					femaleRadioButton.setSelected(false);
					maleRadioButton.setSelected(false);
					datePickerButton.setEnabled(false);		
					button.setEnabled(false);
			        String radioButtonValue = "";
			        switch (genderRadioGroup.getCheckedRadioButtonId())
			        {
			        case R.id.maleRadioButton:
			        	radioButtonValue = "Male";
			        break;
			         
			        case R.id.femaleRadioButton:
			        	radioButtonValue = "Female";
			        break;
			        }
			        
					User entry = new User(ProfileActivity.this);					
					entry.open();
					
					// prepare image for sending to server
					String temp = "";
					System.out.println(pic.getId());
					
					if(user.getPicture().equals("") && pic.getId() == 2131230747) {
						System.out.println("NO PIC RE");
					} else {
						
						pic.buildDrawingCache(true);
						Bitmap profilePic = ((BitmapDrawable) pic.getDrawable()).getBitmap();
						ByteArrayOutputStream baos = new  ByteArrayOutputStream();		
						//profilePic = Utils.resizeBitmap(profilePic, 256, 256);
						profilePic.compress(Bitmap.CompressFormat.PNG,100, baos);
						byte [] b=baos.toByteArray();						
						temp = Base64.encodeToString(b, Base64.DEFAULT);		
			        	Utils.setImgTemp(temp);
			        	System.out.println("PIC RE");
			        	System.out.println(entry.updateUserInfo(strvalue, firstName.getText().toString(), lastName.getText().toString(), radioButtonValue, myYear, myMonth+1, myDay, temp, 1));			        	
					}
					
		        	entry.close();
				}
			}
		});
		
		deleteProfileButton = (ImageButton) findViewById(R.id.deleteProfileButton);
		deleteProfileButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ProfileActivity.this);
				 myAlertDialog.setMessage("You are about to delete your profile.\n"+"This action cannot be undone!\n\n"
				 +"Your information will be deleted and you will be logged out.\n"+ "Do you want to proceed?");
				 myAlertDialog.setIcon(R.drawable.warning);
				 myAlertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

				 public void onClick(DialogInterface arg0, int arg1) {
					 
					 User entry = new User(ProfileActivity.this);
					 entry.open();
					 entry.deleteUserInfo(strvalue);
					 entry.close();
					 
					 Intent broadcastIntent = new Intent();
					 broadcastIntent.setAction("com.package.ACTION_LOGOUT");
					 sendBroadcast(broadcastIntent);
					 Intent logInIntent = new Intent(ProfileActivity.this, LoginActivity.class);
					 startActivity(logInIntent);
				 }});
				 
				 myAlertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				       
				  public void onClick(DialogInterface arg0, int arg1) {
					  arg0.cancel();
				  }});
				 myAlertDialog.show();				 
			}
		});
		
	}
	
	private ImageButton.OnClickListener datePickerButtonOnClickListener = new ImageButton.OnClickListener(){		
		@Override
		public void onClick(View v) {
	    // TODO Auto-generated method stub
//	    final Calendar c = Calendar.getInstance();
//	    myYear = c.get(Calendar.YEAR);
//	    myMonth = c.get(Calendar.MONTH);
//	    myDay = c.get(Calendar.DAY_OF_MONTH);
	    showDialog(ID_DATEPICKER);
		}
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {	
		switch(id){
		case ID_DATEPICKER:
//	    Toast.makeText(ProfileActivity.this,"- onCreateDialog -", Toast.LENGTH_LONG).show();
		System.out.println(myYear);
	    return new DatePickerDialog(this, myDateSetListener,myYear, myMonth, myDay);
	    default:
	    return null;
	    }
	 }
    
	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener(){
		 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//			 String date = "Year: " + String.valueOf(year) + "\n" + "Month: " + String.valueOf(monthOfYear+1) + "\n" + "Day: " + String.valueOf(dayOfMonth);
//			 String sYear = String.valueOf(year);
//			 String sMonth = String.valueOf(monthOfYear);
//			 String sDay = String.valueOf(dayOfMonth);
//			 String con = sYear + "-" + sMonth + "-" + sDay;
//		        java.util.Calendar cal = java.util.Calendar.getInstance();
//			    java.util.Date utilDate = cal.getTime();
//			    final java.sql.Timestamp sqlDate = new  java.sql.Timestamp(utilDate.getTime());
//			 System.out.println(sqlDate);
			 myYear = year;
			 myMonth = monthOfYear;
			 myDay = dayOfMonth;
			 String labelDate = String.valueOf(dayOfMonth)+ " , " + String.valueOf(monthOfYear+1) + " , " + String.valueOf(year);
//			 Toast.makeText(ProfileActivity.this, date, Toast.LENGTH_LONG).show();
			 dobLabel.setText("Date of Birth: " + labelDate);
		 } 
	 };
	 
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    if (resultCode != RESULT_OK) return;		   
		    switch (requestCode) {
			    case CAMERA_PHOTO:
			    	doCrop();			    	
			    	break;			    	
			    case SELECT_PHOTO: 
			    	mImageCaptureUri = data.getData();			    	
			    	doCrop();		    
			    	break;	    			    
			    case CROP_FROM_CAMERA:	    	
			        Bundle extras = data.getExtras();		
			        if (extras != null) {	        	
			            Bitmap photo = extras.getParcelable("data");			           
			            pic.setImageBitmap(photo);
			            pic.setId(12345);
			        }		
			        File f = new File(mImageCaptureUri.getPath());            			        
			        if (f.exists()) f.delete();		
			        break;
		    }
		}
		
		private void doCrop() {
	        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();	 
	        Intent intent = new Intent("com.android.camera.action.CROP");
	        intent.setType("image/*");	 
	        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );	 
	        int size = list.size();	 
	        if (size == 0) {
	            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
	            return;
	        } else {
	            intent.setData(mImageCaptureUri);	 
	            intent.putExtra("outputX", 200);
	            intent.putExtra("outputY", 200);
	            intent.putExtra("aspectX", 1);
	            intent.putExtra("aspectY", 1);
	            intent.putExtra("scale", true);
	            intent.putExtra("return-data", true);
	 
	            if (size == 1) {
	                Intent i        = new Intent(intent);
	                ResolveInfo res = list.get(0);	 
	                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));	 
	                startActivityForResult(i, CROP_FROM_CAMERA);
	            } else {
	                for (ResolveInfo res : list) {
	                    final CropOption co = new CropOption();
	 
	                    co.title    = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
	                    co.icon     = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
	                    co.appIntent= new Intent(intent);
	 
	                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	 
	                    cropOptions.add(co);
	                }
	 
	                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);	 
	                AlertDialog.Builder builder = new AlertDialog.Builder(this);
	                builder.setTitle("Choose Crop App");
	                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
	                    public void onClick( DialogInterface dialog, int item ) {
	                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
	                    }
	                });
	 
	                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
	                    @Override
	                    public void onCancel( DialogInterface dialog ) {	 
	                        if (mImageCaptureUri != null ) {
	                            getContentResolver().delete(mImageCaptureUri, null, null );
	                            mImageCaptureUri = null;
	                        }
	                    }
	                } );	 
	                AlertDialog alert = builder.create();	 
	                alert.show();
	            }
	        }
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.activity_profile, menu);		
		return true;
	}
	
    public void onBackPressed() 
    {
    	if(firstName.isEnabled() == false) {
    		finish();
    		
    	} else {
	    	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ProfileActivity.this);
			myAlertDialog.setMessage("Your changes will not be saved!\n" + "Please use the save button at the top to save your changes, or continue to cancel them.");
			myAlertDialog.setIcon(R.drawable.warning);
			myAlertDialog.setPositiveButton("Discard changes", new DialogInterface.OnClickListener() {
	
				public void onClick(DialogInterface arg0, int arg1) {
					finish();	    	
				}});
			 
			myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			       
				public void onClick(DialogInterface arg0, int arg1) {
					 arg0.cancel();
				}});
			 
			myAlertDialog.show();
    	}
    }
}