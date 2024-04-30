var from_date;
var to_date;
var selectedDate;
var myAttandanceTable;
var loggedInEmpRole;
var loggedInUserId;
var addOutgoingTable;
var loggedInUserName;
var SelectedcurrentDate = true;
var attendanceTable;
var startYear = 2005;
var currentYear = new Date().getFullYear();

$(document).ready(function() {
	$(".body").css("overflow", "hidden");
	$("#addOutgoingSpanBtn").css("margin-top", "-15%");
	loggedInEmpRole = $("#loggedInUserRole").val();
	loggedInUserId = $("#loggedInUserId").val();
	loggedInUserName = $("#LoggedInUserName").val();
	if (loggedInEmpRole === '1') {
		$("#switch1").css("display", "inline");
		$("#checkbox1").css("display", "inline");
	} else {
		$("#addOutgoingSpanBtn").css("pointer-events", "initial");
		$("#addOutgoingSpanBtn").css("opacity", "1");
		$("#addOutgoingSpanBtn").css("cursor", "pointer");
		//$("#addOutgoingSpanBtn").css("margin-left","500%")
	}
	loadAttendance();
	
	//Select Date Datepicker	
	$('#calicon').click(function() {
		$('#from_date').datepicker('show');
	});
	$('#from_date').on('changeDate', function(selected) {
		if ($('#to_date').val() != '') {
			$('#to_date').val('');
		}
		var minimumDate = new Date($('#from_date').val());
		$('#to_date').datepicker('setStartDate', minimumDate);
	});

	//Display Date picker From 01-Jan-2005 to current date
	$('#from_date').datepicker({
		constrainInput: true,
		format: 'yyyy-mm-dd',
		startDate: new Date(startYear, 0, 1), // January 1st of startYear
		endDate: '+0d',
		autoclose: true
	});
});

/*
* on click view button 
* all employee attendance List is displaye if login user is admin
* if not then only login employee attendance details will be displayed
*/
$('#viewBtn').on('click', function(selected) {
	allAttendanceViewAndSwitch();
})

//Display Self Attendance of Employee
$('#switch1').on('click', function(selected) {
	allAttendanceViewAndSwitch();
})

//Display All Attendance Tabulator
function loadAttendance() {
	//var checkSessionForAttendance = sessionCheck();
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		dataType: 'json',
		success: function(response) {
			if (response.session == 'false') {
				window.location.href = _contextPath + 'login';
			} else {
				var checkboxVal = $("#switch1").is(':checked');
				selectedDate = $('#from_date').val();
				var params = {
					"punch_date": selectedDate,
					"emp_id": loggedInUserId,
					"myAttendanceCheckBox": checkboxVal,
					"role_id": loggedInEmpRole
				}
				if (selectedDate === "") {
					$("#allEmpAttendanceLink").addClass("disableActiveLink");
					$("#allEmpAttendanceLink").removeClass("attendanceLink");
				}
				attendanceTable = new Tabulator("#allEmp_AttendanceListTableDiv", {
					height: "421px",
					layout: "fitColumns",
					ajaxURL: _contextPath + "user/getAttendance", //ajax URL
					ajaxConfig: "get",
					ajaxParams: params,
					tooltips: function(cell) {
						var value = cell.getValue();
						if (value !== "" && value !== null) {
							return value;
						}
						return "-"; // Display a placeholder when the value is null or empty
					},
					rowFormatter: function(row) {
						if (row.getData().out_going_flg == "1") {
							row.getElement().style.color = 'green';
						}
					},
					columns: [
						{
							title: punchDate,
							field: "punch_date",
							headerFilter: "input",
							headerFilter: joindateEditor,
							headerFilterPlaceholder: "YYYY-MM-DD",
							width: "128",
						},
						{
							title: employeeId,
							field: "emp_id",
							headerFilter: "input",
							headerFilterPlaceholder: search + ' ' + employeeId,
							width: "115",
						},
						{
							title: employeeName,
							field: "emp_name",
							formatter: "center",
							headerFilter: "input",
							headerFilterPlaceholder: search + ' ' + employeeName,
							widthGrow: 2,

						},
						{
							title: punchInTime,
							field: "punch_in_time",
							headerFilter: "input",
							headerFilterPlaceholder: search + ' ' + punchInTime,
							headerFilterFunc: function(headerValue, rowValue, rowData, filterParams) {
								if (headerValue.trim() === "*") {
									return rowValue === ""; // Include rows with empty name
								}
								return rowValue.toLowerCase().includes(headerValue.toLowerCase());
							},
							width: "125",
						},
						{
							title: punchOutTime,
							field: "punch_out_time",
							formatter: "center",
							headerFilter: "input",
							headerFilterPlaceholder: search + ' ' + punchOutTime,
							headerFilterFunc: function(headerValue, rowValue, rowData, filterParams) {
								if (headerValue.trim() === "*") {
									return rowValue === ""; // Include rows with empty name
								}
								return rowValue.toLowerCase().includes(headerValue.toLowerCase());
							},
							width: "135",
						},

						{
							title: totalHours,
							field: "working_hours",
							hozAlign: "center",
							headerFilter: "input",
							headerFilterPlaceholder: search + ' ' + totalHours,
							width: "165",
						},
						{
							title: outgoingStatus,
							field: "out_going_flg",
							width: "135",
							align: 'center',
							headerFilter: customCheckboxFilter,
							formatter: "tickCross",
							headerSort: false,
							headerFilterEmptyCheck: function(value) {
								return value === null;
							}
							, headerClick: function(e, column) {
								customHeaderSort(column);
							}
						},
					],
				});

				if (selectedDate === "") {
					$("#allEmpAttendanceLink").addClass("disableActiveLink");
				}
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
}

//Tabulator Joined Date picker
var joindateEditor = function(cell, onRendered, success, cancel) {
	//create and style input
	var input = document.createElement("input");
	input.setAttribute("type", "text");
	input.setAttribute("maxLength", "10");
	input.setAttribute("id", "punch_date");
	$(input).css({
		"padding": "4px",
		"width": "100%",
		"boxSizing": "border-box",
		"outline-offset": "0px"
	});

	$(input).datepicker({
		format: 'yyyy/mm/dd',
		autoclose: true,
		startDate: new Date(startYear, 0, 1), // January 1st of startYear
		endDate: '+0d',
		pickTime: false,
	}).on('changeDate', function(selected) {
		var newDate = new Date(selected.date.valueOf());
		success(moment(newDate).format("YYYY-MM-DD"));
	});

	$(input).inputmask({
		mask: '9999-99-99',
		showMaskOnHover: false,
		showMaskOnFocus: false,
	});

	$(input).click(function(e) {
		$(input).css("outline-offset", "0px");
		$(input).css("outline-color", "#007bff");
		$('#end_date').datepicker('hide');
		$('#from_date').datepicker('hide');
	});

	$(input).blur(function(e) {
		var newDate = new Date($("#punch_date").val());
		if (Object.prototype.toString.call(newDate) === "[object Date]") {
			// it is a date
			if (isNaN(newDate.getTime())) {  // d.valueOf() could also work
				// date is not valid
				$("#punch_date").val("");
			}
		} else {
			// not a date
			$("#punch_date").val("");
		}
	});

	return input;
};
