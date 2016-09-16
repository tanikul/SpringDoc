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
				                    <div class="form-group">
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
										<label for="date">ช่วงเวลาในการค้นหา</label>
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
								    <label class="col-sm-3 control-label">หมายเหตุ</label>
								    <div class="col-sm-3">
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
								  <div class="form-group">
								  	<label for="year" class="col-sm-3 control-label">ค้นหาจากปี</label>
								    <div class="col-sm-3">
								    	<select class="form-control" id="year">
								    		<!-- <option value="">--ทั้งหมด--</option> -->
								    	</select>
								    </div>
								  </div>
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
												<th class="header">ปี</th>
												<th class="header">วันที่ส่งหนังสือ</th>
												<th class="header">เลขทะเบียนส่ง</th>
												<th class="header">ที่</th>
												<th class="header">ลงวันที่</th>
												<th class="header">จาก</th>
												<th class="header">ถึง</th>
												<th class="header">เรื่อง</th>
												<th class="header">สถานะ</th>
												<th class="header">ชั้นความลับ</th>
												<th class="header">หมายเหตุ</th>
												<th class="header">แก้ไข</th>
											</tr>
										</thead>
										<tbody>
											
										</tbody>
										<tfoot>
											<tr>
										      <th class="header" style="display:none;">1</th>
										      <th>ปี</th>
										      <th>วันที่ส่งหนังสือ</th>
										      <th>เลขทะเบียนส่ง</th>
										      <th>ที่</th>
										      <th>ลงวันที่</th>
										      <th>จาก</th>
										      <th>ถึง</th>
										      <th>เรื่อง</th>
										      <th>สถานะ</th>
										      <th>ชั้นความลับ</th>
										      <th>หมายเหตุ</th>
										      <th>แก้ไข</th>
										    </tr>
											<tr>
										      <td class="pager" colspan="12">
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
										      <th>ปี</th>
										      <th>วันที่ส่งหนังสือ</th>
										      <th>เลขทะเบียนส่ง</th>
										      <th>ที่</th>
										      <th>ลงวันที่</th>
										      <th>จาก</th>
										      <th>ถึง</th>
										      <th>เรื่อง</th>
										      <th>สถานะ</th>
										      <th>ชั้นความลับ</th>
										      <th>หมายเหตุ</th>
										      <th>แก้ไข</th>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
				<tfoot>
					<tr>
				      <th class="header" style="display:none;">1</th>
										      <th>ปี</th>
										      <th>วันที่ส่งหนังสือ</th>
										      <th>เลขทะเบียนส่ง</th>
										      <th>ที่</th>
										      <th>ลงวันที่</th>
										      <th>จาก</th>
										      <th>ถึง</th>
										      <th>เรื่อง</th>
										      <th>สถานะ</th>
										      <th>ชั้นความลับ</th>
										      <th>หมายเหตุ</th>
										      <th>แก้ไข</th>
				    </tr>
					<tr>
				      <td class="pager" colspan="12">
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
		<script src="<c:url value="/js/moment.js" />"></script>
		<script src="<c:url value="/js/bootstrap-datepicker.js" />"></script>
		<script src="<c:url value="/js/bootstrap-datepicker-thai.js" />"></script>
		<script src="<c:url value="/js/locale/bootstrap-datepicker.th.js" />"></script>
		<script src="<c:url value="/js/jquery.tablesorter.js" />"></script>
		<script src="<c:url value="/js/jquery.tablesorter.pager.js" />"></script>
		<script src="<c:url value="/js/jquery.tablesorter.widgets.js" />"></script>
		
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
