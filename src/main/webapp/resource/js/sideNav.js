$(document).ready(function(){
	var loggedInEmployeeRole = $("#loggedInUserRole").val();
	$("#allEmpAttendanceLink").click(function(){
		$("#loader").show();
		window.location.href = _contextPath + 'user/viewAttendance';
	});
	$("#allEmpOutgoingAttendanceLink").click(function(){
		$("#loader").show();
		window.location.href = _contextPath + 'user/viewOutgoing';
	});
	$("#item-1").click(function(){
		$("#loader").show();
		window.location.href = _contextPath+'admin/getEmpManagement';
		
	});
	$("#item-2").click(function() {
		$("#loader").show();
		window.location.href = _contextPath + 'admin/Department';
	});
	$("#item-3").click(function() {
		$("#loader").show();
		window.location.href = _contextPath + 'admin/Designation';
	});
	$("#item-4").click(function() {
		$("#loader").show();
		window.location.href = _contextPath + 'admin/getLeaveMaster';
	});
	$("#item-5").click(function() {
		$("#loader").show();
		window.location.href = _contextPath + 'admin/getHolidayMaster';
	});
});

$(function () {
    $('body').show();
});
