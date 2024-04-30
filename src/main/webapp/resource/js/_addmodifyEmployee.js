/*210016
*/
var mobilePattern = /^(0|[+91]{3})?[7-9][0-9]{9}$/;
var empIdPattern = /^[0-9]+$/;
var mailPattern = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
var onmicrosoftPattern = /^\w+([\.-]?\w+)*@[\w-]+(\.onmicrosoft\.com)$/i;
var empNamePattern = /^[a-zA-Z]+$/;
var form = true;
var tempDirectoryPath;
var imageFilePath;
var mainFilePath;
var filename;
var OSName;
var ptmsAdminFlg;
var isFunctionCalled = false;
var loggedInEmpRole;
var loggedInUserId;
var editUserId;
var startYear = 2005;

$(document).ready(function() {
	loggedInEmpRole = $("#loggedInUserRole").val();
	loggedInUserId = $("#empId").val();
	editUserId = $("#emp_id").val();
	if (loggedInUserId == editUserId) {
		$("#emp_role").prop('disabled', true);
	}

	$("#emp_contact_Number").on('input', function(e) {
		var input = $("#emp_contact_Number").val();
		var letterNumber = /^[0-9]+$/;
		if (input.match(letterNumber)) {
			return true;
		}
		else {
			$("#emp_contact_Number").val("");
			return false;
		}
	});

	$("#closeEmployee").on('click', function(e) {
		$("#addEmployeeModal").modal("hide");
		disableButtons();
	});

	$("#employeeForm").validate({
		rules: {
			emp_id: "required",
			emp_name: "required",
			emp_contact_Number: {
				minlength: 10
			},
			office_email: {
				required: true,
				office_email: true,
			},
			office_email_365: {
				required: true,
				office65_email: true,
			},
			emp_type: "required",
			emp_role: "required",
			emp_designation: "required"

		}, messages: {
			emp_id: 'MSG1 : ' + MSG1,
			emp_name: 'MSG13 : ' + MSG2,
			emp_contact_Number: 'MSG101 : ' + MSG101,
			office_email: 'MSG60 : ' + MSG60,
			office_email_365: 'MSG31 : ' + MSG31,
			emp_type: 'MSG95 : ' + MSG95,
			emp_role: 'MSG75 : ' + MSG75,
			emp_designation: 'MSG99 : ' + MSG99
		}, errorPlacement: function(error, element) {
			return false;
		}, invalidHandler: function(form, validator) {
			var number = validator.numberOfInvalids();
			if (number) {
				var errors = '';
				if (validator.errorList.length > 0) {
					for (x = 0; x < validator.errorList.length; x++) {
						errors += '\n' + validator.errorList[x].message;
					}
				}
			}
		}, submitHandler: function(form) {
			return true;
		}
	});

})

//On choose file button Click
$("#inputImageUploadBtn").on('change', function(e) {
	var fileName = e.target.files[0].name;
	var selectedFile = $('#inputImageUploadBtn')[0].files[0];
	var uploaded = new FormData();
	var selectedImageFileExtn = fileName.substring(fileName.lastIndexOf('.') + 1);
	uploaded.append("file", selectedFile);
	uploaded.append("selectedFileExtn", selectedImageFileExtn);
	var urlPath = _contextPath + 'uploadFile';
	$.ajax({
		url: urlPath,
		mimeType: 'multipart/form-data',
		type: "POST",
		method: "POST",
		data: uploaded,
		cache: false,
		contentType: false,
		processData: false,
		dataType: 'json',
		success: function(response) {
			setTimeout(function() {
				$("#tempDirectoryPath").val(response.tempDirectoryPath);
				$("#uploadFileResult").val(response.uploadFileResult);
			}, 30);
		},
		error: setTimeout(function() {
			alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
			$("#addEmployeeModal").modal("show");
		}, 30),
	});
	previewImageFile()
});

//Remove Choose file Image
function removImage() {
	isFunctionCalled = true;
	document.getElementById("img").src = "";
	document.querySelector('input[type=file]').files[0] = "";
	$("#inputImageUploadBtn").val('');
	$("#updatedProfile").val('');
	$("#imgDiv").css("display", "none");
	$("#updatedProfile").css("display", "block");
}

