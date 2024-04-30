/*210016*/
var employeeTable;
var tempDirectoryPath;
var loaded = false;
var isUpdate = false;

$(document).ready(function() {
	dropDownHideShow();
	loadEmployee();

	//Download Tabulator
	$("#btnDownload").on('click', function(e) {
		$.ajax({
			async: false,
			type: 'get',
			url: _contextPath + 'checkSessionTimeout',
			success: function(response) {
				if (response.session == 'false' || response.session == undefined) {
					window.location.href = _contextPath + 'login';
				} else {
					var extension = excelFileNameExtension();
					var fileName = "Employee Master_" + extension + ".xlsx";
					employeeTable.download("xlsx", fileName);
				}
			},
			Error: function(msg) {
				window.location.href = _contextPath + 'login';
			}
		});
	});
})

//Display pop-up onclick of Add
$("#addEmployeeBtn").click(function() {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				jQuery.get('getAddEmployee', function(data) {
					jQuery("#modelpopup").empty();
					jQuery("#modelpopup").append(data);
					jQuery("#addEmployeeModal").modal("show");
				});
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
})

//Display pop-up onclick of Modify
$("#ModifyEmployee").on('click', function(e) {
	$("#ModifyEmployee").prop('disabled', true);
	var selectedData = employeeTable.getSelectedData();
	var param = selectedData[0]; // Assign the whole object directly
	isUpdate = true;
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				jQuery.get('getUpdateEmployee', param, function(data) {
					jQuery("#modelpopup").empty();
					jQuery("#modelpopup").append(data);
					jQuery("#addEmployeeModal").modal("show");
				});
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
});

//Display pop-up onclick of Delete
$("#DeleteEmployee").click(function(e) {
	$("#DeleteEmployee").prop('disabled', true);
	var selectedData = employeeTable.getSelectedData();
	var params = selectedData[0];

	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				jQuery.get('getDeleteEmployee', params, function(response) {
					jQuery("#modelpopup").empty();
					jQuery("#modelpopup").append(response);
					jQuery("#DeleteEmployeeModel").modal("show");
				});
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
});

//Enable Modify & Delete buttons
function enableButtons(row) {
	var selectedData = employeeTable.getSelectedData();
	var active_flg = selectedData[0].active_flg;
	if (active_flg == 1) {
		$("#ModifyEmployee").prop('disabled', false);
		$("#ModifyEmployee").addClass('btn-sm-enabled');
		$("#DeleteEmployee").prop('disabled', false);
		$("#DeleteEmployee").addClass('btn-sm-enabled-danger');
	}
}

// Disable Modify & Delete buttons
function disableButtons(row) {
	$("#ModifyEmployee").prop('disabled', true);
	$("#ModifyEmployee").removeClass('btn-sm-enabled');
	$("#DeleteEmployee").prop('disabled', true);
	$("#DeleteEmployee").removeClass('btn-sm-enabled-danger');
}

