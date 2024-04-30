var from_date;
var to_date;
var selectedDate = $('#from_date').val();
var myAttandanceTable;
var loggedInEmpRole;
var loggedInUserId;
var addOutgoingTable;
var loggedInUserName;
var SelectedcurrentDate = true;
var attendanceTable;
var allEmpOutgoingDtlsTable;
var outgoingRegisterDataToDisplay = [];
var useroutgoingRegisterData = [];
var selectedData;
var rowData;
var currstrDate;
var value;
var addRowClick = 0;
var startYear = 2005;
var currentYear = new Date().getFullYear();
var currDate = new Date();
var months = currDate.getMonth() + 1;
var date = currDate.getDate();

$(document).ready(function() {
	$(".body").css("overflow", "hidden");
	loggedInEmpRole = $("#loggedInUserRole").val();
	loggedInUserId = $("#loggedInUser").val();
	loggedInUserName = $("#LoggedInUserName").val();
	AllEmpOutgoingDtls();

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
	if (loggedInEmpRole == "2") {
		allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { width: "121" });
		allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { visible: true });
		$("#addOutgoing").on('click', function() {
			if (addRowClick == 0) {
				addRow();
			}
		});
	}

	if (loggedInEmpRole == "1") {
		$("#addOutgoing").on('click', function() {
			if ($("#switch1").is(':checked') == true) {
				if (addRowClick == 0) {
					addRow();
				}
			}
		});
	}

	//Enable Add Icon ontoggle ON
	$("#checkbox1").click(function() {
		$("#addOutgoingSpanBtn").toggleClass("addOutgoingSpanBtn_inactive");
		addRowClick = 0;
	});
});

/*
* on click view button 
* all employee attendance List is displaye if login user is admin
* if not then only login employee attendance details will be displayed
*/
$('#viewBtn').on('click', function(selected) {
	addRowClick = 0;
	outgoingViewAndSwitch();
})

//Display Self Outgoing Attendance of Employee
$('#switch1').on('click', function(selected) {
	outgoingViewAndSwitch();
})

//Display Save and Delete Icon ontoggle ON
function displayEditOptions() {
	var checkboxVal = $("#switch1").is(':checked');
	if (checkboxVal) {
		allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { width: "121" });
		allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { visible: true });
	} else if (loggedInEmpRole == 2) {
		allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { width: "121" });
		allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { visible: true });
	} else {
		allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { visible: false });
	}
}

if (months < 10) {
	months = "0" + months;
}

if (date < 10) {
	date = "0" + date;
}
currstrDate = currDate.getFullYear() + "-" + months + "-" + date;

