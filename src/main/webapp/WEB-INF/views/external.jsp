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
					<c:if test="${mode == 'add'}"><h2>เพิ่มรายการหนังสือส่ง - ออกภายนอก</h2></c:if>
			        <c:if test="${mode == 'edit'}"><h2>แก้ไขรายการหนังสือส่ง - ออกภายนอก</h2></c:if>
						<div class="widget_inside">	
	      <springForm:form id="sendOutForm" action="" method="POST" commandName="sendOut" cssClass="form-horizontal">
	      	<input type="hidden" id="attachmentIdList" name="attachmentIdList" value=""/>
	      <div class="col-md-7">
			  <div class="form-group" style="color: red">
			    <label class="col-sm-2 control-label">ขั้นความเร็ว</label>
			    <div class="col-sm-9">
			    <c:forEach var="item" items="${quick}" varStatus="loop">
			      <div class="radio-inline">
				    <label>
				    	<springForm:radiobutton path="bsTypeQuick" value="${item.getId()}" disabled="${disable}"/> ${item.getTypeQuick()}
				    </label>
				  </div>
				 </c:forEach>
			    </div>
			  </div>
			  
			  <div class="form-group">
			    <label class="col-sm-2 control-label">วันที่ส่งหนังสือ</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker1' data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="bsRdate" cssClass="form-control" readonly="true" disabled="${disable}"/>
		                <span class="input-group-addon">
		                    <span class="glyphicon glyphicon-calendar"></span>
		                </span>
		            </div>
		            <small class="help-block" style="color:red" id="err-bsDate"></small>
			    </div>
			    <div class="col-sm-5">
			      <div class="checkbox">
				    <label>
				      <c:if test="${mode == 'add'}"><input type="checkbox" id="date_1" value="1" onclick="ex.checkChangeDate(this);"> กำหนดเอง</c:if>
				    </label>
				  </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">หน่วยงานที่ส่ง</label>
			    <div class="col-sm-4">
			    	<springForm:select path="bsDivision" cssClass="form-control" onchange="ex.changeDivision(this);" disabled="${disable}">
			    		<springForm:option value="" label="--- เลือกหน่วยงาน ---" />
			    		<springForm:options items="${divisions}" />
			    	</springForm:select>
			    	<small class="help-block" style="color:red" id="err-bsDivision"></small>
			    </div>
			  </div>
			  <div class="form-group">
				    <label class="col-sm-2 control-label">เลขทะเบียนส่ง</label>
				    <%-- <div class="col-sm-2 slash">
				    	<springForm:hidden path="bsId" cssClass="form-control" />
				    </div>
				    <div class="col-sm-1 slashs"><label class="label-control">/</label></div> --%>
				    <div class="col-sm-2 slash">
				      <springForm:hidden path="bsId" cssClass="form-control" />
				      <springForm:input path="bsNum" cssClass="form-control" disabled="${disable}"/>
				    </div>
				    <div class="col-sm-1 slashs"><label class="label-control">/</label></div>
				    <div class="col-sm-2 slash">				 
				      <springForm:input path="bsYear" cssClass="form-control" disabled="${disable}"/>
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
			      <springForm:input path="bsPlace" cssClass="form-control" readonly="true" disabled="${disable}"/>
			    </div>
			    <div class="col-sm-3">
			      <div class="checkbox">
				    <label>
				      <c:if test="${mode == 'add'}"><input type="checkbox" onclick="ex.checkbsPlace(this);"> กำหนดเอง</c:if>
				    </label>
				  </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">ลงวันที่</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker2'  data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="bsDate" cssClass="form-control" disabled="${disable}"/>
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
			      <springForm:input path="bsFrom" cssClass="form-control" disabled="${disable}"/>
			      <small class="help-block" style="color:red" id="err-bsFrom"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">ถึง</label>
			    <div class="col-sm-9">
			      <springForm:input path="bsTo" cssClass="form-control" disabled="${disable}"/>
			      <small class="help-block" style="color:red" id="err-bsTo"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">เรื่อง</label>
			    <div class="col-sm-9">
			       <springForm:input path="bsSubject" cssClass="form-control" disabled="${disable}"/>
			       <small class="help-block" style="color:red" id="err-bsSubject"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-2 control-label">หมายเหตุ</label>
			    <div class="col-sm-9">
			      <%-- <textarea class="form-control" id="bsRemark"></textarea> --%>
			      <springForm:textarea path="bsRemark" cssClass="form-control" disabled="${disable}"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-2"></div>
			    <div class="col-sm-9">
			    	<c:choose>
			    		<c:when test="${disable == 'true'}">
			    			<button class="btn btn-info" type="submit" id="plupload_start" disabled="${disable}">บันทึก</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button class="btn btn-info" type="submit" id="plupload_start">บันทึก</button>
			    		</c:otherwise>
			    	</c:choose>
			      
			      <!-- <a href="#" class="plupload_button plupload_start">Start Upload</a>-->
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
						    	<springForm:radiobutton path="bsTypeSecret" value="${item.getId()}" checked="checked" disabled="${disable}"/> ${item.getTypeSecret()}
						    </c:when>
						    <c:otherwise>
						    	<springForm:radiobutton path="bsTypeSecret" value="${item.getId()}" disabled="${disable}"/> ${item.getTypeSecret()}
						    </c:otherwise> 
					      </c:choose>
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
				<div class="form-group">
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
	
	<script src="<c:url value="/plugins/plupload-2.1.8/js/plupload.full.min.js" />"></script>
	<script src="<c:url value="/plugins/plupload-2.1.8/js/jquery.plupload.queue/jquery.plupload.queue.js" />"></script>
	<link href="<c:url value="/plugins/plupload-2.1.8/js/jquery.plupload.queue/css/jquery.plupload.queue.css" />" rel="stylesheet">
	
	<script src="<c:url value="/js/jquery.tablesorter.js" />"></script>
	<script src="<c:url value="/js/jquery.tablesorter.pager.js" />"></script>
	<script src="<c:url value="/js/jquery.tablesorter.widgets.js" />"></script>
		
	<script src="<c:url value="/js/bootbox.min.js" />"></script>
	
	<script type="text/javascript">
		$(function() {
			ex.lastId = "${lastId}";
			var mode = "${mode}";
			if(mode == 'edit'){
				$('#bsPlace').removeAttr('readonly');
				$('#bsRdate').removeAttr('readonly');
				$('#datetimepicker1').datepicker({
					/* language : 'th',
					format : "dd/mm/yyyy",
					startDate : new Date(), */
					autoclose : true,
					todayHighlight : true,
					disabled : true
				});
			}else{
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
			}
			$('#datetimepicker2').datepicker({
				/* language : 'th',
				format : "dd/mm/yyyy",
				startDate : new Date(), */
				autoclose : true,
				todayHighlight : true,
				disabled : true
			});
			
			
			$("#uploader").pluploadQueue({
		        // General settings
		        runtimes : 'html5,flash,silverlight,html4',
		        url : '<c:url value="/external/upload/?id=${sendOut.bsId}"/>',
		        chunk_size : '15mb',
		        rename : true,
		        dragdrop: false,
		        flagType: 'OUT', 
		        multi_selection: false,
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
		                // Called when file has finished uploading
		                console.log(info.response);
		                var attachmentId = info.response;
		                var attachmentIdList = $("#attachmentIdList").val() + (($("#attachmentIdList").val() != '') ? ',' : '') + attachmentId;
		                $("#attachmentIdList").val(attachmentIdList);
		                console.log(attachmentIdList);
		            },
		            UploadComplete: function(up, files) {
		        	   if(fileCnt > 0) location.reload(true);
		            },
		            FilesAdded: function(up, files) {
		        	   fileCnt = fileCnt + 1;
		           },
		           FilesRemoved: function(up, files) {
		        	   fileCnt = fileCnt - 1;
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
								console.log(data);
								$('.btn-del-attachment[data-attachment-id="' + attachmentId + '"]').closest('tr').remove();
							}
						);
					}
				});
			 });
		});
	</script> 
	</jsp:body>
</t:master>
