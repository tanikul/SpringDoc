package com.java.doc.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.java.doc.model.PdfForm;
import com.java.doc.util.UtilDateTime;

public class ExcelRevenueReportView extends AbstractExcelView{
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook,
		HttpServletRequest request, 
		HttpServletResponse response)
		throws Exception {
	
		HashMap<Integer, String> typeQuick = (HashMap<Integer, String>) request.getSession().getAttribute("typeQuick");
		HashMap<Integer, String> typeSecret = (HashMap<Integer, String>) request.getSession().getAttribute("typeQuick");
		Map<String, List<PdfForm>> revenueData = (Map<String, List<PdfForm>>) model.get("revenueData");
		//create a wordsheet
		
		
		
		if(revenueData.containsKey("IN")){
			int rowNum = 1;	
			PdfForm pdfForm= (PdfForm) revenueData.entrySet().iterator().next().getValue().get(0);
			HSSFSheet sheet = workbook.createSheet(pdfForm.getBrYear().toString());
			HSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("BR_ID");
			header.createCell(1).setCellValue("BR_NUM");	
			header.createCell(2).setCellValue("BR_YEAR");	
			header.createCell(3).setCellValue("BR_RDATE");	
			header.createCell(4).setCellValue("BR_TYPE_QUICK");	
			header.createCell(5).setCellValue("BR_TYPE_SECRET");	
			header.createCell(6).setCellValue("BR_PLACE");	
			header.createCell(7).setCellValue("BR_DATE");	
			header.createCell(8).setCellValue("BR_FROM");	
			header.createCell(9).setCellValue("BR_TO");	
			header.createCell(10).setCellValue("BR_SUBJECT");	
			header.createCell(11).setCellValue("BR_REMARK");	
			header.createCell(12).setCellValue("BR_DIVISION");	
			header.createCell(13).setCellValue("BR_PCODE");	
			header.createCell(14).setCellValue("BR_STATUS");	
			header.createCell(15).setCellValue("BR_IMAGE");
			Integer year = pdfForm.getBrYear();
			Map<Integer, Integer> tmpYear = new HashMap<Integer, Integer>();
			for (Map.Entry<String, List<PdfForm>> entry : revenueData.entrySet()) {
				for(PdfForm item : entry.getValue()){
					if(item.getBrYear().compareTo(year) != 0){
						tmpYear.put(year, rowNum);
						if(tmpYear.containsKey(item.getBrYear())){
							rowNum = tmpYear.get(item.getBrYear());
						}else{
							rowNum = 1;
							year = item.getBrYear();
							sheet = workbook.createSheet(year.toString());
							header = sheet.createRow(0);
							header.createCell(0).setCellValue("BR_ID");
							header.createCell(1).setCellValue("BR_NUM");	
							header.createCell(2).setCellValue("BR_YEAR");	
							header.createCell(3).setCellValue("BR_RDATE");	
							header.createCell(4).setCellValue("BR_TYPE_QUICK");	
							header.createCell(5).setCellValue("BR_TYPE_SECRET");	
							header.createCell(6).setCellValue("BR_PLACE");	
							header.createCell(7).setCellValue("BR_DATE");	
							header.createCell(8).setCellValue("BR_FROM");	
							header.createCell(9).setCellValue("BR_TO");	
							header.createCell(10).setCellValue("BR_SUBJECT");	
							header.createCell(11).setCellValue("BR_REMARK");	
							header.createCell(12).setCellValue("BR_DIVISION");	
							header.createCell(13).setCellValue("BR_PCODE");	
							header.createCell(14).setCellValue("BR_STATUS");	
							header.createCell(15).setCellValue("BR_IMAGE");
						}
					}
					
					HSSFRow row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(item.getBrId());
					row.createCell(1).setCellValue(item.getBrNum());
					row.createCell(2).setCellValue(item.getBrYear());
					row.createCell(3).setCellValue(item.getBrRdateStr());
					row.createCell(4).setCellValue((typeQuick.containsKey(item.getBrTypeQuick())) ? typeQuick.get(item.getBrTypeQuick()) : "");
					row.createCell(5).setCellValue((typeSecret.containsKey(item.getBrTypeSecret())) ? typeSecret.get(item.getBrTypeSecret()) : "");
					row.createCell(6).setCellValue(item.getBrPlace());
					row.createCell(7).setCellValue(item.getBrDateStr());
					row.createCell(8).setCellValue(item.getBrFrom());
					row.createCell(9).setCellValue(item.getBrTo());
					row.createCell(10).setCellValue(item.getBrSubject());
					row.createCell(11).setCellValue(item.getBrRemark());
					row.createCell(12).setCellValue(item.getBrDivision());
					row.createCell(13).setCellValue(item.getBrPcode());
					row.createCell(14).setCellValue(item.getBrStatus());
					row.createCell(15).setCellValue(item.getBrImage());
				}
	        }
		}else{
			int rowNum = 1;	
			PdfForm pdfForm= (PdfForm) revenueData.entrySet().iterator().next().getValue().get(0);
			HSSFSheet sheet = workbook.createSheet(pdfForm.getBrYear().toString());
			HSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("BS_ID");
			header.createCell(1).setCellValue("BS_NUM");	
			header.createCell(2).setCellValue("BS_TYPE_QUICK");	
			header.createCell(3).setCellValue("BS_TYPE_SECRET");	
			header.createCell(4).setCellValue("BS_YEAR");	
			header.createCell(5).setCellValue("BS_RDATE");	
			header.createCell(6).setCellValue("BS_PLACE");	
			header.createCell(7).setCellValue("BS_DATE");	
			header.createCell(8).setCellValue("BS_FROM");	
			header.createCell(9).setCellValue("BS_TO");	
			header.createCell(10).setCellValue("BS_SUBJECT");	
			header.createCell(11).setCellValue("BS_REMARK");	
			header.createCell(12).setCellValue("BS_DIVISION");	
			header.createCell(13).setCellValue("BS_PCODE");	
			header.createCell(14).setCellValue("BS_STATUS");	
			header.createCell(15).setCellValue("BS_IMAGE");	
			Integer year = pdfForm.getBrYear();
			Map<Integer, Integer> tmpYear = new HashMap<Integer, Integer>();
			for (Map.Entry<String, List<PdfForm>> entry : revenueData.entrySet()) {
				for(PdfForm item : entry.getValue()){
					if(item.getBrYear() != year){
						tmpYear.put(year, rowNum);
						if(tmpYear.containsKey(item.getBrYear())){
							rowNum = tmpYear.get(item.getBrYear());
						}else{
							rowNum = 1;
							year = item.getBrYear();	
							sheet = workbook.createSheet(year.toString());
							header = sheet.createRow(0);
							header.createCell(0).setCellValue("BS_ID");
							header.createCell(1).setCellValue("BS_NUM");	
							header.createCell(2).setCellValue("BS_TYPE_QUICK");	
							header.createCell(3).setCellValue("BS_TYPE_SECRET");	
							header.createCell(4).setCellValue("BS_YEAR");	
							header.createCell(5).setCellValue("BS_RDATE");	
							header.createCell(6).setCellValue("BS_PLACE");	
							header.createCell(7).setCellValue("BS_DATE");	
							header.createCell(8).setCellValue("BS_FROM");	
							header.createCell(9).setCellValue("BS_TO");	
							header.createCell(10).setCellValue("BS_SUBJECT");	
							header.createCell(11).setCellValue("BS_REMARK");	
							header.createCell(12).setCellValue("BS_DIVISION");	
							header.createCell(13).setCellValue("BS_PCODE");	
							header.createCell(14).setCellValue("BS_STATUS");	
							header.createCell(15).setCellValue("BS_IMAGE");	
						}
					}
					HSSFRow row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue(item.getBrId());
					row.createCell(1).setCellValue(item.getBrNum());
					row.createCell(2).setCellValue((typeQuick.containsKey(item.getBrTypeQuick())) ? typeQuick.get(item.getBrTypeQuick()) : "");
					row.createCell(3).setCellValue((typeSecret.containsKey(item.getBrTypeSecret())) ? typeSecret.get(item.getBrTypeSecret()) : "");
					row.createCell(4).setCellValue(item.getBrYear());
					row.createCell(5).setCellValue(item.getBrRdateStr());
					row.createCell(6).setCellValue(item.getBrPlace());
					row.createCell(7).setCellValue(item.getBrDateStr());
					row.createCell(8).setCellValue(item.getBrFrom());
					row.createCell(9).setCellValue(item.getBrTo());
					row.createCell(10).setCellValue(item.getBrSubject());
					row.createCell(11).setCellValue(item.getBrRemark());
					row.createCell(12).setCellValue(item.getBrDivision());
					row.createCell(13).setCellValue(item.getBrPcode());
					row.createCell(14).setCellValue(item.getBrStatus());
					row.createCell(15).setCellValue(item.getBrImage());
				}
	        }
		}
		
		
		
	}
}