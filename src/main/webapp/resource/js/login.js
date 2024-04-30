/**
 * 161001
 */
$(document).ready(function() {
	// Code to check first login
	var firstLogin = $("#msg").attr('data-isFirstLogin');
	if (firstLogin == "true") {
		jQuery("#firstTimePassword").modal("show");
	}

	var msg = $("#msg").attr('data-msg');
	if (msg != null && msg != "") {
		$("#msg").val("");
	}

	if ($("#invalidText").attr('data-inValidText') == "true") {
		$("#invalid").append('<span style="color: red;">' + MSG94 + '</span>');
	}

	$("#login").on('click', function(e) {
		$('#clearMessage').css('display', 'none');
	});

	$("#loginForm").validate({
		rules: {
			username: {
				required: true,
			},
			password: {
				required: true,
			}
		}, messages: {
			username: 'MSG1 : ' + MSG1,
			password: 'MSG13 : ' + MSG13
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
				alert(errors);
			}
		}, submitHandler: function(form) {
			return true;
		}
	});

	//Display Forgot Password Modal Popup
	jQuery("#forgotPasswordLink").on('click', function(e) {
		$('#clearMessage').css('display', 'none');
		event.preventDefault();
		displayForgotPasswordScreen();
		jQuery.get('hrms/login/getpwForgot', function(data) {
			$("#forgotpwdmodal").clear();
			$("#modelpopup").html('');
		}, 30);
	});

	$("#SendPasswordChangeLink").click(function() {
		var btn_flag = 0;
		if ($('#user_Idpw').val() == null
			|| $('#user_Idpw').val() == "") {
			alert('MSG1 : ' + MSG1);
			$('#user_Idpw').focus();
			return false;
		}
		$("#SendPasswordChangeLink").prop("disabled", true).css("background-color", "#a9d18e");
		//jQuery('#company').val(location.search.split('hDkHdCid=')[1]);
		$("#loaderDiv").removeClass("displayNone");
		$("#loaderDiv").addClass("displayBlock");
		var givenData = {
			emp_id: $("#user_Idpw").val()
		}
		$.ajax({
			type: 'POST',
			url: _contextPath + 'sendForgotPasswordMail',
			data: givenData,
			dataType: 'json',
			success: function(response) {
				$("#loader").css("display", "none");
				setTimeout(function() {
					if (response.givenInput == "INACTIVE") {
						btn_flag = 1;
						alert('MSG18 : ' + MSG18);
						sendPasswordChangeLinkButton(btn_flag);
						return false;
					} else if (response.givenInput == "FIRSTLOGIN") {
						btn_flag = 1;
						alert('MSG19 : ' + MSG19);
						sendPasswordChangeLinkButton(btn_flag);
						return false;
					} else if (response.givenInput == "NOTEXISTS") {
						btn_flag = 1;
						alert('MSG20 : ' + MSG20);
						sendPasswordChangeLinkButton(btn_flag);
						return false;
					} else if (response.givenInput == "LOCKED") {
						btn_flag = 1;
						alert('MSG73 : ' + MSG73);
						$('#user_Idpw').focus();
						sendPasswordChangeLinkButton(btn_flag);
						return false;
					} else if (response.givenInput == "mailNotSent") {
						btn_flag = 1;
						alert('MSG71 : ' + MSG71);
						$('#user_Idpw').focus();
						sendPasswordChangeLinkButton(btn_flag);
						return false;
					} else if (response.givenInput == "mailSent") {
						btn_flag = 0;
						sendPasswordChangeLinkButton(btn_flag);
						$("#mailSentText").css("display", "block");
					} else {
						btn_flag = 1;
						alert("Default Error");
						sendPasswordChangeLinkButton(btn_flag);
					}
					/*$("#forgotpwdmodal").clear();
					$("#modelpopup").html('');*/
				}, 30);

			}
		});

	});
	function sendPasswordChangeLinkButton(btn_flag) {
		if (btn_flag == 0) {
			$("#SendPasswordChangeLink").prop("disabled", true).css("background-color", "#a9d18e");
		}
		else {
			$("#SendPasswordChangeLink").prop("disabled", false).css("background-color", "#548325");
		}
		$("#loaderDiv").removeClass("displayBlock");
		$("#loaderDiv").addClass("displayNone");
		return;
	}

	$("#passwordReset").click(function() {
		if ($("#newpassword").val() == ''
			|| $("#newpassword").val().length < 3) {
			alert('MSG14 : ' + MSG14);
			$("#forgotnewpassword").val("");
			$("#forgotnewpassword").focus();
			return false;
		} else if ($("#newpassword").val() != $("#confirmpassword").val()) {
			alert('MSG15 : ' + MSG15);
			$("#forgotconfirmpassword").val("");
			$("#forgotconfirmpassword").focus();
			return false;
		}

		var resetData = {
			emp_id: $("#user_id").val().trim(),
			password: $("#confirmpassword").val().trim(),
			emp_name: $("#user_name").val(),
			first_login_flg: '0',
		}
		$.ajax({
			type: 'GET',
			url: _contextPath + 'saveForgotPassword',
			data: resetData,
			dataType: 'json',
			success: function(response) {
				if (response.givenInput == "SUCCESS") {
					alert('MSG9 : ' + MSG9);
					window.location.href = _contextPath + "login";
				} else if (response.givenInput == "OLDPASSWORD") {
					alert('MSG57 : ' + MSG57);
					$("#forgotnewpassword").val('');
					$("#forgotconfirmpassword").val('');
					$("#forgotnewpassword").focus();
					return false;
				} else {
					alert('MSG10 : ' + MSG10);
					return false;
				}
			}
		});
	});

	$("#passwordResetClear").click(function() {
		$("#user_Idpw").val('');
		$("#forgotnewpassword").val('');
		$("#forgotconfirmpassword").val('');
		return false;
	});

	$("#closeChangePassword").on('click', function() {
		$("#firstTimePassword").modal("hide");
		$("#newpassword").val('');
		$("#confirmpassword").val('');
		location.reload();
		return false;
	});

	$("#forgotPasswordClose").on('click', function() {
		$("#forgotpwdmodal").modal("hide");
		$("#user_Idpw").val('');
		$("#forgotnewpassword").val('');
		$("#forgotconfirmpassword").val('');
		location.reload();
		return false;
	});

	$("#changePassbtnSubmit").click(function() {

		if ($("#newpassword").val().length > 10
			|| $("#newpassword").val().length < 3) {
			alert('MSG14 : ' + MSG14);
			$("#newpassword").val("");
			$("#newpassword").focus();
			return false;
		} else if ($("#confirmpassword").val().length > 10
			|| $("#confirmpassword").val().length < 3) {
			alert('MSG15 : ' + MSG15);
			$("#confirmpassword").val("");
			$("#confirmpassword").focus();
			return false;
		}
		if ($("#newpassword").val() != $("#confirmpassword").val()) {
			alert('MSG15 : ' + MSG15);
			$("#confirmpassword").focus();
			return false;
		}
		var resetData = {
			emp_id: $("#msg").attr('data-userid'),
			password: $("#confirmpassword").val().trim(),
			first_login_flg: '1',
		}
		$.ajax({
			type: 'GET',
			url: _contextPath + 'saveResetPassword',
			data: resetData,
			dataType: 'json',
			success: function(response) {
				if (response.givenInput == "SUCCESS") {
					alert('MSG9 : ' + MSG9);
					window.location.href = _contextPath + "login";
				} else if (response.givenInput == "OLDPASSWORD") {
					alert('MSG57 : ' + MSG57);
					$("#newpassword").val('');
					$("#confirmpassword").val('');
					return false;
				} else {
					alert('MSG10 : ' + MSG10);
					return false;
				}
			}
		});
	});

	$("#changePassbtnClear").click(function() {
		$("#newpassword").val('');
		$("#confirmpassword").val('');
		return false;
	});

	$("#confirmpassword").keypress(function() {
		var x = document.getElementById("newpassword");
		x.type = "password";
	});
	$("#newpassword").keyup(function() {
		var pswrdVal = $('#newpassword').val()
		var x = document.getElementById("newpassword");
		if (pswrdVal == "" || pswrdVal == null) {
			x.type = "text";
		}

	});
});

//Display popup onclick of Forgot Password link
function displayForgotPasswordScreen() {
	$("#loader").css("display", "block");
	var givenData = {
		emp_id: $("#userId").val()
	}
	jQuery.get('passwordResetPage', givenData, function(data) {
		jQuery("#modelpopup").empty();
		jQuery("#modelpopup").append(data);
		jQuery("#forgotpwdmodal").modal("show");
		var uId = $("#userId").val();
		$("#user_Idpw").val(uId);
	});
}

//Convert text to encrypted password format 
function convertToPassword() {
	var inputField_NP = document.getElementById('newpassword');
	var inputField_CP = document.getElementById('confirmpassword');
	var inputType_NP = inputField_NP.getAttribute('type');
	var inputType_CP = inputField_CP.getAttribute('type');
	if (inputType_NP === 'text') {
		inputField_NP.setAttribute('type', 'password');
	}
	if (inputType_CP === 'text') {
		inputField_CP.setAttribute('type', 'password');
	}
}