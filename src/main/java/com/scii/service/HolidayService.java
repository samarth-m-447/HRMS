package com.scii.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.scii.model.Employee;
import com.scii.model.Holiday;
import com.scii.model.UploadFile;

@Service
public interface HolidayService {

	public List<Holiday> getAllHolidayMasterDetails(Holiday holidayModelReq);
	
	public Map<String,Object> insertHolidays(ArrayList<Holiday> holidayList);
	
	List<Holiday> getHolidayList();

	public String checkHolidayIDExists(Holiday importHoliday);

	public Map<String, Object> updateHolidayFromImport(List<Holiday> primaryholidayList);

	public Map<String, Object> importHolidayImplimentation(UploadFile uploadFile, Employee employeeModel) throws IOException;
}
