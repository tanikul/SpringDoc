package com.java.doc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
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
import com.java.doc.model.BookSendOut;
import com.java.doc.model.DataTable;
import com.java.doc.model.ResponseData;
import com.java.doc.model.UserTable;
import com.java.doc.model.Users;
import com.java.doc.service.BookReciveOutService;
import com.java.doc.service.BookSendOutService;
import com.java.doc.service.DivisionService;
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;
import com.java.doc.service.UserService;
import com.java.doc.util.Constants;
import com.java.doc.util.UtilDateTime;
import com.java.doc.validator.ErrorMessage;
import com.java.doc.validator.LoginValidator;
import com.java.doc.validator.ValidatorResponse;
import com.mysql.jdbc.StringUtils;


@Controller
public class AccountController {

	@Autowired
	@Qualifier(value = "divisionService")
	private DivisionService divisionService;
	
	@Autowired
	@Qualifier(value = "userService")
	private UserService userService;
	
	@Autowired
	@Qualifier(value = "typeQuickService")
	private TypeQuickService typeQuickService;
	
	@Autowired
	@Qualifier(value = "typeSecretService")
	private TypeSecretService typeSecretService;
	
	@Autowired
	@Qualifier(value = "bookSendOutService")
	private BookSendOutService bookSendOutService;
	
	@Autowired
	@Qualifier(value = "bookReciveOutService")
	private BookReciveOutService bookReciveOutService;
	
