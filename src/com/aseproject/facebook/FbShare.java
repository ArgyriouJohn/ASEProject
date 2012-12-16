package com.aseproject.facebook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.aseproject.map.R;
import com.aseproject.utilities.AseMapApplication;
import com.facebook.GraphObject;
import com.facebook.GraphObjectWrapper;
import com.facebook.GraphPlace;
import com.facebook.GraphUser;
import com.facebook.HttpMethod;
import com.facebook.OpenGraphAction;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This Activity creates a listView with its sublist views and allows sharing on facebook.
 * @author Socratis Michaelides 2012
 * @reference: http://developers.facebook.com/docs/tutorials/androidsdk/3.0/scrumptious/
 * @see BaseListElement
 */
public class FbShare extends Activity 
{
	Button confirmButton , cancelButton;
	//action path
    private static final String POST_ACTION_PATH = "me/ase_maps_project:send";
    //permissions list
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final int REAUTH_ACTIVITY_CODE = 100;
    String TAG = "DEBUG";
    private static final String PENDING_ANNOUNCE_KEY = "pendingAnnounce";
    private boolean pendingAnnounce;
    private ListView listView;
	private List<BaseListElement> listElements;
    private ProgressDialog progressDialog;
    
    /**
     * Called when activity starts.
     *creates a new list view
     *Initialises the main listview
     *gets the active session
     *Initialises the confrim and cancel buttons
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fbshare);
		//set session
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) 
		{
	        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() 
	        {
	            @Override
	            public void onCompleted(GraphUser user, Response response) 
	            {        	
	                if (session == Session.getActiveSession()) 
	                {
	                    if (user != null) {}
	                }
	            }
	        });
	        Request.executeBatchAsync(request);
		}
	    //set listview for facebook options
	    listView = (ListView)findViewById(R.id.selection_list);
	    //initialize facebook
	    init(savedInstanceState);
     
	    confirmButton = (Button) findViewById(R.id.confirmBtn);
	    confirmButton.setOnClickListener(new View.OnClickListener()
	    {
			@Override
			public void onClick(View v)
			{
				handleShare();
			}	   
	    });
     
	  cancelButton = (Button) findViewById(R.id.cancelBtn);
	  cancelButton.setOnClickListener(new View.OnClickListener()
	  {
	
			@Override
			public void onClick(View v) 
			{
			    finish();			
			}	 
	  });
  }
	
	/**
     * Resets the view to the initial defaults.
     */
    private void init(Bundle savedInstanceState) 
    {
        listElements = new ArrayList<BaseListElement>();
      
        listElements.add(new PeopleListElement(0));
        listElements.add(new DescriptionListElement(1));
        listElements.add(new LocationListElement(2));

        if (savedInstanceState != null) 
        {
            for (BaseListElement listElement : listElements) 
            {
                listElement.restoreState(savedInstanceState);
            }
            pendingAnnounce = savedInstanceState.getBoolean(PENDING_ANNOUNCE_KEY, false);
        }
        listView.setAdapter(new ActionListAdapter(getBaseContext(), R.id.selection_list, listElements));
    }
    /**
     * This class defines the friends list element of type BaseListElement
     *creates a listview representing the facebook friends
     *@See BaseListElement
     */
    private class PeopleListElement extends BaseListElement
    {
        private static final String FRIENDS_KEY = "friends";

        private List<GraphUser> selectedUsers;

        public PeopleListElement(int requestCode) 
        {
        	super(getResources().getDrawable(R.drawable.action_people),
        	getResources().getString(R.string.action_people),
        	getResources().getString(R.string.action_people_default),
        	requestCode);
        }

        @Override
		public View.OnClickListener getOnClickListener() 
        {
            return new View.OnClickListener() 
            {
                @Override
                public void onClick(View view)
                {
                   startPickerActivity(PickerActivity.FRIEND_PICKER, getRequestCode());
                }
            };
        }

        @Override
		public void onActivityResult(Intent data) 
        {
            selectedUsers = ((AseMapApplication) getApplication()).getSelectedUsers();
            setUsersText();
            notifyDataChanged();
        }

