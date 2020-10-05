package com.java.doc.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
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
import com.java.doc.model.Divisions;
import com.java.doc.model.Groups;
import com.java.doc.model.Users;
import com.java.doc.service.AttachmentService;
import com.java.doc.service.BookReciveOutService;
import com.java.doc.service.DivisionService;
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;
import com.java.doc.service.UserService;
import com.java.doc.util.Constants;
import com.java.doc.util.UtilDateTime;
import com.mysql.jdbc.StringUtils;

@Controller
public class InternalController {

	private static final Logger logger = Logger.getLogger(InternalController.class);
	
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
	@Qualifier(value = "bookReciveOutService")
	private BookReciveOutService reciveout;
	
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
	
	@RequestMapping(value = "/addinternal", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView index(HttpServletRequest request){
		ModelAndView model = new ModelAndView();
		try {
			Date date = new Date();
		    Calendar cal = Calendar.getInstance(Locale.US);
		    cal.setTime(date);
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
			sendRecive.setBrToDepartment("-1");
			sendRecive.setBrTo("สำนักการวางผังและพัฒนาเมือง");
			
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
			model.addObject("disableAll", "");
			model.addObject("groups", new HashMap<Integer, String>());
			model.addObject("sections", new HashMap<Integer, String>());
			model.addObject("userGroups", new HashMap<Integer, String>());
			model.addObject("BrToMode", "N");
			model.addObject("role", user.getRole());
			model.addObject("sendRecive", sendRecive);
			model.addObject("quick", this.typeQuick.listTypeQuick());
			model.addObject("secret", this.typeSecret.listTypeSecret());
			model.addObject("divisions", this.divisions.selectDivision());
			model.addObject("lastId", sendRecive.getBrNum());
			model.addObject("mode", "add");
			model.setViewName("internal");
		}catch(Exception ex){
			logger.error("addinternal : ", ex);
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
			BookReciveOut sendRecive = reciveout.getDataFromId(id, user);
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(sendRecive.getBrRdate());
			cal.add(Calendar.YEAR, 543); // to get previous year add -1
			Date brRDate = cal.getTime();
			sendRecive.setBrRdate(brRDate);
			Calendar cal2 = Calendar.getInstance(Locale.US);
			cal2.setTime(sendRecive.getBrDate());
			cal2.add(Calendar.YEAR, 543); // to get previous year add -1
			Date brDate = cal2.getTime();
			sendRecive.setBrDate(brDate);
			
			model.addObject("BrToMode", "N");
			model.addObject("role", user.getRole());
			model.addObject("mode", "edit");
			model.addObject("sendRecive", sendRecive);
			model.addObject("disable", (!user.getRole().equals("ADMIN") || (!StringUtils.isNullOrEmpty(sendRecive.getBrStatus()) && "Y".equals(sendRecive.getBrStatus()))) ? "true" : "");
			model.addObject("disableRemarkDepartment", (!user.getRole().equals("DEPARTMENT") || (!StringUtils.isNullOrEmpty(sendRecive.getBrStatus()) && "Y".equals(sendRecive.getBrStatus()))) ? "true" : "");
			model.addObject("disableRemarkGroup", (!user.getRole().equals("GROUP") || (!StringUtils.isNullOrEmpty(sendRecive.getBrStatus()) && "Y".equals(sendRecive.getBrStatus()))) ? "true" : "");
			model.addObject("disableRemarkUser", (!user.getRole().equals("USER") || (!StringUtils.isNullOrEmpty(sendRecive.getBrStatus()) && "Y".equals(sendRecive.getBrStatus()))) ? "true" : "");
			model.addObject("disableAll", (!StringUtils.isNullOrEmpty(sendRecive.getBrStatus()) && "Y".equals(sendRecive.getBrStatus())) ? "true" : "");
			model.addObject("quick", this.typeQuick.listTypeQuick());
			model.addObject("secret", this.typeSecret.listTypeSecret());			
			if(user.getRole().equals("ADMIN")){
				model.addObject("divisions", this.divisions.selectDivision());
				model.addObject("brTo", sendRecive.getBrToDepartment());
				model.addObject("brToGroup", sendRecive.getBrToGroup());
				model.addObject("brToSection", sendRecive.getBrToSection());
				model.addObject("brToUser", sendRecive.getBrToUser());
			}else if(user.getRole().equals("DEPARTMENT")){
				model.addObject("groups", userService.getGroupFromDivisionDropDown(sendRecive.getBrToDepartment()));
				model.addObject("brTo", sendRecive.getBrToGroup());
				model.addObject("brToUser", sendRecive.getBrToUser());
			}else if(user.getRole().equals("GROUP")){
				model.addObject("userGroups", userService.getUserFromGroupDropDown(sendRecive.getBrToGroup()));
				model.addObject("brToSection", sendRecive.getBrToSection());
				model.addObject("brTo", sendRecive.getBrToUser());
				model.addObject("brToUser", sendRecive.getBrToUser());
				model.addObject("brToGroup", sendRecive.getBrToGroup());
			}else if(user.getRole().equals("SECTION")){
				model.addObject("userGroups", userService.getSectionFromGroupDropDown(sendRecive.getBrToSection()));
				model.addObject("brTo", sendRecive.getBrToUser());	
				model.addObject("brToSection", sendRecive.getBrToSection());
				model.addObject("brToUser", sendRecive.getBrToUser());
			}
			model.addObject("attachments", this.attachmentService.listAttachment(id, Constants.OBJECT_NAME_BOOK_RECIVE_OUT));
			model.setViewName("internal");
		}catch(Exception ex){
			logger.error("Edit : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/saveRecive", method = RequestMethod.POST, produces="application/json;charset=UTF-8",headers = {"Accept=text/xml, application/json"})
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String post(@ModelAttribute("sendRecive") BookReciveOut pet, HttpServletRequest request) throws ServletException {
		String result = Constants.FAIL;
		try{
			Calendar cal = Calendar.getInstance(Locale.US);
			cal.setTime(pet.getBrRdate());
			cal.add(Calendar.YEAR, -543);
			Date brRDate = cal.getTime();
			pet.setBrRdate(brRDate);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(pet.getBrDate());
			cal2.add(Calendar.YEAR, -543);
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
			pet.setUpdatedBy(user.getId().toString());
			pet.setDivision(user.getDivision());
			int id = reciveout.SaveReciveOut(pet);
			if(id > 0){
				// Update Attachment
				String attachmentIdList = request.getParameter("attachmentIdList");
				attachmentService.updateObjectId(attachmentIdList, id, Constants.OBJECT_NAME_BOOK_RECIVE_OUT);
				result = Constants.SUCCESS;
			}	
		}catch(Exception ex){
			logger.error("saveRecive : ", ex);
			throw new ServletException(ex); 
		}
		
		return result;
	}
	
	@RequestMapping(value = "/internal/edit", method = RequestMethod.POST, produces="application/json;charset=UTF-8",headers = {"Accept=text/xml, application/json"})
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String Edit(@ModelAttribute("sendReciveForm") BookReciveOut recive, HttpServletRequest request) {
		String result = Constants.FAIL;
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
			if(user.getRole().equals("ADMIN")){
				Calendar cal = Calendar.getInstance(Locale.US);
				cal.setTime(recive.getBrRdate());
				cal.add(Calendar.YEAR, -543);
				Date brRDate = cal.getTime();
				recive.setBrRdate(brRDate);
				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(recive.getBrDate());
				cal2.add(Calendar.YEAR, -543);
				Date brDate = cal2.getTime();
				recive.setBrDate(brDate);
			}
			recive.setUpdatedBy(user.getId().toString());
			if(reciveout.updateReciveOut(recive, user.getRole()) == 1){
				if(user.getRole().equals("ADMIN")){
					// Update Attachment
					String attachmentIdList = request.getParameter("attachmentIdList");
					attachmentService.updateObjectId(attachmentIdList, recive.getBrId(), Constants.OBJECT_NAME_BOOK_RECIVE_OUT);
				}
				result = Constants.SUCCESS;
			}	
		}catch(Exception ex){
			logger.error("/internal/edit post : ", ex);
		}
		return result;
	}
	
	@RequestMapping(value = "/internal/upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("name") String[] names,
            @RequestParam("file") MultipartFile[] files,
            @RequestParam("id") int id) {
		String message = "";
		try{
			if (files.length != names.length)
	            return "Mandatory information missing";
	        
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
		}catch(Exception ex){
			logger.error("/internal/upload post : ", ex);
		}
        
        return message;
    }
	
	@RequestMapping(value = "/getGroupSelectedByAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody Map<String, List<String>> getGroupSelectedByAdmin(@RequestParam("departments") String departments) {
		Map<String, List<String>> message = null;
		try{
			message = reciveout.getGroupSelectedByAdmin(departments);
		}catch(Exception ex){
			logger.error("/getGroupSelectedByAdmin : ", ex);
		}
		return message;
	}
	
	@RequestMapping(value = "/getSectionSelectedByAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody Map<String, List<String>> getSectionSelectedByAdmin(@RequestParam("groups") String groups) {
		Map<String, List<String>> message = null;
		try{
			message = reciveout.getSectionSelectedByAdmin(groups);
		}catch(Exception ex){
			logger.error("/getSectionSelectedByAdmin : ", ex);
		}
		return message;
	}
	
	@RequestMapping(value = "/getUserSelectedByAdmin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody Map<String, List<String>> getUserSelectedByAdmin(@RequestParam("groups") String groups, @RequestParam("sections") String sections) {
		Map<String, List<String>> message = null;
		try{
			message = reciveout.getUserSelectedByAdmin(groups, sections);
		}catch(Exception ex){
			logger.error("/getUserSelectedByAdmin : ", ex);
		}
		return message;
	}
}
