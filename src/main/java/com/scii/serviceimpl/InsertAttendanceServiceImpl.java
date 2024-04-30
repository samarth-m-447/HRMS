package com.scii.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.scii.constants.HRMSConstants;
import com.scii.mapper.InsertAttendanceMapper;
import com.scii.model.Attendance;
import com.scii.model.AttendanceOutgoing;
import com.scii.model.Employee;
import com.scii.service.EmployeeService;
import com.scii.service.InsertAttendanceService;
import com.scii.util.CommonUtil;

@Service
public class InsertAttendanceServiceImpl implements InsertAttendanceService {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	public InsertAttendanceMapper attendanceMapper;

	@Autowired
	MessageSource messageSource;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private EmployeeService empService;

	String OS;
	String fileSepartor = "/";

	// insert attendance from excel file to database
	@Override
	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Map<String, Object> insertAttendance(ArrayList<Object> insertAttendanceList) {
		Map<String, Object> map = new HashMap<>();
		if (insertAttendanceList.size() > 0) {
			ArrayList<Object> insertAttendanceDataList = new ArrayList<>();
			insertAttendanceDataList.add(attendanceMapper.insertAttendance(insertAttendanceList));
		}
		return map;
	}

	// get all Employee's attendance details
	@Override
	public List<Attendance> findAllAttendances(Attendance attendanceModelReq) {
		Date systemPunchDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String currdate = formatter.format(systemPunchDate);

		if (attendanceModelReq.getPunch_date().isEmpty() || attendanceModelReq.getPunch_date().equalsIgnoreCase(null)
				|| attendanceModelReq.getPunch_date().equals("undefined")
				|| attendanceModelReq.getPunch_date().equals("")) {
			attendanceModelReq.setPunch_date(currdate);
		}
		List<Attendance> attendanceListAll = attendanceMapper.findAttendances(attendanceModelReq);
		return attendanceListAll;
	}

	@Override
	public String lateComing() {
		String lateComing = attendanceMapper.lateComing();
		return lateComing;
	}

	@Override
	public String earlyLeaving() {
		String earlyLeaving = attendanceMapper.earlyLeaving();
		return earlyLeaving;
	}

	// get employee punch in time from T_ATTENDANCE_DETAILS table
	@Override
	public String getPunchInTime(Attendance attendanceModel) {
		String punchInTime = attendanceMapper.getPunchInTime(attendanceModel);
		return punchInTime;
	}

	// get employee punch out time from T_ATTENDANCE_DETAILS table
	@Override
	public String getPunchOutTime(Attendance attendanceModel) {
		String punchOutTime = attendanceMapper.getPunchOutTime(attendanceModel);
		return punchOutTime;
	}

	// update employee attendance from excel file
	@Override
	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Map<String, Object> updateAttendance(ArrayList<Object> attendanceList) {
		Map<String, Object> map = new HashMap<>();
		if (attendanceList.size() > 0) {
			ArrayList<Object> updateAttendanceDataList = new ArrayList<>();
			updateAttendanceDataList.add(attendanceMapper.updateAttendance(attendanceList));
		}
		return map;
	}

	// insert outgoing details to T_ATTENDANCE_OUTGOING_DETAILS
	@Override
	public boolean addOutgoingEmpDetails(AttendanceOutgoing attendanceOutgoing) {
		// TODO Auto-generated method stub
		boolean insertFlg = attendanceMapper.insertOutgoingAttendanceDetails(attendanceOutgoing);
		return insertFlg;
	}

	// Display Outgoing Details
	@Override
	public List<AttendanceOutgoing> getOutgoingAttendanceList(AttendanceOutgoing attendanceOutgoing) {
		Date outgoingDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String currdate = formatter.format(outgoingDate);
		if (attendanceOutgoing.getPunch_date().isEmpty() || attendanceOutgoing.getPunch_date().equals("")
				|| attendanceOutgoing.getPunch_date().equalsIgnoreCase(null)
				|| attendanceOutgoing.getPunch_date().equals("undefined")) {
			attendanceOutgoing.setPunch_date(currdate);
		}
		List<AttendanceOutgoing> attendanceList = attendanceMapper.getOutgoingAttendanceList(attendanceOutgoing);
		return attendanceList;
	}

