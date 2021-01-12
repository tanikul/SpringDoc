<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<c:url value="/exportReport" var="downloadUrl"/>
<t:master>
	<jsp:body>
	<div class="modal fade" id="form-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	                <h4 class="modal-title"></h4>
	            </div>
	            <div class="modal-body">
	
	            </div>
	        </div>
	    </div>
	</div>
	<div id="msgbox" title="" style="display:none;"></div>
	   <div id="actualbody">
			<div class="row clearfix">
				<div class="col_12">
					<div class="widget clearfix">
			        <h2>ค้นหา</h2>
						<div class="widget_inside">				
							<div class="col-md-3">
			                    <div class="clearfix">
			                    <c:if test="${role ne 'ADMIN'}">
				                     <div class="form-group">
									  	<label for="year" class="col-sm-5 control-label">ค้นหาจากปี</label>
									    <div class="col-sm-7">
									    	<select class="form-control" id="year">
									    		<!-- <option value="">--ทั้งหมด--</option> -->
									    	</select>
									    </div>
									  </div>
								  </c:if>
				                    <div class="form-group">
				                    	<div <c:if test="${role ne 'ADMIN'}">style="display:none;"</c:if>>
					                    	<label>ประเภท</label>
					                        <div class="radio">
											  <label>
											    <input type="radio" id="type" name="type" value="1" <c:if test="${type eq '' or type eq 'IN'}">checked="checked"</c:if> onchange="SearchResult();"> หนังสือรับ - จากภายนอก
											  </label>
											</div>
											<div class="radio">
											  <label>
											    <input type="radio" id="type" name="type" value="2" <c:if test="${type eq 'OUT'}">checked="checked"</c:if> onchange="SearchResult();"> หนังสือส่ง - ออกภายนอก
											  </label>
											</div>
										</div>
										
										<label for="date">ค้นหาแบบช่วงเวลา</label>
								      	<div class='input-group date' id='datetimepicker6' data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
							                <input type='text' class="form-control" id="startDate"
												placeholder="วันที่เริ่มต้น" />
							                <span class="input-group-addon">
							                    <span class="glyphicon glyphicon-calendar"></span>
							                </span>
							            </div>
							            <label></label>
							            <div class='input-group date' id='datetimepicker7' data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
							                <input type='text' class="form-control" id="endDate"
												placeholder="วันที่สิ้นสุด" />
							                <span class="input-group-addon">
							                    <span class="glyphicon glyphicon-calendar"></span>
							                </span>
							            </div>
							           
									</div>
									
								</div>
								
							</div>
							<div class="col-md-9">
								<form class="form-horizontal" role="form">
								  <div class="form-group">
								    <label class="col-sm-3 control-label">ชื่อเรื่อง</label>
								    <div class="col-sm-9">
								      <input type="text" class="form-control" id="subject">
								    </div>
								  </div>
								  <div class="form-group">
								    <label class="col-sm-3 control-label">เลขที่หนังสือ</label>
								    <div class="col-sm-3">
								      <input type="text" class="form-control" id="num">
								    </div>
								    <label class="col-sm-3 control-label" style="display:none;">หมายเหตุ</label>
								    <div class="col-sm-3" style="display:none;">
								      <input type="text" class="form-control" id="remark">
								    </div>
								  </div>
								  <div class="form-group">
								    <label class="col-sm-3 control-label">จากหน่วยงาน/บุคคล</label>
								    <div class="col-sm-3">
								      <input type="text" class="form-control" id="from">
								    </div>
								    <label class="col-sm-3 control-label">ถึงหน่วยงาน/บุคคล</label>
								    <div class="col-sm-3">
								      <input type="text" class="form-control" id="to">
								    </div>
								  </div>
								  <c:if test="${role eq 'ADMIN'}">
									  <div class="form-group">
									  	<label for="year" class="col-sm-3 control-label">ค้นหาจากปี</label>
									    <div class="col-sm-3">
									    	<select class="form-control" id="year">
									    		<!-- <option value="">--ทั้งหมด--</option> -->
									    	</select>
									    </div>
									  </div> 
								  </c:if>
								</form>
								<div class="form-group">
									<div class="col-sm-3"></div>
									<div class="col-sm-3">
										<input type="submit" class="button blue" value="ค้นหา" onclick="SearchResult();">
									</div>
								</div>
							</div>
						</div>
			        </div>
			    </div>
			</div>

			<div class="row clearfix">
				<div class="col_12">
					<div class="widget clearfix">
			        <h2>Report</h2>
						<div class="widget_inside">				
							<div class="report">
				            	<div class="col_12" id="table-display" style="display:none;">
									<div class="bnt-export">
										<input type="button" onclick="download(1);" value="ออกรายงาน (Pdf)" class="button blue">
										<input type="button" onclick="download(2);" value="ออกรายงาน (Excel)" class="button blue">
									</div>
									<table id="myTable" class="tablesorter">
										<thead>
											<tr>
												<th class="header" style="display:none;">1</th>
												<c:choose>
													<c:when test="${role == 'DEPARTMENT'}">
														<th class="header">ลำดับ</th>
													</c:when>
													<c:otherwise>
														<th class="header" style="display:none;">ลำดับ</th>
													</c:otherwise>
												</c:choose>
												<th class="header">ปี</th>
												<th class="header">วันที่ส่งหนังสือ</th>
												<th class="header" id="registerLabel_1">เลขทะเบียนรับ</th>
												<th class="header">ที่</th>
												<th class="header">ลงวันที่</th>
												<th class="header">จาก</th>
												<c:choose>
													<c:when test="${role == 'ADMIN'}">
														<th class="header">ถึงกอง</th>
													</c:when>
													<c:when test="${role == 'DEPARTMENT'}">
														<th class="header">ถึงฝ่าย</th>
													</c:when>
													<c:when test="${role == 'GROUP'}">
														<th class="header">ถึง</th>
													</c:when>
												</c:choose>
												<th class="header">เรื่อง</th>
												<th class="header">ขั้นความเร็ว</th>
												<th class="header">ชั้นความลับ</th>
												<th class="header">หมายเหตุ</th>
												<th class="header">สถานะ</th>
												<c:choose>
												  <c:when test="${role == 'ADMIN'}">
											      	<th style="text-align:center;">แก้ไข/ลบ</th>
											      </c:when>
											      <c:when test="${role == 'DEPARTMENT' || role == 'GROUP'}">
											      	<th class="header">แก้ไข</th>
											      </c:when>
											      <c:otherwise>
											      	<th class="header">ข้อมูล</th>
											      </c:otherwise>
										      </c:choose>
											</tr>
										</thead>
										<tbody>
											
										</tbody>
										<tfoot>
											<tr>
										      <th class="header" style="display:none;">1</th>
										      <c:choose>
												<c:when test="${role == 'DEPARTMENT'}">
													<th style="text-align:center;">ลำดับ</th>
												</c:when>
												<c:otherwise>
													<th class="header" style="display:none;">ลำดับ</th>
												</c:otherwise>
											  </c:choose>
										      <th style="text-align:center;">ปี</th>
										      <th style="text-align:center;">วันที่ส่งหนังสือ</th>
										      <th style="text-align:center;" id="registerLabel_2">เลขทะเบียนรับ</th>
										      <th style="text-align:center;">ที่</th>
										      <th style="text-align:center;">ลงวันที่</th>
										      <th style="text-align:center;">จาก</th>
										      <c:choose>
												<c:when test="${role == 'ADMIN'}">
													<th class="header">ถึงกอง</th>
												</c:when>
												<c:when test="${role == 'DEPARTMENT'}">
													<th class="header">ถึงฝ่าย</th>
												</c:when>
												<c:when test="${role == 'GROUP'}">
													<th class="header">ถึง</th>
												</c:when>
											  </c:choose>
										      <th style="text-align:center;">เรื่อง</th>
										      <th style="text-align:center;">ขั้นความเร็ว</th>
										      <th style="text-align:center;">ชั้นความลับ</th>
										      <th style="text-align:center;">หมายเหตุ</th>
										      <th style="text-align:center;">สถานะ</th>
										      <c:choose>
										      	  <c:when test="${role == 'ADMIN'}">
											      	<th style="text-align:center;">แก้ไข/ลบ</th>
											      </c:when>
											      <c:when test="${role == 'DEPARTMENT' || role == 'GROUP'}">
											      	<th style="text-align:center;">แก้ไข</th>
											      </c:when>
											      <c:otherwise>
											      	<th style="text-align:center;">ข้อมูล</th>
											      </c:otherwise>
										      </c:choose>
										    </tr>
											<tr>
										      <td class="pager" colspan="13">
										        <img src="<c:url value="/css/blue/icon/first.png" />" class="first"/>
										        <img src="<c:url value="/css/blue/icon/prev.png" />" class="prev"/>
										        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
										        <img src="<c:url value="/css/blue/icon/next.png" />" class="next"/>
										        <img src="<c:url value="/css/blue/icon/last.png" />" class="last"/>
										        <select class="pagesize">
										          	<option value="50">50</option>
										        	<option value="100">100</option>
										        	<option value="200">200</option>
										        </select>
										      </td>
										     <tr>
										</tfoot>
									</table>                
				            	</div>
							</div>
			            </div>
			        </div>
			    </div>
			</div>
		</div>
		<div id="hide" style="display:none;">
			<div class="bnt-export">
				<input type="button" onclick="download(1);" value="ออกรายงาน (Pdf)" class="button blue">
				<input type="button" onclick="download(2);" value="ออกรายงาน (Excel)" class="button blue">
			</div>
			<table class="tablesorter" id="search_tb">
				<thead>
					<tr>
					  <th class="header" style="display:none;">1</th>
					  <c:choose>
						<c:when test="${role == 'DEPARTMENT'}">
							<th style="text-align:center;">ลำดับ</th>
						</c:when>
						<c:otherwise>
							<th class="header" style="display:none;">ลำดับ</th>
						</c:otherwise>
					  </c:choose>
				      <th style="text-align:center;">ปี</th>
				      <th style="text-align:center;">วันที่ส่งหนังสือ</th>
				      <th style="text-align:center;" id="registerLabel_3">เลขทะเบียนรับ</th>
				      <th style="text-align:center;">ที่</th>
				      <th style="text-align:center;">ลงวันที่</th>
				      <th style="text-align:center;">จาก</th>
				      <c:choose>
						<c:when test="${role == 'ADMIN'}">
							<th style="text-align:center;">ถึงกอง</th>
						</c:when>
						<c:when test="${role == 'DEPARTMENT'}">
							<th style="text-align:center;">ถึงฝ่าย</th>
						</c:when>
						<c:when test="${role == 'GROUP'}">
							<th style="text-align:center;">ถึง</th>
						</c:when>
				      </c:choose>
				      <th style="text-align:center;">เรื่อง</th>
				      <th style="text-align:center;">ขั้นความเร็ว</th>
				      <th style="text-align:center;">ชั้นความลับ</th>
				      <th style="text-align:center;">หมายเหตุ</th>
				      <th style="text-align:center;">สถานะ</th>
				      <c:choose>
				      	  <c:when test="${role == 'ADMIN'}">
					      	<th style="text-align:center;">แก้ไข/ลบ</th>
					      </c:when>
					      <c:when test="${role == 'DEPARTMENT' || role == 'GROUP'}">
					      	<th style="text-align:center;">แก้ไข</th>
					      </c:when>
					      <c:otherwise>
					      	<th style="text-align:center;">ข้อมูล</th>
					      </c:otherwise>
				      </c:choose>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
				<tfoot>
					<tr>
				      <th class="header" style="display:none;">1</th>
				      <c:choose>
						<c:when test="${role == 'DEPARTMENT'}">
							<th style="text-align:center;">ลำดับ</th>
						</c:when>
						<c:otherwise>
							<th class="header" style="display:none;">ลำดับ</th>
						</c:otherwise>
					  </c:choose>
										      <th style="text-align:center;">ปี</th>
										      <th style="text-align:center;">วันที่ส่งหนังสือ</th>
										      <th style="text-align:center;" id="registerLabel_4">เลขทะเบียนรับ</th>
										      <th style="text-align:center;">ที่</th>
										      <th style="text-align:center;">ลงวันที่</th>
										      <th style="text-align:center;">จาก</th>
										      <c:choose>
												<c:when test="${role == 'ADMIN'}">
													<th style="text-align:center;">ถึงกอง</th>
												</c:when>
												<c:when test="${role == 'DEPARTMENT'}">
													<th style="text-align:center;">ถึงฝ่าย</th>
												</c:when>
												<c:when test="${role == 'GROUP'}">
													<th style="text-align:center;">ถึง</th>
												</c:when>
											  </c:choose>
										      <th style="text-align:center;">เรื่อง</th>
										      <th style="text-align:center;">ขั้นความเร็ว</th>
										      <th style="text-align:center;">ชั้นความลับ</th>
										      <th style="text-align:center;">หมายเหตุ</th>
										      <th style="text-align:center;">สถานะ</th>
										      <c:choose>
											      <c:when test="${role == 'ADMIN'}">
											      	<th style="text-align:center;">แก้ไข/ลบ</th>
											      </c:when>
											      <c:when test="${role == 'DEPARTMENT' || role == 'GROUP'}">
											      	<th style="text-align:center;">แก้ไข</th>
											      </c:when>
											      <c:otherwise>
											      	<th style="text-align:center;">ข้อมูล</th>
											      </c:otherwise>
										      </c:choose>
				    </tr>
					<tr>
				      <td class="pager" colspan="13">
				        <img src="<c:url value="/css/blue/icon/first.png" />" class="first"/>
				        <img src="<c:url value="/css/blue/icon/prev.png" />" class="prev"/>
				        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
				        <img src="<c:url value="/css/blue/icon/next.png" />" class="next"/>
				        <img src="<c:url value="/css/blue/icon/last.png" />" class="last"/>
				        <select class="pagesize">
				          <option value="50">50</option>
						  <option value="100">100</option>
						  <option value="200">200</option>
				        </select>
				      </td>
				     <tr>
				</tfoot>
			</table>   
		</div>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" id="_csrf"/>
		<link href="<c:url value="/css/blue/style.css" />" rel="stylesheet">
		<link href="<c:url value="/css/bootstrap-datetimepicker.css" />" rel="stylesheet">
		<link href="<c:url value="/css/tooltipster.bundle.min.css" />" rel="stylesheet">
		<script src="<c:url value="/js/moment.js" />"></script>
		<script src="<c:url value="/js/bootstrap-datepicker.js" />"></script>
		<script src="<c:url value="/js/bootstrap-datepicker-thai.js" />"></script>
		<script src="<c:url value="/js/locale/bootstrap-datepicker.th.js" />"></script>
		<script src="<c:url value="/js/jquery.tablesorter.js" />"></script>
		<script src="<c:url value="/js/jquery.tablesorter.pager.js" />"></script>
		<script src="<c:url value="/js/jquery.tablesorter.widgets.js" />"></script>
		<script src="<c:url value="/js/tooltipster.bundle.min.js" />"></script>
		
		<script type="text/javascript">
			$(function() {
				$("#loading").show();
				$('#datetimepicker6').datepicker({
					/* language : 'th',
					format : "dd/mm/yyyy", */
					autoclose : true,
					todayHighlight : true,
					disabled : true
				});
				$('#datetimepicker7').datepicker({
					/* language : 'th',
					format : "dd/mm/yyyy", */
					autoclose : true,
					todayHighlight : true,
					enableOnReadonly : false
				});
				/*$("#datetimepicker6").on("dp.change", function(e) {
					$('#datetimepicker7').data("DateTimePicker").minDate(e.date);
				});
				$("#datetimepicker7").on("dp.change", function(e) {
					$('#datetimepicker6').data("DateTimePicker").maxDate(e.date);
				});*/
				//$("#myTable").tablesorter({sortList:[[0,0],[2,1]], widgets: ['zebra']});
				loadTable();
			  	$('#year').change(function(){
					 if($(this).val() != '') {
						 $('#startDate').val('');
						 $('#endDate').val('');
					 }
				});
				  
				$('#startDate').change(function(){
					if($(this).val() != '') {
						$('#year').val('');
					}
				});
				
				$('#endDate').change(function(){
					if($(this).val() != '') {
						$('#year').val('');
					}
				});
			});

			function download(id) {
				var type = ($('input[name=type]:checked').val() == 1) ? 'IN' : 'OUT';
				if(id == 1){
					window.open('${downloadUrl}?type=' + type);
				}else{
				
				 	var form = document.createElement("form"); //created dummy form for submitting.
				    var element1 = document.createElement("input"); 
				    form.method = "POST";
				    form.action = (type == 'IN') ? '${pageContext.servletContext.contextPath}/BOOK_RECIEVE_OUT' : '${pageContext.servletContext.contextPath}/BOOK_SEND_OUT';
				
				    element1.value = type; //its a json string I need to pass to server.
				    element1.name = "type";
				    element1.type = 'hidden'
				    form.appendChild(element1);
				
				    document.body.appendChild(form);
				
				    form.submit();
    				form.remove();
				}
			}
		</script>
	</jsp:body>
</t:master>
