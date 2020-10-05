package com.java.doc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.BookReciveOutDAO;
import com.java.doc.model.BookRecieveDepartment;
import com.java.doc.model.BookRecieveGroup;
import com.java.doc.model.BookRecieveSection;
import com.java.doc.model.BookRecieveUser;
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookReciveOutTable;
import com.java.doc.model.Divisions;
import com.java.doc.model.Groups;
import com.java.doc.model.Sections;
import com.java.doc.model.StatusDetail;
import com.java.doc.model.Users;
import com.java.doc.util.Constants;
import com.java.doc.util.TableSorter;
import com.java.doc.util.UtilDateTime;
import com.java.doc.util.Utility;
import com.mysql.jdbc.StringUtils;

@Service("bookReciveOutService")
public class BookReciveOutServiceImpl implements BookReciveOutService {

	private static final Logger logger = Logger.getLogger(BookReciveOutServiceImpl.class);
	
	@Autowired
	@Qualifier("bookReciveOutDao")
	private BookReciveOutDAO reciveout;
	
	@Autowired
	@Qualifier(value = "divisionService")
	private DivisionService divisions;
	
	@Autowired
	@Qualifier(value = "userService")
	private UserService userService;

	@Override
	public long Count() {
		return reciveout.Count();
	}

	@Override
	public BookReciveOutTable ListPageRecive(TableSorter table) {
		return reciveout.ListPageRecive(table);
	}

	@Override
	public int CountSelect() {
		return reciveout.CountSelect();
	}

	@Override
	@Transactional
	public int SaveReciveOut(BookReciveOut recive) throws Exception {
		int brId = 0;
		try{
			String[] arrDepartment = (StringUtils.isNullOrEmpty(recive.getBrToDepartment()) ? null : recive.getBrToDepartment().split(","));
			String[] arrGroup = (StringUtils.isNullOrEmpty(recive.getBrToGroup()) ? null : recive.getBrToGroup().split(","));
			String[] arrSection = (StringUtils.isNullOrEmpty(recive.getBrToSection()) ? null : recive.getBrToSection().split(","));
			String[] arrUser = (StringUtils.isNullOrEmpty(recive.getBrToUser()) ? null : recive.getBrToUser().split(","));
			brId = reciveout.Save(recive);
			if(brId > 0){
				if(arrDepartment != null){
					List<String> chkFindDepartmentFromGroup = new ArrayList<String>();
					if(!StringUtils.isNullOrEmpty(recive.getBrToGroup())){
						String[] chkj = recive.getBrToGroup().split(",");
						for(String g : chkj){
							String[] n = g.split("xx##xx");
							chkFindDepartmentFromGroup.add(n[0]);
						}
					}
					for(String item : arrDepartment){
						Divisions division = divisions.getDivisionByCode(item);
						BookRecieveDepartment bookRecieveTo = new BookRecieveDepartment();
						Integer runningNo = reciveout.getNextRunningNoDepartment(UtilDateTime.getCurrentYear(),item);
						bookRecieveTo.setRunningNo(runningNo);
						bookRecieveTo.setBrId(recive.getBrId());
						bookRecieveTo.setBrToDepartment(item);
						bookRecieveTo.setBrToDepartmentName(division.getDivisionName());
						bookRecieveTo.setBrToDepartmentShort(division.getDivisionShort());
						bookRecieveTo.setStatus((chkFindDepartmentFromGroup.indexOf(item) == -1) ? "N" : "Y");
						bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
						bookRecieveTo.setRemark(recive.getBrRemarkDepartment());
						reciveout.insertRecieveDepartment(bookRecieveTo);
					}
				}
				if(arrGroup != null){
					List<String> chkFindGroupFromUser = new ArrayList<String>();
					if(!StringUtils.isNullOrEmpty(recive.getBrToUser())){
						String[] d = recive.getBrToUser().split(",");
						for(String e : d){
							String[] w = e.split("xx##xx");
							chkFindGroupFromUser.add(w[1]);
						}
					}
					for(String item : arrGroup){
						item = item.trim();
						String[] arr = item.split("xx##xx");
						Groups group = userService.getGroupName(arr[1]);
						BookRecieveGroup bookRecieveTo = new BookRecieveGroup();
						bookRecieveTo.setBrId(recive.getBrId());
						bookRecieveTo.setBrToDepartment(arr[0]);
						bookRecieveTo.setBrToGroup(arr[1]);
						bookRecieveTo.setBrToGroupName(group.getGroupName());
						bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
						bookRecieveTo.setStatus((chkFindGroupFromUser.indexOf(arr[1]) == -1) ? "N" : "Y");
						bookRecieveTo.setRemark(recive.getBrRemarkGroup());
						reciveout.insertRecieveGroup(bookRecieveTo);
					}
				}
				if(arrSection != null){
					for(String item : arrSection){
						item = item.trim();
						String[] arr = item.split("xx##xx");
						Sections section = userService.getSectionName(arr[1]);
						BookRecieveSection bookRecieveTo = new BookRecieveSection();
						bookRecieveTo.setBrId(recive.getBrId());
						bookRecieveTo.setBrToGroup(arr[0]);
						bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
						bookRecieveTo.setBrToDepartment(recive.getBrToDepartment());
						bookRecieveTo.setBrToSection(arr[1]);
						bookRecieveTo.setBrToSectionName(section.getSectionName());
						bookRecieveTo.setStatus("N");
						bookRecieveTo.setRemark(recive.getBrRemarkSection());
						reciveout.insertRecieveSection(bookRecieveTo);
					}
				}
				if(arrUser != null){
					for(String item : arrUser){
						item = item.trim();
						String[] arr = item.split("xx##xx");
						Users user = userService.getUserById(Integer.parseInt(arr[2]));
						BookRecieveUser bookRecieveTo = new BookRecieveUser();
						bookRecieveTo.setBrId(recive.getBrId());
						bookRecieveTo.setBrToGroup(arr[1]);
						bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
						bookRecieveTo.setBrToDepartment(arr[0]);
						bookRecieveTo.setBrToUser(arr[2]);
						//bookRecieveT
						bookRecieveTo.setStatus("N");
						bookRecieveTo.setBrToUserName(user.getPrefix() + user.getFname() + " " + user.getLname());
						bookRecieveTo.setRemark(recive.getBrRemarkUser());
						reciveout.insertRecieveUser(bookRecieveTo);
					}
				}
				//insertBrToExcel(recive);
			}	
		}catch(Exception ex){
			throw ex;
		}
		return brId;
	}