/*
* @Descriptin
* it is used to display all register outoging details of All Employee
* On Click of Outgoing Detail button in Admin user Screen
*/
function AllEmpOutgoingDtls() {
	var checkboxVal = $("#switch1").is(':checked');
	selectedDate = $('#from_date').val();
	var params = {
		"punch_date": selectedDate,
		"emp_id": loggedInUserId,
		"myAttendanceCheckBox": checkboxVal,
		"role_id": loggedInEmpRole
	}

	if (selectedDate === "") {
		$("#allEmpOutgoingAttendanceLink").addClass("disableActiveLink");
		$("#allEmpOutgoingAttendanceLink").removeClass("attendanceLink");
	}

	allEmpOutgoingDtlsTable = new Tabulator("#AllEmpOutgoingDtlsTable", {
		height: "421px",
		layout: "fitColumns",
		ajaxURL: _contextPath + "user/getOutgoingDetails", //ajax URL
		ajaxConfig: "get",
		ajaxParams: params,
		tooltips: function(cell) {
			var value = cell.getValue();
			if (value !== "" && value !== null) {
				return value;
			}
			return "-"; // Display a placeholder when the value is null or empty
		},
		columns: [
			{
				title: punchDate,
				field: "punch_date",
				headerFilter: "input",
				headerFilter: joindateEditor,
				headerFilterPlaceholder: "YYYY-MM-DD",
				width: "175",
				formatter: function(cell, formatterParams) {
					if ($("#switch1").is(':checked') == false) {
						if (selectedDate == "" || selectedDate == 'undefined' || selectedDate == null) {
							return '<input type="text" class="form-control-sm" value ="' + currstrDate + '" style="width:100%;" disabled>';
						} else {
							return '<input type="text" class="form-control-sm" value ="' + selectedDate + '" style="width:100%;" disabled>';
						}
					} else {
						if (selectedDate == "" || selectedDate == 'undefined' || selectedDate == null) {
							return '<input type="text" class="form-control-sm" value ="' + currstrDate + '" placeholder="YYYY-MM-DD" disabled>';
						} else {
							return '<input type="text" class="form-control-sm" value ="' + selectedDate + '" placeholder="YYYY-MM-DD" disabled>';
						}
					}
				}

			},
			{
				title: employeeId,
				field: "emp_id",
				headerFilter: "input",
				headerFilterPlaceholder: search + ' ' + employeeId,
				widthGrow: 2,
				formatter: function(cell, formatterParams) {
					value = cell.getValue();
					if ($("#switch1").is(':checked') == false) {
						return '<input type="text" class="form-control-sm" value="' + value + '" id="employeeID" disabled>';
					} else {
						return '<input type="text" class="form-control-sm" value="' + value + '" id="employeeID" disabled>';
					}
				}
			},
			{
				title: employeeName,
				field: "emp_name",
				formatter: "center",
				headerFilter: "input",
				headerFilterPlaceholder: search + ' ' + employeeName,
				widthGrow: 3,
				formatter: function(cell, formatterParams) {
					value = cell.getValue();
					if ($("#switch1").is(':checked') == false) {
						return '<input type="text" class="form-control-sm" value="' + value + '" disabled>';
					} else {
						return '<input type="text" class="form-control-sm" value="' + value + '" disabled>';
					}
				}
			},
			{
				title: outgoingFrom,
				field: "out_going_from_time",
				formatter: "HH:MM",
				headerFilter: "input",
				headerFilterPlaceholder: search + ' ' + outgoingFrom,
				width: "160",
				cellEdited: function(cell) {
					var value = cell.getValue();
					var row = cell.getRow();
					var fieldName = cell.getField();

					row.update({ [fieldName]: value });
				},
				formatter: function(cell, formatterParams) {
					var value = cell.getValue();
					/*var disabled = ($("#switch1").is(':checked') == false) ? "disabled" : "";*/
					if (value != undefined) {
						return '<input type="time" placeholder="HH:MM" class="form-control-sm" value="' + (value || "") + '" disabled>';
					} else {
						//return '<input type="text" placeholder="HH:MM" class="form-control-sm" value="' + (value || "") + '" ' +  (value ? 'disabled' : 'readonly') + '>';
						return '<input type="time" id="outgoingfromtime" placeholder="HH:MM" class="form-control-sm" value="' + (value || "") + '" >';
					}
				}
			},
			{
				title: outgoingIn,
				field: "out_going_in_time",
				format: "hh:mm",
				formatter: "center",
				headerFilter: "input",
				headerFilterPlaceholder: search + ' ' + outgoingIn,
				width: "160",
				cellEdited: function(cell) {
					var value = cell.getValue();
					var row = cell.getRow();
					var fieldName = cell.getField();

					row.update({ [fieldName]: value });
				},
				formatter: function(cell, formatterParams) {
					var value = cell.getValue();
					if (value != undefined) {
						var disabled = ($("#switch1").is(':checked') == false) ? "disabled" : "disabled";
						return '<input type="time" placeholder="HH:MM" class="form-control-sm" value="' + (value || "") + '" ' + disabled + '>';
					} else {
						return '<input type="time" id="outgoingintime" placeholder="HH:MM" class="form-control-sm" value="' + (value || "") + '" >';
					}

				}
			},
			{
				title: "",
				field: "edit",
				formatter: "center",
				width: "100",
				headerSort: false,
				visible: false,
				formatter: function(cell, row, formatterParams) {
					var cellPostion = cell.getRow().getPosition(true);
					var saveRowFunction = "saveRow(" + cellPostion + ")";
					var deleteRowFunction = "deleteOutgoingRow(" + cellPostion + ")";
					return '<span>' + '</span><span class="text-center ml-5 tabulator-icons-cell" style="margin-top:4px; width: 0px;position: relative;left: -13px;">' +
						'<i class="fas fa-save cursorAuto text-left" title="Save" style="color: #1d56b9; font-size: 17px; position: relative; left: -10px" ' +
						'onClick="' + saveRowFunction + '"></i>' +
						'<i class="fa-solid fa-circle-minus fa-circle-minus-active fa-lg text-center ml-2" title="Delete" style="color: #0e5add;margin-top: 3px;"' + 'onClick="' + deleteRowFunction + '"></i>' +
						'</span>';
				},

			},

		],

	})
}

