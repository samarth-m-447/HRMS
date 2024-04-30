package com.scii.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.scii.model.Attendance;
import com.scii.model.AttendanceOutgoing;
import com.scii.model.Employee;

public interface InsertAttendanceService {
	public Map<String,Object> insertAttendance(ArrayList<Object> arrayList);
	
	public String lateComing();
	
	public String earlyLeaving();
	
	public List<Attendance> findAllAttendances(Attendance attendance);
	
	public Map<String,Object> updateAttendance(ArrayList<Object> arrayList);
		
	public String getPunchInTime(Attendance attendanceModel);
	
	public String getPunchOutTime(Attendance attendanceModel);
	
	public boolean addOutgoingEmpDetails(AttendanceOutgoing attendanceOutgoing);
	
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

	public List<AttendanceOutgoing> getoutgoingInList(AttendanceOutgoing outgoingModel);
	
	public List<AttendanceOutgoing> getoutgoingFromList(AttendanceOutgoing outgoingModel);

	public Map<String, Object> readExcelImplementation() throws IOException;

	public Map<String, Object> deleteOugoingImplementation(AttendanceOutgoing outgoingDetails, Employee userModel);

	public Map<String, Object> saveOutgoingAttendanceImplementation(AttendanceOutgoing outgoingDetails,
			Employee userModel);
	
	public boolean updateEmployeAttendanceHours(Attendance attendanceModel);
}
