<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/WEB-INF/jsp/common/include.jsp"%>
<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
<%@ include file="/WEB-INF/jsp/common/sideContainer.jsp"%>
<%@ include file="/WEB-INF/jsp/common/footer.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title><spring:message code="login.label.hrms" /></title>
<%-- <jsp:include page="../common/header.jsp"></jsp:include> --%>
<style>
.modal-footer{
	justify-content:center;
}
.modal-header{
	align-items: center;
}
.imageSection{
	/* height: 100%;
    width: 100%;
    border-radius: 5px;
    margin-top: -18px; */
    height: 83%; 
    width: 83%; 
    border-radius: 5px; 
    margin-top: -4px;
}
.imgDivStyle{
	/* background: #d1dce8; 
	width: 110px; 
	height: 110px; 
	display: none; 
	border-radius: 8px; */
	background: #d1dce8; 
	width: 130px; 
	height: 130px; 
	display: none; 
	border-radius: 8px;
}
.uploadedImgClose{
	margin-left: 112px; 
	font-size: 18px; 
}
.imageInput{
	height: 32px;
	width: 60%; 
	display: inline;
	float: right; 
	line-height: 14px;
}
.imageUploadPath{
    word-wrap: break-word; 
    width: 60%;
    display: inline;
    float: right;
    padding-right:0px;     
    padding-left:0px;  
    line-height: 1.5;
    border: 0px solid #fff;   
}
.updatedProfileImg{
	width: 110px;
	height: 110px; 
	border-radius: 15%; 
}
.updatedEmptyProfileImg{
	width: 109px; 
	position: relative; 
	right: -450px; 
	top: -60px; 
	height: 103px; 
	border-radius: 15%; 
	display: none;
}
.employeeLable{
	padding-top:2px;
	padding-bottom:8px;
}
.empInput{
	width: 60%; 
	display: inline; 
	float: right; 
	line-height: 5px;
}
.empSelect{
	height: 32px; 
	width: 60%; 
	display: inline; 
	float: right; 
	line-height: 14px;
}
#empSaveBtn, #empUpdateBtn, #empFormCancelBtn{
	width: 80px;
}
#checkbox2, #checkbox1{
	margin-top: -48px; 
	margin-left: 166px;
}
.empJDA{
	height: 34px; 
	margin-left: 85px; 
	border-radius: 4px; 
	width: 140px;
}
.empJDM{
	width: 60%; 
	display: inline; 
	float: right; 
	line-height: 14px;
}
.empJDM_Calicon{
	margin-top: -32px; 
	margin-left: 91%;
}
.empSelect_ReportingLeader{
	margin-left: 40%; 
	margin-top: -32px; 
	width: 60%;
}
.empModal{
	width: 896px; 
	margin-left: -175px; 
	margin-top: 58px;
}
.containerStyle{
	margin-left: 15%; 
	margin-top: 5%;
}
</style>
</head>
<body class="hold-transition sidebar-mini layout-fixed excludepage white_BG">
	<%-- <jsp:include page="../common/sideContainer.jsp" /> --%>
	<input type="hidden" value="${pageContext.request.contextPath}" id="context"></input>
	<input type="hidden" value="${empId}" id="empId">
	<input type="hidden" id="loggedInUserRole" value='<c:out value="${loggedInUserId}"/>'>
	<div class="container-wrapper containerStyle">
		<div class="modal fade" id="addEmployeeModal" data-backdrop="static">				
			<div class="modal-dialog" role="document">
				<div class="modal-content empModal">
					<!--Modal header starts -->
					<div class="modal-header">	
						<c:choose>
							<c:when test="${isUpdate eq 1}">
								<h4 class="modal-title text-center ">
									<i class="fa fa-user-plus"></i>&nbsp;<spring:message code="employee.lable.addemployee"></spring:message>
								</h4>
							</c:when>
							<c:otherwise>
								<b>
									<h4 class="modal-title text-center ">
										<i class="fa fa-user-pen"></i>&nbsp;<spring:message code="employee.lable.modifyemployee"></spring:message>
									</h4>
								</b>
								<br>
							</c:otherwise>
						</c:choose>
						<button type="button" class="close" data-dismiss="modal"
								onclick="onClickAddEmployeeClose()">&times;</button>
					</div>
					<!--Modal header ends -->
					<!--Modal body starts -->
					<div class="modal-body mt-3 pb-0">
					<input type="hidden" value="${isUpdate}" id="isUpdate" /> 
						<input type="hidden" value="${pageContext.request.contextPath}" id="context"></input> 
						<input type="hidden" value="${operatingSystem}" id="operatingSystem" /> 
						<input type="hidden" id="tempDirectoryPath" />
						<input type="hidden" id="uploadFileResult" value="${uploadFileResult}">
						<form:form id="employeeForm">
						<div class="row">
						
							<div class="col-lg-6 col-xs-6">
								<div class="form-group mb-3">
									<c:choose>
										<c:when test="${isUpdate eq 1}">
											<label class="col-form-label employeeLable">
												<spring:message code="login.label.empid"></spring:message>
												<span class="required-asterisk">*</span>
											</label>
											<input class="form-control emp_id empInput" type="text" name="emp_id"
												id="emp_id" autocomplete="off"
												value='<c:out value="${employeeUpdateReq.emp_id}"/>'
												maxlength="6" tabindex="1" minlength="3" required>	
										</c:when>
										<c:otherwise>
											<label class="col-form-label employeeLable">
												<spring:message	code="login.label.empid"></spring:message>
												<span class="required-asterisk">*</span>
											</label>
											<input class="form-control emp_id empInput" type="text" name="emp_id"
												id="emp_id" autocomplete="off"
												value='<c:out value="${employeeUpdateReq.emp_id}"/>'
												minlength="3" maxlength="6" tabindex="1" disabled>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-lg-6 col-xs-6">
								<div class="form-group mb-3">
									<c:choose>
										<c:when test="${isUpdate eq 1}">
											<label class="col-form-label employeeLable"><spring:message
													code="employee.lable.employeename"></spring:message>
													<span class="required-asterisk">*</span>
											</label>
											<input class="form-control emp_name empInput" type="text"
												name="emp_name" id="emp_name" required maxlength="200"
												tabindex="2" pattern="[A-Za-z]" required autocomplete="off">
										</c:when>
										<c:otherwise>
											<label class="col-form-label employeeLable">
												<spring:message	code="employee.lable.employeename"></spring:message>
												<span class="required-asterisk">*</span>
											</label>
											<input class="form-control emp_name empInput" type="text"
												name="emp_name" id="emp_name"
												value='<c:out value="${employeeUpdateReq.emp_name}"/>'
												required maxlength="200" tabindex="2" pattern="[A-Za-z]" autocomplete="off">
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						
						<!-- Second Row -->
						<div class="row">
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3">
									<c:choose>
										<c:when test="${isUpdate ne 2}">
											<label class="form-label"><spring:message code="employee.lable.contactnumber"></spring:message></label>
											<input class="form-control emp_contact_Number empInput" type="text"
												name="emp_contact_Number" id="emp_contact_Number" autocomplete="off"
												maxlength="10" value='<c:out value="${employeeModelReq.contact_number}"/>'
												 tabindex="3">
										</c:when>
										<c:otherwise>
											<label class="form-label"><spring:message code="employee.lable.contactnumber"></spring:message></label>
											<input class="form-control emp_contact_Number empInput" type="text"
												name="emp_contact_Number" id="emp_contact_Number" autocomplete="off"
												value='<c:out value="${employeeUpdateReq.contact_number}"/>'
												maxlength="10"  tabindex="3">
										</c:otherwise>
									</c:choose>
								</div>
							</div>

							<div class="col-6 col-xs-6">
								<div class="form-group mb-3">
									<label class="form-label">
										<spring:message code="employee.lable.reportingleaderid"></spring:message>
									</label>
									${reportingLeader.emp_id} <select id="emp_leader_id"
										name="emp_leader_id" class="form-control empSelect_ReportingLeader"
										tabindex="4">
										<option></option>
										<c:forEach var="reportingLeader" items="${reportingManagerList}">
											<c:choose>
												<c:when test="${reportingLeader.emp_id eq employeeUpdateReq.reporting_leader_id}">
													<option value="${reportingLeader.emp_id}" selected><c:out
															value="${reportingLeader.emp_name}" escapeXml="true"></c:out></option>
												</c:when>
												<c:otherwise>
													<option value="${reportingLeader.emp_id}"><c:out
															value="${reportingLeader.emp_name}" escapeXml="true"></c:out></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>

						<!-- Fourth row -->
						<div class="row">
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3">
									<c:choose>
										<c:when test="${isUpdate ne 2}">
											<label class="form-label"><spring:message
													code="employee.lable.email"></spring:message>
													<span class="required-asterisk">*</span></label>
											<input class="form-control office_email empInput" type="email"
												name="office_email" id="office_email" autocomplete="off"
												value='<c:out value="${employeeUpdateReq.office_email}"/>'
												required maxlength="50" tabindex="5">
										</c:when>
										<c:otherwise>
											<label class="form-label"><spring:message
													code="employee.lable.email"></spring:message>
													<span class="required-asterisk">*</span></label>
											<input class="form-control office_email empInput" type="email"
												name="office_email" id="office_email" autocomplete="off"
												value='<c:out value="${employeeUpdateReq.office_email}"/>'
												required maxlength="50" tabindex="5">
										</c:otherwise>
									</c:choose>
								</div>
							</div>

							<div class="col-6 col-xs-6">
								<div class="form-group mb-3">
									<c:choose>
										<c:when test="${isUpdate ne 2}">
											<label class="form-label"><spring:message
													code="employee.lable.office365mail"></spring:message>
												<span class="required-asterisk">*</span>		
											</label>
											<input class="form-control office_email empInput" type="email"
												name="office_email_365" id="office_email_365" autocomplete="off"
												value='<c:out value="${employeeUpdateReq.office_email_365}"/>'
												pattern="^\S+@[^.]+\.onmicrosoft\.com$" required
												maxlength="50" tabindex="6">
										</c:when>
										<c:otherwise>
											<label class="form-label"><spring:message
													code="employee.lable.office365mail"></spring:message>
													<span class="required-asterisk">*</span>
											</label>
											<input class="form-control office_email empInput" type="email"
												name="office_email_365" id="office_email_365" autocomplete="off"
												value='<c:out value="${employeeUpdateReq.office_email_365}"/>'
												pattern="^\S+@[^.]+\.onmicrosoft\.com$" required
												maxlength="50" tabindex="6" >
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<!-- Fifth row -->
						<div class="row">
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3 w-2">
									<label class="form-label"><spring:message
											code="employee.lable.employeetype"></spring:message></label>
											<span class="required-asterisk">*</span>
									<c:choose>
										<c:when test="${isUpdate eq 1 }">
											<select class="form-control emp_level empSelect" name="emp_type"
												id="emp_type" tabindex="7">
												<option value=""></option>
												<c:forEach var="employee" items="${employeeType}">
													<option value="${employee.value}"><c:out
															value="${employee.value}" escapeXml="true" /></option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<select class="form-control emp_level empSelect" name="emp_type"
												id="emp_type" tabindex="7">
												<option value=""></option>
												<c:forEach var="employee" items="${employeeType}">
													<c:choose>
														<c:when
															test="${employee.value eq employeeUpdateReq.emp_type}">
															<option value="${employee.value}" selected><c:out
																	value="${employee.value}" escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${employee.value}"><c:out
																	value="${employee.value}" escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3 w-2">
									<c:choose>
										<c:when test="${isUpdate eq 1 }">
											<label class="form-label"><spring:message
													code="employee.lable.employeelevel"></spring:message></label>
											<select class="form-control emp_level empSelect" name="emp_level"
												id="emp_level" tabindex="8">
												<option value=""></option>
												<c:forEach var="empLevel" items="${employeeLevel}">
													<option value="${empLevel.value}"><c:out
															value="${empLevel.value}" escapeXml="true" /></option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<label class="form-label"><spring:message
													code="employee.lable.employeelevel"></spring:message></label>
											<select class="form-control emp_level empSelect" name="emp_level"
												id="emp_level" tabindex="8">
												<option value=""></option>
												<c:forEach var="empLevel" items="${employeeLevel}">
													<c:choose>
														<c:when
															test="${empLevel.value eq employeeUpdateReq.emp_level}">
															<option value="${empLevel.value}" selected><c:out
																	value="${empLevel.value}" escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${empLevel.value}"><c:out
																	value="${empLevel.value}" escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						
						<!-- Sixth row -->
						<div class="row">
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3">
									<c:choose>
										<c:when test="${isUpdate ne 2 }">
											<div class="input-group date">
												<label class="float-left">
													<spring:message	code="employee.lable.empjoineddate"></spring:message>
												</label> 
												<input type="text" id="emp_joined_date" name="emp_joined_date"
													class="form-control white_BG empJDA" placeholder="YYYY-MM-DD"
													maxlength="10" tabindex="9" readonly>
												<div class="input-group-append" id="calicon1">
													<span class="input-group-text">
														<i class="fa fa-calendar"></i>
													</span>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<label class="form-label">
												<spring:message	code="employee.lable.empjoineddate"></spring:message>
											</label>
											<input class="form-control emp_joined_date white_BG empJDM" type="text"
												placeholder="YYYY-MM-DD" id="emp_joined_date"
												value='<c:out value="${employeeUpdateReq.emp_joined_date}"/>'
												tabindex="9" readonly>
											<div class="input-group-append" id="calicon1">
												<span class="input-group-text empJDM_Calicon"><i
													class="fa fa-calendar"></i></span>
											</div>
										</c:otherwise>

									</c:choose>

								</div>
							</div>
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3 w-2">
									<c:choose>
										<c:when test="${isUpdate ne 2}">
											<label class="form-label">
												<spring:message	code="employee.lable.role"></spring:message>
												<span class="required-asterisk">*</span>
											</label>
											<select class="form-control emp_role empSelect" name="emp_role"
												id="emp_role" tabindex="10">
												<option value=""></option>
												<c:forEach var="role" items="${roleMaster}">
													<c:choose>
														<c:when
															test="${role.role_id eq employeeUpdateReq.role_id}">
															<option value="${role.role_id}" selected><c:out
																	value="${role.role_name}" escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${role.role_id}"><c:out
																	value="${role.role_name}" escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<label class="form-label">
												<spring:message	code="employee.lable.role"></spring:message>
												<span class="required-asterisk">*</span>
											</label>
											<select class="form-control emp_role empSelect" name="emp_role"
												id="emp_role" tabindex="11">
												<option value=""></option>
												<c:forEach var="role" items="${roleMaster}">
													<c:choose>
														<c:when
															test="${role.role_id eq employeeUpdateReq.role_id}">
															<option value="${role.role_id}" selected><c:out
																	value="${role.role_name}" escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${role.role_id}"><c:out
																	value="${role.role_name}" escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						
						<!-- Seventh row -->
						<div class="row">
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3">

									<c:choose>
										<c:when test="${isUpdate ne 2}">
											<label class="form-label"><spring:message
													code="employee.lable.empdepartment"></spring:message></label>
											<select class="form-control emp_department empSelect"
												name="emp_department" id="emp_department" tabindex="12">
												<option value=""></option>
												<c:forEach var="department" items="${departmentMaster}">
													<c:choose>
														<c:when
															test="${department.dept_name eq employeeUpdateReq.dept_id}">
															<option value="${employeeModel.dept_id}" selected><c:out
																	value="${employeeModel.dept_id} " escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${department.dept_id}"><c:out
																	value="${department.dept_name} " escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<label class="form-label"><spring:message
													code="employee.lable.empdepartment"></spring:message></label>
											<select class="form-control emp_department empSelect"
												name="emp_department" id="emp_department" tabindex="13">
												<option value=""></option>
												<c:forEach var="department" items="${departmentMaster}">
													<c:choose>
														<c:when
															test="${department.dept_id eq employeeUpdateReq.dept_id}">
															<option value="${department.dept_id}" selected><c:out
																	value="${department.dept_name }" escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${department.dept_id}"><c:out
																	value="${department.dept_name }" escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="col-6 col-xs-6">
								<div class="form-group mb-3">
									<c:choose>
										<c:when test="${isUpdate ne 2}">
											<label class="form-label"><spring:message
													code="employee.lable.empdesignation"></spring:message>
													<span class="required-asterisk">*</span></label>
											<select class="form-control emp_designation empSelect"
												name="emp_designation" id="emp_designation" tabindex="14">
												<option value=""></option>
												<c:forEach var="designation" items="${designationMaster}">
													<c:choose>
														<c:when
															test="${designation.desg_id == employeeUpdateReq.desg_id}">
															<option value="${designation.desg_id}" selected><c:out
																	value="${designation.desg_name}" escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${designation.desg_id}"><c:out
																	value="${designation.desg_name}" escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<label class="form-label"><spring:message
													code="employee.lable.empdesignation"></spring:message>
													<span class="required-asterisk">*</span></label>
											<select class="form-control emp_designation empSelect"
												name="emp_designation" id="emp_designation" tabindex="15">
												<option value=""></option>
												<c:forEach var="designation" items="${designationMaster}">
													<c:choose>
														<c:when
															test="${designation.desg_id eq employeeUpdateReq.desg_id}">
															<option value="${designation.desg_id}" selected><c:out
																	value="${designation.desg_name}" escapeXml="true" /></option>
														</c:when>
														<c:otherwise>
															<option value="${designation.desg_id}"><c:out
																	value="${designation.desg_name}" escapeXml="true" /></option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>

						<!-- Eight row -->
						<div class= "row">
							<div class="col-6 col-xs-6">
								<div class="form-group mb-0">
									<c:choose>
										<c:when test="${isUpdate eq 2}">
											<label class="form-label">
												<spring:message code="employee.lable.ptmsadmin"></spring:message>
											</label>
											<div class="custom-switch mt-3">
												<c:choose>
													<c:when test="${employeeUpdateReq.ptms_admin_flg eq 1}">
														<input type="checkbox" class="custom-control-input"
															id="ptmsAdmin" name="example"
															value="${employeeUpdateReq.ptms_admin_flg}" checked>
													</c:when>
													<c:otherwise>
														<input type="checkbox" class="custom-control-input"
															id="ptmsAdmin" name="example"
															value="${employeeUpdateReq.ptms_admin_flg}">
													</c:otherwise>
												</c:choose>
												<label class="custom-control-label" for="ptmsAdmin"
													id="checkbox1"></label>
											</div>
										</c:when>
									</c:choose>
								</div>
							</div>
							<!-- Eight row -->
							<input type="hidden" value="${employeeUpdateReq.login_fail_attempts}" id="loginAttempts">
							<div class="col-6 col-xs-6">
								<div class="form-group mb-0">
									<c:choose>
										<c:when test="${isUpdate eq 2}">
											<label class="form-label" >
												<spring:message code="employee.lable.locked"></spring:message>
											</label>
											<div class="custom-switch mt-3">
												<c:choose>
													<c:when test="${employeeUpdateReq.user_lock_flg eq 1}">
														<input type="checkbox" class="custom-control-input"
															id="userLockFlag" name="userLockFlag"
															value="${employeeUpdateReq.user_lock_flg}" checked>
													</c:when>
													<c:otherwise>
															<input type="checkbox" class="custom-control-input"
																id="userLockFlag" name="userLockFlag"
																value="${employeeUpdateReq.user_lock_flg}" disabled>
														</c:otherwise>
												</c:choose>
												<label class="custom-control-label" for="userLockFlag"
													id="checkbox2"></label>
											</div>
										</c:when>
									</c:choose>
								</div>
							</div>
						</div>
						
						<form method="POST" enctype="multipart/form-data">
							<div class="row ">
								<div class="col-6 col-xs-6">
									<div class="form-group mb-3">
										<label class="form-label">
											<spring:message	code="employee.lable.uploadimage"></spring:message>
										</label>									
										<c:choose>
											<c:when test="${isUpdate ne 2}">
												<input type="file" class="form-control imageInput"	id="inputImageUploadBtn" 
												accept="image/jpg, image/jpeg, image/png" />
												<br />
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${empty employeeUpdateReq.emp_image_path}">
														<input type="file" class="form-control imageInput"
															id="inputImageUploadBtn" />
														<br/>
													
														<label class="form-label" style="display: none;">
															<spring:message code="employee.lable.currentimagepath"></spring:message>
														</label>
														<p class="imageUploadPath form-control"
															id="imgPath">${employeeUpdateReq.emp_image_path}</p>
															
													</c:when>
													<c:otherwise>
														<input type="file" class="form-control imageInput"
															id="inputImageUploadBtn"/>
														<br />
														
														<label class="form-label">
															<spring:message	code="employee.lable.currentimagepath"></spring:message>
														</label>
														<p class="imageUploadPath form-control"
															id="imgPath" value="${employeeUpdateReq.emp_image_path}">${employeeUpdateReq.emp_image_path}</p>
														
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>									
									</div>
								</div>
								<div class="col-6 col-xs-6">
									<div class="form-group mb-3">
										<c:choose>
											<c:when test="${isUpdate eq 2}">
												<c:choose>
													<c:when test="${empty employeeUpdateReq.emp_image_path}">
														<img class="updatedEmptyProfileImg" id="updatedEmptyProfile"
															src="${pageContext.request.contextPath}/externalImages/${employeeUpdateReq.emp_image_path}"
															onerror="this.style.display='none'" alt="">
													</c:when>
													<c:otherwise>
														<img class="updatedProfileImg" id="updatedProfile"
															src="${pageContext.request.contextPath}/externalImages/${employeeUpdateReq.emp_image_path}"
															onerror="this.style.display='none'" alt="">
													</c:otherwise>
												</c:choose>
											</c:when>
										</c:choose>
										
										<div class="form-group text-center mb-0">
											<div id="imgDiv" class="imgDivStyle">
												<span>
													<i class="fa fa-times-circle uploadedImgClose" aria-hidden="true" onclick="removImage()"></i>
												</span>
												<img src="" class="imageSection" alt="Image preview" id="img" />
											</div>
										</div>								
									</div>
								</div>
							</div>
						</form>
						</form:form>										
					</div>
					<!--Modal body ends -->
					<!--Modal footer starts -->
					<div class="modal-footer">
						<c:choose>
							<c:when test="${isUpdate eq 1}">
								<button class="btn-sm btn-sm-enabled" type="submit"
									id="empSaveBtn">
									<spring:message code="btn.save"></spring:message>
								</button>
							</c:when>
							<c:otherwise>
								<button class="btn-sm btn-sm-enabled" type="submit"	id="empUpdateBtn">
									<spring:message code="btn.save"></spring:message>
								</button>

							</c:otherwise>
						</c:choose>
						<button class="btn-sm btn-sm-enabled-danger" type="submit"
							id="empFormCancelBtn">
							<spring:message code="btn.cancel"></spring:message>
						</button>
					</div><!--Modal footer ends -->									
				</div>
			</div>
		</div>
		<div class="modal fade" data-backdrop="static"
			id="DeleteEmployeeModel">
			<div class="modal-dialog ">
				<div class="modal-content">
					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">
							<i class="fa fa-user-xmark"></i>&nbsp;<spring:message code="employee.lable.deleteemployee"></spring:message>
						</h4>
						<button type="button" class="close" data-dismiss="modal"
							onclick="onClickAddEmployeeClose()">&times;</button>
					</div>

					<!-- Modal body -->
					<div class="modal-body" id="modwindow" data-screenid="HRMS01C">
						<input type="hidden" id="emp" value="${emp_id}">
						<div>
							<c:if test="${isCurrentUser eq true}">
								<span style="color:RED"><spring:message code="MSG37"></spring:message></span>
							</c:if>
							<c:if test="${isCurrentUser eq false}">
								<spring:message code="MSG36"></spring:message>
							</c:if>
						</div>
					</div>

					<!-- Modal footer -->
					<div class="modal-footer">
						<c:if test="${isCurrentUser eq false}">
							<button type="button" id="btnDeleteEmployee"
								class="btn-sm btn-sm-enabled">
								<spring:message code="btn.yes"></spring:message>
							</button>
						</c:if>
						<button type="button"
							class="btn-sm btn-sm-enabled-danger admin-button"
							data-dismiss="modal" onclick="onClickAddEmployeeClose()">
							<c:choose>
								<c:when test="${isCurrentUser eq false}">
									<spring:message code="btn.no"></spring:message>
								</c:when>
								<c:otherwise>
									<spring:message code="btn.close"></spring:message>
								</c:otherwise>
							</c:choose>
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- REQUIRED SCRIPTS -->	
	<script src="${pageContext.request.contextPath}/resource/lib/jquery.validate.min.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/resource/js/_addmodifyEmployee.js"></script>

</body>
</html>