	@Override
	public BookReciveOut getDataFromId(int brId, Users user) {
		return reciveout.getDataFromId(brId, user);
	}

	@Override
	public int LastID() {
		return reciveout.LastID();
	}

	@Override
	public List<Integer> getYear() {
		return reciveout.getYear();
	}

	@Override
	public Integer getNextBrNum(int brYear) {
		return reciveout.getNextBrNum(brYear);
	}

	@Override
	public String delete(BookReciveOut br) throws Exception {
		/*boolean chk = false;
		try {
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelRecievePath").split("\\.");
			String fileName = cons.getProperty("excelRecievePath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + br.getBrYear() + "." + arrStr[1];
			}
	    	File f = new File(fileName);
	    	XSSFWorkbook workbook;
	    	FileInputStream file = null;
	    	XSSFSheet sheet = null;
	    	int lastSheet = 0;
	    	if(f.exists() && !f.isDirectory()){
	    		file = new FileInputStream(new File(fileName));
	            workbook = new XSSFWorkbook(file);
	            lastSheet = workbook.getNumberOfSheets();
	            for(int i = 0; i < lastSheet; i++){
	            	sheet = workbook.getSheetAt(i);
	            	int rowNumber = Utility.getRowNumberForDelete(sheet, br.getBrNum(), 2);
	            	if(rowNumber > 0){
	            		sheet.removeRow(sheet.getRow(rowNumber)); 
	            		chk = true;
	            		break;
	            	}
	            }
	            file.close();
            	FileOutputStream out = new FileOutputStream(new File(fileName));
    			workbook.write(out);
    			out.close();
	    	}
		}catch(Exception ex) {
			logger.error("delete BrToExcel : ", ex);
			throw ex;
		}
		return (chk) ? reciveout.delete(br.getBrId()) : "0";*/
		return reciveout.delete(br.getBrId());
	}
	
