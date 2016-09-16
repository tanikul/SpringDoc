package com.java.doc.validator;

import java.util.List;

public class ValidatorResponse {

	private String status;
	private List<ErrorMessage> errorMessageList;
	private Object result;
	
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ErrorMessage> getErrorMessageList() {
		return errorMessageList;
	}
	public void setErrorMessageList(List<ErrorMessage> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}
	
}
