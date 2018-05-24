function formSubmit() {
	$("#logoutForm").submit();
}

function collectFormData(fields) {
	var data = {};
	for (var i = 0; i < fields.length; i++) {
		var $item = $(fields[i]);
		data[$item.attr('name')] = $item.val();
	}
	return data;
}

var table;
var ex = {
	errMessage : {
		bsDate : 'วันที่ส่งหนังสือไม่ควรว่าง',
		bsFrom : 'กรุณาระบุจากหน่วยงาน',
		bsId : 'เลขทะเบียนไม่ควรเป็นค่าว่าง',
		bsNum : 'เลขทะเบียนไม่ควรเป็นค่าว่าง',
		bsPlace : 'เลขที่หนังสือไม่ควรเป็นค่าว่าง',
		bsRdate : 'ลงวันที่ไม่ควรเป็นค่าว่าง',
		bsSubject : 'หัวข้อเรื่องไม่ควรเป็นค่าว่าง',
		bsTo : 'ระบุปลายทาง',
		bsYear : 'เลขทะเบียนไม่ควรเป็นค่าว่าง',
		bsDivision : 'กรุณาเลือกหน่วยงาน',
	},
	lastId : 0,
};
ex.checkChangeDate = function(obj){
	var input = $("#datetimepicker"+obj.value).find('input');
	if($(obj).is(':checked')){
		input.removeAttr('readonly');
		//input.val('');
	}else{
		var now = new Date();
		input.val(convertToBuddhist(now.format("dd/mm/yyyy")));
		input.attr('readonly','readonly');
	}
}

ex.checkChangeRegis = function(obj){
	if($(obj).is(':checked')){
		$('#bsId').removeAttr('disabled');
		$('#bsNum').removeAttr('disabled');
		$('#bsYear').removeAttr('disabled');
		$('#bsId').val('');
		$('#bsNum').val('');
		$('#bsYear').val('');
	}else{
		$('#bsId').attr('disabled','disabled');
		$('#bsNum').attr('disabled','disabled');
		$('#bsYear').attr('disabled','disabled');
		ex.changeDivision($('#bsDivision'));
	}
}

ex.checkValidateSendOut = function(data){
	var chk = true;
	for(var x in data){
		if($('body').find('#err-'+x).length > 0){
			if(data[x] == ''){
				$('#err-'+x).html(ex.errMessage[x]);	
				chk = false;
			}else{
				$('#err-'+x).html('');	
			}
		}
	}
	return chk;
}

ex.changeDivision = function(obj){
	//$('#bsId').val($(obj).val());
	//$('#bsNum').val(ex.lastId);
	var year = new Date().getFullYear();
	year = year + 543;
	$('#bsYear').val(year);
	$('#bsPlace').val("กท " + $(obj).val() + "/" + ex.lastId);
}

ex.checkbsPlace = function(obj){
	if($(obj).is(':checked')){
		$('#bsPlace').removeAttr('readonly');
		$('#bsPlace').val('');
	}else{
		$('#bsPlace').attr('readonly','readonly');
		$('#bsPlace').val("กท " + $('#bsDivision').val() + "/" + ex.lastId);
	}
}

ex.editGet = function(id){
	url = GetSiteRoot() + "/external/edit/?id=" + id;
	window.location.href = url;
}

var inter = {
	errMessage : {
		brDate : 'วันที่ส่งหนังสือไม่ควรว่าง',
		brFrom : 'กรุณาระบุจากหน่วยงาน',
		brId : 'เลขทะเบียนไม่ควรเป็นค่าว่าง',
		brNum : 'เลขทะเบียนไม่ควรเป็นค่าว่าง',
		brPlace : 'เลขที่หนังสือไม่ควรเป็นค่าว่าง',
		brRdate : 'ลงวันที่ไม่ควรเป็นค่าว่าง',
		brSubject : 'หัวข้อเรื่องไม่ควรเป็นค่าว่าง',
		brTo : 'ระบุปลายทาง',
		brYear : 'เลขทะเบียนไม่ควรเป็นค่าว่าง',
		brDivision : 'กรุณาเลือกหน่วยงาน',
	},
	lastId : 0,
};

inter.checkChangeDate = function(obj){
	var input = $("#datetimepicker"+obj.value).find('input');
	if($(obj).is(':checked')){
		input.removeAttr('readonly');
		//input.val('');
	}else{
		var now = new Date();
		input.val(convertToBuddhist(now.format("dd/mm/yyyy")));
		input.attr('readonly','readonly');
	}
}