	@Override
	public boolean insertBrToExcel(BookReciveOut recive) {
		System.out.println("insertBrToExcel : " + recive.toString());
		logger.info("insertBrToExcel : " + recive.toString());
		try {
			boolean chkExistFile = false;
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelRecievePath").split("\\.");
			String fileName = cons.getProperty("excelRecievePath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + recive.getBrYear() + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	if(f.exists() && !f.isDirectory()) { 
        		chkExistFile = true;
        	}
        	XSSFWorkbook workbook;
        	FileInputStream file = null;
        	XSSFSheet sheet = null;
        	int lastSheet = 0;
        	int lastRow = 0;
        	if(chkExistFile){
        		file = new FileInputStream(new File(fileName));
                workbook = new XSSFWorkbook(file);
                lastSheet = workbook.getNumberOfSheets();
                sheet = workbook.getSheetAt(lastSheet - 1);
                lastRow = sheet.getLastRowNum();
        	}else{
        		workbook = new XSSFWorkbook();
        	}
                      
            int limitOfExcel = Integer.parseInt(cons.getProperty("limitOfExcel"));
            Row row;
            Cell cell;
            
            if(lastRow >= limitOfExcel || lastRow == 0){
            	sheet = workbook.createSheet("Sheet-" + Integer.toString(lastRow + 1));
            	row = sheet.createRow(0);				

            	cell = row.createCell(0);
            	cell.setCellValue("ปี");
            	
            	cell = row.createCell(1);
            	cell.setCellValue("วันที่รับหนังสือ");
            	
            	cell = row.createCell(2);
            	cell.setCellValue("เลขทะเบียนรับ");
            	
            	cell = row.createCell(3);
            	cell.setCellValue("ที่");
            	
            	cell = row.createCell(4);
            	cell.setCellValue("ลงวันที่");
            	
            	cell = row.createCell(5);
            	cell.setCellValue("จาก");
            	
            	cell = row.createCell(6);
            	cell.setCellValue("ถึงกอง");
            	
            	cell = row.createCell(7);
            	cell.setCellValue("เรื่อง");
            	
            	cell = row.createCell(8);
            	cell.setCellValue("ขั้นความเร็ว");
            	
            	cell = row.createCell(9);
            	cell.setCellValue("ขั้นความลับ");
            	
            	cell = row.createCell(10);
            	cell.setCellValue("หมายเหตุ");
            		
            }
            
            row = sheet.createRow(lastRow + 1);
            
        	cell = row.createCell(0);
        	cell.setCellValue((int) recive.getBrYear());
        	
        	cell = row.createCell(1);
        	cell.setCellValue(UtilDateTime.convertToDateTH(recive.getBrRdate()));
        	
        	cell = row.createCell(2);
        	cell.setCellValue((int) recive.getBrNum());
        	
        	cell = row.createCell(3);
        	cell.setCellValue((String) recive.getBrPlace());
        	
        	cell = row.createCell(4);
        	cell.setCellValue(UtilDateTime.convertToDateTH(recive.getBrDate()));
        	
        	cell = row.createCell(5);
        	cell.setCellValue((String) recive.getBrFrom());
        	
        	cell = row.createCell(6);
        	cell.setCellValue((String) recive.getBrToDepartment());
        	
        	cell = row.createCell(7);
        	cell.setCellValue((String) recive.getBrSubject());
        	
        	cell = row.createCell(8);
        	cell.setCellValue((int) recive.getBrTypeQuick());
        	
        	cell = row.createCell(9);
        	cell.setCellValue((int) recive.getBrTypeSecret());
        	
        	cell = row.createCell(10);
        	cell.setCellValue((String) recive.getBrRemark());
        	
        	if(chkExistFile){
	        	file.close();
        	}
        	FileOutputStream out = new FileOutputStream(new File(fileName));
			workbook.write(out);
			out.close();
        }catch(Exception ex){
        	logger.error("insertBrToExcel : ", ex);
        	System.out.println("insertBrToExcel : " + ex.getMessage());
        }
            
        return true;
	}

