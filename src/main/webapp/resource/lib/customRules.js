
$(document).ready(function(e) {
	$.validator.setDefaults({
		onclick : false,
		onfocusout : false,
		onkeyup : false,
		errorClass : "errorClass",
		highlight : function(element, errorClass) {
			$(element).addClass(errorClass);
		},
		unhighlight : function(element, errorClass) {
			$(element).removeClass(errorClass);
		},
		showErrors : function(errorMap, errorList) {
			var errorMessage = new Array();
			for (var i = 0; i < errorList.length; i++) {
				errorMessage.push( errorList[i].message);
			}
			if (errorMessage.length > 0) {
				alert(errorMessage.join("\n"));
			}
		}
	});

	$.validator.addMethod("required", function(value, element, params) {
		return $.trim(value) != "";
	}, $.validator.format("{0} is mandatory."));
	
	$.validator.addMethod('office_email', function (value) { 
    return /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(value); 
});
$.validator.addMethod('office65_email', function (value) { 
    return /^\w+([\.-]?\w+)*@[\w-]+(\.onmicrosoft\.com)$/i.test(value); 
});
	$.validator.addMethod("maxlen", function(value, element, params) {
		if(params[2]==0)
		{	
			return true;
		}
			return this.optional(element) || value.length <= params[0];
		}, $.validator.format("Max {0} character can be entered for {1}."));
	
	$.validator.addMethod("minlen", function(value, element, params) {
		if(params[2]==0)
		{	
			return true;
		}

		return this.optional(element) || value.length >= params[0];
		}, $.validator.format("Min {0} character must be entered for {1}."));

	$.validator.addMethod("date", function(value) {
		if (value.trim() == "" || isValidDate(value, window.top.appDateDispFormat)) {
			return true;
		}
		return false;
	});

	$.validator.addMethod("compareDate", function(value, element, param) {
		if (compareDates(value, $(param[0]).val(), window.top.appDateDispFormat, param[1])) {
			return true;
		}
		return false;
	});

	$.validator.addMethod("checkStatus", function(value, element, param) {
		if(value.trim() == '' && $(param).val() == 'D05')
			return false;
		return true;
	});

	$.validator.addMethod("checkActlEndDate", function(value, element, param) {
		if(value.trim() != '100' && $(param).val() != '')
			return false;
		return true;
	});

	//Start date end date comparison so that start should be earlier date and end date should be latest date 
	 $.validator.addMethod("greaterThan", function(value, element, params) {
		var dateParts = '';
		var startDateStr = $(params).val();
		if(startDateStr == '' || value == '') return true;
		if (value.indexOf('/') != -1) {
			dateParts = value.split("/");
		} else if (dateFormat.indexOf('-') != -1) {
				dateParts = value.split("-");		
		}
		
		var endDate = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
		dateParts='';
		if (startDateStr.indexOf('/') != -1) {
			dateParts = startDateStr.split("/");
		} else if (dateFormat.indexOf('-') != -1) {
				dateParts = startDateStr.split("-");		
		}
		 var startDate = new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
		 return startDate <= endDate;
	});
});

function isValidDate(myValue, dateFormat) {
	var arrFormat;
	var arrMyValue;
	var myValDay;
	var myValMonth;
	var myValYear;

	if (dateFormat.indexOf('/') != -1) {
		arrMyValue = myValue.split("/");
		arrFormat = dateFormat.split("/");
	} else if (dateFormat.indexOf('-') != -1) {
		arrMyValue = myValue.split("-");
		arrFormat = dateFormat.split("-");
	}

	// Get the Day, Month, Year of two dates
	$.each(arrFormat, function(index, element) {
		if (element.toUpperCase().indexOf('D') != -1) {
			myValDay = arrMyValue[index];
		}
		if (element.toUpperCase().indexOf('M') != -1) {
			myValMonth = checkForMonth(arrMyValue[index]);
		}
		if (element.toUpperCase().indexOf('Y') != -1) {
			myValYear = arrMyValue[index];
		}
	});
	--myValMonth;
	if (myValMonth >= 0 && myValMonth <= 11 && myValDay >= 1 && myValDay <= 31) {
		var vDt = new Date(myValYear, myValMonth, myValDay);
		if (isNaN(vDt)) {
			return false;
		} else if (vDt.getFullYear() == myValYear && vDt.getMonth() == myValMonth && vDt.getDate() == myValDay) {
			return true;
		} else {
			return false;
		}
	} else {
		return false;
	}

}

