package com.scii.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.scii.mapper.HolidayMapper;
import com.scii.model.Employee;
import com.scii.model.Holiday;
import com.scii.model.UploadFile;
import com.scii.service.EmployeeService;
import com.scii.service.HolidayService;
import com.scii.util.CommonUtil;

@Service
public class HolidayServiceImpl implements HolidayService{
		
	@Autowired
	private HolidayMapper holidayMapper;
	
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CommonUtil commonUtil;
	
	@Override
	public List<Holiday> getAllHolidayMasterDetails(Holiday holidayModelReq) {
		List<Holiday> holidayMasterList = new ArrayList<>();
	   try {
		   holidayMasterList= holidayMapper.getAllHolidayMasterDetails(holidayModelReq);
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
	   return holidayMasterList;
	}
	
	//insert imported holidays
	@Override
	@Transactional(value = "transactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Map<String, Object> insertHolidays(ArrayList<Holiday> holidayList) {
		Map<String, Object> map = new HashMap<>();
		int holidayStatus = holidayMapper.insertHolidays(holidayList);
		if (holidayStatus > 0) {
			map.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
			map.put(HRMSConstants.MESSAGE, "MSG44");
		} else {
			map.put(HRMSConstants.RESULT, HRMSConstants.ERROR);
			map.put(HRMSConstants.MESSAGE, "MSG45");
		}
		return map;
	}

	// get all holidays from database
	@Override
	public List<Holiday> getHolidayList() {
		List<Holiday> levList = new ArrayList<>();
		levList = holidayMapper.getHolidayList();
		return levList;
	}

	// check holiday date already exist
	@Override
	public String checkHolidayIDExists(Holiday holidayModel) {
		String holidayIDExists = holidayMapper.checkHolidayIDExists(holidayModel);
		return holidayIDExists;
	}
	
	// Update imported holiday's
	@Override
	@Transactional(value = "transactionManager", propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
	public Map<String, Object> updateHolidayFromImport(List<Holiday> holidayList) {
		Map<String,Object> map = new HashMap<>();
		if(holidayList.size()>0) {
			ArrayList<Object> updateHolidayDataList = new ArrayList<>();
			updateHolidayDataList.add(holidayMapper.updateHolidayFromImport(holidayList));
		}
		return map;
	}


	//Import Holiday from excel file
	@Override
	public Map<String, Object> importHolidayImplimentation(UploadFile uploadFile, Employee employeeModel) throws IOException {
		Map<String, Object> mapJson = new HashMap<>();
		String holidayCreated = "";
		String holidayCreationError = "";
		String holidayUpdated = "";
		Properties properties = commonUtil.loadPropertyFile("application.properties");
		String holidayFilePath = properties.getProperty("tempPath");
		ArrayList<Holiday> holidayList = new ArrayList<>();
		ArrayList<Holiday> invalidHolidayList = new ArrayList<>();
		ArrayList<Holiday> holidayDataList = new ArrayList<>();
		ArrayList<Object> holidayHeadersList = new ArrayList<>();
		File folder = new File(holidayFilePath);
		List<Holiday> primaryholidayList = new ArrayList<>();
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
						        holidayHeadersList.add(cellValue1);
						    }
						 List<String> expectedHeaders = new ArrayList<>();
							Properties holidayProperties = commonUtil.loadPropertyFile("configFile.properties");
							int holidayColSize = Integer.parseInt(holidayProperties.getProperty("totalHolidayColumns"));
							for (int p = 0; p < holidayColSize; p++) {
								expectedHeaders.add((holidayProperties.getProperty("holidaycol" + p + ".name")).trim());
							}
							if (!holidayHeadersList.isEmpty() && holidayHeadersList.size() <= holidayColSize) {
								if (holidayHeadersList.get(0).toString().equals(expectedHeaders.get(0))
										&& holidayHeadersList.get(1).toString().equals(expectedHeaders.get(1))
										&& holidayHeadersList.get(2).toString().equals(expectedHeaders.get(2))) {
								for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
									isValid = true;
									Holiday holidayModelReq = new Holiday();
									XSSFRow row = sheet.getRow(i);
									if (row != null) {
										Cell cell = row.getCell(0);
										if (cell == null
												|| cell.toString().isEmpty()
												|| cell.getCellType() == CellType.BLANK) {
											
										}else {
											for (int j = 0; j < holidayHeadersList.size(); j++) {
												Cell currentCell = row.getCell(j);
												String dateCellValue;
												if (j == 0) {
													if (commonUtil.getCellValue(currentCell) != null
															&& !commonUtil.getCellValue(currentCell).isEmpty()
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("undefined")
															&& !commonUtil.getCellValue(currentCell).equalsIgnoreCase("")) {
														try{
															dateCellValue = commonUtil.getDateCellValue(currentCell);
														}catch(Exception e) {
															logger.error(e);
															holidayModelReq.setHoliday_date(commonUtil.getCellValue(currentCell));
															isValid = false;
															break;
														}
														if (!dateCellValue.equals("null")) {
															boolean isValidFormat = false;
															if (dateCellValue.matches("\\d{2}-\\d{2}-\\d{4}")
																	|| dateCellValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
																isValidFormat = true;
															} else {
																holidayModelReq.setHoliday_date(dateCellValue);
																isValid = false;
																break;
															}
															if (isValidFormat) {
																SimpleDateFormat dateFormat = new SimpleDateFormat(
																		"yyyy-MM-dd");
																dateFormat.setLenient(false);
																try {
																	Date holidayDate = dateFormat.parse(dateCellValue);
																	// Validate year, month, and date
																	Calendar cal = Calendar.getInstance();
																	cal.setTime(holidayDate);
																	int year = cal.get(Calendar.YEAR);
																	int month = cal.get(Calendar.MONTH) + 1;
																	int day = cal.get(Calendar.DAY_OF_MONTH);

																	// Perform your validation logic here
																	boolean isValidYear = year >= 2005
																			&& year <= Integer.parseInt(
																					employeeService.getSystemDate());
																	boolean isValidMonth = month >= 1 && month <= 12;
																	boolean isValidDay = day >= 1 && day <= 31;

																	if (isValidYear && isValidMonth && isValidDay) {
																		holidayModelReq.setHoliday_date(dateCellValue);
																	} else {
																		holidayModelReq.setHoliday_date(dateCellValue);
																		isValid = false;
																		break;
																	}
																} catch (java.text.ParseException e) {
																	holidayModelReq.setHoliday_date(dateCellValue);
																	isValid = false;
																	break;
																}
															} else {
																holidayModelReq.setHoliday_date(dateCellValue);
																isValid = false;
																break;
															}
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
															holidayModelReq.setHoliday_name(commonUtil.getCellValue(currentCell));
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
															holidayModelReq.setActive_flg(currentCellValue);
														} else if (currentCellValue == null
																|| currentCellValue.isEmpty() 
																|| !currentCellValue.equalsIgnoreCase("1") 
																|| !currentCellValue.equalsIgnoreCase("0")) {
															holidayModelReq.setActive_flg("1");
														} else {
															isValid = false;
															break;
														}
													}
											}
											holidayModelReq.setCreated_by(employeeModel.getEmp_id());
											holidayModelReq.setUpdated_by(employeeModel.getEmp_id());
											if (isValid) {
												holidayDataList.add(holidayModelReq);
											} else {
												invalidHolidayList.add(holidayModelReq);
											}

										}
									}
								}
								if (holidayDataList.size() > 0 || invalidHolidayList.size() > 0) {
									for (Object updateHoliday : holidayDataList) {
										Holiday importHoliday = (Holiday) updateHoliday;
										String holidayId = holidayMapper.checkHolidayIDExists(importHoliday);
										if (holidayId == null) {
											holidayList.add(importHoliday);
										} else {
											primaryholidayList.add(importHoliday);
										}

									}
									
									if (primaryholidayList.size() > 0) {
										try {
											holidayMapper.updateHolidayFromImport(primaryholidayList);
											if (holidayUpdated.equals("")) {
												for (Holiday holi : primaryholidayList) {
													if (!holidayUpdated.contains(holi.getHoliday_date())) {
														holidayUpdated += (" " + holi.getHoliday_date());
													}
												}
											} else {
												for (Holiday holi : primaryholidayList) {
													if (!holidayUpdated.contains(holi.getHoliday_date())) {
														holidayUpdated += (", " + holi.getHoliday_date());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);

										}
									}
									if (holidayList.size() > 0) {
										try {
											holidayMapper.insertHolidays(holidayList);
											if (holidayCreated.equals("")) {
												for (Holiday holi : holidayList) {
													if (!holidayCreated.contains(holi.getHoliday_date())) {
														holidayCreated += (" " + holi.getHoliday_date());
													}
												}
											} else {
												for (Holiday holi : holidayList) {
													if (!holidayCreated.contains(holi.getHoliday_date())) {
														holidayCreated += (", " + holi.getHoliday_date());
													}
												}
											}
										} catch (Exception e) {
											logger.error(e);

										}
									}

									
									if (invalidHolidayList.size() > 0) {
										for (Holiday invalidHoliday : invalidHolidayList) {
											if (invalidHoliday.getHoliday_date() != null
													&& !invalidHoliday.getHoliday_date().isEmpty()
													&& !invalidHoliday.getHoliday_date().equalsIgnoreCase("undefined")
													&& !invalidHoliday.getHoliday_date().equalsIgnoreCase("")) {
												if (!holidayCreationError.contains(invalidHoliday.getHoliday_date())) {
													holidayCreationError += (" " + invalidHoliday.getHoliday_date());
												}
											} else {
												continue;
											}
										}
									}
									String infoMsg = "";

									if (holidayCreationError.equals("") == false) {
										String holidayCreationErrorMsg = ((String) employeeService.getMessage("MSG89"))
												+ holidayCreationError;
										logger.error(holidayCreationErrorMsg);
										infoMsg += "\n" + holidayCreationErrorMsg;
									}
									if (invalidHolidayList.size() > 0) {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG83");
										mapJson.put("infoMsg", infoMsg);
									} else {
										mapJson.put(HRMSConstants.RESULT, HRMSConstants.SUCCESS);
										mapJson.put(HRMSConstants.MESSAGE, "MSG44");
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
