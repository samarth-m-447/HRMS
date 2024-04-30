/*
 * 210016
 * 
 */
package com.scii.controller;
import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.ognl.ParseException;
import org.apache.log4j.Logger;
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
import com.scii.model.Holiday;
import com.scii.model.UploadFile;
import com.scii.service.EmployeeService;
import com.scii.service.HolidayService;

@Controller
public class HolidayController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private HolidayService holidayService;

	//@Description getHolidayMaster() method will display Holiday Information.
	@GetMapping("/admin/getHolidayMaster")
	public ModelAndView getHolidayMaster(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = employeeService.getUserModelFromSecurityContext();

		modelAndView.addObject("empId", userModel.getEmp_id());
		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_name());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", employeeService.employeeImagePath(userModel).getEmp_image_path());
		modelAndView.setViewName("holiday/holiday");
		return modelAndView;
	}

	// @Description getAllHolidayMasterDetails() method will fetch AllHoliday Details from Database
	@GetMapping("/admin/getAllHolidayMasterDetails")
	public @ResponseBody List<Holiday> getAllHolidayMasterDetails(
			@ModelAttribute("holidayModel") Holiday holidayModelReq) {
		Year currentYear = Year.now();
		int yearValue = currentYear.getValue();
		if (holidayModelReq.getYear().equalsIgnoreCase(null) || holidayModelReq.getYear().equalsIgnoreCase("")
				|| holidayModelReq.getYear().equalsIgnoreCase("undefined")) {
			holidayModelReq.setYear(Integer.toString(yearValue));
		}
		List<Holiday> holidayList = holidayService.getAllHolidayMasterDetails(holidayModelReq);
		return holidayList;
	}

	// Method to import excel File
	@Transactional
	@PostMapping(value = { "/admin/importHoliday" }, headers = ("content-type=multipart/*"))
	public @ResponseBody Object importHoliday(@ModelAttribute UploadFile uploadFile)
			throws IOException, ParseException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee employeeModel = null;
		try {
			employeeModel = employeeService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = holidayService.importHolidayImplimentation(uploadFile, employeeModel);
		return mapJson;
	}
}