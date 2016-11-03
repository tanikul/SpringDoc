package com.java.doc.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.java.doc.model.Attachment;
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.Users;
import com.java.doc.service.AttachmentService;
import com.java.doc.service.BookReciveOutService;
import com.java.doc.service.DivisionService;
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;
import com.java.doc.service.UserService;
import com.java.doc.util.Constants;
import com.java.doc.util.UtilDateTime;

@Controller
public class InternalController {

	private static final Logger logger = Logger.getLogger(InternalController.class);
	
	private TypeQuickService typeQuick;
	private TypeSecretService typeSecret;
	private DivisionService divisions;
	private BookReciveOutService reciveout;
	private AttachmentService attachmentService;
	private UserService userService;
	
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
	
	@Autowired(required = true)
	@Qualifier(value = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@Autowired(required = true)
	@Qualifier(value = "userService")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
	
	@RequestMapping(value = "/addinternal", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView index(HttpServletRequest request){
		ModelAndView model = new ModelAndView();
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, 543); // to get previous year add -1
			Date now = cal.getTime();
			BookReciveOut sendRecive = new BookReciveOut();
			sendRecive.setBrRdate(now);
			sendRecive.setBrDate(now);
			sendRecive.setBrId(this.reciveout.LastID() + 1);
			sendRecive.setBrNum(this.reciveout.getNextBrNum(UtilDateTime.getCurrentYear()));
			sendRecive.setBrYear(UtilDateTime.getCurrentYear());
			sendRecive.setBrTypeQuick(1);
			sendRecive.setBrTypeSecret(1);
			sendRecive.setBrTo("สำนักผังเมือง");
			
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
			model.addObject("role", user.getRole());
			model.addObject("sendRecive", sendRecive);
			model.addObject("quick", this.typeQuick.listTypeQuick());
			model.addObject("secret", this.typeSecret.listTypeSecret());
			model.addObject("divisions", this.divisions.selectDivision());
			model.addObject("lastId", sendRecive.getBrNum());
			model.addObject("mode", "add");
			model.setViewName("internal");
		}catch(Exception ex){
			logger.error("index : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/internal/edit", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView Edit(@RequestParam("id")int id , HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		try{
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
			model.addObject("role", user.getRole());
			BookReciveOut sendRecive = reciveout.getDataFromId(id);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sendRecive.getBrRdate());
			cal.add(Calendar.YEAR, 543); // to get previous year add -1
			Date brRDate = cal.getTime();
			sendRecive.setBrRdate(brRDate);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(sendRecive.getBrDate());
			cal2.add(Calendar.YEAR, 543); // to get previous year add -1
			Date brDate = cal2.getTime();
			sendRecive.setBrDate(brDate);
			
			
			model.addObject("mode", "edit");
			model.addObject("sendRecive", sendRecive);
			model.addObject("disable", (!user.getRole().equals("ADMIN")) ? "true" : "");
			model.addObject("quick", this.typeQuick.listTypeQuick());
			model.addObject("secret", this.typeSecret.listTypeSecret());
			model.addObject("divisions", this.divisions.selectDivision());
			model.addObject("attachments", this.attachmentService.listAttachment(id, Constants.OBJECT_NAME_BOOK_RECIVE_OUT));
			model.setViewName("internal");
		}catch(Exception ex){
			logger.error("Edit : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/saveRecive", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean post(@ModelAttribute("sendRecive") BookReciveOut pet, HttpServletRequest request) {
		boolean result = false;
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(pet.getBrRdate());
			cal.add(Calendar.YEAR, -543); // to get previous year add -1
			Date brRDate = cal.getTime();
			pet.setBrRdate(brRDate);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(pet.getBrDate());
			cal2.add(Calendar.YEAR, -543); // to get previous year add -1
			Date brDate = cal2.getTime();
			pet.setBrDate(brDate);
			
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
			pet.setCreatedBy(user.getId().toString());
			pet.setCreatedDate(new Date());
			pet.setDivision(user.getDivision());
			if(reciveout.SaveReciveOut(pet)){
				result = true;
				// Update Attachment
				String attachmentIdList = request.getParameter("attachmentIdList");
				int objectId = reciveout.LastID();
				attachmentService.updateObjectId(attachmentIdList, objectId, Constants.OBJECT_NAME_BOOK_RECIVE_OUT);
			}	
		}catch(Exception ex){
			logger.error("post : ", ex);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/internal/edit", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean Edit(@ModelAttribute("sendReciveForm") BookReciveOut recive) {
		boolean result = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(recive.getBrRdate());
		cal.add(Calendar.YEAR, -543); // to get previous year add -1
		Date brRDate = cal.getTime();
		recive.setBrRdate(brRDate);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(recive.getBrDate());
		cal2.add(Calendar.YEAR, -543); // to get previous year add -1
		Date brDate = cal2.getTime();
		recive.setBrDate(brDate);
		
		if(reciveout.SaveReciveOut(recive)){
			result = true;
		}
		return result;
	}
	
	@RequestMapping(value = "/internal/upload", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("name") String[] names,
            @RequestParam("file") MultipartFile[] files,
            @RequestParam("id") int id) {
 
        if (files.length != names.length)
            return "Mandatory information missing";
        String message = "";
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String name = names[i];
            try {
                Attachment attachment = new Attachment(name, id, Constants.OBJECT_NAME_BOOK_RECIVE_OUT, 
                		file.getOriginalFilename(), file.getContentType(), file.getBytes());
                int attachmentId = attachmentService.save(attachment);
                
                message += (!"".equals(message) ? "," : "") + attachmentId;
            } catch (Exception e) {
            	logger.error("uploadMultipleFileHandler : ", e);
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        }
        return message;
    }
}
