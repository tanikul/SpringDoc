<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<div class="panel panel-default">
  <div class="panel-body">
      <springForm:form id="usersForm" action="" method="POST" commandName="users" cssClass="form-horizontal">
			  <springForm:hidden path="id" value="${obj.getId()}"/>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Username</label>
			    <div class="col-sm-9">
			    	<springForm:input path="username" cssClass="form-control" value="${obj.getUsername()}"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">Password</label>
			    <div class="col-sm-9">
			    	<springForm:password path="password" cssClass="form-control" value="${obj.getPassword()}"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">ชื่อ</label>
			    <div class="col-sm-9">
			    	<springForm:input path="fname" cssClass="form-control" value="${obj.getFname()}"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">นามสกุล</label>
			    <div class="col-sm-9">
			    	<springForm:input path="lname" cssClass="form-control" value="${obj.getLname()}"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">หน่วยงาน</label>
			    <div class="col-sm-9">
			    	<springForm:select path="divisionCode" cssClass="form-control" value="${obj.getDivisionCode()}" onchange="ex.changeDivision(this);">
			    		<springForm:option value="" label="--- เลือกหน่วยงาน ---" />
			    		<springForm:options items="${divisions}" />
			    	</springForm:select>   
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-2"></div>
			    <div class="col-sm-9">
			      <button class="btn btn-info" type="button" onclick="saveEditUser();">บันทึก</button>
			    </div>
			  </div>
		</springForm:form>
    </div>
 </div>