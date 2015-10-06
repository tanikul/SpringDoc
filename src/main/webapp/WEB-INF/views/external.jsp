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
			        <h2>เพิ่มรายการหนังสือส่ง - ออกภายนอก</h2>
						<div class="widget_inside">	
	      <springForm:form id="sendOutForm" action="" method="POST" commandName="sendOut" cssClass="form-horizontal">
	      <div class="col-md-7">
			  <div class="form-group" style="color: red">
			    <label class="col-sm-2 control-label">ขั้นความเร็ว</label>
			    <div class="col-sm-9">
			    <c:forEach var="item" items="${quick}" varStatus="loop">
			      <div class="radio-inline">
				    <label>
				    <c:choose>
				    	<c:when test="${loop.index == 0}">
				    		<springForm:radiobutton path="bsTypeQuick" value="${item.getId()}" checked="checked"/> ${item.getTypeQuick()}
				    	</c:when>
				    	<c:otherwise>
				    		<springForm:radiobutton path="bsTypeQuick" value="${item.getId()}" /> ${item.getTypeQuick()}
				    	</c:otherwise>
				    </c:choose>
				    </label>
				  </div>
				 </c:forEach>
			    </div>
			  </div>
			  
			  <div class="form-group">
			    <label class="col-sm-2 control-label">วันที่ส่งหนังสือ</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker1' data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="bsDate" cssClass="form-control"
													value="${date}" disabled="true" />
		                <span class="input-group-addon">
		                    <span class="glyphicon glyphicon-calendar"></span>
		                </span>
		            </div>
		            <small class="help-block" style="color:red" id="err-bsDate"></small>
			    </div>
			    <div class="col-sm-5">
			      <div class="checkbox">
				    <label>
				      <input type="checkbox" id="date_1" value="1"
													onclick="ex.checkChangeDate(this);"> กำหนดเอง
				    </label>
				  </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">หน่วยงานที่ส่ง</label>
			    <div class="col-sm-4">
			    	<springForm:select path="bsDivision" cssClass="form-control" onchange="ex.changeDivision(this);">
			    		<springForm:option value="" label="--- เลือกหน่วยงาน ---" />
			    		<springForm:options items="${divisions}" />
			    	</springForm:select>
			    	<small class="help-block" style="color:red" id="err-bsDivision"></small>
			    </div>
			  </div>
			  <div class="form-group">
				    <label class="col-sm-2 control-label">เลขทะเบียนส่ง</label>
				    <div class="col-sm-2 slash">
				    	<springForm:input path="bsId" cssClass="form-control" />
				    </div>
				    <div class="col-sm-1 slashs"><label class="label-control">/</label></div>
				    <div class="col-sm-2 slash">
				      <springForm:input path="bsNum" cssClass="form-control" />
				    </div>
				    <div class="col-sm-1 slashs"><label class="label-control">/</label></div>
				    <div class="col-sm-2 slash">				 
				      <springForm:input path="bsYear" cssClass="form-control" />
				    </div>
				    <!-- <div class="col-sm-2">
				      <div class="checkbox">
					    <label>
					      <input type="checkbox" onclick="ex.checkChangeRegis(this);"> กำหนดเอง
					    </label>
					  </div>
				    </div> -->
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">เลขที่หนังสือ</label>
			    <div class="col-sm-4">
			      <springForm:input path="bsPlace" cssClass="form-control" disabled="true"/>
			    </div>
			    <div class="col-sm-3">
			      <div class="checkbox">
				    <label>
				      <input type="checkbox" onclick="ex.checkbsPlace(this);"> กำหนดเอง
				    </label>
				  </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">ลงวันที่</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker2'  data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="bsRdate" cssClass="form-control" value="${date}" />
		                <span class="input-group-addon">
		                    <span class="glyphicon glyphicon-calendar"></span>
		                </span>
		            </div>
		            <small class="help-block" style="color:red" id="err-bsRDate"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">จาก</label>
			    <div class="col-sm-9">
			      <springForm:input path="bsFrom" cssClass="form-control"/>
			      <small class="help-block" style="color:red" id="err-bsFrom"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">ถึง</label>
			    <div class="col-sm-9">
			      <springForm:input path="bsTo" cssClass="form-control"/>
			      <small class="help-block" style="color:red" id="err-bsTo"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">เรื่อง</label>
			    <div class="col-sm-9">
			       <springForm:input path="bsSubject" cssClass="form-control"/>
			       <small class="help-block" style="color:red" id="err-bsSubject"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">หมายเหตุ</label>
			    <div class="col-sm-9">
			      <textarea class="form-control" id="bsRemark"></textarea>
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-2"></div>
			    <div class="col-sm-9">
			      <button class="btn btn-info" type="submit">บันทึก</button>
			      <a href="#" class="plupload_button plupload_start">Start Upload</a>
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
					    <c:choose>
						    <c:when test="${loop.index == 0}">
						    	<springForm:radiobutton path="bsTypeSecret" value="${item.getId()}" checked="checked"/> ${item.getTypeSecret()}
						    </c:when>
						    <c:otherwise>
						    	<springForm:radiobutton path="bsTypeSecret" value="${item.getId()}" /> ${item.getTypeSecret()}
						    </c:otherwise> 
					      </c:choose>
					    </label>
					  </div>
					  </c:forEach>
				    </div>
				</div>
				<div class="form-group">
					<div class="row">
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
	<link href="<c:url value="/css/bootstrap-datetimepicker.css" />" rel="stylesheet">
	<script src="<c:url value="/js/moment.js" />"></script>
	<script src="<c:url value="/js/bootstrap-datepicker.js" />"></script>
	<script src="<c:url value="/js/bootstrap-datepicker-thai.js" />"></script>
	<script src="<c:url value="/js/locale/bootstrap-datepicker.th.js" />"></script>
	<script src="<c:url value="/js/plupload.full.min.js" />"></script>
	<script src="<c:url value="/js/jquery.plupload.queue.js" />"></script>
	<link href="<c:url value="/css/jquery.ui.plupload.css" />" rel="stylesheet">
	<link href="<c:url value="/css/jquery.plupload.queue.css" />" rel="stylesheet">
	<script type="text/javascript">
		$(function() {
			ex.lastId = "${lastId}";
			$('#datetimepicker1').datepicker({
				/* language : 'th',
				format : "dd/mm/yyyy",
				startDate : new Date(), */
				autoclose : true,
				todayHighlight : true,
				disabled : true
			}).on('click', function() {
				if (!$('#date_1').is(':checked')) {
					$(this).datepicker('hide');
				}
			});
			$('#datetimepicker2').datepicker({
				/* language : 'th',
				format : "dd/mm/yyyy",
				startDate : new Date(), */
				autoclose : true,
				todayHighlight : true,
				disabled : true
			});
	
			var $form = $('#sendOutForm');
			$form.bind('submit', function(e) {
				// Ajax validation
				var $inputs = $form.find('input');
				var data = collectFormData($inputs);
				data['bsDivision'] = $('#bsDivision').val();
				data['bsRemark'] = $('#bsRemark').val();
				if(!ex.checkValidateSendOut(data)) return false;
				$.post('saveSendOut', data, function(response) {
					$form.find('.control-group').removeClass('error');
					$form.find('.help-block').empty();
					$form.find('.alert').remove();
					
					if (response.status == 'FAIL') {
						for (var i = 0; i < response.errorMessageList.length; i++) {
							var item = response.errorMessageList[i];
							var $controlGroup = $('#' + item.fieldName);
							$controlGroup.addClass('error');
							$controlGroup.find('.help-block').html(item.message);
						}
					} else {
						$form.unbind('submit');
						$form.submit();
					}
				}, 'json');
				
				e.preventDefault();
				return false;
			});
			
			$("#uploader").pluploadQueue({
		        // General settings
		        runtimes : 'html5,flash,silverlight,html4',
		        url : '<c:url value="/external/upload" />',
		         
		        chunk_size : '15mb',
		        rename : true,
		        dragdrop: false,
		         
		        filters : {
		            // Maximum file size
		            max_file_size : '15mb',
		            // Specify what files to browse for
		            mime_types: [
		                {title : "ไฟล์รูป", extensions : "jpg,gif,png,pdf,csv,xlsx,xls"},
		                {title : "ไฟล์เอกสาร", extensions : "pdf,csv,xlsx,xls,zip,rar"}
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
		    });
		});
	</script> 
	</jsp:body>
</t:master>