	@Override
	public List<AttendanceOutgoing> getMyOutgoingAttendanceList(AttendanceOutgoing attendanceOutgoing) {
		List<AttendanceOutgoing> attendanceList = attendanceMapper.getMyOutgoingAttendanceList(attendanceOutgoing);
		return attendanceList;
	}

	// check for duplicate punch date in attendance
	@Override
	public int checkDuplicateAttendance(String punchDate) {
		int attendanceData = attendanceMapper.checkDuplicateAttendance(punchDate);
		return attendanceData;
	}

	// delete duplicate punch date data from T_ATTENDANCE_OUTGOING_DETAILS
	public void deleteDuplicateAttendance(ArrayList<Object> insertAttendanceList) {
		attendanceMapper.deleteDuplicateAttendance(insertAttendanceList);
	}

	// To add into t_attendance_outgoing_details table
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertToAttendanceOutgoingDetails(AttendanceOutgoing attendanceOutgoing) {
		// TODO Auto-generated method stub
		boolean insertFlg = attendanceMapper.insertToAttendanceOutgoingDetails(attendanceOutgoing);
		return insertFlg;
	}

	// Delete Attendance Outgoing details
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteAttendanceOutgoingDetails(AttendanceOutgoing attendanceOutgoing) {
		// attendanceOutgoing.setActive_flg("0");
		boolean flag = attendanceMapper.deleteAttendanceOutgoingDetails(attendanceOutgoing);
		return flag;
	}

	// get employee's outgoing attendance details
	public List<AttendanceOutgoing> getOutgoingAttendance(AttendanceOutgoing outgoingDetails) {
		List<AttendanceOutgoing> OutgoingAttendance = attendanceMapper.getOutgoingAttendance(outgoingDetails);
		return OutgoingAttendance;
	}

	// update OUT_GOING_FLG in T_ATTENDANCE_DETAILS
	@Override
	public boolean updateEmployeAttendance(Attendance attendanceModel) {
		boolean attendanceOutgoingStatus = attendanceMapper.updateEmployeAttendance(attendanceModel);
		return attendanceOutgoingStatus;
	}

	// Delete from Attendance Table Data for Selected Date
	public void deleteAttendanceDetails(String currentDate) {
		attendanceMapper.deleteAttendanceDetails(currentDate);
	}

	// get outgoing from details from T_ATTENDANCE_OUTGOING_DETAILS
	public String getoutgoingFrom(AttendanceOutgoing outgoingDetails) {
		String outgoingFromExists = attendanceMapper.getoutgoingFrom(outgoingDetails);
		return outgoingFromExists;
	}

	// get outgoing in details from T_ATTENDANCE_OUTGOING_DETAILS
	public String getoutgoingIn(AttendanceOutgoing outgoingDetails) {
		String outgoingInExists = attendanceMapper.getoutgoingIn(outgoingDetails);
		return outgoingInExists;
	}

	// get employee's active outgoing flag count
	@Override
	public int getEmployeeOutgoingActive(AttendanceOutgoing outgoingDetails) {
		int EmployeeOutgoingActive = attendanceMapper.getEmployeeOutgoingActive(outgoingDetails);
		return EmployeeOutgoingActive;
	}

	// get employee's outgoing FROM and IN time
	public List<Attendance> getOutgoinDetails(Attendance attendanceModel) {
		List<Attendance> outgoingList = attendanceMapper.getOutgoinDetails(attendanceModel);
		return outgoingList;
	}

	// Get latest Outgoing In details of Employee
	public String getexistingOutgoingDetails(AttendanceOutgoing outgoingDetails) {
		String chcekexixtingOutgoingDetails = attendanceMapper.getexistingOutgoingDetails(outgoingDetails);
		return chcekexixtingOutgoingDetails;
	}

	// Get Outgoing In details of Employee
	@Override
	public List<AttendanceOutgoing> getoutgoingInList(AttendanceOutgoing outgoingModel) {
		List<AttendanceOutgoing> outgoingInList = attendanceMapper.getoutgoingInList(outgoingModel);
		return outgoingInList;
	}

	// Get Outgoing From details of Employee
	@Override
	public List<AttendanceOutgoing> getoutgoingFromList(AttendanceOutgoing outgoingModel) {
		List<AttendanceOutgoing> outgoingFromList = attendanceMapper.getoutgoingFromList(outgoingModel);
		return outgoingFromList;
	}

