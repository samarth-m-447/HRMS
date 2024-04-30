/*
 * 210016
 * 
 */
package com.scii.controller;

import java.io.File;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.scii.constants.HRMSConstants;
import com.scii.model.Department;
import com.scii.model.Designation;
import com.scii.model.Employee;
import com.scii.model.Role;
import com.scii.model.UploadFile;
import com.scii.service.DepartmentService;
import com.scii.service.DesignationService;
import com.scii.service.EmployeeService;
import com.scii.util.CommonUtil;

@Controller
public class EmployeeController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DesignationService designationService;

	@Autowired
	private CommonUtil commonUtil;

	@Value("#{${employeeType.map}}")
	Map<String, String> employeeType;

	@Value("#{${employeeLevel.map}}")
	Map<String, String> employeeLevel;

	File filePathWithTemp;
	String UPLOAD_TEMP_DIR;
	String UPLOAD_DIR;
	String filePath;
	String OS;
	String fileSeparator = "/";

	// @Description showEmployeePage() method will display employee Information in
	// home page on click Employee Master in sidebar
	@GetMapping("admin/getEmpManagement")
	public ModelAndView showEmployeePage(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = null;
		try {
			userModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			modelAndView.setViewName(HRMSConstants.LOGOUT);
			return modelAndView;
		}

		modelAndView.addObject("empId", userModel.getEmp_id());
		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", employeeService.employeeImagePath(userModel).getEmp_image_path());

		modelAndView.setViewName("employee/_mEmployee");
		return modelAndView;
	}

	// On Click Add Button it will display Employee Form to Add new Employee.
	@GetMapping("/admin/getAddEmployee")
	public ModelAndView getAddEmployee() {
		ModelAndView modelAndView = new ModelAndView();

		try {
			Employee userModel = employeeService.getUserModelFromSecurityContext();
			OS = commonUtil.getOperatingSystem();
			modelAndView.addObject("empId", userModel.getEmp_id());
			modelAndView.addObject("userName", userModel.getEmp_name());
			modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
			modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
			modelAndView.addObject("profileImage", employeeService.employeeImagePath(userModel).getEmp_image_path());

			List<Department> departmentMaster = departmentService.getAllDepartment();
			List<Designation> designationMaster = designationService.getActiveDesignation();
			List<Employee> employeeMaster = employeeService.getAllEmployees();
			List<Role> roleMaster = employeeService.getAllRole();
			List<Employee> reportingManagerList = employeeService.getManagerID();
			modelAndView.addObject("isUpdate", 1);
			modelAndView.addObject("operatingSystem", OS);
			modelAndView.addObject("departmentMaster", departmentMaster);
			modelAndView.addObject("designationMaster", designationMaster);
			modelAndView.addObject("employeeMaster", employeeMaster);
			modelAndView.addObject("roleMaster", roleMaster);
			modelAndView.addObject("reportingManagerList", reportingManagerList);
			modelAndView.addObject("employeeType", employeeType);
			modelAndView.addObject("employeeLevel", employeeLevel);
			modelAndView.setViewName("employee/_addmodifyEmployee");
		} catch (Exception e) {
			logger.error(e);
			modelAndView.setViewName("login/login");
		}
		return modelAndView;
	}

	// @description saveEmployee() will save Employee Information to Database
	@PostMapping("/admin/saveEmployee")
	public @ResponseBody Object saveEmployee(@ModelAttribute("employee") Employee employee) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee userModel = null;
		try {
			userModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = employeeService.saveEmployeeImplementation(employee, userModel);
		return mapJson;
	}

	// @Description getAllEmployee() method will fetch AllEmployee Details from Database
	@GetMapping("/admin/getAllEmployee")
	public @ResponseBody List<Employee> getAllEmployee() {
		List<Employee> list = employeeService.getAllEmployees();
		return list;
	}

	// @Description getUpdateEmployeeForm() method used for Displaying employee information for modify
	@GetMapping(value = { "/admin/getUpdateEmployee" })
	public Object getUpdateEmployeeForm(@ModelAttribute("EmployeeModel") Employee employeeUpdateReq) {

		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = null;
		try {
			userModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}

		OS = commonUtil.getOperatingSystem();
		modelAndView.addObject("operatingSystem", OS);
		modelAndView.addObject("empId", userModel.getEmp_id());
		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", employeeService.employeeImagePath(userModel).getEmp_image_path());

		try {
			List<Department> departmentMaster = departmentService.getAllDepartment();
			List<Designation> designationMaster = designationService.getActiveDesignation();
			List<Employee> employeeMaster = employeeService.getAllEmployees();
			List<Role> roleMaster = employeeService.getAllRole();
			List<Employee> reportingManagerList = employeeService.getManagerID();
			modelAndView.addObject("departmentMaster", departmentMaster);
			modelAndView.addObject("designationMaster", designationMaster);
			modelAndView.addObject("employeeMaster", employeeMaster);
			modelAndView.addObject("roleMaster", roleMaster);
			modelAndView.addObject("reportingManagerList", reportingManagerList);
			modelAndView.addObject("isUpdate", 2);
			modelAndView.addObject("employeeUpdateReq", employeeUpdateReq);
			modelAndView.addObject("employeeType", employeeType);
			modelAndView.addObject("employeeLevel", employeeLevel);
			modelAndView.setViewName("employee/_addmodifyEmployee");
		} catch (Exception e) {
			logger.error(e);
		}
		return modelAndView;
	}

	// Method to Update Employee
	@PostMapping(value = { "/admin/updateEmployee" })
	public @ResponseBody Object updateEmployee(@ModelAttribute("employee") Employee employeeReq) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee employeeModel = null;
		try {
			employeeModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = employeeService.updateEmployeeImplementation(employeeReq, employeeModel);
		return mapJson;
	}

	// @Description getDeleteEmployee() method used for display confirmation of employee for delete
	@GetMapping(value = { "/admin/getDeleteEmployee" })
	public @ResponseBody Object getDeleteEmployee(@ModelAttribute("employee") Employee employeeReq) {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = null;
		try {
			userModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}

		modelAndView.addObject("empId", userModel.getEmp_id());
		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", employeeService.employeeImagePath(userModel).getEmp_image_path());
		try {
			modelAndView.setViewName("employee/_addmodifyEmployee");
			modelAndView.addObject("emp_id", employeeReq.getEmp_id());
			modelAndView.addObject("isCurrentUser", false);
			if (userModel.getEmp_id().equals(employeeReq.getEmp_id())) {
				modelAndView.addObject("isCurrentUser", true);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return modelAndView;
	}

	// Method to Delete Employee
	@PostMapping(value = { "/admin/deleteEmployee" })
	public @ResponseBody Object deleteEmployee(@ModelAttribute("employee") Employee employeeReq) {
		Map<String, Object> mapJson = new HashMap<>();
		Employee employeeModel = null;
		try {
			employeeModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = employeeService.deleteEmployeeImplementation(employeeReq, employeeModel);
		return mapJson;
	}

	// Method to import excel File
	@Transactional
	@PostMapping(value = { "/admin/importEmployee" }, headers = ("content-type=multipart/*"))
	public @ResponseBody Object importEmployee(@ModelAttribute UploadFile uploadFile) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee employeeModel = null;
		try {
			employeeModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = employeeService.importEmployeeImplementation(uploadFile, employeeModel);
		return mapJson;
	}

	// Upload files to server resource file paths
	@PostMapping(value = { "/uploadFile" }, headers = ("content-type=multipart/*"))
	public @ResponseBody Map<String, Object> uploadFile(@ModelAttribute UploadFile uploadFile) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map = employeeService.uploadImageImplementation(uploadFile);
		return map;
	}
}
