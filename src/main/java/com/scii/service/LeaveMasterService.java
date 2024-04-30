package com.scii.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.scii.model.Employee;
import com.scii.model.Leave;
import com.scii.model.UploadFile;


@Service
public interface LeaveMasterService {
	
	public List<Leave> getAllLeaveMasterDetails();
	
	public Map<String,Object> insertLeaves(ArrayList<Leave> leaveList);
	
	List<Leave> getLeaveList();

	public List<Leave> checkLeaveIDExists(Leave importLeave);
	
	public List<Leave> checkLeaveCodeExists(Leave leaveCode);
	
	public List<Leave> checkLeaveNameExists(Leave leaveModelReq);

	public Map<String, Object> updateLeaveFromImport(List<Leave> primarylevList);

	public Map<String, Object> importLeaveMasterImplementation(UploadFile uploadFile, Employee employeeModel) throws IOException;
	
}
