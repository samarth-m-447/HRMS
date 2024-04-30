<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" xml:lang="en">
<head>
	<title><spring:message code="login.label.hrms" /></title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/sideContainer.css">
</head>

<style>
body {
    display: none;
}
#hamburgerMenuIcon{
	padding-top: 6px;
	margin-left: -10px;
}
</style>
<body>
	<div class="sidebar-menu">
		<input type="hidden" id='loggedInUser' value='${loggedInUserId}'>
		<input type="hidden" id='loggedInUserRole' value='${loggedInUserRole}'>
		<input type="hidden" id='loggedInUserImage' value='${profileImage}'>	
		<input type="hidden" id="locale" value="${pageContext.response.locale}" />
		
		<nav class="main-header navbar navbar-expand navbar-dark pl-0"> 
			<a class="brand-link-mobile" style="visibility:hidden" >
				<img src="${pageContext.request.contextPath}/resource/images/scii.png" class="mobilescreenlogo" alt="HRMS Application image" width="40px" height="25" style="opacity: 9.8; margin-left: 18px; margin-right: 5px;">
			</a> 
			<!-- Left navbar links -->
			<ul class="navbar-nav">
				<li class="nav-item" id="togglebtn">
					<a class="nav-link pl-0" data-widget="pushmenu" role="button">
						<i class="fas fa-bars" id="hamburgerMenuIcon"></i>
					</a>
				</li>	
			</ul>	
			<!-- Right navbar links -->
			<ul class="navbar-nav ml-auto">		
				<li class="nav-item ">
					<a class="nav-link dividmedia pr-0" role="button">
						<span class="nav-link pt-0 cursorAuto">
							<c:set var="userName" value="${userName}" />	
							<img class="emp-image-circle" src="${pageContext.request.contextPath}/externalImages/${profileImage}"
							 onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/resource/images/default.png'" alt=""/> ID : ${loggedInUserId} &nbsp;&nbsp;${fn:toUpperCase(userName)}
				</span> 
					</a>
				</li>				
				<!-- Messages Dropdown Menu -->
				<li class="nav-item">
					<a href="${pageContext.request.contextPath}/logout" class="nav-link pl-0" id="logout" style="cursor: pointer;"> 
						<img src="${pageContext.request.contextPath}/resource/images/2.jpeg" width="30" height="30" alt="Logout"/>
					</a>
				</li>
			</ul>
		</nav>
		
		<!-- Main Sidebar Container -->
		<aside class="main-sidebar sidebar-dark-primary elevation-4" id="aside">
			<!-- Brand Logo --> 
			<a href="${pageContext.request.contextPath}/user/home" class="brand-link border-b-0" style="margin-left: -5px;margin-bottom: -3px;  cursor: pointer;"> 
				<img src="${pageContext.request.contextPath}/resource/images/scii.png" alt="HRMS Application image" width="40px" height="30" class="sideBarAppImage"> 
				<span class="brand-text font-weight-light" style="cursor: pointer;">
					<spring:message code="login.label.hrms" />
				</span>
			</a> 
		
			<!-- Sidebar -->

			<div class="sidebar">
			<!-- Sidebar Menu -->
				<nav class="mt-2">
					<ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false" style="cursor: pointer;">
						<!-- Add icons to the links using the .nav-icon class
		               with font-awesome or any other icon font library -->
		
						<li class="nav-item" id="home">
							<a href="${pageContext.request.contextPath}/user/home" class="nav-link">
								<i class="fa-solid fa-house fa-xl sideNavIcon"></i>
								<span class='sideNavText'><spring:message code="topnav.lable.home"></spring:message></span>
							</a>
						</li>
					</ul>			
					<%-- <ul class="nav nav-pills nav-sidebar flex-column"
						data-widget="treeview" role="menu" data-accordion="false" style="cursor: pointer;">
						<!-- Add icons to the links using the .nav-icon class
		               with font-awesome or any other icon font library -->
		
						<li class="nav-item" id="leave">
							<sec:authorize access="hasAnyAuthority('ROLE_USERADMIN','ROLE_USER')">
							<a  href="${pageContext.request.contextPath}/leave/my_leave?emp_id=${loggedInUserId}"
								class="nav-link"> <i class="fa fa-home" aria-hidden="true"></i>
								<spring:message code="topnav.lable.leave"></spring:message>
							</a>
								<a  href="${pageContext.request.contextPath}/user/getLeaveMgmt"	class="nav-link">
									<i class="fa-solid fa-business-time fa-xl sideNavIcon"></i>
									<span class='sideNavText'><spring:message code="topnav.lable.leave"></spring:message></span>
								</a>
							</sec:authorize>
						</li>
					</ul> --%>
					
					<ul class="nav nav-pills nav-sidebar flex-column"
						data-widget="treeview" role="menu" data-accordion="false" style="cursor: pointer;">
						<!-- Add icons to the links using the .nav-icon class
		               with font-awesome or any other icon font library -->
		
						<li class="nav-item" id="attendance">
							<sec:authorize access="hasAnyAuthority('ROLE_USERADMIN','ROLE_USER')">
								<a  href="${pageContext.request.contextPath}/user/viewAttendance" class="nav-link">
									<i class="fa-solid fa-fingerprint fa-xl sideNavIcon"></i>
									<span class='sideNavText'><spring:message code="topnav.lable.attendance"></spring:message></span>
								</a>
							</sec:authorize>
						</li>
					</ul>
					<sec:authorize access="hasAuthority('ROLE_USERADMIN')">
						<ul class="pl-0">
						<li class="nav-item " id="management" style="list-style:none;">
							<a href="#" id="management1_masterMaintenance" class="nav-link management1 ">
								<i class="fa-solid fa-screwdriver-wrench fa-xl sideNavIcon"></i>
								<span>
									<p class="maintainceheight">
										<span class='sideNavText'><spring:message code="topnav.lable.management"></spring:message></span>
										<span class='sideNavText'>
											<i class="right fas fa-angle-left"></i>
											<i class="down fas fa-angle-down" style="display:none"></i>
										</span>
									</p>
								</span>
							</a>
							<ul class="nav nav-treeview management-one" id="management-one" style="display:none">
								<li class="nav-item noHover1 sidenavAnchor">
									<a id="item-1" class="nav-link management1-item1 secondLevelNav">
										<p class="maintainceheight">
											<i class="fa-solid fa-user secondLevelNavIcon"></i>
											<span class='sideNavText'><spring:message code="topnav.lable.empmanagement"></spring:message><span class='sideNavText'>
										</p>
									</a>
								</li>
								<li class="nav-item noHover2">
									<a id="item-2" class="nav-link management1-item2 secondLevelNav">
										<p class="maintainceheight">
											<i class="fa-solid fa-building-user secondLevelNavIcon"></i>
											<span class='sideNavText'><spring:message code="topnav.lable.deptmanagement"></spring:message></span>
										</p>
									</a>
								</li>
								<li class="nav-item noHover3">
									<a id="item-3" class="nav-link management1-item3 secondLevelNav">
										<p class="maintainceheight">
											<i class="fa-solid fa-landmark secondLevelNavIcon"></i>
											<span class='sideNavText'><spring:message code="topnav.lable.desgmanagement"></spring:message></span>
										</p>
									</a>
								</li>
								<li class="nav-item noHover4">
									<a id="item-4" class="nav-link management1-item4 secondLevelNav">
										<p class="maintainceheight">
											<i class="fa-solid fa-circle-info secondLevelNavIcon"></i>
											<span class='sideNavText'><spring:message code="topnav.lable.leavemanagement"></spring:message></span>
										</p>
									</a>
								</li>
								<li class="nav-item noHover5">
									<a id="item-5" class="nav-link management1-item5 secondLevelNav">
										<p class="maintainceheight">
											<i class="fa-solid fa-calendar-days secondLevelNavIcon"></i>
											<span class='sideNavText'><spring:message code="topnav.lable.holidaymanagement"></spring:message></span>
										</p>
									</a>
								</li>
							</ul>
						</li>
						</ul>
					</sec:authorize>
				</nav>
			</div>
		</div>
		<script src="${pageContext.request.contextPath}/resource/lib/jquery.min.js"></script>
		<script	src="${pageContext.request.contextPath}/resource/js/sideContainer.js"></script>
		<%-- <script src="${pageContext.request.contextPath}/resource/js/sideNav.js"></script> --%>
	</body>
</html>