//Display choosed Image 
function previewImageFile() {
	var file = $('#inputImageUploadBtn')[0].files[0];
	var fileType = file["type"];
	const validImageTypes = ['image/jpg', 'image/jpeg', 'image/png'];
	var reader = new FileReader();
	reader.onloadend = function() {
		imageTypeValidation(file, fileType, validImageTypes, reader);
	}
	if (file) {
		reader.readAsDataURL(file);
	} else {
		document.getElementById("img").src = "";
	}
}

//Add/Modify Employee Pop-Up close
$("#empFormCancelBtn").click(function() {
	location.reload();
})

//Joined Date Calender
$('#calicon1').click(function() {
	$('#emp_joined_date').datepicker('show');
});

$('#emp_joined_date').on('changeDate', function(selected) {
	var minimumDate = new Date($('#emp_joined_date').val());
});

//Display Date picker From 01-Jan-2005 to current date
$('#emp_joined_date').datepicker({
	constrainInput: "true",
	format: 'yyyy-mm-dd',
	startDate: new Date(startYear, 0, 1), // January 1st of startYear
	endDate: '+0d',
	autoclose: true
});

//Tabulator Joined Date picker
var joindateEditor = function(cell, onRendered, success, cancel) {
	//create and style input
	var input = document.createElement("input");
	input.setAttribute("type", "text");
	input.setAttribute("maxLength", "10");
	input.setAttribute("id", "emp_joined_date");
	$(input).css({
		"padding": "4px",
		"width": "100%",
		"boxSizing": "border-box",
		"outline-offset": "0px"
	});

	$(input).datepicker({
		format: 'yyyy/mm/dd',
		autoclose: true,
		pickTime: false,
	}).on('changeDate', function(selected) {
		var newDate = new Date(selected.date.valueOf());
		success(moment(newDate).format("YYYY/MM/DD"));
	});

	$(input).inputmask({
		mask: '9999/99/99',
		showMaskOnHover: false,
		showMaskOnFocus: false,
	});

	$(input).click(function(e) {
		$(input).css("outline-offset", "0px");
		$(input).css("outline-color", "#007bff");
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

//Add Employee Save Function 
$("#empSaveBtn").click(function(e) {
	e.preventDefault();
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {

				if ($("#employeeForm").valid()) {

					$.ajax({
						url: _contextPath + 'admin/saveEmployee',
						method: 'POST',
						data: getEmployeeParams(),
						dataType: 'json',
						success: function(response) {
							switch (response.RESULT) {
								case 'SUCCESS':
									setTimeout(function() {
										alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
										$("#addEmployeeModal").modal("hide");
										location.reload();
									}, 30);
									break;
								case 'ERROR':
									setTimeout(function() {
										alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
										$("#addEmployeeModal").modal("show");
									}, 30);
									break;
							}
						},
						error: function(request, status, error) {
							setTimeout(function() {
								alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);
							}, 30);
						}

					}); // ajax closed
				}
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});


});

//Modify Employee Save
$("#empUpdateBtn").click(function(e) {
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				if ($("#employeeForm").valid()) {
					$("#empUpdateBtn").prop("disabled", true);

					$.ajax({
						url: 'updateEmployee',
						method: 'POST',
						data: getEmployeeParams(),
						dataType: 'json',
						success: function(response) {

							switch (response.RESULT) {
								case 'SUCCESS':
									setTimeout(function() {
										alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);//Employee updated successfully.
										$("#addEmployeeModal").modal("hide");
										location.reload();
									}, 30);

									break;
								case 'ERROR':
									setTimeout(function() {
										$("#empUpdateBtn").prop("disabled", false);
									}, 10);
									break;
								default:
									setTimeout(function() {
										$("#empUpdateBtn").prop("disabled", false);
									}, 10);
							}
						}
					});
				}
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});
});