	// Read excel from Attendance folder and insert to t_attendance_details
	public Map<String, Object> readExcelImplementation() throws IOException {
		Map<String, Object> map = new HashMap<>();
		Properties properties = commonUtil.loadPropertyFile("application.properties");

		String attendanceFilePath = properties.getProperty("attendancePath");
		String attendanceBackupFilePath = properties.getProperty("attendanceBackupPath");
		String attendanceBackupFileFailPath = properties.getProperty("attendanceBackupPathFail");

		// ArrayList<Object> attendanceList = new ArrayList<>();
		File folder = new File(attendanceFilePath);
		File[] listOfFiles = folder.listFiles();

		if (listOfFiles != null) {
			String fileName = listOfFiles[0].getName();
			OS = commonUtil.getOperatingSystem();

			if (OS.equalsIgnoreCase("windows")) {
				fileSepartor = "\\";
			}

			String filePath = folder.toString().concat(fileSepartor).concat(fileName);

			FileInputStream attendanceFile = null;

			ArrayList<Object> insertAttendanceList = new ArrayList<>();
			// ArrayList<Object> attendanceList = new ArrayList<>();
			// ArrayList updateAttendanceList = new ArrayList<>();

			try {
				attendanceFile = new FileInputStream(new File(filePath));
				try (XSSFWorkbook workbook = new XSSFWorkbook(attendanceFile)) {
					XSSFSheet sheet = workbook.getSheetAt(0);

					for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
						Attendance attendanceModelReq = new Attendance();
						AttendanceOutgoing outgoingModelReq = new AttendanceOutgoing();
						XSSFRow row = sheet.getRow(i);
						if (row != null) {
							for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
								if (j == 0) {
									if (row.getCell(j).getRawValue().length() == 5) {
										attendanceModelReq.setEmp_id("0".concat(row.getCell(j).getRawValue()));
									} else {
										attendanceModelReq.setEmp_id(row.getCell(j).getRawValue());
									}
								} else if (j == 3) {
									attendanceModelReq.setPunch_date(row.getCell(j).getStringCellValue());
								} else if (j == 5) {
									if (row.getCell(j) != null) {
										attendanceModelReq.setPunch_in_time(row.getCell(j).getStringCellValue());
									} else {
										attendanceModelReq.setPunch_in_time("");
									}
								} else if (j == 6) {
									if (row.getCell(j).getStringCellValue() != null) {
										attendanceModelReq.setPunch_out_time(row.getCell(j).getStringCellValue());
									} else {
										attendanceModelReq.setPunch_out_time("");
									}
								}
							}
							if ((!attendanceModelReq.getPunch_in_time().equalsIgnoreCase(""))
									&& (attendanceModelReq.getPunch_out_time().equalsIgnoreCase(""))) {
								attendanceModelReq.setMgr_approved_flg("0");
								attendanceModelReq.setBackoffice_person_approved_flg("0");
							} else if ((!attendanceModelReq.getPunch_in_time().equalsIgnoreCase(""))
									&& (!attendanceModelReq.getPunch_out_time().equalsIgnoreCase(""))) {
								attendanceModelReq.getMgr_approved_flg();
								attendanceModelReq.getBackoffice_person_approved_flg();
							}
							outgoingModelReq.setEmp_id(attendanceModelReq.getEmp_id());
							outgoingModelReq.setPunch_date(attendanceModelReq.getPunch_date());
							int outgoingStatus = attendanceMapper.getEmployeeOutgoingActive(outgoingModelReq);
							if (outgoingStatus > 0) {
								attendanceModelReq.setOut_going_flg("1");
							} else {
								attendanceModelReq.setOut_going_flg("0");
							}
							// attendanceList.add(attendanceModelReq);
							insertAttendanceList.add(commonUtil.calculatePunchIn_PunchOutTime(attendanceModelReq));
						}
					}
				}
				attendanceFile.close();
				// For Live Data Enable This Code
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String currentDate = df.format(new Date());
				Attendance attendance = (Attendance) insertAttendanceList.get(0);
				String selectedDate = attendance.getPunch_date();
				if (!currentDate.equalsIgnoreCase(selectedDate)) {
					currentDate = selectedDate;
				}
				int checkDuplicateData = attendanceMapper.checkDuplicateAttendance(currentDate);
				if (checkDuplicateData > 0) {
					attendanceMapper.deleteAttendanceDetails(currentDate);
					attendanceMapper.insertAttendance(insertAttendanceList);
				} else {
					attendanceMapper.insertAttendance(insertAttendanceList);
				}
				commonUtil.copyDirectory(attendanceFilePath, attendanceBackupFilePath);
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
				attendanceFile.close();
				commonUtil.copyDirectory(attendanceFilePath, attendanceBackupFileFailPath);
				return map;
			}
		}
		return map;
	}

	// Save Outgoing details to T_ATTENDANCE_OUTGOING_DETAILS
	public Map<String, Object> saveOutgoingAttendanceImplementation(AttendanceOutgoing outgoingDetails,
			Employee userModel) {
		Map<String, Object> mapJson = new HashMap<>();
		try {
			outgoingDetails.setCreated_by(outgoingDetails.getEmp_id());
			outgoingDetails.setUpdated_by(outgoingDetails.getEmp_id());

			Attendance attendanceModel = new Attendance();
			attendanceModel.setEmp_id(outgoingDetails.getEmp_id());
			attendanceModel.setPunch_date(outgoingDetails.getPunch_date());
			String empPunchInTime = attendanceMapper.getPunchInTime(attendanceModel);
			String empPunchOutTime = attendanceMapper.getPunchOutTime(attendanceModel);

			List<Employee> empList = empService.authUser(userModel);

			Employee employeeModel = (Employee) empList.get(0);

			Properties outgoingProperties = commonUtil.loadPropertyFile("application.properties");
			String empPunchStartTime = null;
			String empPunchEndTime = null;

			if (empPunchInTime != null && !empPunchInTime.equals("") && !empPunchInTime.isEmpty()
					&& !empPunchInTime.equals("undefined")) {

				if (!employeeModel.getEmp_type().equalsIgnoreCase("Outsource")) {
					if (employeeModel.getDesg_name().equalsIgnoreCase("Allied Jobs1")) {
						empPunchStartTime = outgoingProperties.getProperty("office.outsource.starttime");
					} else if (employeeModel.getDesg_name().equalsIgnoreCase("Allied Jobs2")) {
						empPunchStartTime = outgoingProperties.getProperty("office.gardener.starttime");
					} else {
						empPunchStartTime = outgoingProperties.getProperty("office.engineer.starttime");
					}
				} else if (employeeModel.getEmp_type().equalsIgnoreCase("Outsource")) {
					if (employeeModel.getDesg_name().equalsIgnoreCase("HouseKeeping")) {
						empPunchStartTime = outgoingProperties.getProperty("office.outsource.starttime");
					} else if (employeeModel.getDesg_name().equalsIgnoreCase("Gardner")) {
						empPunchStartTime = outgoingProperties.getProperty("office.gardener.starttime");
					} else {
						empPunchStartTime = outgoingProperties.getProperty("office.engineer.starttime");
					}
				}

				if (!employeeModel.getEmp_type().equalsIgnoreCase("Outsource")) {
					if (employeeModel.getDesg_name().equalsIgnoreCase("Allied Jobs1")) {
						empPunchEndTime = outgoingProperties.getProperty("office.engineer.endtime");
					} else if (employeeModel.getDesg_name().equalsIgnoreCase("Allied Jobs2")) {
						empPunchEndTime = outgoingProperties.getProperty("office.gardener.endtime");
					} else {
						empPunchEndTime = outgoingProperties.getProperty("office.engineer.endtime");
					}
				} else if (employeeModel.getEmp_type().equalsIgnoreCase("Outsource")) {
					if (employeeModel.getDesg_name().equalsIgnoreCase("HouseKeeping")) {
						empPunchEndTime = outgoingProperties.getProperty("office.outsource.endtime");
					} else if (employeeModel.getDesg_name().equalsIgnoreCase("Gardner")) {
						empPunchEndTime = outgoingProperties.getProperty("office.gardener.endtime");
					} else {
						empPunchEndTime = outgoingProperties.getProperty("office.engineer.endtime");
					}
				}

				if (empPunchStartTime.compareTo(outgoingDetails.getOut_going_from_time()) > 0
						|| empPunchEndTime.compareTo(outgoingDetails.getOut_going_in_time()) < 0
						|| empPunchInTime.compareTo(outgoingDetails.getOut_going_from_time()) > 0) {
					mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
					mapJson.put(HRMSConstants.MESSAGE, "MSG93");
				} else {
					String outgoingFromExists = attendanceMapper.getoutgoingFrom(outgoingDetails);
					String outgoingInExists = attendanceMapper.getoutgoingIn(outgoingDetails);
					if ((outgoingFromExists == null) && (outgoingInExists == null)) {

						if ((empPunchOutTime != null && !empPunchOutTime.equals("") && !empPunchOutTime.isEmpty()
								&& !empPunchOutTime.equals("undefined"))
								&& (outgoingDetails.getOut_going_from_time().compareTo(empPunchOutTime) > 0
										|| outgoingDetails.getOut_going_in_time().compareTo(empPunchOutTime) > 0)) {
							mapJson.put(HRMSConstants.RESULT, HRMSConstants.EXISTS);
							mapJson.put(HRMSConstants.MESSAGE, "MSG91");
						} else {
							String chcekexistingOutgoingDetails = attendanceMapper
									.getexistingOutgoingDetails(outgoingDetails);
							int saveStatus = 0;
							boolean isSaveStatustrue = false;

							if (chcekexistingOutgoingDetails != null) {
								isSaveStatustrue = true;
								saveStatus = outgoingDetails.getOut_going_from_time()
										.compareTo(chcekexistingOutgoingDetails);
							}
							if (saveStatus < 0 || (saveStatus == 0 && isSaveStatustrue)) {
								mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
								mapJson.put(HRMSConstants.MESSAGE, "MSG97");
							} else {
								boolean isInsertedFlg = attendanceMapper
										.insertToAttendanceOutgoingDetails(outgoingDetails);
								if (isInsertedFlg == true) {
									attendanceModel.setOut_going_flg("1");

									boolean outgoingStatus = attendanceMapper.updateEmployeAttendance(attendanceModel);
									if (outgoingStatus == true) {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG16");
									}
								}
							}
						}
					} else if ((outgoingFromExists != null) || (outgoingInExists != null)) {
						mapJson.put(HRMSConstants.RESULT, HRMSConstants.EXISTS);
						mapJson.put(HRMSConstants.MESSAGE, "MSG74");
					}
				}
			} else {
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
				mapJson.put(HRMSConstants.MESSAGE, "MSG69");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return mapJson;
	}

	// Delete Outgoing details from T_ATTENDANCE_OUTGOING_DETAILS
	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> deleteOugoingImplementation(AttendanceOutgoing outgoingDetails, Employee userModel) {
		Map<String, Object> mapJson = new HashMap<>();
		List<AttendanceOutgoing> outgoingAttendance = new ArrayList<>();
		try {
			Attendance attendanceModel = new Attendance();
			outgoingDetails.setCreated_by(outgoingDetails.getEmp_id());
			outgoingDetails.setUpdated_by(outgoingDetails.getEmp_id());
			boolean isDeleteFlg = attendanceMapper.deleteAttendanceOutgoingDetails(outgoingDetails);
			String outgoingFromExists = attendanceMapper.getoutgoingFrom(outgoingDetails);
			String outgoingInExists = attendanceMapper.getoutgoingIn(outgoingDetails);
			if (isDeleteFlg == true) {
				outgoingAttendance = attendanceMapper.getOutgoingAttendance(outgoingDetails);
				int employeeOutgoingActive = attendanceMapper.getEmployeeOutgoingActive(outgoingDetails);
				if (employeeOutgoingActive == 0) {
					attendanceModel.setEmp_id(outgoingDetails.getEmp_id());
					attendanceModel.setPunch_date(outgoingDetails.getPunch_date());
					attendanceModel.setOut_going_flg("0");
					boolean outgoingStatus = attendanceMapper.updateEmployeAttendance(attendanceModel);
				}
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
				mapJson.put(HRMSConstants.MESSAGE, "MSG47");
			} else {
				if ((outgoingFromExists == null) || (outgoingInExists == null)) {
					mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
					mapJson.put(HRMSConstants.MESSAGE, "MSG47");
				} else {
					mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
					mapJson.put(HRMSConstants.MESSAGE, "MSG48");
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return mapJson;
	}
	
	// update OUT_GOING_FLG in T_ATTENDANCE_DETAILS
		@Override
		public boolean updateEmployeAttendanceHours(Attendance attendanceModel) {
			boolean attendanceOutgoingHoursStatus = attendanceMapper.updateEmployeAttendanceHours(attendanceModel);
			return attendanceOutgoingHoursStatus;
		}

}
