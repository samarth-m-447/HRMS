package com.scii.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.scii.constants.HRMSConstants;
import com.scii.mapper.DepartmentMapper;
import com.scii.mapper.DesignationMapper;
import com.scii.mapper.EmployeeMapper;
import com.scii.model.Department;
import com.scii.model.Designation;
import com.scii.model.Employee;
import com.scii.model.Role;
import com.scii.model.UploadFile;
import com.scii.service.EmployeeService;
import com.scii.util.CommonUtil;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private DesignationMapper designationMapper;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Value("#{${employeeType.map}}")
	Map<String, String> employeeType;

	@Value("#{${employeeLevel.map}}")
	Map<String, String> employeeLevel;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	LocalDateTime now = LocalDateTime.now();
	File filePathWithTemp;
	String UPLOAD_TEMP_DIR;
	String UPLOAD_DIR;
	String filePath;
	String OS;
	String fileSeparator = "/";
	
	public final static long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
	
	public EmployeeServiceImpl() {
		super();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Employee getUserModelFromSecurityContext() {
		Employee userModel = new Employee();
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userModel.setEmp_id(userDetails.getUsername());
		userModel = (Employee) getUsersByEmail(userModel).get(0);
		
		return userModel;
	}

	/**
	 * method to get user details by sending email
	 */
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Object> getUsersByEmail(Employee userModel) {
		List<Object> list = employeeMapper.getUsersByEmail(userModel);
		return list;
	}
	
	//get role details from M_ROLE
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Role> getAllRole() {
		List<Role> role= new ArrayList<Role>();
		role = employeeMapper.getAllRole();
		return role;
	}

	//get all Employee ID
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String getAllEmployeeId(Employee employeeModel) {
		String employeeList = employeeMapper.getAllEmployeeId(employeeModel);
		return employeeList;
	}

	//check employee first login
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Employee checkIsFirstLogin(Employee userModel) {
		userModel = employeeMapper.checkIsFirstLogin(userModel);
		return userModel;
	}
	
	//get current system date
	@Override
	public String getSystemDate() {
		String year = employeeMapper.getSystemDate();
		return year;
	}

	// insert employee
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean insertEmployee(Employee employee) {
		return employeeMapper.insertEmployee(employee);
	}

	// get all employees
	@Override
	//@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Employee> getAllEmployees() {
		 List<Employee> emp=employeeMapper.getAllEmployees();
		 return emp;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Employee> authUser(Employee employee) {
		List<Employee> list = employeeMapper.authUser(employee);
		return list;
	}
	
	/**
	 * Function to update user reset password in M_EMPLOYE
	 * @throws NoSuchAlgorithmException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean resetUserPassword(Employee employeeModel) throws NoSuchAlgorithmException {
		employeeModel.setUpdated_by(employeeModel.getEmp_id());
		String password = commonUtil.getSecurePassword(employeeModel.getPassword());
		employeeModel.setPassword(password);
		employeeModel.setFirst_login_flg("0");
		boolean result = employeeMapper.resetUserPassword(employeeModel);
		return result;
	}
	
	// update PW_RESET_FLG 
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updatePwdResetFlag(Employee employeeModel) {
		employeeModel.setPw_reset_flg("0");
		employeeModel.setUpdated_by(employeeModel.getEmp_id());
		employeeMapper.deleteResetLinkDetails(employeeModel);
	}
	
	//get manager by role id
	@Override
	@Transactional
	public List<Employee> getManagerID() {
		List<Employee> list = employeeMapper.getManagerID();
		return list;
	}
	
	/**
	 * method to set message to flash attribute 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String getMessage(String labelId) {
		return messageSource.getMessage(labelId, null,LocaleContextHolder.getLocale());
	}

	@Override
	public List<Employee> getEmpList() {
		List<Employee> empList = new ArrayList<>();
		empList = employeeMapper.getEmpList();
		return empList;
	}
	
	//update employee
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean updateEmployee(Employee employee) {
		boolean flag = employeeMapper.updateEmployee(employee);
		return flag;
	}
	
	//delete employee
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean deleteEmployee(Employee employee) {
		boolean flag= employeeMapper.deleteEmployee(employee);
		return flag; 
	}
	
	//insert imported employee's
	public Map<String, Object> insertEmployeeFromImport(ArrayList<Employee> employeeList) {
		Map<String,Object> map = new HashMap<>();
		
		int employeeStatus = employeeMapper.insertEmployeeFromImport(employeeList);
		if(employeeStatus>0) {
			map.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
			map.put(HRMSConstants.MESSAGE,"MSG34");
		}else {
			map.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
			map.put(HRMSConstants.MESSAGE,"MSG35");
		}
		return map;
	}

	//insert employee's reporting leader ID to r_emp_leader
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean insertToEmpLeader(Employee employee) {
		return employeeMapper.insertToEmpLeader(employee);
	}
	
	//update employee's reporting leader ID to r_emp_leader
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean updateToEmpLeader(Employee employee) {
		return employeeMapper.updateToEmpLeader(employee);
	}
	
	//get employee image path 
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Employee employeeImagePath(Employee userModel) {
		Employee empImagePath = employeeMapper.getEmpImagePath(userModel);
		return empImagePath;
	}
	
	//get employee is active or not
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String getEmpActiveFlag(Employee employeeModel){
		String active_flag = employeeMapper.getEmpActiveFlag(employeeModel);
		return active_flag;
	}
	
	//check employee is first login or not
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public String getEmpFirstLoginFlag(Employee employeeModel){
		String first_login_flag = employeeMapper.getEmpFirstLoginFlag(employeeModel);
		return first_login_flag;		 
	}
	
	// Employee reset password
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean resetPassword(Employee employeeModel) throws NoSuchAlgorithmException {
		employeeModel.setUpdated_by(employeeModel.getEmp_id());
		String password = commonUtil.getSecurePassword(employeeModel.getPassword());
		employeeModel.setPassword(password);
		boolean result = employeeMapper.resetPassword(employeeModel);
		return result;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Employee> getData() {
		List<Employee> empImagePath = employeeMapper.getData();
		return empImagePath;
	}

	// Check Employee ID already exist
	@Override
	public String checkEmployeeIDExists(Employee employeeModel) {
		String employeeIDExists = employeeMapper.checkEmployeeIDExists(employeeModel);
		return employeeIDExists;
	}
	
	// get Employee Password
	@Override
	public String getEmployeePassword(Employee employeeModel) {
		String oldPassword = employeeMapper.getEmployeePassword(employeeModel);
		return oldPassword;
	}
	
	// update Employee lock flag after attempt fail 
	@Override
	public void updateEmployeeAfterAttemptsFail(Employee employeeModel) {
		employeeMapper.updateEmployeeAfterAttemptsFail(employeeModel);
		return;
	}

	// get login fail attempts of employee
	@Override
	public String getLoginAttempts(Employee employeeModel) {
		String LoginAttempts = employeeMapper.getLoginAttempts(employeeModel);
		return LoginAttempts;
	}

	// update login fail attempts of employee
	@Override
	public void updateLoginAttempts(Employee employeeModel) {
		employeeMapper.updateLoginAttempts(employeeModel);
		return;
	}
	
	// update imported employee's
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> updateEmployeeFromImport(ArrayList<Employee> employeeList) {
		Map<String,Object> map = new HashMap<>();
		if(employeeList.size()>0) {
			ArrayList<Object> updateEmployeeDataList = new ArrayList<>();
			updateEmployeeDataList.add(employeeMapper.updateEmployeeFromImport(employeeList));
		}
		return map;
	}
	
	// get total employee's count
	@Override
	public String getEmployeeCount() {
		String employeeCount = employeeMapper.getEmployeeCount();
		return employeeCount;
	}
	
	// insert 051201 employee, if no employee's are inserted
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean insertFirstEmployee(Employee employee) {
		return employeeMapper.insertFirstEmployee(employee);
	}
	
	/**
	 * method to get Decrypted values of given user data
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Employee getDecryptedDetails(Employee userModel) throws Exception {
		String decryptUserId = commonUtil.decrypt(userModel.getEmp_id());
		userModel.setEmp_id(decryptUserId);
		String decryptUserName = commonUtil.decrypt(userModel.getEmp_name());
		userModel.setEmp_name(decryptUserName);
		String decryptEmail = commonUtil.decrypt(userModel.getOffice_email());
		userModel.setOffice_email(decryptEmail);
		return userModel;
	}
	
	/**
	 * method to reset password link validation
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Employee validateResetPasswordLink(Employee userModel){		
		try {
			Employee userModelGetFlagAndTime = employeeMapper.validateResetPasswordLink(userModel);
			if(userModelGetFlagAndTime.getPw_reset_flg().equals("1") ){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				TimeZone.getTimeZone("IST");
				String currentTime = sdf.format(date);
				Date pwResetDate = sdf.parse(userModelGetFlagAndTime.getPw_update_time());
				Date sysDate = sdf.parse(currentTime);
				if((Math.abs(pwResetDate.getTime() - sysDate.getTime()) < MILLIS_PER_DAY)) {
					return userModel;
				}else {
					userModel.setEmp_id(null);
					return userModel;
				}
			}else {
				userModel.setEmp_id(null);
				return userModel;
			}
		}catch(Exception e) {
			logger.info(e.getMessage());
			logger.error(e.getStackTrace());
		}		
		return userModel;
	}

	//get role name
	@Override
	public List<Role> getRoleName() {
		List<Role> list = employeeMapper.getRoleName();
		return list;
	}

	//check designation of employee is active
	@Override
	public List<Object> checkDesignationStatus(Employee employeeModel) {
		List<Object> activeDesgList = employeeMapper.checkDesignationStatus(employeeModel);
		return activeDesgList;
	}
	
	// get reporting leader of employee
	@Override
	public String checkEmpLeader(Employee employeeModel) {
		String empLeader = employeeMapper.checkEmpLeader(employeeModel);
		return empLeader;
	}

	//Save Employee
	@Override
	public Map<String, Object> saveEmployeeImplementation(Employee employee, Employee userModel) {
		Map<String, Object> mapJson = new HashMap<>();
		String tempDirectoryPath = employee.getTempDirectoryPath();
		try {

			Properties properties = commonUtil.loadPropertyFile("application.properties");
			String imageFilePath = properties.getProperty("imagePath");
			String fileSeparator = "/";
			if (commonUtil.getOperatingSystem().equalsIgnoreCase("windows")) {
				fileSeparator = "\\";
			}

			// Save Employee
			UPLOAD_DIR = imageFilePath + fileSeparator + employee.getEmp_id();
			commonUtil.moveFile(tempDirectoryPath, UPLOAD_DIR);
			File uploadedFile = new File(UPLOAD_DIR);
			// generate the default password [Employee ID]
			String encriptPassword = commonUtil.getSecurePassword(employee.getEmp_id());
			employee.setPassword(encriptPassword);
			employee.setCreated_by(userModel.getEmp_id());
			employee.setUpdated_by(userModel.getEmp_id());
			if (!uploadedFile.exists()) {
				employee.setEmp_image_path("");
			}
			String isEmpIdExists = employeeMapper.getAllEmployeeId(employee);
			if (isEmpIdExists != null) {
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
				mapJson.put(HRMSConstants.MESSAGE, "MSG46");
			} else {
				boolean isInsertedFlg = employeeMapper.insertEmployee(employee);
				boolean isInsertEmployeeLeaderFlg = employeeMapper.insertToEmpLeader(employee);

				if (isInsertedFlg && isInsertEmployeeLeaderFlg) {
					mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
					mapJson.put(HRMSConstants.MESSAGE, "MSG22");
				} else {
					mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
					mapJson.put(HRMSConstants.MESSAGE, "MSG11");
				}
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return mapJson;
		
	}

	//Update Employee
	@Override
	public Map<String, Object> updateEmployeeImplementation(Employee employeeReq, Employee employeeModel) {
		Map<String, Object> mapJson = new HashMap<>();
		
		String tempDirectoryPath = employeeReq.getTempDirectoryPath();
		tempDirectoryPath = employeeReq.getTempDirectoryPath();
		try {
			Properties properties = commonUtil.loadPropertyFile("application.properties");
			String imageFilePath = properties.getProperty("imagePath");
			String fileSeparator = "/";
			if (commonUtil.getOperatingSystem().equalsIgnoreCase("windows")) {
				fileSeparator = "\\";
			}

			// update Employee
			UPLOAD_DIR = imageFilePath + fileSeparator + employeeReq.getEmp_id();
			File existingTopicIdPath = new File(UPLOAD_DIR);
			String currentFileName = employeeReq.getEmp_id() + "\\" + employeeReq.getFile_name();
			if (existingTopicIdPath.exists()
					&& !currentFileName.equals(employeeMapper.getEmpImagePath(employeeReq).getEmp_image_path())) {
				try {
					FileUtils.deleteDirectory(existingTopicIdPath);
				} catch (IOException e) {
					e.printStackTrace(); // Handle the exception appropriately
				}
			}
			commonUtil.moveFile(tempDirectoryPath, UPLOAD_DIR);
			if (!existingTopicIdPath.exists()) {
				employeeReq.setEmp_image_path("");
			}
			employeeReq.setUpdated_by(employeeModel.getEmp_id());
			boolean employeeFlag = employeeMapper.updateEmployee(employeeReq);
			String employeeLeader = employeeMapper.checkEmpLeader(employeeReq);
			boolean empLeaderStatus = false;
			if(employeeLeader != null) {
				employeeReq.setUpdated_by(employeeModel.getEmp_id());
				empLeaderStatus = employeeMapper.updateToEmpLeader(employeeReq);
			}else {
				employeeReq.setCreated_by(employeeModel.getEmp_id());
				employeeReq.setUpdated_by(employeeModel.getEmp_id());
				empLeaderStatus = employeeMapper.insertToEmpLeader(employeeReq);
			}
			
			if (employeeFlag == true && empLeaderStatus == true) {
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
				mapJson.put(HRMSConstants.MESSAGE, "MSG23");
			} else {
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
				mapJson.put(HRMSConstants.MESSAGE, "MSG25");
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return mapJson;
	}

	//Delete Employee
	@Override
	public Map<String, Object> deleteEmployeeImplementation(Employee employeeReq, Employee employeeModel) {
		Map<String, Object> mapJson = new HashMap<>();
		try {
			employeeReq.setUpdated_by(employeeModel.getEmp_id());
			boolean flag = employeeMapper.deleteEmployee(employeeReq);
			if (flag) {
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
				mapJson.put(HRMSConstants.MESSAGE, "MSG26");
			} else {
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
				mapJson.put(HRMSConstants.MESSAGE, "MSG27");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return mapJson;
	}

	//Import Employee's from excel file
	@Override
	public Map<String, Object> importEmployeeImplementation(UploadFile uploadFile, Employee employeeModel) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		String empCreated = "";
		String empCreationError = "";
		String empUpdated = "";
		Properties properties = commonUtil.loadPropertyFile("application.properties");
		String employeeFilePath = properties.getProperty("tempPath");
		ArrayList<Employee> employeeList = new ArrayList<>();
		ArrayList<Employee> inValidemployeeList = new ArrayList<>();
		ArrayList<Object> employeeHeadersList = new ArrayList<>();
		File folder = new File(employeeFilePath);
		List<Department> departmentMaster = departmentMapper.getDepartmentName();
		List<Designation> designationMaster = designationMapper.getDesignationName();
		List<Role> roleMaster = employeeMapper.getRoleName();
		List<String> employeeLevelList = new ArrayList<>(employeeLevel.values());
		List<String> employeeTypeList = new ArrayList<>(employeeType.values());
		ArrayList<Employee> empList = new ArrayList<>();
		ArrayList<Employee> primaryEmpList = new ArrayList<>();
		boolean isValid = false;
		String fileExtension = FilenameUtils.getExtension(uploadFile.getFile().getOriginalFilename());
		String UPLOAD_TEMP_DIR;
		if (fileExtension.equalsIgnoreCase("xlsx")) {
			try {
				String fileSeparator = "/";
				if (commonUtil.getOperatingSystem().equalsIgnoreCase("windows")) {
					fileSeparator = "\\";
				}
				UPLOAD_TEMP_DIR = folder + fileSeparator + commonUtil.getCurrentTimestamp() + fileSeparator;
				commonUtil.uploadFileToServer(UPLOAD_TEMP_DIR, uploadFile.getFile());
				filePath = UPLOAD_TEMP_DIR + fileSeparator + uploadFile.getFile().getOriginalFilename();

				try (FileInputStream file = new FileInputStream(new File(filePath));
						XSSFWorkbook workbook = new XSSFWorkbook(file)) {

					XSSFSheet sheet = workbook.getSheetAt(0);
					if (sheet.getRow(0) != null && sheet.getRow(0).getCell(0) != null) {
						XSSFRow firstRow = sheet.getRow(0);
						int cellCount = firstRow.getLastCellNum(); // Use getLastCellNum() to get the number of cells

						// iterate over cells in the header row
						for (int i = 0; i < cellCount; i++) {
							XSSFCell cell = firstRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
							String cellValue1 = cell.getStringCellValue();
							employeeHeadersList.add(cellValue1);
						}

						List<String> expectedHeaders = new ArrayList<>();
						Properties employeeProperties = commonUtil.loadPropertyFile("configFile.properties");
						int employeeColSize = Integer.parseInt(employeeProperties.getProperty("totalEmployeeColumns"));
						for (int p = 0; p < employeeColSize; p++) {
							expectedHeaders.add((employeeProperties.getProperty("employeecol" + p + ".name")).trim());
						}

						if (employeeHeadersList.size() <= employeeColSize) {
							if (employeeHeadersList.size() == expectedHeaders.size()
									&& employeeHeadersList.containsAll(expectedHeaders)) {
								for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
									isValid = true;
									Employee employeeModelReq = new Employee();
									XSSFRow row = sheet.getRow(i);
									if (row != null) {
										Cell cell = row.getCell(0);
										if (cell == null
												|| cell.toString().isEmpty()
												|| cell.getCellType() == CellType.BLANK) {
											
										}else {
											for (int j = 0; j < employeeHeadersList.size(); j++) {
												Cell currentCell = row.getCell(j);
												if (j == 0) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														if (commonUtil.getCellValue(currentCell).trim().matches("\\d+")
																&& commonUtil.getCellValue(currentCell).length() <= 6
																&& commonUtil.getCellValue(currentCell).length() >= 3) {
															employeeModelReq
																	.setEmp_id(commonUtil.getCellValue(currentCell));
														} else {
															employeeModelReq
																	.setEmp_id(commonUtil.getCellValue(currentCell));
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 1) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														if (commonUtil.getCellValue(currentCell)
																.matches("[a-zA-Z ]+")) {
															employeeModelReq
																	.setEmp_name(commonUtil.getCellValue(currentCell));
														} else {
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 2) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														if (commonUtil.getCellValue(currentCell).matches(
																"\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+")) {
															employeeModelReq.setOffice_email(commonUtil.getCellValue(currentCell));
														} else {
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 3) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														if (commonUtil.getCellValue(currentCell).matches(
																"(?i)\\w+([\\.-]?\\w+)*@[\\w-]+(\\.onmicrosoft\\.com)")) {
															employeeModelReq.setOffice_email_365(
																	commonUtil.getCellValue(currentCell));
														} else {
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 4) {

													if (commonUtil.getCellValue(currentCell) == null
															|| commonUtil.getCellValue(currentCell).isEmpty()
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("null")) {
														employeeModelReq.setContact_number("");
													} else {
														String digits = commonUtil.getCellValue(currentCell).replaceAll("\\D+", ""); // Extract digits only
														if (digits.length() == 10) {
															employeeModelReq.setContact_number(digits);
														} else {
															isValid = false;
															break;
														}
													}
												} else if (j == 5) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {

														String matchedCellValue = commonUtil.getCellValue(currentCell);

														Optional<String> matchedEmployeeType = employeeTypeList.stream()
																.filter(employeeType -> employeeType
																		.equalsIgnoreCase(matchedCellValue))
																.findFirst();
														if (matchedEmployeeType.isPresent()) {
															employeeModelReq.setEmp_type(matchedEmployeeType.get());
														} else {
															isValid = false;
															break;
														}

													} else {
														isValid = false;
														break;
													}
												} else if (j == 6) {
													if (commonUtil.getCellValue(currentCell) == null
															|| commonUtil.getCellValue(currentCell).isEmpty()
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("null")) {
														employeeModelReq.setEmp_level("");
													} else {
														String matchedCellValue = commonUtil.getCellValue(currentCell);

														Optional<String> matchedEmployeeLevel = employeeLevelList
																.stream()
																.filter(employeeLevel -> employeeLevel
																		.equalsIgnoreCase(matchedCellValue))
																.findFirst();
														if (matchedEmployeeLevel.isPresent()) {
															employeeModelReq.setEmp_level(matchedEmployeeLevel.get());
														} else {
															isValid = false;
															break;
														}

													}
												} else if (j == 7) {
													if (commonUtil.getCellValue(currentCell) == null
															|| commonUtil.getCellValue(currentCell).isEmpty()
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("NULL")) {
														employeeModelReq.setEmp_joined_date("");
													} else {
														String dateCellValue;
														try{
															dateCellValue = commonUtil.getDateCellValue(currentCell);
														}catch(Exception e) {
															logger.error(e);
															employeeModelReq.setEmp_id(employeeModelReq.getEmp_id());
															isValid = false;
															break;
														}
														if (!dateCellValue.equals("null")) {
															boolean isValidFormat = false;
															if (dateCellValue.matches("\\d{2}-\\d{2}-\\d{4}")
																	|| dateCellValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
																isValidFormat = true;
															} else {
																isValid = false;
																break;
															}
															if (isValidFormat) {
																SimpleDateFormat dateFormat = new SimpleDateFormat(
																		"yyyy-MM-dd");
																dateFormat.setLenient(false);
																try {
																	Date joinedDate = dateFormat.parse(dateCellValue);
																	// Validate year, month, and date
																	Calendar cal = Calendar.getInstance();
																	cal.setTime(joinedDate);
																	int year = cal.get(Calendar.YEAR);
																	// Perform your validation logic here
																	boolean isValidYear = year >= 2005
																			&& year <= Integer.parseInt(
																					employeeMapper.getSystemDate());
																	// Get the current month
																	String Cdate = commonUtil.getCurrentSystemDate();
																	int dateCompare = Cdate.compareTo(dateCellValue);

																	if (dateCompare >= 0) {
																		if (isValidYear) {
																			employeeModelReq
																					.setEmp_joined_date(dateCellValue);
																		} else {
																			isValid = false;
																			break;
																		}
																	} else {
																		isValid = false;
																		break;
																	}

																} catch (java.text.ParseException e) {
																	isValid = false;
																	break;
																}
															} else {
																isValid = false;
																break;
															}
														}
													}
												} else if (j == 8) {
													if (commonUtil.getCellValue(currentCell) == null
															|| commonUtil.getCellValue(currentCell).isEmpty()
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("")
															|| commonUtil.getCellValue(currentCell).equalsIgnoreCase("null")) {
														employeeModelReq.setDept_name("");
													} else {
														String deptcellValue = commonUtil.getCellValue(currentCell).toUpperCase();
														boolean isValidDepartment = departmentMaster.stream().anyMatch(department -> department.getDept_name().equalsIgnoreCase(deptcellValue));

														if (isValidDepartment) {
															employeeModelReq.setDept_name(deptcellValue);
														} else {
															isValid = false;
															break;
														}
													}
												} else if (j == 9) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														String desgName = commonUtil.getCellValue(currentCell)
																.toUpperCase();
														boolean isValidDesignation = designationMaster.stream()
																.anyMatch(designation -> designation.getDesg_name().equalsIgnoreCase(commonUtil.getCellValue(currentCell)));
														if (isValidDesignation) {
															employeeModelReq.setDesg_name(desgName);
														} else {
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 10) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														String empRole = commonUtil.getCellValue(currentCell).toUpperCase();
														boolean isValidRole = roleMaster.stream()
																.anyMatch(role -> role.getRole_name().equalsIgnoreCase(
																		commonUtil.getCellValue(currentCell)));
					
														if (isValidRole) {
															employeeModelReq.setRole_name(empRole);
														} else {
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 11) {
													String currentCellValue = commonUtil.getCellValue(currentCell);
													if (currentCellValue != null
															&& (currentCellValue.equalsIgnoreCase("1")
																	|| currentCellValue.equalsIgnoreCase("0"))) {
														if (employeeModel.getEmp_id().equals(employeeModelReq.getEmp_id())) {
															employeeModelReq.setActive_flg("1");
														} else {
															employeeModelReq.setActive_flg(currentCellValue);
														}
													} else if (currentCellValue == null 
																|| currentCellValue.isEmpty()
																|| !currentCellValue.equalsIgnoreCase("1") 
																|| !currentCellValue.equalsIgnoreCase("0")) {
														if (employeeModelReq.getEmp_id() != null
																&& employeeModelReq.getEmp_name() != null
																&& employeeModelReq.getDesg_name() != null
																&& employeeModelReq.getOffice_email() != null
																&& employeeModelReq.getEmp_type() != null
																&& employeeModelReq.getRole_name() != null
																&& employeeModelReq.getOffice_email_365() != null) {
															employeeModelReq.setActive_flg("1");
														} else {
															employeeModelReq.setActive_flg("0");
														}
													} else {
														isValid = false;
														break;
													}
												}
											}
											// generate the default password [Employee ID]
											if (employeeModelReq.getEmp_id() != null
													&& !employeeModelReq.getEmp_id().isEmpty()
													&& !employeeModelReq.getEmp_id().equalsIgnoreCase("undefined")
													&& !employeeModelReq.getEmp_id().equalsIgnoreCase("")) {
												String encriptPassword = commonUtil.getSecurePassword(employeeModelReq.getEmp_id());
												employeeModelReq.setPassword(encriptPassword);
											} else {
												employeeModelReq.setPassword("");
											}
											employeeModelReq.setCreated_by(employeeModel.getEmp_id());
											employeeModelReq.setUpdated_by(employeeModel.getEmp_id());
											if (isValid) {
												employeeList.add(employeeModelReq);
											} else {
												inValidemployeeList.add(employeeModelReq);
											}
										}
									}
								}
								if (employeeList.size() > 0 || inValidemployeeList.size() > 0) {
									for (Object updateEmployee : employeeList) {
										Employee importEmployee = (Employee) updateEmployee;
										String employeeId = employeeMapper.checkEmployeeIDExists(importEmployee);
										if (employeeId == null) {
											empList.add(importEmployee);
										} else {
											primaryEmpList.add(importEmployee);
										}
									}
									
									if (primaryEmpList.size() > 0) {
										try {
											employeeMapper.updateEmployeeFromImport(primaryEmpList);
											if (empUpdated.equals("")) {
												for (Employee employee : primaryEmpList) {
													if (!empUpdated.contains(employee.getEmp_id())) {
														empUpdated += (" " + employee.getEmp_id());
													}
												}
											} else {
												for (Employee employee : primaryEmpList) {
													if (!empUpdated.contains(employee.getEmp_id())) {
														empUpdated += (", " + employee.getEmp_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
										}
									}

									if (empList.size() > 0) {
										try {
											employeeMapper.insertEmployeeFromImport(empList);
											if (empCreated.equals("")) {
												for (Employee employee : empList) {
													if (!empCreated.contains(employee.getEmp_id())) {
														empCreated += (" " + employee.getEmp_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
										}
									}

									if (inValidemployeeList.size() > 0) {
										for (Employee employee : inValidemployeeList) {
											if (employee.getEmp_id() != null && !employee.getEmp_id().isEmpty()
													&& !employee.getEmp_id().equalsIgnoreCase("undefined")
													&& !employee.getEmp_id().equalsIgnoreCase("")) {
												if (!empCreationError.contains(employee.getEmp_id())) {
													empCreationError += (" " + employee.getEmp_id());
												}
											} else {
												continue;
											}

										}
									}

									String infoMsg = "";

									if (empCreationError.equals("") == false) {
										String empCreationErrorMsg = ((String) messageSource.getMessage("MSG63", null, LocaleContextHolder.getLocale()))
												+ empCreationError;
										logger.error(empCreationErrorMsg);
										infoMsg += "\n" + empCreationErrorMsg;
									}

									if (inValidemployeeList.size() > 0) {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG79");
										mapJson.put("infoMsg", infoMsg);
									} else {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG34");
										mapJson.put("infoMsg", "");
									}
								} else {
									mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
									mapJson.put(HRMSConstants.MESSAGE, "MSG100");
								}
							} else {
								mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
								mapJson.put(HRMSConstants.MESSAGE, "MSG33");
							}
						} else {
							mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
							mapJson.put(HRMSConstants.MESSAGE, "MSG33");
						}
					} else {
						mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
						mapJson.put(HRMSConstants.MESSAGE, "MSG33");
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return mapJson;
				} catch (IOException e) {
					e.printStackTrace();
					return mapJson;
				}

			} catch (Exception e) {
				logger.error(e);
				mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
				mapJson.put(HRMSConstants.MESSAGE, "MSG33");
			}
		} else {
			mapJson.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
			mapJson.put(HRMSConstants.MESSAGE, "MSG30");
		}
		return mapJson;
	}

	
	// Upload employee image
	@Override
	public Map<String, Object> uploadImageImplementation(UploadFile uploadFile) throws IOException {
		Map<String, Object> map = new HashMap<>();
		Properties properties = commonUtil.loadPropertyFile("application.properties");
		String imageFilePath = properties.getProperty("tempPath");
		File folder = new File(imageFilePath);
		String fileExtension = FilenameUtils.getExtension(uploadFile.getFile().getOriginalFilename());

		String resultString = null;
		resultString = uploadFile.getFile().getOriginalFilename();
		if (fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("jpg")
				|| fileExtension.equalsIgnoreCase("jpeg")) {
			try {

				if (commonUtil.getOperatingSystem().equalsIgnoreCase("windows")) {
					fileSeparator = "\\";
				}
				if (folder.exists() == false) {
					folder.mkdir();
				}
				if (UPLOAD_TEMP_DIR != null) {
					File tempDirectoryFile = new File(UPLOAD_TEMP_DIR);
					FileUtils.deleteDirectory(tempDirectoryFile);
				}
				UPLOAD_TEMP_DIR = folder + fileSeparator + commonUtil.getCurrentTimestamp() + fileSeparator;
				commonUtil.uploadFileToServer(UPLOAD_TEMP_DIR, uploadFile.getFile());

				map.put("tempDirectoryPath", UPLOAD_TEMP_DIR);
				map.put("uploadFileResult", resultString);
			} catch (Exception e) {
				// map.put("uploadFileResult","invalid");
				logger.error(e);
			}
			return map;
		} else {
			map.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
			map.put(HRMSConstants.MESSAGE, "MSG21");
		}
		return map;
	}
	
}