	private static final Logger logger = Logger.getLogger(AccountController.class);
	
	
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
				Users user = new Users();
				user = userService.findByUserName(login.getUsername());
				if(user.getRole().equals("ADMIN")){
					CompareDataLogin(user);
				}
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
		model.addObject("divisions", this.divisionService.selectDivision());
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
			UserTable user = userService.findUserById(id); 
			model.addObject("users", user);
			model.addObject("divisions", divisionService.selectDivision());
			model.addObject("roles", Constants.getRoles());
			model.addObject("groups", userService.getGroupFromDivisionDropDown(user.getDivisionCode()));
			model.addObject("prefixs", Constants.PREFIXS);
			model.setViewName("edit_user");
		}catch(Exception ex){
			logger.error("Edit : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/saveEditUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String SaveEdit(@RequestParam("id") int id, 
			@RequestParam("fname") String fname, 
			@RequestParam("lname") String lname,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("divisionCode") String divisionCode,
			@RequestParam("role") String role,
			@RequestParam("groupId") Integer groupId,
			@RequestParam("prefix") String prefix) {
		try{
			Users user = userService.getUserById(id);
			user.setFname(fname);
			user.setLname(lname);
			user.setRole(role);
			user.setUsername(username);
			if(!StringUtils.isNullOrEmpty(password)){
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String hashedPassword = passwordEncoder.encode(password);
				user.setPassword(hashedPassword);
			}
			user.setDivision(divisionCode);
			user.setGroupId(groupId);
			user.setPrefix(prefix);
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
			model.addObject("divisions", divisionService.selectDivision());
			model.addObject("roles", Constants.getRoles());
			model.addObject("prefixs", Constants.PREFIXS);
			model.setViewName("add_user");
		}catch(Exception ex){
			logger.error("AddUser_GET : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String AddUser(@RequestParam("username") String username, 
			@RequestParam("password") String password, 
			@RequestParam("fname") String fname, 
			@RequestParam("lname") String lname, 
			@RequestParam("division") String division,
			@RequestParam("role") String role,
			@RequestParam("groupId") Integer groupId,
			@RequestParam("prefix") String prefix) {
		try {
			Users user = new Users();
			user.setUsername(username);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String hashedPassword = passwordEncoder.encode(password);
			user.setPassword(hashedPassword);
			user.setFname(fname);
			user.setLname(lname);
			user.setDivision(division);
			user.setRole(role);
			user.setGroupId(groupId);
			user.setPrefix(prefix);
			userService.addUser(user);
		}catch(Exception ex){
			logger.error("AddUser_POST : ", ex);
			return ex.getMessage();
		}
		return "success";
	}
	
	@RequestMapping(value = "/account/getGroup", method = RequestMethod.POST, headers = {"Accept=text/xml, application/json;charset=UTF-8"}, produces = "application/json")
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String getGroup(@RequestParam("divisionCode") String divisionCode) {
		String mapAsJson = null;
		try {
			 mapAsJson = new ObjectMapper().writeValueAsString(userService.getGroupFromDivisionDropDown(divisionCode));
		}catch(Exception ex){
			logger.error("getGroup : ", ex);
			return ex.getMessage();
		}
		return mapAsJson;
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
	
	private void CompareDataLogin(Users user){
		try {
			Date date = new Date();
		    Calendar cal = Calendar.getInstance(Locale.US);
		    cal.setTime(date);
		    int year = cal.get(Calendar.YEAR);
		    year = year + 543;
		    int cntRiciveOut = bookReciveOutService.getCountDataBookReciveOut(year);
		    int cntSendOut = bookSendOutService.getCountDataBookSendOut(year);
		    int cntRiciveOutExc = getCntRowReciveInExcel(year);
		    int cntSendOutExc = getCntRowSendOutInExcel(year);
		    if(cntRiciveOutExc > cntRiciveOut){
		    	insertRowReciveFromExcel(year, user);
		    }else if(cntRiciveOutExc < cntRiciveOut){
		    	insertExcelReciveFromDB(year, user);
		    }
		    if(cntSendOutExc > cntSendOut){
		    	insertRowSendOutFromExcel(year, user);
		    }else if(cntSendOutExc < cntSendOut){
		    	insertExcelSendOutFromDB(year, user);
		    }
		} catch (Exception ex){
			logger.error("CompareDataLogin : ", ex);
		}
	}
	
	private int getCntRowReciveInExcel(int year){
		int rs = 0;
		try{
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelRecievePath").split("\\.");
			String fileName = cons.getProperty("excelRecievePath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + year + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	if(f.exists() && !f.isDirectory()) { 
        		FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(inputStream);
                int cntSheet = workbook.getNumberOfSheets();
                for(int i = 0; i < cntSheet; i ++){
                	Sheet sheet = workbook.getSheetAt(i);
                	rs += sheet.getLastRowNum();
                } 
                inputStream.close();
        	}
		}catch(Exception ex){
			logger.error("getCntRowReciveInExcel : ", ex);
		}
		return rs;
	}
	
	private int getCntRowSendOutInExcel(int year){
		int rs = 0;
		try{
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelSendOutPath").split("\\.");
			String fileName = cons.getProperty("excelSendOutPath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + year + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	if(f.exists() && !f.isDirectory()) { 
        		FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(inputStream);
                int cntSheet = workbook.getNumberOfSheets();
                for(int i = 0; i < cntSheet; i ++){
                	Sheet sheet = workbook.getSheetAt(i);
                	rs += sheet.getLastRowNum();
                } 
                inputStream.close();
        	}
		}catch(Exception ex){
			logger.error("getCntRowSendOutInExcel : ", ex);
		}
		return rs;
	}
	
	private void insertRowReciveFromExcel(int year, Users user){
		try{
			BookReciveOut bookReciveOut = bookReciveOutService.getLastRowOfYear(year);
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelRecievePath").split("\\.");
			String fileName = cons.getProperty("excelRecievePath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + year + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	if(f.exists() && !f.isDirectory()) { 
        		FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(inputStream);
                int cntSheet = workbook.getNumberOfSheets() - 1;
                Sheet sheet = workbook.getSheetAt(cntSheet);
                Iterator<Row> iterator = sheet.iterator();
                boolean chk = false;
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    if(!chk){
	                    Cell cell = row.getCell(2);
	                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_STRING:
		                        continue;
		                    case Cell.CELL_TYPE_NUMERIC:
		                    	int brNum = (int) cell.getNumericCellValue();
		                    	if(bookReciveOut == null){
		                    		chk = true;
		                    	}else if(brNum == bookReciveOut.getBrNum()){
		                        	chk = true;
		                        	continue;
		                        }
		                        break;
		                }
                    }
                    if(chk){
                    	BookReciveOut br = new BookReciveOut();
                    	br.setBrYear(row.getCell(0) == null ?  null : (int) row.getCell(0).getNumericCellValue());
                    	if(row.getCell(1) != null){
                        	br.setBrRdate(UtilDateTime.convertDateThaiToDate(row.getCell(1).toString()));
                        }
                    	br.setBrNum(row.getCell(2) == null ?  null : (int) row.getCell(2).getNumericCellValue());
                    	br.setBrPlace(row.getCell(3) == null ?  "" : row.getCell(3).getStringCellValue());
                    	if(row.getCell(4) != null){
                        	br.setBrDate(UtilDateTime.convertDateThaiToDate(row.getCell(4).toString()));
                        }
                    	br.setBrFrom(row.getCell(5) == null ?  "" : row.getCell(5).getStringCellValue());
                    	br.setBrToDepartment(row.getCell(6) == null ?  "" : row.getCell(6).getStringCellValue());
                    	br.setBrSubject(row.getCell(7) == null ?  "" : row.getCell(7).getStringCellValue());
                        br.setBrTypeQuick(row.getCell(8) == null ?  0 : (int)row.getCell(8).getNumericCellValue());
                        br.setBrTypeSecret(row.getCell(9) == null ?  0 : (int)row.getCell(9).getNumericCellValue());
                        br.setBrRemark(row.getCell(10) == null ?  "" : row.getCell(10).getStringCellValue());
                        br.setCreatedBy(user.getId().toString());
                        br.setCreatedDate(new Date());
                        br.setDivision(user.getDivision());
                        bookReciveOutService.SaveReciveOutFromExcel(br);
                    }
                }
                inputStream.close();
        	}
		}catch(Exception ex){
			logger.error("insertRowReciveFromExcel : ", ex);
		}
	}
	
	private void insertExcelReciveFromDB(int year, Users user){
		try{
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelRecievePath").split("\\.");
			String fileName = cons.getProperty("excelRecievePath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + year + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	if(f.exists() && !f.isDirectory()) { 
        		FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(inputStream);
                int cntSheet = workbook.getNumberOfSheets() - 1;
                Sheet sheet = workbook.getSheetAt(cntSheet);
                Iterator<Row> iterator = sheet.iterator();
                int brNum = 0;
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    Cell cell = row.getCell(2);
                    switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_STRING:
	                        continue;
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	brNum = (int) cell.getNumericCellValue();
	                        break;
	                }
                }
                if(brNum > 0){
                	List<BookReciveOut> list = bookReciveOutService.listReciveByYearAndBrNum(year, brNum);
                	for(BookReciveOut re : list){
            			bookReciveOutService.insertBrToExcel(re);
            		}
                }
        	}else{
        		List<BookReciveOut> list = bookReciveOutService.listReciveByYear(year);
        		for(BookReciveOut re : list){
        			bookReciveOutService.insertBrToExcel(re);
        		}
        	}
		}catch(Exception ex){
			logger.error("insertExcelReciveFromDB : ", ex);
		}
	}
	
	private void insertRowSendOutFromExcel(int year, Users user){
		try{
			BookSendOut bookSendOut = bookSendOutService.getLastRowOfYear(year);
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelSendOutPath").split("\\.");
			String fileName = cons.getProperty("excelSendOutPath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + year + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	if(f.exists() && !f.isDirectory()) { 
        		FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(inputStream);
                int cntSheet = workbook.getNumberOfSheets() - 1;
                Sheet sheet = workbook.getSheetAt(cntSheet);
                Iterator<Row> iterator = sheet.iterator();
                boolean chk = false;
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    if(!chk){
	                    Cell cell = row.getCell(3);
	                    switch (cell.getCellType()) {
		                    case Cell.CELL_TYPE_STRING:
		                        continue;
		                    case Cell.CELL_TYPE_NUMERIC:
		                    	int brNum = (int) cell.getNumericCellValue();
		                    	if(bookSendOut == null){
		                    		chk = true;
		                    	}else if(brNum == bookSendOut.getBsNum()){
		                        	chk = true;
		                        	continue;
		                        }
		                        break;
		                }
                    }
                    if(chk){
                    	BookSendOut bs = new BookSendOut();
                    	bs.setBsYear(row.getCell(0) == null ?  null : (int) row.getCell(0).getNumericCellValue());
                    	if(row.getCell(1) != null){
                        	bs.setBsRdate(UtilDateTime.convertDateThaiToDate(row.getCell(1).toString()));
                        }
                    	bs.setBsDivision(row.getCell(2) == null ?  "" : row.getCell(2).getStringCellValue());
                    	bs.setBsNum(row.getCell(3) == null ?  null :  (int) row.getCell(3).getNumericCellValue());
                    	bs.setBsPlace(row.getCell(4) == null ?  "" : row.getCell(4).getStringCellValue());
                    	if(row.getCell(5) != null){
                        	bs.setBsDate(UtilDateTime.convertDateThaiToDate(row.getCell(5).toString()));
                        }
                    	bs.setBsFrom(row.getCell(6) == null ?  "" : row.getCell(6).getStringCellValue());
                    	bs.setBsTo(row.getCell(7) == null ?  "" : row.getCell(7).getStringCellValue());
                    	bs.setBsSubject(row.getCell(8) == null ?  "" : row.getCell(8).getStringCellValue());
                        bs.setBsTypeQuick(row.getCell(9) == null ?  0 : (int)row.getCell(9).getNumericCellValue());
                        bs.setBsTypeSecret(row.getCell(10) == null ?  0 : (int)row.getCell(9).getNumericCellValue());
                        bs.setBsRemark(row.getCell(11) == null ?  "" : row.getCell(11).getStringCellValue());
                        bs.setCreatedBy(user.getId().toString());
                        bs.setCreatedDate(new Date());
                        bs.setDivision(user.getDivision());
                        bookSendOutService.saveBookOutFromExcel(bs);
                    }
                }
                inputStream.close();
        	}
		}catch(Exception ex){
			logger.error("insertRowSendOutFromExcel : ", ex);
		}
	}
	
	private void insertExcelSendOutFromDB(int year, Users user){
		try{
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelSendOutPath").split("\\.");
			String fileName = cons.getProperty("excelSendOutPath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + year + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	if(f.exists() && !f.isDirectory()) { 
        		FileInputStream inputStream = new FileInputStream(f);
                Workbook workbook = new XSSFWorkbook(inputStream);
                int cntSheet = workbook.getNumberOfSheets() - 1;
                Sheet sheet = workbook.getSheetAt(cntSheet);
                Iterator<Row> iterator = sheet.iterator();
                int bsNum = 0;
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    Cell cell = row.getCell(3);
                    switch (cell.getCellType()) {
	                    case Cell.CELL_TYPE_STRING:
	                        continue;
	                    case Cell.CELL_TYPE_NUMERIC:
	                    	bsNum = (int) cell.getNumericCellValue();
	                        break;
	                }
                }
                if(bsNum > 0){
                	List<BookSendOut> list = bookSendOutService.listSendOutByYearAndBsNum(year, bsNum);
                	for(BookSendOut re : list){
                		bookSendOutService.insertBsToExcel(re);
            		}
                }
        	}else{
        		List<BookSendOut> list = bookSendOutService.listSendOutByYear(year);
        		for(BookSendOut re : list){
        			bookSendOutService.insertBsToExcel(re);
        		}
        	}
		}catch(Exception ex){
			logger.error("insertExcelSendOutFromDB : ", ex);
		}
	}
}
