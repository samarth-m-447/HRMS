<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
<title><spring:message code="login.label.hrms" /></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/pw_forgot.css">
<style>
.loaderDiv{
    position: absolute;
    width: 100%;
    height: 100%;
   /*  background: rgba(0,0,0,0.5); */
    z-index: 9999;
    top: 0px;
}  
 .spinner {
  top: 50%;
  left: 48%;
  transform: translate(-50%, -50%);
  position: absolute;
  z-index:1;  
  border: 5px solid #f3f3f3;
  border-radius: 50%;
  border-top: 5px solid #3498db;
  width: 40px;
  height: 40px;
  -webkit-animation: spin 2s linear infinite; 
  animation: spin 2s linear infinite;
}
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
.displayBlock{
	display:block!important;
}
.displayNone{
	display:none!important;
}
</style>

</head>
<body>
	<div class="container">
	<!-- Add/Modify Course -->
	<div class="modal fade" data-backdrop="static" id="forgotpwdmodal">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header">
					<h5 class="modal-title" style="margin-top: -8px;">
						<spring:message code="login.label.forgotpasswordlabel"></spring:message>
					</h5>
					<button type="button" class="close" data-dismiss="modal" style="margin-top: -22px;" id="forgotPasswordClose">&times;</button>
				</div>

			<!-- Modal body -->
			<div class="modal-body">
				<div class="container-fluid" style="margin-top: 25px">
													<form id="forgotPasswdForm" modelAttribute="forgotPasswdForm"
														onsubmit="return false;" autocomplete="off">
														<input type="hidden" value="" name="company" id="company" />
														<input type="hidden" name="${parameterName}" value="${token}" />
														<div class="input-group col-lg-6 col-md-8 col-sm-12 mb-3">
															<div class="input-group-prepend">
																<input type="text" id="user_Idpw" class="form-controlpw"
																	name="user_id" value="${forgotPasswdForm.emp_id}"
																	style="color: AFAFAF; margin-left: 25px; text-align: center;"
																	placeholder="<spring:message code="login.label.empid"/>"
																	maxlength="6" required autofocus>
															</div>

															<div class="input-group mt-4">
																<input type="button"
																	value="<spring:message code="btn.sendPasswordChangeLink"/>"
																	id="SendPasswordChangeLink"
																	class="btn-sm btn-sm-enabled" style="width: 240px;margin-left:33%;background:#548325 !important;cursor: pointer;">
															</div>
															<div id="mailSentText" style="display: none;">
																<div class="mt-2">
																	<p style="white-space: pre-line;width: 15rem;justify-content:end;">
																		<spring:message code="MSG70" />
																	</p>
																</div>
																<div class="input-group  col-12">
																	<p>
																		<%-- <spring:message code="login.label.goto"/>  --%>
																		<a
																			href="${pageContext.request.contextPath}/login">
																			<spring:message code="login.label.goto" />
																		</a>
																		<%-- <spring:message code="login.label.page"/> --%>
																	</p>
																</div>
															</div>
														</div>
														</form>
			</div>
		</div>
	</div>
</div>
</div>
</div>

<div class="loaderDiv displayNone" id="loaderDiv">
	 <div class="spinner"></div>
</div>





<script src="${pageContext.request.contextPath}/resource/js/login.js"></script>
<script src="${pageContext.request.contextPath}/resource/js/common.js"></script>
</body>
</html>