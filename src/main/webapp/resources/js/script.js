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
		input.removeAttr('disabled');
		input.val('');
	}else{
		var now = new Date();
		input.val(convertToBuddhist(now.format("dd/mm/yyyy")));
		input.attr('disabled','disabled');
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
	$('#bsId').val($(obj).val());
	$('#bsNum').val(ex.lastId);
	var year = new Date().getFullYear();
	year = year + 543;
	$('#bsYear').val(year);
	$('#bsPlace').val("กท " + $(obj).val() + "/" + ex.lastId);
}

ex.checkbsPlace = function(obj){
	if($(obj).is(':checked')){
		$('#bsPlace').removeAttr('disabled');
		$('#bsPlace').val('');
	}else{
		$('#bsPlace').attr('disabled','disabled');
		$('#bsPlace').val("กท " + $('#bsDivision').val() + "/" + ex.lastId);
	}
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
		input.removeAttr('disabled');
		input.val('');
	}else{
		var now = new Date();
		input.val(convertToBuddhist(now.format("dd/mm/yyyy")));
		input.attr('disabled','disabled');
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
    }
    return rootPath;
}

SearchResult = function(){
	$("#loading").show();
	$("#table-display").html('');
	$("#table-display").html($('#hide').html());
	$("#table-display").hide();
	$("#table-display").find('table').attr('id','search_tb');
	$("#search_tb").tablesorter({
      theme: 'blue',
      widthFixed: true,
      sortLocaleCompare: true, // needed for accented characters in the data
      sortList: [ [0,1] ],
      widgets: ['zebra', 'filter']
    }).tablesorterPager({
	    container: $(".pager"),
	    ajaxUrl : 'loadData',
	    customAjaxUrl: function(table, url) {
        $(table).trigger('changingUrl', url);
        	return url;
      },
      ajaxObject: {
        dataType: 'json',
      },
      dataSearch : {
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
          return [ total, rows, headers ];
        }
      },
      output: '{startRow} to {endRow} ({totalRows})',
      updateArrows: true,
      /*page: 1,*/
      size: 25,
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
	$.ajax({
        url: GetSiteRoot() + "/saveEditUser",
        type: "POST",
        cache: false,
        data: { 'id': id,'fname': fname, 'lname' : lname, 'divisionCode' : divisionCode },
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
	var fname = $('#fname').val();
	var lname = $('#lname').val();
	var division = $('#division').val();
	var username = $('#username').val();
	var password = $('#password').val();
	$.ajax({
        url: GetSiteRoot() + "/addUser",
        type: "POST",
        cache: false,
        data: { 'username': username, 'password': password,'fname': fname, 'lname' : lname, 'division' : division },
        success: function (response) {
        	if(response == 'success'){
        		table.ajax.reload();
        		$('#form-modal').modal('hide');
        	}
        }
    });
}