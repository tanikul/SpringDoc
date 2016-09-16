package com.java.doc.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;
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
	private TypeQuickService typeQuickService;
	private TypeSecretService typeSecretService;
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	@Autowired(required = true)
	@Qualifier(value = "divisionService")
	public void setDivisions(DivisionService divisions) {
		this.divisions = divisions;
	}
	
	@Autowired(required = true)
	@Qualifier(value = "TypeQuickService")
	public void setTypeQueickService(TypeQuickService typeQuickService) {
		this.typeQuickService = typeQuickService;
	}
	
	@Autowired(required = true)
	@Qualifier(value = "TypeSecretService")
	public void setTypeSecretService(TypeSecretService typeSecretService) {
		this.typeSecretService = typeSecretService;
	}

	@Autowired(required = true)
	@Qualifier(value = "userService")
	public void setUsers(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
			    /* The user is logged in :) */
			    return new ModelAndView("redirect:/welcome");
			}
			
			model.addObject("login", new LoginValidator());
			model.setViewName("login");
		}catch(Exception ex){
			logger.error("login1 : ", ex);
		}
		return model;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request,
			HttpSession session) {
		ModelAndView model = new ModelAndView();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
			    /* The user is logged in :) */
				UserDetails userDetail = (UserDetails) auth.getPrincipal();
			    return new ModelAndView("redirect:/welcome");
			}
			
			if (error != null) {
				model.addObject("error", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
			}
			if (logout != null) {
				model.addObject("msg", "Logged out successfully.");
			}
			model.addObject("login", new LoginValidator());
			model.setViewName("login");
		}catch(Exception ex){
			logger.error("login : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/checkLogin.json", method = RequestMethod.POST, headers = {"Accept=text/xml, application/json"}, produces = "application/json")
	public @ResponseBody ValidatorResponse checkLogin(@Valid @ModelAttribute("login") LoginValidator login, 
			BindingResult bindingResult, 
			Model model,
			HttpSession session,
			HttpServletRequest request){
		ValidatorResponse rs = new ValidatorResponse();
		try {
			if(bindingResult.hasErrors()){
				rs.setStatus("FAIL");
				List<FieldError> allErrors = bindingResult.getFieldErrors();
				List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
				for(FieldError objError : allErrors){
					errorMessages.add(new ErrorMessage(objError.getField(), objError.getDefaultMessage()));
				}
				rs.setErrorMessageList(errorMessages);
			}else{
				session.setAttribute("typeQuick", this.typeQuickService.SelectQuick());
				session.setAttribute("typeSecret", this.typeSecretService.SelectSecret());
			}
		} catch(Exception ex){
			logger.error("checkLogin : ", ex);
		}
		return rs;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/LoadAccount", method = RequestMethod.POST)
	public @ResponseBody ResponseData<UserTable> loadAccount(HttpServletRequest request){
		ResponseData<UserTable> result = new ResponseData<UserTable>();
		try {
			if(request.isUserInRole("ADMIN")){
				DataTable dt = new DataTable();
				List<UserTable> users = userService.searchUser(dt);
				result.setDraw(1);
				result.setRecordsFiltered(46);
				result.setRecordsTotal(46);
				result.setData(users);
			}	
		}catch(Exception ex){
			logger.error("loadAccount : ", ex);
		}
		return result;
	}
	
	@RequestMapping(value = "/ManageAccount", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView manageAccount(HttpServletRequest request){
		ModelAndView model = new ModelAndView();
		Users user = new Users();
		if(request.getSession().getAttribute("user") == null){
			user = userService.findByUserName(request.getUserPrincipal().getName());
			request.getSession().setAttribute("user", user);
		}else{
			user = (Users) request.getSession().getAttribute("user");
			if(!user.getUsername().equals(request.getUserPrincipal().getName())){
				user = userService.findByUserName(request.getUserPrincipal().getName());
				request.getSession().setAttribute("user", user);
			}
		}
		model.addObject("divisions", this.divisions.selectDivision());
		model.addObject("users", new Users());
		model.addObject("role", user.getRole());
		model.setViewName("manageaccount");
		return model;
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView Edit(@RequestParam("id")int id ) {
		ModelAndView model = new ModelAndView();
		try{
			model.addObject("users", userService.findUserById(id));
			model.addObject("divisions", divisions.selectDivision());
			model.setViewName("edit_user");
		}catch(Exception ex){
			logger.error("Edit : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/saveEditUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String SaveEdit(@RequestParam("id") int id, @RequestParam("fname") String fname, @RequestParam("lname") String lname, @RequestParam("divisionCode") String divisionCode) {
		try{
			Users user = new Users();
			user.setId(id);
			user.setFname(fname);
			user.setLname(lname);
			user.setDivision(divisionCode);
			userService.updateUser(user);
		}catch(Exception ex){
			logger.error("Edit : ", ex);
			return ex.getMessage();
		}
		return "success";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/removeUser", method = RequestMethod.POST)
	public @ResponseBody String Delete(@RequestParam("id")int id, HttpServletRequest request){
		try{
			userService.removeUser(id);
		}catch(Exception ex){
			logger.error("Delete : ", ex);
			return ex.getMessage();
		}
		return "success";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public ModelAndView AddUser(){
		ModelAndView model = new ModelAndView();
		try{
			model.addObject("users", new Users());
			model.addObject("divisions", divisions.selectDivision());
			model.setViewName("add_user");
		}catch(Exception ex){
			logger.error("AddUser_GET : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String AddUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("fname") String fname, @RequestParam("lname") String lname, @RequestParam("division") String division) {
		try {
			Users user = new Users();
			user.setUsername(username);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);
			user.setPassword(hashedPassword);
			user.setFname(fname);
			user.setLname(lname);
			user.setDivision(division);
			user.setRole("USER");
			userService.addUser(user);
		}catch(Exception ex){
			logger.error("AddUser_POST : ", ex);
			return ex.getMessage();
		}
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
