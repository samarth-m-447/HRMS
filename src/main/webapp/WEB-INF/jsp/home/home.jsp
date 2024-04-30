<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/sideContainer.jsp"%>
<%@ include file="/WEB-INF/jsp/common/loading.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" xml:lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, shrink-to-fit=no, user-scalable=0" />
<title><spring:message code="login.label.hrms"></spring:message></title>
<%-- <link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/images/sciiFavIcon.ico" type="image/x-icon"> --%>
<%-- <jsp:include page="../common/header.jsp"></jsp:include> --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/fallback.css">
<!-- Font Awesome Icons -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/lib/plugins/fontawesome-free/css/all.min.css">
<!-- IonIcons -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/lib/dist/css/adminlte.min.css">

<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/home_new.css"> --%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">

</head>
<body class="hold-transition sidebar-mini layout-fixed excludepage" style="background: white !important;">

  
	<%-- <jsp:include page="../common/sideContainer.jsp" /> --%>
	<div class="d-none">
		<input id="loggedInUser" value='${loggedInUserId}'> 
		<input id="loggedInUserRole" value='${loggedInUserRole}'>
			<input id='loggedInUserImage' value='${profileImage}'>
	</div>
	
	<!-- REQUIRED SCRIPTS -->
	

	<!-- jQuery -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/lib/plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resource/lib/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<!-- AdminLTE -->
	<script src="${pageContext.request.contextPath}/resource/lib/docs/assets/js/adminlte.js"></script>

	<script src="${pageContext.request.contextPath}/resource/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/sideNav.js"></script>
	<script>
		$('.line-one-text').find('span').wrapInner('<a/>')
		$('.line-one-text span').wrapInner('<a/>')
	</script>
</body>
</html>