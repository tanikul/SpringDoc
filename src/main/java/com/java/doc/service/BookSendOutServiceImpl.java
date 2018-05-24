package com.java.doc.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.doc.dao.BookSendOutDAO;
import com.java.doc.model.BookReciveOut;
import com.java.doc.model.BookSendOut;
import com.java.doc.model.BookSendOutTable;
import com.java.doc.util.Constants;
import com.java.doc.util.TableSorter;
import com.java.doc.util.UtilDateTime;
import com.java.doc.util.Utility;
import com.java.doc.validator.BookSendOutValidator;

@Service("bookSendOutService")
public class BookSendOutServiceImpl implements BookSendOutService {

	private static final Logger logger = Logger.getLogger(BookSendOutServiceImpl.class);
	
	@Autowired
	@Qualifier("bookSendOutDao")
	private BookSendOutDAO sendout;
	
	@Override
	public boolean saveBookOut(BookSendOut sendout) {
		if(this.merge(sendout)){
			insertBsToExcel(sendout);
		}
		return true;
	}

	@Override
	public BookSendOut convert(BookSendOutValidator send) {
		BookSendOut old = new BookSendOut();
		old.setBsDate(send.getBsDate());
		old.setBsDivision(send.getBsDivision());
		old.setBsId(send.getBsId());
		old.setBsImage(send.getBsImage());
		old.setBsNum(send.getBsNum());
		old.setBsPcode(send.getBsPcode());
		old.setBsPlace(send.getBsPlace());
		old.setBsRdate(send.getBsRdate());
		old.setBsRemark(send.getBsRemark());
		old.setBsStatus(send.getBsStatus());
		old.setBsSubject(send.getBsSubject());
		old.setBsTo(send.getBsTo());
		old.setBsTypeQuick(send.getBsTypeQuick());
		old.setBsTypeSecret(send.getBsTypeSecret());
		old.setBsYear(send.getBsYear());
		return old;
	}

	@Override
	public List<BookSendOut> listSendOut() {
		return sendout.listSendOut();
	}

	@Override
	public BookSendOutTable ListPageSendOut(TableSorter table) {
		return sendout.ListPageSendOut(table);
	}

	@Override
	public int CountSelect() {
		return sendout.CountSelect();
	}

	@Override
	public int LastID() {
		return sendout.LastID();
	}

	@Override
	public List<Integer> getYear() {
		return sendout.getYear();
	}

	@Override
	public boolean saveOrUpdate(BookSendOut recive) {
		return sendout.saveOrUpdate(recive);
	}

	@Override
	public boolean merge(BookSendOut recive) {
		return sendout.merge(recive);
	}

	@Override
	public Integer getNextBsNum(int bsYear) {
		return sendout.getNextBsNum(bsYear);
	}

	@Override
	public BookSendOut getDataFromId(int id) {
		return sendout.getDataFromId(id);
	}

