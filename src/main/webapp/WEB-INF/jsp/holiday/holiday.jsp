<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/sideContainer.jsp"%>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!DOCTYPE html>

<html lang="en" xml:lang="en">
<head>
<meta charset="ISO-8859-1">
<title><spring:message code="login.label.hrms"/></title>
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
	href="${pageContext.request.contextPath}/resource/css/common.css">

<%-- <jsp:include page="../common/header.jsp"></jsp:include> --%>

</head>

<!-- Font Awesome Icons -->

<!-- IonIcons -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/ionicons.min.css">

<style>
.modal {
	display: none; /* Hidden by default */
	position: fixed; /* Stay in place */
	z-index: 1; /* Sit on top */
	padding-top: 100px; /* Location of the box */
	left: 0;
	top: 0;
	width: 100%; /* Full width */
	height: 100%; /* Full height */
	overflow: auto; /* Enable scroll if needed */
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
	background-color: #fefefe;
	margin: auto;
	padding: 15px;
	border: 1px solid #888;
	border-radius: 7px;
	width: 37%;
	height: auto;
}

.close {
	color: #aaaaaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
}
.close:hover, .close:focus {
	color: #000;
	text-decoration: none;
	cursor: pointer;
}
.management1-item5 p {
	color: #fff;
}
#managementNavbar{
	margin-top:3px;
}
.noHover5 > a {
  color: currentColor;
  display: inline-block;  /* For IE11/ MS Edge bug */
  pointer-events: none;
  text-decoration: none;
}
.courseNavbar{
	padding:0.5rem;
}
.scroll-control{
	margin-top:65px;
	padding-top:45px;
}
.dbBtns{
	text-align:right; 
	float:right;
	padding-right:4px;
}
.selectYear{
	height:30px;
	border-radius:5px!important;
}
#forresponsivebuttons{
	padding-left:18px;
}
#yearpicker{
	cursor:pointer!important;
}
</style>
<body class="hold-transition sidebar-mini layout-fixed">
 <%-- <jsp:include page="../common/sideContainer.jsp" /> --%>
	<div class="d-none" id="screenId" data-screenId="HRMS04"></div>

	<div class="wrapper">
		<div class="content-wrapper">
			<nav class="navbar navbar-expand-md navbar-light py-0 courseNavbar"
				style="background-color: #a3a3c2; margin-left: -1px;"
				id="managementNavbar">

				<a class="navbar-brand mt-1">
					<span  class="navText">
						<Strong><spring:message code="topnav.lable.holidaymanagement"></spring:message></Strong>
					</span>
				</a>
			</nav>

			<div id="maincontainerdiv" style="margin-top: 22px;">
				<!-- Main Div -->
				<div class="scroll-control">
					<div class="container-fluid mt-2">
						<div class="row">
							<div class="col-lg-7 col-md-7 " id="forresponsivebuttons">
								<div class="row rows">
									<div class="col-lg-4 col-md-4 col-sm-4">
										<div class="input-group">
											<label><spring:message code="holiday.lable.selectyear"></spring:message>&nbsp;</label>
											<input type="text" class="form-control selectYear white_BG" name="yearpicker" id="yearpicker" readonly="readonly" />
										</div>
									</div>
									<div class="col-lg-8 col-md-8 col-sm-8 dbBtns">
									<button class="btn-sm btn-sm-enabled" type="button" style=""
										id="AddHoliday" hidden><spring:message code="btn.add" /></button>
	
									<button class="btn-sm" type="button" id="ModifyHoliday"
										disabled hidden><spring:message code="btn.modify" /></button>
	
									<button class="btn-sm" type="button" id="DeleteHoliday"
										style="background-color: #F4B183" disabled hidden><spring:message code="btn.delete" /></button>
									
									<button class="btn-sm"
										style="background-color: #8497B0;"
										id="btnDownload">
										<i class="fa fa-download"></i><span style="margin-left: 5px;"><spring:message code="btn.download"></spring:message></span></label>
									</button>
											<button id="btnImportHoliday"
												class="btn-sm btn-sm-util-enabled" type="button" onclick="openFileSelection('holiday')">
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
					<div class="container-fluid mt-2" style="padding-left: 1.4%;">
						<div class="row">
							<div class="col-lg-7 col-md-7 ">
								<div id="holidayMaster-table"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
			<div id="modelpopup"></div>
	</div>
	<%-- <jsp:include page="../common/footer.jsp"></jsp:include> --%>
	<script>
		var holidayDate = '<spring:message code="holiday.label.holidaydate"></spring:message>';
		var holidayName = '<spring:message code="holiday.label.holidayname"></spring:message>';
		var active = '<spring:message code="employee.lable.active"></spring:message>';
		
		var search = '<spring:message code="employee.lable.search"></spring:message>';
	</script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/holiday.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/moment.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.17.4/xlsx.full.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/common.js"></script>


	<!-- Bootstrap -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/lib/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/docs/assets/js/adminlte.js"></script>

	<script>
		$("#management").addClass("menu-is-opening menu-open");
	</script>

</body>
</html>