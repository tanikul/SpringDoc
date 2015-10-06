package com.java.doc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.doc.model.BookReciveOut;
import com.java.doc.model.DataTable;
import com.java.doc.model.ResponseData;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;
import com.java.doc.service.DivisionService;
import com.java.doc.service.UserService;
import com.java.doc.validator.ErrorMessage;
import com.java.doc.validator.LoginValidator;
import com.java.doc.validator.ValidatorResponse;

import javax.servlet.*;
import javax.servlet.http.*;

@Controller
public class AccountController {

	private DivisionService divisions;
	private UserService userService;
	
	@Autowired(required = true)
	@Qualifier(value = "divisionService")
	public void setDivisions(DivisionService divisions) {
		this.divisions = divisions;
	}
	
	@Autowired(required = true)
	@Qualifier(value = "userService")
	public void setUsers(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
		    /* The user is logged in :) */
		    return new ModelAndView("redirect:/welcome");
		}
		ModelAndView model = new ModelAndView();
		model.addObject("login", new LoginValidator());
		model.setViewName("login");
		return model;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
		    /* The user is logged in :) */
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
		    return new ModelAndView("redirect:/welcome");
		}
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}
		if (logout != null) {
			model.addObject("msg", "Logged out successfully.");
		}
		model.addObject("login", new LoginValidator());
		model.setViewName("login");
		return model;
	}
	
	@RequestMapping(value = "/checkLogin.json", method = RequestMethod.POST, headers = {"Accept=text/xml, application/json"}, produces = "application/json")
	public @ResponseBody ValidatorResponse checkLogin(@Valid @ModelAttribute("login") LoginValidator login, BindingResult bindingResult, Model model){
		ValidatorResponse rs = new ValidatorResponse();
		if(bindingResult.hasErrors()){
			rs.setStatus("FAIL");
			List<FieldError> allErrors = bindingResult.getFieldErrors();
			List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
			for(FieldError objError : allErrors){
				errorMessages.add(new ErrorMessage(objError.getField(), objError.getDefaultMessage()));
			}
			rs.setErrorMessageList(errorMessages);
		}
		return rs;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/LoadAccount", method = RequestMethod.POST)
	public @ResponseBody ResponseData<UserTable> loadAccount(HttpServletRequest request){
		ResponseData<UserTable> result = new ResponseData<UserTable>();
		if(request.isUserInRole("ADMIN")){
			DataTable dt = new DataTable();
			List<UserTable> users = userService.searchUser(dt);
			result.setDraw(1);
			result.setRecordsFiltered(46);
			result.setRecordsTotal(46);
			result.setData(users);
		}		
		return result;
	}
	
	@RequestMapping(value = "/ManageAccount", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView manageAccount(HttpServletRequest request){
		ModelAndView model = new ModelAndView();
		model.addObject("divisions", this.divisions.selectDivision());
		model.addObject("users", new Users());
		model.setViewName("manageaccount");
		return model;
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView Edit(@RequestParam("id")int id ) {
		ModelAndView model = new ModelAndView();
		model.addObject("users", userService.findUserById(id));
		model.addObject("divisions", divisions.selectDivision());
		model.setViewName("edit_user");
		return model;
	}
	
	@RequestMapping(value = "/saveEditUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String SaveEdit(@RequestParam("id") int id, @RequestParam("fname") String fname, @RequestParam("lname") String lname, @RequestParam("divisionCode") String divisionCode) {
		Users user = new Users();
		user.setId(id);
		user.setFname(fname);
		user.setLname(lname);
		user.setDivision(divisionCode);
		userService.updateUser(user);
		return "success";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/removeUser", method = RequestMethod.POST)
	public @ResponseBody String Delete(@RequestParam("id")int id, HttpServletRequest request){
		userService.removeUser(id);
		return "success";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public ModelAndView AddUser(){
		ModelAndView model = new ModelAndView();
		model.addObject("users", new Users());
		model.addObject("divisions", divisions.selectDivision());
		model.setViewName("add_user");
		return model;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String AddUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("fname") String fname, @RequestParam("lname") String lname, @RequestParam("division") String division) {
		Users user = new Users();
		user.setUsername(username);
		user.setPassword(password);
		user.setFname(fname);
		user.setLname(lname);
		user.setDivision(division);
		userService.addUser(user);
		return "success";
	}
	
	private String getErrorMessage(HttpServletRequest request, String key) {
		Exception exception = (Exception) request.getSession().getAttribute(key);
		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else {
			error = "Invalid username and password!";
		}
		return error;
	}

	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			System.out.println(userDetail);

			model.addObject("username", userDetail.getUsername());

		}
		model.setViewName("403");
		return model;
	}
}
