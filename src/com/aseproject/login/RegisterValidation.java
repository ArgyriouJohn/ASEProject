package com.aseproject.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides the methods that are used to ensure that users put correct info while registering and loging in.
 * @author Thanos Irodotou 2012
 * @author Socratis Michaelides 2012
 */
public class RegisterValidation 
{	
		/**	
	    * This method returns true if the username provided is not a space character or null.
	    * @return true or false 
	    */
		boolean checkUsername(String username) 
		{	
			if(username == null || username.equals("")) 
			{
				return false;
			}
			return true;			
		}
		/**
	    * This method returns true if the email provided is a valid email.
	    * @return true or false 
	    */
		boolean checkEmail(String email) 
		{	
			boolean isValid = false;
			String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
			CharSequence input = email;  
			//Make the comparison case-insensitive.  
			Pattern pat= Pattern.compile(regex,Pattern.CASE_INSENSITIVE);  
			Matcher matcher = pat.matcher(input);
			
			if(matcher.matches()) 
			{
				isValid = true;
			}
			return isValid;
		}
	
		/**
	    * This method returns true if the password is not a space character or null.
	    * @return true or false 
	    */
		boolean checkPasswords(String password) 
		{		 
			if((password==null || password.equals(""))) 
			{
				 return false;
			} 
			else return true;
		}
}
	

