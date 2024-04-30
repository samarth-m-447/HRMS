//210016
var selectedRow;
var holidayMasterTable;
var selectedYear;
var loaded = false;

$(document).ready(function() {
	loadHoliday();
	var yearpickerValue = $(this).val();
	var currentYear = new Date().getFullYear();
	dropDownHideShow();

	// Event handler for yearpicker change event
	$("#yearpicker").on("change", function() {
		if (yearpickerValue > currentYear) {
			$(this).val(currentYear); // Clear the yearpicker input
		} else {
			loadHoliday(yearpickerValue);
		}
	});

	$("#yearpicker").on("keydown", function(event) {
		var inputValue = $(this).val();
		var keyPressed = event.which;

		if (keyPressed >= 48 && keyPressed <= 57) {
			// Only allow numeric keys
			var newValue = inputValue + String.fromCharCode(keyPressed);
			if (parseInt(newValue) > currentYear) {
				event.preventDefault(); // Prevent the key input
			}
		} else {
			event.preventDefault(); // Prevent non-numeric keys
		}
	});

	// Initial loading of Tabulator table with the default year
	var currentYear = new Date().getFullYear(); // Get the current year
	var yearpicker = $("#yearpicker");
	yearpicker.attr("max", currentYear); // Set the maximum value to the current year
	yearpicker.val(currentYear); // Set the default value to the current year
	loadHoliday(currentYear); // Load the Tabulator table with the current year

	//Display Yaer picker From 2005 to current Year
	$("#yearpicker").datepicker({
		format: "yyyy",
		viewMode: "years",
		minViewMode: "years",
		autoclose: true,//to close picker once year is selected
		startDate: "2005", // Set the start date to "2005"
		endDate: "2023",
	});

	//Download Holiday Tabulator
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
					var fileName = "Holiday Master_" + extension + ".xlsx";
					holidayMasterTable.download("xlsx", fileName, {});
				}
			},
			Error: function(msg) {
				window.location.href = _contextPath + 'login';
			}
		});

	});

});

//Onchange of Year
$("#yearpicker").on('change', function(e) {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				loadHoliday();
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
});

//Tabulator Joined Date picker
var joindateEditor = function(cell, onRendered, success, cancel) {
	//create and style input
	var input = document.createElement("input");
	var startYear = 2005;
	var currentYear = new Date().getFullYear();
	input.setAttribute("type", "text");
	input.setAttribute("maxLength", "10");
	input.setAttribute("id", "join_date");
	$(input).css({
		"padding": "4px",
		"width": "100%",
		"boxSizing": "border-box",
	});
	if (cell.getValue() !== null) {
		$(input).val(cell.getValue());
	}
	$(input).datepicker({
		format: 'yyyy-mm-dd',
		startDate: new Date(startYear, 0, 1), // January 1st of startYear
		endDate: new Date(currentYear, 11, 31), // December 31st of currentYear
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
		$('#yearpicker').datepicker('hide');
	});

	$(input).blur(function(e) {
		var newDate = new Date($("#holiday_date").val());
		if (Object.prototype.toString.call(newDate) === "[object Date]") {
			// it is a date
			if (isNaN(newDate.getTime())) {  // d.valueOf() could also work
				// date is not valid
				$("#holiday_date").val("");
			}
		} else {
			// not a date
			$("#holiday_date").val("");
		}
	});
	return input;
};

//Display Holiday Tabulator
function loadHoliday() {
	selectedYear = $("#yearpicker").val();
	var params = {
		"year": selectedYear
	}
	holidayMasterTable = new Tabulator("#holidayMaster-table", {
		height: 421,
		layout: "fitDataStretch",
		selectable: 0,
		ajaxURL: _contextPath + "admin/getAllHolidayMasterDetails", //ajax URL
		ajaxConfig: "get", //ajax HTTP request type
		ajaxParams: params,
		rowSelected: function(row) { enableButtons(row); },
		rowDeselected: function(row) { disableButtons(row); },
		sortOrderReverse: true,
		columnHeaderSortMulti: true,
		tooltips: function(cell) {
			if (cell.getValue() != "" && cell.getValue() != null) {
				return cell.getValue();
			}
		},
		columns: [
			{
				title: holidayDate,
				field: "holiday_date",
				sorter: "date",
				headerFilter: joindateEditor,
				accessorDownload: cellFormater,
				width: "150",
				headerFilterPlaceholder: "YYYY-MM-DD",
				headerSort: false,
				headerClick: function(e, column) {
					customHeaderSort(column);
				}
			},
			{
				title: holidayName,
				field: "holiday_name",
				width: "320",
				headerSort: false,
				accessorDownload: cellFormater,
				headerFilter: "input",
				headerFilterPlaceholder: search + ' ' + holidayName,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},

			{
				title: active,
				field: "active_flg",
				width: "100",
				formatter: "tickCross",
				accessorDownload: cellFormater,
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

}