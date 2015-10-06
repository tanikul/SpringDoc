<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<div class="panel panel-default">
  <div class="panel-body">
      <springForm:form id="sendReciveForm" action="" method="POST" commandName="sendRecive" cssClass="form-horizontal">
	      <springForm:hidden path="brId" value="${obj.getBrId()}" />
	      <div class="col-md-12">
			  <div class="form-group">
			    <label class="col-sm-3 control-label">วันที่ส่งหนังสือ</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker1'>
			      		<springForm:input path="brDate" cssClass="form-control"  data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy"
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
			    <div class="col-sm-2">
			    	<springForm:input path="brId" cssClass="form-control" disabled="true"/>
			    </div>
			    <div class="col-sm-2">
			      <springForm:input path="brYear" cssClass="form-control" disabled="true" />
			    </div>
			    <div class="col-sm-3">
			      <div class="checkbox">
				    <label>
				      <input type="checkbox" onclick="inter.checkChangeRegis(this);"> กำหนดเอง
				    </label>
				  </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">เลขที่หนังสือ</label>
			    <div class="col-sm-4">
			      <springForm:input path="brPlace" cssClass="form-control"/>
			      <small class="help-block" style="color:red" id="err-brPlace"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">ลงวันที่</label>
			    <div class="col-sm-4">
			      <div class='input-group date' id='datetimepicker2'  data-provide="datepicker" data-date-language="th-th" data-date-format="dd/mm/yyyy">
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
			      <springForm:input path="brFrom" value="${obj.getBrFrom()}" cssClass="form-control"/>
			      <small class="help-block" style="color:red" id="err-brFrom"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">ถึง</label>
			    <div class="col-sm-9">
			      <springForm:input path="brTo" value="${obj.getBrTo()}" cssClass="form-control"/>
			      <small class="help-block" style="color:red" id="err-brTo"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">เรื่อง</label>
			    <div class="col-sm-9">
			       <springForm:input path="brSubject" value="${obj.getBrSubject()}" cssClass="form-control"/>
			       <small class="help-block" style="color:red" id="err-brSubject"></small>
			    </div>
			  </div>
			  <div class="form-group">
			    <label class="col-sm-3 control-label">หมายเหตุ</label>
			    <div class="col-sm-9">
			      <textarea class="form-control" id="brRemark">${obj.getBrRemark()}</textarea>
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
			</div>
		</springForm:form>
    </div>
 </div>
<script type="text/javascript">
	$(function() {
		$('#datetimepicker1').datepicker({
			/*language : 'th',
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
			/*language : 'th',
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
			data['brId'] = $('#brId').val();
			data['brRemark'] = $('#brRemark').val();
			if(!inter.checkValidateSendOut(data)) return false;
			$.post( GetSiteRoot()+'/internal/edit', data, function(response) {
				if(response == true){
					var tr = $('#edit-'+data.brId).parent().parent();
					tr.find('td:eq(0)').html(data.brYear);
					tr.find('td:eq(1)').html(data.brRdate);
					tr.find('td:eq(2)').html();
					tr.find('td:eq(3)').html(data.brPlace);
					tr.find('td:eq(4)').html(data.brDate);
					tr.find('td:eq(5)').html(data.brFrom);
					tr.find('td:eq(6)').html(data.brTo);
					tr.find('td:eq(7)').html(data.brSubject);
					tr.find('td:eq(8)').html(data.brRemark);
					$('#form-modal').modal('show');
				}				
			}, 'json');
			
			e.preventDefault();
			return false;
		});
	});
</script> 