<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/sideContainer.jsp"%>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
	<title><spring:message code="login.label.Microlearning"/></title>
	
	<%-- <jsp:include page="header.jsp"></jsp:include> --%>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/languagehide.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/m_common.css">
	
</head>
<body>
	<br/>
	<div id="loader" style="position: absolute;display: none"></div>
	<h3>${errorMsg}</h3>
</body>

</html>