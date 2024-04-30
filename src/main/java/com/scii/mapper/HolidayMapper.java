package com.scii.mapper;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.scii.model.Holiday;

@Mapper
public interface HolidayMapper {
   
	public List<Holiday> getAllHolidayMasterDetails(Holiday holidayModelReq);
	
	public int insertHolidays(ArrayList<Holiday> holidayList);
	
	List<Holiday> getHolidayList();
	
	public String checkHolidayIDExists(Holiday importHoliday);

	public int updateHolidayFromImport(List<Holiday> primaryholidayList);

}
