<head>
<title><spring:message code="login.label.hrms"/></title>
<style>
	a {
		color: white;
	}
	
	a:hover {
		color: white;
	}
	
	.navbar-brand {
		padding-bottom: 0px !important;
	}
	
	.navbar {
		margin-left: 0%;
	}
	
	@media ( min-width :1920px) {
		#managementNavbar {
			margin-top: 0px !important;
		}
	}
	
	@media ( max-width : 1025px) {
		#topiclink {
			margin-left: 0px !important;
		}
	}
/* 	.navText{
		font-size: 16px;
		color:white;
		font-weight:bold;
		margin-left:10px;
	} */
	.navbarULMargin{
		margin:5px 0px 0px 30px
	}
	.attendenceNavLink{
		display: inline-block;
		padding-top:6px;
		padding-bottom:3px;
							  
	}
</style>
</head>
<body class="hold-transition sidebar-mini layout-fixed excludepage"	style="background: white !important; overflow: hidden;">													 

	<nav class="navbar navbar-expand-md navbar-light py-0 courseNavbar " style="background-color: #a3a3c2; margin-left: -1px;" id="managementNavbar">
		<a class="navbar-brand">
			<span class="navText">						
				<spring:message	code="attendancenav.lable.attendanceInformation"></spring:message> 
			</span>
		</a>

		<div class="navbar-collapse courseNav " id="navbarNav2">

			<ul class="navbar-nav navbarULMargin">
				<li class="nav-item">
					<span class="nav-link attendenceNavLink">
									   
						<a href="${pageContext.request.contextPath}/user/viewAttendance" id="allEmpAttendanceLink">
											   
							<spring:message	code="attendancenav.lable.attendanceDetails"></spring:message>
						</a>
					</span>
				</li>
				<li class="nav-item">
					<span class="nav-link attendenceNavLink">
									   
						<a href="${pageContext.request.contextPath}/user/viewOutgoing" id="allEmpOutgoingAttendanceLink">
													   
							<spring:message	code="attendancenav.lable.outgoingDetails"></spring:message>
						</a>
					</span>
				</li>
			</ul>
		</div>

	</nav>
</body>