//Oncick of Add Button add New Row to Outgoing Tabulator
function addRow() {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {

			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				addRowClick = 1;
				//this logic is used for deactivate all tabulator-row cells
				var elements = document.querySelectorAll('.tabulator-row');
				elements.forEach(function(element) {
					element.classList.add('fa-row-deactive');
				});
				//this logic is used for activate current tabulator-row cell
				$('.tabulator-row .tabulator-cell').removeClass('fa-row-deactive').parent().siblings().addClass('fa-row-deactive');
				if ($("#switch1").is(':checked') == true) {
					allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { width: "121" });
					allEmpOutgoingDtlsTable.updateColumnDefinition("edit", { visible: true });
					var newRowData = {
						emp_name: loggedInUserName,
						emp_id: loggedInUserId
					};
					allEmpOutgoingDtlsTable.addRow(newRowData);
				}
				else {
					var newRowData = {
						emp_name: loggedInUserName,
						emp_id: loggedInUserId
					};
					allEmpOutgoingDtlsTable.addRow(newRowData);
				}
				$('.tabulator-row .tabulator-cell i').removeClass('cursorAuto').parent().siblings().last().addClass('cursorPointer');
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
}

//Onclick of Delete icon respective Row will be deleted in Outgoing Tabulator
function deleteOutgoingRow(cellPostion) {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {

			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				var rowData = allEmpOutgoingDtlsTable.getData()[cellPostion];
				// Access the data from the rowData object
				var punchDate = rowData.punch_date || selectedDate || currstrDate;
				var empId = rowData.emp_id;
				var empName = rowData.emp_name;
				var outgoingFrom = rowData.out_going_from_time;
				var outgoingIn = rowData.out_going_in_time;
				var params = {
					"punch_date": punchDate,
					"emp_id": empId,
					"emp_name": empName,
					"out_going_from_time": outgoingFrom,
					"out_going_in_time": outgoingIn
				}
				if (confirm(MSG96) == true) {
					$.ajax({
						url: _contextPath + 'deleteOutgoingAttendance',
						type: 'POST',
						method: "POST",
						data: JSON.stringify(params),
						headers: {
							'Accept': 'application/json',
							'Content-Type': 'application/json'
						},
						success: function(response) {
							addRowClick = 0;
							alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);//OutgoingAttendance Saved successfully.
							AllEmpOutgoingDtls();
							displayEditOptions();

						},
						error: function(request, status, error) {
							alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
							AllEmpOutgoingDtls();
							displayEditOptions();
						}
					}); // ajax closed
				}
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
}

