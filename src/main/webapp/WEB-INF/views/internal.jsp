<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@page session="true"%>
<c:set var="now" value="<%=new java.util.Date()%>" />
<t:master>
	<jsp:body>
		<div id="actualbody">
			<div class="row clearfix">
				<div class="col_12">
					<div class="widget clearfix">
					<c:if test="${mode == 'add'}"><h2>เพิ่มรายการหนังสือรับ - จากภายนอก</h2></c:if>
					<c:if test="${mode == 'edit'}"><h2>แก้ไขรายการหนังสือรับ - จากภายนอก</h2></c:if>
				<div class="widget_inside">	
	      <springForm:form id="sendReciveForm" action="" method="POST" commandName="sendRecive" cssClass="form-horizontal">
	      	<input type="hidden" id="attachmentIdList" name="attachmentIdList" value=""/>
	      	<input type="hidden" id="mode" name="mode" value="${mode}"/>
	      <div class="col-md-7">
			  <div class="form-group" style="color: red">
			    <label class="col-sm-3 control-label">ขั้นความเร็ว</label>
			    <div class="col-sm-9">
			    <c:forEach var="item" items="${quick}" varStatus="loop">
			      <div class="radio-inline">
				    <label>
				    	<springForm:radiobutton path="brTypeQuick" value="${item.getId()}" disabled="${disable}"/> ${item.getTypeQuick()}
				    </label>
				  </div>
				 </c:forEach>
			    </div>
			  </div>
			  
			  <div class="form-group">
			    <label class="col-sm-3 control-label">วันที่รับหนังสือ</label>
			    <div class="col-sm-4">
			      <div class="input-group date" id="datetimepicker1" data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="brRdate" cssClass="form-control" readonly="true" disabled="${disable}"/>
		                <span class="input-group-addon">
		                    <span class="glyphicon glyphicon-calendar"></span>
		                </span>
		            </div>
		            <small class="help-block" style="color:red" id="err-brDate"></small>
			    </div>
			    <div class="col-sm-5">
			      <div class="checkbox">
				    <label>
				      <c:if test="${mode == 'add'}"><input type="checkbox" id="date_1" value="1" onclick="inter.checkChangeDate(this);"> กำหนดเอง</c:if>
				    </label>
				  </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">เลขทะเบียนรับ</label>
			    <div class="col-sm-2 slash">
			    	<springForm:hidden path="brId"/>
			    	<springForm:input path="brNum" cssClass="form-control" disabled="${disable}"/>
			    </div>
			    <div class="col-sm-1 slashs"><label class="label-control">/</label></div>
			    <div class="col-sm-2 slash">
			      <springForm:input path="brYear" cssClass="form-control" disabled="${disable}"/>
			    </div>
			    <!-- <div class="col-sm-3">
			      <div class="checkbox">
				    <label>
				      <input type="checkbox" onclick="inter.checkChangeRegis(this);"> กำหนดเอง
				    </label>
				  </div>
			    </div> -->
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">เลขที่หนังสือ</label>
			    <div class="col-sm-4">
			      <springForm:input path="brPlace" cssClass="form-control" disabled="${disable}"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">ลงวันที่</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker2' data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="brDate" cssClass="form-control" disabled="${disable}"/>
		                <span class="input-group-addon">
		                    <span class="glyphicon glyphicon-calendar"></span>
		                </span>
		            </div>
		            <small class="help-block" style="color:red" id="err-brRDate"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">จาก</label>
			    <div class="col-sm-9">
			      <springForm:input path="brFrom" cssClass="form-control" disabled="${disable}"/>
			      <small class="help-block" style="color:red" id="err-brFrom"></small>
			    </div>
			  </div>
			  <c:if test="${role == 'ADMIN'}">
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึง</label>
				    <div class="col-sm-9">
				      <springForm:input path="brTo" cssClass="form-control" disabled="${disable}"/>
				      <small class="help-block" style="color:red" id="err-brTo"></small>
				    </div>
				  </div>
			  </c:if>
			  <c:choose>
			  	<c:when test="${role == 'ADMIN'}">
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึงสำนัก</label>
				    <div class="col-sm-9">
			    		<springForm:select path="brToDepartment" cssClass="form-control selectpicker" multiple="true" >
				    		<springForm:options items="${divisions}" />
				    	</springForm:select>	    	
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึงฝ่าย</label>
				    <div class="col-sm-9" id="admin-group">
				    	<springForm:select path="brToGroup" cssClass="form-control selectpicker" multiple="true" >
				    		<springForm:options items="${groups}" />
				    	</springForm:select>   
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึงกลุ่มงาน</label>
				    <div class="col-sm-9" id="admin-section">
				    	<springForm:select path="brToSection" cssClass="form-control selectpicker" multiple="true" >
				    		<springForm:options items="${sectionsId}" />
				    	</springForm:select>   
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึงบุคคล</label>
				    <div class="col-sm-9" id="admin-user">
				    	<springForm:select path="brToUser" cssClass="form-control selectpicker" multiple="true" >
				    		<springForm:options items="${userGroups}" />
				    	</springForm:select>   
				    </div>
				  </div>
				</c:when>
				<c:when test="${role == 'DEPARTMENT'}">
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึงฝ่าย</label>
				    <div class="col-sm-9">
				    	<springForm:select path="brToGroup" cssClass="form-control selectpicker" multiple="true" >
				    		<springForm:options items="${groups}" />
				    	</springForm:select>   
				    </div>
				  </div>
				  <springForm:hidden path="brToDepartment"/>
				</c:when>
				<c:when test="${role == 'GROUP'}">
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึงกลุ่มงาน</label>
				    <div class="col-sm-9" id="admin-section">
				    	<springForm:select path="brToSection" cssClass="form-control selectpicker" multiple="true" >
				    		<springForm:options items="${sectionsId}" />
				    	</springForm:select>   
				    </div>
				  </div>
				  <div class="form-group">
				    <label class="col-sm-3 control-label">ถึงบุคคล</label>
				    <div class="col-sm-9" id="admin-user">
				    	<springForm:select path="brToUser" cssClass="form-control selectpicker" multiple="true" >
				    		<springForm:options items="${userGroups}" />
				    	</springForm:select>   
				    </div>
				  </div>
				  <springForm:hidden path="brToGroup"/>
				  <springForm:hidden path="brToDepartment"/>
				  <springForm:hidden path="brToSection"/>
				</c:when>
				<c:when test="${role == 'USER'}">
					<springForm:hidden path="brToGroup"/>
				 	<springForm:hidden path="brToDepartment"/>
				 	<springForm:hidden path="brToUser"/>
				</c:when>
			  </c:choose>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">เรื่อง</label>
			    <div class="col-sm-9">
			       <springForm:input path="brSubject" cssClass="form-control" disabled="${disable}"/>
			       <small class="help-block" style="color:red" id="err-brSubject"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">หมายเหตุ</label>
			    <div class="col-sm-9">
			      <springForm:textarea cssClass="form-control" path="brRemark" disabled="${disable}"></springForm:textarea>
			    </div>
			  </div>
			  <c:if test="${role eq 'USER'}">
				  <div class="form-group">
				    <label class="col-sm-3 control-label" for="brStatus">เอกสารเสร็จสิ้น</label>
				    <div class="col-sm-1">
				      <springForm:checkbox path="brStatus" value="SUCCESS"></springForm:checkbox>
				    </div>
				  </div>
			  </c:if>
			  <div class="form-group">
			    <div class="col-sm-3"></div>
			    <div class="col-sm-9">
			    	<button class="btn btn-info" type="button" id="plupload_start">บันทึก</button>
			    </div>
			  </div>
			</div>
			<div class="col-md-5">
			<div class="form-group" style="color: red">
			    <label class="col-sm-3 control-label">ขั้นความลับ</label>
			    <div class="col-sm-9">
			    <c:forEach var="item" items="${secret}" varStatus="loop">
			      <div class="radio-inline">
				    <label>
					    <springForm:radiobutton path="brTypeSecret" value="${item.getId()}" disabled="${disable}"/> ${item.getTypeSecret()}
				    </label>
				  </div>
				  </c:forEach>
			    </div>
			  </div>
			  <div class="form-group">
			  	<div class="row" style="padding: 0px;">
			  		<label class="col-sm-3 control-label">Attachments</label>
			  	</div>
			  	<div class="row" style="padding: 0px;">
					<div class="col-sm-12">
						<table id="myTable" class="tablesorter">
						  <thead>
							<tr>
							  <th class="header">#</th>
							  <th class="header">Filename</th>
			    			  <c:if test="${disable != 'true'}">
							  	<th class="header">ลบ</th>
							  </c:if>
							</tr>
						  </thead>
						  <tbody>
						    <c:forEach var="attachment" items="${attachments}" varStatus="loop">
						      <tr>
						        <td align="center"><c:out value="${loop.count}"/></td>
						        <td>
						        	<a href="<c:url value="/downloadFiles/?id="/>${attachment.attachmentId}"><c:out value="${attachment.attachmentName}"/></a>
						        </td>
						        <c:if test="${disable != 'true'}">
							        <td align="center">
							        	<a href="javascript:;" class="btn-del-attachment" data-attachment-id="${attachment.attachmentId}"><span class='glyphicon glyphicon-trash'></span></a>
							        </td>
						        </c:if>
						      </tr>
						    </c:forEach>
						  </tbody>
						</table>
					</div>
				</div>
			  </div>
			  <div class="form-group" id="upload_form">
				<div class="row" style="padding: 0px;">
					<div class="col-sm-12">
						<div id="uploader">
						    <p>Your browser doesn't have Flash, Silverlight or HTML5 support.</p>
						</div>
					</div>
				</div>
			  </div>
			</div>
			</springForm:form>
			</div>
	      </div>
	      </div>
	   </div>
	</div>
	<script type="text/javascript">
		var fileCnt = 0;
	</script>
	<link href="<c:url value="/css/blue/style.css" />" rel="stylesheet">
	<link href="<c:url value="/css/bootstrap-datetimepicker.css" />" rel="stylesheet">
	
	<script src="<c:url value="/js/moment.js" />"></script>
	<script src="<c:url value="/js/bootstrap-datepicker.js" />"></script>
	<script src="<c:url value="/js/bootstrap-datepicker-thai.js" />"></script>
	<script src="<c:url value="/js/locale/bootstrap-datepicker.th.js" />"></script>
	
	<!--<script src="<c:url value="/js/plupload.full.min.js" />"></script>
	 <script src="<c:url value="/js/jquery.plupload.queue.js" />"></script>-->
	<script src="<c:url value="/plugins/plupload-2.1.8/js/plupload.full.min.js" />"></script>
	<script src="<c:url value="/plugins/plupload-2.1.8/js/jquery.plupload.queue/jquery.plupload.queue.js" />"></script> 
	<link href="<c:url value="/plugins/plupload-2.1.8/js/jquery.plupload.queue/css/jquery.plupload.queue.css" />" rel="stylesheet">
	
	<script src="<c:url value="/js/jquery.tablesorter.js" />"></script>
	<script src="<c:url value="/js/jquery.tablesorter.pager.js" />"></script>
	<script src="<c:url value="/js/jquery.tablesorter.widgets.js" />"></script>
	<script src="<c:url value="/js/moment.js" />"></script>
	<script src="<c:url value="/js/bootbox.min.js" />"></script>
		
	<script type="text/javascript">
		$(function() {
			
			/* var ds = "${sendRecive.brDate}";
			//Thu May 11 10:43:33 ICT 2017
			var ds1 = ds.split(' ');
			var day = ds1[2];
			var month = ds1[1];
			var year = ds1[5];
			console.log(day);
			console.log(month);
			console.log(year);
			console.log(new Date(Date.parse(month + ' ' + day + ', ' + year))); */
			
			
			var mode = "${mode}";
			var disable = "${disable}";
			var brTo = "${brTo}";
			var role = "${role}";
			if(brTo != null){
				var arrBrTo = brTo.split(',');
				$("#brToDepartment option").each(function(i, item){
					if(arrBrTo.indexOf(item.value) > -1){
						$(item).attr('selected', 'selected');	
					}
				});	
				$("#brToGroup option").each(function(i, item){
					if(arrBrTo.indexOf(item.value) > -1){
						$(item).attr('selected', 'selected');	
					}
				});	
				$("#brToSection option").each(function(i, item){
					if(arrBrTo.indexOf(item.value) > -1){
						$(item).attr('selected', 'selected');	
					}
				});	
				$("#brToUser option").each(function(i, item){
					if(arrBrTo.indexOf(item.value) > -1){
						$(item).attr('selected', 'selected');	
					}
				});	
			}
			if(role == 'USER'){
				if($('body').find('#brStatus1').length > -1){
					var status = "${sendRecive.brStatus}";
					if(status == 'Y'){
						$('#brStatus1').attr('checked', true).attr('disabled', 'disabled');	
					}
				}
			}else if(role == 'GROUP'){
				var strGroup = "${brToGroup}";
				var sectionTmp = [];

				var brToSection = "${brToSection}";
				getUser(strGroup, brToSection, "${brToUser}");
				getSectionEditMode(strGroup, brToSection)
				/*$.ajax({
			        url: GetSiteRoot() + "/getSectionSelectedByAdmin",
			        type: "POST",
			        cache: false,
			        dataType : "json",
			        data: { 'groups': strGroup },
			        success: function (response) {
			        	console.log(response);
        				var s = '';
			        	for(var x in response){
			        		s += '<optgroup label="' + x + '">';
			        		for(var y in response[x]){
			        			var t = response[x][y].split('xx#xx');
			        			s += '<option value="' + t[0] + '">' + t[1] + '</option>'; 
			        		}
			        		s += '</optgroup>';			        	  
			        	}
			        	var strs = '';
				        if(s != ''){
				        	strs = '<select class="form-control selectpicker" id="brToSection" name="brToSection" multiple="true">';
				        	strs += s;
				        	strs += '</select>';
				        	$('#admin-section').html(strs);
				        	$('#brToSection').selectpicker('refresh');
				        	$('#brToSection').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {
				        		resetUserSelect();
				        		if(sectionTmp.hasOwnProperty('x' + clickedIndex)){
   									delete sectionTmp['x' + clickedIndex];
   								}else{
   									var b = $("#brToSection option").eq(clickedIndex).val();
   									var af = b.split('xx##xx');
   									sectionTmp['x' + clickedIndex] = af[1];		
   								}
   								var str = '';
   								for(var x in sectionTmp){
   									str += "'" + sectionTmp[x] + "',";
   								}
   								if(str.length > 0){
   									str = str.substring(0, str.length - 1);
   								}
   								getUser(strGroup, str);
				        	});
			        	}
			        }
			    });*/
			}else if(role == 'ADMIN'){
				var departmentTmp = [];
				var groupTmp = [];
				var sectionTmp = [];
				$('#brToDepartment').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {
					resetGroupSelect();
					resetSectionSelect();
					resetUserSelect();
					if(departmentTmp.hasOwnProperty('x' + clickedIndex)){
						delete departmentTmp['x' + clickedIndex];
					}else{
						departmentTmp['x' + clickedIndex] = $("#brToDepartment option").eq(clickedIndex).val();		
					}
					var str = '';
					for(var x in departmentTmp){
						str += "'" + departmentTmp[x] + "',";
					}
					if(str.length > 0){
						str = str.substring(0, str.length - 1);
					}else{
						str = "'" + "'";
					}
					$.ajax({
				        url: GetSiteRoot() + "/getGroupSelectedByAdmin",
				        type: "POST",
				        cache: false,
				        dataType : "json",
				        data: { 'departments': str },
				        success: function (response) {
				        	var s = '';
				        	for(var x in response){
				        		s += '<optgroup label="' + x + '">';
				        		for(var y in response[x]){
				        			var t = response[x][y].split('xx#xx');
				        			s += '<option value="' + t[0] + '">' + t[1] + '</option>'; 
				        		}
				        		s += '</optgroup>';			        	  
				        	}
				        	var strs = '';
			        		strs = '<select class="form-control selectpicker" id="brToGroup" name="brToGroup" multiple="true">';
			        		strs += s;
			        		strs += '</select>';
			        		$('#admin-group').html(strs);
			        		strs = '<select class="form-control selectpicker" id="brToUser" name="brToUser" multiple="true">';
			        		strs += '</select>';
			        		$('#admin-user').html(strs);
			        		$('#brToGroup').selectpicker('refresh');
			        		$('#brToUser').selectpicker('refresh');
			        		$('#brToGroup').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {
			        			resetSectionSelect();
			        			resetUserSelect();
			        			if(groupTmp.hasOwnProperty('x' + clickedIndex)){
   									delete groupTmp['x' + clickedIndex];
   								}else{
   									var b = $("#brToGroup option").eq(clickedIndex).val();
   									var af = b.split('xx##xx');
   									groupTmp['x' + clickedIndex] = af[1];		
   								}
   								var str = '';
   								for(var x in groupTmp){
   									str += "'" + groupTmp[x] + "',";
   								}
   								if(str.length > 0){
   									str = str.substring(0, str.length - 1);
   								}else{
   									str = "'" + "'";
   								}
   								var strGroup = str;
   								getUser(strGroup);
   								$.ajax({
   							        url: GetSiteRoot() + "/getSectionSelectedByAdmin",
   							        type: "POST",
   							        cache: false,
   							        dataType : "json",
   							        data: { 'groups': strGroup },
   							        success: function (response) {
					        			var s = '';
  							        	for(var x in response){
  							        		s += '<optgroup label="' + x + '">';
  							        		for(var y in response[x]){
  							        			var t = response[x][y].split('xx#xx');
  							        			s += '<option value="' + t[0] + '">' + t[1] + '</option>'; 
  							        		}
  							        		s += '</optgroup>';			        	  
  							        	}
  							        	var strs = '';
  	   							        if(s != ''){
  	   							        	strs = '<select class="form-control selectpicker" id="brToSection" name="brToSection" multiple="true">';
  	   							        	strs += s;
  	   							        	strs += '</select>';
  	   							        	$('#admin-section').html(strs);
  	   							        	$('#brToSection').selectpicker('refresh');
   	   							        	$('#brToSection').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {
   	   							        		resetUserSelect();
   	   							        		if(sectionTmp.hasOwnProperty('x' + clickedIndex)){
	   	   		   									delete sectionTmp['x' + clickedIndex];
	   	   		   								}else{
	   	   		   									var b = $("#brToSection option").eq(clickedIndex).val();
	   	   		   									var af = b.split('xx##xx');
	   	   		   									sectionTmp['x' + clickedIndex] = af[1];		
	   	   		   								}
	   	   		   								var str = '';
	   	   		   								for(var x in sectionTmp){
	   	   		   									str += "'" + sectionTmp[x] + "',";
	   	   		   								}
	   	   		   								if(str.length > 0){
	   	   		   									str = str.substring(0, str.length - 1);
	   	   		   								}
	   	   		   								getUser(strGroup, str);
   	   							        	});
  							        	}
   							        }
   							    });
   							});
				        }
				    });
				});
				
				
				if(brTo != null && brTo != ''){
					
					var brToDepartment = "${sendRecive.brToDepartment}";
					if(brToDepartment != '' && brToDepartment != null){
						var option = $('#brToDepartment').find('option');
						var d = brToDepartment.split(',');
						for(var x = 0; x < option.length; x++){
							if(d.indexOf($(option[x]).val()) > -1){
								departmentTmp['x' + x] = $(option[x]).val();
							}
						}
					}
					
					var str = '';
					for(var x in departmentTmp){
						str += "'" + departmentTmp[x] + "',";
					}
					if(str.length > 0){
						str = str.substring(0, str.length - 1);
					}else{
						str = "'" + "'";
					}
					$.ajax({
				        url: GetSiteRoot() + "/getGroupSelectedByAdmin",
				        type: "POST",
				        cache: false,
				        dataType : "json",
				        data: { 'departments': str },
				        success: function (response) {
				        	var s = '';
				        	for(var x in response){
				        		s += '<optgroup label="' + x + '">';
				        		for(var y in response[x]){
				        			var t = response[x][y].split('xx#xx');
				        			var o = t[0].split('xx##xx');
					
				        			var brToGroup = "${brToGroup}";
									if(brToGroup != '' && brToGroup != null){
										brToGroup = brToGroup.split(',');
									}	
				        			if(brToGroup.indexOf(o[1]) > -1){
				        				s += '<option value="' + t[0] + '" selected>' + t[1] + '</option>'; 	
				        			}else{
				        				s += '<option value="' + t[0] + '">' + t[1] + '</option>'; 
				        			}
				        			
				        		}
				        		s += '</optgroup>';			        	  
				        	}
				        	var strs = '';
			        		strs = '<select class="form-control selectpicker" id="brToGroup" name="brToGroup" multiple="true">';
			        		strs += s;
			        		strs += '</select>';
			        		$('#admin-group').html(strs);
			        		
			        		strs = '<select class="form-control selectpicker" id="brToUser" name="brToUser" multiple="true">';
			        		strs += '</select>';
			        		$('#admin-user').html(strs);
			        		$('#brToGroup').selectpicker('refresh');
			        		$('#brToUser').selectpicker('refresh');
			        		$('#brToSection').selectpicker('refresh');
			        		$('#brToGroup').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {
			        			resetSectionSelect();
			        			resetUserSelect();
   								if(groupTmp.hasOwnProperty('x' + clickedIndex)){
   									delete groupTmp['x' + clickedIndex];
   								}else{
   									var b = $("#brToGroup option").eq(clickedIndex).val();
   									var af = b.split('xx##xx');
   									groupTmp['x' + clickedIndex] = af[1];		
   								}
   								var str = '';
   								for(var x in groupTmp){
   									str += "'" + groupTmp[x] + "',";
   								}
   								if(str.length > 0){
   									str = str.substring(0, str.length - 1);
   								}else{
   									str = "'" + "'";
   								}
   								/*var brToUser = "${brToUser}";
   								var brToSection = "${brToSection}";
   								var brToGroup = "${brToGroup}";
   								getSectionEditMode(brToGroup, brToSection);
   								getUser(brToGroup, brToSection, brToUser);
   								*/
   								var strGroup = str;
   								getUser(strGroup);
   								$.ajax({
   							        url: GetSiteRoot() + "/getSectionSelectedByAdmin",
   							        type: "POST",
   							        cache: false,
   							        dataType : "json",
   							        data: { 'groups': strGroup },
   							        success: function (response) {
					        			var s = '';
  							        	for(var x in response){
  							        		s += '<optgroup label="' + x + '">';
  							        		for(var y in response[x]){
  							        			var t = response[x][y].split('xx#xx');
  							        			s += '<option value="' + t[0] + '">' + t[1] + '</option>'; 
  							        		}
  							        		s += '</optgroup>';			        	  
  							        	}
  							        	var strs = '';
  	   							        if(s != ''){
  	   							        	strs = '<select class="form-control selectpicker" id="brToSection" name="brToSection" multiple="true">';
  	   							        	strs += s;
  	   							        	strs += '</select>';
  	   							        	$('#admin-section').html(strs);
  	   							        	$('#brToSection').selectpicker('refresh');
   	   							        	$('#brToSection').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {
   	   							        		resetUserSelect();
   	   							        		if(sectionTmp.hasOwnProperty('x' + clickedIndex)){
	   	   		   									delete sectionTmp['x' + clickedIndex];
	   	   		   								}else{
	   	   		   									var b = $("#brToSection option").eq(clickedIndex).val();
	   	   		   									var af = b.split('xx##xx');
	   	   		   									sectionTmp['x' + clickedIndex] = af[1];		
	   	   		   								}
	   	   		   								var str = '';
	   	   		   								for(var x in sectionTmp){
	   	   		   									str += "'" + sectionTmp[x] + "',";
	   	   		   								}
	   	   		   								if(str.length > 0){
	   	   		   									str = str.substring(0, str.length - 1);
	   	   		   								}
	   	   		   								getUser(strGroup, str);
   	   							        	});
  							        	}
   							        }
   							    });
   							});
			        		
							var str = '';
							for(var x in brToGroup){
								str += "'" + brToGroup[x] + "',";
							}
							if(str.length > 0){
								str = str.substring(0, str.length - 1);
							}else{
								str = "'" + "'";
							}
							//var brToUser = "${brToUser}";
							//var brToSection = "${brToSection}";
							//var brToGroup = "${brToGroup}";
							//brToUser = brToUser.split(',');
							//getSectionEditMode(brToGroup, brToSection);
							//getUser(str, brToSection, brToUser);
							var brToUser = "${brToUser}";
							var brToSection = "${brToSection}";
							var brToGroup = "${brToGroup}";
							getSectionEditMode(brToGroup, brToSection);
							getUser(brToGroup, brToSection, brToUser);
				        }
					});
		        }	    
			}
			
			if(disable){
				$('#upload_form').hide();
			}
			if(mode == 'edit'){
				$('#brRdate').removeAttr('readonly');
				$('#datetimepicker1').datepicker({
					/* language : 'th',
					format : "dd/mm/yyyy",
					startDate : new Date(),*/
					autoclose : true,
					todayHighlight : true,
					disabled : true 
				});
			}else{
				 $('#datetimepicker1').datepicker({
					/* language : 'th',
					format : "dd/mm/yyyy",
					startDate : new Date(),*/
					autoclose : true,
					todayHighlight : true,
					disabled : true 
				}).on('click', function() {
					if (!$('#date_1').is(':checked')) {
						$(this).datepicker('hide');
					}
				}); 
			}
			 $('#datetimepicker2').datepicker({
				/* language : 'th',
				format : "dd/mm/yyyy",
				startDate : new Date(), */
				autoclose : true,
				todayHighlight : true,
				disabled : true
			}); 
	
			var flagType = 'IN';
			
			$("#uploader").pluploadQueue({
		        // General settings
		        runtimes : 'html5,flash,silverlight,html4',
		        url : '<c:url value="/internal/upload/?id=${sendRecive.brId}"/>',
		        chunk_size : '15mb',
		        rename : true,
		        dragdrop: false,
		        flagType: 'IN',
		        //multi_selection: false,
		        filters : {
		            // Maximum file size
		            max_file_size : '15mb',
		            // Specify what files to browse for
		            mime_types: [
		                {title : "ไฟล์รูป", extensions : "jpg,gif,png,jpeg"},
		                {title : "ไฟล์เอกสาร", extensions : "pdf,csv,xlsx,xls,zip,rar,txt"}
		            ]
		        },
		 
		        // Resize images on clientside if we can
		        resize: {
		            width : 200,
		            height : 200,
		            quality : 90,
		            crop: true // crop to exact dimensions
		        },
		 
		 		flash_swf_url : '<c:url value="/js/Moxie.swf" />',
				silverlight_xap_url : '<c:url value="/js/Moxie.xap" />',
				
				init : {
					FileUploaded: function(up, file, info) {
		                var attachmentId = info.response;
		                var attachmentIdList = $("#attachmentIdList").val() + (($("#attachmentIdList").val() != '') ? ',' : '') + attachmentId;
		                $("#attachmentIdList").val(attachmentIdList);
		            },
		           UploadComplete: function(up, files) {
		        	   var dialog = bootbox.alert({
							message: "บันทึกข้อมูลสำเร็จ",
						    size: 'small'
						});
		        	   if('${mode}' == 'add') {
		        		   dialog.on('hidden.bs.modal', function () {
								window.location.href = GetSiteRoot() + '/addinternal';
							});	
		        	   }else{
		        		   dialog.on('hidden.bs.modal', function () {
								location.reload(true);
							});
		        	   }
		        	   setTimeout(function(){
							dialog.modal('hide');
						}, 1000);
		            },
		           FilesAdded: function(up, files) {
		        	   plupload.each(files, function(file) {
		        		   fileCnt = fileCnt + 1;
		                });
		           },
		           FilesRemoved: function(up, files) {
		        	   plupload.each(files, function(file) {
		        		   fileCnt = fileCnt - 1;
		                });
		            }
				}
		    });
			
			 $("#myTable").tablesorter({
			      theme: 'blue',
			      widthFixed: true,
			      sortLocaleCompare: true, // needed for accented characters in the data
			      sortList: [],
			      widgets: ['zebra']
			 });
			 
			 $(".btn-del-attachment").click(function(){
				var attachmentId = $(this).attr("data-attachment-id");
				bootbox.confirm('คุณต้องการที่จะลบไฟล์ ใช่หรือไม่ ?', function(result) {
					if(result) {
						$.post(GetSiteRoot() + '/deleteFiles/', {id : attachmentId}, 
							function(data){
								$('.btn-del-attachment[data-attachment-id="' + attachmentId + '"]').closest('tr').remove();
							}
						);
					}
				});
			 });
			 function getUser(strGroup, sections, brToUser){
				 
				 if(typeof sections == 'undefined'){
					 sections = null;
				 }
				 if(typeof brToUser == 'undefined'){
					 brToUser = null;
				 }
				 
				 $.ajax({
			        url: GetSiteRoot() + "/getUserSelectedByAdmin",
			        type: "POST",
			        cache: false,
			        dataType : "json",
			        data: { 'groups': strGroup, 'sections': sections },
			        success: function (response) {
			        	var s = '';
	        			if(brToUser != '' && brToUser != null){
	        				brToUser = brToUser.split(',');
						}console.log(brToUser);
			        	for(var x in response){
			        		//s += '<optgroup label="' + x + '">';
			        		//var subject;
			        		for(var y in response[x]){
			        			var t = response[x][y].split('xx#xx');
			        			if(brToUser != null && brToUser.indexOf(t[2]) > -1){
			        				if(t.length == 6){
				        				s += '<option value="' + t[0] + 'xx##xx' + t[1] + 'xx##xx' + t[2] + 'xx##xx' + t[3] + '" selected>' + t[5] + '</option>'; 
				        			}else if(t.length == 4){
				        				s += '<option value="' + t[0] + 'xx##xx' + t[1] + 'xx##xx' + t[2] + '" selected>' + t[3] + '</option>'; 
				        			}	
			        			}else{
			        				if(t.length == 6){
				        				s += '<option value="' + t[0] + 'xx##xx' + t[1] + 'xx##xx' + t[2] + 'xx##xx' + t[3] + '">' + t[5] + '</option>'; 
				        			}else if(t.length == 4){
				        				s += '<option value="' + t[0] + 'xx##xx' + t[1] + 'xx##xx' + t[2] + '">' + t[3] + '</option>'; 
				        			}
			        			}
			        		}
			        		s = '<optgroup label="">' +  s + '</optgroup>';			        	  
			        	}
			        	var strs = '';
			        	if(s != ''){
			        		strs = '<select class="form-control selectpicker" id="brToUser" name="brToUser" multiple="true">';
			        		strs += s;
			        		strs += '</select>';
			        		$('#admin-user').html(strs);
			        		$('#brToUser').selectpicker('refresh');
			        	}
			        }
			    });
			 }
			 
			 
			 function getSectionEditMode(strGroup, brToSection){
				 $.ajax({
			        url: GetSiteRoot() + "/getSectionSelectedByAdmin",
			        type: "POST",
			        cache: false,
			        dataType : "json",
			        data: { 'groups': strGroup },
			        success: function (response) {
	        			var s = '';
	        			if(brToSection != '' && brToSection != null){
							brToSection = brToSection.split(',');
						}
	        			console.log(brToSection);
			        	for(var x in response){

			        		s += '<optgroup label="' + x + '">';
			        		for(var x in response){
				        		s += '<optgroup label="' + x + '">';
				        		for(var y in response[x]){
				        			var t = response[x][y].split('xx#xx');
				        			var o = t[0].split('xx##xx');
				        			console.log(t);
									if(brToSection.indexOf(o[1]) > -1){
				        				s += '<option value="' + t[0] + '" selected>' + t[1] + '</option>'; 	
				        			}else{
				        				s += '<option value="' + t[0] + '">' + t[1] + '</option>'; 
				        			}
				        			
				        		}
				        		s += '</optgroup>';			        	  
				        	}
			        		s += '</optgroup>';			        	  
			        	}
			        	var strs = '';
					        if(s != ''){
					        	strs = '<select class="form-control selectpicker" id="brToSection" name="brToSection" multiple="true">';
					        	strs += s;
					        	strs += '</select>';
					        	$('#admin-section').html(strs);
					        	$('#brToSection').selectpicker('refresh');
					        	$('#brToSection').on('changed.bs.select', function (event, clickedIndex, newValue, oldValue) {
					        		resetUserSelect();
					        		if(sectionTmp.hasOwnProperty('x' + clickedIndex)){
	   									delete sectionTmp['x' + clickedIndex];
	   								}else{
	   									var b = $("#brToSection option").eq(clickedIndex).val();
	   									var af = b.split('xx##xx');
	   									sectionTmp['x' + clickedIndex] = af[1];		
	   								}
	   								var str = '';
	   								for(var x in sectionTmp){
	   									str += "'" + sectionTmp[x] + "',";
	   								}
	   								if(str.length > 0){
	   									str = str.substring(0, str.length - 1);
	   								}
	   								getUser(strGroup, str);
					        	});
			        	}
			        }
			    });
			 }
			 
			 function resetGroupSelect(){
				var strs = '<select class="form-control selectpicker" id="brToGroup" name="brToGroup" multiple="true">';
	        	strs += '</select>';
	        	$('#admin-group').html(strs);
        		$('#brToGroup').selectpicker('refresh');
			 }
			 
			 function resetSectionSelect(){
				var strs = '<select class="form-control selectpicker" id="brToSection" name="brToSection" multiple="true">';
	        	strs += '</select>';
	        	$('#admin-section').html(strs);
        		$('#brToSection').selectpicker('refresh');
			 }
			 
			 function resetUserSelect(){
				var strs = '<select class="form-control selectpicker" id="brToUser" name="brToUser" multiple="true">';
	        	strs += '</select>';
	        	$('#admin-user').html(strs);
        		$('#brToUser').selectpicker('refresh');
			 }
		});
	</script> 
	</jsp:body>
</t:master>
