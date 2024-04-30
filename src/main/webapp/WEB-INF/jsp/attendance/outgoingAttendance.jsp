<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/sideContainer.jsp"%>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, shrink-to-fit=no, user-scalable=0" />
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<title><spring:message code="login.label.hrms" /></title>
<%-- <jsp:include page="../common/header.jsp"></jsp:include> --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/fallback.css">
<!-- Font Awesome Icons -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/lib/plugins/fontawesome-free/css/all.min.css">
<!-- IonIcons -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/lib/dist/css/adminlte.min.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/home_new.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/common.css">

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/attendance.css">
<style>
#allEmpOutgoingAttendanceLink {
	pointer-events: none;
	text-decoration: none;
	color: #000;
	font-weight: bold;
}

.ml-px-10 {
	margin-left: 10px;
}

.ml-px-16 {
	margin-left: 16px;
}
/* .myBtnDiv{
		height:40px;
	} */
.specailFormDate {
	height: 30px;
	margin-left: 10px;
	width: 140px !important;
}

.allAttendanceSwitch {
	float: right;
	padding: 0px 16px 0px 0px;
}

#checkbox1 {
	top: -1px;
}

.textRight {
	text-align: right;
	float: right;
}

#viewBtn {
	height: 30px !important;
}

.form-control-sm {
	width: 100%;
}

.fa-row-deactive {
	pointer-events: none;
	cursor: text;
}

.fa-circle-minus-active {
	pointer-events: auto !important;
	cursor: pointer;
}

.cursorAuto {
	cursor: auto;
}

.cursorPointer {
	cursor: pointer;
}
</style>
</head>
<body class="hold-transition sidebar-mini layout-fixed excludepage"
	style="background: white !important;">
	<div class="content-wrapper">

		<!-- Attendance List Table -->
		<input type="hidden" id="loggedInUserRole"
			value='<c:out value="${loggedInUserId}"/>'> <input
			type='hidden' id="LoggedInUserName"
			value='<c:out value="${userName }"/>'>
		<%@ include file="/WEB-INF/jsp/common/attendanceNav.jsp"%>
		<br/>
		<br/>
		<div class="col form-inline">
			<div class="row rows ml-px-10">
				<div class="input-group date">
					<label class="float-left"><spring:message
							code="attendance.lable.selectDate"></spring:message></label> <input
						type="text" id="from_date" name="from_date"
						class="form-control specailFormDate cursorPointer white_BG"
						placeholder="YYYY-MM-DD" maxlength="10" readonly>
					<div class="input-group-append cursorPointer" id="calicon">
						<span class="input-group-text"><i
							class="fas fa-calendar-days"></i></span>
					</div>
					<button class="btn-sm btn-sm-enabled ml-2" type="button"
						id="viewBtn" onClick="">
						<spring:message code="btn.view"></spring:message>
					</button>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-11 textRight">
				<c:if test="${loggedInUserRole == '1'}">
					<div class="custom-control custom-switch ">
						<input type="checkbox" class="custom-control-input" id="switch1"
							name="example" style="display: none;"> <label
							class="custom-control-label" for="switch1" id="checkbox1"
							style="color: blue;" title="Self details"> </label> <span
							id="addOutgoingSpanBtn"
							class="addOutgoingSpanBtn addOutgoingSpanBtn_inactive"> <i
							class='fa-solid fa-circle-plus fa-lg text-center'
							style='color: #0e5add; margin-top: 9px;' id='addOutgoing'
							title="Add"> </i>
						</span>
					</div>
				</c:if>
				<c:if test="${loggedInUserRole == '2'}">
					<div class="custom-control custom-switch ">
						<span id="addOutgoingSpanBtn"
							class="addOutgoingSpanBtn addOutgoingSpanBtn_active"> <i
							class='fa-solid fa-circle-plus fa-lg text-center '
							style='color: #0e5add; margin-top: 9px;' id='addOutgoing'
							title="Add"></i>
						</span>
					</div>

				</c:if>
			</div>

			<div class="col-lg-11">
				<div class="ml-px-16" id="AllEmpOutgoingDtlsTable"></div>
				<c:if test="${loggedInUserRole == '1' || loggedInUserRole == '2' }">
					<div style="" id="allEmp_AttendanceListTableDiv"></div>
				</c:if>
				<c:if test="${loggedInUserRole == '1' }">
					<div style="" id="AllEmpOutgoingDtlsTable"></div>
				</c:if>
				<c:if test="${loggedInUserRole == '2' ||  loggedInUserRole == '1'}">
					<button class="btn btn-sm btn-sm-enabled btn-primary mt-2 "
						onClick='submitOutgoingDetailsBtn()' id='submitOutgoingBtn'
						style="width: 130px; margin-left: -2%; display: none;"></button>
					<div style="" id="addOutgoingAttendanceTable"></div>
				</c:if>
			</div>
		</div>
	</div>
	<!-- REQUIRED SCRIPTS -->
	<!-- Bootstrap -->
	<script>
		var employeeId = '<spring:message code="login.label.empid"></spring:message>';
		var employeeName = '<spring:message code="employee.lable.employeename"></spring:message>';
		var punchDate = '<spring:message code="attendance.lable.punchdate"></spring:message>';
		var outgoingFrom = '<spring:message code="outgoing.lable.outgoingfrom"></spring:message>';
		var outgoingIn = '<spring:message code="outgoing.lable.outgoingin"></spring:message>';

		var search = '<spring:message code="employee.lable.search"></spring:message>';
	</script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resource/lib/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- AdminLTE -->
	<script
		src="${pageContext.request.contextPath}/resource/lib/docs/assets/js/adminlte.js"></script>

	<script src="${pageContext.request.contextPath}/resource/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/sideNav.js"></script>
	<script
		src="${pageContext.request.contextPath}/resource/js/outgoingAttendance.js"></script>
	<script>
		$('.line-one-text').find('span').wrapInner('<a/>')
		$('.line-one-text span').wrapInner('<a/>')
	</script>
</body>
</html>