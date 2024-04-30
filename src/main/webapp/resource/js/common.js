var ipAddress;
var loopIndexVal;
var scrollTopDemo;
var scrollDemo;

/*$("#management").click(function(e){*/
$("#management1_masterMaintenance").click(function(e) {
	dropDownHideShow();
});

//SideNav Master Maintenance Dropdown Hide/Show
function dropDownHideShow() {
	$('#aside').removeClass('sidebar-focused');
	var management1 = document.getElementById('management-one');

	if (management1.style.display == 'block') {
		$("#management-one").css('display', 'none');
		$(".down").css('display', 'none');
		$(".right").css('display', 'block');
	} else {
		$("#management-one").css('display', 'block');
		$(".down").css('display', 'block');
		$(".right").css('display', 'none');
	}
}

//White Spaces and Characters Not allowed for Employee ID
$(function() {
	$("#userId,#user_Idpw,#emp_id").keypress(function(e) {
		var keyCode = e.keyCode || e.which;
		if (keyCode === 13) {
			return true; // Allow Enter key press
		}
		var regex = new RegExp("^[0-9]+$");
		var isValid = regex.test(String.fromCharCode(keyCode));
		if (!isValid) {
			alert("MSG58 : " + MSG58);
		}
		return isValid;
	});

	$("#password,#newpassword,#confirmpassword").keypress(function(e) {
		var keyCode = e.keyCode || e.which;
		if (keyCode == 32) {
			alert("MSG59 : " + MSG59);
			return false;
		}
		$('#password').attr('type', 'password');
		$('#newpassword').attr('type', 'password');
		$('#confirmpassword').attr('type', 'password');
	});

	$("#emp_name").keypress(function(e) {
		var keyCode = e.keyCode || e.which;
		var regex = new RegExp("^[a-zA-Z ]+$");
		var isValid = regex.test(String.fromCharCode(keyCode));
		if (!isValid) {
			alert("MSG61 : " + MSG61);
			return false;
		}
		return isValid;
	});

	$("#emp_id, #emp_name, #userId,#user_Idpw, #password, #newpassword, #confirmpassword").on("paste", function(e) {
		e.preventDefault();
	});

	$(window).keyup(function(e) {
		if (e.keyCode == 44) {
			alert("Print Screen Disabled")
		}
	});
});

//Download empty cell value in Tabulator 
var cellFormater = function(cellValue, data, type, params, column) {
	if (cellValue != null && cellValue != undefined && cellValue != "" && cellValue != "null") {
		return cellValue;
	} else {
		return "";
	}
};

//Checkbox Tick and cross
function customCheckboxFilter(cell, onRendered, success, cancel) {
	var checkbox = document.createElement("input");

	checkbox.setAttribute("type", "checkbox");
	checkbox.setAttribute("id", "active_flg");
	$(checkbox).css({
		"boxSizing": "border-box",
		"margin-top": "8px",
	});
	$(checkbox).data('checked', 1);
	$(checkbox).prop('indeterminate', true);

	$(checkbox).click(function(e) {
		switch ($(checkbox).data('checked')) {
			// unchecked, going indeterminate
			case 0:
				$(checkbox).data('checked', 1);
				$(checkbox).prop('indeterminate', true);
				success(null);
				break;

			// indeterminate, going checked
			case 1:
				$(checkbox).data('checked', 2);
				$(checkbox).prop('indeterminate', false);
				$(checkbox).prop('checked', true);
				success(1);
				break;

			// checked, going unchecked
			default:
				$(checkbox).data('checked', 0);
				$(checkbox).prop('indeterminate', false);
				$(checkbox).prop('checked', false);
				success(0);
		}
	});

	$(checkbox).on("keyup", function(e) {
		if (e.keyCode == 9) {
			cancel();
		}

		if (e.keyCode == 13) {
			cancel();
		}
	});

	$(checkbox).blur(function(e) {
		cancel();
	});
	return checkbox;
}

//To Select Only Excel Files
$("#fileInput").on("change", function() {
	if ($("#fileInput").val() != "") {
		if (this.value.substring(this.value.lastIndexOf('.') + 1) != 'xlsx') {
			alert('MSG30 : ' + MSG30);
			this.value = '';
			$("#btnImport").prop('disabled', true);
			$("#btnImport").removeClass('btn-sm-enabled');
			return;
		}
		$("#btnImport").prop('disabled', false);
		$("#btnImport").addClass('btn-sm-enabled');
	} else {
		$("#btnImport").prop('disabled', true);
		$("#btnImport").removeClass('btn-sm-enabled');
	}
});

//To Upload Excel file onclick of Import
function openFileSelection(type) {
	var urlPath;
	if (type == 'employee') {
		urlPath = _contextPath + "admin/importEmployee";
	} else if (type == 'department') {
		urlPath = _contextPath + "admin/importDepartment";
	} else if (type == 'designation') {
		urlPath = _contextPath + "admin/importDesignation";
	} else if (type == 'leaveMaster') {
		urlPath = _contextPath + "admin/importLeave";
	} else if (type == 'holiday') {
		urlPath = _contextPath + "admin/importHoliday";
	}

	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				document.getElementById("fileInput").addEventListener("change", handleFileSelection);
				document.getElementById("fileInput").click();
				function handleFileSelection(event) {
					var filePath = event.target.value;
					var selectedFileExtn = filePath.substring(filePath.lastIndexOf('.') + 1);
					if (selectedFileExtn === 'xlsx') {
						var uploaded = new FormData();
						uploaded.append("file", event.target.files[0]);
						uploaded.append("selectedFileExtn", selectedFileExtn);
						uploaded.append("application_updated_id", $('#application_updated_id').val());
						if (loaded) return;

						$.ajax({
							url: urlPath,
							mimeType: 'multipart/form-data',
							type: "POST",
							data: uploaded,
							cache: false,
							contentType: false,
							processData: false,
							async: false,
							dataType: 'json',
							success: function(response) {
								switch (response.RESULT) {
									case 'SUCCESS':
										alert(response.MESSAGE + ' : ' + window[response.MESSAGE] + response.infoMsg);
										location.reload();
										break;
									case 'ERROR':
										alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
										location.reload();
										break;
									default:
										alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
								}
							},
						});
						loaded = true;
					}
				}
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
}

//Download excel with filename Extension
function excelFileNameExtension() {
	var currentDate = new Date(); // Get the current date and time
	var year = currentDate.getFullYear();
	var month = String(currentDate.getMonth() + 1).padStart(2, '0');
	var day = String(currentDate.getDate()).padStart(2, '0');
	var hours = String(currentDate.getHours()).padStart(2, '0');
	var minutes = String(currentDate.getMinutes()).padStart(2, '0');
	var seconds = String(currentDate.getSeconds()).padStart(2, '0');
	return [year + month + day + hours + minutes + seconds];
}

//viewBtn and Switch button use same function in allAttendance 
function allAttendanceViewAndSwitch() {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		//dataType : 'json',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				loadAttendance()
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
}


//viewBtn and Switch button use same function in outgoing details
function outgoingViewAndSwitch() {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				AllEmpOutgoingDtls();
				displayEditOptions()
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
}