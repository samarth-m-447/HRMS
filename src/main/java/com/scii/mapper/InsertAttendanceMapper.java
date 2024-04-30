package com.scii.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.scii.model.Attendance;
import com.scii.model.AttendanceOutgoing;

@Mapper
public interface InsertAttendanceMapper {
	public int insertAttendance(ArrayList<Object> insertAttendanceList);
	
	// this method is used to fetch correct employee data
	public List<Attendance> findAttendances(Attendance attendance);
		
	public int updateAttendance(ArrayList<Object> attendanceList);
	
	public String lateComing();
	
	public String earlyLeaving();
	
	public String getPunchInTime(Attendance attendanceModel);
	
	public String getPunchOutTime(Attendance attendanceModel);
	
	List<Attendance> findAllAttendancesByDate(String punch_date);
	
	public boolean insertOutgoingAttendanceDetails(AttendanceOutgoing attendanceOutgoing);
	
	public List<AttendanceOutgoing> getOutgoingAttendanceList(AttendanceOutgoing attendanceOutgoing);
	
	public List<AttendanceOutgoing> getOutgoingAttendance(AttendanceOutgoing outgoingDetails);
	
	public List<AttendanceOutgoing> getMyOutgoingAttendanceList(AttendanceOutgoing attendanceOutgoing);
	
	public int checkDuplicateAttendance(String punchDate);
	
	public void deleteDuplicateAttendance(ArrayList<Object> attendanceList);
	
	public boolean insertToAttendanceOutgoingDetails(AttendanceOutgoing attendanceOutgoing);

	public boolean deleteAttendanceOutgoingDetails(AttendanceOutgoing attendanceOutgoing);
	
	public boolean updateEmployeAttendance(Attendance attendanceModel);
	
	public void deleteAttendanceDetails(String punchDate);

	public String getoutgoingFrom(AttendanceOutgoing outgoingDetails);

	public String getoutgoingIn(AttendanceOutgoing outgoingDetails);

	public int getEmployeeOutgoingActive(AttendanceOutgoing outgoingDetails);

	public List<Attendance> getOutgoinDetails(Attendance attendanceModel);

	public String getexistingOutgoingDetails(AttendanceOutgoing outgoingDetails);

	public List<AttendanceOutgoing> getoutgoingInList(AttendanceOutgoing outgoingModel);

	public List<AttendanceOutgoing> getoutgoingFromList(AttendanceOutgoing outgoingModel);

	public boolean updateEmployeAttendanceHours(Attendance attendanceModel);
	
}
