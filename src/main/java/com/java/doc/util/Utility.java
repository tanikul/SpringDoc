package com.java.doc.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.java.doc.model.BookReciveOut;
import com.mysql.jdbc.StringUtils;

public class Utility {

	public static String getStrTo(BookReciveOut br){
		String str = "";
		if(!StringUtils.isNullOrEmpty(br.getBrToDepartmentShort())){
			str += "กอง" + br.getBrToDepartmentShort() + " ";
		}else if(!StringUtils.isNullOrEmpty(br.getBrToDepartmentName())){
			str += "กอง" + br.getBrToDepartmentName() + " ";
		}else if(!StringUtils.isNullOrEmpty(br.getBrToDepartment())){
			str += "กอง" + br.getBrToDepartment() + " ";
		}
		if(!StringUtils.isNullOrEmpty(br.getBrToGroupName())){
			str += "ฝ่าย" + br.getBrToGroupName() + " ";
		}
		if(!StringUtils.isNullOrEmpty(br.getBrToUserName())){
			str += br.getBrToUserName();
		}
		return str;
	}
	
	public static String getStatus(BookReciveOut br){
		String str = "";
		try{
			if(!StringUtils.isNullOrEmpty(br.getBrStatus())){
				if(br.getBrStatus().equals("DEPARTMENT")){
					if(!StringUtils.isNullOrEmpty(br.getBrToDepartmentShort())){
						str += "ถึงกอง" + br.getBrToDepartmentShort();
					}else if(!StringUtils.isNullOrEmpty(br.getBrToDepartmentName())){
						str += "กอง" + br.getBrToDepartmentName() + " ";
					}else if(!StringUtils.isNullOrEmpty(br.getBrToDepartment())){
						str += "กอง" + br.getBrToDepartment() + " ";
					}
				}else if(br.getBrStatus().equals("GROUP")){
					if(!StringUtils.isNullOrEmpty(br.getBrToDepartmentShort())){
						str += "ถึงกอง" + br.getBrToDepartmentShort();
					}
					if(!StringUtils.isNullOrEmpty(br.getBrToGroupName())){
						str += " ฝ่าย" + br.getBrToGroupName();
					}
				}else if(br.getBrStatus().equals("USER")){
					if(!StringUtils.isNullOrEmpty(br.getBrToDepartmentShort())){
						str += "ถึงกอง" + br.getBrToDepartmentShort();
					}
					if(!StringUtils.isNullOrEmpty(br.getBrToGroupName())){
						str += " ฝ่าย" + br.getBrToGroupName();
					}
					if(!StringUtils.isNullOrEmpty(br.getBrToUserName())){
						str += " " + br.getBrToUserName();
					}
				}else if(br.getBrStatus().equals("SUCCESS")){
					str += "สิ้นสุด";
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return str;
	}
	
	public static int getRowNumberForDelete(XSSFSheet sheet, int num, int cellColumn) {
		int result = 0;
		for (Row row : sheet) {
			if(row.getRowNum() == 0) continue;
    		Cell cell = row.getCell(cellColumn);  
    		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
    		if (cell.getNumericCellValue() == num) {
            	result = row.getRowNum();
            	break;
            }
        }
		return result;
	}
	
}
