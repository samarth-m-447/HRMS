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
import com.scii.model.Designation;
import com.scii.model.Employee;
import com.scii.model.UploadFile;
import com.scii.service.DesignationService;
import com.scii.service.EmployeeService;
import com.scii.util.CommonUtil;
import com.scii.constants.HRMSConstants;
import com.scii.mapper.*;

@Service
public class DesignationServiceimpl implements DesignationService{
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private DesignationMapper designationMapper;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private EmployeeService empService;
	
	//get all designation from database
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Designation> getDesignation() {
		List<Designation> list = designationMapper.getDesignation();
		return list;
	}
	
	
	//get active designations from database
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public List<Designation> getActiveDesignation() {
		List<Designation> list = designationMapper.getActiveDesignation();
		return list;
	}
	
	//delete designation
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public boolean deleteDesignation(Designation designation) {
		boolean flag= designationMapper.deleteDesignation(designation);
		return flag; 
	}
	
	//insert designation from import
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> insertDesignations(List<Designation> designationList) {
		Map<String,Object> map = new HashMap<>();
		int departmentStatus = designationMapper.insertDesignations(designationList);
		if(departmentStatus>0) {
			map.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
			map.put(HRMSConstants.MESSAGE,"MSG40");
		}else {
			map.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
			map.put(HRMSConstants.MESSAGE,"MSG41");
		}
		return map;
	}
	
	@Override
	public List<Designation> getDesgList() {
		List<Designation> desgList = new ArrayList<>();
		desgList = designationMapper.getDesgList();
		return desgList;
	}

	//check designation already exist
	@Override
	public String checkDesignationIDExists(Designation designationModel) {
		String employeeIDExists = designationMapper.checkDesignationIDExists(designationModel);
		return employeeIDExists;
	}
	
	//Update designation from import
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> updateDesignationFromImport(List<Designation> designationList) {
		Map<String,Object> map = new HashMap<>();
		if(designationList.size()>0) {
			ArrayList<Object> updateDesigDataList = new ArrayList<>();
			updateDesigDataList.add(designationMapper.updateDesignationFromImport(designationList));
		}
		return map;
	}

	//get designation names
	@Override
	public List<Designation> getDesignationName() {
		List<Designation> list = designationMapper.getDesignationName();
		return list;
	}

