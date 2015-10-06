package com.java.doc.controller;

import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.doc.model.BookReciveOut;
import com.java.doc.service.BookReciveOutService;
import com.java.doc.service.DivisionService;
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;

@Controller
public class InternalController {

	private static final Logger logger = Logger.getLogger(InternalController.class);
	
	private TypeQuickService typeQuick;
	private TypeSecretService typeSecret;
	private DivisionService divisions;
	private BookReciveOutService reciveout;
	
	@Autowired(required = true)
	@Qualifier(value = "TypeSecretService")
	public void setTypeSecret(TypeSecretService typeSecret) {
		this.typeSecret = typeSecret;
	}

	@Autowired(required = true)
	@Qualifier(value = "TypeQuickService")
	public void setTypeQuick(TypeQuickService typeQuick) {
		this.typeQuick = typeQuick;
	}
	
	@Autowired(required = true)
	@Qualifier(value = "divisionService")
	public void setDivisions(DivisionService divisions) {
		this.divisions = divisions;
	}
	
	@Autowired(required = true)
	@Qualifier(value = "recive")
	public void setReciveout(BookReciveOutService reciveout) {
		this.reciveout = reciveout;
	}
	
	@RequestMapping(value = "/addinternal", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView index(){
		ModelAndView model = new ModelAndView();
		model.addObject("sendRecive", new BookReciveOut());
		model.addObject("quick", this.typeQuick.listTypeQuick());
		model.addObject("secret", this.typeSecret.listTypeSecret());
		model.addObject("divisions", this.divisions.selectDivision());
		Locale locale = new Locale("th", "TH");
		Locale.setDefault(locale);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
		String d = format.format(new Date());
		model.addObject("date", d);
		model.addObject("lastId", this.reciveout.LastID());
		model.setViewName("internal");
		return model;
	}
	
	@RequestMapping(value = "/saveRecive", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean post(@ModelAttribute("sendRecive") BookReciveOut pet) {
		boolean result = false;
		if(reciveout.SaveReciveOut(pet)){
			result = true;
		}
		return result;
	}
	
	@RequestMapping(value = "/internal/edit", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView Edit(@RequestParam("id")int id ) {
		ModelAndView model = new ModelAndView();
		model.addObject("sendRecive", new BookReciveOut());
		model.addObject("quick", this.typeQuick.listTypeQuick());
		model.addObject("secret", this.typeSecret.listTypeSecret());
		model.addObject("divisions", this.divisions.selectDivision());
		Locale locale = new Locale("th", "TH");
		Locale.setDefault(locale);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"dd/MM/yyyy", java.util.Locale.getDefault());
		String d = format.format(new Date());
		model.addObject("date", d);
		model.addObject("obj", reciveout.getDataFromId(id));
		model.setViewName("edit_internal");
		return model;
	}
	
	@RequestMapping(value = "/internal/edit", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean Edit(@ModelAttribute("sendReciveForm") BookReciveOut recive) {
		boolean result = false;
		if(reciveout.SaveReciveOut(recive)){
			result = true;
		}
		return result;
	}
}
