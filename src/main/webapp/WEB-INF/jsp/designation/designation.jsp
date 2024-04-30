<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/sideContainer.jsp"%>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
<meta charset="ISO-8859-1">
<title><spring:message code="login.label.hrms" /></title>
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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/department.css">
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
.management1-item3 p {
	color: #fff;
}
#managementNavbar{
	margin-top:3px;
}
.noHover3 > a {
  color: currentColor;
  display: inline-block;  /* For IE11/ MS Edge bug */
  pointer-events: none;
  text-decoration: none;
}
.courseNavbar{
	padding:0.5rem;
}
.dbBtns{
	text-align:right; 
	float:right;
	padding-right:3px;
	margin-top:10px;
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
						<Strong><spring:message code="topnav.lable.desgmanagement"></spring:message></Strong>
					</span>
				</a>
			</nav>
		
			<div id="maincontainerdiv">
				<!-- Main Div -->

				<div class="scroll-control">
					<div class="container-fluid mt-2">
						<div class="row">
							<div class="col-lg-6  col-md-6 dbBtns" id="forresponsivebuttons"
								style="">
								<button class="btn-sm btn-sm-enabled" type="button"
									id="AddDesignation" hidden><spring:message code="btn.add" /></button>

								<button class="btn-sm" type="button" id="ModifyDesignation"
									disabled hidden><spring:message code="btn.modify" /></button>

								<button class="btn-sm" type="button" id="DeleteDesignation"
									style="background-color: #F4B183" disabled hidden><spring:message code="btn.delete" /></button>

								<button class="btn-sm" style="background-color: #8497B0;"
									id="btnDownload">
									<i class="fa fa-download"></i><span>&nbsp;<spring:message code="btn.download"></spring:message></span></label>
								</button>

								<button id="btnImportDesignation"
									class="btn-sm btn-sm-util-enabled" type="button" onclick="openFileSelection('designation')">
									<i class="fa fa-share" style="font-size: 11px;"></i>
									<i class="fa fa-database"></i>&nbsp;
									<spring:message code="btn.import" ></spring:message>
								 <input type="file" id="fileInput" style="display: none" />
								</button>

							</div>
						</div>
					</div>
					<!-- Tabulator Table -->
					<div class="container-fluid mt-2" style="padding-left: 1.4%;">
						<div class="row">
							<div class="col-lg-6  col-md-6 ">
								<div id="Designation-table"></div>
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
		var designationId = '<spring:message code="designation.label.designationid"></spring:message>';
		var designationName = '<spring:message code="designation.label.designationname"></spring:message>';
		var active = '<spring:message code="employee.lable.active"></spring:message>';
		
		var search = '<spring:message code="employee.lable.search"></spring:message>';
	</script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/designation.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/moment.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/common.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.17.4/xlsx.full.min.js"></script>

	<!-- Bootstrap -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/lib/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/docs/assets/js/adminlte.js"></script>

</body>
</html>