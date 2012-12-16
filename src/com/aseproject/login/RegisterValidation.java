package com.aseproject.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {

		
		boolean checkUsername(String username) {
			
			if(username == null || username.equals("")) {
				return false;
			}
			return true;			
		}
		
		
		boolean checkEmail(String email) {
			
			boolean isValid = false;
			String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
			CharSequence input = email;  
			//Make the comparison case-insensitive.  
			Pattern pat= Pattern.compile(regex,Pattern.CASE_INSENSITIVE);  
			Matcher matcher = pat.matcher(input);
			
			if(matcher.matches()) {
				isValid = true;
			}
			return isValid;
		}
		
		
		boolean checkPasswords(String password) {
			 
			if((password==null || password.equals(""))) {
				 return false;
			} else return true;
		}
}
	

