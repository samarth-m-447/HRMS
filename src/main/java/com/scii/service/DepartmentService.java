package com.scii.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.scii.model.Department;
import com.scii.model.Employee;
import com.scii.model.UploadFile;

public interface DepartmentService {

	public List<Department> getDepartments();
		
	public List<Department> getAllDepartment();
		
	public Map<String,Object> insertDepartments(List<Department> deptList);

	List<Department> getDeptList();

	public String checkDepartmentIDExists(Department departmentModel);
	
	public Map<String, Object> updateDepartmentFromImport(List<Department> primarydeptList);
	
	public List<Department> getDepartmentName();

	public Map<String, Object> ImportDepartment(UploadFile uploadFile, Employee employeeModel) throws IOException;

}
