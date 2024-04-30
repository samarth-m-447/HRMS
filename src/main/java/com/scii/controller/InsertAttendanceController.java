package com.scii.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.scii.constants.HRMSConstants;
import com.scii.model.Attendance;
import com.scii.model.AttendanceOutgoing;
import com.scii.model.Employee;
import com.scii.service.EmployeeService;
import com.scii.service.InsertAttendanceService;
import com.scii.util.CommonUtil;

@Controller
public class InsertAttendanceController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private InsertAttendanceService insertService;

	@Autowired
	private EmployeeService empService;

	@Autowired
	private CommonUtil commonUtil;

	String OS;
	String fileSepartor = "/";

	// Read excel from Attendance folder and insert to t_attendance_details
	@GetMapping("/readExcel")
	@Scheduled(cron = "0 32 * * * ?")
	public Object readExcel() throws ParseException, IOException {

		Map<String, Object> map = new HashMap<>();
		map = insertService.readExcelImplementation();
		return map;
	}

	// display attendance screen
	@GetMapping("/user/viewAttendance")
	public ModelAndView viewAllAttendance() {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = empService.getUserModelFromSecurityContext();

		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", empService.employeeImagePath(userModel).getEmp_image_path());
		modelAndView.setViewName("attendance/allAttendance");
		return modelAndView;
	}

	// get attendance data from database
	@GetMapping(value = "/user/getAttendance")
	public @ResponseBody Object getAllAttendance(@ModelAttribute("attendanceModel") Attendance attendanceModelReq)
			throws ParseException {

		List<Attendance> listAllattendanceRes = new ArrayList<>();
		List<Attendance> empAttendanceList = new ArrayList<>();

		try {
			listAllattendanceRes = insertService.findAllAttendances(attendanceModelReq);
			for (Attendance attendance : listAllattendanceRes) {
				empAttendanceList.add((Attendance) commonUtil.calculatePunchIn_PunchOutTime(attendance));
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return empAttendanceList;
	}

	// get outgoing data from database
	@GetMapping("/user/getOutgoingDetails")
	public @ResponseBody Object getAllEmpOutgoingDetails(
			@ModelAttribute("attendanceOutgoingModel") AttendanceOutgoing attendanceOutgoingModelReq)
			throws ParseException {
		List<AttendanceOutgoing> outgoingAttendanceList = new ArrayList<>();
		try {
			outgoingAttendanceList = insertService.getOutgoingAttendanceList(attendanceOutgoingModelReq);
		} catch (Exception e) {
			logger.error(e);
		}
		return outgoingAttendanceList;
	}

	// display outgoing screen
	@GetMapping("/user/viewOutgoing")
	public ModelAndView viewOutgoing() {
		ModelAndView modelAndView = new ModelAndView();
		Employee userModel = empService.getUserModelFromSecurityContext();

		modelAndView.addObject("userName", userModel.getEmp_name());
		modelAndView.addObject("loggedInUserRole", userModel.getRole_id());
		modelAndView.addObject("loggedInUserId", userModel.getEmp_id());
		modelAndView.addObject("profileImage", empService.employeeImagePath(userModel).getEmp_image_path());
		modelAndView.setViewName("attendance/outgoingAttendance");
		return modelAndView;
	}

	// Method to save outgoing details
	@PostMapping("/user/saveOutgoingAttendance")
	public @ResponseBody Object saveOutgoingAttendance(@RequestBody AttendanceOutgoing outgoingDetails)
			throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee userModel = null;
		try {
			userModel = empService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = insertService.saveOutgoingAttendanceImplementation(outgoingDetails, userModel);
		return mapJson;
	}

	// Method to delete outgoing details
	@PostMapping("/deleteOutgoingAttendance")
	public @ResponseBody Object deleteOutgoingAttendance(@RequestBody AttendanceOutgoing outgoingDetails)
			throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		Employee userModel = null;
		try {
			userModel = empService.getUserModelFromSecurityContext();
		} catch (Exception e) {
			logger.error(e);
			return HRMSConstants.LOGOUT;
		}
		mapJson = insertService.deleteOugoingImplementation(outgoingDetails, userModel);
		return mapJson;
	}

}