inter.checkChangeRegis = function(obj){
	if($(obj).is(':checked')){
		$('#brId').removeAttr('disabled');
		$('#brYear').removeAttr('disabled');
		$('#brId').val('');
		$('#brYear').val('');
	}else{
		$('#brId').attr('disabled','disabled');
		$('#brYear').attr('disabled','disabled');
		var year = new Date().getFullYear();
		year = year + 543;
		$('#brYear').val(year);
		$('#brId').val(inter.lastId);
	}
}

inter.checkValidateSendOut = function(data){
	var chk = true;
	for(var x in data){
		if($('body').find('#err-'+x).length > 0){
			if(data[x] == ''){
				$('#err-'+x).html(inter.errMessage[x]);	
				chk = false;
			}else{
				$('#err-'+x).html('');	
			}
		}
	}
	return chk;
}

inter.edit = function(id){
	$.ajax({
        url: GetSiteRoot() + "/internal/edit",
        type: "GET",
        cache: false,
        data: { 'id': id },
        success: function (response) {
            $('#form-modal').find('.modal-body').html(response);
            $('#form-modal').find('.modal-title').html("แก้ไขข้อมูล");
            $('#form-modal').modal('show');
        }
    });
}

inter.editGet = function(id){
	url = GetSiteRoot() + "/internal/edit/?id=" + id;
	window.open(url);
}

inter.checkbrPlace = function(obj){
	if($(obj).is(':checked')){
		$('#bsPlace').removeAttr('disabled');
		$('#bsPlace').val('');
	}else{
		$('#bsPlace').attr('disabled','disabled');
		$('#bsPlace').val("กท " + $('#bsDivision').val() + "/" + lastId);
	}
}

function convertToBuddhist(obj){
	var date = obj.split('/');
	var str = date[0] + '/' + date[1] + '/' + (parseInt(date[2])+543);
	return str;
}

function GetSiteRoot() {
    var rootPath = window.location.protocol + "//" + window.location.host;
    if (window.location.hostname == "localhost") {
        var path = window.location.pathname;
        if (path.indexOf("/") == 0) {
            path = path.substring(1);
        }
        
        path = path.split("/", 1);
        if (path != "") {
            rootPath = rootPath + "/" + path;
        }
    }else{
    	rootPath = rootPath + '/SpringDoc';
    }
    return rootPath;
}

SearchResult = function(){
	$("#loading").show();
	//if($("#table-display").find('table').attr('id') != 'search_tb'){
		$("#table-display").html('');
		$("#table-display").html($('#hide').html());
		$("#table-display").hide();
		$("#table-display").find('table').attr('id','search_tb');
	//}
	//setDDLYear($('input[id="type"]:checked').val());
	$("#search_tb").tablesorter({
      theme: 'blue',
      widthFixed: true,
      sortLocaleCompare: true, // needed for accented characters in the data
      sortList: [ [0,1] ],
      widgets: ['zebra', 'filter']
    }).tablesorterPager({
	    container: $(".pager"),
	    ajaxUrl : GetSiteRoot() + '/loadData',
	    customAjaxUrl: function(table, url) {
        $(table).trigger('changingUrl', url);
        	return url;
      },
      ajaxObject: {
        dataType: 'json',
      },
      dataSearch : {
    	year : $('#year').val(),
		type : $('input[id="type"]:checked').val(),
		startDate : $('#startDate').val(),
		endDate : $('#endDate').val(),
		subject : $('#subject').val(),
		num : $('#num').val(),
		remark : $('#remark').val(),
		from : $('#from').val(),
		to : $('#to').val()
      },
      ajaxProcessing: function(data){
        if (data && data.hasOwnProperty('rows')) {
          var indx, r, row, c, d = data.rows,
          total = data.total_rows,
          headers = data.headers,
          headerXref = headers.join(',').replace(/\s+/g,'').split(','),
          rows = [],
          len = d.length;
          for ( r=0; r < len; r++ ) {
            row = []; // new row array
            for ( c in d[r] ) {
              if (typeof(c) === "string") {
                indx = $.inArray( c, headerXref );
                if (indx >= 0) {
                  row[indx] = d[r][c];
                }
              }
            }
            rows.push(row); // add new row array to rows array
          }
          $("#table-display").show();
          $("#loading").hide();
          if($('input[id="type"]:checked').val() == 1){
        	  var th = $('.tablesorter').find('thead').find('tr').find('th')[2];
        	  $(th).find('div').html('วันที่รับหนังสือ');
        	  var tf = $('.tablesorter').find('tfoot').find('tr').find('th')[2];
        	  $(tf).html('วันที่รับหนังสือ');
          }else{
        	  var th = $('.tablesorter').find('thead').find('tr').find('th')[12];
        	  var tf = $('.tablesorter').find('tfoot').find('tr').find('th')[12];
        	  $(th).hide();
        	  $(tf).hide();
        	  th = $('.tablesorter').find('thead').find('tr').find('th')[7];
        	  $(th).find('div').html('ถึง');
          	  tf = $('.tablesorter').find('tfoot').find('tr').find('th')[7];
    	  	  $(tf).html('ถึง');
          }
          return [ total, rows, headers ];
        }
      },
      output: '{startRow} to {endRow} ({totalRows})',
      updateArrows: true,
      /*page: 1,*/
      size: 50,
      fixedHeight: false,
      removeRows: false,
      cssNext        : '.next',  // next page arrow
      cssPrev        : '.prev',  // previous page arrow
      cssFirst       : '.first', // go to first page arrow
      cssLast        : '.last',  // go to last page arrow
      cssPageDisplay : '.pagedisplay', // location of where the "output" is displayed
      cssPageSize    : '.pagesize', // page size selector - select dropdown that sets the "size" option
      cssErrorRow    : 'tablesorter-errorRow', // error information row
      cssDisabled    : 'disabled' // Note there is no period "." in front of this class name
    });
}