function compareDates(myValue, compareValue, dateFormat, operatorType) {

	var arrFormat;
	var arrMyValue;
	var arrCompareValue;
	var myValDay;
	var compareValDay;
	var myValMonth;
	var compareValMonth;
	var myValYear;
	var compareValYear;

	if (dateFormat.indexOf('/') != -1) {
		arrMyValue = myValue.split("/");
		arrCompareValue = compareValue.split("/");
		arrFormat = dateFormat.split("/");
	} else if (dateFormat.indexOf('-') != -1) {
		arrMyValue = myValue.split("-");
		arrCompareValue = compareValue.split("-");
		arrFormat = dateFormat.split("-");
	}

	// Get the Day, Month, Year of two dates
	$.each(arrFormat, function(index, element) {
		if (element.toUpperCase().indexOf('D') != -1) {
			myValDay = arrMyValue[index];
			compareValDay = arrCompareValue[index];
		}
		if (element.toUpperCase().indexOf('M') != -1) {
			myValMonth = checkForMonth(arrMyValue[index]);
			compareValMonth = checkForMonth(arrCompareValue[index]);
		}
		if (element.toUpperCase().indexOf('Y') != -1) {
			myValYear = arrMyValue[index];
			compareValYear = arrCompareValue[index];
		}
	})

	var myDate = new Date(myValYear, parseInt(myValMonth) - 1, myValDay);
	var compareDate = new Date(compareValYear, parseInt(compareValMonth) - 1, compareValDay);

	switch (operatorType) {
		case "LessThan":
			if (myDate < compareDate) {
				return true;
			}
			break;
		case "GreaterThan":
			if (myDate > compareDate) {
				return true;
			}
			break;
		case "LessThanEqual":
			if (myDate <= compareDate) {
				return true;
			}
			break;
		case "GreaterThanEqual":
			if (myDate >= compareDate) {
				return true;
			}
			break;
		case "Equal":
			if (myDate.getTime() == compareDate.getTime()) {
				return true;
			}
			break;
		case "NotEqual":
			if (myDate.getTime() != compareDate.getTime()) {
				return true;
			}
			break;
		default:
			return false;
			break;
	}
	return false;
}

function checkForMonth(month) {
	switch (month.toUpperCase()) {
		case "01":
		case "JAN":
		case "JANUARY":
			return "01";
			break;
		case "02":
		case "FEB":
		case "FEBRUARY":
			return "02";
			break;
		case "03":
		case "MAR":
		case "MARCH":
			return "03";
			break;
		case "04":
		case "APR":
		case "APRIL":
			return "04";
			break;
		case "05":
		case "MAY":
			return "05";
			break;
		case "06":
		case "JUN":
		case "JUNE":
			return "06";
			break;
		case "07":
		case "JUL":
		case "JULY":
			return "07";
			break;
		case "08":
		case "AUG":
		case "AUGUST":
			return "08";
			break;
		case "09":
		case "SEP":
		case "SEPTEMBER":
			return "09";
			break;
		case "10":
		case "OCT":
		case "OCTOBER":
			return "10";
			break;
		case "11":
		case "NOV":
		case "NOVEMBER":
			return "11";
			break;
		case "12":
		case "DEC":
		case "DECEMBER":
			return "12";
			break;
		default:
			return month;
			break;
	}
	return month;
}

//# sourceURL=customRules.js