//Display Employee Tabulator
function loadEmployee() {
	employeeTable = new Tabulator("#employeeTable", {
		tooltips: true,
		height: "421px",
		selectable: 1,
		ajaxURL: _contextPath + "admin/getAllEmployee",
		ajaxConfig: "get",
		rowSelected: function(row) { enableButtons(row); },
		rowDeselected: function(row) { disableButtons(row); },
		sortOrderReverse: true,
		columnHeaderSortMulti: true,
		tooltips: function(cell) {
			var value = cell.getValue();
			if (value !== "" && value !== null) {
				return value;
			}
			return "-"; // Display a placeholder when the value is null or empty
		},
		columns: [

			{
				title: employeeId,
				field: "emp_id",
				headerFilterPlaceholder: search + ' ' + employeeId,
				accessorDownload: cellFormater,
				width: "120",
				headerFilter: "input"
			},
			{
				title: employeeName,
				field: "emp_name",
				headerFilterPlaceholder: search + ' ' + employeeName,
				accessorDownload: cellFormater,
				width: "150",
				headerFilter: "input"
			},
			{
				title: email,
				field: "office_email",
				headerFilterPlaceholder: search + ' ' + email,
				accessorDownload: cellFormater,
				width: "150",
				sorter: "string",
				headerFilter: "input"
			},
			{
				title: microsoftEmail,
				field: "office_email_365",
				headerFilterPlaceholder: search + ' ' + microsoftEmail,
				accessorDownload: cellFormater,
				width: "150",
				headerFilter: "input"
			},
			{
				title: contactNumber,
				field: "contact_number",
				headerFilterPlaceholder: search + ' ' + contactNumber,
				accessorDownload: cellFormater,
				width: "140",
				headerFilter: "input"
			},
			{
				title: employeeType,
				field: "emp_type",
				headerFilterPlaceholder: search + ' ' + employeeType,
				accessorDownload: cellFormater,
				width: "140",
				headerFilter: "input"
			},
			{
				title: employeeCategory,
				field: "emp_level",
				headerFilterPlaceholder: search + ' ' + employeeCategory,
				accessorDownload: cellFormater,
				width: "155",
				headerFilter: "input"
			},
			{
				title: joinedDate
				, field: "emp_joined_date"
				, headerFilter: joindateEditor
				, headerFilterPlaceholder: "YYYY-MM-DD"
				, accessorDownload: cellFormater
				, headerSort: true
				, headerClick: function(e, column) {
					customHeaderSort(column);
				}
			},

			{
				title: department,
				field: "dept_name",
				headerFilterPlaceholder: search + ' ' + department,
				accessorDownload: cellFormater,
				width: "120",
				headerFilter: "input",
				sorter: "string",
			},
			{
				title: designation,
				field: "desg_name",
				headerFilterPlaceholder: search + ' ' + designation,
				accessorDownload: cellFormater,
				width: "150",
				headerFilter: "input",
				sorter: "string"
			},
			{
				title: role,
				field: "role_name",
				headerFilterPlaceholder: search + ' ' + role,
				accessorDownload: cellFormater,
				width: "120",
				headerFilter: "input",
				headerSort: true
			},
			{
				title: active,
				field: "active_flg",
				width: "120",
				align: 'center',
				headerFilter: customCheckboxFilter,
				accessorDownload: cellFormater,
				formatter: "tickCross",
				headerSort: true,
				headerFilterEmptyCheck: function(value) {
					return value === null;
				}
				, headerClick: function(e, column) {
					customHeaderSort(column);
				}
			},
			{
				title: "Image",
				field: "emp_image_path",
				visible: false,
				download: false,

			},
			{
				title: "PTMS",
				field: "ptms_admin_flg",
				visible: false,
				download: false,

			},
			{
				title: "Password",
				field: "password",
				visible: false,
				download: false,
			},
			{
				title: "User Lock",
				field: "user_lock_flg",
				visible: false,
				download: false,
			},
			{
				title: "Attempts",
				field: "login_fail_attempts",
				visible: false,
				download: false,
			},

		],
	})
}

//Tabulator Joined Date picker
var joindateEditor = function(cell, onRendered, success, cancel) {
	//create and style input
	var input = document.createElement("input");
	// Set the start and end year
	const startYear = 2005;
	const currentYear = new Date().getFullYear();
	input.setAttribute("type", "text");
	input.setAttribute("maxLength", "10");
	input.setAttribute("id", "join_date");
	$(input).css({
		"padding": "4px",
		"width": "100%",
		"boxSizing": "border-box"
	});
	if (cell.getValue() !== null) {
		$(input).val(cell.getValue());
	}
	$(input).datepicker({
		format: 'yyyy-mm-dd',
		startDate: new Date(startYear, 0, 1), // January 1st of startYear
		endDate: '+0d',
		autoclose: true,
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
	});

	$(input).blur(function(e) {
		var newDate = new Date($("#emp_joined_date").val());
		if (Object.prototype.toString.call(newDate) === "[object Date]") {
			// it is a date
			if (isNaN(newDate.getTime())) {  // d.valueOf() could also work
				// date is not valid
				$("#emp_joined_date").val("");
			}
		} else {
			// not a date
			$("#emp_joined_date").val("");
		}
	});
	return input;
};