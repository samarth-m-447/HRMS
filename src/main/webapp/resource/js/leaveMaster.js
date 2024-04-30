//210016
var loaded = false;
var selectedRow;
var leaveMasterTable;

$(document).ready(function() {
	dropDownHideShow();
	loadLeave();

	//Download LeaveMaster Tabulator
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
					var fileName = "Leave Master_" + extension + ".xlsx";

					leaveMasterTable.download("xlsx", fileName, {})
				}
			},
			Error: function(msg) {
				window.location.href = _contextPath + 'login';
			}
		});

	});

});

//Display LeaveMaster Tabulator
function loadLeave() {
	leaveMasterTable = new Tabulator("#leaveMaster-table", {
		height: 421,
		layout: "fitDataStretch",
		selectable: 0,
		ajaxURL: _contextPath + "getAllLeaveMasterDetails", //ajax URL
		ajaxConfig: "get", //ajax HTTP request type
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
				title: leaveId,
				field: "leave_id",
				width: "150",
				headerSort: false,
				headerFilter: "input",
				accessorDownload: cellFormater,
				headerFilterPlaceholder: search + ' ' + leaveId,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},
			{
				title: leaveCode,
				field: "leave_type_code",
				width: "150",
				headerSort: false,
				headerFilter: "input",
				accessorDownload: cellFormater,
				headerFilterPlaceholder: search + ' ' + leaveCode,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},
			{
				title: leaveName,
				field: "leave_type_name",
				width: "320",
				headerSort: false,
				headerFilter: "input",
				accessorDownload: cellFormater,
				headerFilterPlaceholder: search + ' ' + leaveName,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},
			{
				title: active,
				field: "active_flg",
				width: "100",
				formatter: "tickCross",
				align: 'center',
				headerFilter: customCheckboxFilter,
				accessorDownload: cellFormater,
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