	public boolean updateBrToExcel(BookReciveOut recive) {
		try {
			System.out.println("updateBrToExcel : " + recive.toString());
			logger.info("updateBrToExcel : " + recive.toString());
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelRecievePath").split("\\.");
			String fileName = cons.getProperty("excelRecievePath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + recive.getBrYear() + "." + arrStr[1];
			}
        	File f = new File(fileName);
        	
        	XSSFWorkbook workbook;
        	FileInputStream file = null;
        	XSSFSheet sheet = null;
        	int lastSheet = 0;
        	if(f.exists() && !f.isDirectory()){
        		file = new FileInputStream(new File(fileName));
                workbook = new XSSFWorkbook(file);
                lastSheet = workbook.getNumberOfSheets();
                for(int i = 0; i < lastSheet; i++){
                	sheet = workbook.getSheetAt(i);
                	Iterator<Row> iterator = sheet.iterator();
                	Row nextRow = null;
                	Cell cell;
                	int j = 0; 
                	while (iterator.hasNext()) {
                		nextRow = iterator.next();
                		if(j == 0){
                			j = 1;
                			continue;
                		}
                        Cell celld = nextRow.getCell(2);
                        int brNum = 0;
                        switch (celld.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                            	brNum = Integer.parseInt(celld.getStringCellValue());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                            	brNum = (int) celld.getNumericCellValue();
                                break;
                        }
                        if(brNum == recive.getBrNum()){
                        	break;
                        }
                	}
                	if(nextRow != null){
                		cell = nextRow.getCell(0);
                    	cell.setCellValue((int) recive.getBrYear());
                    	
                    	cell = nextRow.getCell(1);
                    	cell.setCellValue(UtilDateTime.convertToDateTH(recive.getBrRdate()));
                    	
                    	cell = nextRow.getCell(3);
                    	cell.setCellValue((String) recive.getBrPlace());
                    	
                    	cell = nextRow.getCell(4);
                    	cell.setCellValue(UtilDateTime.convertToDateTH(recive.getBrDate()));
                    	
                    	cell = nextRow.getCell(5);
                    	cell.setCellValue((String) recive.getBrFrom());
                    	
                    	cell = nextRow.getCell(6);
                    	cell.setCellValue((String) recive.getBrToDepartment());
                    	
                    	cell = nextRow.getCell(7);
                    	cell.setCellValue((String) recive.getBrSubject());
                    	
                    	cell = nextRow.getCell(8);
                    	cell.setCellValue((int) recive.getBrTypeQuick());
                    	
                    	cell = nextRow.getCell(9);
                    	cell.setCellValue((int) recive.getBrTypeSecret());
                    	
                    	cell = nextRow.getCell(10);
                    	cell.setCellValue((String) recive.getBrRemark());
                        
                    	file.close();
                    	FileOutputStream out = new FileOutputStream(new File(fileName));
            			workbook.write(out);
            			out.close();
                    	break;
                	}
                }
        	}
        }catch(Exception ex){
        	logger.error("updateBrToExcel : ", ex);
        	System.out.println("updateBrToExcel : " + ex.getMessage());
        }
            
        return true;
	}

