/*
 * 210016
 * 
 */
package com.scii.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;
import com.scii.constants.HRMSConstants;
import com.scii.model.Designation;
import com.scii.model.Employee;
import com.scii.model.UploadFile;
import com.scii.service.DesignationService;
import com.scii.service.EmployeeService;

@Controller
public class DesignationController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private DesignationService designationService;

	@Autowired
	private EmployeeService empService;

	//Display Designation screen after click on designation under master maintenance.
	@GetMapping("admin/Designation")
	public ModelAndView displayDesignation() {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = empService.getUserModelFromSecurityContext();
		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", empService.employeeImagePath(userModel).getEmp_image_path());

		modelAndView.setViewName("designation/designation");
		return modelAndView;
	}

	//Get tabulator data from database.
	@GetMapping(value = { "/getDesignation" })
	public @ResponseBody Object getDesignation(@ModelAttribute("Designation") Designation designationReq) {
		List<Designation> listdesignationRes = new ArrayList<>();
		listdesignationRes = designationService.getDesignation();
		return listdesignationRes;
	}

	// Method to import excel File
	@Transactional
	@PostMapping(value = { "/admin/importDesignation" }, headers = ("content-type=multipart/*"))
	public @ResponseBody Object importDesignation(@ModelAttribute UploadFile uploadFile) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee employeeModel = null;
		try {
			employeeModel = empService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = designationService.importDesignationImplementation(uploadFile, employeeModel);
		return mapJson;
	}

}