exportPdf = function(){
	
}

editUser = function(id){
	$.ajax({
        url: GetSiteRoot() + "/editUser",
        type: "POST",
        cache: false,
        data: { 'id': id },
        success: function (response) {
            $('#form-modal').find('.modal-body').html(response);
            $('#form-modal').find('.modal-title').html("แก้ไขข้อมูล");
            $('#form-modal').modal('show');
        }
    });
}

deleteUser = function(id){
	if(confirm('คุณต้องการลบผู้ใช้รายนี้ ?') == true){
		$.ajax({
	        url: GetSiteRoot() + "/removeUser",
	        type: "POST",
	        cache: false,
	        data: { 'id': id },
	        success: function (response) {
	        	if(response == 'success'){
	        		table.ajax.reload();
	        	}
	        }
	    });
	}
}

saveEditUser = function(id){
	var id = $('#id').val();
	var fname = $('#fname').val();
	var lname = $('#lname').val();
	var divisionCode = $('#divisionCode').val();
	var groupId = $('#groupId').val();
	var role = $('#role').val();
	var prefix = $('#prefix').val();
	var username = $('#username').val();
	var password = $('#password').val();
	$.ajax({
        url: GetSiteRoot() + "/saveEditUser",
        type: "POST",
        cache: false,
        data: { 'id': id,'fname': fname, 'lname' : lname, 'divisionCode' : divisionCode, 'groupId' : groupId, 'role' : role, 'prefix' : prefix, 'password' : password, 'username' : username },
        success: function (response) {
        	if(response == 'success'){
        		table.ajax.reload();
        		$('#form-modal').modal('hide');
        	}
        }
    });
}

addUser = function(){
	$.ajax({
        url: GetSiteRoot() + "/addUser",
        type: "GET",
        cache: false,
        success: function (response) {
    		$('#form-modal').find('.modal-body').html(response);
            $('#form-modal').find('.modal-title').html("เพิ่มข้อมูล");
            $('#form-modal').modal('show');
        }
    });
}