	@Override
	public int updateReciveOut(BookReciveOut recive, String role) {
		int rs = 0;
		try{
			System.out.println("updateReciveOut : " + recive.toString() + ", role : " + role);
			logger.info("updateReciveOut : " + recive.toString() + ", role : " + role);
			if(role.equals("ADMIN")){
				String brTo = recive.getBrToDepartment();
				String brToNotIn = "\'\'";
				String[] arr = null;
				if(!StringUtils.isNullOrEmpty(brTo)){
					arr = (brTo.indexOf(",") > -1) ? brTo.split(",") : new String[]{brTo};
					if(arr.length > 0){
						for(String item : arr){
							brToNotIn += "'" + item + "',";
						}
						brToNotIn = brToNotIn.substring(0, brToNotIn.length() - 1);
					}
				}
				rs = reciveout.updateReciveOutAdmin(recive);
				reciveout.deleteRecieveDepartment(recive, brToNotIn);
				List<BookRecieveDepartment> list = reciveout.getBookRecieveOutDepartment(recive.getBrId());
				List<String> tmpDepartments = new ArrayList<String>();
				for(BookRecieveDepartment item : list){
					tmpDepartments.add(item.getBrToDepartment());
				}
				List<String> chkFindDepartmentFromGroup = new ArrayList<String>();
				if(!StringUtils.isNullOrEmpty(recive.getBrToGroup())){
					String[] chkj = (recive.getBrToGroup().indexOf(",") > -1) ? recive.getBrToGroup().split(",") : new String[]{recive.getBrToGroup()};
					if(chkj.length > 0){
						for(String g : chkj){
							String[] n = g.split("xx##xx");
							chkFindDepartmentFromGroup.add(n[0]);
						}
					}
				}
				if(arr != null){
					for(String item : arr){
						item = item.trim();
						if(tmpDepartments.indexOf(item) == -1){
							Divisions division = divisions.getDivisionByCode(item);
							BookRecieveDepartment bookRecieveTo = new BookRecieveDepartment();
							bookRecieveTo.setStatus((chkFindDepartmentFromGroup.indexOf(item) == -1) ? "N" : "Y");
							bookRecieveTo.setBrId(recive.getBrId());
							bookRecieveTo.setBrToDepartment(item);
							bookRecieveTo.setBrToDepartmentName(division.getDivisionName());
							bookRecieveTo.setBrToDepartmentShort(division.getDivisionShort());
							bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
							bookRecieveTo.setRemark(recive.getBrRemarkDepartment());
							reciveout.insertRecieveDepartment(bookRecieveTo);
						}
					}
				}
				arr = null;
				brTo = recive.getBrToGroup();
				if(!StringUtils.isNullOrEmpty(brTo)){
					arr = (brTo.indexOf(",") > -1) ? brTo.split(",") : new String[]{brTo};
					String chk = "";
					if(arr.length > 0){
						Map<String, String> mapGroups = new HashMap<String, String>();
						String listGroups = "";
						for(String s : arr){
							String[] arr2 = s.split("xx##xx");
							chk = (StringUtils.isNullOrEmpty(chk)) ? arr2[0] : chk;
							if(!chk.equals(arr2[0])){
								listGroups = listGroups.substring(0, listGroups.length() - 1);
								mapGroups.put(chk, listGroups);
								chk = arr2[0];
								listGroups = "";
							}
							listGroups += "'" + arr2[1] + "',";
						}
						listGroups = listGroups.substring(0, listGroups.length() - 1);
						mapGroups.put(chk, listGroups);
						List<String> chkFindGroupFromUser = new ArrayList<String>();
						if(!StringUtils.isNullOrEmpty(recive.getBrToUser())){
							String[] d = (recive.getBrToUser().indexOf(",") > -1) ? recive.getBrToUser().split(",") : new String[]{recive.getBrToUser()};
							if(d.length > 0){
								for(String e : d){
									String[] w = e.split("xx##xx");
									chkFindGroupFromUser.add(w[1]);
								}
							}
						}
						for(Map.Entry<String, String> item : mapGroups.entrySet()){
							BookRecieveDepartment b = new BookRecieveDepartment();
							b.setBrId(recive.getBrId());
							b.setBrToDepartment(item.getKey());
							reciveout.deleteRecieveGroup(b, item.getValue());
							List<BookRecieveGroup> lista = reciveout.getBookRecieveOutGroup(b.getBrId(), b.getBrToDepartment());
							List<String> tmpGroups = new ArrayList<String>();
							for(BookRecieveGroup item2 : lista){
								tmpGroups.add(item2.getBrToGroup());
							}
							String[] h = (item.getValue().indexOf(",") > -1) ? item.getValue().split(",") : new String[]{item.getValue()};
							if(h != null){
								for(String item3 : h){
									item3 = item3.trim().replace("'", "");
									if(tmpGroups.indexOf(item3) == -1){
										Groups group = userService.getGroupName(item3);
										BookRecieveGroup bookRecieveTo = new BookRecieveGroup();
										bookRecieveTo.setBrId(recive.getBrId());
										bookRecieveTo.setBrToDepartment(item.getKey());
										bookRecieveTo.setBrToGroup(item3);
										bookRecieveTo.setBrToGroupName(group.getGroupName());
										bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
										bookRecieveTo.setStatus((chkFindGroupFromUser.indexOf(item3) == -1) ? "N" : "Y");
										bookRecieveTo.setRemark(recive.getBrRemarkGroup());
										reciveout.insertRecieveGroup(bookRecieveTo);
									}
								}
							}
						}
					}
				}else{
					for(BookRecieveDepartment item : list){
						reciveout.deleteRecieveGroup(item, "\'\'");
					}
				}
				
				arr = null;
				brTo = recive.getBrToSection();
				reciveout.deleteRecieveSectionByBrId(recive.getBrId()); 
				if(!StringUtils.isNullOrEmpty(brTo)){
					arr = (brTo.indexOf(",") > -1) ? brTo.split(",") : new String[]{brTo};
					if(arr != null){
						for(String item : arr){
							item = item.trim();
							String[] h = item.split("xx##xx");
							Sections section = reciveout.getSectionById(Integer.parseInt(h[1]));
							BookRecieveSection bookRecieveTo = new BookRecieveSection();
							bookRecieveTo.setBrId(recive.getBrId());
							bookRecieveTo.setBrToGroup(h[0]);
							bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
							bookRecieveTo.setBrToDepartment(recive.getBrToDepartment());
							bookRecieveTo.setBrToSection(h[1]);
							bookRecieveTo.setBrToSectionName(section.getSectionName());
							bookRecieveTo.setStatus("N");
							bookRecieveTo.setRemark(recive.getBrRemarkSection());
							reciveout.insertRecieveSection(bookRecieveTo);
						}
					}	
				}
				
				arr = null;
				brTo = recive.getBrToUser();
				reciveout.deleteRecieveUserByBrId(recive.getBrId()); 
				if(!StringUtils.isNullOrEmpty(brTo)){
					arr = (brTo.indexOf(",") > -1) ? brTo.split(",") : new String[]{brTo};
					if(arr != null){
						for(String item : arr){
							item = item.trim();
							String[] h = item.split("xx##xx");
							Users user = userService.getUserById(Integer.parseInt(h[2]));
							BookRecieveUser bookRecieveTo = new BookRecieveUser();
							bookRecieveTo.setBrId(recive.getBrId());
							bookRecieveTo.setBrToGroup(h[1]);
							bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
							bookRecieveTo.setBrToDepartment(h[0]);
							bookRecieveTo.setBrToUser(h[2]);
							bookRecieveTo.setStatus("N");
							bookRecieveTo.setBrToUserName(user.getPrefix() + user.getFname() + " " + user.getLname());
							bookRecieveTo.setRemark(recive.getBrRemarkUser());
							reciveout.insertRecieveUser(bookRecieveTo);
						}
					}	
				}
				//updateBrToExcel(recive);
			}else if(role.equals("DEPARTMENT")){
				String brTo = recive.getBrToGroup();
				BookRecieveDepartment bookRecieveDepartment = new BookRecieveDepartment();
				bookRecieveDepartment.setStatus("Y");
				bookRecieveDepartment.setBrId(recive.getBrId());
				bookRecieveDepartment.setBrToDepartment(recive.getBrToDepartment());
				bookRecieveDepartment.setUpdatedBy(recive.getUpdatedBy());
				bookRecieveDepartment.setRemark(recive.getBrRemarkDepartment());
				rs = reciveout.updateReciveOutDepartment(bookRecieveDepartment);
				String brToNotIn = "\'\'";
				String[] arr = null;
				if(!StringUtils.isNullOrEmpty(brTo)){
					arr = (brTo.indexOf(",") > -1) ? brTo.split(",") : new String[]{brTo};
					if(arr.length > 0){
						for(String item : arr){
							brToNotIn += "'" + item + "',";
						}
						brToNotIn = brToNotIn.substring(0, brToNotIn.length() - 1);
					}
				}
				reciveout.deleteRecieveGroup(bookRecieveDepartment, brToNotIn);
				List<BookRecieveGroup> list = reciveout.getBookRecieveOutGroup(bookRecieveDepartment.getBrId(), bookRecieveDepartment.getBrToDepartment());
				List<String> tmpGroups = new ArrayList<String>();
				for(BookRecieveGroup item : list){
					tmpGroups.add(item.getBrToGroup());
				}
				if(arr != null){
					for(String item : arr){
						item = item.trim();
						if(tmpGroups.indexOf(item) == -1){
							Groups group = userService.getGroupName(item);
							BookRecieveGroup bookRecieveTo = new BookRecieveGroup();
							bookRecieveTo.setBrId(recive.getBrId());
							bookRecieveTo.setBrToDepartment(recive.getBrToDepartment());
							bookRecieveTo.setBrToGroup(item);
							bookRecieveTo.setBrToGroupName(group.getGroupName());
							bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
							bookRecieveTo.setStatus("N");
							bookRecieveTo.setRemark(recive.getBrRemarkGroup());
							reciveout.insertRecieveGroup(bookRecieveTo);
						}
					}
				}
			}else if(role.equals("GROUP")){
				String brTo = recive.getBrToUser();
				BookRecieveGroup bookRecieveGroup = new BookRecieveGroup();
				bookRecieveGroup.setStatus("Y");
				bookRecieveGroup.setBrId(recive.getBrId());
				bookRecieveGroup.setBrToGroup(recive.getBrToGroup());
				bookRecieveGroup.setUpdatedBy(recive.getUpdatedBy());
				bookRecieveGroup.setBrToDepartment(recive.getBrToDepartment());
				bookRecieveGroup.setRemark(recive.getBrRemarkGroup());
				rs = reciveout.updateReciveOutGroup(bookRecieveGroup);
				String brToNotIn = "\'\'";
				Map<Integer, String> arr = new HashMap<Integer, String>();
				if(!StringUtils.isNullOrEmpty(brTo)){
					String[] arrTmp = (brTo.indexOf(",") > -1) ? brTo.split(",") : new String[]{brTo};
					if(arrTmp.length > 0){
						for(int i = 0; i < arrTmp.length; i++) {
							String[] h = arrTmp[i].split("xx##xx");
							if(h.length > 3) {
								arr.put(Integer.parseInt(h[2]), h[3]);
							}
						}
					}
					if(arr.size() > 0){
						Iterator iterator = arr.entrySet().iterator();
				        while (iterator.hasNext()) {
				        	Map.Entry me2 = (Map.Entry) iterator.next();
							brToNotIn += "'" + me2.getKey() + "',";
						}
						brToNotIn = brToNotIn.substring(0, brToNotIn.length() - 1);
					}
				}
				reciveout.deleteRecieveUser(bookRecieveGroup, brToNotIn);
				List<BookRecieveUser> list = reciveout.getBookRecieveOutUser(bookRecieveGroup.getBrId(), bookRecieveGroup.getBrToDepartment(), bookRecieveGroup.getBrToGroup());
				List<String> tmpUsers = new ArrayList<String>();
				for(BookRecieveUser item : list){
					tmpUsers.add(item.getBrToUser());
				}
				if(arr != null){
					for(Integer item : arr.keySet()){
						if(tmpUsers.indexOf(item) == -1){
							Users user = userService.getUserById(item);
							BookRecieveUser bookRecieveTo = new BookRecieveUser();
							bookRecieveTo.setBrId(recive.getBrId());
							bookRecieveTo.setBrToGroup(recive.getBrToGroup());
							bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
							bookRecieveTo.setBrToDepartment(recive.getBrToDepartment());
							
							bookRecieveTo.setBrToUser(item.toString());
							bookRecieveTo.setStatus("N");
							bookRecieveTo.setBrToUserName(user.getPrefix() + user.getFname() + " " + user.getLname());
							bookRecieveGroup.setRemark(recive.getBrRemarkUser());
							reciveout.insertRecieveUser(bookRecieveTo);
						}
					}
				}
				
				brTo = recive.getBrToSection();
				reciveout.deleteRecieveSectionByBrId(recive.getBrId()); 
				if(!StringUtils.isNullOrEmpty(brTo)){
					if(arr != null){
						Iterator iterator = arr.entrySet().iterator();
				        while (iterator.hasNext()) {
				        	Map.Entry me2 = (Map.Entry) iterator.next();
							if(me2.getValue() != null && !me2.getValue().equals("null")) {
								Sections section = reciveout.getSectionById(Integer.parseInt(me2.getValue().toString()));
								BookRecieveSection bookRecieveTo = new BookRecieveSection();
								bookRecieveTo.setBrId(recive.getBrId());
								bookRecieveTo.setBrToGroup(section.getGroupId().toString());
								bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
								bookRecieveTo.setBrToDepartment(recive.getBrToDepartment());
								bookRecieveTo.setBrToSection(section.getId().toString());
								bookRecieveTo.setBrToSectionName(section.getSectionName());
								bookRecieveTo.setStatus("N");
								bookRecieveGroup.setRemark(recive.getBrRemarkSection());
								reciveout.insertRecieveSection(bookRecieveTo);	
							}
							
						}
					}	
				}
			}else if(role.equals("USER")){
				BookRecieveUser bookRecieveUser = new BookRecieveUser();
				bookRecieveUser.setBrId(recive.getBrId());
				bookRecieveUser.setBrToGroup(recive.getBrToGroup());
				bookRecieveUser.setBrToUser(recive.getBrToUser());
				bookRecieveUser.setBrToDepartment(recive.getBrToDepartment());
				bookRecieveUser.setRemark(recive.getBrRemarkUser());
				if(!StringUtils.isNullOrEmpty(recive.getBrStatus()) && recive.getBrStatus().equals("SUCCESS")){
					bookRecieveUser.setStatus("Y");
				}else{
					bookRecieveUser.setStatus("N");
				}
				rs = reciveout.updateReciveOutUser(bookRecieveUser);
			}
		}catch(Exception ex){
        	logger.error("updateReciveOut : ", ex);
			System.out.println("updateReciveOut : " + ex.getMessage());
        }
		return rs;
	}

