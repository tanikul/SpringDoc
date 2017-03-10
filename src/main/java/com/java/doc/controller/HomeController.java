package com.java.doc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.doc.model.Attachment;
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.model.BookSendOut;
import com.java.doc.model.BookSendOutTable;
import com.java.doc.model.PdfForm;
import com.java.doc.model.Users;
import com.java.doc.service.AttachmentService;
import com.java.doc.service.BookReciveOutService;
import com.java.doc.service.BookSendOutService;
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;
import com.java.doc.service.UserService;
import com.java.doc.util.TableSorter;
import com.java.doc.util.UtilDateTime;
import com.java.doc.validator.LoginValidator;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	final static Logger logger = Logger.getLogger(HomeController.class);
	
	private BookSendOutService sendout;
	private BookReciveOutService reciveout;
	private AttachmentService attachmentService;
	private UserService userService;
	private TypeQuickService typeQuickService;
	private TypeSecretService typeSecretService;
	
	@Autowired(required = true)
	@Qualifier(value = "sendoutservice")
	public void setSendout(BookSendOutService sendout) {
		this.sendout = sendout;
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
	
	@Autowired(required = true)
	@Qualifier(value = "TypeQuickService")
	public void setTypeQuickService(TypeQuickService typeQuickService) {
		this.typeQuickService = typeQuickService;
	}
	
	@Autowired(required = true)
	@Qualifier(value = "TypeSecretService")
	public void setTypeSecretService(TypeSecretService typeSecretService) {
		this.typeSecretService = typeSecretService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView model = new ModelAndView();
		try {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				/* The user is logged in :) */
				return new ModelAndView("redirect:/home");
			}
			
			model.addObject("login", new LoginValidator());
			model.setViewName("login");
		}catch(Exception ex){
			logger.error("index : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView home(@RequestParam(value = "type", required = false, defaultValue = "") String type,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		logger.info("Welcome home The client.");
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
			model.addObject("type", type);
			model.setViewName("home");
		}catch(Exception ex){
			logger.error("home : ", ex);
		}
		return model;
	}
	
	@RequestMapping(value = "/loadData", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public  @ResponseBody Map<String, Object> loadData(@RequestBody TableSorter table, HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String, Object> rs = new HashMap<String, Object>();
		try{
			Users user = (Users) request.getSession().getAttribute("user");
			table.setDivision(user.getDivision());
			table.setUserId(user.getId().toString());
			table.setRole(user.getRole());
			List<HashMap<String, String>> rows = new ArrayList<HashMap<String, String>>();
			List<String> headers = new ArrayList<String>();
			headers.add("ปี");
			headers.add(table.getSearch().getType().equals("1") ? "วันที่รับหนังสือ" : "วันที่ส่งหนังสือ");
			headers.add(table.getSearch().getType().equals("1") ? "เลขทะเบียนรับ" : "เลขทะเบียนส่ง");
			headers.add("ที่");
			headers.add("ลงวันที่");
			headers.add("จาก");
			headers.add("ถึง");
			headers.add("เรื่อง");
			headers.add("สถานะ");
			headers.add("ชั้นความลับ");
			headers.add("หมายเหตุ");
			if(table.getRole().equals("ADMIN")){
				headers.add("แก้ไข");
			}else{
				headers.add("ข้อมูล");
			}
			if(table.getSearch().getType().equals("1")){
				BookReciveOutTable bookReciveOutTable = reciveout.ListPageRecive(table);
				request.getSession().setAttribute("listResultRecive", bookReciveOutTable.getSendoutListReport());
				for(BookReciveOut item : bookReciveOutTable.getSendoutList()){
					HashMap<String, String> row = new HashMap<String, String>();
					row.put("id", item.getBrId().toString());
					row.put("ปี", item.getBrYear().toString());
					row.put("วันที่รับหนังสือ", (UtilDateTime.convertToDateTH(item.getBrRdate()) != null) ? UtilDateTime.convertToDateTH(item.getBrRdate()) : "");
					row.put("เลขทะเบียนรับ", (item.getBrNum() != null) ? item.getBrNum().toString() : "");
					row.put("ที่", (item.getBrPlace() != null) ? item.getBrPlace() : "");
					row.put("ลงวันที่", (UtilDateTime.convertToDateTH(item.getBrDate()) != null) ? UtilDateTime.convertToDateTH(item.getBrDate()) : "");
					row.put("จาก", (item.getBrFrom() != null) ? item.getBrFrom() : "");
					row.put("ถึง", (item.getBrTo() != null) ? item.getBrTo() : "");
					row.put("เรื่อง", (item.getBrSubject() != null) ? item.getBrSubject() : "");
					row.put("สถานะ", (item.getBrTypeQuick() != null) ? typeQuickService.getTypeQuickById(item.getBrTypeQuick()) : "");
					row.put("ชั้นความลับ", (item.getBrTypeSecret() != null) ? typeSecretService.getTypeSecretById(item.getBrTypeSecret()) : "");
					row.put("หมายเหตุ", item.getBrRemark());
					
					//row.put("แก้ไข/ลบ", "<span class='glyphicon glyphicon-pencil' id='edit-"+item.getBrId()+"' style='padding-left:6px;cursor:pointer;' onclick='inter.editGet("+ item.getBrId() +");'></span>  <span class='glyphicon glyphicon-trash' style='padding-left:6px;cursor:pointer;' onclick='removeItem(" + item.getBrId() + ", 0, this)'></span>");
					if(table.getRole().equals("ADMIN")){
						row.put("แก้ไข", "<a class='glyphicon glyphicon-pencil' id='edit-"+item.getBrId()+"' style='padding-left:6px;cursor:pointer;' href='internal/edit/?id="+ item.getBrId() + "'></a>");
					}else{
						row.put("ข้อมูล", "<a class='glyphicon glyphicon-search' id='edit-"+item.getBrId()+"' style='padding-left:6px;cursor:pointer;' href='internal/edit/?id="+ item.getBrId() + "'></a>");
					}
					rows.add(row);
				}
				rs.put("total_rows", bookReciveOutTable.getCountSelect());
			}else{
				BookSendOutTable bookSendOutTable = sendout.ListPageSendOut(table);
				
				request.getSession().setAttribute("listResultSend", bookSendOutTable.getSendoutListReport());
				for(BookSendOut item : bookSendOutTable.getSendoutList()){
					HashMap<String, String> row = new HashMap<String, String>();
					row.put("id", item.getBsId().toString());
					row.put("ปี", item.getBsYear().toString());
					row.put("วันที่ส่งหนังสือ", (UtilDateTime.convertToDateTH(item.getBsRdate()) != null) ? UtilDateTime.convertToDateTH(item.getBsRdate()) : "");
					row.put("เลขทะเบียนส่ง", (item.getBsNum() != null) ? item.getBsNum().toString() : "");
					row.put("ที่", (item.getBsPlace() != null) ? item.getBsPlace() : "");
					row.put("ลงวันที่", (UtilDateTime.convertToDateTH(item.getBsDate()) != null) ? UtilDateTime.convertToDateTH(item.getBsDate()) : "");
					row.put("จาก", (item.getBsFrom() != null) ? item.getBsFrom() : "");
					row.put("ถึง", (item.getBsTo() != null) ? item.getBsTo() : "");
					row.put("เรื่อง", (item.getBsSubject() != null) ? item.getBsSubject() : "");
					row.put("สถานะ", item.getBsTypeQuick() != null ? typeQuickService.getTypeQuickById(item.getBsTypeQuick()) : "");
					row.put("ชั้นความลับ", item.getBsTypeSecret() != null ? typeSecretService.getTypeSecretById(item.getBsTypeSecret()) : "");
					row.put("หมายเหตุ", item.getBsRemark());
					if(table.getRole().equals("ADMIN")){
						row.put("แก้ไข", "<a class='glyphicon glyphicon-pencil' id='edit-"+item.getBsId()+"' style='padding-left:6px;cursor:pointer;' href='external/edit/?id="+ item.getBsId() + "'></a>");
					}else{
						row.put("ข้อมูล", "<a class='glyphicon glyphicon-search' id='edit-"+item.getBsId()+"' style='padding-left:6px;cursor:pointer;' href='external/edit/?id="+ item.getBsId() + "'></a>");
					}
					//row.put("แก้ไข/ลบ", "<span class='glyphicon glyphicon-pencil' id='edit-"+item.getBsId()+"' style='padding-left:6px;cursor:pointer;' onclick='ex.editGet("+ item.getBsId() +");'></span>  <span class='glyphicon glyphicon-trash' style='padding-left:6px;cursor:pointer;' onclick='removeItem(" + item.getBsId() + ", 1, this);'></span>");
					rows.add(row);
				}
				rs.put("total_rows", bookSendOutTable.getCountSelect());
			}
			
			rs.put("headers", headers);
			rs.put("rows", rows);
			
		}catch(Exception ex){
			logger.error("loadData : ", ex);
		}
		return rs;
	}
	
	@RequestMapping(value = "/loadDataYear", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> loadDataYear(@RequestParam(value = "type", required = true) String type) throws UnsupportedEncodingException {
		Map<String, Object> rs = new HashMap<String, Object>();
		List<String> years = new ArrayList<String>();
		try {
			if("1".equals(type)) {	
				List<Integer> list = reciveout.getYear();
				for(Integer year : list){
					years.add(String.valueOf(year));
				}
			} else {
				List<Integer> list = sendout.getYear();
				for(Integer year : list){
					years.add(String.valueOf(year));
				}
			}
		}catch(Exception ex){
			logger.error("loadDataYear : ", ex);
		}
		rs.put("years", years);
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportReport", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody ModelAndView doSalesReportPDF(ModelAndView modelAndView, 
			HttpServletRequest request,
			@RequestParam(value = "type") String type) {
		try {
			List<PdfForm> arrTmp = new ArrayList<PdfForm>();
			if(type.equals("IN")){
				List<BookReciveOut> listResult = (List<BookReciveOut>) request.getSession().getAttribute("listResultRecive");
				for (int i = 0; i < listResult.size(); i++) {
					BookReciveOut rs = listResult.get(i);
					PdfForm tmp = new PdfForm();
					tmp.setBrDateStr(UtilDateTime.convertToDateTH(rs.getBrDate()));
					tmp.setBrDivision(rs.getBrDivision());
					tmp.setBrFrom(rs.getBrFrom());
					tmp.setBrId(rs.getBrId());
					tmp.setBrImage(rs.getBrImage());
					tmp.setBrNum(rs.getBrNum());
					tmp.setBrPcode(rs.getBrPcode());
					tmp.setBrPlace(rs.getBrPlace());
					tmp.setBrRdateStr(UtilDateTime.convertToDateTH(rs.getBrRdate()));
					tmp.setBrRemark(rs.getBrRemark());
					tmp.setBrStatus(rs.getBrStatus());
					tmp.setBrSubject(rs.getBrSubject());
					tmp.setBrTo(rs.getBrTo());
					tmp.setBrTypeQuick(rs.getBrTypeQuick());
					tmp.setBrTypeSecret(rs.getBrTypeSecret());
					tmp.setBrYear(rs.getBrYear());
					arrTmp.add(tmp);
				}
			}else{
				List<BookSendOut> listResult = (List<BookSendOut>) request.getSession().getAttribute("listResultSend");
				for (int i = 0; i < listResult.size(); i++) {
					BookSendOut rs = listResult.get(i);
					PdfForm tmp = new PdfForm();
					tmp.setBrDateStr(UtilDateTime.convertToDateTH(rs.getBsDate()));
					tmp.setBrDivision(rs.getBsDivision());
					tmp.setBrFrom(rs.getBsFrom());
					tmp.setBrId(rs.getBsId());
					tmp.setBrImage(rs.getBsImage());
					tmp.setBrNum(rs.getBsNum());
					tmp.setBrPcode(rs.getBsPcode());
					tmp.setBrPlace(rs.getBsPlace());
					tmp.setBrRdateStr(UtilDateTime.convertToDateTH(rs.getBsRdate()));
					tmp.setBrRemark(rs.getBsRemark());
					tmp.setBrStatus(rs.getBsStatus());
					tmp.setBrSubject(rs.getBsSubject());
					tmp.setBrTo(rs.getBsTo());
					tmp.setBrTypeQuick(rs.getBsTypeQuick());
					tmp.setBrTypeSecret(rs.getBsTypeSecret());
					tmp.setBrYear(rs.getBsYear());
					arrTmp.add(tmp);
				}
			}
			
			JRDataSource datasource = new JRBeanCollectionDataSource(arrTmp); 
			Map<String,Object> parameterMap = new HashMap<String,Object>();
			parameterMap.put("datasource", datasource);
			modelAndView = new ModelAndView("pdfReport", parameterMap);
		}catch(Exception ex){
			logger.error("doSalesReportPDF : ", ex);
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/BOOK_RECIEVE_OUT", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody ModelAndView doSalesReportExcel(ModelAndView modelAndView, 
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type") String type) {
		
		
		List<PdfForm> arrTmp = new ArrayList<PdfForm>();
		try {
			
			if(type.equals("IN")){
				List<BookReciveOut> listResult = (List<BookReciveOut>) request.getSession().getAttribute("listResultRecive");
				for (int i = 0; i < listResult.size(); i++) {
					BookReciveOut rs = listResult.get(i);
					PdfForm tmp = new PdfForm();
					tmp.setBrDateStr(UtilDateTime.convertToDateTH(rs.getBrDate()));
					tmp.setBrDivision(rs.getBrDivision());
					tmp.setBrFrom(rs.getBrFrom());
					tmp.setBrId(rs.getBrId());
					tmp.setBrImage(rs.getBrImage());
					tmp.setBrNum(rs.getBrNum());
					tmp.setBrPcode(rs.getBrPcode());
					tmp.setBrPlace(rs.getBrPlace());
					tmp.setBrRdateStr(UtilDateTime.convertToDateTH(rs.getBrRdate()));
					tmp.setBrRemark(rs.getBrRemark());
					tmp.setBrStatus(rs.getBrStatus());
					tmp.setBrSubject(rs.getBrSubject());
					tmp.setBrTo(rs.getBrTo());
					tmp.setBrTypeQuick(rs.getBrTypeQuick());
					tmp.setBrTypeSecret(rs.getBrTypeSecret());
					tmp.setBrYear(rs.getBrYear());
					arrTmp.add(tmp);
				}
			}else{
				List<BookSendOut> listResult = (List<BookSendOut>) request.getSession().getAttribute("listResultSend");
				for (int i = 0; i < listResult.size(); i++) {
					BookSendOut rs = listResult.get(i);
					PdfForm tmp = new PdfForm();
					tmp.setBrDateStr(UtilDateTime.convertToDateTH(rs.getBsDate()));
					tmp.setBrDivision(rs.getBsDivision());
					tmp.setBrFrom(rs.getBsFrom());
					tmp.setBrId(rs.getBsId());
					tmp.setBrImage(rs.getBsImage());
					tmp.setBrNum(rs.getBsNum());
					tmp.setBrPcode(rs.getBsPcode());
					tmp.setBrPlace(rs.getBsPlace());
					tmp.setBrRdateStr(UtilDateTime.convertToDateTH(rs.getBsRdate()));
					tmp.setBrRemark(rs.getBsRemark());
					tmp.setBrStatus(rs.getBsStatus());
					tmp.setBrSubject(rs.getBsSubject());
					tmp.setBrTo(rs.getBsTo());
					tmp.setBrTypeQuick(rs.getBsTypeQuick());
					tmp.setBrTypeSecret(rs.getBsTypeSecret());
					tmp.setBrYear(rs.getBsYear());
					arrTmp.add(tmp);
				}
			}
			
		}catch(Exception ex){
			logger.error("doSalesReportExcel : ", ex);
			ex.printStackTrace();
		}
		
		Map<String, List<PdfForm>> revenueData = new HashMap<String, List<PdfForm>>();
		revenueData.put(type, arrTmp);
		
		return new ModelAndView("ExcelRevenueSummary","revenueData",revenueData);
			
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/BOOK_SEND_OUT", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody ModelAndView doSalesReportExcelSend(ModelAndView modelAndView, 
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type") String type) {
		
		
		List<PdfForm> arrTmp = new ArrayList<PdfForm>();
		try {
			
			if(type.equals("IN")){
				List<BookReciveOut> listResult = (List<BookReciveOut>) request.getSession().getAttribute("listResultRecive");
				for (int i = 0; i < listResult.size(); i++) {
					BookReciveOut rs = listResult.get(i);
					PdfForm tmp = new PdfForm();
					tmp.setBrDateStr(UtilDateTime.convertToDateTH(rs.getBrDate()));
					tmp.setBrDivision(rs.getBrDivision());
					tmp.setBrFrom(rs.getBrFrom());
					tmp.setBrId(rs.getBrId());
					tmp.setBrImage(rs.getBrImage());
					tmp.setBrNum(rs.getBrNum());
					tmp.setBrPcode(rs.getBrPcode());
					tmp.setBrPlace(rs.getBrPlace());
					tmp.setBrRdateStr(UtilDateTime.convertToDateTH(rs.getBrRdate()));
					tmp.setBrRemark(rs.getBrRemark());
					tmp.setBrStatus(rs.getBrStatus());
					tmp.setBrSubject(rs.getBrSubject());
					tmp.setBrTo(rs.getBrTo());
					tmp.setBrTypeQuick(rs.getBrTypeQuick());
					tmp.setBrTypeSecret(rs.getBrTypeSecret());
					tmp.setBrYear(rs.getBrYear());
					arrTmp.add(tmp);
				}
			}else{
				List<BookSendOut> listResult = (List<BookSendOut>) request.getSession().getAttribute("listResultSend");
				for (int i = 0; i < listResult.size(); i++) {
					BookSendOut rs = listResult.get(i);
					PdfForm tmp = new PdfForm();
					tmp.setBrDateStr(UtilDateTime.convertToDateTH(rs.getBsDate()));
					tmp.setBrDivision(rs.getBsDivision());
					tmp.setBrFrom(rs.getBsFrom());
					tmp.setBrId(rs.getBsId());
					tmp.setBrImage(rs.getBsImage());
					tmp.setBrNum(rs.getBsNum());
					tmp.setBrPcode(rs.getBsPcode());
					tmp.setBrPlace(rs.getBsPlace());
					tmp.setBrRdateStr(UtilDateTime.convertToDateTH(rs.getBsRdate()));
					tmp.setBrRemark(rs.getBsRemark());
					tmp.setBrStatus(rs.getBsStatus());
					tmp.setBrSubject(rs.getBsSubject());
					tmp.setBrTo(rs.getBsTo());
					tmp.setBrTypeQuick(rs.getBsTypeQuick());
					tmp.setBrTypeSecret(rs.getBsTypeSecret());
					tmp.setBrYear(rs.getBsYear());
					arrTmp.add(tmp);
				}
			}
			
		}catch(Exception ex){
			logger.error("doSalesReportExcel : ", ex);
			ex.printStackTrace();
		}
		
		Map<String, List<PdfForm>> revenueData = new HashMap<String, List<PdfForm>>();
		revenueData.put(type, arrTmp);
		
		return new ModelAndView("ExcelRevenueSummary","revenueData",revenueData);
			
	}
	
	@RequestMapping(value = "/downloadFiles", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public String downloadFiles(@RequestParam(value = "id", required = true) int attachmentId, HttpServletResponse response) 
	{
        try {
        	Attachment doc = attachmentService.getAttachment(attachmentId);
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + doc.getOriginalFileName() + "\"");
        	response.setContentType(doc.getContentType() + ";charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentLength(doc.getContent().length);
            
            ServletOutputStream ouputStream = response.getOutputStream();
    		ouputStream.write( doc.getContent(), 0, doc.getContent().length);
    		ouputStream.flush();
    		ouputStream.close();
            
        } catch(Exception ex){
			logger.error("downloadFiles : ", ex);
		}

        return null;
	}
	
	@RequestMapping(value = "/delete")
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String delete(@RequestParam(value = "id", required = true) int Id,
			@RequestParam(value = "type", required = true) String type,
			HttpServletResponse response) {
		String rs = "0";
		try {
			if(type.equals("0")){
				rs = reciveout.delete(Id);
			}else{ 
				rs = sendout.delete(Id);
			}
		}catch(Exception ex){
			logger.error("delete : ", ex);
		}
		return rs;
	}
	
	
	@RequestMapping(value = "/deleteFiles")
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody boolean deleteFiles(@RequestParam(value = "id", required = true) int attachmentId, HttpServletResponse response) 
	{
		return attachmentService.delete(attachmentId);
	}
	
	@RequestMapping(value = "/insertBs")
	public String insert(HttpServletResponse response) 
	{
        try {
            FileInputStream file = new FileInputStream(new File("D:\\BOOK_SEND_OUT.xlsx"));
            
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            int i = 0;
            while (rowIterator.hasNext()) 
            {
            	Row row = rowIterator.next();
            	if(i == 0){
            		i = 1;
            		continue;
            	}
                //For each row, iterate through all the columns
                BookSendOut bs = new BookSendOut();
                if(row.getCell(0) != null){
	                int a = (int)row.getCell(0).getNumericCellValue();
	                bs.setBsId(a);
                }
                bs.setBsNum(row.getCell(1) == null ?  null : Integer.parseInt(row.getCell(1).getStringCellValue()));
                //bs.setBsNum(row.getCell(1) == null ?  null : (int)row.getCell(1).getNumericCellValue());
                bs.setBsTypeQuick(1);
                bs.setBsTypeSecret(1);
                if(row.getCell(4) != null){
	                int b = (int)row.getCell(4).getNumericCellValue();
	                bs.setBsYear(b);
                }
                String[] arr = new String[3];
                if(row.getCell(5) != null){
	                arr = row.getCell(5).getStringCellValue().split("/");
	                if(arr.length > 2){
	                	String day = (arr[0].length() > 1) ? arr[0] : "0" + arr[0];
	                	String month = (arr[1].length() > 1) ? arr[1] : "0" + arr[1];
	                	int year = Integer.parseInt(arr[2]) - 543;
	                	String startDateString = day + "/" + month + "/" + year;
		                DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		                Date startDate;
		                try {
		                    startDate = df.parse(startDateString);
		                    bs.setBsRdate(startDate);
		                } catch (ParseException e) {
		                    e.printStackTrace();
		                }
	                }
                }
                bs.setBsPlace(row.getCell(6) == null ?  "" : row.getCell(6).getStringCellValue());
                if(row.getCell(7) != null){
	                arr = row.getCell(7).getStringCellValue().split("/");
	                if(arr.length > 2){
	                	String day = (arr[0].length() > 1) ? arr[0] : "0" + arr[0];
	                	String month = (arr[1].length() > 1) ? arr[1] : "0" + arr[1];
	                	int year = Integer.parseInt(arr[2]) - 543;
	                	String startDateString = day + "/" + month + "/" + year;
		                DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		                Date startDate;
		                try {
		                    startDate = df.parse(startDateString);
		                    bs.setBsDate(startDate);
		                } catch (ParseException e) {
		                    e.printStackTrace();
		                }
	                }
                }
                bs.setBsFrom(row.getCell(8) == null ?  "" : row.getCell(8).getStringCellValue());
                bs.setBsTo(row.getCell(9) == null ?  "" : row.getCell(9).getStringCellValue());
                bs.setBsSubject(row.getCell(10) == null ?  "" : row.getCell(10).getStringCellValue());
                bs.setBsRemark(row.getCell(11) == null ?  "" : row.getCell(11).getStringCellValue());
                bs.setBsDivision(row.getCell(12) == null ?  "" : row.getCell(12).getStringCellValue());
                bs.setBsPcode(row.getCell(13) == null ?  "" : row.getCell(13).getStringCellValue());
                bs.setBsStatus(row.getCell(14) == null ?  "" : row.getCell(14).getStringCellValue());
                bs.setBsImage(row.getCell(15) == null ?  "" : row.getCell(15).getStringCellValue());
                logger.info(bs.toString());
                this.sendout.saveBookOut(bs);
            }
            file.close();        	
        } catch(Exception ex){
			logger.error("insert : ", ex);
		}

        return "home";
	}
	
	@RequestMapping(value = "/insertBr")
	public String insertBr(HttpServletResponse response) 
	{
		int i = 0;
        try {
            FileInputStream file = new FileInputStream(new File("D:\\BOOK_RECIVE_OUT.xlsx"));
            
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
 
            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
 
            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            
            while (rowIterator.hasNext()) 
            {
            	Row row = rowIterator.next();
            	
            	if(i == 0){
            		i = 1;
            		continue;
            	}
            	 if(row.getCell(0).getNumericCellValue() == 47013){
                   	System.out.println("xxx");
                   }
                //For each row, iterate through all the columns
            	BookReciveOut bs = new BookReciveOut();
                if(row.getCell(0) != null){
	                int a = (int)row.getCell(0).getNumericCellValue();
	                bs.setBrId(a);
                }
               
                bs.setBrNum(row.getCell(1) == null ?  null : Integer.parseInt(row.getCell(1).getStringCellValue()));
                //bs.setBrNum(row.getCell(1) == null ?  null : (int)row.getCell(1).getNumericCellValue());
                if(row.getCell(2) != null){
	                int b = (int)row.getCell(2).getNumericCellValue();
	                bs.setBrYear(b);
                }
                String[] arr = new String[3];
                if(row.getCell(3) != null){
	                arr = row.getCell(3).getStringCellValue().split("/");
	                if(arr.length > 2){
	                	String day = (arr[0].length() > 1) ? arr[0] : "0" + arr[0];
	                	String month = (arr[1].length() > 1) ? arr[1] : "0" + arr[1];
	                	int year = Integer.parseInt(arr[2]) - 543;
	                	String startDateString = day + "/" + month + "/" + year;
		                DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		                Date startDate;
		                try {
		                    startDate = df.parse(startDateString);
		                    bs.setBrRdate(startDate);
		                } catch (ParseException e) {
		                    e.printStackTrace();
		                }
	                }
                }
                bs.setBrTypeQuick(1);
                bs.setBrTypeSecret(1);
                bs.setBrPlace(row.getCell(7) == null ?  "" : row.getCell(7).getStringCellValue());
                if(row.getCell(8) != null){
	                arr = row.getCell(8).getStringCellValue().split("/");
	                if(arr.length > 2){
	                	String day = (arr[0].length() > 1) ? arr[0] : "0" + arr[0];
	                	String month = (arr[1].length() > 1) ? arr[1] : "0" + arr[1];
	                	int year = Integer.parseInt(arr[2]) - 543;
	                	String startDateString = day + "/" + month + "/" + year;
		                DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		                Date startDate;
		                try {
		                    startDate = df.parse(startDateString);
		                    bs.setBrDate(startDate);
		                } catch (ParseException e) {
		                    e.printStackTrace();
		                }
	                }
                }
                bs.setBrFrom(row.getCell(9) == null ?  "" : row.getCell(9).getStringCellValue());
                bs.setBrTo(row.getCell(10) == null ?  "" : row.getCell(10).getStringCellValue());
                bs.setBrSubject(row.getCell(11) == null ?  "" : row.getCell(11).getStringCellValue());
                bs.setBrRemark(row.getCell(12) == null ?  "" : row.getCell(12).getStringCellValue());
                bs.setBrDivision(row.getCell(13) == null ?  "" : row.getCell(13).getStringCellValue());
                bs.setBrPcode(row.getCell(14) == null ?  "" : row.getCell(14).getStringCellValue());
                bs.setBrStatus(row.getCell(15) == null ?  "" : row.getCell(15).getStringCellValue());
                bs.setBrImage(row.getCell(16) == null ?  "" : row.getCell(16).getStringCellValue());
                logger.info(bs.toString());
                this.reciveout.SaveReciveOut(bs);
            }
            file.close();        	
        } catch (Exception ex) {
        	logger.error("insertBr : " + i, ex);
        	ex.printStackTrace();
        }

        return "home";
	}
}