saveAddUser = function(){
	var chk = true;
	if($('#username').val() == ''){
		$('#username').parent().prev().parent().addClass('has-error');
		$('#username').next().html('กรุณากรอก Username ');
		chk = false;
	}else{
		$('#username').next().html('');
		$('#username').parent().prev().parent().removeClass('has-error');
	}
	if($('#password').val() == ''){
		$('#password').parent().prev().parent().addClass('has-error');
		$('#password').next().html('กรุณากรอก Password ');
		chk = false;
	}else{
		$('#password').next().html('');
		$('#password').parent().prev().parent().removeClass('has-error');
	}
	if($('#prefix').val() == ''){
		$('#prefix').parent().prev().parent().addClass('has-error');
		$('#prefix').next().html('กรุณาเลือกคำนำหน้า');
		chk = false;
	}else{
		$('#prefix').next().html('');
		$('#prefix').parent().prev().parent().removeClass('has-error');
	}
	if($('#fname').val() == ''){
		$('#fname').parent().prev().parent().addClass('has-error');
		$('#fname').next().html('กรุณากรอกชื่อ');
		chk = false;
	}else{
		$('#fname').next().html('');
		$('#fname').parent().prev().parent().removeClass('has-error');
	}
	if($('#lname').val() == ''){
		$('#lname').parent().prev().parent().addClass('has-error');
		$('#lname').next().html('กรุณากรอกนามสกุล');
		chk = false;
	}else{
		$('#lname').next().html('');
		$('#lname').parent().prev().parent().removeClass('has-error');
	}
	if($('#division').val() == ''){
		$('#division').parent().prev().parent().addClass('has-error');
		$('#division').next().html('กรุณาระบุหน่วยงาน');
		chk = false;
	}else{
		$('#division').next().html('');
		$('#division').parent().prev().parent().removeClass('has-error');
	}
	if(chk){
		var fname = $('#fname').val();
		var lname = $('#lname').val();
		var division = $('#division').val();
		var username = $('#username').val();
		var password = $('#password').val();
		var groupId = $('#groupId').val();
		var prefix = $('#prefix').val();
		var role = $('#role').val();
		$.ajax({
	        url: GetSiteRoot() + "/addUser",
	        type: "POST",
	        cache: false,
	        data: { 'username': username, 'password': password,'fname': fname, 'lname' : lname, 'division' : division, 'groupId' : groupId, 'prefix' : prefix, 'role' : role },
	        success: function (response) {
	        	if(response == 'success'){
	        		table.ajax.reload();
	        		$('#form-modal').modal('hide');
	        	}
	        }
	    });
	}
}

setDDLYear = function(type){
	$.ajax({
        url: GetSiteRoot() + "/loadDataYear",
        type: "POST",
        cache: false,
        data: { 'type': type },
        success: function (data) {
        	var _html = '<option value="">-- เลือกปี --</option>';
        	_html += '<option value="ALL">ทั้งหมด</option>';
        	$.each(data.years, function(i, year) {
        		_html += '<option value="'+year+'">'+year+'</option>';
        	});
        	$("#year").html(_html);
        }
    });
}

removeItem = function(id, type, obj, num){
	if(confirm('คุณต้องการลบรายการนี้ ?') == true){
		$.ajax({
	        url: GetSiteRoot() + "/delete",
	        type: "GET",
	        cache: false,
	        data: { 'id': id, 'type': type, 'num': num },
	        beforeSend: function (xhr) {
				$.LoadingOverlay("show");
			},
	        success: function (response) {
	        	$.LoadingOverlay("hide");
	            if(response == 1){
	            	$(obj).parent().parent().remove();
	            }else{
	            	alert('ไม่สามารถทำการลบได้ กรุณาติดต่ิผู้ดูแลระบบ');
	            }
	        }
	    });
	}
}