	@Override
	public String delete(BookSendOut bs) throws Exception {
		boolean chk = false;
		try {
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelSendOutPath").split("\\.");
			String fileName = cons.getProperty("excelSendOutPath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + bs.getBsYear() + "." + arrStr[1];
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
	            	int rowNumber = Utility.getRowNumberForDelete(sheet, bs.getBsNum(), 3);
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
			logger.error("delete BsToExcel : ", ex);
			throw ex;
		}
		return (chk) ? sendout.delete(bs.getBsId()) : "0";
	}

	@Override
	public int getCountDataBookSendOut(int year) {
		int rs = 0;
		try{
			rs = sendout.getCountDataBookRecive(year);
		}catch(Exception ex){
        	logger.error("getCountDataBookSendOut : ", ex);
        }
		return rs;
	}

	@Override
	public boolean insertBsToExcel(BookSendOut sendOut) {
		logger.info("insertBsToExcel : " + sendOut.toString());
		try {
			boolean chkExistFile = false;
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelSendOutPath").split("\\.");
			String fileName = cons.getProperty("excelSendOutPath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + sendOut.getBsYear() + "." + arrStr[1];
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
            	cell.setCellValue("วันที่ส่งหนังสือ");
            	
            	cell = row.createCell(2);
            	cell.setCellValue("หน่วยงานที่ส่ง");
            	
            	cell = row.createCell(3);
            	cell.setCellValue("เลขทะเบียนส่ง");
            	
            	cell = row.createCell(4);
            	cell.setCellValue("ที่");
            	
            	cell = row.createCell(5);
            	cell.setCellValue("ลงวันที่");
            	
            	cell = row.createCell(6);
            	cell.setCellValue("จาก");
            	
            	cell = row.createCell(7);
            	cell.setCellValue("ถึง");
            	
            	cell = row.createCell(8);
            	cell.setCellValue("เรื่อง");
            	
            	cell = row.createCell(9);
            	cell.setCellValue("ขั้นความเร็ว");
            	
            	cell = row.createCell(10);
            	cell.setCellValue("ขั้นความลับ");
            	
            	cell = row.createCell(11);
            	cell.setCellValue("หมายเหตุ");
            		
            }
            
			
            row = sheet.createRow(lastRow + 1);
            
        	cell = row.createCell(0);
        	cell.setCellValue((int) sendOut.getBsYear());
        	
        	cell = row.createCell(1);
        	cell.setCellValue(UtilDateTime.convertToDateTH(sendOut.getBsRdate()));
        	
        	cell = row.createCell(2);
        	cell.setCellValue(sendOut.getBsDivision());
        	
        	cell = row.createCell(3);
        	cell.setCellValue((int) sendOut.getBsNum());
        	
        	cell = row.createCell(4);
        	cell.setCellValue((String) sendOut.getBsPlace());
        	
        	cell = row.createCell(5);
        	cell.setCellValue(UtilDateTime.convertToDateTH(sendOut.getBsDate()));
        	
        	cell = row.createCell(6);
        	cell.setCellValue((String) sendOut.getBsFrom());
        	
        	cell = row.createCell(7);
        	cell.setCellValue((String) sendOut.getBsTo());
        	
        	cell = row.createCell(8);
        	cell.setCellValue((String) sendOut.getBsSubject());
        	
        	cell = row.createCell(9);
        	cell.setCellValue((int) sendOut.getBsTypeQuick());
        	
        	cell = row.createCell(10);
        	cell.setCellValue((int) sendOut.getBsTypeSecret());
        	
        	cell = row.createCell(11);
        	cell.setCellValue((String) sendOut.getBsRemark());
        	
        	if(chkExistFile){
	        	file.close();
        	}
        	FileOutputStream out = new FileOutputStream(new File(fileName));
			workbook.write(out);
			out.close();
        }catch(Exception ex){
        	logger.error("insertBsToExcel : ", ex);
        }
            
        return true;
	}
	
	public boolean updateBsToExcel(BookSendOut sendOut) {
		logger.info("updateBsToExcel : " + sendOut.toString());
		try {
			Constants cons = new Constants();
			String[] arrStr = cons.getProperty("excelSendOutPath").split("\\.");
			String fileName = cons.getProperty("excelSendOutPath");
			if(arrStr.length == 2){
				fileName = arrStr[0] + sendOut.getBsYear() + "." + arrStr[1];
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
                        Cell celld = nextRow.getCell(3);
                        int bsNum = 0;
                        switch (celld.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                            	bsNum = Integer.parseInt(celld.getStringCellValue());
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                            	bsNum = (int) celld.getNumericCellValue();
                                break;
                        }
                        if(bsNum == sendOut.getBsNum()){
                        	break;
                        }
                	}
                	if(nextRow != null){
                		cell = nextRow.createCell(0);
                    	cell.setCellValue((int) sendOut.getBsYear());
                    	
                    	cell = nextRow.createCell(1);
                    	cell.setCellValue(UtilDateTime.convertToDateTH(sendOut.getBsRdate()));
                    	
                    	cell = nextRow.createCell(2);
                    	cell.setCellValue(sendOut.getBsDivision());
                    	
                    	cell = nextRow.createCell(4);
                    	cell.setCellValue((String) sendOut.getBsPlace());
                    	
                    	cell = nextRow.createCell(5);
                    	cell.setCellValue(UtilDateTime.convertToDateTH(sendOut.getBsDate()));
                    	
                    	cell = nextRow.createCell(6);
                    	cell.setCellValue((String) sendOut.getBsFrom());
                    	
                    	cell = nextRow.createCell(7);
                    	cell.setCellValue((String) sendOut.getBsTo());
                    	
                    	cell = nextRow.createCell(8);
                    	cell.setCellValue((String) sendOut.getBsSubject());
                    	
                    	cell = nextRow.createCell(9);
                    	cell.setCellValue((int) sendOut.getBsTypeQuick());
                    	
                    	cell = nextRow.createCell(10);
                    	cell.setCellValue((int) sendOut.getBsTypeSecret());
                    	
                    	cell = nextRow.createCell(11);
                    	cell.setCellValue((String) sendOut.getBsRemark());
                    	
                    	file.close();
                    	FileOutputStream out = new FileOutputStream(new File(fileName));
            			workbook.write(out);
            			out.close();
                    	break;
                	}
                }
        	}
        }catch(Exception ex){
        	logger.error("updateBsToExcel : ", ex);
        }
            
        return true;
	}

	
	@Override
	public BookSendOut getLastRowOfYear(int bsYear) {
		BookSendOut rs = null;
		try{
			rs = sendout.getLastRowOfYear(bsYear);
		}catch(Exception ex){
        	logger.error("getLastRowOfYear : ", ex);
        }
		return rs;
	}


	@Override
	public boolean updateBookOut(BookSendOut bookSendOut) {
		boolean result = false;
		try {
			if(sendout.updateSendOut(bookSendOut) == 1){
				updateBsToExcel(bookSendOut);
			}
			result = true;
		}catch(Exception e){
			logger.error("updateBookOut : ", e);
		}
		return result;
	}

	@Override
	public void saveBookOutFromExcel(BookSendOut bookSendOut) {
		this.merge(bookSendOut);
	}

	@Override
	public List<BookSendOut> listSendOutByYearAndBsNum(int year, int bsNum) {
		List<BookSendOut> list = null;
		try{
			list = sendout.listSendOutByYearAndBsNum(year, bsNum);
		}catch(Exception ex){
        	logger.error("listSendOutByYearAndBrNum : ", ex);
        }
		return list;
	}

	@Override
	public List<BookSendOut> listSendOutByYear(int year) {
		List<BookSendOut> list = null;
		try{
			list = sendout.listSendOutByYear(year);
		}catch(Exception ex){
        	logger.error("listSendOutByYear : ", ex);
        }
		return list;
	}

}
