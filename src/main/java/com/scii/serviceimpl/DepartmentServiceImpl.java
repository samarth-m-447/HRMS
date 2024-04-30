package com.scii.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.HashMap;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.scii.constants.HRMSConstants;
import com.scii.mapper.DepartmentMapper;
import com.scii.model.Department;
import com.scii.model.Employee;
import com.scii.model.UploadFile;
import com.scii.service.DepartmentService;
import com.scii.util.CommonUtil;

@Service
public class DepartmentServiceImpl implements DepartmentService{
    
	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	MessageSource messageSource;
	
	private Logger logger = Logger.getLogger(getClass());
	
	//get all departments from database
	@Override
	public List<Department> getAllDepartment() {
		List<Department> departmentList = new ArrayList<>();
	   try {
		   departmentList= departmentMapper.getAllDepartment();
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	   return departmentList;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Department> getDepartments() {
		List<Department> list = departmentMapper.getDepartments();
		return list;
	}
	
	//insert department from import
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> insertDepartments(List<Department> departmentList) {
		Map<String,Object> map = new HashMap<>();
		int departmentStatus = departmentMapper.insertDepartments(departmentList);
		if(departmentStatus>0) {
			map.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
			map.put(HRMSConstants.MESSAGE,"MSG38");
		}else {
			map.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
			map.put(HRMSConstants.MESSAGE,"MSG39");
		}
		return map;
	}
	
	
	@Override
	public List<Department> getDeptList() {
		List<Department> deptList = new ArrayList<>();
		deptList = departmentMapper.getDeptList();
		return deptList;
	}
	
	//check if department already exist
	@Override
	public String checkDepartmentIDExists(Department departmentModel) {
		String employeeIDExists = departmentMapper.checkDepartmentIDExists(departmentModel);
		return employeeIDExists;
	}
	
	//update department from import
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> updateDepartmentFromImport(List<Department> departmentList) {
		Map<String,Object> map = new HashMap<>();
		if(departmentList.size()>0) {
			ArrayList<Object> updateDepartmentDataList = new ArrayList<>();
			updateDepartmentDataList.add(departmentMapper.updateDepartmentFromImport(departmentList));
		}
		return map;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Department> getDepartmentName() {
		List<Department> list = departmentMapper.getDepartmentName();
		return list;
	}

	//Import department
	@Override
	public Map<String, Object> ImportDepartment(UploadFile uploadFile, Employee employeeModel) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		String deptCreated = "";
		String deptCreationError = "";
		String deptUpdated = "";
		Properties properties = commonUtil.loadPropertyFile("application.properties");
		String departmentFilePath = properties.getProperty("tempPath");
		ArrayList<Department> departmentList = new ArrayList<>();
		ArrayList<Department> invalidDepartmentList = new ArrayList<>();
		ArrayList<Object> departmentHeadersList = new ArrayList<>();
		File folder = new File(departmentFilePath);
		List<Department> deptList = new ArrayList<>();
		List<Department> primarydeptList = new ArrayList<>();
		boolean isValid = false;
		boolean insertErrorFlg =false;
		boolean updateErrorFlg =false;
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
				String filePath = UPLOAD_TEMP_DIR + fileSeparator + uploadFile.getFile().getOriginalFilename();

				try {

					FileInputStream file = new FileInputStream(new File(filePath));
					XSSFWorkbook workbook = new XSSFWorkbook(file);
					XSSFSheet sheet = workbook.getSheetAt(0);
					if (sheet.getRow(0) != null && sheet.getRow(0).getCell(0)!= null) {
						XSSFRow firstRow = sheet.getRow(0);
						int cellCount = firstRow.getLastCellNum(); // Use getLastCellNum() to get the number of cells
						
						// iterate over cells in the header row
						 for (int i = 0; i < cellCount; i++) {
						        XSSFCell cell = firstRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						        String cellValue1 = cell.getStringCellValue();
								departmentHeadersList.add(cellValue1);
						    }
						 List<String> expectedHeaders = new ArrayList<>();
						 Properties deptProperties = commonUtil.loadPropertyFile("configFile.properties");
			             int departmentColSize = Integer.parseInt(deptProperties.getProperty("totalDepartmentColumns"));
			             for(int p=0;p<departmentColSize;p++){
			               	expectedHeaders.add((deptProperties.getProperty("departmentcol"+p+".name")).trim());
			             }
						if (departmentHeadersList.size() <= departmentColSize) {
							if (departmentHeadersList.get(0).equals(expectedHeaders.get(0))
									&& departmentHeadersList.get(1).equals(expectedHeaders.get(1))
									&& departmentHeadersList.get(2).equals(expectedHeaders.get(2))) {
								for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
									isValid = true;
									Department departmentModelReq = new Department();
									XSSFRow row = sheet.getRow(i);
									if (row != null) {
										Cell cell = row.getCell(0);
										if (cell == null
												|| cell.toString().isEmpty()
												|| cell.getCellType() == CellType.BLANK) {
											
										}else {
											for (int j = 0; j < departmentHeadersList.size(); j++) {
												Cell currentCell = row.getCell(j);
												if (j == 0) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														if (commonUtil.getCellValue(currentCell).matches("^[a-zA-Z0-9]+$") && commonUtil.getCellValue(currentCell).length() <= 8) {
															departmentModelReq.setDept_id(commonUtil.getCellValue(currentCell));
														} else {
															departmentModelReq.setDept_id(commonUtil.getCellValue(currentCell));
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
														if (commonUtil.getCellValue(currentCell).matches(".+")) {
															departmentModelReq.setDept_name(commonUtil.getCellValue(currentCell));
														} else {
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 2) {
														String currentCellValue = commonUtil.getCellValue(currentCell);
														if (currentCellValue != null && (currentCellValue.equalsIgnoreCase("1")
																|| currentCellValue.equalsIgnoreCase("0"))) {
															departmentModelReq.setActive_flg(currentCellValue);
														} else if (currentCellValue == null 
																|| currentCellValue.isEmpty()
																|| !currentCellValue.equalsIgnoreCase("1") 
																|| !currentCellValue.equalsIgnoreCase("0")) {
															if (departmentModelReq.getDept_id() != null
																	&& departmentModelReq.getDept_name() != null) {
																departmentModelReq.setActive_flg("1");
															}else {
																departmentModelReq.setActive_flg("0");
															}
														} else {
															isValid = false;
															break;
														}
													}
											}
											departmentModelReq.setCreated_by(employeeModel.getEmp_id());
											departmentModelReq.setUpdated_by(employeeModel.getEmp_id());
											if (isValid) {
												departmentList.add(departmentModelReq);
											} else {
												invalidDepartmentList.add(departmentModelReq);
											}
										}
									}
								}
								if (departmentList.size() > 0 || invalidDepartmentList.size() > 0) {
									for (Object updateDepartment : departmentList) {
										Department importDepartment = (Department) updateDepartment;
										String deptId = departmentMapper.checkDepartmentIDExists(importDepartment);
										if (deptId == null) {
											deptList.add(importDepartment);
										} else {
											primarydeptList.add(importDepartment);
										}
									}
									

									if (primarydeptList.size() > 0) {
										try {
											departmentMapper.updateDepartmentFromImport(primarydeptList);
											if (deptUpdated.equals("")) {
												for (Department dept : primarydeptList) {
													if (!deptUpdated.contains(dept.getDept_id())) {
														deptUpdated += (" " + dept.getDept_id());
													}
												}
											} else {
												for (Department dept : primarydeptList) {
													if (!deptUpdated.contains(dept.getDept_id())) {
														deptUpdated += (", " + dept.getDept_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
											updateErrorFlg = true;
										}
									}
									
									if (deptList.size() > 0) {
										try {
											departmentMapper.insertDepartments(deptList);
											if (deptCreated.equals("")) {
												for (Department dept : deptList) {
													if (!deptCreated.contains(dept.getDept_id())) {
														deptCreated += (" " + dept.getDept_id());
													}
												}
											} else {
												for (Department dept : deptList) {
													if (!deptCreated.contains(dept.getDept_id())) {
														deptCreated += (", " + dept.getDept_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
											insertErrorFlg = true;
										}
									}

									if (invalidDepartmentList.size() > 0) {
										for (Department department : invalidDepartmentList) {
											if (department.getDept_id() != null
													&& !department.getDept_id().isEmpty()
													&& !department.getDept_id().equalsIgnoreCase("undefined")
													&& !department.getDept_id().equalsIgnoreCase("")) {
												if (!deptCreationError.contains(department.getDept_id())) {
													deptCreationError += (" " + department.getDept_id());
												}
											} else {
												continue;
											}
										}
									}
									String infoMsg = "";

									if (deptCreationError.equals("") == false) {
										String deptCreationErrorMsg = ((String) messageSource.getMessage("MSG77", null, LocaleContextHolder.getLocale()))
												+ deptCreationError;
										logger.error(deptCreationErrorMsg);
										infoMsg += "\n" + deptCreationErrorMsg;
									}

									if (invalidDepartmentList.size() > 0) {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG76");
										mapJson.put("infoMsg", infoMsg);
									} else {
										if(insertErrorFlg == true || updateErrorFlg == true) {
											mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
											mapJson.put(HRMSConstants.MESSAGE, "MSG103");
											mapJson.put("infoMsg", "");
										}else {
											mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
											mapJson.put(HRMSConstants.MESSAGE, "MSG38");
											mapJson.put("infoMsg", "");
										}
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
					workbook.close();
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
	
}
