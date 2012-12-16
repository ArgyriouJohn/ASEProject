package com.aseproject.utilities;

import java.util.List;


import com.facebook.GraphPlace;
import com.facebook.GraphUser;

import android.app.Application;

/**
* Use a custom Application class to pass state data between Activities.
* @author Socratis Michaelides 2012
*/
public class AseMapApplication extends Application
{
	    boolean isLogin;
	    public boolean isFbLogin;
	    boolean isGPSenabled;
	    boolean isNetworkEnabled;
	    private List<GraphUser> selectedUsers;
		private GraphPlace selectedPlace;
  
	    public GraphPlace getSelectedPlace() 
	    {
	        return selectedPlace;
	    }

	    public void setSelectedPlace(GraphPlace place) 
	    {
	        this.selectedPlace = place;
	    }
	 
	    public List<GraphUser> getSelectedUsers() 
	    {
	        return selectedUsers;
	    }

	    public void setSelectedUsers(List<GraphUser> users) 
	    {
	        selectedUsers = users;
	    }
	    public void setLoggedInStatus(boolean status) 
	    {
	    	this.isLogin = status;
	    } 
	    public boolean getLoggedInStatus() 
	    {
	    	return isLogin;
	    }
	    
	    public void setFBloginStatus(boolean status) 
	    {
	    	this.isFbLogin = status;
	    	
	    } 
	    public boolean getFBloginStatus() 
	    {
	    	return isFbLogin;
	    }

	    public void setNetworkStatus(boolean status)
	    {
	    	this.isNetworkEnabled = status;
	    }
	    
	    public boolean getNetworkStatus() 
	    {
	        return isNetworkEnabled;
	    }
	    
	    public void setGPSstatus(boolean status)
	    {
	    	this.isGPSenabled = status;
	    }
	    
	    public boolean getGPSstatus() 
	    {
	        return isGPSenabled;
	    }
    
	}