        @Override
        protected void populateOGAction(OpenGraphAction action) 
        {
            if (selectedUsers != null) 
            {
                action.setTags(selectedUsers);
            }
        }

        @Override
		public void onSaveInstanceState(Bundle bundle) 
        {
            if (selectedUsers != null) 
            {
                bundle.putByteArray(FRIENDS_KEY, getByteArray(selectedUsers));
            }
        }

        @Override
		public boolean restoreState(Bundle savedState) 
        {
            byte[] bytes = savedState.getByteArray(FRIENDS_KEY);
            if (bytes != null) 
            {
                selectedUsers = restoreByteArray(bytes);
                setUsersText();
                return true;
            }
            return false;
        }
        //set users text
        private void setUsersText() 
        {
            String text = null;
            if (selectedUsers != null) 
            {
                if (selectedUsers.size() == 1) 
                {
                    text = String.format(getResources().getString(R.string.single_user_selected),
                    selectedUsers.get(0).getName());
                    Log.d(TAG, text);                          
                } 
                else if (selectedUsers.size() == 2)
                {
					text = String.format(getResources().getString(R.string.two_users_selected),
					 selectedUsers.get(0).getName(), selectedUsers.get(1).getName());                           
                    Log.d(TAG, text);
                } 
                else if (selectedUsers.size() > 2) 
                {
                    text = String.format(getResources().getString(R.string.multiple_users_selected),
                    selectedUsers.get(0).getName(), (selectedUsers.size() - 1));                  
                    Log.d(TAG, text);
                }
            }
            if (text == null) 
            {
                text = getResources().getString(R.string.action_people_default);
            }
            setText2(text);
        }

        private byte[] getByteArray(List<GraphUser> users) 
        {
            // convert the list of GraphUsers to a list of String where each element is
            // the JSON representation of the GraphUser so it can be stored in a Bundle
            List<String> usersAsString = new ArrayList<String>(users.size());

            for (GraphUser user : users) 
            {
                usersAsString.add(user.getInnerJSONObject().toString());
            }
            try 
            {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                new ObjectOutputStream(outputStream).writeObject(usersAsString);
                return outputStream.toByteArray();
            } 
            catch (IOException e) 
            {
                Log.e(TAG, "Unable to serialize users.", e);
            }
            return null;
        }

        private List<GraphUser> restoreByteArray(byte[] bytes) 
        {
            try 
            {
                @SuppressWarnings("unchecked")
                List<String> usersAsString =(List<String>) (new ObjectInputStream(new ByteArrayInputStream(bytes))).readObject();
                if (usersAsString != null) 
                {
                    List<GraphUser> users = new ArrayList<GraphUser>(usersAsString.size());
                    for (String user : usersAsString) 
                    {
                        GraphUser graphUser = GraphObjectWrapper.createGraphObject(new JSONObject(user), GraphUser.class);
                        users.add(graphUser);
                    }
                    return users;
                }
            } 
            catch (ClassNotFoundException e) 
            {
                Log.e(TAG, "Unable to deserialize users.", e);
            } 
            catch (IOException e) 
            {
                Log.e(TAG, "Unable to deserialize users.", e);
            } catch (JSONException e) 
            {
                Log.e(TAG, "Unable to deserialize users.", e);
            }
            return null;
        }
    }
   
    private void startPickerActivity(Uri data, int requestCode) 
    {
        Intent intent = new Intent();
        intent.setData(data);
        intent.setClass(this, PickerActivity.class);
        startActivityForResult(intent, requestCode);
    }
  