//Onclick of Save icon respective Row will be Saved to Outgoing Tabulator
function saveRow(cellPostion) {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				var rowData = allEmpOutgoingDtlsTable.getData()[cellPostion];
				var prevRowData = allEmpOutgoingDtlsTable.getData()[cellPostion - 1];
				var prevOutgoingIn;
				// Access the data from the rowData object
				var punchDate = rowData.punch_date || selectedDate || currstrDate;
				var empId = rowData.emp_id;
				var empName = rowData.emp_name;
				//var outgoingFrom = rowData.out_going_from_time;
				//var outgoingIn = rowData.out_going_in_time;
				if (prevRowData == null || prevRowData == '' || prevRowData == 'undefined') {
					prevOutgoingIn = "00:00";
				} else {
					prevOutgoingIn = prevRowData.out_going_in_time;
				}

				var outgoingFrom = document.getElementById("outgoingfromtime").value;
				var outgoingIn = document.getElementById("outgoingintime").value;
				var regex = /^([01]\d|2[0-3]):([0-5]\d)$/;
				valid_outgoingFrom = regex.test(outgoingFrom);
				valid_outgoingIn = regex.test(outgoingIn);

				var twohours = 120;
				var isGreaterThan2Hours;

				var prevOutgoingInMin = prevOutgoingIn.substring(3, 6);
				var prevOutgoingInHr = prevOutgoingIn.substring(0, 2);
				var curOutgoingFromHr = outgoingFrom.substring(0, 2);
				var curOutgoingFromMin = outgoingFrom.substring(3, 6);

				var params = {
					"punch_date": punchDate,
					"emp_id": empId,
					"emp_name": empName,
					"out_going_from_time": outgoingFrom,
					"out_going_in_time": outgoingIn
				}

				if ((outgoingFrom == null || outgoingFrom == '') && (outgoingIn == null || outgoingIn == '')) {
					alert('MSG52 : ' + MSG52);
					return false;
				} else if (outgoingFrom == null || outgoingFrom == '') {
					alert('MSG53 : ' + MSG53);
					return false;
				} else if (outgoingIn == null || outgoingIn == '') {
					alert('MSG54 : ' + MSG54);
					return false;
				} else if (outgoingFrom == outgoingIn) {
					alert('MSG50 : ' + MSG50);
					return false;
				} else if (!valid_outgoingFrom) {
					alert('MSG66 : ' + MSG66);
					return false;
				} else if (!valid_outgoingIn) {
					alert('MSG67 : ' + MSG67);
					return false;
				} else if (outgoingFrom > outgoingIn) {
					alert('MSG51 : ' + MSG51);
					return false;
				} else if (outgoingFrom >= "13:00" && outgoingFrom < "14:00") {
					alert('MSG92 : ' + MSG92);
					return false;
				} else if (prevOutgoingInHr <= curOutgoingFromHr) {
					if ((prevOutgoingInHr == curOutgoingFromHr) && (prevOutgoingInMin > 00 && prevOutgoingInMin < 30) && (curOutgoingFromMin < 30)) {
						alert('MSG102 : ' + MSG102 + curOutgoingFromHr + ":29");
						return false;
					} else if ((prevOutgoingInHr == curOutgoingFromHr) && (prevOutgoingInMin >= 31 && prevOutgoingInMin <= 59) && (curOutgoingFromMin >= 30)) {
						alert('MSG102 : ' + MSG102 + curOutgoingFromHr + ":59");
						return false;
					} else {
						insert(params);
					}
				} else {
					insert(params);
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
	var startYear = 2005;
	var currentYear = new Date().getFullYear();
	input.setAttribute("type", "text");
	input.setAttribute("maxLength", "10");
	input.setAttribute("id", "punch_date");
	$(input).css({
		"padding": "4px",
		"width": "100%",
		"boxSizing": "border-box",
	});

	$(input).datepicker({
		format: 'yyyy-mm-dd',
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
		$('#end_date').datepicker('hide');
		$('#from_date').datepicker('hide');
	});

	$(input).blur(function(e) {
		var newDate = new Date($("#punch_date").val());
		if (Object.prototype.toString.call(newDate) === "[object Date]") {
			if (isNaN(newDate.getTime())) {
				$("#punch_date").val("");
			}
		} else {
			$("#punch_date").val("");
		}
	});

	return input;
};

//To check Time difference greater than 2 Hours
function isTimeDifferenceGreaterThantwohours(outgoingFrom, outgoingIn, twohours) {
	var startParts = outgoingFrom.split(':');
	var endParts = outgoingIn.split(':');

	var startDate = new Date();
	startDate.setHours(parseInt(startParts[0], 10));
	startDate.setMinutes(parseInt(startParts[1], 10));

	var endDate = new Date();
	endDate.setHours(parseInt(endParts[0], 10));
	endDate.setMinutes(parseInt(endParts[1], 10));

	var timeDiff = endDate - startDate;
	var minutes = Math.floor(timeDiff / (1000 * 60));

	return Math.abs(minutes) > twohours;
}

//Save Outgoing Details to DB
function insert(params) {
	$.ajax({
		url: _contextPath + 'user/saveOutgoingAttendance',
		type: 'POST',
		method: "POST",
		data: JSON.stringify(params),
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		success: function(response) {
			switch (response.RESULT) {
				case 'SUCCESS':
					addRowClick = 0;
					alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);//OutgoingAttendance Saved successfully.
					AllEmpOutgoingDtls();
					displayEditOptions();
					break;
				case 'ERROR':
					alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
					return false;
				case 'EXISTS':
					alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
					return false;
			}
		},
		error: function(request, status, error) {
			alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
			return false;
		}

	}); // ajax closed
}