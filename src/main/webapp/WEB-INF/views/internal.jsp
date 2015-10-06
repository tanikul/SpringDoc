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
			        <h2>เพิ่มรายการหนังสือรับ - ออกภายนอก</h2>
						<div class="widget_inside">	
	      <springForm:form id="sendReciveForm" action="" method="POST" commandName="sendRecive" cssClass="form-horizontal">
	      <div class="col-md-7">
			  <div class="form-group" style="color: red">
			    <label class="col-sm-3 control-label">ขั้นความเร็ว</label>
			    <div class="col-sm-9">
			    <c:forEach var="item" items="${quick}" varStatus="loop">
			      <div class="radio-inline">
				    <label>
				    <c:choose>
				    	<c:when test="${loop.index == 0}">
				    		<springForm:radiobutton path="brTypeQuick" value="${item.getId()}" checked="checked"/> ${item.getTypeQuick()}
				    	</c:when>
				    	<c:otherwise>
				    		<springForm:radiobutton path="brTypeQuick" value="${item.getId()}" /> ${item.getTypeQuick()}
				    	</c:otherwise>
				    </c:choose>
				    </label>
				  </div>
				 </c:forEach>
			    </div>
			  </div>
			  
			  <div class="form-group">
			    <label class="col-sm-3 control-label">วันที่ส่งหนังสือ</label>
			    <div class="col-sm-4">
			      <div class="input-group date" id="datetimepicker1" data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="brDate" cssClass="form-control"
													value="${date}" disabled="true" />
		                <span class="input-group-addon">
		                    <span class="glyphicon glyphicon-calendar"></span>
		                </span>
		            </div>
		            <small class="help-block" style="color:red" id="err-brDate"></small>
			    </div>
			    <div class="col-sm-5">
			      <div class="checkbox">
				    <label>
				      <input type="checkbox" id="date_1" value="1"
													onclick="inter.checkChangeDate(this);"> กำหนดเอง
				    </label>
				  </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">เลขทะเบียนรับ</label>
			    <div class="col-sm-2 slash">
			    	<springForm:input path="brId" cssClass="form-control" value="${lastId}" />
			    </div>
			    <div class="col-sm-1 slashs"><label class="label-control">/</label></div>
			    <div class="col-sm-2 slash">
			      <springForm:input path="brYear" cssClass="form-control" value="${now.getYear() + 1900 + 543 }"/>
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
			      <springForm:input path="brPlace" cssClass="form-control"/>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">ลงวันที่</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker2' data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
			      		<springForm:input path="brRdate" cssClass="form-control" value="${date}" />
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
			      <springForm:input path="brFrom" cssClass="form-control"/>
			      <small class="help-block" style="color:red" id="err-brFrom"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">ถึง</label>
			    <div class="col-sm-9">
			      <springForm:input path="brTo" cssClass="form-control"/>
			      <small class="help-block" style="color:red" id="err-brTo"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">เรื่อง</label>
			    <div class="col-sm-9">
			       <springForm:input path="brSubject" cssClass="form-control"/>
			       <small class="help-block" style="color:red" id="err-brSubject"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">หมายเหตุ</label>
			    <div class="col-sm-9">
			      <textarea class="form-control" id="brRemark"></textarea>
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-3"></div>
			    <div class="col-sm-9">
			      <button class="btn btn-info" type="submit">บันทึก</button>
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
					    	<springForm:radiobutton path="brTypeSecret" value="${item.getId()}" checked="checked"/> ${item.getTypeSecret()}
					    </c:when>
					    <c:otherwise>
					    	<springForm:radiobutton path="brTypeSecret" value="${item.getId()}" /> ${item.getTypeSecret()}
					    </c:otherwise> 
				      </c:choose>
				    </label>
				  </div>
				  </c:forEach>
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
	<script type="text/javascript">
		$(function() {
			inter.lastId = "${lastId}";
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
			 $('#datetimepicker2').datepicker({
				/* language : 'th',
				format : "dd/mm/yyyy",
				startDate : new Date(), */
				autoclose : true,
				todayHighlight : true,
				disabled : true
			}); 
	
			var $form = $('#sendReciveForm');
			$form.bind('submit', function(e) {
				// Ajax validation
				var $inputs = $form.find('input');
				var data = collectFormData($inputs);
				data['brDivision'] = $('#brDivision').val();
				data['brRemark'] = $('#brRemark').val();
				if(!inter.checkValidateSendOut(data)) return false;
				$.post('saveRecive', data, function(response) {
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
		});
	</script> 
	</jsp:body>
</t:master>
