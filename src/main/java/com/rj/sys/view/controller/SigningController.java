package com.rj.sys.view.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.sys.view.model.SignInViewModel;
import com.rj.sys.view.model.ViewName;

@Slf4j
@Controller
public class SigningController {
	
	private final static String viewModelName = "signInViewModel";
	
	/**
	 * Returns the login form when the root '/' is accessed
	 * @param model
	 * @return The login view
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String  home(Model model){
		log.info("Accessing login form");
		SignInViewModel signInViewModel = new SignInViewModel();
		model.addAttribute(viewModelName, signInViewModel);
		return ViewName.loginView;
	}
	
	/**
	 * @param signInViewModel
	 * @param model
	 * @return The login view when user was not successfully logged in and the home page otherwise.
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String home(SignInViewModel signInViewModel, Model model){
		log.info("Login request received for user with name : {}", signInViewModel.getUsername());
		model.addAttribute(viewModelName, new SignInViewModel());
		return ViewName.loginView;//TODO return view base on operation outcome
	}
	
}
