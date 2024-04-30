//210016
var selectedRow;
var deptTable;
var loaded = false;

$(document).ready(function() {
	dropDownHideShow();
	loadDesignation();

	//Download Designation Tabulator
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
					var fileName = "Designation Master_" + extension + ".xlsx";
					desgTable.download("xlsx", fileName, {});
				}
			},
			Error: function(msg) {
				window.location.href = _contextPath + 'login';
			}
		});

	});
});

//Display Designation Tabulator
function loadDesignation() {
	desgTable = new Tabulator("#Designation-table", {
		tooltips: true,
		height: "421px",
		layout: "fitDataStretch",
		selectable: 0,
		ajaxURL: _contextPath + "getDesignation", //ajax URL
		ajaxConfig: "get", //ajax HTTP request type
		persistence: {
			sort: true,
		},
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
				title: designationId,
				field: "desg_id",
				width: "170",
				headerSort: false,
				headerFilter: "input",
				accessorDownload: cellFormater,
				headerFilterPlaceholder: search + ' ' + designationId,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},
			{
				title: designationName,
				field: "desg_name",
				width: "240",
				headerSort: false,
				accessorDownload: cellFormater,
				headerFilter: "input",
				headerFilterPlaceholder: search + ' ' + designationName,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},
			{
				title: active,
				field: "active_flg",
				width: "120",
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

			{ field: "updated_time", visible: false },
		],
	});
}