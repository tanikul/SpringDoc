package com.java.doc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import com.java.doc.model.BookSendOut;
import com.java.doc.model.Users;
import com.java.doc.service.AttachmentService;
import com.java.doc.service.BookSendOutService;
import com.java.doc.service.DivisionService;
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;
import com.java.doc.service.UserService;
import com.java.doc.util.Constants;
import com.java.doc.util.UtilDateTime;

@Controller
public class ExternalController {

	private static final Logger logger = Logger.getLogger(ExternalController.class);
	
	@Autowired
	@Qualifier(value = "typeQuickService")
	private TypeQuickService typeQuick;
	
	@Autowired
	@Qualifier(value = "typeSecretService")
	private TypeSecretService typeSecret;
	
	@Autowired
	@Qualifier(value = "divisionService")
	private DivisionService divisions;
	
	@Autowired
	@Qualifier(value = "bookSendOutService")
	private BookSendOutService sendout;
	
	@Autowired
	@Qualifier(value = "attachmentService")
	private AttachmentService attachmentService;
	
	@Autowired
	@Qualifier(value = "userService")
	private UserService userService;
	
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
	
	@RequestMapping(value = "addexternal", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView AddExternal(HttpServletRequest request) throws ParseException {
		ModelAndView model = new ModelAndView();
		try{
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.add(Calendar.YEAR, 543); // to get previous year add -1
			Date now = cal.getTime();
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
			BookSendOut sendOut = new BookSendOut();
			sendOut.setBsRdate(now);
			sendOut.setBsDate(now);
			sendOut.setBsId(this.sendout.LastID() + 1);
			sendOut.setBsNum(this.sendout.getNextBsNum(UtilDateTime.getCurrentYear()));
			sendOut.setBsYear(UtilDateTime.getCurrentYear());
			sendOut.setBsTypeQuick(1);
			sendOut.setBsTypeSecret(1);
			sendOut.setBsFrom("สำนักผังเมือง");
			sendOut.setBsPlace("กท /" + sendOut.getBsNum());
			
			model.addObject("sendOut", sendOut);
			model.addObject("quick", this.typeQuick.listTypeQuick());
			model.addObject("secret", this.typeSecret.listTypeSecret());
			model.addObject("divisions", this.divisions.selectDivision());
			model.addObject("lastId", sendOut.getBsNum());
			model.addObject("mode", "add");
			model.setViewName("external");
		}catch(Exception ex){
			logger.error("AddExternal : " , ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/external/edit", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView Edit(@RequestParam("id")int id, HttpServletRequest request ) {
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
			BookSendOut sendOut = sendout.getDataFromId(id);
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(sendOut.getBsRdate());
			cal.add(Calendar.YEAR, 543); // to get previous year add -1
			Date bsRDate = cal.getTime();
			sendOut.setBsRdate(bsRDate);
			Calendar cal2 = Calendar.getInstance(Locale.US);
			cal2.setTime(sendOut.getBsDate());
			cal2.add(Calendar.YEAR, 543); // to get previous year add -1
			Date bsDate = cal2.getTime();
			sendOut.setBsDate(bsDate);
			model.addObject("role", user.getRole());
			model.addObject("sendOut", sendOut);
			model.addObject("quick", this.typeQuick.listTypeQuick());
			model.addObject("disable", (!user.getRole().equals("ADMIN")) ? "true" : "");
			model.addObject("secret", this.typeSecret.listTypeSecret());
			model.addObject("divisions", this.divisions.selectDivision());
			model.addObject("lastId", sendOut.getBsNum());
			model.addObject("attachments", this.attachmentService.listAttachment(id, Constants.OBJECT_NAME_BOOK_SEND_OUT));
			model.addObject("mode", "edit");
			model.setViewName("external");
		}catch(Exception ex){
			logger.error("Edit : " , ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/external/edit", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean saveEdit(@ModelAttribute("sendOut") BookSendOut pet, HttpServletRequest request) {
		boolean result = false;
		try{
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(pet.getBsRdate());
			cal.add(Calendar.YEAR, -543);
			Date bsRDate = cal.getTime();
			pet.setBsRdate(bsRDate);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(pet.getBsDate());
			cal2.add(Calendar.YEAR, -543);
			Date bsDate = cal2.getTime();
			pet.setBsDate(bsDate);
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
			pet.setUpdatedBy(user.getId().toString());
			pet.setDivision(user.getDivision());
			if(sendout.updateBookOut(pet)) {
				String attachmentIdList = request.getParameter("attachmentIdList");
				int objectId = sendout.LastID();
				attachmentService.updateObjectId(attachmentIdList, objectId, Constants.OBJECT_NAME_BOOK_SEND_OUT);
				result = true;
			}
		}catch(Exception ex){
			logger.error("saveEdit : " , ex);
		}
		return result;
	}
	
	@RequestMapping(value = "/saveSendOut", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean post(@ModelAttribute("sendOut") BookSendOut pet, HttpServletRequest request) {
		boolean result = false;
		try{
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(pet.getBsRdate());
			cal.add(Calendar.YEAR, -543);
			Date bsRDate = cal.getTime();
			pet.setBsRdate(bsRDate);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(pet.getBsDate());
			cal2.add(Calendar.YEAR, -543);
			Date bsDate = cal2.getTime();
			pet.setBsDate(bsDate);
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
			if(sendout.saveBookOut(pet)) {
				String attachmentIdList = request.getParameter("attachmentIdList");
				int objectId = sendout.LastID();
				attachmentService.updateObjectId(attachmentIdList, objectId, Constants.OBJECT_NAME_BOOK_SEND_OUT);
				result = true;
			}
		}catch(Exception ex){
			logger.error("saveSendOut : " , ex);
		}
		return result;
	}
	
	@RequestMapping(value = "/external/upload", method = RequestMethod.POST)
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
                Attachment attachment = new Attachment(name, id, Constants.OBJECT_NAME_BOOK_SEND_OUT, 
                		file.getOriginalFilename(), file.getContentType(), file.getBytes());
                int attachmentId = attachmentService.save(attachment);
                
                message += (!"".equals(message) ? "," : "") + attachmentId;
            } catch (Exception e) {
            	logger.error("You failed to upload " + name + " => " , e);
            }
        }
        return message;
    }
}
