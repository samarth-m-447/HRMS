package com.scii.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.scii.constants.HRMSConstants;
import com.scii.mapper.LeaveMasterMapper;
import com.scii.model.Employee;
import com.scii.model.Leave;
import com.scii.model.UploadFile;
import com.scii.service.EmployeeService;
import com.scii.service.LeaveMasterService;
import com.scii.util.CommonUtil;

@Service
public class LeaveMasterServiceImpl implements LeaveMasterService {
	
	@Autowired
	private LeaveMasterMapper leaveMasterMapper;
	
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CommonUtil commonUtil;
	
	@Override
	public List<Leave> getAllLeaveMasterDetails() {
		List<Leave> leaveMasterList = new ArrayList<>();
	   try {
		   leaveMasterList= leaveMasterMapper.getAllLeaveMasterDetails();
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	   return leaveMasterList;
	}
	
	// insert imported leaves
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> insertLeaves(ArrayList<Leave> leaveList) {
		Map<String,Object> map = new HashMap<>();
		int leaveStatus = leaveMasterMapper.insertLeaves(leaveList);
		if(leaveStatus>0) {
			map.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
			map.put(HRMSConstants.MESSAGE,"MSG42");
		}else {
			map.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
			map.put(HRMSConstants.MESSAGE,"MSG43");
		}
		return map;
	}
	
	// get all types of leaves details
	@Override
	public List<Leave> getLeaveList() {
		List<Leave> levList = new ArrayList<>();
		levList = leaveMasterMapper.getLeaveList();
		return levList;
	}

	//check leave ID already exist
	@Override
	public List<Leave> checkLeaveIDExists(Leave importLeave) {
		List<Leave> leaveIDExists = leaveMasterMapper.checkLeaveIDExists(importLeave);
		return leaveIDExists;
	}

	//update imported leaves from excel file
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> updateLeaveFromImport(List<Leave> primarylevList) {
		Map<String,Object> map = new HashMap<>();
		if(primarylevList.size()>0) {
			ArrayList<Object> updateLeaveFromImport = new ArrayList<>();
			updateLeaveFromImport.add(leaveMasterMapper.updateLeaveFromImport(primarylevList));
		}
		return map;
	}
	
	//check leave code already exist
	@Override
	public List<Leave> checkLeaveCodeExists(Leave importLeave) {
		List<Leave> leaveCodeExists = leaveMasterMapper.checkLeaveCodeExists(importLeave);
		return leaveCodeExists;
	}
	
	//check leave name already exist
	@Override
	public List<Leave> checkLeaveNameExists(Leave importLeave) {
		List<Leave> leaveNameExists = leaveMasterMapper.checkLeaveNameExists(importLeave);
		return leaveNameExists;
	}

	// Import leave master from excel file
	@Override
	public Map<String, Object> importLeaveMasterImplementation(UploadFile uploadFile, Employee employeeModel) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		String leaveCreated = "";
		String leaveCreationError = "";
		String leaveUpdated = "";
		Properties properties = commonUtil.loadPropertyFile("application.properties");
		String leaveFilePath = properties.getProperty("tempPath");
		ArrayList<Leave> leaveList = new ArrayList<>();
		ArrayList<Leave> invalidLeaveList = new ArrayList<>();
		ArrayList<Leave> leaveDataList = new ArrayList<>();
		ArrayList<Object> leaveHeadersList = new ArrayList<>();
		File folder = new File(leaveFilePath);
		List<Leave> primarylevList = new ArrayList<>();
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
					if (sheet.getRow(0) != null && sheet.getRow(0).getCell(0) != null) {
						XSSFRow firstRow = sheet.getRow(0);
						int cellCount = firstRow.getLastCellNum(); // Use getLastCellNum() to get the number of cells
						
						// iterate over cells in the header row
						 for (int i = 0; i < cellCount; i++) {
						        XSSFCell cell = firstRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						        String cellValue1 = cell.getStringCellValue();
						        leaveHeadersList.add(cellValue1);
						    }
						
						 List<String> expectedHeaders = new ArrayList<>();
							Properties leaveProperties = commonUtil.loadPropertyFile("configFile.properties");
							int leaveColSize = Integer.parseInt(leaveProperties.getProperty("totalLeaveColumns"));
							for (int p = 0; p < leaveColSize; p++) {
								expectedHeaders.add((leaveProperties.getProperty("leavecol" + p + ".name")).trim());
							}
							if (!leaveHeadersList.isEmpty() && leaveHeadersList.size() <= leaveColSize) {
								if (leaveHeadersList.get(0).toString().equalsIgnoreCase(expectedHeaders.get(0))
										&& leaveHeadersList.get(1).toString().equals(expectedHeaders.get(1))
										&& leaveHeadersList.get(2).toString().equals(expectedHeaders.get(2))
										&& leaveHeadersList.get(3).toString().equals(expectedHeaders.get(3))) {
								for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
									isValid = true;
									Leave leaveModelReq = new Leave();
									XSSFRow row = sheet.getRow(i);
									if (row != null) {
										Cell cell = row.getCell(0);
										if (cell == null
												|| cell.toString().isEmpty()
												|| cell.getCellType() == CellType.BLANK) {
											
										}else {
											for (int j = 0; j < leaveHeadersList.size(); j++) {
												Cell currentCell = row.getCell(j);
												
												if (j == 0) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														if (commonUtil.getCellValue(currentCell).matches("^[a-zA-Z0-9]+$")
																&& commonUtil.getCellValue(currentCell).length() <= 8) {
															leaveModelReq.setLeave_id(commonUtil.getCellValue(currentCell));
														} else {
															leaveModelReq.setLeave_id(commonUtil.getCellValue(currentCell));
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
														if (commonUtil.getCellValue(currentCell).matches("^[a-zA-Z0-9]+$")) {
															leaveModelReq.setLeave_type_code(commonUtil.getCellValue(currentCell));
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
														if (commonUtil.getCellValue(currentCell).matches(".+")) {
															leaveModelReq.setLeave_type_name(commonUtil.getCellValue(currentCell));
														} else {
															isValid = false;
															break;
														}
													} else {
														isValid = false;
														break;
													}
												} else if (j == 3) {
														String currentCellValue = commonUtil.getCellValue(currentCell);
														if (currentCellValue != null && (currentCellValue.equalsIgnoreCase("1")
																		|| currentCellValue.equalsIgnoreCase("0"))) {
															leaveModelReq.setActive_flg(currentCellValue);
														} else if (currentCellValue == null
																|| currentCellValue.isEmpty() 
																|| !currentCellValue.equalsIgnoreCase("1") 
																|| !currentCellValue.equalsIgnoreCase("0")) {
															leaveModelReq.setActive_flg("1");
															if (leaveModelReq.getLeave_id() != null
																	&& leaveModelReq.getLeave_type_code() != null
																	&& leaveModelReq.getLeave_type_name() != null) {
																leaveModelReq.setActive_flg("1");
															} else {
																leaveModelReq.setActive_flg("0");
															}
														} else {
															isValid = false;
															break;
														}
													}
											}
											leaveModelReq.setCreated_by(employeeModel.getEmp_id());
											leaveModelReq.setUpdated_by(employeeModel.getEmp_id());
											if (isValid) {
												leaveDataList.add(leaveModelReq);
											} else {
												invalidLeaveList.add(leaveModelReq);
											}
										}
									}
								}
								if (leaveDataList.size() > 0 || invalidLeaveList.size() > 0) {
									for (Object updateLeave : leaveDataList) {
										Leave importLeave = (Leave) updateLeave;
										List<Leave> leaveId = leaveMasterMapper.checkLeaveIDExists(importLeave);
										if (leaveId.isEmpty()) {
											leaveList.add(importLeave);
										} else {
											primarylevList.add(importLeave);
										}
									}
									if (primarylevList.size() > 0) {
										try {
											leaveMasterMapper.updateLeaveFromImport(primarylevList);
											if (leaveUpdated.equals("")) {
												for (Leave lev : primarylevList) {
													if (!leaveUpdated.contains(lev.getLeave_id())) {
														leaveUpdated += (" " + lev.getLeave_id());
													}
												}
											} else {
												for (Leave lev : primarylevList) {
													if (!leaveUpdated.contains(lev.getLeave_id())) {
														leaveUpdated += (", " + lev.getLeave_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
											updateErrorFlg = true;
										}
									}
									if (leaveList.size() > 0) {
										try {
											leaveMasterMapper.insertLeaves(leaveList);
											if (leaveCreated.equals("")) {
												for (Leave lev : leaveList) {
													if (!leaveCreated.contains(lev.getLeave_id())) {
														leaveCreated += (" " + lev.getLeave_id());
													}
												}
											} else {
												for (Leave lev : leaveList) {
													if (!leaveCreated.contains(lev.getLeave_id())) {
														leaveCreated += (", " + lev.getLeave_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
											insertErrorFlg = true;
										}
									}
									if (invalidLeaveList.size() > 0) {
										for (Leave leave : invalidLeaveList) {
											if (leave.getLeave_id() != null && !leave.getLeave_id().isEmpty()
													&& !leave.getLeave_id().equalsIgnoreCase("undefined")
													&& !leave.getLeave_id().equalsIgnoreCase("")) {
												if (!leaveCreationError.contains(leave.getLeave_id())) {
													leaveCreationError += (" " + leave.getLeave_id());
												}
											} else {
												continue;
											}
										}
									}
									String infoMsg = "";
									if (leaveCreationError.equals("") == false) {
										String leaveCreationErrorMsg = ((String) employeeService.getMessage("MSG85"))
												+ leaveCreationError;
										logger.error(leaveCreationErrorMsg);
										infoMsg += "\n" + leaveCreationErrorMsg;
									}
									if (invalidLeaveList.size() > 0) {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG80");
										mapJson.put("infoMsg", infoMsg);
									} else {
										if(insertErrorFlg==true || updateErrorFlg == true) {
											mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
											mapJson.put(HRMSConstants.MESSAGE, "MSG103");
											mapJson.put("infoMsg", "");
										}else {
											mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
											mapJson.put(HRMSConstants.MESSAGE, "MSG42");
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