/*
 * 210016
 * 
 */
package com.scii.controller;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.ibatis.ognl.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.scii.constants.HRMSConstants;
import com.scii.model.Employee;
import com.scii.model.Leave;
import com.scii.model.UploadFile;
import com.scii.service.EmployeeService;
import com.scii.service.LeaveMasterService;

@Controller
public class LeaveMasterController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private LeaveMasterService leaveMasterService;

	// @Description getLeaveMaster() method will display Leave Information.
	@GetMapping("admin/getLeaveMaster")
	public ModelAndView getLeaveMaster(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = employeeService.getUserModelFromSecurityContext();

		modelAndView.addObject("empId", userModel.getEmp_id());
		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_name());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", employeeService.employeeImagePath(userModel).getEmp_image_path());
		modelAndView.setViewName("leaveMaster/leaveMaster");
		return modelAndView;
	}

	// @Description getAllLeaveMasterDetails() method will fetch all types of Leave Details from Database
	@GetMapping("/getAllLeaveMasterDetails")
	public @ResponseBody List<Leave> getAllLeaveMasterDetails() {
		List<Leave> list = leaveMasterService.getAllLeaveMasterDetails();
		return list;
	}

	// Method to import excel File
	@Transactional
	@PostMapping(value = { "/admin/importLeave" }, headers = ("content-type=multipart/*"))
	public @ResponseBody Object importLeave(@ModelAttribute UploadFile uploadFile) throws IOException, ParseException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee employeeModel = null;

		try {
			employeeModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = leaveMasterService.importLeaveMasterImplementation(uploadFile, employeeModel);
		return mapJson;
	}
}