        /**
 		* This class defines an ActionList adapter used to manipulate data in the BaseListView listview.
 		* @see BaseListElement
		*/
	  private class ActionListAdapter extends ArrayAdapter<BaseListElement> 
	  {
	        private List<BaseListElement> listElements;
	        /**
   			 * This constructor creates an ActionListAdapter object.
  			 * @param context parent context
   			 * @param resourceId resource id.
  			 * @param listElements List<BaseListElement> list.
  			 */
	        public ActionListAdapter(Context context, int resourceId, List<BaseListElement> listElements) 
	        {
	            super(context, resourceId, listElements);
	            this.listElements = listElements;
	            for (int i = 0; i < listElements.size(); i++) 
	            {
	                listElements.get(i).setAdapter(this);
	            }
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
	            View view = convertView;
	            if (view == null) 
	            {
	                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                view = inflater.inflate(R.layout.listitem, null);
	            }	
	            BaseListElement listElement = listElements.get(position);
	            if (listElement != null) 
	            {
	                view.setOnClickListener(listElement.getOnClickListener());
	                ImageView icon = (ImageView) view.findViewById(R.id.icon);
	                TextView text1 = (TextView) view.findViewById(R.id.text1);
	                TextView text2 = (TextView) view.findViewById(R.id.text2);
	                if (icon != null) 
	                {
	                    icon.setImageDrawable(listElement.getIcon());
	                }
	                if (text1 != null) 
	                {
	                    text1.setText(listElement.getText1());
	                }
	                if (text2 != null) 
	                {
	                    text2.setText(listElement.getText2());
	                }
	            }
	            return view;
	        }
		  }

		    /**
		     * Notifies that the session token has been updated.
		     */
		    public void tokenUpdated()
		    {
		        if (pendingAnnounce)
		        {
		            handleShare();
		        }
		    }
	    
		    @Override
		    public void onActivityResult(int requestCode, int resultCode, Intent data) 
		    {
		        super.onActivityResult(requestCode, resultCode, data);
		        if (requestCode == REAUTH_ACTIVITY_CODE) 
		        {
		            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		        } 
		        else if (resultCode == Activity.RESULT_OK && requestCode >= 0 && requestCode < listElements.size()) 
		        {
		            listElements.get(requestCode).onActivityResult(data);
		        }
		    }
	    
		    @Override
		    public void onSaveInstanceState(Bundle bundle) 
		    {
		        super.onSaveInstanceState(bundle);
		        for (BaseListElement listElement : listElements) 
		        {
		            listElement.onSaveInstanceState(bundle);
		        }
		        bundle.putBoolean(PENDING_ANNOUNCE_KEY, pendingAnnounce);
		    }
		    
      		 /**
 			* This class defines a LocationListElement 
 			* @see BaseListElement
			*/
		    private class LocationListElement extends BaseListElement 
		    {
		        private static final String PLACE_KEY = "place";
	
		        private GraphPlace selectedPlace = null;
	              /**
   				 * This constructor displays the content in the listview
  				 * @param requestCode request code
  				 */
		        public LocationListElement(int requestCode) 
		        {
		            super(getResources().getDrawable(R.drawable.action_location),
            		getResources().getString(R.string.action_location),
            		getResources().getString(R.string.action_location_default),
            		requestCode);
		        }
	            
		        @Override
				public View.OnClickListener getOnClickListener() 
		        {
		            return new View.OnClickListener() 
		            {
		                @Override
		                public void onClick(View view) 
		                {
		                    startPickerActivity(PickerActivity.PLACE_PICKER, getRequestCode());
		                }
		            };
		        }
	
		        @Override
				public void onActivityResult(Intent data) 
		        {
		            selectedPlace = ((AseMapApplication) getApplication()).getSelectedPlace();
		            setPlaceText();
		            notifyDataChanged();
		        }
	
		        @Override
		        protected void populateOGAction(OpenGraphAction action) 
		        {
		            if (selectedPlace != null) 
		            {
		                action.setPlace(selectedPlace);
		            }
		        }
	
		        @Override
				public void onSaveInstanceState(Bundle bundle) 
		        {
		            if (selectedPlace != null) 
		            {
		                bundle.putString(PLACE_KEY, selectedPlace.getInnerJSONObject().toString());
		            }
		        }
	
		        @Override
				public boolean restoreState(Bundle savedState) 
		        {
		            String place = savedState.getString(PLACE_KEY);
		            if (place != null) 
		            {
		                try {
		                    selectedPlace = GraphObjectWrapper.createGraphObject(new JSONObject(place), GraphPlace.class);
		                    setPlaceText();
		                    return true;
		                } 
		                catch (JSONException e) 
		                {
		                    Log.e(TAG, "Unable to deserialize place.", e);
		                }
		            }
		            return false;
		        }
	