//Delete Employee
$("#btnDeleteEmployee").click(function(e) {
	var params = {
		'emp_id': $('#emp').val().trim(),
	};
	$.ajax({
		async: false,
		type: 'get',
		url: _contextPath + 'checkSessionTimeout',
		success: function(response) {
			if (response.session == 'false' || response.session == undefined) {
				window.location.href = _contextPath + 'login';
			} else {
				$.ajax({
					url: 'deleteEmployee',
					method: 'POST',
					data: params,
					dataType: 'json',
					success: function(response) {

						switch (response.RESULT) {
							case 'SUCCESS':
								alert(response.MESSAGE + ' : ' + window[response.MESSAGE]);//Employee delete successfully.
								disableButtons();
								jQuery("#DeleteEmployeeModel").modal("hide");
								location.reload();
								break;
							case 'ERROR':
								break;
							default:
								alert(response.MESSAGE + ' : ' + window[response.MESSAGE])
						}
					}

				});
			}
		},
		Error: function(msg) {
			window.location.href = _contextPath + 'login';
		}
	});

});

function onClickAddEmployeeClose() {
	location.reload();
}

//Pass all parameters to Add and Modify 
function getEmployeeParams() {
	var imgPathValue = $("#imgPath").text().trim();
	imageFilePath = $('#inputImageUploadBtn').val();
	tempDirectoryPath = $("#tempDirectoryPath").val();
	var emp_id = $("#emp_id").val().trim();
	OSName = $("#operatingSystem").val();
	if ((isFunctionCalled && imageFilePath == null) || imageFilePath === '' || imageFilePath === undefined) {
		$("#inputImageUploadBtn").val("");
		filename = imgPathValue.substring(imgPathValue.lastIndexOf('\\') + 1);
		mainFilePath = imgPathValue;

	} else {
		filename = imageFilePath.substring(imageFilePath.lastIndexOf('\\') + 1);
		var folderName = $("#uploadFileResult").val();
		if (OSName == "windows") {
			mainFilePath = emp_id + '\\' + folderName;
		}
		if (OSName == "linux") {
			mainFilePath = emp_id + '/' + folderName;
		}

	}
	var params = {
		'emp_id': $('#emp_id').val().trim(),
		'emp_name': $('#emp_name').val().trim(),
		'office_email_365': $("#office_email_365").val(),
		'office_email': $("#office_email").val().trim(),
		'contact_number': $("#emp_contact_Number").val().trim(),
		'reporting_leader_id': $("#emp_leader_id").find(":selected").val(),
		'emp_contact_Number': $("#emp_contact_Number").val(),
		'emp_type': $('#emp_type').find(":selected").val(),
		'emp_level': $('#emp_level').find(":selected").val(),
		'emp_joined_date': $("#emp_joined_date").val(),
		'role_id': $('#emp_role').find(":selected").val(),
		'dept_id': $("#emp_department").find(":selected").val(),
		'desg_id': $("#emp_designation").find(":selected").val(),
		'tempDirectoryPath': tempDirectoryPath,
		'file_name': filename,
		'emp_image_path': mainFilePath
	}
	if (isUpdate == true) {
		var userLockFlg;
		var checkbox = document.getElementById("ptmsAdmin");
		var checkbox2 = document.getElementById("userLockFlag");

		if (checkbox.checked) {
			ptmsAdminFlg = 1;
		} else {
			ptmsAdminFlg = 0;
		}
		if (checkbox2.checked) {
			userLockFlg = 1;
		} else {
			userLockFlg = 0;
		}
		$.extend(params, {
			"ptms_admin_flg": ptmsAdminFlg,
			"user_lock_flg": userLockFlg
		});

	}
	return params;
}

//function is basically applied for fiff image and image file validations
function imageTypeValidation(file, fileType, validImageTypes, reader) {
	if (!validImageTypes.includes(fileType) || (fileType === 'image/jpeg' && file.name.toLowerCase().endsWith('.jfif'))) {
		$("#imgDiv").css("display", "none");
		$("#updatedProfile").css("display", "block");
		$("#inputImageUploadBtn").val('');
		alert("MSG21 : " + MSG21)
	}
	else {
		document.getElementById("img").src = reader.result;
		$("#imgDiv").css("display", "block");
		$("#updatedProfile").css("display", "none");
	}

}

//# sourceURL=_addmodifyEmployee.js