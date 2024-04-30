<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
<title><spring:message code="login.label.hrms" /></title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/languagehide.css">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/images/sciiFavIcon.ico" type="image/x-icon">
<!--=================================================-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/lib/bootstrap.min.css">
<!--=================================================-->

<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/common.css">
<style>
h5 {
	margin-bottom: 0rem!important;
}

@media ( max-width : 485px) {
	.input-group-text {
		width: 160px !important;
	}
}
</style>
</head>
<body>
	<c:if test="${invalid == true}">
		<div id="invalid"></div>
		<div class="d-none" id="invalidText" data-inValidText="${invalidText}"></div>
	</c:if>
	<c:if test="${invalid == false}">
		<div class="container-fluid py-2"
			style="background-color: #e6e6e6; margin-top: 0px;">
			<h5>
				<spring:message code="login.label.resetPassword" />
			</h5>
		</div>

		<div class="container-fluid" style="margin-top: 2px">
			<div class="row content">
				<div class="card-body row">
					<div class="col-md-7 col-lg-5">
						<form modelAttribute="employeeModel" id="pw_resetForm" autocomplete="off"
							onsubmit="return false;">
							<div class="row">
								<div class="input-group col-12 mb-2	">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon1"
											style="width: 194px;"> <spring:message
												code="login.label.empid" />
										</span>
									</div>
									<input type="text" class="form-control" id="user_id"
										name="user_id" path="user_id" value="${employeeModel.emp_id}"
										disabled="true" />
								</div>

								<div class="input-group col-12 mb-2">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon1"
											style="width: 194px;"> <spring:message
												code="employee.lable.employeename" />
										</span>
									</div>
									<input type="text" class="form-control unblockScroll"
										id="user_name" path="user_name" value="${employeeModel.emp_name}"
										name="user_name" disabled="true" />
								</div>

								<div class="input-group  col-12 mt-3 mb-2">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon1"
											style="width: 194px;"> <spring:message
												code="login.label.newPassword" />
										</span>
									</div>
									<input type="password" class="form-control" id="newpassword"
										path="password" maxlength="10" autocomplete="current-password"
										placeholder="<spring:message code="login.label.enterNewPassword"/>"
										name="newpassword" onchange="convertToPassword()" />
								</div>

								<div class="input-group  col-12 mb-2">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon1"
											style="width: 194px;"> <spring:message
												code="login.label.confirmPassword" />
										</span>
									</div>
									<input type="password" path="password" class="form-control"
										id="confirmpassword" maxlength="10" autocomplete="current-password"
										placeholder="<spring:message code="login.label.confirmNewPassword"/>"
										name="confirmpassword" onchange="convertToPassword()" />
								</div>

								<div class="input-group  col-4 mt-2 mb-2">
									<button type="button" id="passwordReset"
										class="btn-sm btn-sm-enabled">
										<spring:message code="btn.reset"></spring:message>
									</button>
								</div>
								<div id="resetPasswordText" style="display: none;">
									<div class="input-group col-12 mt-2">
										<p style="white-space: nowrap">
											<spring:message code="MSG72" />
										</p>
									</div>
									<div class="input-group  col-12">
										<p>
											<%-- <spring:message code="login.label.goto"/> --%>
											<a
												href="${pageContext.request.contextPath}/login">
												<spring:message code="login.label.goto" />
											</a>
											<%-- <spring:message code="login.label.page"/> --%>
										</p>
									</div>
								</div>
							</div>
							<div class="text-center" id="loading" Style="display: none;">
								<div class="spinner-border" role="status">
									<span class="sr-only">Loading...</span>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</c:if>

	
	<nav
		class="container-fluid-nav navbar-light bg-light text-center fixed-bottom">
		<small> &copy; <spring:message code="login.label.copyright"
				arguments="${year}"></spring:message> <a href="https://scii.in/"
		target="_blank"> <spring:message code="login.label.scii" /></a> <spring:message
				code="login.label.allrightsReserved"></spring:message>
		</small>
	</nav>
	<!--=================================================-->
	<script
		src="${pageContext.request.contextPath}/resource/lib/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resource/lib/popper.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resource/lib/bootstrap.min.js"></script>
	<!--=================================================-->
	<script
		src="${pageContext.request.contextPath}/resource/lib/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resource/lib/jquery.validate.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/login.js"></script>
</body>
</html>