		        private void setPlaceText() 
		        {
		            String text = null;
		            if (selectedPlace != null) 
		            {
		                text = selectedPlace.getName();
		            }
		            if (text == null)
		            {
		                text = getResources().getString(R.string.action_location_default);
		            }
		            setText2(text);
		        }
	    }
        /**
    	 * This method handles the facebook posts
    	 */
	    private void handleShare() 
	    {
	        pendingAnnounce = false;
	        Session session = Session.getActiveSession();

	        if (session == null || !session.isOpened()) 
	        {
	            return;
	        }
            //set permissions
	        List<String> permissions = session.getPermissions();
	        if (!permissions.containsAll(PERMISSIONS)) 
	        {
	            pendingAnnounce = true;
	            Session.ReauthorizeRequest reauthRequest = new Session.ReauthorizeRequest(this, PERMISSIONS).
	                    setRequestCode(REAUTH_ACTIVITY_CODE);
	            session.reauthorizeForPublish(reauthRequest);
	            return;
	        }

	        // Show a progress dialog because sometimes the requests can take a while.
	        progressDialog = ProgressDialog.show( FbShare.this, "",
	        getResources().getString(R.string.progress_dialog_text), true);

	        // Run this in a background thread since some of the populate methods may take
	        // a non-trivial amount of time.
	        AsyncTask<Void, Void, Response> task = new AsyncTask<Void, Void, Response>() 
	        {
                //handle the response
	            @Override
	            protected Response doInBackground(Void... voids) 
	            {
	                SentAction sentAction = GraphObjectWrapper.createGraphObject(SentAction.class);
	                for (BaseListElement element : listElements) 
	                {
	                	element.populateOGAction(sentAction);
	                }
	                Request request = new Request(Session.getActiveSession(),
                    POST_ACTION_PATH, null, HttpMethod.POST);
                    request.setGraphObject(sentAction);
	                return request.executeAndWait();
	            }
	            
	            @Override
	            protected void onPostExecute(Response response)
	            {
	                onPostActionResponse(response);
	             }
	        };
	        task.execute();
	    }

	    private void onPostActionResponse(Response response) 
	    {
	        if (progressDialog != null) 
	        {
	            progressDialog.dismiss();
	            progressDialog = null;
	        }
	        String id = getIdFromResponseOrShowError(response);
	        if (id != null) 
	        {
	            String dialogBody = String.format(getResources().getString(R.string.result_dialog_text), id);
	            AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setPositiveButton(R.string.result_dialog_button_text, null).
	            setTitle(R.string.result_dialog_title).setMessage(dialogBody);
	            builder.show();
	            finish();
	        }
	        init(null);
	    }

	    private String getIdFromResponseOrShowError(Response response) 
	    {
	        PostResponse postResponse = response.getGraphObjectAs(PostResponse.class);

	        String id = null;
	        PostResponse.Body body = null;
	        if (postResponse != null) 
	        {
	            id = postResponse.getId();
	            body = postResponse.getBody();
	        }

	        String dialogBody = "";

	        if (body != null && body.getError() != null) 
	        {
	            dialogBody = body.getError().getMessage();
	        } else if (response.getError() != null) 
	        {
	            dialogBody = response.getError().getLocalizedMessage();
	        } else if (id != null) 
	        {
	            return id;
	        }

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setPositiveButton(R.string.error_dialog_button_text, null).
	        setTitle(R.string.error_dialog_title).setMessage(dialogBody);
	        builder.show();
	        return null;
	    }
	    
	    /**
	     *Interface representing the response of type GraphObject
	     */
	    private interface PostResponse extends GraphObject 
	    {
	        Body getBody();
	    
	        String getId();

	        interface Body extends GraphObject 
	        {
	            Error getError();
	        }

	        interface Error extends GraphObject 
	        {
	            String getMessage();            
	        }
	    }
	    