	//Import designation
	@Override
	public Map<String, Object> importDesignationImplementation(UploadFile uploadFile, Employee employeeModel) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		String desgCreated = "";
		String desgCreationError = "";
		String desgUpdated = "";
		Properties properties = commonUtil.loadPropertyFile("application.properties");
		String designationFilePath = properties.getProperty("tempPath");
		ArrayList<Designation> designationList = new ArrayList<>();
		ArrayList<Designation> invalidDesignationList = new ArrayList<>();
		ArrayList<Object> designationHeadersList = new ArrayList<>();
		File folder = new File(designationFilePath);
		List<Designation> desgList = new ArrayList<>();
		List<Designation> primarydesgList = new ArrayList<>();
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
							designationHeadersList.add(cellValue1);
						}

						List<String> expectedHeaders = new ArrayList<>();
						Properties desgProperties = commonUtil.loadPropertyFile("configFile.properties");
						int desgColSize = Integer.parseInt(desgProperties.getProperty("totalDesignationColumns"));
						for (int p = 0; p < desgColSize; p++) {
							expectedHeaders.add((desgProperties.getProperty("designationcol" + p + ".name")).trim());
						}
						if (designationHeadersList.size() <= desgColSize) {
							if (designationHeadersList.get(0).equals(expectedHeaders.get(0))
									&& designationHeadersList.get(1).equals(expectedHeaders.get(1))
									&& designationHeadersList.get(2).equals(expectedHeaders.get(2))) {
								for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
									isValid = true;
									Designation designationModelReq = new Designation();
									XSSFRow row = sheet.getRow(i);
									if (row != null) {
										Cell cell = row.getCell(0);
										if (cell == null
												|| cell.toString().isEmpty()
												|| cell.getCellType() == CellType.BLANK) {
											
										}else {
											for (int j = 0; j < designationHeadersList.size(); j++) {
												Cell currentCell = row.getCell(j);
												if (j == 0) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														if (commonUtil.getCellValue(currentCell)
																.matches("^[a-zA-Z0-9]+$")
																&& commonUtil.getCellValue(currentCell).length() <= 8) {
															designationModelReq
																	.setDesg_id(commonUtil.getCellValue(currentCell));
														} else {
															designationModelReq
																	.setDesg_id(commonUtil.getCellValue(currentCell));
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
															designationModelReq
																	.setDesg_name(commonUtil.getCellValue(currentCell));
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
													if (currentCellValue != null
															&& (currentCellValue.equalsIgnoreCase("1")
																	|| currentCellValue.equalsIgnoreCase("0"))) {
														if (currentCellValue.equals("0")) {
															employeeModel.setDesg_id(designationModelReq.getDesg_id());
															List<Object> desgStatusList = empService
																	.checkDesignationStatus(employeeModel);
															if (desgStatusList.size() > 0) {
																isValid = false;
																break;
															} else {
																designationModelReq.setActive_flg(currentCellValue);
															}
														} else {
															designationModelReq.setActive_flg(currentCellValue);
														}
													} else if (currentCellValue == null 
																|| currentCellValue.isEmpty()
																|| !currentCellValue.equalsIgnoreCase("1") 
																|| !currentCellValue.equalsIgnoreCase("0")) {
														designationModelReq.setActive_flg("1");
													} else {
														isValid = false;
														break;
													}
												}
											}
											designationModelReq.setCreated_by(employeeModel.getEmp_id());
											designationModelReq.setUpdated_by(employeeModel.getEmp_id());
											if (isValid) {
												designationList.add(designationModelReq);
											} else {
												invalidDesignationList.add(designationModelReq);
											}
										}
									}
								}
								if (designationList.size() > 0 || invalidDesignationList.size() > 0) {
									for (Object updateDesignation : designationList) {
										Designation importDesignation = (Designation) updateDesignation;
										String desgId = designationMapper.checkDesignationIDExists(importDesignation);
										if (desgId == null) {
											desgList.add(importDesignation);
										} else {
											primarydesgList.add(importDesignation);
										}
									}

									if (primarydesgList.size() > 0) {
										try {
											designationMapper.updateDesignationFromImport(primarydesgList);
											if (desgUpdated.isEmpty()) {
												for (Designation desg : primarydesgList) {
													if (!desgUpdated.contains(desg.getDesg_id())) {
														desgUpdated += (" " + desg.getDesg_id());
													}
												}
											} else {
												for (Designation desg : primarydesgList) {
													if (!desgUpdated.contains(desg.getDesg_id())) {
														desgUpdated += (", " + desg.getDesg_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
											updateErrorFlg = true;
										}
									}
									
									if (desgList.size() > 0) {
										try {
											designationMapper.insertDesignations(desgList);
											if (desgCreated.isEmpty()) {
												for (Designation desg : desgList) {
													if (!desgCreated.contains(desg.getDesg_id())) {
														desgCreated += (" " + desg.getDesg_id());
													}
												}
											} else {
												for (Designation desg : desgList) {
													if (!desgCreated.contains(desg.getDesg_id())) {
														desgCreated += (", " + desg.getDesg_id());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);
											insertErrorFlg = true;
										}
									}
									
									if (invalidDesignationList.size() > 0) {
										for (Designation designation : invalidDesignationList) {
											if (designation.getDesg_id() != null && !designation.getDesg_id().isEmpty()
													&& !designation.getDesg_id().equalsIgnoreCase("undefined")
													&& !designation.getDesg_id().equalsIgnoreCase("")) {
												if (!desgCreationError.contains(designation.getDesg_id())) {
													desgCreationError += (" " + designation.getDesg_id());
												}
											} else {
												continue;
											}
										}
									}
									String infoMsg = "";

									if (desgCreationError.equals("") == false) {
										String desgCreationErrorMsg = ((String) empService.getMessage("MSG81"))
												+ desgCreationError;
										logger.error(desgCreationErrorMsg);
										infoMsg += "\n" + desgCreationErrorMsg;
									}
									if (invalidDesignationList.size() > 0) {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG78");
										mapJson.put("infoMsg", infoMsg);
									} else {
										if(insertErrorFlg==true || updateErrorFlg == true) {
											mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
											mapJson.put(HRMSConstants.MESSAGE, "MSG103");
											mapJson.put("infoMsg", "");
										}else {
											mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
											mapJson.put(HRMSConstants.MESSAGE, "MSG40");
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
