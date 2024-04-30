/*
 * 210016
 * 
 */
package com.scii.controller;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.scii.model.UploadFile;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;
import com.scii.constants.HRMSConstants;
import com.scii.model.Department;
import com.scii.model.Employee;
import com.scii.service.DepartmentService;
import com.scii.service.EmployeeService;

@Controller
public class DepartmentController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private EmployeeService empService;

	// Display Department screen after click on department under master maintenance.
	@GetMapping("admin/Department")
	public ModelAndView displayDepartment() {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = null;
		try {
			userModel = empService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			modelAndView.setViewName(HRMSConstants.LOGOUT);
			return modelAndView;
		}
		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", empService.employeeImagePath(userModel).getEmp_image_path());
		modelAndView.setViewName("department/department");
		return modelAndView;
	}

	// Get tabulator data from database.
	@GetMapping(value = { "/getDepartments" })
	public @ResponseBody Object getDepartments(@ModelAttribute("Department") Department departmentReq) {
		List<Department> listdepartmentRes = new ArrayList<>();
		listdepartmentRes = departmentService.getDepartments();
		return listdepartmentRes;
	}

	// Method to import excel File
	@Transactional
	@PostMapping(value = { "/admin/importDepartment" }, headers = ("content-type=multipart/*"))
	public @ResponseBody Object importDepartment(@ModelAttribute UploadFile uploadFile) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee employeeModel = null;
		try {
			employeeModel = empService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			return HRMSConstants.LOGOUT;
		}
		mapJson = departmentService.ImportDepartment(uploadFile, employeeModel);
		return mapJson;
	}
}