	    /**
	     * Interface representing the Review of type Graph object.
	     */
	    private interface ReviewGraphObject extends GraphObject 
	    {
	        public String getUrl();
	        /**
	        * Method setting the url of the object.
	        *@param url object url
	        */
	        public void setUrl(String url);
	        public String getId();
	        public void setId(String id);
	    }

	    /**
	     * Interface representing the review action.
	     */
	    private interface SentAction extends OpenGraphAction 
	    {
	        public ReviewGraphObject getReview();
	        /**
	        * Method setting the object
	        *@param review review object
	        */
	        public void setReview(ReviewGraphObject review);
	    }
        /**
 		* This class defines an DescriptionListElement of type BaseListElement 
 		* @see BaseListElement
		*/
	    private class DescriptionListElement extends BaseListElement 
	    {
	        private static final String DESCRIPTION_KEY = "description";
	        private static final String DESCRIPTION_URL_KEY = "description_url";

	        private final String[] descriptionChoices;
	        private final String[] descriptionUrls;
	        private String descriptionChoiceUrl = null;
	        private String descriptionChoice = null;
	         /**
 	     	* This constructor sets the content of the listview
 	     	* @param requestCode request code
	    	*/
	        public DescriptionListElement(int requestCode) 
	        {
	            super(getResources().getDrawable(R.drawable.action_describe),
				getResources().getString(R.string.action_describe),
				getResources().getString(R.string.action_describe_default),
				requestCode);
				descriptionChoices = getResources().getStringArray(R.array.description_types);
				descriptionUrls = getResources().getStringArray(R.array.description_og_urls);
	        }

	        @Override
			public View.OnClickListener getOnClickListener() 
	        {
	            return new View.OnClickListener() 
	            {
	                @Override
	                public void onClick(View view)
	                {
	                    showDescriptionOptions();
	                }
	            };
	        }

	        @Override
	        protected void populateOGAction(OpenGraphAction action) 
	        {
	            if (descriptionChoiceUrl != null) 
	            {
	                SentAction sentAction = action.cast(SentAction.class);
	                ReviewGraphObject review = GraphObjectWrapper.createGraphObject(ReviewGraphObject.class);
	                review.setUrl(descriptionChoiceUrl);
	                sentAction.setReview(review);
	            }
	        }

	        @Override
			public void onSaveInstanceState(Bundle bundle) 
	        {
	            if (descriptionChoice != null && descriptionChoiceUrl != null) 
	            {
	                bundle.putString(DESCRIPTION_KEY, descriptionChoice);
	                bundle.putString(DESCRIPTION_URL_KEY, descriptionChoiceUrl);
	            }
	        }

	        @Override
			public boolean restoreState(Bundle savedState) 
	        {
	            String description = savedState.getString(DESCRIPTION_KEY);
	            String descriptionUrl = savedState.getString(DESCRIPTION_URL_KEY);
	            if (description != null && descriptionUrl != null) 
	            {
	                descriptionChoice = description;
	                descriptionChoiceUrl = descriptionUrl;
	                setDescriptionText();
	                return true;
	            }
	            return false;
	        }

	        private void showDescriptionOptions() 
	        {
	            String title = getResources().getString(R.string.select_description);
	            AlertDialog.Builder builder = new AlertDialog.Builder(FbShare.this);
	            builder.setTitle(title).
	                    setCancelable(true).
	                    setItems(descriptionChoices, new DialogInterface.OnClickListener() 
	                    {
	                        @Override
	                        public void onClick(DialogInterface dialogInterface, int i) 
	                        {
	                            descriptionChoice = descriptionChoices[i];
	                            descriptionChoiceUrl = descriptionUrls[i];
	                            setDescriptionText();
	                            notifyDataChanged();
	                        }
	                    });
	            builder.show();
	        }

	        private void setDescriptionText() 
	        {
	            if (descriptionChoice != null && descriptionChoiceUrl != null) 
	            {
	                setText2(descriptionChoice);
	            } 
	            else 
	            {
	                setText2(getResources().getString(R.string.action_describe_default));
	            }
	        }
	    }
}

