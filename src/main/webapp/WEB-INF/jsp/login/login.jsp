<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" xml:lang="en">
<head>
<title><spring:message code="login.label.hrms"/></title>

<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="pragma" content="no-cache" />

<%-- <jsp:include page="../common/errorMsg.jsp"></jsp:include> --%>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0">

<link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/images/sciiFavIcon.ico" type="image/x-icon">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/lib/font-awesome.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/lib/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/login.css">

<style>
.modal-dialog {
	max-width: 332px;
	margin-left: 31.75rem;
}

button:focus {
	outline: 5px auto #80bdff;
}
body{
	overflow: hidden;
}
#userId{
	margin-top:30px;
}
.clearBtn{
	background-color: #ED7D31 !important; 
	width: 80px;
}
.submitBtn{
	background-color: #548325 !important;
	width: 80px; 
	margin-left:60px;
	margin-right:4px;
}
.pwdInput{
	position: relative; 
	left: 20px; 
	text-align: center;
}
.modalAllignment{
	margin-top: 11rem;
	padding-right: 15px!important;
}
</style>
</head>
<%-- <body>
	<div class="d-none" id="msg" data-msg="${message}"
		data-isFirstLogin="${isFirstLogin}" data-userid="${user_id}">
	</div> --%>

<body>
	<div class="d-none" id="msg" data-msg="${message}" data-isFirstLogin="${isFirstLogin}" data-userid="${user_id}"	data-screenId="Login"></div>
	<div class="cotainer">
		<div class="jumbotron min-vh-100 text-center m-0 d-flex flex-column justify-content-center"	style="overflow: hidden;">
			<div class="col-lg-4 col-md-4 offset-md-2" style="padding:0px 15px 0px 0px">
				<div class="card rounded">
					<div class="card-header text-center container d-flex h-25" style="background-color: #363636; max-width: 1230px;">
						<div class="col-2 mt-2 px-0">
							<img src="${pageContext.request.contextPath}/resource/images/scii.png"	alt="SCII Image" width="90%"/>
						</div>						
						<div class="col-8 text-white justify-content-center align-self-center">							
							<h2 class="microlearninglabel" style="overflow-wrap: normal; margin-left: 15px; font-size: 1.5rem;">
								<spring:message code="login.label.hrms"></spring:message>
							</h2>
						</div>
					</div>
					<div class="card-body">
						<form:form action="${pageContext.request.contextPath}/hrmsLoginAuth" id="loginForm" model-attribute="loginForm" autocomplete="off">
							<input type="hidden" name="${parameterName}" value="${token}" />
							<%-- ${requestScope.error} --%>
							<h6 id="clearMessage" style="color:red">${message}</h6>
							<div class="form-group">
							
								<input type="text" id="userId" class="form-control loginInputField" name="username" placeholder="<spring:message code="login.label.empid"/>" maxlength="6" required autofocus />
								
								<input type="password" id="password" class="form-control loginInputField" name="password" placeholder="<spring:message code="login.label.password"/>"	maxlength="10" required>
															
								<button type="submit" class="btn btn-sm btn-sm-enabled loginButton"	id="login">
									<spring:message code="btn.login" />
								</button>
								<br/>
								<a id="forgotPasswordLink" class="btn btn-link textCenter"> 
									<small>
										<span style="color:#212529"><spring:message code="login.label.forgotpassword" /></span>
									</small>                             
								</a>
							</div>

						</form:form>
					</div>
				</div>
			</div>
		</div>
		<div id="modelpopup"></div>
	</div>
	
	<!-- Footer -->
	<nav
		class="container-fluid-nav navbar-light bg-light text-center fixed-bottom">
	<small> &copy; <spring:message code="login.label.copyright"
			arguments="${year}"></spring:message> <a href="https://scii.in/"
		target="_blank"> <spring:message code="login.label.scii" /></a> <spring:message
			code="login.label.allrightsReserved"></spring:message>
	</small> </nav>
<div class="container">
	<div class="modal fade" data-backdrop="static" id="firstTimePassword">
		<div class="modal-dialog modal-lg modalAllignment">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<h5 class="modal-title">
						<spring:message code="login.label.changePassword"></spring:message>
					</h5>
					<button type="button" class="close" data-dismiss="modal" id="closeChangePassword" style="outline: none;">&times;</button>
				</div>

				<!-- Modal body -->
				<div class="modal-body">

					<div>
						<div style="text-align: center;">
							<span style="color:#F4B183" class="mb-3"> <spring:message
									code="login.label.changePassNote"></spring:message>
							</span>
						</div>

						<form method="POST" id="firsttimepwchange"
							style="padding-left: 0px;">
							<input type="hidden" name="${parameterName}" value="${token}" />
							<div class="input-group  col-10 mt-3 mb-2">
								<input type="password" class="form-control pwdInput" id="newpassword" 
								path="password" maxlength="10" name="newpassword" onchange="convertToPassword()"
								placeholder="<spring:message code="login.label.enterNewPassword"/>"
									 />
							</div>
							<div class="input-group  col-10 mb-2">
								<input type="password" path="password" class="form-control pwdInput" id="confirmpassword"
									maxlength="10" name="confirmpassword" onchange="convertToPassword()" 
									placeholder="<spring:message code="login.label.confirmNewPassword"/>"
									/>
							</div>
							<div class="row col-12 mb-3">
								<!-- <div class="col-5" style="text-align: right; padding: 0; margin-left: 10%;"> -->
									<button type="button" class=" btn-sm btn-sm-enabled submitBtn" id="changePassbtnSubmit">
										<spring:message code="btn.submit"></spring:message>
									</button>
								<!-- </div>
								<div class="col-5"> -->
									<button type="button" class="btn-sm btn-sm-enabled-danger clearBtn" id="changePassbtnClear">
										<spring:message code="btn.clear"></spring:message>
									</button>
								<!-- </div> -->
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	<!--=================================================-->
	<script src="${pageContext.request.contextPath}/resource/lib/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/popper.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/lib/bootstrap.min.js"></script>
	<!--=================================================-->

	<script src="${pageContext.request.contextPath}/resource/lib/jquery.validate.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/login.js"></script>

</body>
</html>