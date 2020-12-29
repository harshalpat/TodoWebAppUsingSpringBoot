package com.harshal.springboot.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("name")
public class WelcomeController { 
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	// to return to the browser as it is!
	//@ResponseBody  
	public String showWelcomePage(ModelMap model) {
		// search for view named "login" and coz of view resolver it returns login.jsp
		model.put("name", getLoggedInUserName());
		return "welcome"; 
	}
	
	private String getLoggedInUserName() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			return ((UserDetails) principal).getUsername();
		}
		return principal.toString();
	}
} 
