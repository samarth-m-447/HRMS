package com.scii.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.scii.model.Designation;
import com.scii.model.Employee;
import com.scii.model.UploadFile;

public interface DesignationService {

	public List<Designation> getDesignation();
	
	public List<Designation> getActiveDesignation();
	
	boolean deleteDesignation(Designation designation);
	
	public  Map<String, Object> insertDesignations(List<Designation> desgList);

	List<Designation> getDesgList();

	public String checkDesignationIDExists(Designation importDesignation);

	public Map<String, Object> updateDesignationFromImport(List<Designation> designationList);

	public List<Designation> getDesignationName();

	public Map<String, Object> importDesignationImplementation(UploadFile uploadFile, Employee employeeModel) throws IOException;

}