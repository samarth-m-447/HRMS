/*210016*/
var selectedRow;
var deptTable;
var loaded = false;

$(document).ready(function() {
	dropDownHideShow();
	loadDepartment();

	//Download Department Tabulator
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
					var fileName = "Department Master_" + extension + ".xlsx";
					deptTable.download("xlsx", fileName, {});
				}
			},
			Error: function(msg) {
				window.location.href = _contextPath + 'login';
			}
		});
	});


});

//Display Department Tabulator
function loadDepartment() {
	deptTable = new Tabulator("#department-table", {
		height: 421,
		layout: "fitDataStretch",
		selectable: 0,
		ajaxURL: _contextPath + "getDepartments", //ajax URL
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
				title: departmentId,
				field: "dept_id",
				width: "160",
				headerSort: false,
				headerFilter: "input",
				accessorDownload: cellFormater,
				headerFilterPlaceholder: search + ' ' + departmentId,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},
			{
				title: departmentName,
				field: "dept_name",
				width: "250",
				headerSort: false,
				headerFilter: "input",
				accessorDownload: cellFormater,
				headerFilterPlaceholder: search + ' ' + departmentName,
				headerClick: function(e, column) {
					headerSort(column);
				}
			},

			{
				title: active,
				field: "active_flg",
				width: "130",
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

			{ field: "updated_time", visible: false },
		],
	});
}