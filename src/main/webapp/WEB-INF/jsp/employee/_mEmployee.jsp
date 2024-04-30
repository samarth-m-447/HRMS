<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/include.jsp"%> 
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/sideContainer.jsp"%>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title><spring:message code="login.label.hrms" /></title>
<link href="${pageContext.request.contextPath}/resource/lib/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resource/lib/tabulator.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resource/lib/bootstrap-datetimepicker.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resource/css/common.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/resource/lib/moment.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/fallback.css">
<!-- Font Awesome Icons -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/lib/plugins/fontawesome-free/css/all.min.css">
<!-- IonIcons -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/lib/dist/css/adminlte.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/home_new.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
<script src="${pageContext.request.contextPath}/resource/lib/moment.js"></script>
<style>
.management1-item1 p {
	color: #fff;
}
#managementNavbar{
	margin-top:3px;
}
.noHover1 > a {
  color: currentColor;
  display: inline-block;  /* For IE11/ MS Edge bug */
  pointer-events: none;
  text-decoration: none;
}
.scroll-control{
	margin-top: 60px;
    padding-top: 50px;  
}
.dbBtns{
    text-align: right;
    padding-right: 57px!important;
 }
 .dbActionBtns{
 	padding-left:15px!important;
 }
 .pr-7_5-px{
	padding-right: 7.5px!important;
 }
 .courseNavbar{
 	padding-left:0.5em!important;
 	background-color: #a3a3c2; 
 	margin-left: -1px!important;
 }
</style>
</head>
<body class="hold-transition sidebar-mini layout-fixed excludepage" style="background: white !important; overflow: hidden;">

	<%-- <jsp:include page="../common/sideContainer.jsp" /> --%>
	<input type="hidden" value="${pageContext.request.contextPath}"
		id="context"></input>
	<div class="content-wrapper">
		<nav class="navbar navbar-expand-md navbar-light py-0 courseNavbar" id="managementNavbar">
			<a class="navbar-brand mt-1">
				<span  class="navText">
				<spring:message code="topnav.lable.empmanagement"></spring:message>
				</span>				
			</a>
		</nav>
		<div id="maincontainerdiv">
			<!-- Main Div -->

			<div class="scroll-control">
				<div class="container-fluid">
					<div class="row">
						<div class="col-lg-12 col-md-12 col-sm-12" id="forresponsivebuttons">
							<div class="row">
							<div class="col-lg-6 col-md-6 col-sm-6 dbActionBtns">
							<button class="btn-sm btn-sm-enabled" type="button" id="addEmployeeBtn">
								<spring:message code="btn.add" />
							</button>

							<button class="btn-sm" type="button" id="ModifyEmployee" disabled>
								<spring:message code="btn.modify" />
							</button>

							<button class="btn-sm" type="button" id="DeleteEmployee" style="background-color: #F4B183" disabled>
								<spring:message code="btn.delete" />
							</button>
							</div>
							<div class="col-lg-6 col-md-6 col-sm-6 dbBtns">
							<button class="btn-sm" style="background-color: #8497B0;" id="btnDownload">
								<i class="fa fa-download"></i><span>&nbsp;<spring:message code="btn.download"></spring:message></span>
							</button>
							<button id="btnImportEmployee" class="btn-sm btn-sm-util-enabled"
								type="button" onclick="openFileSelection('employee')">
								<i class="fa fa-share" style="font-size: 11px;"></i>
								<i class="fa fa-database"></i>&nbsp;
								<spring:message code="btn.import"></spring:message>
								<input type="file" id="fileInput" style="display: none" />
							</button>
							</div>
							</div>
						</div>
					</div>
				</div>

				<!-- Tabulator Table -->
				<div class="container-fluid mt-2" style="padding-left: 1.4%; margin-left: -50px;">
					<div class="row">
						<div class="col-md-12 pr-7_5-px">
							<div class="mt-2 ml-5 employeeGridDiv" id="employeeTable"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container-wrapper"
		style="margin-left: 15%; margin-top: 5%;">
		<div id="modelpopup"></div>
	</div>
	
	<!--======================================================-->
	<script>
		var employeeId = '<spring:message code="login.label.empid"></spring:message>';
		var employeeName = '<spring:message code="employee.lable.employeename"></spring:message>';
		var email = '<spring:message code="employee.lable.email"></spring:message>';
		var microsoftEmail = '<spring:message code="employee.lable.office365mail"></spring:message>';
		var contactNumber = '<spring:message code="employee.lable.contactnumber"></spring:message>';
		var employeeType = '<spring:message code="employee.lable.employeetype"></spring:message>';
		var employeeCategory = '<spring:message code="employee.lable.employeelevel"></spring:message>';
		var joinedDate = '<spring:message code="employee.lable.empjoineddate"></spring:message>';
		var department = '<spring:message code="employee.lable.empdepartment"></spring:message>';
		var designation = '<spring:message code="employee.lable.empdesignation"></spring:message>';
		var role = '<spring:message code="employee.lable.role"></spring:message>';
		var active = '<spring:message code="employee.lable.active"></spring:message>';
		
		var search = '<spring:message code="employee.lable.search"></spring:message>';
	</script>
	
	<script src="${pageContext.request.contextPath}/resource/lib/tabulator.min.css"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.17.4/xlsx.full.min.js"></script>
	<script type='text/javascript' src="https://rawgit.com/RobinHerbots/jquery.inputmask/3.x/dist/jquery.inputmask.bundle.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/inputmask.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/_mEmployee.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/lib/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/docs/assets/js/adminlte.js"></script>
</body>
</html>