	@Override
	public int getCountDataBookReciveOut(int year) {
		int rs = 0;
		try{
			rs = reciveout.getCountDataBookRecive(year);
		}catch(Exception ex){
        	logger.error("getCountDataBookSendOut : ", ex);
        }
		return rs;
	}

	@Override
	public BookReciveOut getLastRowOfYear(int brYear) {
		BookReciveOut rs = null;
		try{
			rs = reciveout.getLastRowOfYear(brYear);
		}catch(Exception ex){
        	logger.error("getLastRowOfYear : ", ex);
        }
		return rs;
	}

	@Override
	public List<StatusDetail> getStatusDetail(String brId) {
		List<StatusDetail> list = null;
		try{
			list = reciveout.getStatusDetail(brId);
		}catch(Exception ex){
        	logger.error("getStatusDetail : ", ex);
        }
		return list;
	}

	@Override
	public List<BookReciveOut> listReciveByYear(int year) {
		List<BookReciveOut> list = null;
		try{
			list = reciveout.listReciveByYear(year);
		}catch(Exception ex){
        	logger.error("listReciveByYear : ", ex);
        }
		return list;
	}

	@Override
	public List<BookReciveOut> listReciveByYearAndBrNum(int year, int brNum) {
		List<BookReciveOut> list = null;
		try{
			list = reciveout.listReciveByYearAndBrNum(year, brNum);
		}catch(Exception ex){
        	logger.error("listReciveByYearAndBrNum : ", ex);
        }
		return list;
	}

