<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<script src="<c:url value="/js/script.js" />"></script>
<div class="panel panel-default">
  <div class="panel-body">
      <springForm:form id="usersForm" action="" method="POST" commandName="users" cssClass="form-horizontal">
			  <springForm:hidden path="id"/>
			  
			  <div class="form-group">
			    <label class="col-sm-3 control-label">Username</label>
			    <div class="col-sm-9">
			    	<springForm:input path="username" cssClass="form-control"disabled="true"/>
			    </div>
			  </div>
			  <c:choose>
				  <c:when test="${role != 'ADMIN'}">
					  <div class="form-group">
					    <label class="col-sm-3 control-label">Password</label>
					    <div class="col-sm-9">
					    	<springForm:password path="password" cssClass="form-control"/>
					    </div>
					  </div>
				  </c:when>
				  <c:otherwise>
				  	 <springForm:hidden path="password"/>
				  </c:otherwise>
			  </c:choose>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">ชื่อ</label>
			    <div class="col-sm-3">
			    	<springForm:select path="prefix" cssClass="form-control">
			    		<springForm:option value="" label="--- คำนำหน้า ---" />
			    		<springForm:options items="${prefixs}" />
			    	</springForm:select>   
			    </div>
			    <div class="col-sm-6">
			    	<springForm:input path="fname" cssClass="form-control"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">นามสกุล</label>
			    <div class="col-sm-9">
			    	<springForm:input path="lname" cssClass="form-control"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">สิทธิ์การใช้ระบบ</label>
			    <div class="col-sm-9">
			    	<springForm:select path="role" cssClass="form-control" onchange="changeRoleToSection(this);">
			    		<springForm:option value="" label="--- เลือกสิทธิ์ ---" />
			    		<springForm:options items="${roles}" />
			    	</springForm:select>   
			    	<p class="help-block"></p>
			    </div>
			  </div>
			  <div class="form-group" id="division-box" style="display:none;">
			    <label class="col-sm-3 control-label">ส่วนราชการ</label>
			    <div class="col-sm-9">
			    	<springForm:select path="divisionCode" cssClass="form-control" onchange="changeDivisionToGroup(this);">
			    		<springForm:option value="" label="--- เลือกสำนัก ---" />
			    		<springForm:options items="${divisions}" />
			    	</springForm:select>   
			    </div>
			  </div>
			  <div class="form-group" id="group-boxes" style="display:none;">
			    <label class="col-sm-3 control-label">ฝ่าย/ส่วนงาน</label>
			    <div class="col-sm-9" id="group-box">
			    	<springForm:select path="groupId" cssClass="form-control" onchange="changeGroupToSection(this);">
			    		<springForm:option value="" label="--- เลือกฝ่าย ---" />
			    		<springForm:options items="${groups}" />
			    	</springForm:select>   
			    	<p class="help-block"></p>
			    </div>
			  </div>
			  <div class="form-group" id="section-boxes" style="display:none;">
			    <label class="col-sm-3 control-label">กลุ่มงาน</label>
			    <div class="col-sm-9" id="section-box">
			    	<springForm:select path="sectionId" cssClass="form-control">
			    		<springForm:option value="" label="--- เลือกกลุ่มงาน ---" />
			    		<springForm:options items="${sections}" />
			    	</springForm:select>   
			    	<p class="help-block"></p>
			    </div>
			  </div>
			  
			  <div class="form-group" id="position-box" style="display:none;">
			    <label class="col-sm-3 control-label">ตำแหน่ง</label>
			    <div class="col-sm-9">
			    	<springForm:select path="boardId" cssClass="form-control">
			    		<springForm:option value="" label="--- เลือกตำแหน่ง ---" />
			    		<springForm:options items="${boards}" />
			    	</springForm:select>   
			    	<p class="help-block"></p>
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-3"></div>
			    <div class="col-sm-9">
			      <button class="btn btn-info" type="button" onclick="saveEditUser();">บันทึก</button>
			    </div>
			  </div>
		</springForm:form>
    </div>
 </div>
 
 <script type="text/javascript">
	$(function() {
		var role = "${users.role}";
		if(role == 'BOARD'){
			$('#position-box').show();
			$('#division-box').hide();
			$('#group-boxes').hide();
			$('#section-boxes').hide();
		}else{
			$('#position-box').hide();
			
			$('#division-box').show();
			$('#group-boxes').show();
			$('#section-boxes').show();
		}
	});
 </script>