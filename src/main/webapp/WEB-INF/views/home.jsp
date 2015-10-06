<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form"
	prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<c:url value="/download" var="downloadUrl"/>
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
										    <input type="radio" id="type" name="type" value="1" checked="checked" onchange="SearchResult();"> หนังสือรับ - จากภายนอก
										  </label>
										</div>
										<div class="radio">
										  <label>
										    <input type="radio" id="type" name="type" value="2" onchange="SearchResult();"> หนังสือส่ง - ออกภายนอก
										  </label>
										</div>
										<label>ช่วงเวลาในการค้นหา</label>
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
										<input type="button" onclick="download();" value="ออกรายงาน" class="button blue">
									</div>
									<table id="myTable" class="tablesorter">
										<thead>
											<tr>
												<th class="header">1</th>
												<th class="header">2</th>
												<th class="header">3</th>
												<th class="header">4</th>
												<th class="header">5</th>
												<th class="header">6</th>
												<th class="header">7</th>
												<th class="header">8</th>
												<th class="header">9</th>
												<th class="header">10</th>
											</tr>
										</thead>
										<tbody>
											
										</tbody>
										<tfoot>
											<tr>
										      <th>1</th>
										      <th>2</th>
										      <th>3</th>
										      <th>4</th>
										      <th>5</th>
										      <th>6</th>
										      <th>7</th>
										      <th>8</th>
										      <th>9</th>
										      <th>10</th>
										    </tr>
											<tr>
										      <td class="pager" colspan="10">
										        <img src="<c:url value="/css/blue/icon/first.png" />" class="first"/>
										        <img src="<c:url value="/css/blue/icon/prev.png" />" class="prev"/>
										        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
										        <img src="<c:url value="/css/blue/icon/next.png" />" class="next"/>
										        <img src="<c:url value="/css/blue/icon/last.png" />" class="last"/>
										        <select class="pagesize">
										          <option value="25">25</option>
										        </select>
										      </td>
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
				<input type="button" onclick="download();" value="ออกรายงาน" class="button blue">
			</div>
			<table class="tablesorter">
				<thead>
					<tr>
						<th class="header">1</th>
						<th class="header">2</th>
						<th class="header">3</th>
						<th class="header">4</th>
						<th class="header">5</th>
						<th class="header">6</th>
						<th class="header">7</th>
						<th class="header">8</th>
						<th class="header">9</th>
						<th class="header">10</th>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
				<tfoot>
					<tr>
				      <th>1</th>
				      <th>2</th>
				      <th>3</th>
				      <th>4</th>
				      <th>5</th>
				      <th>6</th>
				      <th>7</th>
				      <th>8</th>
				      <th>9</th>
				      <th>10</th>
				    </tr>
					<tr>
				      <td class="pager" colspan="10">
				        <img src="<c:url value="/css/blue/icon/first.png" />" class="first"/>
				        <img src="<c:url value="/css/blue/icon/prev.png" />" class="prev"/>
				        <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
				        <img src="<c:url value="/css/blue/icon/next.png" />" class="next"/>
				        <img src="<c:url value="/css/blue/icon/last.png" />" class="last"/>
				        <select class="pagesize">
				          <option value="25">25</option>
				        </select>
				      </td>
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
		
			  $("#myTable").tablesorter({
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
			          $('#table-display').show();
			          return [ total, rows, headers ];
			        }
			      },
			      output: '{startRow} to {endRow} ({totalRows})',
			      updateArrows: true,
			      /* page: 0, */
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
			});

			function download() {
				window.open('${downloadUrl}');
			}
		</script>
	</jsp:body>
</t:master>