function loadTable(){
	  $("#myTable").tablesorter({
	      theme: 'blue',
	      widthFixed: true,
	      sortLocaleCompare: true, // needed for accented characters in the data
	      sortList: [ [0,1] ],
	      widgets: ['zebra', 'filter']
	    }).tablesorterPager({
		    container: $(".pager"),
		    ajaxUrl : GetSiteRoot() + '/loadData',
		    customAjaxUrl: function(table, url) {
	        $(table).trigger('changingUrl', url);
	        	return url;
	      },
	      ajaxObject: {
	        dataType: 'json',
	      },
	      ajaxProcessing: function(data){
	        if (data && data.hasOwnProperty('rows')) {
	          var indx, r, row, c, d = data.rows,
	          total = data.total_rows,
	          headers = data.headers,
	          headerXref = headers.join(',').replace(/\s+/g,'').split(','),
	          rows = [],
	          len = d.length;
	          for ( r=0; r < len; r++ ) {
	            row = []; // new row array
	            for ( c in d[r] ) {
	              if (typeof(c) === "string") {
	                indx = $.inArray( c, headerXref );
	                if (indx >= 0) {
	                  row[indx] = d[r][c];
	                }
	              }
	            }
	            rows.push(row); // add new row array to rows array
	          }
	          $("#loading").hide();
	          if($('input[id="type"]:checked').val() == 1){
	        	  var th = $('.tablesorter').find('thead').find('tr').find('th')[2];
	        	  $(th).find('div').html('วันที่รับหนังสือ');
	          	  var tf = $('.tablesorter').find('tfoot').find('tr').find('th')[2];
        	  	  $(tf).html('วันที่รับหนังสือ');
	          }else{
	        	  var th = $('.tablesorter').find('thead').find('tr').find('th')[7];
	        	  $(th).find('div').html('ถึง');
	          	  var tf = $('.tablesorter').find('tfoot').find('tr').find('th')[7];
        	  	  $(tf).html('ถึง');
	          }
	          var tf = $('.tablesorter').find('tfoot').find('tr').find('th')[2];
	          $('#table-display').show();
	          var type = $("input:radio[name ='type']:checked").val();
			  setDDLYear(type);
	          return [ total, rows, headers ];
	        }
	      },
	      output: '{startRow} to {endRow} ({totalRows})',
	      updateArrows: true,
	      /* page: 0, */
	      size: 50,
	      fixedHeight: false,
	      removeRows: false,
	      cssNext        : '.next',  // next page arrow
	      cssPrev        : '.prev',  // previous page arrow
	      cssFirst       : '.first', // go to first page arrow
	      cssLast        : '.last',  // go to last page arrow
	      cssPageDisplay : '.pagedisplay', // location of where the "output" is displayed
	      cssPageSize    : '.pagesize', // page size selector - select dropdown that sets the "size" option
	      cssErrorRow    : 'tablesorter-errorRow', // error information row
	      cssDisabled    : 'disabled' // Note there is no period "." in front of this class name
	    }).bind('pagerChange pagerComplete', function(event, options){
	      $('.tooltips').tooltipster({	     
			    functionBefore: function(instance, helper) { 
			    	var id = $(helper.origin).attr('val');
			    	$.ajax({
			            url: GetSiteRoot() + "/getStatusDetail",
			            type: "POST",
			            cache: false,
			            data: id,
			            contentType: "application/json; charset=utf-8",
			            dataType: "json",
			            success: function (response) {
				            var tmpDepartment = "";
							var tmpGroup = "";
							var strUser = "";
							var mapGroup = {};
							var rs = '';
							var status = '';
							$(response).each(function (i, row){
								status = row.status;
								if(i == 0){
									tmpDepartment = row.brToDepartmentName;
								}
								tmpGroup = (tmpGroup == '') ? row.brToGroupName : tmpGroup;
								if(tmpDepartment != row.brToDepartmentName){
									rs += "<div style=\"margin-left:5px;\">" + tmpDepartment + "</div>";
									for(var x in mapGroup){
										rs += "<div style=\"margin-left:20px; list-style-type: disc;\">" + x + "</div>";
										rs += mapGroup[x];
									}
									mapGroup = {};
									tmpDepartment = row.brToDepartmentName;
								}
								if(row.brToGroup != ''){
									if(tmpGroup != row.brToGroupName){
										mapGroup["<li>ฝ่่าย" + tmpGroup + "</li>"] = strUser;
										strUser = "";
										tmpGroup = row.brToGroupName;
									}
									if(row.brToUserName != ''){
										if(status == 'Y'){
											strUser += "<li style=\"margin-left:40px; list-style-type: disc;\">" + row.brToUserName + "</li>";
										}else{
											strUser += "<li style=\"margin-left:40px; list-style-type: circle;\">" + row.brToUserName + "</li>";
										}
									}
								}
							});
							if(tmpGroup != ''){
								mapGroup["<li>ฝ่่าย" + tmpGroup + "</li>"] = strUser;	
							}
							rs += "<div style=\"margin-left:5px;\">" + tmpDepartment + "</div>";
							for(var x in mapGroup){
								rs += "<div style=\"margin-left:20px; list-style-type: disc;\">" + x + "</div>";
								rs += mapGroup[x];
							}
			            	instance.content(rs);
			            }
			        });
			    },
			    contentAsHTML: true,
			    animation: 'fade',
			    delay: 200,
			    maxWidth: 'auto',
			    trigger: 'click'
			});
	    });
	  
	}

function changeDivisionToGroup(obj){
	$.ajax({
        url: GetSiteRoot() + "/account/getGroup",
        type: "POST",
        cache: false,
        dataType : "json",
        data: { 'divisionCode': $(obj).val() },
        success: function (response) {
        	var str = '';
        	for(var x in response){
        		str += '<option value="' + x + '">' + response[x] + '</option>';
        	}
        	if(str != ''){
        		str = '<select id="groupId" name="groupId" class="form-control"><option value="">--- เลือกฝ่าย ---</option>' + str + '</select>';
        		$('#group-box').html(str);
        	}else{
        		$('#groupId').val('').attr('disabled', true);
        	}
        }
    });
}

