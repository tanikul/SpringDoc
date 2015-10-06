<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@page session="true"%>
<t:master>
	<jsp:body>
	<script src="<c:url value="/js/jquery.dataTables.js" />"></script>
	<script src="<c:url value="/js/fnReloadAjax.js" />"></script>
	<link href="<c:url value="/css/dataTables.css" />" rel="stylesheet">
		<div id="actualbody">
			<div class="row clearfix">
				<div class="col_12">
					<div class="widget clearfix">
			        <h2>ค้นหา</h2>
						<div class="widget_inside">	
							<springForm:form id="userTable" action="" method="POST" commandName="users" cssClass="form-horizontal">			
								<c:if test="${pageContext.request.isUserInRole('USER')}">
									<div class="col-md-3">
					                    <div class="clearfix">
						                   <div class="form-group">
						                    	<label>หน่วยงาน</label>
						                    	<springForm:select path="role" cssClass="form-control" onchange="ex.changeDivision(this);">
										    		<springForm:option value="" label="--- เลือกหน่วยงาน ---" />
										    		<springForm:options items="${divisions}" />
										    	</springForm:select>            	
						                    </div>
										</div>
									</div>
									<div class="col-md-3">
										<label>ชื่อ</label>
										<input type="text" class="form-control">
									</div>
									<div class="col-md-3">
										<label>นามสกุล</label>
										<input type="text" class="form-control">
									</div>
									<div class="col-md-3">
										<div style="margin-top:22px;">
											<input type="submit" class="button blue" value="ค้นหา" onclick="SearchResult();" />
										</div>
									</div>
								</c:if>
								<c:if test="${pageContext.request.isUserInRole('ADMIN')}">
									<div class="row">
										<div class="pull-right" style="margin-right:25px;">
											<springForm:button type="button" onclick="addUser();" class="btn btn-primary"><span class="glyphicon glyphicon-user"></span> เพิ่มผู้ใช้งาน</springForm:button>
										</div>
									</div>
									
									<table id="example" class="display" cellspacing="0" width="100%">
								        <thead>
								            <tr>
								             	<th>ลำดับ</th>
								                <th>ชื่อ</th>
								                <th>นามสกุล</th>
								                <th>แผนก</th>
								                <th>สังกัดหน่วยงาน</th>
								                <th>แก้ไข / ลบ</th>
								            </tr>
								        </thead>
								 
								    </table>
							    </c:if>
							</springForm:form>
						</div>
			        </div>
			    </div>
			</div>
		</div>

		<c:if test="${pageContext.request.isUserInRole('ADMIN')}">
			<script type="text/javascript" charset="utf-8">
				$(document).ready(function() {
				    table = $('#example').DataTable( {
				    	ajax: {
			                url: 'LoadAccount',
			                type: 'POST',
			            },
						"columns": [
							{ "data" : "seq" },
				        	{ "data" : "fname" },
				        	{ "data" : "lname" },
				        	{ "data" : "divisionName" },
				        	{ "data" : "organization" },
				        	{ "data" : "button" }
				        ]
				    } );
				} );
			</script>
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
		</c:if>
	</jsp:body>
</t:master>