	@Override
	public void SaveReciveOutFromExcel(BookReciveOut recive) {
		int brId = 0;
		try{
			String[] arr = (StringUtils.isNullOrEmpty(recive.getBrToDepartment()) ? null : recive.getBrToDepartment().split(","));
			brId = reciveout.Save(recive);
			recive.setBrId(brId);
			if(brId > 0){
				if(arr != null){
					for(String item : arr){
						Divisions division = divisions.getDivisionByCode(item);
						BookRecieveDepartment bookRecieveTo = new BookRecieveDepartment();
						bookRecieveTo.setBrId(recive.getBrId());
						bookRecieveTo.setBrToDepartment(item);
						bookRecieveTo.setBrToDepartmentName(division.getDivisionName());
						bookRecieveTo.setBrToDepartmentShort(division.getDivisionShort());
						bookRecieveTo.setStatus("N");
						bookRecieveTo.setUpdatedBy(recive.getUpdatedBy());
						reciveout.insertRecieveDepartment(bookRecieveTo);
					}
				}
			}	
		}catch(Exception ex){
			logger.error("SaveReciveOutFromExcel : ", ex);
		}
	}

	@Override
	public Map<String, List<String>> getGroupSelectedByAdmin(String departments) {
		Map<String, List<String>> map = null;
		try {
			map = reciveout.getGroupSelectedByAdmin(departments);
		}catch(Exception ex){
			logger.error("getGroupSelectedByAdmin : ", ex);
		}
		return map;
	}

	@Override
	public Map<String, List<String>> getSectionSelectedByAdmin(String groups) {
		Map<String, List<String>> map = null;
		try {
			map = reciveout.getSectionSelectedByAdmin(groups);
		}catch(Exception ex){
			logger.error("getSectionSelectedByAdmin : ", ex);
		}
		return map;
	}

	@Override
	public Map<String, List<String>> getUserSelectedByAdmin(String groups, String sections) {
		Map<String, List<String>> map = null;
		try {
			map = reciveout.getUserSelectedByAdmin(groups, sections);
		}catch(Exception ex){
			logger.error("getGroupSelectedByAdmin : ", ex);
		}
		return map;
	}
}
